package Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e.ryedup.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import Fragment.FragmentHistoryDetails;
import Model.ServerResponseHistory;

public class RVHistoryAdapter extends RecyclerView.Adapter<RVHistoryAdapter.RVHolder> {
    FragmentActivity activity;
    List<ServerResponseHistory.Data> historylist;
    public RVHistoryAdapter(FragmentActivity activity, List<ServerResponseHistory.Data> historylist) {
        this.activity=activity;
        this.historylist=historylist;
    }

    @NonNull
    @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_history_adapter,viewGroup,false);

        return new RVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder rvHolder, final int i) {
        String type=historylist.get(i).getReservation_type();
        if (type!=null && type.length()>0){
            rvHolder.txt_type.setText("Booking type : "+type.substring(0, 1).toUpperCase() + type.substring(1,type.length()));
        }
         rvHolder.txt_price.setText("$ "+historylist.get(i).getAmount());
         rvHolder.txt_source.setText(historylist.get(i).getFrom_location());
         rvHolder.txt_destination.setText(historylist.get(i).getTo_location());
        ////////////// Start date converter//////////////////////////
        String DateFromDb = historylist.get(i).getReq_date_updated();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//set format of date you receiving from db
        sdf.setTimeZone(TimeZone.getDefault());
        Date date = null;
        try {
            date = (Date) sdf.parse(DateFromDb);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newDate = new SimpleDateFormat("dd MMM yyyy hh:mm a");//set format of new date
        // System.out.println(newDate.format(date));
        String ActDate = newDate.format(date);// here is your new date !
        rvHolder.txt_date.setText(ActDate);


        rvHolder.ll_card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id=historylist.get(i).getRequest_id();
                /*Intent intent=new Intent(activity, NavigationActivity.class);
                intent.putExtra("type","history details");
                intent.putExtra("id",id);
                activity.startActivity(intent);
*/

                Fragment fragment= new FragmentHistoryDetails();
                Bundle bundle=new Bundle();
                bundle.putString("id",id);
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack("").commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return historylist.size();
    }

    public class RVHolder extends RecyclerView.ViewHolder  {
        LinearLayout ll_card_layout;
        TextView txt_type,txt_price,txt_date,txt_source,txt_destination;
        public RVHolder(@NonNull View itemView) {
            super(itemView);
            ll_card_layout=itemView.findViewById(R.id.ll_card_layout);

            txt_type=itemView.findViewById(R.id.txt_type);
            txt_price=itemView.findViewById(R.id.txt_price);
            txt_date=itemView.findViewById(R.id.txt_date);
            txt_source=itemView.findViewById(R.id.txt_source);
            txt_destination=itemView.findViewById(R.id.txt_destination);
        }
    }
}
