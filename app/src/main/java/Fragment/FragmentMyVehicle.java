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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.ryedup.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.RVMyvehicleAdapter;
import Model.ServerResponseCarRent;
import Model.ServerResponseRequestcar;
import Service.Interface;
import Utils.AppUtils;
import Utils.Constants;
import Utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMyVehicle extends Fragment implements RVMyvehicleAdapter.OnClickSectionListener {
    RecyclerView rv_myvechicle;
    TextView txt_no_data;
    RVMyvehicleAdapter adapter;
    AppUtils appUtils;
    SharedPreferences preference;
    String login_id="";
    List<ServerResponseCarRent.Data> carRentlist;
    RVMyvehicleAdapter.OnClickSectionListener mOnClickSectionListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_my_vehicle,container,false);
        BindUI(view);
        preference = getActivity().getSharedPreferences(Constants.Shared_Pref, Context.MODE_PRIVATE);
        login_id=preference.getString("loginid","");
        appUtils=new AppUtils(getActivity());
        mOnClickSectionListener = this;
        carRentlist=new ArrayList<>();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(appUtils.isInternetConnected()){
            Hitcar_Api();
        }else {
            ShowMessage("Please check your Internet connection");
        }
    }

    private void Hitcar_Api() {
        appUtils.showDialog(getActivity());
        appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseCarRent> call=service.ownerVehicleList(login_id,Constants.accesstoken);
        call.enqueue(new Callback<ServerResponseCarRent>() {
            @Override
            public void onResponse(Call<ServerResponseCarRent> call, Response<ServerResponseCarRent> response) {
                ServerResponseCarRent data = response.body();
                appUtils.dialog.dismiss();
                if(data.getCode().equalsIgnoreCase("200")){
                    carRentlist.clear();
                    if (data.getData().size()!=0){
                        txt_no_data.setVisibility(View.GONE);
                        rv_myvechicle.setVisibility(View.VISIBLE);
                        carRentlist = data.getData();
                        adapter=new RVMyvehicleAdapter(getActivity(),mOnClickSectionListener,carRentlist);
                        rv_myvechicle.setAdapter(adapter);
                    }else {
                        txt_no_data.setVisibility(View.VISIBLE);
                        rv_myvechicle.setVisibility(View.GONE);
                    }

                }else {
                    ShowMessage(data.getMessage());
                    txt_no_data.setVisibility(View.VISIBLE);
                    rv_myvechicle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ServerResponseCarRent> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error",t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

    private void BindUI(View view) {
        rv_myvechicle=view.findViewById(R.id.rv_myvechicle);
        rv_myvechicle.setLayoutManager(new LinearLayoutManager(getActivity()));
        txt_no_data=view.findViewById(R.id.txt_no_data);
    }

    private void ShowMessage(String sms) {
        Toast.makeText(getActivity(),sms,Toast.LENGTH_LONG).show();
    }


    @Override
    public void onItemClick(final String id, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        // Get the custom alert dialog view widgets reference
        TextView txt_msg =dialogView.findViewById(R.id.txtMessage);

        TextView txt_ok = dialogView.findViewById(R.id.txtOK);
        TextView txt_cancel = dialogView.findViewById(R.id.txtCancel);
        txt_msg.setText("Are you sure?");
        // create and show the alert dialog
        final AlertDialog dialog = builder.create();
        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hit delete vehicle api
                hit_deleteVehicleapi(id,position);
                dialog.dismiss();
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void hit_deleteVehicleapi(String id, final int position) {

        appUtils.showDialog(getActivity());
        appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseRequestcar> call=service.deleteVehicle(id,Constants.accesstoken);

        call.enqueue(new Callback<ServerResponseRequestcar>() {
            @Override
            public void onResponse(Call<ServerResponseRequestcar> call, Response<ServerResponseRequestcar> response) {
                ServerResponseRequestcar data= response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")){
                    ShowMessage(data.getMessage());
                    carRentlist.remove(position);
                    adapter.notifyDataSetChanged();
                }else {
                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseRequestcar> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error",t.toString());
                appUtils.dialog.dismiss();
            }
        });

    }
}
