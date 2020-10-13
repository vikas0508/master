package Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ServerResponseCarRent implements Serializable {
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
        @SerializedName("firstname")
        private String firstname;
        @SerializedName("driver_id")
        private String driver_id;
        @SerializedName("PassengerAirbag")
        private String PassengerAirbag;
        @SerializedName("PowerDoorLocks")
        private String PowerDoorLocks;
        @SerializedName("vehicle_price_hourly")
        private String vehicle_price_hourly;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("DriverAirbag")
        private String DriverAirbag;
        @SerializedName("updated_at")
        private String updated_at;
        @SerializedName("CDPlayer")
        private String CDPlayer;
        @SerializedName("id")
        private String id;
        @SerializedName("PowerSteering")
        private String PowerSteering;
        @SerializedName("fuel_type")
        private String fuel_type;
        @SerializedName("number_of_seats")
        private String number_of_seats;
        @SerializedName("number_of_doors")
        private String number_of_doors;
        @SerializedName("PowerWindows")
        private String PowerWindows;
        @SerializedName("air_condition")
        private String air_condition;
        @SerializedName("LeatherSeats")
        private String LeatherSeats;
        @SerializedName("vehicle_overview")
        private String vehicle_overview;
        @SerializedName("heater")
        private String heater;
        @SerializedName("vehicle_type")
        private String vehicle_type;
        @SerializedName("model_year")
        private String model_year;
        @SerializedName("brand_name")
        private String brand_name;
        @SerializedName("vehicles_brand")
        private String vehicles_brand;
        @SerializedName("vehicle_image")
        private String vehicle_image;
        @SerializedName("lastname")
        private String lastname;
        @SerializedName("CrashSensor")
        private String CrashSensor;
        @SerializedName("vehicle_number")
        private String vehicle_number;
        @SerializedName("vehicle_price")
        private String vehicle_price;
        @SerializedName("vehicle_name")
        private String vehicle_name;
        @SerializedName("CentralLocking")
        private String CentralLocking;
        @SerializedName("status")
        private String status;
        @SerializedName("availability")
        private String availability;
        @SerializedName("rent_type")
        private String rent_type;
        @SerializedName("vehicle_location")
        private String vehicle_location;
        @SerializedName("miles")
        private String miles;

        public String getFirstname ()
        {
            return firstname;
        }

        public void setFirstname (String firstname)
        {
            this.firstname = firstname;
        }

        public String getDriver_id ()
        {
            return driver_id;
        }

        public void setDriver_id (String driver_id)
        {
            this.driver_id = driver_id;
        }

        public String getPassengerAirbag ()
        {
            return PassengerAirbag;
        }

        public void setPassengerAirbag (String PassengerAirbag)
        {
            this.PassengerAirbag = PassengerAirbag;
        }

        public String getPowerDoorLocks ()
        {
            return PowerDoorLocks;
        }

        public void setPowerDoorLocks (String PowerDoorLocks)
        {
            this.PowerDoorLocks = PowerDoorLocks;
        }

        public String getVehicle_price_hourly ()
        {
            return vehicle_price_hourly;
        }

        public void setVehicle_price_hourly (String vehicle_price_hourly)
        {
            this.vehicle_price_hourly = vehicle_price_hourly;
        }

        public String getCreated_at ()
        {
            return created_at;
        }

        public void setCreated_at (String created_at)
        {
            this.created_at = created_at;
        }

        public String getDriverAirbag ()
        {
            return DriverAirbag;
        }

        public void setDriverAirbag (String DriverAirbag)
        {
            this.DriverAirbag = DriverAirbag;
        }

        public String getUpdated_at ()
        {
            return updated_at;
        }

        public void setUpdated_at (String updated_at)
        {
            this.updated_at = updated_at;
        }

        public String getCDPlayer ()
        {
            return CDPlayer;
        }

        public void setCDPlayer (String CDPlayer)
        {
            this.CDPlayer = CDPlayer;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getPowerSteering ()
        {
            return PowerSteering;
        }

        public void setPowerSteering (String PowerSteering)
        {
            this.PowerSteering = PowerSteering;
        }

        public String getFuel_type ()
        {
            return fuel_type;
        }

        public void setFuel_type (String fuel_type)
        {
            this.fuel_type = fuel_type;
        }

        public String getNumber_of_seats ()
        {
            return number_of_seats;
        }

        public void setNumber_of_seats (String number_of_seats)
        {
            this.number_of_seats = number_of_seats;
        }

        public String getNumber_of_doors ()
        {
            return number_of_doors;
        }

        public void setNumber_of_doors (String number_of_doors)
        {
            this.number_of_doors = number_of_doors;
        }

        public String getPowerWindows ()
        {
            return PowerWindows;
        }

        public void setPowerWindows (String PowerWindows)
        {
            this.PowerWindows = PowerWindows;
        }

        public String getAir_condition ()
        {
            return air_condition;
        }

        public void setAir_condition (String air_condition)
        {
            this.air_condition = air_condition;
        }

        public String getLeatherSeats ()
        {
            return LeatherSeats;
        }

        public void setLeatherSeats (String LeatherSeats)
        {
            this.LeatherSeats = LeatherSeats;
        }

        public String getVehicle_overview ()
        {
            return vehicle_overview;
        }

        public void setVehicle_overview (String vehicle_overview)
        {
            this.vehicle_overview = vehicle_overview;
        }

        public String getHeater ()
        {
            return heater;
        }

        public void setHeater (String heater)
        {
            this.heater = heater;
        }

        public String getVehicle_type ()
        {
            return vehicle_type;
        }

        public void setVehicle_type (String vehicle_type)
        {
            this.vehicle_type = vehicle_type;
        }

        public String getModel_year ()
        {
            return model_year;
        }

        public void setModel_year (String model_year)
        {
            this.model_year = model_year;
        }

        public String getBrand_name ()
        {
            return brand_name;
        }

        public void setBrand_name (String brand_name)
        {
            this.brand_name = brand_name;
        }

        public String getVehicles_brand ()
        {
            return vehicles_brand;
        }

        public void setVehicles_brand (String vehicles_brand)
        {
            this.vehicles_brand = vehicles_brand;
        }

        public String getVehicle_image ()
        {
            return vehicle_image;
        }

        public void setVehicle_image (String vehicle_image)
        {
            this.vehicle_image = vehicle_image;
        }

        public String getLastname ()
        {
            return lastname;
        }

        public void setLastname (String lastname)
        {
            this.lastname = lastname;
        }

        public String getCrashSensor ()
        {
            return CrashSensor;
        }

        public void setCrashSensor (String CrashSensor)
        {
            this.CrashSensor = CrashSensor;
        }

        public String getVehicle_number ()
        {
            return vehicle_number;
        }

        public void setVehicle_number (String vehicle_number)
        {
            this.vehicle_number = vehicle_number;
        }

        public String getVehicle_price ()
        {
            return vehicle_price;
        }

        public void setVehicle_price (String vehicle_price)
        {
            this.vehicle_price = vehicle_price;
        }

        public String getVehicle_name ()
        {
            return vehicle_name;
        }

        public void setVehicle_name (String vehicle_name)
        {
            this.vehicle_name = vehicle_name;
        }

        public String getCentralLocking ()
        {
            return CentralLocking;
        }

        public void setCentralLocking (String CentralLocking) {
            this.CentralLocking = CentralLocking;
        }

        public String getStatus ()
        {
            return status;
        }

        public void setStatus (String status)
        {
            this.status = status;
        }

        public String getAvailability() {
            return availability;
        }

        public void setAvailability(String availability) {
            this.availability = availability;
        }
        public String getVehicle_location() {
            return vehicle_location;
        }

        public void setVehicle_location(String vehicle_location) {
            this.vehicle_location = vehicle_location;
        }
        public String getRent_type() {
            return rent_type;
        }

        public void setRent_type(String rent_type) {
            this.rent_type = rent_type;
        }

        public String getMiles() {
            return miles;
        }

        public void setMiles(String miles) {
            this.miles = miles;
        }
    }
}
