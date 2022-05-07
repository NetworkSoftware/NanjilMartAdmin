package pro.network.nanjilmartadmin.filter;

public class ShopFilterBean {
    String status;
    String id;

    public ShopFilterBean(String e){

    }
    public ShopFilterBean(String status,String id) {
        this.status = status;
        this.id = id;
    }

    public ShopFilterBean() {

    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
