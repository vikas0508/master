package Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.e.ryedup.HomeActivity;
import com.e.ryedup.R;

import Model.ServerResponeLogin;
import Model.ServerResponseOtp;
import Service.Interface;
import Utils.AppUtils;
import Utils.Constants;
import Utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentOtp  extends Fragment implements View.OnClickListener {
    EditText edt_one,edt_two,edt_three,edt_four,edt_five,edt_six;
    RelativeLayout btn_submit;
    TextView txt_resend,txt_timer;
    ImageView img_back;
    Intent intent;
    String phone_number="",otp_number="",device_id="",device_type="1";
    AppUtils appUtils;
    SharedPreferences preference;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_otp,container,false);
        BindId(view);
        appUtils=new AppUtils(getActivity());
        preference = getActivity().getSharedPreferences(Constants.Shared_Pref, Context.MODE_PRIVATE);
        device_id=preference.getString(Constants.FCMID,"");
        phone_number=getArguments().getString("number");

        btn_submit.setOnClickListener(this);
        txt_resend.setOnClickListener(this);
        img_back.setOnClickListener(this);


        edt_one.addTextChangedListener(new GenericTextWatcher(edt_one));
        edt_two.addTextChangedListener(new GenericTextWatcher(edt_two));
        edt_three.addTextChangedListener(new GenericTextWatcher(edt_three));
        edt_four.addTextChangedListener(new GenericTextWatcher(edt_four));

      //  edt_five.addTextChangedListener(new GenericTextWatcher(edt_five));
      //  edt_six.addTextChangedListener(new GenericTextWatcher(edt_six));

        //////////set timer
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                txt_resend.setEnabled(false);
                txt_timer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                txt_resend.setEnabled(true);
                txt_timer.setVisibility(View.GONE);
                txt_resend.setText("Didn't receive a text message? Resend code");
            }
        }.start();

        return view;
    }

    private void BindId(View view) {
        edt_one=view.findViewById(R.id.edt_one);
        edt_two=view.findViewById(R.id.edt_two);
        edt_three=view.findViewById(R.id.edt_three);
        edt_four=view.findViewById(R.id.edt_four);

       // edt_five=view.findViewById(R.id.edt_five);
      //  edt_six=view.findViewById(R.id.edt_six);

        btn_submit=view.findViewById(R.id.btn_submit);
        txt_resend=view.findViewById(R.id.txt_resend);
        txt_timer=view.findViewById(R.id.txt_timer);
        img_back=view.findViewById(R.id.img_back);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                String one=edt_one.getText().toString();
                String two=edt_two.getText().toString();
                String three=edt_three.getText().toString();
                String four=edt_four.getText().toString();
               // String five=edt_five.getText().toString();
            //    String six=edt_six.getText().toString();

                otp_number=one+two+three+four;
                Log.e("ffffff",otp_number);

                if (otp_number.isEmpty()){
                    ShowMessage("Please enter OTP number");
                }else {

                   if (appUtils.isInternetConnected()){
                         Hit_otpVerfyApi(otp_number);
                    }else {
                       ShowMessage("Please check your Internet connection");
                    }
                }
             /*   intent=new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();*/
                break;
            case R.id.txt_resend:
                if (appUtils.isInternetConnected()){
                    Hit_resendcode(phone_number);
                }else {
                    ShowMessage("Please check your Internet connection");
                }
                break;
            case R.id.img_back:
                hideKeyboard();
                getActivity().finish();
                break;


        }
    }

    private void Hit_resendcode(String phone_number) {
        appUtils.showDialog(getActivity());
        appUtils.dialog.show();

        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponeLogin> call=service.Login(phone_number,Constants.accesstoken);

        call.enqueue(new Callback<ServerResponeLogin>() {
            @Override
            public void onResponse(Call<ServerResponeLogin> call, Response<ServerResponeLogin> response) {
                ServerResponeLogin data=response.body();
                appUtils.dialog.dismiss();

                if (data.getCode().equalsIgnoreCase("200")){
                    ShowMessage(data.getData().getOtp());
                    Log.e("success",data.getData().getOtp());
                    //////////set timer
                    new CountDownTimer(60000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            txt_timer.setVisibility(View.VISIBLE);
                            txt_resend.setEnabled(false);
                            txt_resend.setText("Resend code:");
                            txt_timer.setText(String.valueOf(millisUntilFinished / 1000));
                        }

                        public void onFinish() {
                            txt_timer.setVisibility(View.GONE);
                            txt_resend.setEnabled(true);
                            txt_resend.setText("Didn't receive a text message? Resend code");

                        }
                    }.start();
                }else {
                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponeLogin> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error",t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

    private void Hit_otpVerfyApi(String otp) {
        appUtils.showDialog(getActivity());
        appUtils.dialog.show();

        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseOtp> call=service.verifyOTP(otp,phone_number,device_type,device_id,Constants.accesstoken);

        call.enqueue(new Callback<ServerResponseOtp>() {
            @Override
            public void onResponse(Call<ServerResponseOtp> call, Response<ServerResponseOtp> response) {
                ServerResponseOtp data=response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")){
                    editor = preference.edit();
                    if (data.getData()!=null ){
                        editor.putString("firstname",data.getData().getFirstname());
                        editor.putString("lastname",data.getData().getLastname());
                        editor.putString("address",data.getData().getAddress());
                        editor.putString("email",data.getData().getEmail());
                        editor.putString("contact",data.getData().getContact());
                        editor.putString("loginid",data.getData().getId());
                        editor.putString("user_id",data.getData().getLoginid());
                        editor.putString("user_type",data.getData().getRoletype());
                        editor.putString("image",data.getData().getProfileimage());
                        editor.putString("is_verified",data.getData().getIs_verified());

                        editor.commit();

                        intent=new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                        getActivity().finishAffinity();

                    }

                }else {
                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseOtp> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error",t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

    private void ShowMessage(String sms) {
        Toast.makeText(getActivity(),sms,Toast.LENGTH_LONG).show();
    }
    public void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;
        private GenericTextWatcher(View view)
        {
            this.view = view;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = s.toString();
            switch(view.getId())
            {
                case R.id.edt_one:
                    if(text.length()==1)
                        edt_two.requestFocus();
                    break;
                case R.id.edt_two:
                    if(text.length()==1)
                        edt_three.requestFocus();
                    else if(text.length()==0)
                        edt_one.requestFocus();
                    break;
                case R.id.edt_three:
                    if(text.length()==1)
                        edt_four.requestFocus();
                    else if(text.length()==0)
                        edt_two.requestFocus();
                    break;
                case R.id.edt_four:
                    if(text.length()==0)
                        edt_three.requestFocus();
                    break;
           /*     case R.id.edt_four:
                    if(text.length()==1)
                        edt_five.requestFocus();
                    else if(text.length()==0)
                        edt_three.requestFocus();
                    break;
                case R.id.edt_five:
                    if(text.length()==1)
                        edt_six.requestFocus();
                    else if(text.length()==0)
                        edt_four.requestFocus();
                    break;
                case R.id.edt_six:
                    if(text.length()==0)
                        edt_five.requestFocus();
                    break;*/

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
