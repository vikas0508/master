package com.e.ryedup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_countinue;
    CountryCodePicker mCountryCodePicker;
    EditText edt_name,edt_email,edt_mobile_no;
    CheckBox checkbox_term;
    LinearLayout ll_customer,ll_owner;
    TextView txt_customer,txt_owner,txt_signin;
    public String mCode = "",phone_number="",number="",name="",email="",chk_status="0",user_type="";
    AppUtils appUtils;
   public static SignUpActivity signUpActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpActivity=this;
         BindUI();
        initCountryPicker();
        initView();

        appUtils=new AppUtils(getApplicationContext());

        ll_customer.setOnClickListener(this);
        ll_owner.setOnClickListener(this);
        btn_countinue.setOnClickListener(this);
        ///////////////set customer tab==============
        user_type="1";
        ll_customer.setBackgroundResource(R.drawable.left_view);
        ll_owner.setBackgroundColor(Color.TRANSPARENT);
        txt_customer.setTextColor(Color.WHITE);
        txt_owner.setTextColor(Color.BLACK);

        checkbox_term.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (checkbox_term.isChecked()){
                        chk_status="1";
                    }else {
                        chk_status="0";
                    }
            }
        });

    }

    private void Hit_signupApi(final String phone_number) {
       appUtils.showDialog(this);
       appUtils.dialog.show();

       Interface service = RetrofitClient.getClient().create(Interface.class);
       Call<ServerResponeLogin> call=service.signup(name,email,phone_number, Constants.accesstoken,chk_status,user_type);

       call.enqueue(new Callback<ServerResponeLogin>() {
           @Override
           public void onResponse(Call<ServerResponeLogin> call, Response<ServerResponeLogin> response) {
               ServerResponeLogin data=response.body();
               appUtils.dialog.dismiss();

               if (data.getCode().equalsIgnoreCase("200")){
                   ShowMessage(data.getData().getOtp());
                   Log.e("success",data.getData().getOtp());
                   edt_mobile_no.setText("");
                   edt_email.setText("");
                   edt_name.setText("");
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


    private void BindUI() {
        btn_countinue=findViewById(R.id.btn_countinue);
        edt_name=findViewById(R.id.edt_name);
        edt_email=findViewById(R.id.edt_email);
        edt_mobile_no=findViewById(R.id.edt_mobile_no);
        mCountryCodePicker=findViewById(R.id.ccp);
        checkbox_term=findViewById(R.id.checkbox_term);
        ll_customer=findViewById(R.id.ll_customer);
        ll_owner=findViewById(R.id.ll_owner);
        txt_customer=findViewById(R.id.txt_customer);
        txt_owner=findViewById(R.id.txt_owner);
        txt_signin=findViewById(R.id.txt_signin);
    }

    void initCountryPicker() {
        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        mCode= manager.getSimCountryIso().toUpperCase();
        if (mCode.equalsIgnoreCase("")) {
            mCountryCodePicker.setDefaultCountryUsingNameCode("BS");
            mCountryCodePicker.resetToDefaultCountry();
        } else {
            mCountryCodePicker.setDefaultCountryUsingNameCode(mCode);
            mCountryCodePicker.resetToDefaultCountry();
        }
        mCode = mCountryCodePicker.getSelectedCountryCodeWithPlus();
        mCountryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country country) {
                mCode = mCountryCodePicker.getSelectedCountryCodeWithPlus();
            }
        });
    }

    void initView() {
        SpannableString ss = new SpannableString(getResources().getString(R.string.already_have_an_account_login));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                //startActivity(SignUpActivity.createIntent(LoginActivity.this).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finishAffinity();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(SignUpActivity.this, R.color.colorPrimary));
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 24, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txt_signin.setText(ss);
        txt_signin.setMovementMethod(LinkMovementMethod.getInstance());
        txt_signin.setHighlightColor(Color.TRANSPARENT);
    }

    @Override
    public void onClick(View v) {

             switch (v.getId()){
                 case R.id.btn_countinue:
                     if(Validation()){
                         phone_number=mCode+number;
                         if (appUtils.isInternetConnected()){
                            Hit_signupApi(phone_number);
                         }else {
                             ShowMessage("Please check your Internet connection");
                         }

                   /* Intent intent=new Intent(getApplicationContext(),NavigationActivity.class);
                    intent.putExtra("type","otp");
                    intent.putExtra("number",phone_number);
                    startActivity(intent);*/

                     }
                     break;
                 case R.id.ll_customer:
                     user_type="1";
                     ll_customer.setBackgroundResource(R.drawable.left_view);
                     ll_owner.setBackgroundColor(Color.TRANSPARENT);
                     txt_customer.setTextColor(Color.WHITE);
                     txt_owner.setTextColor(Color.BLACK);
                     break;
                 case R.id.ll_owner:
                     user_type="3";
                     ll_owner.setBackgroundResource(R.drawable.right_view);
                     ll_customer.setBackgroundColor(Color.TRANSPARENT);
                     txt_customer.setTextColor(Color.BLACK);
                     txt_owner.setTextColor(Color.WHITE);
                     break;
             }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(getApplicationContext(), LoginActivity.class));

    }

    private boolean Validation() {
        number=edt_mobile_no.getText().toString().trim();
        name=edt_name.getText().toString().trim();
        email=edt_email.getText().toString().trim();
        if (name.isEmpty()){
            ShowMessage("Please enter Name");
            return false;
        }else if(email.isEmpty()){
            ShowMessage("Please enter Email address");
            return false;
        }else if(!isValidEmail(email)){
            ShowMessage("Please enter valid Email address");
            return false;
        }else if(number.isEmpty()){
            ShowMessage("Please enter Mobile Number");
            return false;
        }else if (number.length()<6) {
            ShowMessage("Please enter a valid Phone number");
            return false;
        } else if(chk_status.equalsIgnoreCase("0")){
            ShowMessage("Please select Terms & Conditions");
            return false;
        }

        return true;
    }

    private void ShowMessage(String sms) {
        Toast.makeText(getApplicationContext(),sms,Toast.LENGTH_LONG).show();
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


}
