package Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.e.ryedup.NavigationActivity;
import com.e.ryedup.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.RVRentCarAdapter;
import Model.ServerResponseCarRent;
import Service.Interface;
import Utils.AppUtils;
import Utils.Constants;
import Utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentRentCar extends Fragment {
    EditText edt_serch;
    TextView no_text;
    RecyclerView rv_carlist;
    RVRentCarAdapter adapter;
    AppUtils appUtils;
    List<ServerResponseCarRent.Data> carRentlist;
    SharedPreferences preference;
    String searchstring="",licences_verify="",login_id="";
    boolean ischeak=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_rent_car,container,false);
        NavigationActivity.fragmentActivity.toolbar_title.setText("Rent a car");
        NavigationActivity.fragmentActivity.img_filter.setVisibility(View.VISIBLE);
        BindUI(view);
        appUtils=new AppUtils(getActivity());
        preference = getActivity().getSharedPreferences(Constants.Shared_Pref, Context.MODE_PRIVATE);
        login_id=preference.getString("loginid","");
        licences_verify=preference.getString("is_verified","");

        rv_carlist.setLayoutManager(new LinearLayoutManager(getActivity()));
        carRentlist=new ArrayList<>();

        edt_serch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchstring = edt_serch.getText().toString().toLowerCase().trim();
                if (searchstring.length()==0){
                    hideKeyboard();
                    Hitcar_rentApi(ischeak=false);
                }else if(searchstring.length()>=3) {
                    if(appUtils.isInternetConnected()){
                        Hitcar_serchapi();
                    }else {
                        ShowMessage("Please check your Internet connection");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(appUtils.isInternetConnected()){
            Hitcar_rentApi(ischeak=true);
        }else {
            ShowMessage("Please check your Internet connection");
        }

    }

    private void Hitcar_serchapi() {
        appUtils.showDialog(getActivity());
       // appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseCarRent> call=service.searchVehicle(searchstring,Constants.accesstoken);
        call.enqueue(new Callback<ServerResponseCarRent>() {
            @Override
            public void onResponse(Call<ServerResponseCarRent> call, Response<ServerResponseCarRent> response) {

                ServerResponseCarRent data = response.body();
                appUtils.dialog.dismiss();
                carRentlist=new ArrayList<>();
                carRentlist.clear();
                    /*Log.e("succes", response.body().toString());
                    Log.e("sxzvxcvvxcucces", data.getCode().toString());*/
                if (data.getCode().equalsIgnoreCase("200")) {
                    if(data.getData().size()!=0){
                        no_text.setVisibility(View.GONE);
                        rv_carlist.setVisibility(View.VISIBLE);
                        carRentlist = data.getData();
                        adapter = new RVRentCarAdapter(getActivity(), carRentlist, licences_verify);
                        rv_carlist.setAdapter(adapter);
                    }else {
                        no_text.setVisibility(View.VISIBLE);
                        rv_carlist.setVisibility(View.GONE);
                    }
                } else {
                   // ShowMessage(data.getMessage());
                    no_text.setVisibility(View.VISIBLE);
                    rv_carlist.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ServerResponseCarRent> call, Throwable t) {
                no_text.setVisibility(View.VISIBLE);
                rv_carlist.setVisibility(View.GONE);
                ShowMessage("Try again");
                Log.e("error",t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

    private void Hitcar_rentApi(boolean ischeck) {
        appUtils.showDialog(getActivity());

        if (ischeck==true){
            appUtils.dialog.show();
        }else {

        }
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseCarRent> call=service.getVehicleList(login_id,Constants.accesstoken);
        call.enqueue(new Callback<ServerResponseCarRent>() {
            @Override
            public void onResponse(Call<ServerResponseCarRent> call, Response<ServerResponseCarRent> response) {

                    ServerResponseCarRent data = response.body();
                    appUtils.dialog.dismiss();
                    /*Log.e("succes", response.body().toString());
                    Log.e("sxzvxcvvxcucces", data.getCode().toString());*/
                    if (data.getCode().equalsIgnoreCase("200")) {
                        if (data.getData().size()!=0){
                            no_text.setVisibility(View.GONE);
                            rv_carlist.setVisibility(View.VISIBLE);
                            carRentlist = data.getData();
                            adapter = new RVRentCarAdapter(getActivity(), carRentlist,licences_verify);
                            rv_carlist.setAdapter(adapter);
                        }

                    } else {
                       // ShowMessage(data.getMessage());
                        no_text.setVisibility(View.VISIBLE);
                        rv_carlist.setVisibility(View.GONE);
                    }
                }

            @Override
            public void onFailure(Call<ServerResponseCarRent> call, Throwable t) {
                no_text.setVisibility(View.VISIBLE);
                rv_carlist.setVisibility(View.GONE);
                ShowMessage("Connection timed out");
                Log.e("error",t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

    private void BindUI(View view) {
        edt_serch=view.findViewById(R.id.edt_serch);
        rv_carlist=view.findViewById(R.id.rv_carlist);
        no_text=view.findViewById(R.id.no_text);
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void ShowMessage(String sms) {
        Toast.makeText(getActivity(),sms,Toast.LENGTH_LONG).show();
    }
}
