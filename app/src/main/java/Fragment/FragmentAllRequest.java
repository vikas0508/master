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

import Adapter.RVAllRequestAceeptAdapter;
import Model.ServerResponseAcceptRequest;
import Service.Interface;
import Utils.AppUtils;
import Utils.Constants;
import Utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAllRequest extends Fragment {
    RecyclerView rv_allrequest;
    TextView txt_no_data;
    AppUtils appUtils;
    SharedPreferences preference;
    String login_id="";
    List<ServerResponseAcceptRequest.Data> requestlist;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_allrequest,container,false);
        BindUI(view);
        preference = getActivity().getSharedPreferences(Constants.Shared_Pref, Context.MODE_PRIVATE);
        login_id=preference.getString("loginid","");
        appUtils=new AppUtils(getActivity());

        requestlist=new ArrayList<>();
        if(appUtils.isInternetConnected()){
            Hit_acceptrequestSApi();
        }else {
            ShowMessage("Please check your Internet connection");
        }

        return view;
    }

    private void Hit_acceptrequestSApi() {
        appUtils.showDialog(getActivity());
        appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseAcceptRequest> call=service.getAcceptedRequests(login_id,Constants.accesstoken);

        call.enqueue(new Callback<ServerResponseAcceptRequest>() {
            @Override
            public void onResponse(Call<ServerResponseAcceptRequest> call, Response<ServerResponseAcceptRequest> response) {
                ServerResponseAcceptRequest data=response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")){
                    if(data.getData().size()!=0){
                        txt_no_data.setVisibility(View.GONE);
                        rv_allrequest.setVisibility(View.VISIBLE);
                        requestlist.clear();
                        requestlist=data.getData();
                        rv_allrequest.setAdapter(new RVAllRequestAceeptAdapter(getActivity(),requestlist));
                    }else {
                        txt_no_data.setVisibility(View.VISIBLE);
                        rv_allrequest.setVisibility(View.GONE);
                    }

                }else {
                    ShowMessage(data.getMessage());
                    txt_no_data.setVisibility(View.VISIBLE);
                    rv_allrequest.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ServerResponseAcceptRequest> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error",t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

    private void BindUI(View view) {
        rv_allrequest= view.findViewById(R.id.rv_allrequest);
        rv_allrequest.setLayoutManager(new LinearLayoutManager(getActivity()));
        txt_no_data=view.findViewById(R.id.txt_no_data);
    }

    private void ShowMessage(String sms) {
        Toast.makeText(getActivity(),sms,Toast.LENGTH_LONG).show();
    }
}
