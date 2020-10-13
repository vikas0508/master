package Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.e.ryedup.R;

import java.util.ArrayList;


public class CustomSpinnerAdapter extends BaseAdapter {

    private ArrayList<String> sCategoriesList;
    private Activity mActivity;
    private LayoutInflater inflater;

    public CustomSpinnerAdapter(Activity activity, ArrayList<String> mCategories) {
        mActivity = activity;
        this.sCategoriesList = mCategories;
    }


    @Override
    public int getCount() {
        return sCategoriesList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
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
                inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                myView = inflater.inflate(R.layout.spinner_layout, null);
                holder = new Holder();
                holder.tvCategoryName = myView.findViewById(R.id.tvCategoryName);
                myView.setTag(holder);
            } else {
                holder = (Holder) myView.getTag();
            }

            holder.tvCategoryName.setText(sCategoriesList.get(position));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return myView;
    }

}

