package Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServerResponseOtp implements Serializable {
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
        @SerializedName("address")
        private String address;
        @SerializedName("loginid")
        private String loginid;
        @SerializedName("gender")
        private String gender;
        @SerializedName("terms_conditions")
        private String terms_conditions;
        @SerializedName("contact")
        private String contact;
        @SerializedName("id")
        private String id;
        @SerializedName("email")
        private String email;
        @SerializedName("lastname")
        private String lastname;
        @SerializedName("roletype")
        private String roletype;
        @SerializedName("profileimage")
        private String profileimage;

        @SerializedName("is_verified")
        private String is_verified;



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

        public String getLoginid ()
        {
            return loginid;
        }

        public void setLoginid (String loginid)
        {
            this.loginid = loginid;
        }

        public String getGender ()
        {
            return gender;
        }

        public void setGender (String gender)
        {
            this.gender = gender;
        }

        public String getTerms_conditions ()
        {
            return terms_conditions;
        }

        public void setTerms_conditions (String terms_conditions)
        {
            this.terms_conditions = terms_conditions;
        }

        public String getContact ()
        {
            return contact;
        }

        public void setContact (String contact)
        {
            this.contact = contact;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getEmail ()
        {
            return email;
        }

        public void setEmail (String email)
        {
            this.email = email;
        }

        public String getLastname ()
        {
            return lastname;
        }

        public void setLastname (String lastname)
        {
            this.lastname = lastname;
        }
        public String getRoletype() {
            return roletype;
        }

        public void setRoletype(String roletype) {
            this.roletype = roletype;
        }

        public String getProfileimage() {
            return profileimage;
        }

        public void setProfileimage(String profileimage) {
            this.profileimage = profileimage;
        }
        public String getIs_verified() {
            return is_verified;
        }

        public void setIs_verified(String is_verified) {
            this.is_verified = is_verified;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [firstname = "+firstname+", address = "+address+", loginid = "+loginid+", gender = "+gender+", terms_conditions = "+terms_conditions+", contact = "+contact+", id = "+id+", email = "+email+", lastname = "+lastname+"]";
        }
    }
}
