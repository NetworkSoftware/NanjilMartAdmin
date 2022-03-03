package pro.network.nanjilmartadmin.deliveryboy;

import java.io.Serializable;

/**
 * Created by ravi on 16/11/17.
 */

public class DeliveryBean implements Serializable {
    String id;
    String name;
    String phone;
    String password;
    String profileImage;
    String license;
    String adharcard;
    String status,walletAmt;

    public DeliveryBean() {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getAdharcard() {
        return adharcard;
    }

    public void setAdharcard(String adharcard) {
        this.adharcard = adharcard;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWalletAmt() {
        return walletAmt;
    }

    public void setWalletAmt(String walletAmt) {
        this.walletAmt = walletAmt;
    }
}