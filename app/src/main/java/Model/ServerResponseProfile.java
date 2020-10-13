package Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerResponseProfile {
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

    public class Data {
        @SerializedName("image")
        private String image;
        @SerializedName("loginid")
        private String login_id;
        @SerializedName("firstname")
        private String firstname;
        @SerializedName("address")
        private String address;
        @SerializedName("gender")
        private String gender;
        @SerializedName("updated_at")
        private String updated_at;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("id")
        private String id;
        @SerializedName("lastname")
        private String lastname;
        @SerializedName("status")
        private String status;
        @SerializedName("card_no")
        private String card_no;
        @SerializedName("email")
        private String email;
        @SerializedName("contact")
        private String contact_no;
        @SerializedName("is_verified")
        private String is_verified;

        @SerializedName("driving_licence")
        private String driving_licence;


        public String getImage ()
        {
            return image;
        }

        public void setImage (String image)
        {
            this.image = image;
        }

        public String getLogin_id ()
        {
            return login_id;
        }

        public void setLogin_id (String login_id)
        {
            this.login_id = login_id;
        }

        public String getFirstname ()
        {
            return firstname;
        }

        public void setFirstname (String firstname)
        {
            this.firstname = firstname;
        }

        public String getAddress ()
        {
            return address;
        }

        public void setAddress (String address)
        {
            this.address = address;
        }

        public String getGender ()
        {
            return gender;
        }

        public void setGender (String gender)
        {
            this.gender = gender;
        }

        public String getUpdated_at ()
        {
            return updated_at;
        }

        public void setUpdated_at (String updated_at)
        {
            this.updated_at = updated_at;
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

        public String getLastname ()
        {
            return lastname;
        }

        public void setLastname (String lastname)
        {
            this.lastname = lastname;
        }

        public String getStatus ()
        {
            return status;
        }

        public void setStatus (String status)
        {
            this.status = status;
        }
        public String getCard_no() {
            return card_no;
        }

        public void setCard_no(String card_no) {
            this.card_no = card_no;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getContact_no() {
            return contact_no;
        }

        public void setContact_no(String contact_no) {
            this.contact_no = contact_no;
        }
        public String getIs_verified() {
            return is_verified;
        }

        public void setIs_verified(String is_verified) {
            this.is_verified = is_verified;
        }
        public String getDriving_licence() {
            return driving_licence;
        }

        public void setDriving_licence(String driving_licence) {
            this.driving_licence = driving_licence;
        }

    }
}
