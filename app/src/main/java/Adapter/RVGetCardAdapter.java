package Adapter;

import android.app.Dialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.ryedup.HomeActivity;
import com.e.ryedup.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.ServerResponseGetCard;

public class RVGetCardAdapter extends RecyclerView.Adapter<RVGetCardAdapter.RVHolder> {
    HomeActivity homeActivity;
    List<ServerResponseGetCard.Data> cardlist;
    int row_index;
    Dialog dialog;
    public RVGetCardAdapter(HomeActivity homeActivity, List<ServerResponseGetCard.Data> cardlist, Dialog dialog) {
        this.homeActivity=homeActivity;
        this.cardlist=cardlist;
        this.dialog=dialog;
    }

    @NonNull
    @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_getcard_adapter,parent,false);

        return new RVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RVHolder holder, final int position) {
        final String number =cardlist.get(position).getCard_no();
        String mask = number.replaceAll("\\w(?=\\w{4})", "*");
        holder.txt_card_no.setText(mask);

        String path=cardlist.get(position).getImage_path();
        if(path!=null){
            Picasso.with(homeActivity).load(path).placeholder(R.mipmap.credit_card).into(holder.img_card);
        }

        holder.rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.rl_layout.setBackgroundColor(Color.parseColor("#A4A4D0F3"));
                String card=cardlist.get(position).getCard_no();
                String card_id=cardlist.get(position).getId();
                String card_image=cardlist.get(position).getImage_path();
                String strip_token=cardlist.get(position).getStrip_token();
                homeActivity.selectcrad(card,card_id,card_image,strip_token);
                dialog.dismiss();
            }
        });


    }

    @Override
    public int getItemCount() {
        return cardlist.size();
    }

    public class RVHolder extends RecyclerView.ViewHolder {
          ImageView img_card;
          TextView txt_card_no;
          RelativeLayout rl_layout;
        public RVHolder(@NonNull View itemView) {
            super(itemView);
            img_card=itemView.findViewById(R.id.img_card);
            txt_card_no=itemView.findViewById(R.id.txt_card_no);
            rl_layout=itemView.findViewById(R.id.rl_layout);

        }
    }
}
