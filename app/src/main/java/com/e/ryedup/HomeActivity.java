package com.e.ryedup;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.skyfishjy.library.RippleBackground;
import com.squareup.picasso.Picasso;
import com.willy.ratingbar.ScaleRatingBar;

import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import Adapter.RVBookTypeAdapter;
import Adapter.RVGetCardAdapter;
import Adapter.RVNavigationAdapter;
import Adapter.RVRequestAdapter;
import Model.ServerResponeStatusUpdate;
import Model.ServerResponseDriverDetails;
import Model.ServerResponseGetCard;
import Model.ServerResponseNearby;
import Model.ServerResponseNoticount;
import Model.ServerResponseRequestcar;
import Model.ServerResponseRequestlist;
import Model.ServerResponseTrackDriver;
import Model.ServerResponseUpdate;
import Model.ServerResponsehomelocation;
import Service.Interface;
import Utils.AppUtils;
import Utils.Constants;
import Utils.GMapV2GetRoute;
import Utils.GPSTracker;
import Utils.RetrofitClient;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    public DrawerLayout drawerLayout;
    RecyclerView rv_navigationlist, rv_owner_requstlist, rv_rydeup_type, rv_cardlist;
    List<String> titles, booking_titles;
    List<ServerResponseNearby.Data> bookinglist;

    TypedArray nav_image, booking_type;
    CircleImageView img_profile, img_driver, img_driver_profile;
    public TextView txt_name, txt_location, txt_username, txt_dashbord, txt_cash, txt_time, txt_no_data, tvTitle, txt_count,txt_card_no;
    public TextView txt_driver_name, txt_plat_info, txt_car_name, txt_cancel, txt_contact, txt_driver, txt_trip_price, toolbar_title,txt_sectect_cash;
    public  ImageView img_call, img_sms,img_card;
    EditText edt_to_loc, edt_from_loc, edt_feedback;
    public Button btn_conform, btn_continue;
    public String categoryid = "", driver_id = "";
    boolean isVisibilty = false, isDriverTrack = false, istrackIcon = false, isPayment = false, isStart_trak = false;
    View view_line;
    RelativeLayout rl_header, rl_customer, rl_owner, rl_default_layout, rl_bottom, rl_complete, rl_type, rl_request_count,
            rl_notification, rl_anim,rl_select_card;
    FrameLayout fram_customer;
    ImageView img_add, image_car;
    RippleBackground content_ripp;
    List<ServerResponseRequestlist.Data> requestlist;
    public RVRequestAdapter adapter;
    RVGetCardAdapter rvGetCardAdapter;
    List<ServerResponseGetCard.Data> cardlist;
    Intent intent;
    SharedPreferences preference,tempprefrence;
    SharedPreferences.Editor editor;
    public String login_id = "", address = "", firstname = "", user_type = "", image = "", map_address = "You are here at!", source_name = "", device_id = "",
            destination_name = "", trak_destination = "";
    public String time = "", stauts="",request_id = "", phone_number = "", driver_name = "", driver_image = "", driver_token = "", paymentMode = "cash";

    public String trip_price = "",select_card_id="",card_no="",card_image="",strip_token="",noti_typ="",device_type="";

    ScaleRatingBar simpleRatingBar;

    //////map reletaed varieble
    private GoogleMap mMap, map2;
    public static HomeActivity homeActivity;
    double latitude = 0.0, longitude = 0.0;
    double dest_latitude = 0.0, dest_longitude = 0.0, trak_lat = 0.0, trak_long = 0.0;
    double updat_lat = 0.0, update_long = 0.0;
    GPSTracker gps;
    MapFragment mapFragment;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    GMapV2GetRoute v2GetRouteDirection;
    Document document;
    LatLng destination;
    LatLng source;
    LatLng car_position;
    /////////////
    Animation animation;
    AppUtils appUtils;
    Dialog dialog=null;
   // BottomSheetDialog dialog = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_title);

        homeActivity = this;
        setNavigationView();
        // set navigation profile data
        preference = getSharedPreferences(Constants.Shared_Pref, Context.MODE_PRIVATE);
        user_type = preference.getString("user_type", "");
        login_id = preference.getString("loginid", "");
        device_id = preference.getString(Constants.FCMID, "");
        //noti_typ=getIntent().getStringExtra("noti_type");

        BindUI();
        appUtils = new AppUtils(this);
        if (stauts.equalsIgnoreCase("")){
            checkPermissions();
        }

       /* SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
        Log.e("device_id", device_id);
        /* try {
            user_type=SignUpActivity.signUpActivity.user_type;
            Log.e("aaaa",user_type);
        }catch (Exception e){

        }*/

        if (user_type.equalsIgnoreCase("3")) {
            rl_owner.setVisibility(View.VISIBLE);
            rl_customer.setVisibility(View.GONE);
            fram_customer.setVisibility(View.GONE);
            nav_image = this.getResources().obtainTypedArray(R.array.owner_icon);
            // nav_image= new int[]{R.mipmap.history, R.mipmap.plus, R.mipmap.wallet, R.mipmap.setting_icon, R.mipmap.logout};
            titles = Arrays.asList(this.getResources().getStringArray(R.array.navigation_owner));
            ////owner layout home list
            rv_owner_requstlist.setLayoutManager(new LinearLayoutManager(this));
            requestlist = new ArrayList<>();

        } else {

            rl_owner.setVisibility(View.GONE);
            rl_customer.setVisibility(View.VISIBLE);
            fram_customer.setVisibility(View.VISIBLE);
            nav_image = this.getResources().obtainTypedArray(R.array.customer_icon);
            //   nav_image= new int[] {R.mipmap.home,R.mipmap.history,R.mipmap.user_driver,R.mipmap.car,R.mipmap.setting_icon,R.mipmap.logout};
            titles = Arrays.asList(this.getResources().getStringArray(R.array.navigation_menu));


        }
        rv_navigationlist.setLayoutManager(new LinearLayoutManager(this));
        rv_navigationlist.setAdapter(new RVNavigationAdapter(this, titles, nav_image, user_type));

        rl_header.setOnClickListener(this);
        edt_to_loc.setOnClickListener(this);
        edt_from_loc.setOnClickListener(this);

        btn_conform.setOnClickListener(this);
        rl_select_card.setOnClickListener(this);
        btn_continue.setOnClickListener(this);

        rl_notification.setOnClickListener(this);
        txt_cancel.setOnClickListener(this);
        txt_contact.setOnClickListener(this);

        //   rl_default_layout.setOnClickListener(this);
        // img_add.setOnClickListener(this);
        rl_default_layout.setVisibility(View.VISIBLE);
        //rl_bottom.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_in_up);
        rl_default_layout.setAnimation(animation);

        if(appUtils.isInternetConnected()){
            hit_noticount();
        }else {
            ShowMessage("Please check your Internet connection");
        }

        //showEditDialog();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();

        address = preference.getString("address", "");
        firstname = preference.getString("firstname", "");
        image = preference.getString("image", "");

        if (firstname != null && !firstname.equalsIgnoreCase("") && firstname.length() > 0) {
            txt_name.setText(firstname.substring(0, 1).toUpperCase() +
                    firstname.substring(1, firstname.length()));
            txt_username.setText("Hey" + " " + firstname);
        }
        ///  txt_name.setText(firstname);
        if (!address.equalsIgnoreCase("")) {
            txt_location.setText("Location : " + address);
            txt_location.setText(address);
        }

        if (!image.equalsIgnoreCase("")) {
            Picasso.with(this).load(image).placeholder(R.mipmap.default_user).into(img_profile);
        }

        if (user_type.equalsIgnoreCase("3")) {
            if (appUtils.isInternetConnected()) {
                Hit_getRequestApi();
            } else {
                ShowMessage("Please check your Internet connection");
            }
        } else {

            tempprefrence = getSharedPreferences("temp", Context.MODE_PRIVATE);
            stauts=tempprefrence.getString("stauts","");
            request_id=tempprefrence.getString("request_id","");


            if(!request_id.equalsIgnoreCase("")){
                // ShowMessage("On Resume");
                rl_default_layout.setVisibility(View.GONE);
                if(appUtils.isInternetConnected()){
                    hit_updateStaus();
                }
                else {
                    ShowMessage("Please check your Internet connection");
                }
            }else {

            }

        }

        try{
            /////////////////register custom  Broadcast receiver
            registerReceiver(new MyReceiver(), new IntentFilter("com.e.ryedup.CUSTOM_INTENT"));
        }catch (Exception e){
            ShowMessage(e.getMessage());
        }

        ///////////////////////start driver tracking==========

        if (isDriverTrack == true) {
            final Handler handler = new Handler();
            final int delay = 3000; //milliseconds
            handler.postDelayed(new Runnable() {
                public void run() {
                    ///// hit driver track api
                    Hit_trackDriver();
                    //  ShowMessage("test");
                    handler.postDelayed(this, delay);
                }
            }, delay);
        }

        /////////////////hit get card api
        if(isPayment==true){
            if(appUtils.isInternetConnected()){
                hit_getcard(dialog);
            }else {
                ShowMessage("Please check your Internet connection");
            }
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // ShowMessage("On Restart");
        if (user_type.equalsIgnoreCase("3")) {
            if (appUtils.isInternetConnected()) {
                Hit_getRequestApi();
            } else {
                ShowMessage("Please check your Internet connection");
            }
        } else {

            tempprefrence = getSharedPreferences("temp", Context.MODE_PRIVATE);
            stauts = tempprefrence.getString("stauts", "");
            request_id = tempprefrence.getString("request_id", "");

            if (isVisibilty==false){
                rl_default_layout.setVisibility(View.VISIBLE);
            }else {
                rl_default_layout.setVisibility(View.GONE);
            }

            if (request_id.equalsIgnoreCase("")) {

                if (appUtils.isInternetConnected()) {
                    hit_updateStaus();
                } else {
                    ShowMessage("Please check your Internet connection");
                }
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
      //  ShowMessage("On Pause");
        // unregisterReceiver(new MyReceiver());
    }

    private void  BindUI() {

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        // img_add=toolbar.findViewById(R.id.img_add);
        rl_customer = findViewById(R.id.rl_customer);
        rl_owner = findViewById(R.id.rl_owner);
        rl_default_layout = findViewById(R.id.rl_default_layout);
        rl_bottom = findViewById(R.id.rl_bottom);
        rl_complete = findViewById(R.id.rl_complete);
        rl_type = findViewById(R.id.rl_type);
        rl_request_count = findViewById(R.id.rl_request_count);
        rl_notification = findViewById(R.id.rl_notification);
        rl_anim = findViewById(R.id.rl_anim);
        rl_select_card = findViewById(R.id.rl_select_card);

        content_ripp = findViewById(R.id.content_ripp);
        fram_customer = findViewById(R.id.fram_customer);
        rv_owner_requstlist = findViewById(R.id.rv_owner_requstlist);
        txt_username = findViewById(R.id.txt_username);
        txt_dashbord = findViewById(R.id.txt_dashbord);
        txt_count = findViewById(R.id.txt_count);
        txt_cancel = findViewById(R.id.txt_cancel);
        txt_contact = findViewById(R.id.txt_contact);
        txt_driver = findViewById(R.id.txt_driver);
        txt_trip_price = findViewById(R.id.txt_trip_price);

        img_card = findViewById(R.id.img_card);
        txt_card_no = findViewById(R.id.txt_card_no);

        txt_no_data = findViewById(R.id.txt_no_data);
        edt_to_loc = findViewById(R.id.edt_to_loc);
        edt_from_loc = findViewById(R.id.edt_from_loc);
        edt_feedback = findViewById(R.id.edt_feedback);

        image_car = findViewById(R.id.image_car);
        img_driver = findViewById(R.id.img_driver);
        img_driver_profile = findViewById(R.id.img_driver_profile);
        // img_call=findViewById(R.id.img_call);
        // img_sms=findViewById(R.id.img_sms);
        txt_driver_name = findViewById(R.id.txt_driver_name);
        txt_plat_info = findViewById(R.id.txt_plat_info);
        txt_car_name = findViewById(R.id.txt_car_name);
        simpleRatingBar = findViewById(R.id.simpleRatingBar);
        btn_continue = findViewById(R.id.btn_continue);

        btn_conform = findViewById(R.id.btn_conform);
        txt_cash = findViewById(R.id.txt_cash);
        rv_rydeup_type = findViewById(R.id.rv_rydeup_type);
        rv_rydeup_type.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //  rv_rydeup_type.setAdapter(new RVBookTypeAdapter(this,booking_titles,booking_type));
    }

    private void setNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_home);
        //  View header = navigationView.getHeaderView(0);
        rl_header = navigationView.findViewById(R.id.rl_header);
        img_profile = navigationView.findViewById(R.id.img_profile);
        txt_name = navigationView.findViewById(R.id.txt_name);
        txt_location = navigationView.findViewById(R.id.txt_location);
        rv_navigationlist = navigationView.findViewById(R.id.rv_navigationlist);

        toggle = new ActionBarDrawerToggle(HomeActivity.this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboard();
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                hideKeyboard();
            }
        };
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_notification:
                if (appUtils.isInternetConnected()) {
                    hit_notiview();
                } else {
                    ShowMessage("Please check your Internet connection");
                }
                break;
            case R.id.rl_header:
                drawerLayout.closeDrawers();
                intent = new Intent(getApplicationContext(), NavigationActivity.class);
                intent.putExtra("type", "profile");
                startActivity(intent);
                break;
         /*   case R.id.img_add:
                intent=new Intent(getApplicationContext(), NavigationActivity.class);
                intent.putExtra("type","add_vihicle");
                startActivity(intent);
                break;*/
            case R.id.edt_to_loc:
                Places.initialize(getApplicationContext(), getResources().getString(R.string.map_key));
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        //  .setTypeFilter(TypeFilter.ADDRESS)
                        .build(this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                break;
            case R.id.edt_from_loc:
                Places.initialize(getApplicationContext(), getResources().getString(R.string.map_key));
                List<Place.Field> field = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                Intent intent1 = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, field)
                        // .setTypeFilter(TypeFilter.ADDRESS)
                        .build(this);
                startActivityForResult(intent1, 2);

                break;
              /* case R.id.rl_default_layout:
                  // Toast.makeText(this,"click hear",Toast.LENGTH_LONG).show();
                   animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_top);
                   rl_default_layout.setAnimation(animation);
                   rl_default_layout.setVisibility(View.INVISIBLE);
                break;*/
            case R.id.btn_conform:
                String src_long = String.valueOf(longitude);
                String src_lat = String.valueOf(latitude);
                String dest_long = String.valueOf(dest_longitude);
                String dest_lati = String.valueOf(dest_latitude);
                // Log.e("id",categoryid);
                ////// send driver request api
                if (appUtils.isInternetConnected()) {
                    hit_requestApi(categoryid, src_long, src_lat, dest_long, dest_lati);
                } else {
                    ShowMessage("Please check your Internet connection");
                }

                break;
            case R.id.rl_select_card:
                openAlertDailog_cash();
                break;

            case R.id.btn_continue:
                String feedback = edt_feedback.getText().toString();
                String rate_point = String.valueOf(simpleRatingBar.getRating());
                if (rate_point.equalsIgnoreCase("0.1")){

                }else {
                    hideKeyboard();
                    if (appUtils.isInternetConnected()) {
                        hit_rateApi(request_id, feedback, rate_point);
                    } else {
                        ShowMessage("Please check your Internet connection");
                    }
                }

                break;
            case R.id.img_call:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone_number));
                startActivity(callIntent);
                break;
            case R.id.img_sms:
                intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("driverid", driver_id);
                intent.putExtra("driver_name", driver_name);
                intent.putExtra("request_id", request_id);
                intent.putExtra("driver_image", driver_image);
                intent.putExtra("deriver_token", driver_token);
                intent.putExtra("device_type",device_type);
                startActivity(intent);
                break;

            case R.id.txt_cancel:
                if (appUtils.isInternetConnected()) {
                    hit_cancelApi(request_id);
                } else {
                    ShowMessage("Please check your Internet connection");
                }
                break;

            case R.id.txt_contact:
                openAlertDailog_contact();
                break;

        }
    }

    ////////////owner section get request api
    private void Hit_getRequestApi() {
        appUtils.showDialog(this);
        //  appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseRequestlist> call = service.getAllRequests(login_id, Constants.accesstoken);
        call.enqueue(new Callback<ServerResponseRequestlist>() {
            @Override
            public void onResponse(Call<ServerResponseRequestlist> call, Response<ServerResponseRequestlist> response) {
                ServerResponseRequestlist data = response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")) {
                    txt_no_data.setVisibility(View.GONE);
                    rv_owner_requstlist.setVisibility(View.VISIBLE);
                    rl_request_count.setVisibility(View.VISIBLE);
                    if (data.getData().size() != 0 && data.getData() != null) {
                        requestlist = data.getData();
                        txt_dashbord.setText("You have " + data.getData().size() + " new request");
                        adapter = new RVRequestAdapter(HomeActivity.this, requestlist);
                        rv_owner_requstlist.setAdapter(adapter);
                    } else {
                        //ShowMessage(data.getMessage());
                        txt_no_data.setVisibility(View.VISIBLE);
                        rv_owner_requstlist.setVisibility(View.GONE);
                        rl_request_count.setVisibility(View.GONE);
                    }
                } else {
                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseRequestlist> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error", t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }


    private void ShowMessage(String sms) {
        Toast.makeText(getApplicationContext(), sms, Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBackPressed() {

        if (isVisibilty == true) {
            rl_type.setVisibility(View.GONE);
            animation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_in_top);
            rl_type.setAnimation(animation);

            rl_default_layout.setVisibility(View.VISIBLE);
            edt_from_loc.setText("");
            isVisibilty = false;
            mMap.clear();
            getCurrentlocation();
            mapFragment.getMapAsync(this);
        } else {
            super.onBackPressed();
        }
        //  mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        /* if (id == android.R.id.home) {
           onBackPressed();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    this.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE}, 1);
                return;
            } else {
                getCurrentlocation();
            }
        } else {
            getCurrentlocation();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            getCurrentlocation();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getCurrentlocation() {
        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.e("aaaa", "" + latitude + "  " + longitude);
            Geocoder geocoder;
            Address returnAddress;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
               if(latitude!=0.0&&longitude!=0.0){
                    addresses = geocoder.getFromLocation(latitude, longitude, 2); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    returnAddress = addresses.get(0);
                    String locality = returnAddress.getLocality();
                    String name = returnAddress.getFeatureName();
                    String subLocality = returnAddress.getSubLocality();
                    String country = returnAddress.getCountryName();
                    String region_code = returnAddress.getCountryCode();
                    String zipcode = returnAddress.getPostalCode();
                    String state = returnAddress.getAdminArea();

                    if (locality == null && subLocality == null) {
                        // map_address=locality;
                        edt_to_loc.setText(name);
                        source_name = name;
                        mapFragment.getMapAsync(this);
                    } else if (subLocality == null) {
                        edt_to_loc.setText(name);
                        source_name = name;
                        mapFragment.getMapAsync(this);
                    } else {
                         map_address=subLocality+","+locality;
                        edt_to_loc.setText(name + " " + subLocality + " " + locality);
                        source_name = name + " " + subLocality + " " + locality;
                        mapFragment.getMapAsync(this);
                    }

                    String mlongitude = String.valueOf(longitude);
                    String mlatitude = String.valueOf(latitude);

                   if (appUtils.isInternetConnected()) {
                       hit_upadtelocApi(mlongitude, mlatitude);
                   } else {
                       ShowMessage("Please check your Internet connection");
                   }

               }

                //  edt_country.setText(country);
            } catch (IOException e) {
                Log.e("error message", e.getMessage());
                e.printStackTrace();
            }
        } else {
            gps.showSettingsAlert();
        }
    }

    private void hit_upadtelocApi(String mlongitude, String mlatitude) {
        appUtils.showDialog(this);
        //  appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponsehomelocation> call = service.updateCustomerLocation(login_id, mlongitude, mlatitude, Constants.accesstoken);

        call.enqueue(new Callback<ServerResponsehomelocation>() {
            @Override
            public void onResponse(Call<ServerResponsehomelocation> call, Response<ServerResponsehomelocation> response) {
                ServerResponsehomelocation data = response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")) {
                    if (data.getData().size() != 0) {
                        for (int idx = 0; idx < data.getData().size(); idx++) {
                            Double latitude = Double.valueOf(data.getData().get(idx).getLatitude());
                            Double longitude = Double.valueOf(data.getData().get(idx).getLongitude());
                            LatLng nearbuy = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(nearbuy)
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_large)));
                            // ShowMessage(data.getMessage());
                        }
                    }
                } else {
                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponsehomelocation> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error", t.toString());
                appUtils.dialog.dismiss();
            }
        });

    }

    public void selectcrad(String card, String card_id, String card_image, String strip_token) {
        select_card_id=card_id;
        card_no=card;
        this.card_image=card_image;
        this.strip_token=strip_token;
       // ShowMessage(select_card_id);
        Log.e("card",select_card_id);
         if (card.equalsIgnoreCase("")){
                    txt_cash.setVisibility(View.VISIBLE);
                }else {
                    txt_cash.setVisibility(View.GONE);
                    txt_card_no.setVisibility(View.VISIBLE);
                    img_card.setVisibility(View.VISIBLE);
                    paymentMode="card";

                    String mask1 = card.replaceAll("\\w(?=\\w{4})", "*");
                    txt_card_no.setText(mask1);

                    if (card_image!=null){
                        Picasso.with(homeActivity).load(card_image).skipMemoryCache().placeholder(R.mipmap.credit_card).into(img_card);
                    }
                }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng latLng = place.getLatLng();
                longitude = latLng.longitude;
                latitude = latLng.latitude;
                source_name = place.getName();
                String mStringLatitude = String.valueOf(latLng.latitude);
                map_address = place.getName();
                String mStringLongitude = String.valueOf(latLng.longitude);
                mMap.clear();
                mapFragment.getMapAsync(this);
                if (appUtils.isInternetConnected()) {
                    hit_upadtelocApi(String.valueOf(longitude), String.valueOf(latitude));
                } else {
                    ShowMessage("Please check your Internet connection");
                }
                Log.e("home=====", "" + place.getName() + ", " + mStringLatitude + "," + mStringLongitude);
                edt_to_loc.setText(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.e("home=====", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng latLng = place.getLatLng();
                dest_longitude = latLng.longitude;
                dest_latitude = latLng.latitude;
                destination_name = place.getName();

                trak_long = latLng.longitude;
                trak_lat = latLng.latitude;
                trak_destination = place.getName();
                String mStringLatitude = String.valueOf(latLng.latitude);
                String mStringLongitude = String.valueOf(latLng.longitude);
                // Log.e("home=====", "" + place.getName() + ", " +mStringLatitude+","+mStringLongitude);
                Log.e("home=====", "" + place.getName() + ", " + place.getId());
                edt_from_loc.setText(place.getName());
                if (!edt_from_loc.getText().toString().trim().equalsIgnoreCase("")) {
                    // mapFragment.getMapAsync(this);
                    hitRoute();
                    // openAlertDailog_type();
                    bookinglist = new ArrayList<>();
                    isVisibilty = true;
                    rl_default_layout.setVisibility(View.GONE);
                    rl_type.setVisibility(View.VISIBLE);

                    //////////set animation
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.slide_in_up);
                    rl_type.setAnimation(animation);

                    String mlongitude = String.valueOf(longitude);
                    String mlatitude = String.valueOf(latitude);
                    String desti_long=String.valueOf(dest_longitude);
                    String desti_lati=String.valueOf(dest_latitude);

                    if (appUtils.isInternetConnected()) {
                        hit_booktypeApi(mlatitude, mlongitude,desti_long,desti_lati);
                    } else {
                        ShowMessage("Please check your Internet connection");
                    }
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.e("home=====", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.

            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng latLng = place.getLatLng();
                if (map_address.equalsIgnoreCase(source_name)) {
                    longitude = latLng.longitude;
                    latitude = latLng.latitude;
                    source_name = place.getName();
                    edt_to_loc.setText(source_name);

                    if (appUtils.isInternetConnected()) {
                        hit_upadtelocApi(String.valueOf(longitude), String.valueOf(latitude));
                    } else {
                        ShowMessage("Please check your Internet connection");
                    }
                } else {
                    dest_longitude = latLng.longitude;
                    dest_latitude = latLng.latitude;
                    destination_name = place.getName();
                    edt_from_loc.setText(destination_name);

                    if (appUtils.isInternetConnected()) {
                        hit_upadtelocApi(String.valueOf(longitude), String.valueOf(latitude));
                    } else {
                        ShowMessage("Please check your Internet connection");
                    }
                }
                // Log.e("home=====", "" + place.getName() + ", " +mStringLatitude+","+mStringLongitude);
                Log.e("home=====", "" + place.getName() + ", " + place.getId());
                mMap.clear();
                mapFragment.getMapAsync(this);
                if(!source_name.equalsIgnoreCase("") && !destination_name.equalsIgnoreCase("")){
                    hitRoute();
                }
            }

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        // Add a marker in Sydney, Australia, and move the camera.
        source = new LatLng(latitude, longitude);

        // mMap.addMarker(new MarkerOptions().position(sydney).title(map_address));
        boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));
        if (!success) {
            Log.e("map stayle", "Style parsing failed.");
        }

        Marker marker ;
        if (isStart_trak == true) {
            marker = mMap.addMarker(new MarkerOptions()
                    .position(source)
                    .title(source_name)
                    .snippet("")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_large)));
        } else {
            marker = mMap.addMarker(new MarkerOptions()
                    .position(source)
                    .title(source_name)
                    .snippet(""));
            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        }

        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(source_name, time));
        marker.showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(source, 11));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
// Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(stauts.equalsIgnoreCase("3") || isVisibilty==false){

                }else {
                    map_address = marker.getTitle();
                    Places.initialize(getApplicationContext(), getResources().getString(R.string.map_key));
                    List<Place.Field> field = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                    Intent intent2 = new Autocomplete.IntentBuilder(
                            AutocompleteActivityMode.FULLSCREEN, field)
                            // .setTypeFilter(TypeFilter.ADDRESS)
                            .build(getApplicationContext());
                    startActivityForResult(intent2, 3);
                }

            }
        });
    }

    private void hitRoute() {
        v2GetRouteDirection = new GMapV2GetRoute();
        GetRouteTask getRoute = new GetRouteTask();
        getRoute.execute();

    }

    private class GetRouteTask extends AsyncTask<String, Void, String> {
        String response = "";

        @Override
        protected String doInBackground(String... urls) {
            //Get All Route values
            source = new LatLng(latitude, longitude);
            destination = new LatLng(dest_latitude, dest_longitude);
            //  LatLng user = new LatLng(30.7105, 76.7033);
            // LatLng other = new LatLng(30.7585, 76.6626);

            document = v2GetRouteDirection.getDocument(source, destination, GMapV2GetRoute.MODE_DRIVING);
            response = "Success";
            return response;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("response", result);
            if (response.equalsIgnoreCase("Success")) {
                ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(document);

                if (directionPoint.size() != 0) {
                    PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.parseColor("#000000"));
                    for (int i = 0; i < directionPoint.size(); i++) {
                        rectLine.add(directionPoint.get(i));
                    }
                    mMap.addPolyline(rectLine);
                    int distance = Integer.valueOf(v2GetRouteDirection.getDistanceValue(document));
                    time = v2GetRouteDirection.getDurationText(document);
                    Log.e("distance", String.valueOf(distance));
                    if (distance>=20000){
                        mMap.animateCamera(CameraUpdateFactory.zoomIn());
                  // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(6), 2000, null);
                    }else {

                    }
                    //   mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(destination_name,time));

                    // ShowMessage(time);
                    /*  map_address="My location"+""+time;*/
                    Marker marker1 = null;
                    if (istrackIcon == true) {
                        marker1 = mMap.addMarker(new MarkerOptions()
                                .position(destination)
                                .title(destination_name)
                                .snippet(time)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_large)));
                    } else if (isStart_trak) {
                        destination_name = v2GetRouteDirection.getEndAddress(document);
                        marker1 = mMap.addMarker(new MarkerOptions()
                                .position(destination)
                                .title(destination_name)
                                .snippet(time));
                    } else {
                        marker1 = mMap.addMarker(new MarkerOptions()
                                .position(destination)
                                .title(destination_name)
                                .snippet(time));
                    }

                    //.snippet("Snippet")
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

                    marker1.showInfoWindow();
                } else {
                    ShowMessage("Root not found");
                }
            }
        }
    }

    ////////////////// create custom info window=====
    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;
        String time;

        MyInfoWindowAdapter(String source_name, String time) {
            myContentsView = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
            this.time = time;

        }

        @Override
        public View getInfoWindow(Marker marker) {

            String time_total = marker.getSnippet();

            tvTitle = myContentsView.findViewById(R.id.txt_maptitle);
            txt_time = myContentsView.findViewById(R.id.txt_time);
            tvTitle.setText(marker.getTitle());
            if (time_total != null) {
                myContentsView.setVisibility(View.VISIBLE);
                if (time_total.equalsIgnoreCase("")) {
                    txt_time.setVisibility(View.GONE);
                } else {
                    txt_time.setVisibility(View.VISIBLE);
                    String[] addres = time_total.split(" ");
                    String name = addres[0];
                    // String city=addres[1];
                    //    txt_time.setText(name+" "+"Min");
                    txt_time.setText(time_total);
                }
                return myContentsView;
            }
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    /////////////owner section api
    public void accept_reject(String id, final int i, final String action_id) {
        appUtils.showDialog(this);
       // appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseRequestcar> call = service.requestAction(id, action_id, Constants.accesstoken);

        call.enqueue(new Callback<ServerResponseRequestcar>() {
            @Override
            public void onResponse(Call<ServerResponseRequestcar> call, Response<ServerResponseRequestcar> response) {
                ServerResponseRequestcar data = response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")) {

                    if (action_id.equalsIgnoreCase("0")) {
                        requestlist.remove(i);
                        adapter.notifyDataSetChanged();
                        txt_dashbord.setText("You have " + requestlist.size() + " new request");
                        ShowMessage(data.getMessage());
                    } else {
                        ShowMessage(data.getMessage());
                        requestlist.remove(i);
                        adapter.notifyDataSetChanged();
                        txt_dashbord.setText("You have " + requestlist.size() + " new request");
                    }

                } else {
                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseRequestcar> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error", t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

    /* private void openAlertDailog_type() {
         dialog = new BottomSheetDialog(this);
         LayoutInflater inflater1 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View dialogView = inflater1.inflate(R.layout.bottom_sheet_homedailog, null);
         // this is set the view from XML inside AlertDialog
         //builder.setView(dialogView);
         dialog.setContentView(dialogView);
         dialog.setCancelable(true);

       *//*  booking_type = this.getResources().obtainTypedArray(R.array.rydeup_type_icon);
        booking_titles= Arrays.asList(this.getResources().getStringArray(R.array.rydeup_type));*//*
        bookinglist=new ArrayList<>();

        btn_conform=dialogView.findViewById(R.id.btn_conform);
        txt_cash=dialogView.findViewById(R.id.txt_cash);
        rv_rydeup_type=dialogView.findViewById(R.id.rv_rydeup_type);
        rv_rydeup_type.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
       //  rv_rydeup_type.setAdapter(new RVBookTypeAdapter(this,booking_titles,booking_type));

        btn_conform.setOnClickListener(this);
        txt_cash.setOnClickListener(this);

        String mlongitude=String.valueOf(longitude);
        String mlatitude=String.valueOf(latitude);
        if (appUtils.isInternetConnected()){
            hit_booktypeApi(mlatitude,mlongitude);
        }else {
            ShowMessage("Please check internet conection");

        }

        dialog.show();
    }*/
    private void openAlertDailog_contact() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.custome_call_dailog, null);
        builder.setView(dialogView);
        builder.setCancelable(true);
        // Get the custom alert dialog view widgets reference
        img_call = dialogView.findViewById(R.id.img_call);
        img_sms = dialogView.findViewById(R.id.img_sms);
        img_call.setOnClickListener(this);
        img_sms.setOnClickListener(this);
        // create and show the alert dialog
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openAlertDailog_cash() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        View view = LayoutInflater.from(this).inflate(R.layout.payment_alert_dialog, null);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        dialog.setContentView(view);
        //dialog.getWindow().setGravity(Gravity.START);
        dialog.getWindow().setWindowAnimations(R.style.DialogTheme);
        dialog.setCancelable(true);
        ImageView img_back = dialog.findViewById(R.id.img_back);
        cardlist = new ArrayList<>();
        TextView txt_add_option = dialog.findViewById(R.id.txt_add_option);
        txt_sectect_cash=dialog.findViewById(R.id.txt_sectect_cash);
        rv_cardlist = dialog.findViewById(R.id.rv_cardlist);
        rv_cardlist.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
        isPayment = true;
        this.dialog=dialog;

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txt_add_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ShowMessage("click here");
                openPayment_dailog();
            }
        });

        txt_sectect_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_cash.setVisibility(View.VISIBLE);
                txt_sectect_cash.setBackgroundColor(Color.parseColor("#A4A4D0F3"));
                txt_card_no.setVisibility(View.GONE);
                img_card.setVisibility(View.GONE);
                paymentMode="cash";
                dialog.dismiss();
            }
        });

        /////////////////hit get card api
        if (appUtils.isInternetConnected()) {
            hit_getcard(dialog);
        } else {
            ShowMessage("Please check your Internet connection");
        }
        dialog.show();
    }

    private void openPayment_dailog() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        View view = LayoutInflater.from(this).inflate(R.layout.add_payment_dialog, null);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        dialog.setContentView(view);
        //  dialog.getWindow().setGravity(Gravity.START);
        dialog.getWindow().setWindowAnimations(R.style.DialogTheme);
        dialog.setCancelable(true);

        TextView txt_credit = dialog.findViewById(R.id.txt_credit);
        ImageView img_back = dialog.findViewById(R.id.img_back);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txt_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   ShowMessage("pending strip payment getway");
                intent = new Intent(HomeActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }

    private void totalPayment_dailog() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        View view = LayoutInflater.from(this).inflate(R.layout.total_payment_dialog, null);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        dialog.setContentView(view);
        //  dialog.getWindow().setGravity(Gravity.START);
        dialog.getWindow().setWindowAnimations(R.style.DialogTheme);
        dialog.setCancelable(true);

        TextView txt_card_payment = dialog.findViewById(R.id.txt_card_payment);
        ImageView img_back = dialog.findViewById(R.id.img_back);
        ImageView img_card_payment=dialog.findViewById(R.id.img_card_payment);
        EditText edt_cvc=dialog.findViewById(R.id.edt_cvc);
        EditText edt_total_amount=dialog.findViewById(R.id.edt_total_amount);
        Button btn_pay=dialog.findViewById(R.id.btn_pay);

        String mask = card_no.replaceAll("\\w(?=\\w{4})", "*");
        txt_card_payment.setText(mask);
        if (card_image!=null){
            Picasso.with(homeActivity).load(card_image).placeholder(R.mipmap.credit_card).into(img_card_payment);
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  ShowMessage("pending strip payment getway");

            }
        });

        dialog.show();
    }

    private void hit_getcard(final Dialog dialog) {
        appUtils.showDialog(this);
     //   appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseGetCard> call = service.getCards(login_id, Constants.accesstoken);
        call.enqueue(new Callback<ServerResponseGetCard>() {
            @Override
            public void onResponse(Call<ServerResponseGetCard> call, Response<ServerResponseGetCard> response) {
                ServerResponseGetCard data = response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")) {
                    cardlist = data.getData();
                    rvGetCardAdapter = new RVGetCardAdapter(HomeActivity.this, cardlist,dialog);
                    rv_cardlist.setAdapter(rvGetCardAdapter);
                } else {
                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseGetCard> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error", t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

    private void hit_booktypeApi(String mlatitude, String mlongitude, String desti_long, String desti_lati) {
        appUtils.showDialog(this);
      //  appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseNearby> call = service.getNearByVehicles(Constants.accesstoken,mlongitude,mlatitude,desti_long,desti_lati);
        call.enqueue(new Callback<ServerResponseNearby>() {
            @Override
            public void onResponse(Call<ServerResponseNearby> call, Response<ServerResponseNearby> response) {
                ServerResponseNearby data = response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")) {
                    bookinglist.clear();
                    if (data.getData().size() != 0) {
                        bookinglist = data.getData();
                    } else {
                        ShowMessage(data.getMessage());
                    }

                } else {
                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseNearby> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error", t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

    private void hit_requestApi(String categoryid, String src_long, String src_lat, String dest_long, String dest_lati) {
        appUtils.showDialog(this);
       // appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseUpdate> call = service.reservationRequest(login_id,categoryid, source_name, destination_name, src_long, src_lat, dest_long, dest_lati,Constants.accesstoken,paymentMode);

        call.enqueue(new Callback<ServerResponseUpdate>() {
            @Override
            public void onResponse(Call<ServerResponseUpdate> call, Response<ServerResponseUpdate> response) {
                ServerResponseUpdate data = response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")) {
                    // ShowMessage(data.getMessage());
                    //  dialog.dismiss();
                    isVisibilty = false;
                    rl_default_layout.setVisibility(View.GONE);
                    rl_type.setVisibility(View.GONE);
                    mMap.clear();
                    rl_anim.setVisibility(View.VISIBLE);
                    content_ripp.startRippleAnimation();
                } else {
                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseUpdate> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error", t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

    public void logout() {
        appUtils.showDialog(this);
        // appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseUpdate> call = service.logout(login_id, Constants.accesstoken);
        call.enqueue(new Callback<ServerResponseUpdate>() {
            @Override
            public void onResponse(Call<ServerResponseUpdate> call, Response<ServerResponseUpdate> response) {
                ServerResponseUpdate data = response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")) {
                    editor = preference.edit();
                    editor.clear();
                    editor.commit();
                    /////////temp data clear
                    editor = tempprefrence.edit();
                    editor.clear();
                    editor.commit();

                    intent = new Intent(homeActivity, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    //  ShowMessage("User logout successfully");
                } else {
                    ShowMessage(data.getMessage());

                }
            }

            @Override
            public void onFailure(Call<ServerResponseUpdate> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error", t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

    ///////// create broadcast receiver class==================================
    public class MyReceiver extends BroadcastReceiver {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {

            stauts = intent.getStringExtra("status");
            request_id = intent.getStringExtra("reqId");
            tempprefrence = getSharedPreferences("temp", Context.MODE_PRIVATE);
            editor = tempprefrence.edit();
            editor.putString("stauts",stauts);
            editor.putString("request_id",request_id);
            editor.commit();

            txt_count.setText(intent.getStringExtra("count"));
            if (stauts.equalsIgnoreCase("2")) {
                // Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
                // hit driver detais api
                rl_anim.setVisibility(View.GONE);
                rl_bottom.setVisibility(View.VISIBLE);
                animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_up);
                rl_bottom.setAnimation(animation);

                Hit_driveDetails(request_id);
                rl_default_layout.setVisibility(View.GONE);
            } else if (stauts.equalsIgnoreCase("3")) {
                rl_bottom.setVisibility(View.GONE);
                istrackIcon = false;
                isStart_trak = true;
                source_name=tempprefrence.getString("souce_name","");
                dest_longitude = trak_long;
                dest_latitude = trak_lat;
                mMap.clear();
                mapFragment.getMapAsync(HomeActivity.this);
                hitRoute();

            } else if (stauts.equalsIgnoreCase("4")) {

                if(paymentMode.equalsIgnoreCase("card")){
                    totalPayment_dailog();
                }else {
                    driver_image=tempprefrence.getString("image","");
                    trip_price=tempprefrence.getString("price","");
                    driver_name=tempprefrence.getString("name","");
                    isStart_trak = false;
                    mMap.clear();
                    rl_default_layout.setVisibility(View.GONE);
                    rl_complete.setVisibility(View.VISIBLE);
                    toolbar_title.setText("Ended Trip");
                    animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_up);
                    rl_complete.setAnimation(animation);
                    txt_trip_price.setText("$" + trip_price);
                    txt_driver.setText(driver_name);
                    if (driver_image != null &&!driver_image.equalsIgnoreCase("")) {
                        Picasso.with(getApplicationContext()).load(driver_image).placeholder(R.mipmap.default_user).into(img_driver_profile);
                    }

                }

            }else if(stauts.equalsIgnoreCase("7")){
                rl_anim.setVisibility(View.GONE);
                rl_default_layout.setVisibility(View.VISIBLE);
                animation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_in_top);
                rl_default_layout.setAnimation(animation);
                edt_from_loc.setText("");
                isVisibilty = false;
                mMap.clear();
                getCurrentlocation();
                mapFragment.getMapAsync(HomeActivity.this);

            }else {

            }

        }
    }

    private void Hit_driveDetails(String request_id) {
        appUtils.showDialog(this);
        //  appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseDriverDetails> call = service.reservationDetail(request_id, Constants.accesstoken);

        call.enqueue(new Callback<ServerResponseDriverDetails>() {
            @Override
            public void onResponse(Call<ServerResponseDriverDetails> call, Response<ServerResponseDriverDetails> response) {
                ServerResponseDriverDetails data = response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")) {
                    txt_driver_name.setText(data.getData().getFirstname());
                    txt_plat_info.setText("Vehicle no: " + data.getData().getVehicle_number());
                    txt_car_name.setText("Vehicle name: " + data.getData().getVehicle_name());
                    driver_id = data.getData().getDriverID();
                    phone_number = data.getData().getContact_no();
                    driver_name = data.getData().getFirstname();
                    driver_image = data.getData().getImage();
                    driver_token = data.getData().getDriver_token();
                    device_type=data.getData().getDevice_type();
                    source_name=data.getData().getFrom_location();
                    destination_name=data.getData().getTo_location();
                    trak_long =Double.valueOf(data.getData().getDest_long()) ;
                    trak_lat = Double.valueOf(data.getData().getDest_lati()) ;
                    latitude=Double.valueOf(data.getData().getSrc_lati());
                    longitude=Double.valueOf(data.getData().getSrc_long());

                    String url = data.getData().getImage();
                    if (!url.equalsIgnoreCase("")) {
                        Picasso.with(getApplicationContext()).load(url).placeholder(R.mipmap.default_user).into(img_driver);
                    }

                    tempprefrence = getSharedPreferences("temp", Context.MODE_PRIVATE);
                    editor = tempprefrence.edit();
                    editor.putString("src_lat", data.getData().getSrc_lati());
                    editor.putString("src_long",data.getData().getSrc_long());
                    editor.putString("desti_lat", data.getData().getDest_lati());
                    editor.putString("desti_long",data.getData().getDest_long());
                    editor.putString("souce_name",source_name);
                    editor.putString("desti_name",destination_name);
                    editor.putString("image",driver_image);
                    editor.putString("name",driver_name);
                   // editor.putString("device_type",device_type);
                    editor.commit();

                    dest_latitude = Double.valueOf(data.getData().getLatitude());
                    dest_longitude = Double.valueOf(data.getData().getLongitude());

                    String address = data.getData().getDriver_current_location();
                    String[] addres = address.split(",");
                    String name = addres[0];
                    String city = "";
                  /*  if (addres[1] != null) {
                        city = addres[1];
                    } else {
                        city = addres[0];
                    }*/

                    destination_name = name;
                    //////////create driver path
                    mMap.clear();
                    hitRoute();
                    mapFragment.getMapAsync(HomeActivity.this);

                    isDriverTrack = true;
                    istrackIcon = true;

                } else {
                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseDriverDetails> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error", t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

    private void Hit_trackDriver() {
        appUtils.showDialog(this);
        //  appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseTrackDriver> call = service.trackDriverLocation(request_id, Constants.accesstoken);

        call.enqueue(new Callback<ServerResponseTrackDriver>() {
            @Override
            public void onResponse(Call<ServerResponseTrackDriver> call, Response<ServerResponseTrackDriver> response) {
                ServerResponseTrackDriver data = response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")) {
                    Double latitude = Double.valueOf(data.getData().getDriver_latitude());
                    Double longitude = Double.valueOf(data.getData().getDriver_longitude());
                    String time = data.getData().getDurationtime();
                    LatLng nearbuy = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(nearbuy).snippet(time).title("away")
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_large)));
                } else {
                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseTrackDriver> call, Throwable t) {
                //ShowMessage("Try again");
                Log.e("error", t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

    private void hit_rateApi(String rid, String feedback, String rating) {
        appUtils.showDialog(this);
      //  appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseRequestcar> call = service.addDriverRating(rid, login_id, rating, feedback, Constants.accesstoken);

        call.enqueue(new Callback<ServerResponseRequestcar>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<ServerResponseRequestcar> call, Response<ServerResponseRequestcar> response) {
                ServerResponseRequestcar data = response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")) {
                    toolbar_title.setText("Home");

                    rl_complete.setVisibility(View.GONE);
                    rl_default_layout.setVisibility(View.VISIBLE);
                    isVisibilty = false;
                    mMap.clear();
                    getCurrentlocation();
                    mapFragment.getMapAsync(HomeActivity.this);
                    edt_from_loc.setText("");
                    edt_feedback.setText("");
                    simpleRatingBar.setRating(0);
                    // ShowMessage(data.getMessage());
                    editor = tempprefrence.edit();
                    editor.clear();
                    editor.commit();


                } else {
                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseRequestcar> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error", t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

    private void hit_cancelApi(String request_id) {
        appUtils.showDialog(this);
      //  appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseRequestcar> call = service.cancelReservation(request_id, Constants.accesstoken);
        call.enqueue(new Callback<ServerResponseRequestcar>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<ServerResponseRequestcar> call, Response<ServerResponseRequestcar> response) {
                ServerResponseRequestcar data = response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")) {
                    editor = tempprefrence.edit();
                    editor.clear();
                    editor.commit();

                    rl_bottom.setVisibility(View.GONE);
                    rl_default_layout.setVisibility(View.VISIBLE);
                    edt_from_loc.setText("");
                    isVisibilty = false;
                    mMap.clear();
                    getCurrentlocation();
                    mapFragment.getMapAsync(HomeActivity.this);
                } else {
                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseRequestcar> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error", t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

    private void hit_noticount() {
        appUtils.showDialog(this);
       // appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseNoticount> call =service.notificationCount(login_id,Constants.accesstoken);
        call.enqueue(new Callback<ServerResponseNoticount>() {
            @Override
            public void onResponse(Call<ServerResponseNoticount> call, Response<ServerResponseNoticount> response) {
                ServerResponseNoticount data=response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")){
                     if (data.getNcount().equalsIgnoreCase("0")){
                         txt_count.setVisibility(View.INVISIBLE);
                     }else {
                         txt_count.setVisibility(View.VISIBLE);
                         txt_count.setText(data.getNcount());
                     }
                }else {
                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseNoticount> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error", t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

    private void hit_notiview() {
        appUtils.showDialog(this);
       // appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseRequestcar> call=service.notificationRead(login_id, Constants.accesstoken);
        call.enqueue(new Callback<ServerResponseRequestcar>() {
            @Override
            public void onResponse(Call<ServerResponseRequestcar> call, Response<ServerResponseRequestcar> response) {
                ServerResponseRequestcar data= response.body();
                appUtils.dialog.dismiss();
                if(data.getCode().equalsIgnoreCase("200")){
                    txt_count.setVisibility(View.GONE);
                    intent = new Intent(getApplicationContext(), NavigationActivity.class);
                    intent.putExtra("type", "notification");
                    startActivity(intent);
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


    private void hit_updateStaus() {
        appUtils.showDialog(this);
        // appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponeStatusUpdate> call=service.requestStaus(request_id,login_id,Constants.accesstoken);
        call.enqueue(new Callback<ServerResponeStatusUpdate>() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<ServerResponeStatusUpdate> call, Response<ServerResponeStatusUpdate> response) {
                ServerResponeStatusUpdate data=response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")){

                   stauts=data.getData().getRequest_status();
                   request_id=data.getData().getRequest_id();
                    if(request_id.equalsIgnoreCase("")){

                    }else {
                        //rl_default_layout.setVisibility(View.GONE);
                        if (isVisibilty==false){
                            rl_default_layout.setVisibility(View.VISIBLE);
                        }else {
                            rl_default_layout.setVisibility(View.GONE);
                        }
                        if (stauts.equalsIgnoreCase("2")) {
                            // Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
                            // hit driver detais api
                            rl_anim.setVisibility(View.GONE);
                            rl_bottom.setVisibility(View.VISIBLE);
                            animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_up);
                            rl_bottom.setAnimation(animation);

                            Hit_driveDetails(request_id);
                            rl_default_layout.setVisibility(View.GONE);
                        } else if (stauts.equalsIgnoreCase("3")) {
                            rl_default_layout.setVisibility(View.GONE);
                            rl_bottom.setVisibility(View.GONE);
                            istrackIcon = false;
                            isStart_trak = true;
                            source_name=tempprefrence.getString("souce_name","");
                            destination_name=tempprefrence.getString("desti_name","");
                            Double src_lat=Double.valueOf(tempprefrence.getString("src_lat","0.0"));
                            Double src_long=Double.valueOf(tempprefrence.getString("src_long","0.0"));
                            Double desti_lat=Double.valueOf(tempprefrence.getString("desti_lat","0.0"));
                            Double desti_long=Double.valueOf(tempprefrence.getString("desti_long","0.0"));

                            latitude=src_lat;
                            longitude=src_long;
                            dest_longitude =desti_long;
                            dest_latitude = desti_lat;
                             mMap.clear();
                             mapFragment.getMapAsync(HomeActivity.this);
                            hitRoute();

                        } else if (stauts.equalsIgnoreCase("4")) {

                            if(paymentMode.equalsIgnoreCase("card")){
                                totalPayment_dailog();
                            }else {
                                driver_image=tempprefrence.getString("image","");
                                trip_price=tempprefrence.getString("price","");
                                driver_name=tempprefrence.getString("name","");

                                isStart_trak = false;
                                rl_default_layout.setVisibility(View.GONE);
                                rl_complete.setVisibility(View.VISIBLE);
                                toolbar_title.setText("Ended Trip");
                                animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_up);
                                rl_complete.setAnimation(animation);
                                txt_trip_price.setText("$" + trip_price);
                                txt_driver.setText(driver_name);
                                if (driver_image != null && !driver_image.equalsIgnoreCase("")) {
                                    Picasso.with(getApplicationContext()).load(driver_image).placeholder(R.mipmap.default_user).into(img_driver_profile);
                                }
                            }

                        }else if(stauts.equalsIgnoreCase("7")){
                            rl_anim.setVisibility(View.GONE);
                            rl_default_layout.setVisibility(View.VISIBLE);
                            animation = AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.slide_in_top);
                            rl_default_layout.setAnimation(animation);
                            edt_from_loc.setText("");
                            isVisibilty = false;
                            mMap.clear();
                            getCurrentlocation();
                            mapFragment.getMapAsync(HomeActivity.this);

                        } else {

                        }
                    }

                }else {

                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponeStatusUpdate> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error",t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

}
