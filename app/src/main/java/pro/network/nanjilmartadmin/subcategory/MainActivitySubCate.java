package pro.network.nanjilmartadmin.subcategory;

import static pro.network.nanjilmartadmin.app.Appconfig.CATEGORIES_GET_ALL;
import static pro.network.nanjilmartadmin.app.Appconfig.SUBCATEGORIE;

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
import pro.network.nanjilmartadmin.categories.Categories;
import pro.network.nanjilmartadmin.categories.CategoriesAdapter;
import pro.network.nanjilmartadmin.categories.CategoriesClick;
import pro.network.nanjilmartadmin.categories.CategoriesRegister;
import pro.network.nanjilmartadmin.categories.CategoriesUpdate;

public class MainActivitySubCate extends AppCompatActivity implements CategoriesClick {
    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private List<SubCategory> subCategoryList;
    private SubCateAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_subcate);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sub Categories");


        recyclerView = findViewById(R.id.recycler_view);
        subCategoryList = new ArrayList<>();
        mAdapter = new SubCateAdapter(this, subCategoryList, this);
        final LinearLayoutManager addManager1 = new GridLayoutManager(MainActivitySubCate.this, 2);
        recyclerView.setLayoutManager(addManager1);
        recyclerView.setAdapter(mAdapter);

        whiteNotificationBar(recyclerView);

        FloatingActionButton addStock = findViewById(R.id.addbanner);
        addStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivitySubCate.this, SubCateReg.class);
                startActivity(intent);
            }
        });
    }

    /**
     * fetches json by making http calls
     */
    private void fetchContacts() {
        String tag_string_req = "req_register";
        progressDialog.setMessage("Processing ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                SUBCATEGORIE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    if (success) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        subCategoryList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            SubCategory subCategory = new SubCategory();
                            subCategory.setId(jsonObject.getString("id"));
                            subCategory.setName(jsonObject.getString("name"));
                            subCategory.setImage(jsonObject.getString("image"));
                            subCategory.setCreatedOn(jsonObject.getString("createdOn"));
                            subCategoryList.add(subCategory);
                        }
                        mAdapter.notifyData(subCategoryList);
                        getSupportActionBar().setSubtitle(subCategoryList.size() + " Nos");

                    } else {
                        Toast.makeText(MainActivitySubCate.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(MainActivitySubCate.this, "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(MainActivitySubCate.this,
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
        Intent intent = new Intent(MainActivitySubCate.this, SubCateReg.class);
        intent.putExtra("data", subCategoryList.get(position));
        startActivity(intent);
    }

    private void deleteFile(final int position) {
        String tag_string_req = "req_register";
        progressDialog.setMessage("Fetching ...");
        showDialog();
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.DELETE,
                SUBCATEGORIE+"?id="+subCategoryList.get(position).id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getBoolean("success")) {
                        fetchContacts();
                    }
                    Toast.makeText(MainActivitySubCate.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(MainActivitySubCate.this, "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(MainActivitySubCate.this,
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
                hideDialog();
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

}
