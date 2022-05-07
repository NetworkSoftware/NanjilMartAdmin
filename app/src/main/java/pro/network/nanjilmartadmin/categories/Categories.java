package pro.network.nanjilmartadmin.categories;

import java.io.Serializable;

/**
 * Created by ravi on 16/11/17.
 */

public class Categories implements Serializable {
    String id;
    String title;
    String image;
    String deliveryCost;

    public Categories() {
    }

    public Categories(String title, String image,String deliveryCost) {
        this.title = title;
        this.image = image;
        this.deliveryCost = deliveryCost;
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