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
    String amount;

    public Order() {
    }

    public Order(String items, String quantity, String price, String status, String name, String phone, String address, String reson, ArrayList<Product> productBeans, String createdOn) {
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}