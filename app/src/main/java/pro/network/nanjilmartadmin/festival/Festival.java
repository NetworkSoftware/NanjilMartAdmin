package pro.network.nanjilmartadmin.festival;

import java.io.Serializable;

public class Festival implements Serializable {
    String id;
    String gifUrl;
    String enable;

    public Festival(String id, String gifUrl, String enable) {
        this.id = id;
        this.gifUrl = gifUrl;
        this.enable = enable;
    }

    public Festival() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }
}
