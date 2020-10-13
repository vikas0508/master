package Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.e.ryedup.NavigationActivity;
import com.e.ryedup.R;

import java.util.List;

import Adapter.RVHistoryAdapter;
import Model.ServerResponseHistory;
import Service.Interface;
import Utils.AppUtils;
import Utils.Constants;
import Utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHistory extends Fragment implements View.OnClickListener {
    RecyclerView rv_historylist;
    RVHistoryAdapter adapter;
    LinearLayout ll_layout,ll_share,ll_rental;
    TextView txt_share,txt_rental,txt_no_data;
    String user_type="",Fiter_value="",login_id="";
    SharedPreferences preference;
    AppUtils appUtils;
    List<ServerResponseHistory.Data> historylist;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_history,container,false);
        BindUI(view);
        NavigationActivity.fragmentActivity.toolbar_title.setText("History");
        NavigationActivity.fragmentActivity.img_filter.setVisibility(View.GONE);

        appUtils=new AppUtils(getActivity());
        preference = getActivity().getSharedPreferences(Constants.Shared_Pref, Context.MODE_PRIVATE);
        user_type=preference.getString("user_type","");
        login_id=preference.getString("loginid","");

        if (user_type.equalsIgnoreCase("3")){
            ll_layout.setVisibility(View.GONE);
            Fiter_value="rental";
            if (appUtils.isInternetConnected()){
                Hit_OwnerHistApi();
            }else {
                ShowMessage("Please check your Internet connection");
            }
        }else {
            ll_layout.setVisibility(View.VISIBLE);
            ////////////////first time select default layout
            Fiter_value="rydeup";
            ll_share.setBackgroundResource(R.drawable.left_view);
            ll_rental.setBackgroundColor(Color.TRANSPARENT);
            txt_share.setTextColor(Color.WHITE);
            txt_rental.setTextColor(Color.BLACK);

            if (appUtils.isInternetConnected()){
                Hit_HistroyApi();
            }else {
                ShowMessage("Please check your Internet connection");
            }
        }

       /* try {
            user_type= SignUpActivity.signUpActivity.user_type;
            ll_layout.setVisibility(View.GONE);
            Log.e("aaaa",user_type);
        }catch (Exception e){

        }
*/

        rv_historylist.setLayoutManager(new LinearLayoutManager(getActivity()));

        ll_share.setOnClickListener(this);
        ll_rental.setOnClickListener(this);

        return view;
    }

    private void BindUI(View view) {
        rv_historylist=view.findViewById(R.id.rv_historylist);
        ll_layout=view.findViewById(R.id.ll_layout);
        ll_share=view.findViewById(R.id.ll_share);
        ll_rental=view.findViewById(R.id.ll_rental);
        txt_share=view.findViewById(R.id.txt_share);
        txt_rental=view.findViewById(R.id.txt_rental);
        txt_no_data=view.findViewById(R.id.txt_no_data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_share:
                Fiter_value="rydeup";
                ll_share.setBackgroundResource(R.drawable.left_view);
                ll_rental.setBackgroundColor(Color.TRANSPARENT);
                txt_share.setTextColor(Color.WHITE);
                txt_rental.setTextColor(Color.BLACK);
                if (appUtils.isInternetConnected()){
                    Hit_HistroyApi();
                }else {
                    ShowMessage("Please check your Internet connection");
                }
                break;
            case R.id.ll_rental:
                Fiter_value="rental";
                ll_rental.setBackgroundResource(R.drawable.right_view);
                ll_share.setBackgroundColor(Color.TRANSPARENT);
                txt_share.setTextColor(Color.BLACK);
                txt_rental.setTextColor(Color.WHITE);
                if (appUtils.isInternetConnected()){
                    Hit_HistroyApi();
                }else {
                    ShowMessage("Please check your Internet connection");
                }
                break;
        }
    }
    private void Hit_HistroyApi() {
        appUtils.showDialog(getActivity());
        appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseHistory> call=service.history(login_id,Fiter_value,Constants.accesstoken);

        call.enqueue(new Callback<ServerResponseHistory>() {
            @Override
            public void onResponse(Call<ServerResponseHistory> call, Response<ServerResponseHistory> response) {
                ServerResponseHistory data= response.body();
                appUtils.dialog.dismiss();

                if(data.getCode().equalsIgnoreCase("200")){
                    if (data.getData().size()!=0){
                        rv_historylist.setVisibility(View.VISIBLE);
                        txt_no_data.setVisibility(View.GONE);
                        historylist=data.getData();
                        adapter=new RVHistoryAdapter(getActivity(),historylist);
                        rv_historylist.setAdapter(adapter);
                    }else {
                        rv_historylist.setVisibility(View.GONE);
                        txt_no_data.setVisibility(View.VISIBLE);
                    }

                }else {
                    rv_historylist.setVisibility(View.GONE);
                    txt_no_data.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ServerResponseHistory> call, Throwable t) {
                rv_historylist.setVisibility(View.GONE);
                txt_no_data.setVisibility(View.VISIBLE);
                ShowMessage("Try again");
                Log.e("error",t.toString());
                appUtils.dialog.dismiss();

            }
        });
    }
/////////////Owner panel history api
    private void Hit_OwnerHistApi() {
        appUtils.showDialog(getActivity());
        appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseHistory> call=service.ownerRentalHistory(login_id,Fiter_value,Constants.accesstoken);
        call.enqueue(new Callback<ServerResponseHistory>() {
            @Override
            public void onResponse(Call<ServerResponseHistory> call, Response<ServerResponseHistory> response) {
                ServerResponseHistory data= response.body();
                appUtils.dialog.dismiss();
                if(data.getCode().equalsIgnoreCase("200")){
                    if (data.getData().size()!=0){
                        rv_historylist.setVisibility(View.VISIBLE);
                        txt_no_data.setVisibility(View.GONE);
                        historylist=data.getData();
                        adapter=new RVHistoryAdapter(getActivity(),historylist);
                        rv_historylist.setAdapter(adapter);
                    }else {
                        rv_historylist.setVisibility(View.GONE);
                        txt_no_data.setVisibility(View.VISIBLE);
                    }

                }else {
                    rv_historylist.setVisibility(View.GONE);
                    txt_no_data.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ServerResponseHistory> call, Throwable t) {
                rv_historylist.setVisibility(View.GONE);
                txt_no_data.setVisibility(View.VISIBLE);
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
