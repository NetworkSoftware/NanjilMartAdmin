package pro.network.nanjilmartadmin.shopreg;

import java.io.Serializable;


public class Shop implements Serializable {
    String id;
    String shop_name;
    String phone;
    String stock_update;
    String latlong;

    public Shop() {
    }

    public Shop(String shop_name, String phone,String stock_update,String latlong) {
        this.shop_name = shop_name;
        this.phone = phone;
        this.stock_update = stock_update;
        this.latlong=latlong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStock_update() {
        return stock_update;
    }

    public void setStock_update(String stock_update) {
        this.stock_update = stock_update;
    }

    public String getLatlong() {
        return latlong;
    }

    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }
}