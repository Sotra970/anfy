package anfy.com.anfy.Model;

public class ConsultChatItem {

    public final static int TYPE_IMAGE = 0;
    public final static int TYPE_TEXT = 1;

    public final static int SIDE_ME = 0;
    public final static int SIDE_OTHER = 1;

    private String text;
    private String date;
    private int type;
    private int side;

    public ConsultChatItem(String text, String date, int type, int side) {
        this.text = text;
        this.date = date;
        this.type = type;
        this.side = side;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    public int getSide() {
        return side;
    }
}
