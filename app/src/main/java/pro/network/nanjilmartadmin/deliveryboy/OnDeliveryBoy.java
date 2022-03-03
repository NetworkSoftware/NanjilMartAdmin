package pro.network.nanjilmartadmin.deliveryboy;

public interface OnDeliveryBoy {

    void onStatusClick(int position, String status);
    void onWalletClick(DeliveryBean deliveryBean);

    void onEditClick(pro.network.nanjilmartadmin.deliveryboy.DeliveryBean deliveryBean);

    void onDeleteClick(int position);
    void onHistoryClick(DeliveryBean deliveryBean);
}
