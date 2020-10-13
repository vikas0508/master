package Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerResponseHistory {
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
        @SerializedName("amount")
        private String amount;
        @SerializedName("to_location")
        private String to_location;
        @SerializedName("updated_at")
        private String updated_at;
        @SerializedName("reservation_type")
        private String reservation_type;
        @SerializedName("from_location")
        private String from_location;
        @SerializedName("driverName")
        private String driverName;
        @SerializedName("id")
        private String id;
        @SerializedName("req_date_updated")
        private String req_date_updated;
        @SerializedName("request_id")
        private String request_id;
        @SerializedName("categoryID")
        private String categoryID;
        @SerializedName("trip_distance")
        private String trip_distance;


        public String getReq_date_updated() {
            return req_date_updated;
        }

        public void setReq_date_updated(String req_date_updated) {
            this.req_date_updated = req_date_updated;
        }

        public String getRequest_id() {
            return request_id;
        }

        public void setRequest_id(String request_id) {
            this.request_id = request_id;
        }

        public String getCategoryID() {
            return categoryID;
        }

        public void setCategoryID(String categoryID) {
            this.categoryID = categoryID;
        }

        public String getTrip_distance() {
            return trip_distance;
        }

        public void setTrip_distance(String trip_distance) {
            this.trip_distance = trip_distance;
        }

        public String getAmount ()
        {
            return amount;
        }

        public void setAmount (String amount)
        {
            this.amount = amount;
        }

        public String getTo_location ()
        {
            return to_location;
        }

        public void setTo_location (String to_location)
        {
            this.to_location = to_location;
        }

        public String getUpdated_at ()
        {
            return updated_at;
        }

        public void setUpdated_at (String updated_at)
        {
            this.updated_at = updated_at;
        }

        public String getReservation_type ()
        {
            return reservation_type;
        }

        public void setReservation_type (String reservation_type)
        {
            this.reservation_type = reservation_type;
        }

        public String getFrom_location ()
        {
            return from_location;
        }

        public void setFrom_location (String from_location)
        {
            this.from_location = from_location;
        }

        public String getDriverName ()
        {
            return driverName;
        }

        public void setDriverName (String driverName)
        {
            this.driverName = driverName;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

    }

}
