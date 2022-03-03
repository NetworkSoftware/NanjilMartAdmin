package pro.network.nanjilmartadmin.priceQty;

import java.io.Serializable;

public class QuantityPrice implements Serializable {

    String quantity;
    String price;
    String id;

    public QuantityPrice() {
    }

    public QuantityPrice(String quantity, String price) {
        this.quantity = quantity;
        this.price = price;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
