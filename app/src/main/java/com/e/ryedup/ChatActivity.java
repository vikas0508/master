package com.e.ryedup;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import Adapter.ChatArrayAdapter;
import Model.ChatMessage;
import Model.SendChat;
import Model.UserModel;
import Utils.Constants;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {
    Toolbar toolbar;
    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private ImageView img_send;
    private boolean side = false;
    TextView toolbar_title;
    RelativeLayout rl_send;
    private ArrayList<ChatMessage> chatMessageList;
    OkHttpClient mClient;
    DatabaseReference database;
    SharedPreferences preference;
    String login_id="",driver_id="",driver_name="",user_type="",image="",request_id="",token="",device_type="",login_name="";
    private String sender_id1="",sender_id="";
    private String reciver_id1="",reciver_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BindUI();

        preference = getSharedPreferences(Constants.Shared_Pref, Context.MODE_PRIVATE);
        user_type=preference.getString("user_type","");
        login_id=preference.getString("loginid","");
        login_name=preference.getString("firstname","");

        sender_id=login_id;

        driver_id=getIntent().getStringExtra("driverid");
        driver_name=getIntent().getStringExtra("driver_name");
        request_id=getIntent().getStringExtra("request_id");
        image=getIntent().getStringExtra("driver_image");
        token=getIntent().getStringExtra("deriver_token");
        device_type=getIntent().getStringExtra("device_type");

        // reciver_id=driver_id;
        toolbar_title.setText(driver_name);

        if(login_id.equalsIgnoreCase(sender_id))
        {
            sender_id=login_id;
            reciver_id=driver_id;
        }else
        {
            reciver_id=sender_id;
            sender_id=login_id;
        }

        chatMessageList = new ArrayList<ChatMessage>();
        chatMessageList.clear();
        chatArrayAdapter = new ChatArrayAdapter(ChatActivity.this, R.layout.left);
        listView.setAdapter(chatArrayAdapter);

        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });

        rl_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
               sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);
        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });

       incomingmsg();
    }

    private void BindUI() {
        listView= (ListView) findViewById(R.id.msgview);
        chatText= (EditText) findViewById(R.id.msg);
        img_send= (ImageView) findViewById(R.id.img_send);
        toolbar_title= (TextView) findViewById(R.id.toolbar_title);
        rl_send=(RelativeLayout)findViewById(R.id.rl_send);
    }

    private boolean sendChatMessage() {
        if(chatText.getText().toString().trim().length()>0) {
           /* chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
            chatText.setText("");
            side = !side;*/
            Date localTime = new Date();
            DateFormat converter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //getting GMT timezone, you can get any timezone e.g. UTC
            converter.setTimeZone(TimeZone.getTimeZone("GMT"));

            String curenttime = converter.format(localTime);
            String milisecond= String.valueOf(localTime.getTime());
            long value=-1*localTime.getTime();

            Log.e("curenttime=========", "" + curenttime+" "+ -1*value);

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(token);
            String postid=request_id, sms=chatText.getText().toString().trim(),receiver_id=driver_id,sender=login_id,date=curenttime;
            sendMessage(jsonArray,request_id,sms,receiver_id,sender,date);
            // create frind list
            database = FirebaseDatabase.getInstance().getReference("Friends_list");
            UserModel userModel= new UserModel();
            userModel.setName(driver_name);
            userModel.setImage(image);
            userModel.setUser_id(driver_id);
            userModel.setRequest_id(request_id);
            userModel.setMessage(chatText.getText().toString().trim());
            userModel.setDevice_id(token);
            userModel.setUpdated_at(curenttime);
            database.child(login_id).setValue(userModel);

            database = FirebaseDatabase.getInstance().getReference("Message");
            SendChat sendChat = new SendChat();
            // sendChat.setId(unique);
             sendChat.setMessage(chatText.getText().toString().trim());
             sendChat.setSenderId(login_id);
             sendChat.setReceiverId(receiver_id);
             sendChat.setrequestId(request_id);
             sendChat.setSender_image(image);
           // sendChat.setReceiver_image("");
             sendChat.setDate(curenttime);
             sendChat.setText(chatText.getText().toString().trim());
             sendChat.set_id(milisecond);
             sendChat.setCreatedAt(milisecond);
             sendChat.setOrder(value);
            database.push().setValue(sendChat);

            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    chatText.setText("");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                   Toast.makeText(getApplicationContext(),databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }
        return true;
    }

    private void sendMessage(final JSONArray jsonArray, final String title, final String sms, final String receiver_id, final String sender_id, final String date) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    JSONObject data = new JSONObject();
                    if (device_type.equalsIgnoreCase("1")){
                        notification.put("body",sms);
                        notification.put("title",login_name);
                        notification.put("sound","default");
                        notification.put("icon","chat");
                        //                   notification.put("icon","chat");
                        //   data.put("postId",post_id);
                        data.put("requestStatus","chat");
                        data.put("requestID",HomeActivity.homeActivity.request_id);
                        data.put("badge","0");
                  /*  data.put("receiverId",receiver_id);
                    data.put("date",date);*/
                        root.put("notification", notification);
                        //root.put("data", data);
                        root.put("registration_ids", jsonArray);

                    }else {
                        notification.put("body",sms);
                        notification.put("title",login_name);
                        notification.put("sound","default");
                        notification.put("icon","chat");
//                    notification.put("icon", icon);
                        //   data.put("postId",post_id);
                        data.put("body",sms);
                        data.put("title",driver_name);
                        data.put("sound","default");
                        data.put("icon","chat");

                        root.put("notification", notification);
                        root.put("data", data);
                        root.put("registration_ids", jsonArray);
                    }

                    String result = postToFCM(root.toString());
                    Log.e("Main Activity", "Result: " + result);
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    if (result!=null){
                        JSONObject resultJson = new JSONObject(result);
                        int success, failure;
                        success = resultJson.getInt("success");

                        failure = resultJson.getInt("failure");
                        //  Toast.makeText(getApplicationContext(), "Message Success: " + success + "Message Failed: " + failure, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Message Failed, Unknown error occurred.", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    String postToFCM(String bodyString) throws IOException {
        mClient= new OkHttpClient();
        String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Authorization", "key=" + "AIzaSyAn6eEncn8uzr5DQUt8CSB8uixeu1iN_SM")
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

    private void incomingmsg() {
        DatabaseReference ref = null;

        ref = FirebaseDatabase.getInstance().getReference().child("Message");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Get map of users in datasnapshot
                chatMessageList.clear();
                // GenericTypeIndicator<HashMap<String, Object>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Object>>() {};
                Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.getValue();
                List<SendChat> list = new ArrayList<SendChat>();
                if (objectMap == null) {

                } else {
                    for (Object obj : objectMap.values()) {
                        if (obj instanceof Map) {
                            String reciver = (String) ((Map) obj).get("receiverId");
                            String sender = (String) ((Map) obj).get("senderId");
                            String date = (String) ((Map) obj).get("date");
                            String Sender_image = (String) ((Map) obj).get("sender_image");
                           // String reciver_image = (String) ((Map) obj).get("receiver_image");
                            String requestid = (String) ((Map) obj).get("requestId");
                            String OurDate = "";
                            try {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                                Date value = formatter.parse(date);

                                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //this format changeable
                                dateFormatter.setTimeZone(TimeZone.getDefault());
                                OurDate = dateFormatter.format(value);

                                //Log.d("OurDate", OurDate);
                            } catch (Exception e) {
                                OurDate = "0000-00-00 00:00";
                            }
                            if (requestid.equalsIgnoreCase(request_id)) {

                              if (sender.equalsIgnoreCase(login_id)) {
                                String chatmessage = (String) ((Map) obj).get("message");
                                //     chatArrayAdapter.add(new ChatMessage(side,chatmessage,OurDate));
                               ChatMessage message = new ChatMessage();
                                message.setDate(OurDate);
                                message.setLeft(side);
                                message.setMessage(chatmessage);
                                message.setImage(Sender_image);
                                chatMessageList.add(message);
                                //chatArrayAdapter.notifyDataSetChanged();
                            } else {
                                String chatmessage = (String) ((Map) obj).get("message");
                                ChatMessage message = new ChatMessage();
                                message.setDate(OurDate);
                                message.setLeft(!side);
                                message.setMessage(chatmessage);
                                message.setImage(Sender_image);
                                chatMessageList.add(message);
                                //   chatArrayAdapter.notifyDataSetChanged();
                            }

                            } else {

                            }

                            class StringDateComparator implements Comparator<ChatMessage> {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                public int compare(ChatMessage lhs, ChatMessage rhs) {
                                    try {
                                        return dateFormat.parse(lhs.getDate()).compareTo(dateFormat.parse(rhs.getDate()));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    return 0;
                                }
                            }

                            Collections.sort(chatMessageList, new StringDateComparator());

                            chatArrayAdapter.setChatMessageList(chatMessageList);
                            chatArrayAdapter.notifyDataSetChanged();

                        }
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //handle databaseError
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
