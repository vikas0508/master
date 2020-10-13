package Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e.ryedup.ChatActivity;
import com.e.ryedup.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Model.UserModel;
import Utils.Constants;
import de.hdodenhof.circleimageview.CircleImageView;

public class RVMessageAdapter extends RecyclerView.Adapter<RVMessageAdapter.RVHolder> {
    FragmentActivity activity;
    ArrayList<UserModel> userfcmList;
    public RVMessageAdapter(FragmentActivity activity, ArrayList<UserModel> userfcmList) {
        this.activity=activity;
        this.userfcmList=userfcmList;
    }

    @NonNull
    @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_message_adapter,viewGroup,false);
        return new RVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder rvHolder, final int i) {

        rvHolder.txt_name.setText(userfcmList.get(i).getName());
        rvHolder.txt_lastmsg.setText(userfcmList.get(i).getMessage());
        String url=userfcmList.get(i).getImage();
        if (url!=null && url.equalsIgnoreCase("")){
            Picasso.with(activity).load(url).placeholder(R.mipmap.default_user).into(rvHolder.img_profile_message);
        }

        rvHolder.txt_update_time.setText("" + Constants.dateTimeComparison(Constants.getCurrentDate(),userfcmList.get(i).getUpdated_at()));

        rvHolder.ll_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(activity, ChatActivity.class);
                intent.putExtra("driverid",userfcmList.get(i).getUser_id());
                intent.putExtra("driver_name",userfcmList.get(i).getName());
                intent.putExtra("request_id",userfcmList.get(i).getRequest_id());
                intent.putExtra("driver_image",userfcmList.get(i).getImage());
                intent.putExtra("deriver_token",userfcmList.get(i).getDevice_id());
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
       // return 10;
        return userfcmList.size();
    }

    public class RVHolder extends RecyclerView.ViewHolder {
        CircleImageView img_profile_message;
        LinearLayout ll_layout;
        TextView txt_name,txt_lastmsg,txt_update_time,txt_msg_count;
        public RVHolder(@NonNull View itemView) {
            super(itemView);
            ll_layout=itemView.findViewById(R.id.ll_layout);
            img_profile_message= itemView.findViewById(R.id.img_profile_message);
            txt_name= itemView.findViewById(R.id.txt_name);
            txt_lastmsg= itemView.findViewById(R.id.txt_lastmsg);
            txt_update_time= itemView.findViewById(R.id.txt_update_time);
          //  txt_msg_count = itemView.findViewById(R.id.txt_msg_count);
        }
    }
}
