package Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerResponseNearby {
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
        @SerializedName("type_image")
        private String category_image;
        @SerializedName("name")
        private String category_name;
        @SerializedName("updated_at")
        private String updated_at;
        @SerializedName("type_price")
        private String category_price;
        @SerializedName("type_seats")
        private String category_seats;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("id")
        private String id;
        @SerializedName("type_status")
        private String type_status;
        @SerializedName("service_type")
        private String service_type;

        public String getCategory_image ()
        {
            return category_image;
        }

        public void setCategory_image (String category_image)
        {
            this.category_image = category_image;
        }

        public String getCategory_name ()
        {
            return category_name;
        }

        public void setCategory_name (String category_name)
        {
            this.category_name = category_name;
        }

        public String getUpdated_at ()
        {
            return updated_at;
        }

        public void setUpdated_at (String updated_at)
        {
            this.updated_at = updated_at;
        }

        public String getCategory_price ()
        {
            return category_price;
        }

        public void setCategory_price (String category_price)
        {
            this.category_price = category_price;
        }

        public String getCategory_seats ()
        {
            return category_seats;
        }

        public void setCategory_seats (String category_seats)
        {
            this.category_seats = category_seats;
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
        public String getType_status() {
            return type_status;
        }

        public void setType_status(String type_status) {
            this.type_status = type_status;
        }

        public String getService_type() {
            return service_type;
        }

        public void setService_type(String service_type) {
            this.service_type = service_type;
        }

    }
}
