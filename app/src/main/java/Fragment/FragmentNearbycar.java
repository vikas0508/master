package Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.e.ryedup.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import Model.ServerResponsehomelocation;
import Service.Interface;
import Utils.AppUtils;
import Utils.Constants;
import Utils.GPSTracker;
import Utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentNearbycar  extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    MapFragment mapFragment;
    GPSTracker gps;
    double latitude=0.0,longitude=0.0;
    LatLng source;
    String source_name="",login_id="";
    AppUtils appUtils;
    SharedPreferences preference;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_nearbycar,container,false);
        preference =getActivity().getSharedPreferences(Constants.Shared_Pref, Context.MODE_PRIVATE);
        login_id=preference.getString("loginid","");
        appUtils=new AppUtils(getActivity());
      /*  mapFragment = (SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
        mapFragment = (MapFragment)getActivity().getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getCurrentlocation();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getCurrentlocation() {
        gps = new GPSTracker(getActivity());
        if (gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.e("aaaa",""+latitude+"  "+longitude);
            Geocoder geocoder;
            Address returnAddress;
            List<Address> addresses;
            geocoder = new Geocoder(getActivity(), Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude,longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                returnAddress = addresses.get(0);
                String locality = returnAddress.getLocality();
                String name = returnAddress.getFeatureName();
                String subLocality = returnAddress.getSubLocality();
                String country = returnAddress.getCountryName();
                String region_code = returnAddress.getCountryCode();
                String zipcode = returnAddress.getPostalCode();
                String state = returnAddress.getAdminArea();

                if(locality==null && subLocality==null){
                    // map_address=locality;
                    source_name=name;
                    mapFragment.getMapAsync(this);
                }else if(subLocality==null){
                    source_name=name;
                    mapFragment.getMapAsync(this);
                } else {
                    // map_address=subLocality+","+locality;
                    source_name=subLocality+" "+locality;
                    mapFragment.getMapAsync(this);
                }


                String mlongitude=String.valueOf(longitude);
                String mlatitude=String.valueOf(latitude);

                if (appUtils.isInternetConnected()){
                    hit_upadtelocApi(mlongitude,mlatitude);
                }else {
                    ShowMessage("Please check your Internet connection");

                }

                //  edt_country.setText(country);
            } catch (IOException e) {
                Log.e("error message", e.getMessage());
                e.printStackTrace();
            }
        }else {
            gps.showSettingsAlert();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        source = new LatLng(latitude, longitude);
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(source)
                .title(source_name));
        //marker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(source,15));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());

// Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
    }

    private void hit_upadtelocApi(String mlongitude, String mlatitude) {
        appUtils.showDialog(getActivity());
        appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponsehomelocation> call=service.updateCustomerLocation(login_id,mlongitude,mlatitude,Constants.accesstoken);

        call.enqueue(new Callback<ServerResponsehomelocation>() {
            @Override
            public void onResponse(Call<ServerResponsehomelocation> call, Response<ServerResponsehomelocation> response) {
                ServerResponsehomelocation data= response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")){
                    if (data.getData().size()!=0){
                        for (int idx=0;idx<data.getData().size();idx++){
                            Double latitude= Double.valueOf(data.getData().get(idx).getLatitude());
                            Double longitude= Double.valueOf(data.getData().get(idx).getLongitude());
                            LatLng nearbuy=new LatLng(latitude,longitude);
                            mMap.addMarker(new MarkerOptions().position(nearbuy)
                           .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_large)));
                            // ShowMessage(data.getMessage());
                        }
                    }
                }else {
                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponsehomelocation> call, Throwable t) {
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
