package pro.network.nanjilmartadmin.categories;
import java.io.Serializable;

public class Categories implements Serializable {
    String id;
    String title;
    String image;
    String deliveryCost;
    String row;
    String latlong;
    String nextOpen;
    String category_enabled;

    public Categories() {
    }

    public Categories(String title, String image,String deliveryCost,
                      String row,String category_enabled,String latlong,String nextOpen) {
        this.title = title;
        this.image = image;
        this.deliveryCost = deliveryCost;
        this.row = row;
        this.category_enabled = category_enabled;
        this.latlong = latlong;
        this.nextOpen = nextOpen;
    }

    public String getNextOpen() {
        return nextOpen;
    }

    public void setNextOpen(String nextOpen) {
        this.nextOpen = nextOpen;
    }

    public String getLatlong() {
        return latlong;
    }

    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getCategory_enabled() {
        return category_enabled;
    }

    public void setCategory_enabled(String category_enabled) {
        this.category_enabled = category_enabled;
    }

    public String getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(String deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}