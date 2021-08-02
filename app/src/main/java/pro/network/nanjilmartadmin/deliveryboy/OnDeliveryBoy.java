package pro.network.nanjilmartadmin.deliveryboy;

public interface OnDeliveryBoy {

    void onStatusClick(int position, String status);

    void onEditClick(pro.network.nanjilmartadmin.deliveryboy.DeliveryBean deliveryBean);

    void onDeleteClick(int position);
}
