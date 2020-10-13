package Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.e.ryedup.NavigationActivity;
import com.e.ryedup.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import Model.ServerResponseHistoryDetils;
import Service.Interface;
import Utils.AppUtils;
import Utils.Constants;
import Utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHistoryDetails extends Fragment {
    TextView txt_type,txt_price,txt_date,txt_source,txt_destination,txt_total,txt_discount,txt_paid;
    String id="",login_id="",user_type="";
    SharedPreferences preference;
    AppUtils appUtils;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view=inflater.inflate(R.layout.fragment_history_details,container,false);
         BindUI(view);
         NavigationActivity.fragmentActivity.toolbar_title.setText("Past trip details");
         NavigationActivity.fragmentActivity.img_filter.setVisibility(View.GONE);
         preference = getActivity().getSharedPreferences(Constants.Shared_Pref, Context.MODE_PRIVATE);
         login_id=preference.getString("loginid","");
         id=getArguments().getString("id");
         appUtils=new AppUtils(getActivity());

        if (appUtils.isInternetConnected()){
            Hit_HistroyDetailsApi();
        }else {
            ShowMessage("Please check your Internet connection");
        }
         return view;
    }

    private void Hit_HistroyDetailsApi() {
        appUtils.showDialog(getActivity());
        appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseHistoryDetils> call= service.tripDetail(id,Constants.accesstoken);

        call.enqueue(new Callback<ServerResponseHistoryDetils>() {
            @Override
            public void onResponse(Call<ServerResponseHistoryDetils> call, Response<ServerResponseHistoryDetils> response) {
                ServerResponseHistoryDetils data=response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")){
                    String type=data.getData().getReservation_type();
                    if (type!=null && type.length()>0){
                        txt_type.setText("Booking type : "+type.substring(0, 1).toUpperCase() + type.substring(1,type.length()));
                    }
                    txt_price.setText("$ "+data.getData().getAmount());
                    txt_source.setText(data.getData().getFrom_location());
                    txt_destination.setText(data.getData().getTo_location());
                    txt_total.setText(data.getData().getAmount());
                    txt_discount.setText(data.getData().getFare());
                    txt_paid.setText(data.getData().getRydeUp_Fee());
////////////// Start date converter//////////////////////////
                    String DateFromDb = data.getData().getReq_date_updated();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//set format of date you receiving from db
                    sdf.setTimeZone(TimeZone.getDefault());
                    Date date = null;
                    try {
                        date = (Date) sdf.parse(DateFromDb);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat newDate = new SimpleDateFormat("dd MMM yyyy hh:mm a");//set format of new date
                    // System.out.println(newDate.format(date));
                    String ActDate = newDate.format(date);// here is your new date !
                    txt_date.setText(ActDate);
                }else {
                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseHistoryDetils> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error",t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

    private void BindUI(View view) {
        txt_type=view.findViewById(R.id.txt_type);
        txt_price=view.findViewById(R.id.txt_price);
        txt_date=view.findViewById(R.id.txt_date);
        txt_source=view.findViewById(R.id.txt_source);
        txt_destination=view.findViewById(R.id.txt_destination);
        txt_total=view.findViewById(R.id.txt_total);
        txt_discount=view.findViewById(R.id.txt_discount);
        txt_paid=view.findViewById(R.id.txt_paid);

    }
    private void ShowMessage(String sms) {
        Toast.makeText(getActivity(),sms,Toast.LENGTH_LONG).show();
    }
}
