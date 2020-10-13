package Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.ryedup.HomeActivity;
import com.e.ryedup.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.ServerResponseNearby;
import de.hdodenhof.circleimageview.CircleImageView;

public class RVBookTypeAdapter extends RecyclerView.Adapter<RVBookTypeAdapter.RVHolder> {
    HomeActivity homeActivity;
    List<ServerResponseNearby.Data> bookinglist;
    TypedArray booking_type;
    List<String> booking_titles;
    SharedPreferences preference,tempprefrence;
    SharedPreferences.Editor editor;

    int row_index;
    public RVBookTypeAdapter(HomeActivity homeActivity, List<ServerResponseNearby.Data> bookinglist) {
        this.homeActivity=homeActivity;
        this.bookinglist=bookinglist;
    }
  /*  public RVBookTypeAdapter(HomeActivity homeActivity, List<String> booking_titles, TypedArray booking_type) {
        this.homeActivity=homeActivity;
        this.booking_type=booking_type;
        this.booking_titles=booking_titles;
    }*/

    @NonNull
    @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_booktype_adapter,parent,false);
        return new RVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RVHolder holder, final int position) {

            holder.txt_title.setText("RydeUp "+bookinglist.get(position).getCategory_name());
            holder.txt_seats.setText("Seats 1-"+bookinglist.get(position).getCategory_seats());
            holder.txt_price.setText("Price "+"$"+bookinglist.get(position).getCategory_price());
            String url=bookinglist.get(position).getCategory_image();
            if (!url.equalsIgnoreCase("")){
                Picasso.with(homeActivity).load(url).placeholder(R.mipmap.photo_car).into(holder.img_rydeup);
            }
        /*holder.txt_title.setText("RydeUp "+booking_titles.get(position));
        holder.img_rydeup.setImageResource(booking_type.getResourceId(position,0));*/

            holder.ll_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   //  holder.ll_layout.setBackgroundResource(R.color.color_select);
                    row_index=position;
                    notifyDataSetChanged();
                }
            });

        if(row_index==position){
            holder.ll_layout.setBackgroundColor(Color.parseColor("#A4A4D0F3"));
           // holder.tv1.setTextColor(Color.parseColor("#ffffff"));
            HomeActivity.homeActivity.btn_conform.setText("Confirm "+holder.txt_title.getText());
            HomeActivity.homeActivity.trip_price=bookinglist.get(row_index).getCategory_price();
            HomeActivity.homeActivity.categoryid=bookinglist.get(row_index).getId();
            tempprefrence = homeActivity.getSharedPreferences("temp", Context.MODE_PRIVATE);
            editor = tempprefrence.edit();
            editor.putString("price",bookinglist.get(row_index).getCategory_price());
            editor.commit();



        }
        else
        {
           holder.ll_layout.setBackgroundColor(Color.TRANSPARENT);
            //holder.tv1.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        return bookinglist.size();
        //return booking_titles.size();
    }

    public class RVHolder extends RecyclerView.ViewHolder {
           TextView txt_title,txt_seats,txt_price;
           CircleImageView img_rydeup;
           LinearLayout ll_layout;
        public RVHolder(@NonNull View itemView) {
            super(itemView);
            img_rydeup=itemView.findViewById(R.id.img_rydeup);
            txt_title=itemView.findViewById(R.id.txt_title);
            txt_seats=itemView.findViewById(R.id.txt_seats);
            txt_price=itemView.findViewById(R.id.txt_price);
            ll_layout=itemView.findViewById(R.id.ll_layout);
        }
    }
}
