package pro.network.nanjilmartadmin.product;

import static pro.network.nanjilmartadmin.app.Appconfig.DATA_FETCH_ALL_SHOP;
import static pro.network.nanjilmartadmin.app.Appconfig.PRODUCT_GET_ALL;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import pro.network.nanjilmartadmin.R;
import pro.network.nanjilmartadmin.app.AppController;
import pro.network.nanjilmartadmin.app.Appconfig;
import pro.network.nanjilmartadmin.filter.OnStatus;
import pro.network.nanjilmartadmin.filter.ShopFilterAdapter;
import pro.network.nanjilmartadmin.filter.ShopFilterBean;
import pro.network.nanjilmartadmin.shopreg.MainActivityShop;
import pro.network.nanjilmartadmin.shopreg.Shop;

public class MainActivityProduct extends AppCompatActivity implements ProductAdapter.ContactsAdapterListener, OnStatus {
    private static final String TAG = MainActivityProduct.class.getSimpleName();
    ProgressDialog progressDialog;
    int offset = 0;
    boolean isAlreadyLoading;
    ProgressBar productRecycle;
    private RecyclerView recyclerView;
    private List<Product> productList;
    private ProductAdapter mAdapter;
    private SearchView searchView;
    private ArrayList<ShopFilterBean> shopFilterBean = new ArrayList<>();
    private Set<String> subCategories = new HashSet<>();
    private String selectedShop = null;
    ShopFilterAdapter shopFilterAdapter;
    RecyclerView recycleSubCat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainstock);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_title);

        recyclerView = findViewById(R.id.recycler_view);
        productList = new ArrayList<>();
        mAdapter = new ProductAdapter(this, productList, this, this);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(addManager1);
        recyclerView.setAdapter(mAdapter);

        productRecycle = findViewById(R.id.productRecycle);
        NestedScrollView nestedScrollview = findViewById(R.id.nestedScrollview);
        nestedScrollview.setNestedScrollingEnabled(false);
        nestedScrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) { //scrollY is the sliding distance
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    if (!isAlreadyLoading) {
                        if (searchView != null && !searchView.isIconified() && searchView.getQuery().toString().length() > 0) {
                            fetchProduct(searchView.getQuery().toString());
                        } else {
                            fetchProduct("");
                        }
                    }
                }
            }
        });

        FloatingActionButton addStock = findViewById(R.id.addStock);
        addStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityProduct.this, ProductUpdate.class);
                startActivity(intent);
            }
        });
        filterSubCate();

    }
    private void filterSubCate() {
        shopFilterBean = new ArrayList<>();
        recycleSubCat = findViewById(R.id.recycler_view_chip);
        shopFilterAdapter = new ShopFilterAdapter(MainActivityProduct.this, shopFilterBean, this, selectedShop);
        final LinearLayoutManager addManager2 = new LinearLayoutManager(MainActivityProduct.this, LinearLayoutManager.HORIZONTAL, false);
        recycleSubCat.setLayoutManager(addManager2);
        recycleSubCat.setAdapter(shopFilterAdapter);
        fetchShop();
    }

    private void fetchProduct(final String searchKey) {
        isAlreadyLoading = true;
        String tag_string_req = "req_register";
        progressDialog.setMessage("Processing ...");
        productRecycle.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                PRODUCT_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                productRecycle.setVisibility(View.GONE);
                Log.d("Register Response: ", response);
                if (offset == 0) {
                    productList = new ArrayList<>();
                    subCategories = new HashSet<>();
                }
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        offset = offset + 1;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Product product = new Product();
                            product.setId(jsonObject.getString("id"));
                            product.setBrand(jsonObject.getString("brand"));
                            product.setCategory(jsonObject.getString("category"));
                            if (!jsonObject.isNull("qtyPrice")) {
                                product.setQtyPrice(jsonObject.getString("qtyPrice"));
                            }
                            product.setSub_category(jsonObject.getString("sub_category"));

                            if(jsonObject.has("strikeoutAmt")){
                                product.setStrikeoutAmt(jsonObject.getString("strikeoutAmt"));
                            }
                            product.setShopname(jsonObject.getString("shopname"));
                            product.setStock_update(jsonObject.getString("stock_update"));
                            product.setDescription(jsonObject.getString("description"));
                            product.setPrice(jsonObject.getString("price"));
                            product.setModel(jsonObject.getString("model"));
                            product.setImage(jsonObject.getString("image"));

                            try {
                                JSONObject shopObject = jsonObject.getJSONObject("shop");
                                product.setShopid(shopObject.getString("id"));
                                product.setShopname(shopObject.getString("shop_name"));
                                product.setLatlong(shopObject.getString("latlong"));
                            } catch (Exception e) {
                                Log.e("xxxxxxxxx", e.toString());
                            }
                            productList.add(product);
                        }
                        mAdapter.notifyData(productList);
                        isAlreadyLoading = false;
                        getSupportActionBar().setSubtitle(productList.size() + "  Nos");

                    } else {
                        Toast.makeText(getApplication(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(getApplication(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                productRecycle.setVisibility(View.GONE);
                isAlreadyLoading = false;
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(getApplication(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("searchKey", searchKey);
                if (productList.size() < 10) {
                    offset = 0;
                }
                localHashMap.put("offset", offset * 10 + "");
                if(selectedShop !=null){
                    localHashMap.put("shopname", selectedShop);
                }
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void fetchShop() {
        String tag_string_req = "req_register";
        progressDialog.setMessage("Processing ...");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                DATA_FETCH_ALL_SHOP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        shopFilterBean = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ShopFilterBean shopBean = new ShopFilterBean();
                            shopBean.setId(jsonObject.getString("id"));
                            shopBean.setStatus(jsonObject.getString("shop_name").toUpperCase(Locale.ROOT));

                            if (shopFilterBean.size() <= 0) {
                                shopFilterBean = new ArrayList<>();
                                shopFilterBean.add(new ShopFilterBean("ALL","ALL"));
                                for (String e : subCategories) {
                                    shopFilterBean.add(new ShopFilterBean(e));
                                }
                                shopFilterAdapter.notifyData(shopFilterBean);
                                selectedShop = "ALL";
                                getSupportActionBar().setSubtitle(selectedShop);
                                shopFilterAdapter.notifyData(selectedShop);
                            }
                            shopFilterBean.add(shopBean);
                        }
                        shopFilterAdapter.notifyData(shopFilterBean);

                    } else {
                        Toast.makeText(MainActivityProduct.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(MainActivityProduct.this, "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(MainActivityProduct.this,
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();

        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                offset = 0;
                fetchProduct("");
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 3) {
                    offset = 0;
                    fetchProduct(query);
                } else if (query.length() == 0) {
                    fetchProduct("");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                if (query.length() > 3) {
                    offset = 0;
                    fetchProduct(query);
                } else if (query.length() == 0) {
                    fetchProduct("");
                }
                return false;
            }
        });
        if (getIntent() != null && getIntent().getBooleanExtra("isSearch", false)) {
            searchView.setIconified(false);
        }
        return true;
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
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
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
    public void onContactSelected(Product contact) {
        Intent intent = new Intent(MainActivityProduct.this, ProductUpdate.class);
        intent.putExtra("data", contact);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        offset = 0;
        fetchProduct("");
    }


    @Override
    public void onStatus(ShopFilterBean status) {
        selectedShop = status.getId();
        shopFilterAdapter.notifyData(selectedShop);
        offset = 0;
        fetchProduct("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle(productList.size() + " Nos");
    }
}
