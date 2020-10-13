package Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.ryedup.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.RVNotificationAdapter;
import Model.ServerResponseNotification;
import Service.Interface;
import Utils.AppUtils;
import Utils.Constants;
import Utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentNotification extends Fragment {
    RecyclerView rv_notiList;
    TextView txt_no_text;
    SharedPreferences preference;
    AppUtils appUtils;
    String login_id="";
    List<ServerResponseNotification.Data> noti_list;
    RVNotificationAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_notification,container,false);
        appUtils=new AppUtils(getActivity());
        preference = getActivity().getSharedPreferences(Constants.Shared_Pref, Context.MODE_PRIVATE);
        login_id=preference.getString("loginid","");

        noti_list=new ArrayList<>();

        rv_notiList=view.findViewById(R.id.rv_notiList);
        txt_no_text=view.findViewById(R.id.txt_no_text);
        rv_notiList.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (appUtils.isInternetConnected()){
            hit_notiApi();
        }else {
            ShowMessage("Please check internet connection");

        }
        return view;
    }

    private void hit_notiApi() {
        appUtils.showDialog(getActivity());
        appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseNotification> call=service.getNotifications(login_id, Constants.accesstoken);
         call.enqueue(new Callback<ServerResponseNotification>() {
             @Override
             public void onResponse(Call<ServerResponseNotification> call, Response<ServerResponseNotification> response) {
                 ServerResponseNotification data= response.body();
                 appUtils.dialog.dismiss();
                 if (data.getCode().equalsIgnoreCase("200")){
                     if (data.getData().size()!=0){
                         rv_notiList.setVisibility(View.VISIBLE);
                         txt_no_text.setVisibility(View.GONE);
                         noti_list=data.getData();
                         adapter=new RVNotificationAdapter(getContext(),noti_list);
                         rv_notiList.setAdapter(adapter);

                     }else {
                         rv_notiList.setVisibility(View.GONE);
                         txt_no_text.setVisibility(View.VISIBLE);
                     }

                 }else {
                     ShowMessage(data.getMessage());
                 }
             }

             @Override
             public void onFailure(Call<ServerResponseNotification> call, Throwable t) {
                 rv_notiList.setVisibility(View.GONE);
                 txt_no_text.setVisibility(View.VISIBLE);
                 ShowMessage("Try again");
                 Log.e("error",t.toString());
                 appUtils.dialog.dismiss();
             }
         });
    }


    private void ShowMessage(String sms) {
        Toast.makeText(getActivity(),sms,Toast.LENGTH_LONG).show();
    }
}
