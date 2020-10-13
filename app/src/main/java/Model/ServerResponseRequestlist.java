package Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerResponseRequestlist {
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
        @SerializedName("image")
        private String image;
        @SerializedName("firstname")
        private String firstname;
        @SerializedName("id")
        private String id;
        @SerializedName("lastname")
        private String lastname;
        @SerializedName("vehicle_name")
        private String vehicle_name;

        public String getImage ()
        {
            return image;
        }

        public void setImage (String image)
        {
            this.image = image;
        }

        public String getFirstname ()
        {
            return firstname;
        }

        public void setFirstname (String firstname)
        {
            this.firstname = firstname;
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
