package anfy.com.anfy.Model;

public class DrawerItem {

    private int imageResItem;
    private int textResItem;

    public DrawerItem(int imageResItem, int textResItem) {
        this.imageResItem = imageResItem;
        this.textResItem = textResItem;
    }

    public int getImageResItem() {
        return imageResItem;
    }

    public int getTextResItem() {
        return textResItem;
    }
}
