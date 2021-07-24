package pro.network.nanjilmartadmin.banner;

import java.io.Serializable;

/**
 * Created by ravi on 16/11/17.
 */

public class Banner implements Serializable {
    String id;
    String banner;
    String description;

    public Banner() {
    }

    public Banner(String banner, String description) {
        this.banner = banner;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}