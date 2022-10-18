package pro.network.nanjilmartadmin.order;

import java.io.Serializable;
import java.util.ArrayList;

import pro.network.nanjilmartadmin.product.Product;

/**
 * Created by ravi on 16/11/17.
 */

public class Order implements Serializable {
    String id;
    String items;
    String quantity;
    String price;
    String status;
    String name;
    String phone;
    String address;
    String reson;
    ArrayList<Product> productBeans;
    String createdOn;
    String dcharge;
    String pincode;
    String total;
    String gstAmt;
    String dtime;
    String paymentProgress;
    String shopname,latlong,paymentId,
            paymentMode,subProduct,strikeoutAmt,coupon,couponCost;

    public Order() {
    }

    public Order(String items, String quantity, String price, String status, String name,
                 String phone, String address, String reson, ArrayList<Product> productBeans,
                 String createdOn,String subProduct,String strikeoutAmt,
                 String gstAmt,String coupon,String couponCost ) {
        this.items = items;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.reson = reson;
        this.productBeans = productBeans;
        this.createdOn = createdOn;
        this.subProduct = subProduct;
        this.strikeoutAmt = strikeoutAmt;
        this.gstAmt = gstAmt;
        this.coupon = coupon;
        this.couponCost = couponCost;
    }

    public String getPaymentProgress() {
        return paymentProgress;
    }

    public void setPaymentProgress(String paymentProgress) {
        this.paymentProgress = paymentProgress;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getCouponCost() {
        return couponCost;
    }

    public void setCouponCost(String couponCost) {
        this.couponCost = couponCost;
    }

    public String getGstAmt() {
        return gstAmt;
    }

    public void setGstAmt(String gstAmt) {
        this.gstAmt = gstAmt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }

    public ArrayList<Product> getProductBeans() {
        return productBeans;
    }

    public void setProductBeans(ArrayList<Product> productBeans) {
        this.productBeans = productBeans;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getDcharge() {
        return dcharge;
    }

    public void setDcharge(String dcharge) {
        this.dcharge = dcharge;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDtime() {
        return dtime;
    }

    public void setDtime(String dtime) {
        this.dtime = dtime;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getLatlong() {
        return latlong;
    }

    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getSubProduct() {
        return subProduct;
    }

    public void setSubProduct(String subProduct) {
        this.subProduct = subProduct;
    }

    public String getStrikeoutAmt() {
        return strikeoutAmt;
    }

    public void setStrikeoutAmt(String strikeoutAmt) {
        this.strikeoutAmt = strikeoutAmt;
    }
}