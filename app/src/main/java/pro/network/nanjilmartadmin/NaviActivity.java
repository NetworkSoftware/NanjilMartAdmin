package pro.network.nanjilmartadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import pro.network.nanjilmartadmin.banner.MainActivityBanner;
import pro.network.nanjilmartadmin.categories.MainActivityCategories;
import pro.network.nanjilmartadmin.coupon.MainActivityCoupon;
import pro.network.nanjilmartadmin.deliveryboy.MainActivityDelivery;
import pro.network.nanjilmartadmin.enquires.MainActivityEnquire;
import pro.network.nanjilmartadmin.festival.FestivalMainActivity;
import pro.network.nanjilmartadmin.news.NewsRegister;
import pro.network.nanjilmartadmin.order.MainActivityOrder;
import pro.network.nanjilmartadmin.product.MainActivityProduct;
import pro.network.nanjilmartadmin.shopreg.MainActivityShop;
import pro.network.nanjilmartadmin.videos.MainActivityVideo;

public class NaviActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);


        CardView news =  findViewById(R.id.news);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, NewsRegister.class);
                startActivity(io);
            }
        });

        CardView festival =  findViewById(R.id.festival);
        festival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, FestivalMainActivity.class);
                startActivity(io);
            }
        });

        CardView coupon =  findViewById(R.id.coupon);
        coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityCoupon.class);
                startActivity(io);
            }
        });
        CardView catrgories =  findViewById(R.id.catrgories);
        catrgories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityCategories.class);
                startActivity(io);
            }
        });
        CardView video =  findViewById(R.id.video);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityVideo.class);
                startActivity(io);
            }
        });

        CardView dboy =  findViewById(R.id.dboy);
        dboy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityDelivery.class);
                startActivity(io);
            }
        });


        CardView stock =  findViewById(R.id.stock);
        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityProduct.class);
                startActivity(io);

            }
        });

        CardView shop = findViewById(R.id.shop);
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityShop.class);
                startActivity(io);

            }
        });
        CardView banner = findViewById(R.id.banner);
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityBanner.class);
                startActivity(io);

            }
        });
        CardView order = findViewById(R.id.orders);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navOrderPage("ordered");
            }
        });
        CardView returned = findViewById(R.id.returned);
        returned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navOrderPage("Returned");
            }
        });

        CardView canceled =  findViewById(R.id.canceled);
        canceled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navOrderPage("canceled");
            }
        });
        CardView delivered = findViewById(R.id.delivered);
        delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navOrderPage("Delivered");
            }
        });
        CardView enquire =  findViewById(R.id.enquiresCard);
        enquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityEnquire.class);
                startActivity(io);
            }
        });
    }

    private void navOrderPage(String status){
        Intent io = new Intent(NaviActivity.this, MainActivityOrder.class);
        io.putExtra("status",status);
        startActivity(io);
    }
}
