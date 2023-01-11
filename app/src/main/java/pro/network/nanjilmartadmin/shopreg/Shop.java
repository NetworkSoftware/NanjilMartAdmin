package pro.network.nanjilmartadmin.shopreg;

import java.io.Serializable;
import java.util.ArrayList;


public class Shop implements Serializable {
    String id;
    String shop_name;
    String phone;
    String stock_update;
    String latlong;
    String image;
    String time_schedule;
    String address,category,offerAmt;
    ArrayList<Time> times;
    String shop_enabled;
    String area;
    String freeDelivery,estimateTime,rating;

    public Shop() {
    }

    public Shop(String shop_name, String phone,String stock_update,String latlong,
                String image,String time_schedule,ArrayList<Time> times,
                String shop_enabled,String area) {
        this.shop_name = shop_name;
        this.phone = phone;
        this.stock_update = stock_update;
        this.latlong=latlong;
        this.image=image;
        this.time_schedule=time_schedule;
        this.times=times;
        this.shop_enabled=shop_enabled;
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFreeDelivery() {
        return freeDelivery;
    }

    public void setFreeDelivery(String freeDelivery) {
        this.freeDelivery = freeDelivery;
    }

    public String getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(String estimateTime) {
        this.estimateTime = estimateTime;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getShop_enabled() {
        return shop_enabled;
    }

    public void setShop_enabled(String shop_enabled) {
        this.shop_enabled = shop_enabled;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime_schedule() {
        return time_schedule;
    }

    public void setTime_schedule(String time_schedule) {
        this.time_schedule = time_schedule;
    }


    public ArrayList<Time> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<Time> times) {
        this.times = times;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOfferAmt() {
        return offerAmt;
    }

    public void setOfferAmt(String offerAmt) {
        this.offerAmt = offerAmt;
    }
}