package pro.network.nanjilmartadmin.enquires;

import static pro.network.nanjilmartadmin.app.Appconfig.COUPON;
import static pro.network.nanjilmartadmin.app.Appconfig.ENQUIRE;
import static pro.network.nanjilmartadmin.app.Appconfig.mypreference;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import pro.network.nanjilmartadmin.coupon.CouponAdapter;
import pro.network.nanjilmartadmin.coupon.CouponProduct;
import pro.network.nanjilmartadmin.coupon.CouponRegister;

public class MainActivityEnquire extends AppCompatActivity implements  OnEnquires{
    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private List<EnquireBean> enquireBeans;
    private EnquireAdapter enquireAdapter;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainenquires);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Enquires");

        recyclerView = findViewById(R.id.coupon_recycler_view);
        enquireBeans = new ArrayList<>();
        enquireAdapter = new EnquireAdapter(MainActivityEnquire.this, enquireBeans, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(enquireAdapter);

        FloatingActionButton addStock = (FloatingActionButton) findViewById(R.id.addCoupon);
        addStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityEnquire.this, CouponRegister.class);
                startActivity(intent);
            }
        });
    }

    private void fetchContacts() {
        String tag_string_req = "req_register";
        progressDialog.setMessage("Processing ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                ENQUIRE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");

                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        enquireBeans = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            EnquireBean bean = new EnquireBean();
                            bean.setId(jsonObject.getString("id"));
                            bean.setUserId(jsonObject.getString("userId"));
                            bean.setUserName(jsonObject.getString("userName"));
                            bean.setPhone(jsonObject.getString("phone"));
                            bean.setEnquire(jsonObject.getString("enquiry"));
                            bean.setCreatedOn(jsonObject.getString("createdon"));
                            enquireBeans.add(bean);
                        }
                        enquireAdapter.notifyData(enquireBeans);
                        getSupportActionBar().setSubtitle(enquireBeans.size() + "  Nos");

                    } else {
                        Toast.makeText(MainActivityEnquire.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(MainActivityEnquire.this, "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(MainActivityEnquire.this,
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
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

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
    public void onCAllClick(EnquireBean enquireBean) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+enquireBean.phone));
            startActivity(callIntent);
        }catch (Exception e){
            Log.e("phone",e.toString());
        }

    }
}
