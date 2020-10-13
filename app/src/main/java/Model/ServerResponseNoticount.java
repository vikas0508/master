package Model;

import com.google.gson.annotations.SerializedName;

public class ServerResponseNoticount {

    @SerializedName("code")
    private String code;
    @SerializedName("message")
    private String message;
    @SerializedName("Ncount")
    private String Ncount;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNcount() {
        return Ncount;
    }

    public void setNcount(String ncount) {
        Ncount = ncount;
    }



}
