package Model;

import com.google.gson.annotations.SerializedName;

public class ServerResponseHistoryDetils {
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
        @SerializedName("fare")
        private String fare;
        @SerializedName("amount")
        private String amount;
        @SerializedName("comments")
        private String comments;
        @SerializedName("to_location")
        private String to_location;
        @SerializedName("updated_at")
        private String updated_at;
        @SerializedName("RydeUp_Fee")
        private String RydeUp_Fee;
        @SerializedName("from_location")
        private String from_location;
        @SerializedName("rating_stars")
        private String rating_stars;
        @SerializedName("id")
        private String id;
        @SerializedName("reservation_type")
        private String reservation_type;
        @SerializedName("req_date_updated")
        private String req_date_updated;

        public String getFare ()
        {
            return fare;
        }

        public void setFare (String fare)
        {
            this.fare = fare;
        }

        public String getAmount ()
        {
            return amount;
        }

        public void setAmount (String amount)
        {
            this.amount = amount;
        }

        public String getComments ()
        {
            return comments;
        }

        public void setComments (String comments)
        {
            this.comments = comments;
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

        public String getRydeUp_Fee ()
        {
            return RydeUp_Fee;
        }

        public void setRydeUp_Fee (String RydeUp_Fee)
        {
            this.RydeUp_Fee = RydeUp_Fee;
        }

        public String getFrom_location ()
        {
            return from_location;
        }

        public void setFrom_location (String from_location)
        {
            this.from_location = from_location;
        }

        public String getRating_stars ()
        {
            return rating_stars;
        }

        public void setRating_stars (String rating_stars)
        {
            this.rating_stars = rating_stars;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }
        public String getReservation_type() {
            return reservation_type;
        }

        public void setReservation_type(String reservation_type) {
            this.reservation_type = reservation_type;
        }
        public String getReq_date_updated() {
            return req_date_updated;
        }

        public void setReq_date_updated(String req_date_updated) {
            this.req_date_updated = req_date_updated;
        }


    }


}
