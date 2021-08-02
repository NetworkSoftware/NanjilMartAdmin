package pro.network.nanjilmartadmin.deliveryboy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

import static pro.network.nanjilmartadmin.app.Appconfig.DELIVERY_BOY_CHANGE_STATUS;
import static pro.network.nanjilmartadmin.app.Appconfig.DELIVERY_GET_ALL;


public class MainActivityDelivery extends AppCompatActivity {
    private static final String TAG = MainActivityDelivery.class.getSimpleName();
    ProgressDialog progressDialog;
    FloatingActionButton addDelivery;
    private RecyclerView recyclerView;
    private List<pro.network.nanjilmartadmin.deliveryboy.DeliveryBean> deliveryBeans;
    private pro.network.nanjilmartadmin.deliveryboy.DeliveryAdapter deliveryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_main_list);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Delivery Boy");


        recyclerView = findViewById(R.id.deliveryList);
        deliveryBeans = new ArrayList<>();
        deliveryAdapter = new pro.network.nanjilmartadmin.deliveryboy.DeliveryAdapter(this, deliveryBeans, new OnDeliveryBoy() {
            @Override
            public void onStatusClick(int position, String status) {
                statusChange(deliveryBeans.get(position).getId(), status);
            }

            @Override
            public void onEditClick(pro.network.nanjilmartadmin.deliveryboy.DeliveryBean deliveryBeans) {
                Intent intent = new Intent(MainActivityDelivery.this, pro.network.nanjilmartadmin.deliveryboy.CreateDeliveryBoy.class);
                intent.putExtra("data", deliveryBeans);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteFile(position);
            }
        });
        addDelivery = findViewById(R.id.addDelivery);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(addManager1);
        recyclerView.setAdapter(deliveryAdapter);

        addDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityDelivery.this, pro.network.nanjilmartadmin.deliveryboy.CreateDeliveryBoy.class);
                startActivity(intent);
            }
        });


        fetchContacts();
    }

    private void fetchContacts() {
        String tag_string_req = "req_register";
        progressDialog.setMessage("Processing ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                DELIVERY_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        deliveryBeans = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            pro.network.nanjilmartadmin.deliveryboy.DeliveryBean deliveryBean = new pro.network.nanjilmartadmin.deliveryboy.DeliveryBean();
                            deliveryBean.setId(jsonObject.getString("id"));
                            deliveryBean.setProfileImage(jsonObject.getString("image"));
                            deliveryBean.setName(jsonObject.getString("name"));
                            deliveryBean.setPhone(jsonObject.getString("phone"));
                            deliveryBean.setLicense(jsonObject.getString("license"));
                            deliveryBean.setAdharcard(jsonObject.getString("adharcard"));
                            deliveryBean.setPassword(jsonObject.getString("password"));
                            deliveryBean.setStatus(jsonObject.getString("status"));
                            deliveryBeans.add(deliveryBean);
                        }
                        deliveryAdapter.notifyData(deliveryBeans);
                        getSupportActionBar().setSubtitle(deliveryBeans.size() + "Nos");
                    } else {
                        Toast.makeText(getApplication(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplication(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        fetchContacts();

    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


    private void statusChange(final String id, final String status) {
        String tag_string_req = "req_register";
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                DELIVERY_BOY_CHANGE_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        fetchContacts();
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
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(getApplication(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("id", id);
                localHashMap.put("status", status);
                return localHashMap;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void deleteFile(final int position) {
        String tag_string_req = "req_register";
        progressDialog.setMessage("Fetching ...");
        showDialog();
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Appconfig.DELETE_DELIVERYBOY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getBoolean("success")) {
                        deliveryBeans.remove(position);
                        deliveryAdapter.notifyData(deliveryBeans);
                    }
                    Toast.makeText(MainActivityDelivery.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(MainActivityDelivery.this, "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(MainActivityDelivery.this,
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("id", deliveryBeans.get(position).id);
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}
