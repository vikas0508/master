package Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.e.ryedup.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import Model.ChatMessage;

/**
 * Created by vikas on 2/1/2016.
 */
public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {
    TextView chatText,txt_chattim,txt_mechattim,txt_date;
    ImageView img_profile,img_send_profile;
    private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();

    private Context context;
    String value;
    String OurDate,time1;
    String date1;
    @Override
    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);

    }
    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context=context;

    }
    public void setChatMessageList(List<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
    }
    public int getCount() {
        return this.chatMessageList.size();
    }
    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

            ChatMessage chatMessageObj = getItem(position);
          boolean value = chatMessageList.get(position).isLeft();
            row = convertView;
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            if (value) {
                row = inflater.inflate(R.layout.left, parent, false);
                txt_chattim = (TextView) row.findViewById(R.id.txt_chattim);
             //   img_profile=(ImageView) row.findViewById(R.id.img_profile);
               OurDate=chatMessageList.get(position).getDate();
                //time hours
                StringTokenizer tk = new StringTokenizer(OurDate);
                date1 = tk.nextToken();
                String time = tk.nextToken();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");

                Date dt;
                try {
                    dt = sdf.parse(time);
                    time1 = sdfs.format(dt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Log.e("Time Display: ", "" + time1); // <-- I got result here

                txt_chattim.setText(time1);
               /* String sender_url=chatMessageList.get(position).getImage();

                if (sender_url!=null)
                {
                    Picasso.with(context).load(sender_url).placeholder(R.mipmap.default_user).into(img_profile);
                }*/


            }else{
                row = inflater.inflate(R.layout.right, parent, false);
                txt_mechattim = (TextView) row.findViewById(R.id.txt_mechattim);
                //img_send_profile=(ImageView) row.findViewById(R.id.img_send_profile);
                OurDate=chatMessageList.get(position).getDate();

                //time hours
                StringTokenizer tk = new StringTokenizer(OurDate);
                date1 = tk.nextToken();
                String time = tk.nextToken();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");

                Date dt;
                try {
                    dt = sdf.parse(time);
                    time1 = sdfs.format(dt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Log.e("Time Display: ", "" + time1); // <-- I got result here

                txt_mechattim.setText(time1);

              /*  String sender_url=chatMessageList.get(position).getImage();
                if (sender_url!=null)
                {
                    Picasso.with(context).load(sender_url).placeholder(R.mipmap.default_user).into(img_send_profile);
                }*/
            }
            chatText = (TextView) row.findViewById(R.id.msgr);
            txt_date =(TextView)row.findViewById(R.id.txt_date);

            // convet date in time miles
      //  "EEE MMM dd HH:mm:ss z yyyy"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date mDate = sdf.parse(OurDate);
            long timeInMilliseconds = mDate.getTime();
            Log.e("finaldate", String.valueOf(timeInMilliseconds));
            dateFormat(timeInMilliseconds,txt_date,position,chatMessageList);
        } catch (ParseException e) {
            e.printStackTrace();
        }
            chatText.setText(chatMessageList.get(position).getMessage());

           /* chatText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                   UserChatActivity.removeValue(position);
                    return true;

                }
            });*/

        return row;
    }

    public void dateFormat(Long timeValue, TextView finalDate, int selectedPosition, List<ChatMessage> list) {

        if (selectedPosition == 0) {
            Calendar cl = Calendar.getInstance();
            cl.setTimeInMillis(timeValue);  //here your time in miliseconds
            String date = "" + cl.get(Calendar.DAY_OF_MONTH) + "/" + ((cl.get(Calendar.MONTH)) + 1) + "/" + cl.get(Calendar.YEAR);

            Calendar smsTime = Calendar.getInstance();
            smsTime.setTimeInMillis(timeValue);
            Calendar now = Calendar.getInstance();
            final String timeFormatString = "h:mm aa";
            final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
            final long HOURS = 60 * 60 * 60;
            DateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
            if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
                finalDate.setText("Today");
            } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
                finalDate.setText("yestraday");
            } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
                finalDate.setText(formatter.format(timeValue));
            } else {
                finalDate.setText(formatter.format(timeValue));
            }
            finalDate.setVisibility(View.VISIBLE);
        } else {
            String data_timestamp=list.get(selectedPosition-1).getDate();
            long timeInMillisecond = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date mDate = sdf.parse(data_timestamp);
                timeInMillisecond = mDate.getTime();
                System.out.println("Date in milli :: " + timeInMillisecond);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long timeValue2 =timeInMillisecond;

            // for previous index
            Calendar cl = Calendar.getInstance();
            cl.setTimeInMillis(timeValue2);  //here your time in miliseconds
            String date = "" + cl.get(Calendar.DAY_OF_MONTH) + "/" + ((cl.get(Calendar.MONTH)) + 1) + "/" + cl.get(Calendar.YEAR);

            Calendar smsTime = Calendar.getInstance();
            smsTime.setTimeInMillis(timeValue2);
            Calendar now = Calendar.getInstance();
            final String timeFormatString = "h:mm aa";
            final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
            final long HOURS = 60 * 60 * 60;
            String previosTime = "";
            DateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
            if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
                // finalDate.setText("Today");
                previosTime = "Today";
            } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
                // finalDate.setText("yestraday");
                previosTime = "yestraday";
            } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
                //finalDate.setText(formatter.format(timeValue));
                previosTime = formatter.format(timeValue2);
            } else {
                //    finalDate.setText(formatter.format(timeValue));
                previosTime = formatter.format(timeValue2);
            }
            String data_timestamp1=list.get(selectedPosition).getDate();
            long timeInMillisecond2 = 0;
            SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date mDate = sdfs.parse(data_timestamp1);
                timeInMillisecond2 = mDate.getTime();
                System.out.println("Date in milli :: " + timeInMillisecond2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long timeValue1 = timeInMillisecond2;

            String currentTime = "";
            // for previous index
            Calendar cl1 = Calendar.getInstance();
            cl1.setTimeInMillis(timeValue1);  //here your time in miliseconds
            String date1 = "" + cl1.get(Calendar.DAY_OF_MONTH) + "/" + ((cl1.get(Calendar.MONTH)) + 1) + "/" + cl1.get(Calendar.YEAR);

            Calendar smsTime1 = Calendar.getInstance();
            smsTime1.setTimeInMillis(timeValue1);
            Calendar now1 = Calendar.getInstance();

            DateFormat formatter1 = new SimpleDateFormat("dd-MMMM-yyyy");
            if (now1.get(Calendar.DATE) == smsTime1.get(Calendar.DATE)) {
                //   finalDate.setText("Today");
                currentTime = "Today";
            } else if (now1.get(Calendar.DATE) - smsTime1.get(Calendar.DATE) == 1) {
                // finalDate.setText("yestraday");
                currentTime = "yestraday";
            } else if (now1.get(Calendar.YEAR) == smsTime1.get(Calendar.YEAR)) {
                //  finalDate.setText(formatter1.format(timeValue1));
                currentTime = formatter1.format(timeValue1);
            } else {
                // finalDate.setText(formatter1.format(timeValue1));
                currentTime = formatter1.format(timeValue1);
            }

            if (previosTime.equalsIgnoreCase(currentTime)) {
                finalDate.setVisibility(View.GONE);
            }else {
                finalDate.setVisibility(View.VISIBLE);
                finalDate.setText(currentTime);
            }
        }
    }
}
