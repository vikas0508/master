package Model;

import com.google.gson.annotations.SerializedName;

public class ServerResponseTrackDriver {
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
        @SerializedName("driver_longitude")
        private String driver_longitude;
        @SerializedName("customer_latitude")
        private String customer_latitude;
        @SerializedName("driverID")
        private String driverID;
        @SerializedName("driver_latitude")
        private String driver_latitude;
        @SerializedName("customer_longitude")
        private String customer_longitude;
        @SerializedName("distance")
        private String distance;
        @SerializedName("durationtime")
        private String durationtime;


        public String getDriver_longitude ()
        {
            return driver_longitude;
        }

        public void setDriver_longitude (String driver_longitude)
        {
            this.driver_longitude = driver_longitude;
        }

        public String getCustomer_latitude ()
        {
            return customer_latitude;
        }

        public void setCustomer_latitude (String customer_latitude)
        {
            this.customer_latitude = customer_latitude;
        }

        public String getDriverID ()
        {
            return driverID;
        }

        public void setDriverID (String driverID)
        {
            this.driverID = driverID;
        }

        public String getDriver_latitude ()
        {
            return driver_latitude;
        }

        public void setDriver_latitude (String driver_latitude)
        {
            this.driver_latitude = driver_latitude;
        }

        public String getCustomer_longitude ()
        {
            return customer_longitude;
        }

        public void setCustomer_longitude (String customer_longitude)
        {
            this.customer_longitude = customer_longitude;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getDurationtime() {
            return durationtime;
        }

        public void setDurationtime(String durationtime) {
            this.durationtime = durationtime;
        }

    }
}
