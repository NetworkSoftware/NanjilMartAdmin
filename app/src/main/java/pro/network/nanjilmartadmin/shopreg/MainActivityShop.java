package pro.network.nanjilmartadmin.shopreg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
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

import static pro.network.nanjilmartadmin.app.Appconfig.DATA_FETCH_ALL_SHOP;

public class MainActivityShop extends AppCompatActivity implements ShopClick {
    private static final String TAG = MainActivityShop.class.getSimpleName();
    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private List<Shop> categoriesList;
    private ShopAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shop);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Shop Name");


        recyclerView = findViewById(R.id.recycler_view);
        categoriesList = new ArrayList<>();
        mAdapter = new ShopAdapter(this, categoriesList, this);
        final LinearLayoutManager addManager1 = new GridLayoutManager(MainActivityShop.this, 1);
        recyclerView.setLayoutManager(addManager1);
        recyclerView.setAdapter(mAdapter);
        whiteNotificationBar(recyclerView);

        FloatingActionButton addStock = findViewById(R.id.addbanner);
        addStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityShop.this, ShopRegister.class);
                startActivity(intent);
            }
        });
    }

    private void fetchContacts() {
        String tag_string_req = "req_register";
        progressDialog.setMessage("Processing ...");
      //  showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                DATA_FETCH_ALL_SHOP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        categoriesList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Shop categories = new Shop();
                            categories.setId(jsonObject.getString("id"));
                            categories.setShop_name(jsonObject.getString("shop_name"));
                            categories.setPhone(jsonObject.getString("phone"));
                            categories.setLatlong(jsonObject.getString("latlong"));
                            categories.setStock_update(jsonObject.getString("stock_update"));
                            categories.setCategory(jsonObject.getString("category"));
                            categories.setAddress(jsonObject.getString("address"));
                            categories.setOfferAmt(jsonObject.getString("offerAmt"));
                            categories.setTime_schedule(jsonObject.getString("time_schedule"));
                            if(!jsonObject.isNull("image")){
                                categories.setImage(jsonObject.getString("image"));
                            }
                            if(jsonObject.has("shop_enabled")){
                                categories.setShop_enabled(jsonObject.getString("shop_enabled"));
                            }
                            categoriesList.add(categories);
                        }
                        mAdapter.notifyData(categoriesList);
                        getSupportActionBar().setSubtitle(categoriesList.size() + " Nos");

                    } else {
                        Toast.makeText(MainActivityShop.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(MainActivityShop.this, "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(MainActivityShop.this,
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("admin", "true");
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


    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        fetchContacts();
    }

    @Override
    public void onDeleteClick(int position) {
        deleteFile(position);

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivityShop.this, ShopUpdate.class);
        intent.putExtra("data", categoriesList.get(position));
        startActivity(intent);
    }

    private void deleteFile(final int position) {
        String tag_string_req = "req_register";
        progressDialog.setMessage("Fetching ...");
        showDialog();
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Appconfig.DELETE_SHOP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getBoolean("success")) {
                        categoriesList.remove(position);
                        mAdapter.notifyData(categoriesList);
                    }
                    Toast.makeText(MainActivityShop.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(MainActivityShop.this, "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(MainActivityShop.this,
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("id", categoriesList.get(position).id);
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
