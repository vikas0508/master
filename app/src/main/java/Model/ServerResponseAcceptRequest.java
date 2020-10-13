package Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerResponseAcceptRequest {
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
        @SerializedName("end_date")
        private String end_date;
        @SerializedName("request_status")
        private String request_status;
        @SerializedName("image")
        private String image;
        @SerializedName("start_time")
        private String start_time;
        @SerializedName("firstname")
        private String firstname;
        @SerializedName("to_location")
        private String to_location;
        @SerializedName("from_date")
        private String from_date;
        @SerializedName("end_time")
        private String end_time;
        @SerializedName("from_location")
        private String from_location;
        @SerializedName("id")
        private String id;
        @SerializedName("lastname")
        private String lastname;
        @SerializedName("vehicle_name")
        private String vehicle_name;

        public String getEnd_date ()
        {
            return end_date;
        }

        public void setEnd_date (String end_date)
        {
            this.end_date = end_date;
        }

        public String getRequest_status ()
        {
            return request_status;
        }

        public void setRequest_status (String request_status)
        {
            this.request_status = request_status;
        }

        public String getImage ()
        {
            return image;
        }

        public void setImage (String image)
        {
            this.image = image;
        }

        public String getStart_time ()
        {
            return start_time;
        }

        public void setStart_time (String start_time)
        {
            this.start_time = start_time;
        }

        public String getFirstname ()
        {
            return firstname;
        }

        public void setFirstname (String firstname)
        {
            this.firstname = firstname;
        }

        public String getTo_location ()
        {
            return to_location;
        }

        public void setTo_location (String to_location)
        {
            this.to_location = to_location;
        }

        public String getFrom_date ()
        {
            return from_date;
        }

        public void setFrom_date (String from_date)
        {
            this.from_date = from_date;
        }

        public String getEnd_time ()
        {
            return end_time;
        }

        public void setEnd_time (String end_time)
        {
            this.end_time = end_time;
        }

        public String getFrom_location ()
        {
            return from_location;
        }

        public void setFrom_location (String from_location)
        {
            this.from_location = from_location;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getLastname ()
        {
            return lastname;
        }

        public void setLastname (String lastname)
        {
            this.lastname = lastname;
        }

        public String getVehicle_name ()
        {
            return vehicle_name;
        }

        public void setVehicle_name (String vehicle_name)
        {
            this.vehicle_name = vehicle_name;
        }

    }
}
