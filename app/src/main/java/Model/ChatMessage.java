package Model;

/**
 * Created by vikas on 2/1/2016.
 */
public class ChatMessage {
    public boolean left;
    public String message;
    public String date;
    public String image;
    public String reciver_image;

  /*  public ChatMessage(boolean left, String message) {
        super();
        this.left = left;
        this.message = message;
    }*/

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public boolean isLeft() {
        return left;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
