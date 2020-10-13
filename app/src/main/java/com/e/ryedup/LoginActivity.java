package com.e.ryedup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import Model.ServerResponeLogin;
import Service.Interface;
import Utils.AppUtils;
import Utils.Constants;
import Utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_sign_in;
    CountryCodePicker mCountryCodePicker,ccp_number;
    EditText edit_mobile_no;
    TextView txt_mobile_no,txt_error,txt_signup;
    View vew_line;
    RelativeLayout fram_mobile,rl_next;
    LinearLayout ll_layout;
    ImageView img_back;
    private String mCode = "",phone_number="",number="";
    Intent intent;
    AppUtils appUtils;
    Animation animation;
    SharedPreferences preference;
    boolean isVisibilty=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        preference = getSharedPreferences(Constants.Shared_Pref, Context.MODE_PRIVATE);

        BindUI();
        initCountryPicker();
        initView();
        appUtils=new AppUtils(getApplicationContext());

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("token", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        SharedPreferences.Editor editor = preference.edit();
                        editor.putString(Constants.FCMID,token);
                        editor.commit();
                        // Log and toast
                        String msg = token;
                        Log.e("token", msg);
                        //     fmHwLGN2zWo:APA91bEU0-SmffJsBd5HkcQWV57pE51lNZa7GfezedGoFwxpj714K9jctooLi085T8u1ny_Kbv3O5v1-Hgvg5lk4q1vWI001ij4vEt4O6GzBWHseEpN5WtzNzd-BasyDqLKaXmQfeNT2
                        // Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        rl_next.setOnClickListener(this);
        txt_signup.setOnClickListener(this);
        txt_mobile_no.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    void initCountryPicker() {
        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        mCode= manager.getSimCountryIso().toUpperCase();
        if (mCode.equalsIgnoreCase("")) {
            mCountryCodePicker.setDefaultCountryUsingNameCode("BS");
            mCountryCodePicker.resetToDefaultCountry();
            ccp_number.setDefaultCountryUsingNameCode("BS");
            ccp_number.resetToDefaultCountry();
        } else {
            mCountryCodePicker.setDefaultCountryUsingNameCode(mCode);
            mCountryCodePicker.resetToDefaultCountry();
            ccp_number.setDefaultCountryUsingNameCode(mCode);
            ccp_number.resetToDefaultCountry();
        }
        mCode = ccp_number.getSelectedCountryCodeWithPlus();
        ccp_number.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country country) {
                mCode = ccp_number.getSelectedCountryCodeWithPlus();
            }
        });
    }
    void initView() {
        SpannableString ss = new SpannableString(getResources().getString(R.string.don_t_have_an_account_sign_up));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                //startActivity(SignUpActivity.createIntent(LoginActivity.this).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txt_signup.setText(ss);
        txt_signup.setMovementMethod(LinkMovementMethod.getInstance());
        txt_signup.setHighlightColor(Color.TRANSPARENT);
    }

    private void BindUI() {
        rl_next=findViewById(R.id.rl_next);
        txt_mobile_no=findViewById(R.id.txt_mobile_no);
        mCountryCodePicker=findViewById(R.id.ccp);
        ccp_number=findViewById(R.id.ccp_number);
        txt_signup=findViewById(R.id.txt_signup);
        fram_mobile=findViewById(R.id.fram_mobile);
        ll_layout=findViewById(R.id.ll_layout);
        img_back=findViewById(R.id.img_back);
        edit_mobile_no=findViewById(R.id.edit_mobile_no);
        txt_error=findViewById(R.id.txt_error);
        vew_line=findViewById(R.id.vew_line);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_next:
                if(validation()){
                     phone_number=mCode+number;
                    if (appUtils.isInternetConnected()){
                        Hit_loginApi(phone_number);
                    }else {
                        ShowMessage("Please check your Internet connection");
                    }
                    /*Intent intent=new Intent(getApplicationContext(),NavigationActivity.class);
                    intent.putExtra("type","otp");
                    intent.putExtra("number",phone_number);
                    startActivity(intent);*/
                   // overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                }
                break;

            case R.id.txt_signup:
                intent=new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.txt_mobile_no:
                 isVisibilty=true;
                fram_mobile.setVisibility(View.VISIBLE);
                 animation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_in_up);
                fram_mobile .setAnimation(animation);

                break;

            case R.id.img_back:
                hideKeyboard();
                isVisibilty=false;
                animation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_in_top);
                fram_mobile .setAnimation(animation);
                fram_mobile.setVisibility(View.GONE);

                break;
        }
    }
    @Override
    public void onBackPressed() {
        if (isVisibilty==true){
            hideKeyboard();
            isVisibilty=false;
            animation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_in_top);
            fram_mobile .setAnimation(animation);
            fram_mobile.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
        }
    }

    private void Hit_loginApi(final String phone_number) {
        appUtils.showDialog(this);
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
                    Intent intent=new Intent(getApplicationContext(), NavigationActivity.class);
                    intent.putExtra("type","otp");
                    intent.putExtra("number",phone_number);
                    startActivity(intent);
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

    private boolean validation() {
        number=edit_mobile_no.getText().toString().trim();
        if (number.isEmpty()){
           // ShowMessage("Please enter phone number");
            txt_error.setVisibility(View.VISIBLE);
            vew_line.setBackgroundColor(Color.RED);
            return false;
        }else if (number.length()<6) {
            txt_error.setVisibility(View.VISIBLE);
            txt_error.setText("Please enter a valid Phone Number");
            vew_line.setBackgroundColor(Color.RED);
            return false;
        } else {
            txt_error.setVisibility(View.GONE);
            vew_line.setBackgroundColor(Color.GRAY);
        }
        return true;
    }
    private void ShowMessage(String sms) {
        Toast.makeText(getApplicationContext(),sms,Toast.LENGTH_LONG).show();
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
