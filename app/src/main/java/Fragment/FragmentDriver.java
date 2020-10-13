package Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.e.ryedup.HomeActivity;
import com.e.ryedup.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;

public class FragmentDriver extends Fragment implements View.OnClickListener {
    CircleImageView img_profile;
    EditText edt_name,edt_location,edt_service_type,edt_payment_mode;
    TextView edt_license;
    Button btn_submit;
    String name="",location="",licence="",service_type="",payment_mode="";
    public File finalFile;
    Bitmap bitmap;
    MultipartBody.Part image;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_driver,container,false);
        BindUI(view);
        btn_submit.setOnClickListener(this);
        edt_license.setOnClickListener(this);
        return view;
    }

    private void BindUI(View view) {
        img_profile=view.findViewById(R.id.img_profile);
        edt_name=view.findViewById(R.id.edt_name);
        edt_license=view.findViewById(R.id.edt_license);
        edt_location=view.findViewById(R.id.edt_location);
        edt_service_type=view.findViewById(R.id.edt_service_type);
        edt_payment_mode=view.findViewById(R.id.edt_payment_mode);
        btn_submit=view.findViewById(R.id.btn_submit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                if(Validation()){
                    getActivity().finish();
                }
                break;
            case R.id.edt_license:
                checkPermissions();
                break;
        }
    }
    private boolean Validation() {
        name=edt_name.getText().toString().trim();
        licence=edt_license.getText().toString().trim();
        location=edt_location.getText().toString().trim();
        service_type=edt_service_type.getText().toString().trim();
        payment_mode=edt_payment_mode.getText().toString().trim();

        if(name.isEmpty()){
            ShowMessage("Please enter Name");
            return false;
        }else if (licence.isEmpty()){
            ShowMessage("Please upload Licence file");
            return false;
        }else if(location.isEmpty()){
            ShowMessage("Please enter Location Address");
            return false;
        }else if(service_type.isEmpty()){
            ShowMessage("Please enter Service type");
            return false;
        }else if(payment_mode.isEmpty()){
            ShowMessage("Please enter Payment mode");
            return false;
        }
        return true;
    }
    private void ShowMessage(String sms) {
        Toast.makeText(getActivity(),sms,Toast.LENGTH_LONG).show();
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (HomeActivity.homeActivity.getApplicationContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && HomeActivity.homeActivity.getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                    && HomeActivity.homeActivity.getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE )!=PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return;
            } else {
                take_license();
            }
        } else
            take_license();
    }

    private void take_license() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Upload license!");
        builder.setCancelable(false);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");
                    // pickIntent.setType("*/*");
                    startActivityForResult(pickIntent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }
    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode", "" + resultCode + requestCode + data);
        if (resultCode == getActivity().RESULT_OK) {

            // requestCode == 1
            //  requestCode == 2
            if (requestCode == 1) {
                try {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    // imagePicker.setImageBitmap(photo);
                    Uri tempUri = getImageUri(getActivity(), bitmap);
                    //compressImage((String) data.getExtras().get("data"));
                    finalFile = new File(getRealPathFromURI(tempUri));
                    //img_url = tempUri;
                    edt_license.setText(finalFile.getName());
                    // img_profile.setImageURI(tempUri);
                    //  Log.e("from_uri", "" + img_url);
                } catch (Exception e) {
                    Log.e("from_signup", e.toString());
                }


            } else if (requestCode == 2) {
                Uri selectedImage1 = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage1, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String Path = c.getString(columnIndex);
                finalFile = new File(Path);
                bitmap = (BitmapFactory.decodeFile(Path));
                // img_profile.setVisibility(View.VISIBLE);
                //  img_url = selectedImage1;
                edt_license.setText(finalFile.getName());
                // img_profile.setImageBitmap(bitmap);
                //img_postImage.setImageURI(img_url);
                c.close();

            }
        }
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
