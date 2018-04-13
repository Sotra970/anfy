package anfy.com.anfy.Model;

public class ConsultationItem {

    private String title;
    private String date;
    private String id;

    public ConsultationItem(String title, String date, String id) {
        this.title = title;
        this.date = date;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }
}
