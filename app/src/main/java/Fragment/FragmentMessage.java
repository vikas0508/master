package Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e.ryedup.HomeActivity;
import com.e.ryedup.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import Adapter.RVMessageAdapter;
import Model.UserModel;
import Utils.AppUtils;
import Utils.Constants;

public class FragmentMessage extends Fragment {
    TextView txt_no_list;
    RecyclerView rv_messagelist;
    RVMessageAdapter adapter;
    private ArrayList<UserModel> userfcmList;
    DatabaseReference database;
    SharedPreferences preference;
    String login_id="",token="", request_iid="";
    KProgressHUD dialog;
    AppUtils appUtils;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_message,container,false);

        preference =getActivity().getSharedPreferences(Constants.Shared_Pref, Context.MODE_PRIVATE);
        login_id=preference.getString("loginid","");
        request_iid= HomeActivity.homeActivity.request_id;
        userfcmList= new ArrayList<>();
        txt_no_list=view.findViewById(R.id.txt_no_list);
        rv_messagelist=view.findViewById(R.id.rv_messagelist);
        rv_messagelist.setLayoutManager(new LinearLayoutManager(getActivity()));
        /*adapter=new RVMessageAdapter(getActivity());
        rv_messagelist.setAdapter(adapter);*/

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getFrendlist();

    }

    private void getFrendlist() {
        database = FirebaseDatabase.getInstance().getReference().child("Friends_list");
        appUtils=new AppUtils(getActivity());
        appUtils.showDialog(getActivity());
        appUtils.dialog.show();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userfcmList.clear();
                appUtils.dialog.dismiss();
                Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.getValue();

                if (objectMap == null) {
                    txt_no_list.setVisibility(View.VISIBLE);
                    rv_messagelist.setVisibility(View.GONE);
                }else {
                    txt_no_list.setVisibility(View.GONE);
                    rv_messagelist.setVisibility(View.VISIBLE);
                    for (Object obj : objectMap.values()) {
                        if (obj instanceof Map) {
                            String name = (String) ((Map) obj).get("name");
                            String image = (String) ((Map) obj).get("image");
                            String user_id = (String) ((Map) obj).get("user_id");
                            String token = (String) ((Map) obj).get("device_id");
                            String message = (String) ((Map) obj).get("message");
                            String request_id = (String) ((Map) obj).get("request_id");
                            String updated_at = (String) ((Map) obj).get("updated_at");

                            String OurDate = "";
                            try {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                                Date value = formatter.parse(updated_at);

                                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //this format changeable
                                dateFormatter.setTimeZone(TimeZone.getDefault());
                                OurDate = dateFormatter.format(value);

                                //Log.d("OurDate", OurDate);
                            } catch (Exception e) {
                                OurDate = "0000-00-00 00:00";
                            }
                            if (request_id.equalsIgnoreCase(request_iid)){

                                UserModel userModel=new UserModel();
                                userModel.setName(name);
                                userModel.setImage(image);
                                userModel.setUser_id(user_id);
                                userModel.setMessage(message);
                                userModel.setRequest_id(request_id);
                                userModel.setDevice_id(token);
                                userModel.setUpdated_at(OurDate);
                                if (user_id.equalsIgnoreCase(login_id)){

                                }else {
                                    userfcmList.add(userModel);
                                }
                            }else {
                                txt_no_list.setVisibility(View.VISIBLE);
                                rv_messagelist.setVisibility(View.GONE);
                            }




                        }else {

                        }
                    }
                    class StringDateComparator implements Comparator<UserModel> {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        public int compare(UserModel lhs, UserModel rhs) {

                            try {
                                return dateFormat.parse(rhs.getUpdated_at()).compareTo(dateFormat.parse(lhs.getUpdated_at()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return 0;
                        }
                    }
                    Collections.sort(userfcmList, new StringDateComparator());
                    Collections.reverseOrder();
                    rv_messagelist.setAdapter(new RVMessageAdapter(getActivity(),userfcmList));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                appUtils.dialog.dismiss();
            }
        });

    }
}
