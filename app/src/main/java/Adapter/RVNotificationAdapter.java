package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.e.ryedup.R;

import java.util.List;

import Model.ServerResponseNotification;

public class RVNotificationAdapter extends RecyclerView.Adapter<RVNotificationAdapter.RVHolder>{
    Context context;
    List<ServerResponseNotification.Data> noti_list;
    public RVNotificationAdapter(Context context, List<ServerResponseNotification.Data> noti_list) {
        this.context=context;
        this.noti_list=noti_list;
    }

    @NonNull
    @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_notification_adapter,viewGroup,false);
        return new RVHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder rvHolder, int i) {
         rvHolder.txt_nav_title.setText(noti_list.get(i).getNoti_message());
    }

    @Override
    public int getItemCount() {
        return noti_list.size();
    }

    public class RVHolder extends RecyclerView.ViewHolder {
        TextView txt_nav_title;
        public RVHolder(@NonNull View itemView) {
            super(itemView);
            txt_nav_title=itemView.findViewById(R.id.txt_nav_title);
        }
    }
}
