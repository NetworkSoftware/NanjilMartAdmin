package pro.network.spirtualadmin.order;

public interface StatusListener {

    void onDeliveredClick(String id);
    void onWhatsAppClick(String phone);
    void onCallClick(String phone);
    void onCancelClick(String id);

}
