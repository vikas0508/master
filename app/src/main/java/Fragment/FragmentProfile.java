package Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.e.ryedup.ImagePickerActivity;
import com.e.ryedup.R;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import Model.ServerResponseProfile;
import Service.Interface;
import Utils.AppUtils;
import Utils.Constants;
import Utils.RetrofitClient;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProfile extends Fragment implements View.OnClickListener {
    CircleImageView img_profile;
    TextView txt_name,txt_licence;
    EditText edt_phone,edt_location,edt_card,edt_unlock,edt_L_name,edt_F_name;
    Button btn_save;
    RelativeLayout rl_edit_profile;
    String phone="",location="",card="",unlock="",login_id="",user_id="",name,lastname="",img_url ="";
    AppUtils appUtils;
    SharedPreferences preference;
    SharedPreferences.Editor editor;
    public File finalFile,lincenceFile;
    Bitmap bitmap;
    MultipartBody.Part image,lincence;
    final int PIC_CROP = 100;
    public static final int REQUEST_IMAGE = 100;
    private Uri mCropImageUri;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile,container,false);
        BindUI(view);
        preference = getActivity().getSharedPreferences(Constants.Shared_Pref, Context.MODE_PRIVATE);
        login_id=preference.getString("loginid","");
        user_id=preference.getString("user_id","");
        appUtils=new AppUtils(getActivity());
        if (appUtils.isInternetConnected()){
            HitpofileApi();
        }else {
            ShowMessage("Please check your Internet connection");
        }

        btn_save.setOnClickListener(this);
        rl_edit_profile.setOnClickListener(this);
        txt_licence.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.rl_edit_profile:
               onProfileImageClick();
               //ShowMessage("Click here");

               break;
           case R.id.btn_save:
               if(Validation()){
                   if (appUtils.isInternetConnected()){
                        Hitedit_pofileApi();
                   }else {
                       ShowMessage("Please check your Internet connection");
                   }
               }
               break;
           case R.id.txt_licence:
               take_licences();
               break;
       }
    }

    private void Hitedit_pofileApi() {
        appUtils.showDialog(getActivity());
        appUtils.dialog.show();
        final Interface service = RetrofitClient.getClient().create(Interface.class);

        if(null==finalFile){

        }else {
          //  Log.e("file",img_url);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), finalFile);
            image = MultipartBody.Part.createFormData("cust_img_name", finalFile.getName(), requestFile);
        }

        if(null==lincenceFile){

        }else {
            //  Log.e("file",img_url);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), lincenceFile);
            lincence = MultipartBody.Part.createFormData("driving_licence", lincenceFile.getName(), requestFile);
        }

        RequestBody C_name = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody C_email = RequestBody.create(MediaType.parse("text/plain"),"");
        RequestBody C_contact = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody C_lastname = RequestBody.create(MediaType.parse("text/plain"),lastname);
        RequestBody C_gender = RequestBody.create(MediaType.parse("text/plain"),"");
        RequestBody C_address = RequestBody.create(MediaType.parse("text/plain"),location);
        RequestBody C_cid = RequestBody.create(MediaType.parse("text/plain"),login_id);
        RequestBody C_uid = RequestBody.create(MediaType.parse("text/plain"),user_id);
        RequestBody C_card = RequestBody.create(MediaType.parse("text/plain"),card);
        RequestBody C_paymentmode = RequestBody.create(MediaType.parse("text/plain"),"");
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), Constants.accesstoken);
        Call<ServerResponseProfile> call= service.update_profle(C_name,C_email,C_contact,C_lastname,C_gender,C_address,C_cid,C_uid,C_card,C_paymentmode,token,image,lincence);

        call.enqueue(new Callback<ServerResponseProfile>() {
          @Override
          public void onResponse(Call<ServerResponseProfile> call, Response<ServerResponseProfile> response) {
              ServerResponseProfile data=response.body();
              appUtils.dialog.dismiss();
              if (data.getCode().equalsIgnoreCase("200")){

                  editor = preference.edit();
                  if (data.getData()!=null ){
                      editor.putString("firstname",data.getData().getFirstname());
                      editor.putString("lastname",data.getData().getLastname());
                      editor.putString("address",data.getData().getAddress());
                      editor.putString("email",data.getData().getEmail());
                      editor.putString("contact",data.getData().getContact_no());
                      editor.putString("loginid",data.getData().getId());
                      editor.putString("user_id",data.getData().getLogin_id());
                      editor.putString("image",data.getData().getImage());
                      editor.putString("is_verified",data.getData().getIs_verified());
                      editor.commit();
                      ShowMessage(data.getMessage());
                      getActivity().finish();
                  }

              }else {
                  ShowMessage(data.getMessage());
              }
          }

          @Override
          public void onFailure(Call<ServerResponseProfile> call, Throwable t) {
              ShowMessage("Try again");
              Log.e("error",t.toString());
              appUtils.dialog.dismiss();
          }
      });
    }

    private void HitpofileApi() {
        appUtils.showDialog(getActivity());
        appUtils.dialog.show();
        final Interface service = RetrofitClient.getClient().create(Interface.class);
        Call<ServerResponseProfile> call=service.getcustomer_profile(login_id,Constants.accesstoken);

        call.enqueue(new Callback<ServerResponseProfile>() {
            @Override
            public void onResponse(Call<ServerResponseProfile> call, Response<ServerResponseProfile> response) {
                ServerResponseProfile data=response.body();
                appUtils.dialog.dismiss();
                if (data.getCode().equalsIgnoreCase("200")){
                     txt_name.setText(data.getData().getFirstname());
                     edt_F_name.setText(data.getData().getFirstname());
                     edt_L_name.setText(data.getData().getLastname());
                     edt_phone.setText(data.getData().getContact_no());
                     edt_location.setText(data.getData().getAddress());
                     txt_licence.setText(data.getData().getDriving_licence());
//                     edt_card.setText(data.getData().get(0).getCard_no());
                    Picasso.with(getActivity()).load(data.getData().getImage()).placeholder(R.mipmap.default_user).into(img_profile);
                   // Picasso.with(getActivity()).load(data.getData().getImage()).placeholder(R.mipmap.default_user).into(img_back);


                }else {
                    ShowMessage(data.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServerResponseProfile> call, Throwable t) {
                ShowMessage("Try again");
                Log.e("error",t.toString());
                appUtils.dialog.dismiss();
            }
        });
    }

    private boolean Validation() {
        phone=edt_phone.getText().toString().trim();
        location=edt_location.getText().toString().trim();
        lastname =edt_L_name.getText().toString().trim();
        name=edt_F_name.getText().toString().trim();
      //  unlock=edt_unlock.getText().toString().trim();
        if(name.isEmpty()){
            ShowMessage("Please enter First name");
            return false;
        }else if (lastname.isEmpty()){
            ShowMessage("Please enter Last name");
            return false;
        }else if(phone.isEmpty()){
            ShowMessage("Please enter Phone number");
            return false;
        }else if (phone.length()<6) {
            ShowMessage("Please enter a valid Phone Number");
            return false;
        }/* else if(location.isEmpty()){
            ShowMessage("Please enter Address");
            return false;
        }*/
        return true;
    }

    private void BindUI(View view) {
        img_profile=view.findViewById(R.id.img_profile);
      //  img_back=view.findViewById(R.id.img_back);
        txt_name=view.findViewById(R.id.txt_name);
        txt_licence=view.findViewById(R.id.txt_licence);
        edt_phone=view.findViewById(R.id.edt_phone);
        edt_location=view.findViewById(R.id.edt_location);
        edt_F_name=view.findViewById(R.id.edt_F_name);
        edt_L_name=view.findViewById(R.id.edt_L_name);
        btn_save=view.findViewById(R.id.btn_save);
        rl_edit_profile=view.findViewById(R.id.rl_edit_profile);
    }

    private void ShowMessage(String sms) {
        Toast.makeText(getActivity(),sms,Toast.LENGTH_LONG).show();
    }

    private void take_licences() {
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

    void onProfileImageClick() {
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

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
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

        startActivityForResult(intent, 3);
    }

    private void GalleryIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    Uri tempUri = getImageUri(getActivity(), bitmap);
                    finalFile = new File(getRealPathFromURI(tempUri));
                    img_url = tempUri.toString();
                    // loading profile image from local cache
                    img_profile.setImageURI(uri);
                   // img_back.setImageURI(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode==3){
            Uri uri = data.getParcelableExtra("path");
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                Uri tempUri = getImageUri(getActivity(), bitmap);
                lincenceFile = new File(getRealPathFromURI(tempUri));
                txt_licence.setText(lincenceFile.getName());
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
}
