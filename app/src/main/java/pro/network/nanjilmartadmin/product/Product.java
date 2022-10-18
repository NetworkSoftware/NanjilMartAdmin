package pro.network.nanjilmartadmin.product;

import java.io.Serializable;

import pro.network.nanjilmartadmin.shopreg.Shop;

/**
 * Created by ravi on 16/11/17.
 */

public class Product implements Serializable {
    String id;
    String brand,qtyPrice;
    String model;
    String price;
    String name;
    String image;
    String description;
    String category;
    String qty;
    String stock_update;
    String userId;
    String subCate;
    String subCateID;
    String dCost;
    String offer,offerPercent,offerImage;
    public String shopname;
    public String shopid;
    public String latlong,subProduct,strikeoutAmt,
            time_periods,category_enabled,shop_enabled;


    public Product() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Product(String brand, String model, String price, String name,
                   String image, String description, String category,
                   String subCate,String subCateID,String shopname,String subProduct,
                   String offer,String offerPercent,String offerImage,
                   String time_periods,String category_enabled,String shop_enabled) {
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.name = name;
        this.image = image;
        this.description = description;
        this.category = category;
        this.subCate = subCate;
        this.subCateID=subCateID;
        this.shopname = shopname;
        this.subProduct = subProduct;
        this.offer = offer;
        this.offerPercent = offerPercent;
        this.offerImage = offerImage;
        this.time_periods = time_periods;
        this.category_enabled = category_enabled;
        this.shop_enabled = shop_enabled;
    }

    public String getCategory_enabled() {
        return category_enabled;
    }

    public void setCategory_enabled(String category_enabled) {
        this.category_enabled = category_enabled;
    }

    public String getShop_enabled() {
        return shop_enabled;
    }

    public void setShop_enabled(String shop_enabled) {
        this.shop_enabled = shop_enabled;
    }

    public String getTime_periods() {
        return time_periods;
    }

    public void setTime_periods(String time_periods) {
        this.time_periods = time_periods;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getOfferPercent() {
        return offerPercent;
    }

    public void setOfferPercent(String offerPercent) {
        this.offerPercent = offerPercent;
    }

    public String getOfferImage() {
        return offerImage;
    }

    public void setOfferImage(String offerImage) {
        this.offerImage = offerImage;
    }

    public String getdCost() {
        return dCost;
    }

    public void setdCost(String dCost) {
        this.dCost = dCost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }



    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getStock_update() {
        return stock_update;
    }

    public void setStock_update(String stock_update) {
        this.stock_update = stock_update;
    }

    public String getSubCate() {
        return subCate;
    }

    public void setSubCate(String subCate) {
        this.subCate = subCate;
    }

    public String getSubCateID() {
        return subCateID;
    }

    public void setSubCateID(String subCateID) {
        this.subCateID = subCateID;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getLatlong() {
        return latlong;
    }

    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }

    public String getQtyPrice() {
        return qtyPrice;
    }

    public void setQtyPrice(String qtyPrice) {
        this.qtyPrice = qtyPrice;
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