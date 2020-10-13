package Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.e.ryedup.NavigationActivity;
import com.e.ryedup.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Model.ServerResponseCarDetails;
import Model.ServerResponseRequestcar;
import Service.Interface;
import Utils.AppUtils;
import Utils.Constants;
import Utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCarDetails extends Fragment implements View.OnClickListener {
    TextView txt_no_seats,txt_car_name,txt_variant,txt_car_price,txt_insurance,txt_rent_type;
    ImageView img_car,img_cancel;
    Button btn_make_payment,btn_send_request;
    EditText edt_from_loc,edt_to_loc,edt_from_date,edt_to_date,edt_from_time,edt_to_time;
    Button btn_submit;
    String source="",destination="",start_date="",end_date="",start_time="",end_time="";
    LinearLayout ll_layout_lock,ll_layout_unlock;
    AppUtils appUtils;
    String id="",login_id="";
    SharedPreferences preference;
    Calendar myCalendar,tocalender;
    DatePickerDialog.OnDateSetListener date_from,date_to;
    DatePickerDialog from_dialog,to_dailog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_car_details,container,false);
        BindUI(view);
        NavigationActivity.fragmentActivity.toolbar_title.setText("Car Details");
        NavigationActivity.fragmentActivity.img_filter.setVisibility(View.GONE);
        appUtils=new AppUtils(getActivity());

        preference = getActivity().getSharedPreferences(Constants.Shared_Pref, Context.MODE_PRIVATE);
        login_id=preference.getString("loginid","");

        id=getArguments().getString("id");

        if(appUtils.isInternetConnected()){
            Hitcar_detailsApi();
        }else {
            ShowMessage("Please check your Internet connection");
        }

        btn_send_request.setOnClickListener(this);
        btn_make_payment.setOnClickListener(this);

        myCalendar = Calendar.getInstance();
        date_from = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        tocalender = Calendar.getInstance();
        date_to= new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                tocalender.set(Calendar.YEAR, year);
                tocalender.set(Calendar.MONTH, monthOfYear);
                tocalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel_to();
            }

        };

        return view;
    }

    private void BindUI(View view) {
        img_car=view.findViewById(R.id.img_car);
        txt_no_seats=view.findViewById(R.id.txt_no_seats);
        txt_car_name=view.findViewById(R.id.txt_car_name);
        txt_variant=view.findViewById(R.id.txt_variant);
        txt_car_price=view.findViewById(R.id.txt_car_price);
        txt_rent_type=view.findViewById(R.id.txt_rent_type);
        txt_insurance=view.findViewById(R.id.txt_insurance);
       // ll_layout_unlock=view.findViewById(R.id.ll_layout_unlock);
        btn_make_payment=view.findViewById(R.id.btn_make_payment);
        btn_send_request=view.findViewById(R.id.btn_send_request);
    }
    private void ShowMessage(String sms) {
        Toast.makeText(getActivity(),sms,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

             switch (v.getId()){
                 case R.id.btn_send_request:
                    openAlertDailog();
                     break;

                 case R.id.btn_make_payment:

                     break;
                 case R.id.edt_from_date:
                     from_dialog = new DatePickerDialog(getActivity(), date_from,myCalendar.get(Calendar.YEAR),
                             myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                     from_dialog.getDatePicker().setMinDate(new Date().getTime());
                     from_dialog.show();

                     break;
                 case R.id.edt_to_date:
                    to_dailog= new DatePickerDialog(getActivity(), date_to, tocalender
                             .get(Calendar.YEAR), tocalender.get(Calendar.MONTH),
                             tocalender.get(Calendar.DAY_OF_MONTH));
                     to_dailog.getDatePicker().setMinDate(new Date().getTime());
                     to_dailog.show();
                     break;
                 case R.id.edt_from_time:
                     int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                     int minute = myCalendar.get(Calendar.MINUTE);
                     TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                             new TimePickerDialog.OnTimeSetListener() {

                                 @Override
                                 public void onTimeSet(TimePicker view, int hourOfDay,
                                                       int minute) {

                                     edt_from_time.setText(hourOfDay + ":" + minute+":"+"00");
                                 }
                             }, hour, minute, true);
                     timePickerDialog.show();
                     break;
                 case R.id.edt_to_time:
                     int hour1 = myCalendar.get(Calendar.HOUR_OF_DAY);
                     int minute1 = myCalendar.get(Calendar.MINUTE);
                     TimePickerDialog timePickerDialog1 = new TimePickerDialog(getActivity(),
                             new TimePickerDialog.OnTimeSetListener() {

                                 @Override
                                 public void onTimeSet(TimePicker view, int hourOfDay,
                                                       int minute) {

                                     edt_to_time.setText(hourOfDay + ":" + minute+":"+"00");
                                 }
                             }, hour1, minute1, true);
                     timePickerDialog1.show();
                     break;
             }
    }

    private void openAlertDailog() {

        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater1.inflate(R.layout.bottom_sheet_dailog, null);
        // this is set the view from XML inside AlertDialog
        //builder.setView(dialogView);
        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
        // Get the custom alert dialog view widgets reference
        edt_from_loc = dialogView.findViewById(R.id.edt_from_loc);
        edt_to_loc = dialogView.findViewById(R.id.edt_to_loc);
        edt_from_date = dialogView.findViewById(R.id.edt_from_date);
        edt_to_date = dialogView.findViewById(R.id.edt_to_date);
        img_cancel=dialogView.findViewById(R.id.img_cancel);
        edt_from_time=dialogView.findViewById(R.id.edt_from_time);
        edt_to_time=dialogView.findViewById(R.id.edt_to_time);
        btn_submit = dialogView.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()){
                    if(appUtils.isInternetConnected()){
                         Hitrequsrt_carApi();
                         dialog.dismiss();
                     }else {
                        ShowMessage("Please check your Internet connection");
                     }
                }
            }
        });

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        edt_from_date.setOnClickListener(this);
        edt_to_date.setOnClickListener(this);
        edt_from_time.setOnClickListener(this);
        edt_to_time.setOnClickListener(this);

        dialog.show();
    }

    private void Hitrequsrt_carApi() {
        appUtils.showDialog(getActivity());
        appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseRequestcar> call=service.requestVehicle(id,login_id,source,destination,start_date,end_date,
                start_time,end_time,Constants.accesstoken);
        call.enqueue(new Callback<ServerResponseRequestcar>() {
            @Override
            public void onResponse(Call<ServerResponseRequestcar> call, Response<ServerResponseRequestcar> response) {
                ServerResponseRequestcar data = response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")){
                    btn_send_request.setHint("Request sent");
                    btn_send_request.setEnabled(false);
                }else {
                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseRequestcar> call, Throwable t) {
              //  ShowMessage("Try again");
                Log.e("error",t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

    private void Hitcar_detailsApi() {
        appUtils.showDialog(getActivity());
        appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseCarDetails> call=service.getSingleVehicle(id,login_id,Constants.accesstoken);
        call.enqueue(new Callback<ServerResponseCarDetails>() {
            @Override
            public void onResponse(Call<ServerResponseCarDetails> call, Response<ServerResponseCarDetails> response) {
                ServerResponseCarDetails data = response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")){
                    txt_no_seats.setText("Number of seats : "+data.getData().getNumber_of_seats());
                    txt_variant.setText("Variant : "+data.getData().getFuel_type());
                    txt_car_price.setText("Price: $"+data.getData().getVehicle_price());
                    txt_car_name.setText(data.getData().getVehicle_type());
                    txt_rent_type.setText("Rent type: "+data.getData().getRent_type());
                    Picasso.with(getActivity()).load(data.getData().getVehicle_image()).into(img_car);

                    if(data.getData().getRequest_status().equalsIgnoreCase("1")){
                        btn_send_request.setHint("Request sent");
                        btn_send_request.setEnabled(false);
                    }else if(data.getData().getRequest_status().equalsIgnoreCase("2")){
                        btn_send_request.setHint("Request Accepted");
                        btn_send_request.setEnabled(false);
                    }else {
                        btn_send_request.setEnabled(true);
                    }

                    if (data.getData().getVehicle_insurance().equalsIgnoreCase("")){
                        txt_insurance.setText("Car insurance: No");
                    }else {
                        txt_insurance.setText("Car insurance: Yes");
                    }


                }else {
                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseCarDetails> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error",t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }
    private boolean validation() {
        source=edt_from_loc.getText().toString();
        destination=edt_to_loc.getText().toString();
        start_date=edt_from_date.getText().toString();
        end_date=edt_to_date.getText().toString();
        start_time=edt_from_time.getText().toString();
        end_time=edt_to_time.getText().toString();

        if (source.isEmpty()){
            ShowMessage("Please enter Source address");
            return false;
        }else if(destination.isEmpty()){
            ShowMessage("Please enter Destination address");
            return false;
        }else if(start_date.isEmpty()){
            ShowMessage("Please enter Start date");
            return false;
        }else if(end_date.isEmpty()){
            ShowMessage("Please enter End date");
            return false;
        }else if(start_time.isEmpty()){
            ShowMessage("Please enter Start time");
            return false;
        }else if(end_time.isEmpty()){
            ShowMessage("Please enter End time");
            return false;
        }
        return true;
    }
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        edt_from_date.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabel_to() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        edt_to_date.setText(sdf.format(tocalender.getTime()));
    }
}
