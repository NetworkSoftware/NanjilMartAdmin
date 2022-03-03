package pro.network.nanjilmartadmin.enquires;

import java.io.Serializable;

public class EnquireBean implements Serializable {
    String id;
    String enquire;
    String createdOn;
    String userId;
    String userName;
    String phone;

    public EnquireBean() {
    }

    public EnquireBean(String enquire, String createdOn) {
        this.enquire = enquire;
        this.createdOn = createdOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnquire() {
        return enquire;
    }

    public void setEnquire(String enquire) {
        this.enquire = enquire;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
