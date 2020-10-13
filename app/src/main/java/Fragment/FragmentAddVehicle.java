package Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.e.ryedup.NavigationActivity;
import com.e.ryedup.HomeActivity;
import com.e.ryedup.ImagePickerActivity;
import com.e.ryedup.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import Adapter.CustomSpinnerAdapter;
import Adapter.SpinTypeAdapter;
import Model.ServerResponseCarDetails;
import Model.ServerResponseRequestcar;
import Model.UserCategory;
import Service.Interface;
import Utils.AppUtils;
import Utils.Constants;
import Utils.RetrofitClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAddVehicle extends Fragment implements View.OnClickListener {
    EditText edt_title,edt_company,edt_category,edt_date,edt_price,edt_phone,
            edt_rent,edt_address,edt_seet_no,edt_fule,edt_Vname,edt_V_plate_info;
    Button btn_save;
    TextView edt_upload,edt_insurance ;
    Spinner spin_category,spin_rent_type,spin_fule_type,spin_vech_type;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    String vehicle_name="", insurance="",vehicle_model="",vehicle_category="",renttype="",price="",address="",no_of_seat="",
            fule_type="",login_id="",page="",id="",v_name="",vehicle_plate_no="",vehicle_id="";
    public File finalFile,insurenceFile;
    Bitmap bitmap;
    MultipartBody.Part image,insurence;
    CustomSpinnerAdapter spinnerAdapter,rentAdapter,fueladapter;
    SpinTypeAdapter vehicleTypeadapter;
    AppUtils appUtils;
    SharedPreferences preference;
    UserCategory usercat;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_vehicle,container,false);

        BindUI(view);
        appUtils=new AppUtils(getActivity());
        preference = getActivity().getSharedPreferences(Constants.Shared_Pref, Context.MODE_PRIVATE);
       // user_type=preference.getString("user_type","");
        login_id=preference.getString("loginid","");
        page=getArguments().getString("page");
        id= getArguments().getString("id");

        if (page.equalsIgnoreCase("edit")){
            NavigationActivity.fragmentActivity.toolbar_title.setText("Edit vehicle");
            NavigationActivity.fragmentActivity.img_filter.setVisibility(View.GONE);
            if (appUtils.isInternetConnected()){
                Hit_getcarApi();
            }else {
                ShowMessage("Please check your Internet connection");
            }

        }else {

        }

         myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

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

        /// set category Spinner
        final ArrayList<String> Typelist = new ArrayList<>();
        Typelist.add("Suv");
        Typelist.add("Compact");
        Typelist.add("Mid-size");
     //   Typelist.add("Van");
        Typelist.add("Luxury");
        Typelist.add("Premium");
        spinnerAdapter = new CustomSpinnerAdapter(getActivity(), Typelist);
        spin_category.setAdapter(spinnerAdapter);
        //spin_category.setSelection(0, false);

        spin_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vehicle_category = Typelist.get(i);
                edt_category.setText(vehicle_category);
                //idea_value = ideaTypelist.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                vehicle_category = Typelist.get(0);
                edt_category.setText(vehicle_category);
                // idea_value = ideaTypelist.get(0).getId();
            }
        });
    /// set renttype Spinner

        final ArrayList<String> rentlist = new ArrayList<>();
        rentlist.add("Hourly");
        rentlist.add("Daily");
        rentlist.add("Weekly");
        rentAdapter = new CustomSpinnerAdapter(getActivity(), rentlist);
        spin_rent_type.setAdapter(rentAdapter);
       // spin_rent_type.setSelection(0, false);

        spin_rent_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                renttype = rentlist.get(i);
                edt_rent.setText(renttype);
                //idea_value = ideaTypelist.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                renttype = rentlist.get(0);
                edt_rent.setText(renttype);
                // idea_value = ideaTypelist.get(0).getId();
            }
        });

        /// set vehicle type Spinner
        final ArrayList<UserCategory> vehiclelist = new ArrayList<>();
        usercat = new UserCategory();
        usercat.setId("5");
        usercat.setValue("car");
        vehiclelist.add(0, usercat);

       /* usercat = new UserCategory();
        usercat.setId("6");
        usercat.setValue("Bike");
        vehiclelist.add(0, usercat);

        usercat = new UserCategory();
        usercat.setId("7");
        usercat.setValue("Boat");
        vehiclelist.add(0, usercat);*/

        vehicleTypeadapter = new SpinTypeAdapter(getActivity(), vehiclelist);
        spin_vech_type.setAdapter(vehicleTypeadapter);
       // spin_vech_type.setSelection(0, false);
        spin_vech_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vehicle_name = vehiclelist.get(i).getValue();
                edt_title.setText(vehicle_name);
                vehicle_id = vehiclelist.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                vehicle_name = vehiclelist.get(0).getValue();
                edt_title.setText(vehicle_name);
                // vehicle_id = vehiclelist.get(i).getId();
            }
        });
        /// set fule type Spinner
        final ArrayList<String> fulelist = new ArrayList<>();
        fulelist.add("CNG");
        fulelist.add("Petrol");
        fulelist.add("Diesel ");
        fueladapter = new CustomSpinnerAdapter(getActivity(), fulelist);
        spin_fule_type.setAdapter(fueladapter);
     //   spin_fule_type.setSelection(0, false);
        spin_fule_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fule_type = fulelist.get(i);
                edt_fule.setText(fule_type);
                //idea_value = ideaTypelist.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                fule_type = fulelist.get(0);
                edt_fule.setText(fule_type);
                // idea_value = ideaTypelist.get(0).getId();
            }
        });

        //    edt_date.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        edt_category.setOnClickListener(this);
        edt_rent.setOnClickListener(this);
        edt_upload.setOnClickListener(this);
        edt_insurance.setOnClickListener(this);
        edt_title.setOnClickListener(this);
        edt_fule.setOnClickListener(this);
        return view;
    }

    private void BindUI(View view) {
        edt_title=view.findViewById(R.id.edt_title);
        edt_insurance=view.findViewById(R.id.edt_insurance);
        edt_company=view.findViewById(R.id.edt_company);
        edt_category=view.findViewById(R.id.edt_category);
        edt_date=view.findViewById(R.id.edt_date);
        edt_price=view.findViewById(R.id.edt_price);
        edt_upload=view.findViewById(R.id.edt_upload);
        edt_phone=view.findViewById(R.id.edt_phone);
        edt_rent=view.findViewById(R.id.edt_rent);
        edt_address=view.findViewById(R.id.edt_address);
        edt_seet_no=view.findViewById(R.id.edt_seet_no);
        edt_fule=view.findViewById(R.id.edt_fule);
        edt_V_plate_info=view.findViewById(R.id.edt_V_plate_info);
        edt_Vname=view.findViewById(R.id.edt_Vname);
        btn_save=view.findViewById(R.id.btn_save);
        spin_rent_type=view.findViewById(R.id.spin_rent_type);
        spin_category=view.findViewById(R.id.spin_category);
        spin_fule_type=view.findViewById(R.id.spin_fule_type);
        spin_vech_type=view.findViewById(R.id.spin_vech_type);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edt_date:
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.btn_save:
                if (validation()){
                    if (appUtils.isInternetConnected()){
                        if (page.equalsIgnoreCase("edit")){
                            Hit_updatecarApi();
                        }else {
                            Hit_addcarApi();
                        }

                    }else {
                        ShowMessage("Please check your Internet connection");
                    }
                }
                break;
            case R.id.edt_category:
                   spin_category.setVisibility(View.VISIBLE);
                   spin_category.performClick();

                break;
            case R.id.edt_rent:
                   spin_rent_type.setVisibility(View.VISIBLE);
                   spin_rent_type.performClick();
                break;
            case R.id.edt_upload:
                take_Picture();
                break;
            case R.id.edt_insurance:
                checkPermissions();
                break;

            case R.id.edt_title:
                spin_vech_type.setVisibility(View.VISIBLE);
                spin_vech_type.performClick();

                break;
            case R.id.edt_fule:
                spin_fule_type.setVisibility(View.VISIBLE);
                spin_fule_type.performClick();
                break;
        }
    }

    private void Hit_updatecarApi() {
        appUtils.showDialog(getActivity());
        appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        if(null==finalFile){

        }else {
            //  Log.e("file",img_url);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), finalFile);
            image = MultipartBody.Part.createFormData("VehicleImage", finalFile.getName(), requestFile);
        }

        if (null == insurence) {

        }else {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), insurenceFile);
            insurence = MultipartBody.Part.createFormData("VehicleImage", insurenceFile.getName(), requestFile);
        }
        RequestBody VehicleType = RequestBody.create(MediaType.parse("text/plain"), vehicle_id);
        RequestBody ModelYear = RequestBody.create(MediaType.parse("text/plain"),vehicle_model);
        RequestBody VehicleCategory = RequestBody.create(MediaType.parse("text/plain"),vehicle_category);
        RequestBody RentType = RequestBody.create(MediaType.parse("text/plain"),renttype);
        RequestBody FuelType = RequestBody.create(MediaType.parse("text/plain"),fule_type);
        RequestBody VehiclePrice = RequestBody.create(MediaType.parse("text/plain"),price);
        RequestBody vid = RequestBody.create(MediaType.parse("text/plain"),id);
        RequestBody NumberOfSeats = RequestBody.create(MediaType.parse("text/plain"),no_of_seat);
        RequestBody vlocation = RequestBody.create(MediaType.parse("text/plain"),address);
        RequestBody vehicleName  = RequestBody.create(MediaType.parse("text/plain"),v_name);
        RequestBody vehiclenumber = RequestBody.create(MediaType.parse("text/plain"),vehicle_plate_no);
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), Constants.accesstoken);

        Call<ServerResponseRequestcar> call=service.updateVehicle(vid,VehicleType,insurence,ModelYear,VehicleCategory,RentType,FuelType,VehiclePrice,
                NumberOfSeats,vlocation,token,image,vehicleName,vehiclenumber);
        call.enqueue(new Callback<ServerResponseRequestcar>() {
            @Override
            public void onResponse(Call<ServerResponseRequestcar> call, Response<ServerResponseRequestcar> response) {
                ServerResponseRequestcar data = response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")){
                    ShowMessage(data.getMessage());
                    getActivity().finish();
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

    private void Hit_addcarApi() {
        appUtils.showDialog(getActivity());
        appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        if(null==finalFile){

        }else {
            //  Log.e("file",img_url);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), finalFile);
            image = MultipartBody.Part.createFormData("VehicleImage", finalFile.getName(), requestFile);
        }

        if (null == insurence) {

        }else {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), insurenceFile);
            insurence = MultipartBody.Part.createFormData("VehicleImage", insurenceFile.getName(), requestFile);
        }
        RequestBody VehicleType = RequestBody.create(MediaType.parse("text/plain"), vehicle_id);
        RequestBody ModelYear = RequestBody.create(MediaType.parse("text/plain"),vehicle_model);
        RequestBody VehicleCategory = RequestBody.create(MediaType.parse("text/plain"),vehicle_category);
        RequestBody RentType = RequestBody.create(MediaType.parse("text/plain"),renttype);
        RequestBody FuelType = RequestBody.create(MediaType.parse("text/plain"),fule_type);
        RequestBody VehiclePrice = RequestBody.create(MediaType.parse("text/plain"),price);
        RequestBody owner = RequestBody.create(MediaType.parse("text/plain"),login_id);
        RequestBody NumberOfSeats = RequestBody.create(MediaType.parse("text/plain"),no_of_seat);
        RequestBody vlocation = RequestBody.create(MediaType.parse("text/plain"),address);
        RequestBody vehicleName  = RequestBody.create(MediaType.parse("text/plain"),v_name);
        RequestBody vehiclenumber = RequestBody.create(MediaType.parse("text/plain"),vehicle_plate_no);
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), Constants.accesstoken);

        Call<ServerResponseRequestcar> call=service.newVehicle(owner,VehicleType,insurence,ModelYear,VehicleCategory,RentType,FuelType,VehiclePrice,
                                                                NumberOfSeats,vlocation,token,image,vehicleName,vehiclenumber);
        call.enqueue(new Callback<ServerResponseRequestcar>() {
            @Override
            public void onResponse(Call<ServerResponseRequestcar> call, Response<ServerResponseRequestcar> response) {
                ServerResponseRequestcar data = response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")){
                    ShowMessage(data.getMessage());
                    getActivity().finish();
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
    private void Hit_getcarApi() {
        appUtils.showDialog(getActivity());
        appUtils.dialog.show();
        Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseCarDetails> call=service.owner_singleVehicle(id,Constants.accesstoken);

        call.enqueue(new Callback<ServerResponseCarDetails>() {
            @Override
            public void onResponse(Call<ServerResponseCarDetails> call, Response<ServerResponseCarDetails> response) {
                ServerResponseCarDetails data=response.body();
                appUtils.dialog.dismiss();
                if(data.getCode().equalsIgnoreCase("200")){
                    edt_title.setText(data.getData().getVehicle_type());
                    edt_insurance.setText(data.getData().getVehicle_insurance());
                    edt_company.setText(data.getData().getModel_year());
                    edt_Vname.setText(data.getData().getVehicle_name());
                    edt_V_plate_info.setText(data.getData().getVehicle_number());
                    edt_category.setText(data.getData().getVehicle_category());
                    edt_rent.setText(data.getData().getRent_type());
                    edt_fule.setText(data.getData().getFuel_type());
                    edt_price.setText(data.getData().getVehicle_price());
                    edt_seet_no.setText(data.getData().getNumber_of_seats());
                    edt_address.setText(data.getData().getVehicle_location());
                    edt_upload.setText(data.getData().getVehicle_image());
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
        vehicle_name=edt_title.getText().toString().trim();
        insurance=edt_insurance.getText().toString().trim();
        vehicle_model=edt_company.getText().toString().trim();
        v_name=edt_Vname.getText().toString().trim();
        vehicle_plate_no=edt_V_plate_info.getText().toString().trim();
        vehicle_category=edt_category.getText().toString().trim();
        renttype=edt_rent.getText().toString().trim();
        fule_type=edt_fule.getText().toString().trim();
        price=edt_price.getText().toString().trim();
        no_of_seat=edt_seet_no.getText().toString().trim();
        address=edt_address.getText().toString().trim();
        String upload_image=edt_upload.getText().toString();
        if (vehicle_name.isEmpty()){
            ShowMessage("Please Select Vehicle type");
            return false;
        }else if(insurance.isEmpty()){
            ShowMessage("Please Select Vehicle insurance");
            return false;
        }else if(vehicle_model.isEmpty()){
            ShowMessage("Please enter Vehicle model");
            return false;
        }else if(v_name.isEmpty()){
            ShowMessage("Please enter Vehicle name");
            return false;
        }else if(vehicle_plate_no.isEmpty()){
            ShowMessage("Please enter Vehicle plate number");
            return false;
        }else if(vehicle_category.isEmpty()){
            ShowMessage("Please select Vehicle category");
            return false;
        }else if(renttype.isEmpty()){
            ShowMessage("Please select Vehicle rent type");
            return false;
        }else if(fule_type.isEmpty()){
            ShowMessage("Please select Vehicle fuel type");
            return false;
        }else if(price.isEmpty()){
            ShowMessage("Please enter Vehicle price");
            return false;
        }else if(no_of_seat.isEmpty()){
            ShowMessage("Please enter Number of seats");
            return false;
        }else if(address.isEmpty()){
            ShowMessage("Please enter  address");
            return false;
        }else if(upload_image.isEmpty()){
            ShowMessage("Please select Upload vehicle image");
            return false;
        }

        return true;
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        edt_date.setText(sdf.format(myCalendar.getTime()));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (HomeActivity.homeActivity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&HomeActivity.homeActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return;
            } else {
                take_insurence();
            }
        }else {
            take_insurence();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==1){
            take_insurence();
        }
    }

    private void take_insurence() {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            ImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }
    private void take_Picture() {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }
    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(getActivity(), new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, 1);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, 1);
    }

    private void ImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(getActivity(), new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                CameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                GalleryIntent();
            }
        });
    }

    private void CameraIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, 2);
    }

    private void GalleryIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, 2);
    }




    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode", "" + resultCode + requestCode + data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    Uri tempUri = getImageUri(getActivity(), bitmap);
                    finalFile = new File(getRealPathFromURI(tempUri));
                    edt_upload.setText(finalFile.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode==2){
            Uri uri = data.getParcelableExtra("path");
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                Uri tempUri = getImageUri(getActivity(), bitmap);
                insurenceFile = new File(getRealPathFromURI(tempUri));
                edt_insurance.setText(insurenceFile.getName());
                //img_url = tempUri;
                //  img_profile.setImageURI(tempUri);
                //  Log.e("from_uri", "" + img_url);
            } catch (Exception e) {
                Log.e("from_signup", e.toString());
            }
        }

    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }
    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String pathone = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        // compressImage(pathone);
        return Uri.parse(pathone);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA);
        // video_path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
        return cursor.getString(idx);

    }

    private void ShowMessage(String sms) {
        Toast.makeText(getActivity(),sms,Toast.LENGTH_LONG).show();
    }
}
