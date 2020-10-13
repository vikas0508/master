package Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.e.ryedup.NavigationActivity;
import com.e.ryedup.R;

import java.util.ArrayList;

import Adapter.CustomSpinnerAdapter;
import Model.ServerResponseCarRent;
import Service.Interface;
import Utils.AppUtils;
import Utils.Constants;
import Utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCarFilter extends Fragment implements View.OnClickListener {
    Button btn_hours,btn_daily,btn_weekly,btn_submit;
    EditText edt_min_price,edt_max_price;
    TextView txt_spin;
    Spinner type_spiner;
    String rent_type="",min_price="",max_price="",select_car_type="";
    CustomSpinnerAdapter spinnerAdapter;
    AppUtils appUtils;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_car_filter,container,false);

        BindUI(view);
        appUtils=new AppUtils(getActivity());

        final ArrayList<String> Typelist = new ArrayList<>();
        Typelist.add(0,"Suv");
       // Typelist.add(1,"Truck");
        Typelist.add(1,"Compact");
        Typelist.add(2,"Mid-size");
    //    Typelist.add(4,"Van");
        Typelist.add(3,"Luxury");
        Typelist.add(4,"Premium");


        spinnerAdapter = new CustomSpinnerAdapter(getActivity(), Typelist);
        type_spiner.setAdapter(spinnerAdapter);

        type_spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                select_car_type = Typelist.get(i);
                txt_spin.setText(select_car_type);
                //idea_value = ideaTypelist.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                select_car_type = Typelist.get(0);
                txt_spin.setText(select_car_type);
               // idea_value = ideaTypelist.get(0).getId();
            }
        });


        btn_submit.setOnClickListener(this);
        btn_hours.setOnClickListener(this);
        btn_daily.setOnClickListener(this);
        btn_weekly.setOnClickListener(this);
        txt_spin.setOnClickListener(this);

        return view;
    }

    private void BindUI(View view) {
        btn_hours=view.findViewById(R.id.btn_hours);
        btn_daily=view.findViewById(R.id.btn_daily);
        btn_weekly=view.findViewById(R.id.btn_weekly);
        btn_submit=view.findViewById(R.id.btn_submit);
        edt_min_price=view.findViewById(R.id.edt_min_price);
        edt_max_price=view.findViewById(R.id.edt_max_price);

        txt_spin=view.findViewById(R.id.txt_spin);
        type_spiner=view.findViewById(R.id.type_spiner);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                if (appUtils.isInternetConnected()){
                    Hit_filterApi();
                }else {
                    ShowMessage("Please check your Internet connection");
                }
                break;
            case R.id.btn_hours:
                rent_type="hourly";
                btn_hours.setBackgroundResource(R.drawable.rounded_button);
                btn_daily.setBackgroundResource(R.drawable.rounded_grey_button);
                btn_weekly.setBackgroundResource(R.drawable.rounded_grey_button);
                btn_hours.setHintTextColor(Color.parseColor("#FFFFFF"));
                btn_daily.setHintTextColor(Color.parseColor("#2A2A2A"));
                btn_weekly.setHintTextColor(Color.parseColor("#2A2A2A"));
                break;
            case R.id.btn_daily:
                rent_type="daily";
                btn_hours.setBackgroundResource(R.drawable.rounded_grey_button);
                btn_daily.setBackgroundResource(R.drawable.rounded_button);
                btn_weekly.setBackgroundResource(R.drawable.rounded_grey_button);
                btn_daily.setHintTextColor(Color.parseColor("#FFFFFF"));
                btn_hours.setHintTextColor(Color.parseColor("#2A2A2A"));
                btn_weekly.setHintTextColor(Color.parseColor("#2A2A2A"));
                break;

            case R.id.btn_weekly:
                rent_type="weekly";
                btn_hours.setBackgroundResource(R.drawable.rounded_grey_button);
                btn_daily.setBackgroundResource(R.drawable.rounded_grey_button);
                btn_weekly.setBackgroundResource(R.drawable.rounded_button);
                btn_weekly.setHintTextColor(Color.parseColor("#FFFFFF"));
                btn_daily.setHintTextColor(Color.parseColor("#2A2A2A"));
                btn_hours.setHintTextColor(Color.parseColor("#2A2A2A"));
                break;
            case R.id.txt_spin:
                 type_spiner.performClick();
                 type_spiner.setVisibility(View.INVISIBLE);

                break;
        }
    }
    private void Hit_filterApi() {
        appUtils.showDialog(getActivity());
        appUtils.dialog.show();

        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseCarRent> call=service.vehicleFilter(rent_type,select_car_type,min_price,max_price,Constants.accesstoken);

        call.enqueue(new Callback<ServerResponseCarRent>() {
            @Override
            public void onResponse(Call<ServerResponseCarRent> call, Response<ServerResponseCarRent> response) {
                ServerResponseCarRent data=response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")){
                    Intent  intent=new Intent(getActivity(), NavigationActivity.class);
                    intent.putExtra("type","rent");
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }else {
                    ShowMessage(data.getMessage());
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


    private void ShowMessage(String sms) {
        Toast.makeText(getActivity(),sms,Toast.LENGTH_LONG).show();
    }
}
