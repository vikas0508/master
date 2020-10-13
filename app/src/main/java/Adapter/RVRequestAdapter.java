package Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.e.ryedup.HomeActivity;
import com.e.ryedup.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.ServerResponseRequestlist;
import de.hdodenhof.circleimageview.CircleImageView;

public class RVRequestAdapter extends RecyclerView.Adapter<RVRequestAdapter.RVHolder> {
    HomeActivity homeActivity;
    List<ServerResponseRequestlist.Data> requestlist;
    String action_id="";
    public RVRequestAdapter(HomeActivity homeActivity, List<ServerResponseRequestlist.Data> requestlist) {
       this.homeActivity=homeActivity;
       this.requestlist=requestlist;
    }

    @NonNull
    @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_request_adapter,viewGroup,false);
        return new RVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder rvHolder, final  int i) {
        rvHolder.txt_user_name.setText(requestlist.get(i).getFirstname());
        String url=requestlist.get(i).getImage();
        if (!url.equalsIgnoreCase("")){
            Picasso.with(homeActivity).load(url).placeholder(R.mipmap.default_user).into(rvHolder.img_profile);
        }
        rvHolder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=requestlist.get(i).getId();
                action_id="2";
                homeActivity.accept_reject(id,i,action_id);
            }
        });
        rvHolder.btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=requestlist.get(i).getId();
                action_id="0";
                homeActivity.accept_reject(id,i,action_id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestlist.size();
    }


    public class RVHolder extends RecyclerView.ViewHolder {
         CircleImageView img_profile;
         TextView txt_user_name;
         Button btn_accept,btn_reject;

        public RVHolder(@NonNull View itemView) {
            super(itemView);
            img_profile=itemView.findViewById(R.id.img_profile);
            txt_user_name=itemView.findViewById(R.id.txt_user_name);
            btn_accept=itemView.findViewById(R.id.btn_accept);
            btn_reject=itemView.findViewById(R.id.btn_reject);
        }
    }

}
