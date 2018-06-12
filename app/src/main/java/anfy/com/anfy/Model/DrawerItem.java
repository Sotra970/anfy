package anfy.com.anfy.Model;

public class DrawerItem {

    private int imageResItem;
    private int textResItem;
    private int count;


    public DrawerItem(int imageResItem, int textResItem) {
        this.imageResItem = imageResItem;
        this.textResItem = textResItem;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getImageResItem() {
        return imageResItem;
    }

    public int getTextResItem() {
        return textResItem;
    }
}
