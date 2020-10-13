package Model;

import com.google.gson.annotations.SerializedName;

public class ServerResponseDriverDetails {
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
        @SerializedName("firstname")
        private String firstname;
        @SerializedName("latitude")
        private String latitude;
        @SerializedName("dest_long")
        private String dest_long;
        @SerializedName("lastname")
        private String lastname;
        @SerializedName("src_lati")
        private String src_lati;
        @SerializedName("driverID")
        private String driverID;
        @SerializedName("to_location")
        private String to_location;
        @SerializedName("vehicle_number")
        private String vehicle_number;
        @SerializedName("from_location")
        private String from_location;
        @SerializedName("dest_lati")
        private String dest_lati;
        @SerializedName("src_long")
        private String src_long;
        @SerializedName("longitude")
        private String longitude;
        @SerializedName("status")
        private String status;
        @SerializedName("image")
        private String image;
        @SerializedName("contact_no")
        private String contact_no;
        @SerializedName("vehicle_name")
        private String vehicle_name;

        @SerializedName("driver_current_location")
        private String driver_current_location;
        @SerializedName("driver_token")
        private String driver_token;
        @SerializedName("device_type")
        private String device_type;


        public String getFirstname ()
        {
            return firstname;
        }

        public void setFirstname (String firstname)
        {
            this.firstname = firstname;
        }

        public String getLatitude ()
        {
            return latitude;
        }

        public void setLatitude (String latitude)
        {
            this.latitude = latitude;
        }

        public String getDest_long ()
        {
            return dest_long;
        }

        public void setDest_long (String dest_long)
        {
            this.dest_long = dest_long;
        }

        public String getLastname ()
        {
            return lastname;
        }

        public void setLastname (String lastname)
        {
            this.lastname = lastname;
        }

        public String getSrc_lati ()
        {
            return src_lati;
        }

        public void setSrc_lati (String src_lati)
        {
            this.src_lati = src_lati;
        }

        public String getDriverID ()
        {
            return driverID;
        }

        public void setDriverID (String driverID)
        {
            this.driverID = driverID;
        }

        public String getTo_location ()
        {
            return to_location;
        }

        public void setTo_location (String to_location)
        {
            this.to_location = to_location;
        }

        public String getVehicle_number ()
        {
            return vehicle_number;
        }

        public void setVehicle_number (String vehicle_number)
        {
            this.vehicle_number = vehicle_number;
        }

        public String getFrom_location ()
        {
            return from_location;
        }

        public void setFrom_location (String from_location)
        {
            this.from_location = from_location;
        }

        public String getDest_lati ()
        {
            return dest_lati;
        }

        public void setDest_lati (String dest_lati)
        {
            this.dest_lati = dest_lati;
        }

        public String getSrc_long ()
        {
            return src_long;
        }

        public void setSrc_long (String src_long)
        {
            this.src_long = src_long;
        }

        public String getLongitude ()
        {
            return longitude;
        }

        public void setLongitude (String longitude)
        {
            this.longitude = longitude;
        }

        public String getStatus ()
        {
            return status;
        }

        public void setStatus (String status)
        {
            this.status = status;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getContact_no() {
            return contact_no;
        }

        public void setContact_no(String contact_no) {
            this.contact_no = contact_no;
        }
        public String getVehicle_name() {
            return vehicle_name;
        }

        public void setVehicle_name(String vehicle_name) {
            this.vehicle_name = vehicle_name;
        }

        public String getDriver_current_location() {
            return driver_current_location;
        }

        public void setDriver_current_location(String driver_current_location) {
            this.driver_current_location = driver_current_location;
        }

        public String getDriver_token() {
            return driver_token;
        }

        public void setDriver_token(String driver_token) {
            this.driver_token = driver_token;
        }

        public String getDevice_type() {
            return device_type;
        }

        public void setDevice_type(String device_type) {
            this.device_type = device_type;
        }
    }
}
