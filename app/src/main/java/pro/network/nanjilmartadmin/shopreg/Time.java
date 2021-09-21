package pro.network.nanjilmartadmin.shopreg;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Time implements Serializable {
    @JsonProperty("dayInWeek")
    String dayInWeek;

    String openHours, closeHours;

    @JsonProperty("isOpen")
    boolean isOpen;

    public Time() {
    }

    public Time(boolean isOpen, String dayInWeek, String openHours, String closeHours) {
        this.isOpen = isOpen;
        this.dayInWeek = dayInWeek;
        this.openHours = openHours;
        this.closeHours = closeHours;
    }


    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    public String getDayInWeek() {
        return dayInWeek;
    }

    public void setDayInWeek(String dayInWeek) {
        this.dayInWeek = dayInWeek;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public String getCloseHours() {
        return closeHours;
    }

    public void setCloseHours(String closeHours) {
        this.closeHours = closeHours;
    }


}
