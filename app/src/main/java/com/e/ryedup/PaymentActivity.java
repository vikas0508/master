package com.e.ryedup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import com.stripe.android.view.CardMultilineWidget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Model.ServerResponseRequestcar;
import Service.Interface;
import Utils.AppUtils;
import Utils.Constants;
import Utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btn_submit;
    CardMultilineWidget card_multiline_widget;
    Card cardToSave;
    EditText edt_one,edt_two,edt_three,edt_four,edt_month,edt_year,edt_cvc,edt_postal;
    SharedPreferences preference;
    String login_id="",code="";
    AppUtils appUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BindUI();
        appUtils=new AppUtils(this);
        preference = getSharedPreferences(Constants.Shared_Pref, Context.MODE_PRIVATE);
        login_id=preference.getString("loginid","");

        edt_one.addTextChangedListener(new GenericTextWatcher(edt_one));
        edt_two.addTextChangedListener(new GenericTextWatcher(edt_two));
        edt_three.addTextChangedListener(new GenericTextWatcher(edt_three));
        edt_four.addTextChangedListener(new GenericTextWatcher(edt_four));

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Validition()){
                    String cardNumber=edt_one.getText().toString()+edt_two.getText().toString()+edt_three.getText().toString()+edt_four.getText().toString();
                    int cardExpMonth= Integer.valueOf(edt_month.getText().toString());
                    int cardExpYear= Integer.valueOf(edt_year.getText().toString());
                    String cardCVC=edt_cvc.getText().toString();
                    String card_postal=edt_postal.getText().toString();
                    try {
                        cardToSave = Card.create(cardNumber, cardExpMonth, cardExpYear, cardCVC);
                        cardToSave.validateNumber();
                        cardToSave.validateCVC();
                    }catch (Exception e){
                        ShowMessage(e.getMessage());
                    }
                    //  cardToSave = card_multiline_widget.getCard();
                    final String number=cardToSave.getNumber();
                    String month= String.valueOf(cardToSave.getExpMonth());
                    String year=String.valueOf(cardToSave.getExpYear());
                    final String ccv=String.valueOf(cardToSave.getCVC());
                    final String expiryDate=month+"/"+year;
                    final String brandname=cardToSave.getBrand();

                    // cardToSave = cardToSave.toBuilder().name("Customer Name").build();
                    // cardToSave = cardToSave.toBuilder().addressZip("12345").build();
                    if (cardToSave == null) {
                        ShowMessage("Invalid Card Data");
                    }else {
                        final Stripe stripe = new Stripe(getApplicationContext(), "pk_test_Vr0acqNnbf984ISonHhSwxtc00gVrwbar5");
                        stripe.createToken(
                                cardToSave,
                                new TokenCallback() {
                                    public void onSuccess(@NonNull Token token) {
                                        // Send token to your server
                                        Log.e("strip token",token.getId());
                                        if (code.isEmpty()){
                                            hit_savecard(number,expiryDate,ccv,token.getId(),brandname);
                                        }else {

                                        }

                                    }

                                    public void onError(@NonNull Exception error) {
                                        // Show localized error message
                                        ShowMessage(error.getLocalizedMessage());
                                    }
                                }
                        );
                    }
                }

            }
        });


    }

    private boolean Validition() {

        String cardNumber=edt_one.getText().toString()+edt_two.getText().toString()+edt_three.getText().toString()+edt_four.getText().toString();
             if (cardNumber.isEmpty()){
                 ShowMessage("Please enter your card number");
                 return false;
             }else if(edt_month.getText().toString().isEmpty()){
                 ShowMessage("Please enter Expiry month ");
                 return false;
             }else if(edt_year.getText().toString().isEmpty()){
                 ShowMessage("Please enter Expiry year");
                 return false;
             }else if(edt_cvc.getText().toString().isEmpty()){
                 ShowMessage("Please enter CVC number");
                 return false;
             }
        return true;
    }

    private void BindUI() {
        btn_submit=findViewById(R.id.btn_submit);
        //card_multiline_widget=findViewById(R.id.card_multiline_widget);
        edt_one=findViewById(R.id.edt_one);
        edt_two=findViewById(R.id.edt_two);
        edt_three=findViewById(R.id.edt_three);
        edt_four=findViewById(R.id.edt_four);
        edt_month=findViewById(R.id.edt_month);
        edt_year=findViewById(R.id.edt_year);
        edt_cvc=findViewById(R.id.edt_cvc);
        edt_postal=findViewById(R.id.edt_postal);

    }

    private void hit_savecard(String number, String expiryDate, String ccv, String id, String brandname) {
        appUtils.showDialog(this);
      //  appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseRequestcar> call=service.saveCard(login_id,number,expiryDate,ccv,id,brandname,Constants.accesstoken);
        call.enqueue(new Callback<ServerResponseRequestcar>() {
            @Override
            public void onResponse(Call<ServerResponseRequestcar> call, Response<ServerResponseRequestcar> response) {

                ServerResponseRequestcar data=response.body();
                appUtils.dialog.dismiss();
                if(data.getCode().equalsIgnoreCase("200")){
                     code=data.getCode();
                     finish();
                    ShowMessage(data.getMessage());
                 //   Log.e("messsage",data.getMessage());
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


    private void ShowMessage(String sms) {
        Toast.makeText(getApplicationContext(),sms,Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            // overridePendingTransition(R.anim.slide_from_top,R.anim.slide_in_top);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    if(text.length()==4)
                        edt_two.requestFocus();
                    break;
                case R.id.edt_two:
                    if(text.length()==4)
                        edt_three.requestFocus();
                    else if(text.length()==0)
                        edt_one.requestFocus();
                    break;
                case R.id.edt_three:
                    if(text.length()==4)
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
