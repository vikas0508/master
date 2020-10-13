package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.e.ryedup.R;

import java.util.ArrayList;

import Model.UserCategory;

public class SpinTypeAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    FragmentActivity activity;
    ArrayList<UserCategory> vehiclelist;

    public SpinTypeAdapter(FragmentActivity activity, ArrayList<UserCategory> vehiclelist) {
        this.activity=activity;
        this.vehiclelist=vehiclelist;
    }


    @Override
    public int getCount() {
        return vehiclelist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class Holder {
        private TextView tvCategoryName;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View myView = null;
        try {
            Holder holder;
            myView = convertView;
            if (myView == null) {
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                myView = inflater.inflate(R.layout.spinner_layout, null);
                holder = new Holder();
                holder.tvCategoryName = myView.findViewById(R.id.tvCategoryName);
                myView.setTag(holder);
            } else {
                holder = (Holder) myView.getTag();
            }

            holder.tvCategoryName.setText(vehiclelist.get(position).getValue());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return myView;
    }
}
