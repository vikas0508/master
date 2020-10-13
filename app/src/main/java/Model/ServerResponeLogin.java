package Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServerResponeLogin implements Serializable {

    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private Data data;
    @SerializedName("code")
    private String code;
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public class Data
    {
        @SerializedName("otp")
        private String otp;
        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

    }

}
