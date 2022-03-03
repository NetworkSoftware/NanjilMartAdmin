package pro.network.nanjilmartadmin.deliveryboy;

import static pro.network.nanjilmartadmin.app.Appconfig.DELIVERY_BOY_CHANGE_STATUS;
import static pro.network.nanjilmartadmin.app.Appconfig.DELIVERY_GET_ALL;
import static pro.network.nanjilmartadmin.app.Appconfig.WALLET;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
import pro.network.nanjilmartadmin.wallet.WalletActivity;


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
            public void onWalletClick(DeliveryBean deliveryBean) {
                showCashBack(deliveryBean);
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

            @Override
            public void onHistoryClick(DeliveryBean deliveryBean) {
                Intent intent = new Intent(MainActivityDelivery.this, WalletActivity.class);
                intent.putExtra("id", deliveryBean.id);
                startActivity(intent);
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


        fetchDBoy();
    }

    private void fetchDBoy() {
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
                            deliveryBean.setWalletAmt(String.valueOf(jsonObject.getInt("walletAmt")));
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
        fetchDBoy();

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
                        fetchDBoy();
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

    private void showCashBack(final DeliveryBean deliveryBean) {
        final RoundedBottomSheetDialog mBottomSheetDialog = new RoundedBottomSheetDialog(MainActivityDelivery.this);
        LayoutInflater inflater = MainActivityDelivery.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.bottom_amount_layout, null);

        TextInputLayout reviewTxt = dialogView.findViewById(R.id.walletTxt);
        final TextInputEditText walletEdit = dialogView.findViewById(R.id.wallet);
        final TextInputEditText description = dialogView.findViewById(R.id.description);


        final RadioButton radioIn = dialogView.findViewById(R.id.radioIn);
        final RadioButton radioOut = dialogView.findViewById(R.id.radioOut);
        radioIn.setChecked(true);

        radioIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioIn.setChecked(true);
                    radioOut.setChecked(false);
                } else {
                    radioIn.setChecked(false);
                    radioOut.setChecked(true);
                }
            }
        });
        radioOut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioIn.setChecked(false);
                    radioOut.setChecked(true);
                } else {
                    radioIn.setChecked(true);
                    radioOut.setChecked(false);
                }
            }
        });

        final Button submit = dialogView.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walletEdit.getText().toString().length() <= 0 ||
                        description.getText().toString().length() <= 0) {
                    Toast.makeText(MainActivityDelivery.this, "Enter Valid Cashback", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    updateWallet(deliveryBean.getId(), walletEdit.getText().toString(), description.getText().toString(),
                            radioIn.isChecked(), mBottomSheetDialog);
                }
            }
        });
        mBottomSheetDialog.setContentView(dialogView);
        walletEdit.requestFocus();
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mBottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RoundedBottomSheetDialog d = (RoundedBottomSheetDialog) dialog;
                        FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
                        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }, 0);
            }
        });
        mBottomSheetDialog.show();
    }

    private void updateWallet(final String userId, final String wallet, final String description, final boolean isCredit,
                              final RoundedBottomSheetDialog mBottomSheetDialog) {
        String tag_string_req = "req_register";
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.PUT,
                WALLET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.d("Register Response: ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    Toast.makeText(getApplication(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        if (mBottomSheetDialog != null) {
                            mBottomSheetDialog.cancel();
                        }
                        fetchDBoy();
                    }
                } catch (Exception e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(getApplication(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(getApplication(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("dboyId", userId);
                localHashMap.put("amt", wallet);
                localHashMap.put("description", description);
                localHashMap.put("credit", "add");
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
