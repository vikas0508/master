package Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerResponseGetCard {
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
        @SerializedName("card_status")
        private String card_status;
        @SerializedName("card_no")
        private String card_no;
        @SerializedName("updated_at")
        private String updated_at;
        @SerializedName("expiry_date")
        private String expiry_date;
        @SerializedName("strip_token")
        private String strip_token;
        @SerializedName("cvv_no")
        private String cvv_no;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("id")
        private String id;
        @SerializedName("customer_id")
        private String customer_id;
        @SerializedName("image_path")
        private String image_path;

        public String getCard_status ()
        {
            return card_status;
        }

        public void setCard_status (String card_status)
        {
            this.card_status = card_status;
        }

        public String getCard_no ()
        {
            return card_no;
        }

        public void setCard_no (String card_no)
        {
            this.card_no = card_no;
        }

        public String getUpdated_at ()
        {
            return updated_at;
        }

        public void setUpdated_at (String updated_at)
        {
            this.updated_at = updated_at;
        }

        public String getExpiry_date ()
        {
            return expiry_date;
        }

        public void setExpiry_date (String expiry_date)
        {
            this.expiry_date = expiry_date;
        }

        public String getStrip_token ()
        {
            return strip_token;
        }

        public void setStrip_token (String strip_token)
        {
            this.strip_token = strip_token;
        }

        public String getCvv_no ()
        {
            return cvv_no;
        }

        public void setCvv_no (String cvv_no)
        {
            this.cvv_no = cvv_no;
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

        public String getCustomer_id ()
        {
            return customer_id;
        }

        public void setCustomer_id (String customer_id)
        {
            this.customer_id = customer_id;
        }

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }
    }

}
