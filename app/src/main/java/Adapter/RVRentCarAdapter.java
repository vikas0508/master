package Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.e.ryedup.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import Fragment.FragmentCarDetails;
import Model.ServerResponseCarRent;

public class RVRentCarAdapter extends RecyclerView.Adapter<RVRentCarAdapter.RVRentHolder> {
    FragmentActivity activity;
    List<ServerResponseCarRent.Data> carRentlist;
    String licences_verify;
    public RVRentCarAdapter(FragmentActivity activity, List<ServerResponseCarRent.Data> carRentlist, String licences_verify) {
        this.activity=activity;
        this.carRentlist=carRentlist;
        this.licences_verify=licences_verify;
    }

    @NonNull
    @Override
    public RVRentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_rentcar_adapter,viewGroup,false);

        return new RVRentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVRentHolder rvRentHolder, final int i) {
        Transformation transformation = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {
                int targetWidth = 100;

                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    // Same bitmap is returned if sizes are the same
                    source.recycle();
                }
                return result;
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };


        rvRentHolder.txt_car_size.setText(carRentlist.get(i).getVehicle_type());
        rvRentHolder.txt_price.setText("Price: $"+carRentlist.get(i).getVehicle_price());
        rvRentHolder.txt_location.setText(carRentlist.get(i).getVehicle_location());
        rvRentHolder.txt_rent_type.setText(carRentlist.get(i).getRent_type());
        rvRentHolder.txt_mils_car.setText(carRentlist.get(i).getMiles()+" "+"miles");

        if (carRentlist.get(i).getAvailability().equalsIgnoreCase("0")){
            rvRentHolder.txt_avilable.setText("No");
        }else {
            rvRentHolder.txt_avilable.setText("Yes");
        }

        String url=carRentlist.get(i).getVehicle_image();
        if (!url.equalsIgnoreCase("")){
            Picasso.with(activity).load(url).transform(transformation).placeholder(R.mipmap.photo_car).into(rvRentHolder.img_car_image);
        }

        rvRentHolder.ll_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (licences_verify.equalsIgnoreCase("0")){
                      alertdoilog();
                }else {
                    String id=carRentlist.get(i).getId();
                    /*Intent intent=new Intent(activity, NavigationActivity.class);
                    intent.putExtra("type","car details");
                    intent.putExtra("id",id);
                    activity.startActivity(intent);*/
                    Fragment fragment= new FragmentCarDetails();
                    Bundle  bundle=new Bundle();
                    bundle.putString("id",id);
                    fragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack("").commit();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return carRentlist.size();
       // return 6;
    }

    public class RVRentHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_layout;
        ImageView img_car_image;
        TextView txt_car_size,txt_location,txt_price,txt_avilable,txt_rent_type,txt_mils_car;
        public RVRentHolder(@NonNull View itemView) {
            super(itemView);
            ll_layout=itemView.findViewById(R.id.ll_layout);
           // btn_select=itemView.findViewById(R.id.btn_select);
            img_car_image=itemView.findViewById(R.id.img_car_image);
            txt_car_size=itemView.findViewById(R.id.txt_car_size);
            txt_avilable=itemView.findViewById(R.id.txt_avilable);
            txt_price=itemView.findViewById(R.id.txt_price);
            txt_location=itemView.findViewById(R.id.txt_location);
            txt_rent_type=itemView.findViewById(R.id.txt_rent_type);
            txt_mils_car=itemView.findViewById(R.id.txt_mils_car);

        }
    }

    private void alertdoilog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        // Get the custom alert dialog view widgets reference
        TextView txt_msg =dialogView.findViewById(R.id.txtMessage);

        TextView txt_ok = dialogView.findViewById(R.id.txtOK);
        txt_ok.setVisibility(View.GONE);
        TextView txt_cancel = dialogView.findViewById(R.id.txtCancel);
        txt_cancel.setText("Cancel");
        txt_msg.setText("Your license has not been approved yet.!");
        // create and show the alert dialog
        final AlertDialog dialog = builder.create();
        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hit delete vehicle api
                dialog.dismiss();
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
