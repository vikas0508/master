package Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerResponseNotification {
    @SerializedName("code")
    private String code;
    @SerializedName("data")
    private List<Data> data;
    @SerializedName("message")
    private String message;

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public List<Data> getData ()
    {
        return data;
    }

    public void setData (List<Data> data)
    {
        this.data = data;
    }

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [code = "+code+", data = "+data+", message = "+message+"]";
    }
    public class Data
    {
        @SerializedName("to_owner")
        private String to_owner;
        @SerializedName("noti_status")
        private String noti_status;
        @SerializedName("updated_at")
        private String updated_at;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("id")
        private String id;
        @SerializedName("noti_message")
        private String noti_message;
        @SerializedName("to_customer")
        private String to_customer;
        @SerializedName("to_driver")
        private String to_driver;

        public String getTo_owner ()
        {
            return to_owner;
        }

        public void setTo_owner (String to_owner)
        {
            this.to_owner = to_owner;
        }

        public String getNoti_status ()
        {
            return noti_status;
        }

        public void setNoti_status (String noti_status)
        {
            this.noti_status = noti_status;
        }

        public String getUpdated_at ()
        {
            return updated_at;
        }

        public void setUpdated_at (String updated_at)
        {
            this.updated_at = updated_at;
        }

        public String getCreated_at ()
        {
            return created_at;
        }

        public void setCreated_at (String created_at)
        {
            this.created_at = created_at;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getNoti_message ()
        {
            return noti_message;
        }

        public void setNoti_message (String noti_message)
        {
            this.noti_message = noti_message;
        }

        public String getTo_customer ()
        {
            return to_customer;
        }

        public void setTo_customer (String to_customer)
        {
            this.to_customer = to_customer;
        }

        public String getTo_driver ()
        {
            return to_driver;
        }

        public void setTo_driver (String to_driver)
        {
            this.to_driver = to_driver;
        }

    }
}
