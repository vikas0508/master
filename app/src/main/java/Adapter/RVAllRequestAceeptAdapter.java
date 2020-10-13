package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.e.ryedup.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Model.ServerResponseAcceptRequest;
import de.hdodenhof.circleimageview.CircleImageView;

public class RVAllRequestAceeptAdapter extends RecyclerView.Adapter<RVAllRequestAceeptAdapter.RVHolder> {
    FragmentActivity activity;
    List<ServerResponseAcceptRequest.Data> requestlist;
    public RVAllRequestAceeptAdapter(FragmentActivity activity, List<ServerResponseAcceptRequest.Data> requestlist) {
        this.activity=activity;
        this.requestlist=requestlist;
    }

    @NonNull
    @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_allrequest_acecept_adapter,parent,false);

        return new RVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, int position) {
        String url=requestlist.get(position).getImage();
        if(!url.equalsIgnoreCase("")){
            Picasso.with(activity).load(url).placeholder(R.mipmap.default_user).into(holder.img_profile);
        }
        ////////////// Start date converter//////////////////////////
        String DateFromDb = requestlist.get(position).getFrom_date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//set format of date you receiving from db
        Date date = null;
        try {
            date = (Date) sdf.parse(DateFromDb);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newDate = new SimpleDateFormat("dd MMM yyyy");//set format of new date
       // System.out.println(newDate.format(date));
        String ActDate = newDate.format(date);// here is your new date !
       ////////////// End date converter//////////////////////////
        String DateToDb = requestlist.get(position).getEnd_date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");//set format of date you receiving from db
        Date date1 = null;
        try {
            date1 = (Date) sdf.parse(DateToDb);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newDate1 = new SimpleDateFormat("dd MMM yyyy");//set format of new date
       // System.out.println(newDate.format(date1));
        String to_Date = newDate1.format(date1);// here is your new date !

        holder.txt_name.setText(requestlist.get(position).getFirstname());
        holder.txt_date.setText(ActDate+" "+requestlist.get(position).getStart_time());
        holder.txt_source.setText(requestlist.get(position).getFrom_location());
        holder.txt_destination.setText(requestlist.get(position).getTo_location());

    }

    @Override
    public int getItemCount() {
        return requestlist.size();
    }

    public class RVHolder  extends RecyclerView.ViewHolder{
        CircleImageView img_profile;
        TextView txt_name,txt_price,txt_date,txt_source,txt_destination;
        public RVHolder(@NonNull View itemView) {
            super(itemView);
            img_profile=itemView.findViewById(R.id.img_profile);
            txt_name=itemView.findViewById(R.id.txt_name);
            txt_price=itemView.findViewById(R.id.txt_price);
            txt_date=itemView.findViewById(R.id.txt_date);
            txt_source=itemView.findViewById(R.id.txt_source);
            txt_destination=itemView.findViewById(R.id.txt_destination);

        }
    }
}
