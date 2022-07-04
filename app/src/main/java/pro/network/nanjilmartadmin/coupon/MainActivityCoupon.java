package pro.network.nanjilmartadmin.coupon;

import static pro.network.nanjilmartadmin.app.Appconfig.COUPON;
import static pro.network.nanjilmartadmin.app.Appconfig.mypreference;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.network.nanjilmartadmin.R;
import pro.network.nanjilmartadmin.app.AppController;
import pro.network.nanjilmartadmin.app.Appconfig;

public class MainActivityCoupon extends AppCompatActivity implements CouponAdapter.CouponsAdapterListener {
    private static final String TAG = MainActivityCoupon.class.getSimpleName();
    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private List<CouponProduct> bannerList;
    private CouponAdapter mAdapter;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincoupon);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Coupons");

        recyclerView = findViewById(R.id.coupon_recycler_view);
        bannerList = new ArrayList<>();
        mAdapter = new CouponAdapter(MainActivityCoupon.this, bannerList, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton addStock = (FloatingActionButton) findViewById(R.id.addCoupon);
        addStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityCoupon.this, CouponRegister.class);
                startActivity(intent);
            }
        });
    }

    private void fetchContacts() {
        String tag_string_req = "req_register";
        progressDialog.setMessage("Processing ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                COUPON+"?isAdmin=true", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");

                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        bannerList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            CouponProduct banner = new CouponProduct();
                            banner.setId(jsonObject.getString("id"));
                            banner.setCoupon(jsonObject.getString("coupon"));
                            banner.setAmt(jsonObject.getString("amt"));
                            banner.setStatus(jsonObject.getString("status"));
                            banner.setDescription(jsonObject.getString("description"));
                            banner.setIsPercent(jsonObject.getString("isPercent"));
                            banner.setHide(jsonObject.getString("isHide"));
                            banner.setPercentage(jsonObject.getString("percentage"));
                            banner.setMinimumOrder(jsonObject.getString("miniorder"));
                            banner.setMaxNumbers(jsonObject.getString("maxNumbers"));
                            banner.setShopId(jsonObject.getString("shopId"));
                            bannerList.add(banner);
                        }
                        mAdapter.notifyData(bannerList);
                        getSupportActionBar().setSubtitle(bannerList.size() + "  Nos");

                    } else {
                        Toast.makeText(MainActivityCoupon.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(MainActivityCoupon.this, "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(MainActivityCoupon.this,
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchContacts();
    }

    @Override
    public void onContactSelected(CouponProduct product) {
        Intent intent=new Intent(MainActivityCoupon.this, CouponRegister.class);
        intent.putExtra("data",product);
        startActivity(intent);
    }
}
