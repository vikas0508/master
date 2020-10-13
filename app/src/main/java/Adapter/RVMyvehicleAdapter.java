package Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.e.ryedup.NavigationActivity;
import com.e.ryedup.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import Model.ServerResponseCarRent;

public class RVMyvehicleAdapter extends RecyclerView.Adapter<RVMyvehicleAdapter.RVHolder> {
    FragmentActivity activity;
    List<ServerResponseCarRent.Data> carRentlist;
    OnClickSectionListener mOnClickSectionListener;
    public RVMyvehicleAdapter(FragmentActivity activity, OnClickSectionListener mOnClickSectionListener, List<ServerResponseCarRent.Data> carRentlist) {
        this.activity=activity;
        this.carRentlist=carRentlist;
        this.mOnClickSectionListener=mOnClickSectionListener;
    }

    @NonNull
    @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_myvehicle_adapter,parent,false);

        return new RVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, final int position) {

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

        holder.txt_car_size.setText(carRentlist.get(position).getVehicle_type());
        holder.txt_avilable.setText(carRentlist.get(position).getAvailability());
        holder.txt_price.setText("Price: $"+carRentlist.get(position).getVehicle_price());
        holder.txt_location.setText(carRentlist.get(position).getVehicle_location());
        holder.txt_rent_type.setText(carRentlist.get(position).getRent_type());

        String url=carRentlist.get(position).getVehicle_image();
        if (!url.equalsIgnoreCase("")){
            Picasso.with(activity).load(url).transform(transformation).placeholder(R.mipmap.photo_car).into(holder.img_car_image);
        }

        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=carRentlist.get(position).getId();
                Intent intent=new Intent(activity, NavigationActivity.class);
                intent.putExtra("type","add_vihicle");
                intent.putExtra("id",id);
                intent.putExtra("page","edit");
                activity.startActivity(intent);
            }
        });
        
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=carRentlist.get(position).getId();
              //  activity.deleteVehicle(id,position);
               mOnClickSectionListener.onItemClick(id,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return carRentlist.size();
    }

    public class RVHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_layout;
        ImageView img_car_image,img_edit,img_delete;
        TextView txt_car_size,txt_location,txt_price,txt_avilable,txt_rent_type;
        public RVHolder(@NonNull View itemView) {
            super(itemView);
            ll_layout=itemView.findViewById(R.id.ll_layout);
            // btn_select=itemView.findViewById(R.id.btn_select);
            img_car_image=itemView.findViewById(R.id.img_car_image);
            txt_car_size=itemView.findViewById(R.id.txt_car_size);
            txt_avilable=itemView.findViewById(R.id.txt_avilable);
            txt_price=itemView.findViewById(R.id.txt_price);
            txt_location=itemView.findViewById(R.id.txt_location);
            txt_rent_type=itemView.findViewById(R.id.txt_rent_type);
            img_edit=itemView.findViewById(R.id.img_edit);
            img_delete=itemView.findViewById(R.id.img_delete);
        }
    }

    public interface OnClickSectionListener {

        void onItemClick(String id, int position);
    }
}
