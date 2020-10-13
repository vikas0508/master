package Model;

import com.google.gson.annotations.SerializedName;

public class ServerResponeStatusUpdate {
    @SerializedName("code")
    private String code;
    @SerializedName("data")
    private Data data;
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

    public Data getData ()
    {
        return data;
    }

    public void setData (Data data)
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
        @SerializedName("request_status")
        private String request_status;
        @SerializedName("source_long")
        private String source_long;
        @SerializedName("to_location")
        private String to_location;
        @SerializedName("destination_long")
        private String destination_long;
        @SerializedName("from_location")
        private String from_location;
        @SerializedName("request_id")
        private String request_id;
        @SerializedName("notifications")
        private String notifications;
        @SerializedName("source_lati")
        private String source_lati;
        @SerializedName("destination_lati")
        private String destination_lati;
        @SerializedName("is_cancelled")
        private String is_cancelled;

        public String getRequest_status ()
        {
            return request_status;
        }

        public void setRequest_status (String request_status)
        {
            this.request_status = request_status;
        }

        public String getSource_long ()
        {
            return source_long;
        }

        public void setSource_long (String source_long)
        {
            this.source_long = source_long;
        }

        public String getTo_location ()
        {
            return to_location;
        }

        public void setTo_location (String to_location)
        {
            this.to_location = to_location;
        }

        public String getDestination_long ()
        {
            return destination_long;
        }

        public void setDestination_long (String destination_long)
        {
            this.destination_long = destination_long;
        }

        public String getFrom_location ()
        {
            return from_location;
        }

        public void setFrom_location (String from_location)
        {
            this.from_location = from_location;
        }

        public String getRequest_id ()
        {
            return request_id;
        }

        public void setRequest_id (String request_id)
        {
            this.request_id = request_id;
        }

        public String getNotifications ()
        {
            return notifications;
        }

        public void setNotifications (String notifications)
        {
            this.notifications = notifications;
        }

        public String getSource_lati ()
        {
            return source_lati;
        }

        public void setSource_lati (String source_lati)
        {
            this.source_lati = source_lati;
        }

        public String getDestination_lati ()
        {
            return destination_lati;
        }

        public void setDestination_lati (String destination_lati)
        {
            this.destination_lati = destination_lati;
        }

        public String getIs_cancelled ()
        {
            return is_cancelled;
        }

        public void setIs_cancelled (String is_cancelled)
        {
            this.is_cancelled = is_cancelled;
        }


    }
}
