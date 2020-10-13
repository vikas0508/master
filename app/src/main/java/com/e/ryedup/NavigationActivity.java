package com.e.ryedup;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import Fragment.FragmentOtp;
import Fragment.FragmentMessage;
import Fragment.FragmentHistory;
import Fragment.FragmentProfile;
import Fragment.FragmentDriver;
import Fragment.FragmentRentCar;
import Fragment.FragmentSettings;
import Fragment.FragmentAddVehicle;
import Fragment.FragmentWallet;
import Fragment.FragmentCarFilter;
import Fragment.FragmentNearbycar;
import Fragment.FragmentMyVehicle;
import Fragment.FragmentAllRequest;
import Fragment.FragmentNotification;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    Drawable upArrow;
    public TextView toolbar_title;
    String type="",phone_number,user_type="",id="",page="";
    Fragment fragment;
    Bundle bundle;
    public ImageView img_filter;
   public static NavigationActivity fragmentActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentActivity=this;
        setContentView(R.layout.activity_fragment);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title=toolbar.findViewById(R.id.toolbar_title);
        img_filter=toolbar.findViewById(R.id.img_filter);
        img_filter.setOnClickListener(this);

        type=getIntent().getStringExtra("type");
        phone_number=getIntent().getStringExtra("number");
        id=getIntent().getStringExtra("id");
        page=getIntent().getStringExtra("page");


        switch (type){
            case "otp":
                toolbar.setVisibility(View.GONE);
                fragment= new FragmentOtp();
                bundle=new Bundle();
                bundle.putString("number",phone_number);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
            break;
            case "message":
                toolbar_title.setText("Messages");
                img_filter.setVisibility(View.GONE);
                fragment= new FragmentMessage();
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                break;
            case "history":
              //  toolbar_title.setText("History");
                img_filter.setVisibility(View.GONE);
                fragment= new FragmentHistory();
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                break;
            case "driver":
                toolbar_title.setText("Go with RYDEUP Driver");
                fragment= new FragmentDriver();
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                break;
            case "rent":
                toolbar_title.setText("Rent a car");
                img_filter.setVisibility(View.VISIBLE);
                fragment= new FragmentRentCar();
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                break;
            case "setting":
                toolbar_title.setText("Settings");
                img_filter.setVisibility(View.GONE);
                fragment= new FragmentSettings();
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                break;

            case "profile":
                toolbar_title.setText("Profile");
                img_filter.setVisibility(View.GONE);
                fragment= new FragmentProfile();
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                break;

          /*  case "car details":
                toolbar_title.setText("Car Details");
                img_filter.setVisibility(View.GONE);
                fragment= new FragmentCarDetails();
                bundle=new Bundle();
                bundle.putString("id",id);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                break;*/

            case "add_vihicle":
                toolbar_title.setText("Upload vehicle");
                img_filter.setVisibility(View.GONE);
                fragment= new FragmentAddVehicle();
                bundle=new Bundle();
                bundle.putString("id",id);
                bundle.putString("page",page);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                break;

            case "own_history":
                toolbar_title.setText("History");
                img_filter.setVisibility(View.GONE);
                fragment= new FragmentHistory();
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                break;

            case "wallet":
                toolbar_title.setText("My Wallet");
                img_filter.setVisibility(View.GONE);
                fragment= new FragmentWallet();
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                break;

            case "own_setting":
                toolbar_title.setText("Settings");
                img_filter.setVisibility(View.GONE);
                fragment= new FragmentSettings();
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                break;
            /*case "history details":
                toolbar_title.setText("Past trip details");
                img_filter.setVisibility(View.GONE);
                fragment= new FragmentHistoryDetails();
                bundle=new Bundle();
                bundle.putString("id",id);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                break;*/
            case "rent_nearby":
                toolbar_title.setText("Near by cars");
                img_filter.setVisibility(View.GONE);
                fragment= new FragmentNearbycar();
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                break;
            case "my_vehicle":
                toolbar_title.setText("My vehicle");
                img_filter.setVisibility(View.GONE);
                fragment= new FragmentMyVehicle();
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                break;
            case "all_request":
                toolbar_title.setText("All request");
                img_filter.setVisibility(View.GONE);
                fragment= new FragmentAllRequest();
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                break;
            case "notification":
                toolbar_title.setText("Notifications");
                img_filter.setVisibility(View.GONE);
                fragment= new FragmentNotification();
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                break;



        }

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

    @Override
    public void onClick(View v) {
        toolbar_title.setText("Filters");
        img_filter.setVisibility(View.GONE);
        fragment= new FragmentCarFilter();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack("").commit();
    }
}
