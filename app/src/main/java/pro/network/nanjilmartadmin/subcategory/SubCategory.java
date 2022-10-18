package pro.network.nanjilmartadmin.subcategory;

import java.io.Serializable;
import java.util.ArrayList;

import pro.network.nanjilmartadmin.shopreg.Time;


public class SubCategory implements Serializable {
    String id;
    String name;
    String image;
    String createdOn;


    public SubCategory() {
    }

    public SubCategory(String name, String image) {
        this.name = name;
        this.image = image;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}