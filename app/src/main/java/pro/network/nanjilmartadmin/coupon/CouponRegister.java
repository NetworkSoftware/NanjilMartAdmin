package pro.network.nanjilmartadmin.coupon;

import static pro.network.nanjilmartadmin.app.Appconfig.COUPON;
import static pro.network.nanjilmartadmin.app.Appconfig.mypreference;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pro.network.nanjilmartadmin.R;
import pro.network.nanjilmartadmin.app.AppController;
import pro.network.nanjilmartadmin.app.Appconfig;


public class CouponRegister extends AppCompatActivity {


    private final String[] STOCKUPDATE = new String[]{
            "0", "1",
    };
    EditText coupon;
    EditText amt;
    EditText description;
    MaterialBetterSpinner status;
    TextView submit;
    EditText percentage,minimumOrder,maxNumbers;
    RadioButton rupeesBtn, percentBtn;
    private ProgressDialog pDialog;
    private CheckBox isNotify,isHidden;
    SharedPreferences sharedpreferences;
    String couponId = null;
    private CouponProduct couponProduct = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_register);

        getSupportActionBar().setTitle("Coupon Register");

        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        isNotify=findViewById(R.id.isNotify);
        isHidden=findViewById(R.id.isHidden);

        amt = findViewById(R.id.amt);
        description = findViewById(R.id.description);

        coupon = findViewById(R.id.coupon);
        rupeesBtn = findViewById(R.id.rupeesBtn);
        percentBtn = findViewById(R.id.percentBtn);
        percentage = findViewById(R.id.percentage);
        minimumOrder=findViewById(R.id.minimumOrder);
        maxNumbers=findViewById(R.id.maxNumbers);

        rupeesBtn.setChecked(true);

        rupeesBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    percentage.setVisibility(View.GONE);
                    rupeesBtn.setChecked(true);
                    percentBtn.setChecked(false);
                } else {
                    rupeesBtn.setChecked(false);
                    percentBtn.setChecked(true);
                    percentage.setVisibility(View.VISIBLE);
                }
            }
        });

        percentBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    percentage.setVisibility(View.VISIBLE);
                    rupeesBtn.setChecked(false);
                    percentBtn.setChecked(true);
                } else {
                    rupeesBtn.setChecked(true);
                    percentBtn.setChecked(false);
                    percentage.setVisibility(View.GONE);
                }
            }
        });

        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (amt.getText().toString().length() <= 0) {
                    amt.setError("Select the Amount");
                } else if (description.getText().toString().length() <= 0) {
                    description.setError("Enter the Description");
                } else if (coupon.getText().toString().length() <= 0) {
                    coupon.setError("Enter the Coupon");
                } else if (status.getText().toString().length() <= 0) {
                    status.setError("Enter the Status");
                } else if (minimumOrder.getText().toString().length() <= 0) {
                    minimumOrder.setError("Enter the Minimum order value");
                } else if (percentBtn.isChecked() && percentage.getText().toString().length() <= 0) {
                    percentage.setError("Enter the percentage");
                } else {

                    registerUser();
                }

            }
        });
        status = findViewById(R.id.status);

        ArrayAdapter<String> stockAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, STOCKUPDATE);
        status.setAdapter(stockAdapter);
        status.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        try {
            couponProduct = (CouponProduct) getIntent().getSerializableExtra("data");
            couponId = couponProduct.id;
            amt.setText(couponProduct.amt);
            description.setText(couponProduct.description);
            status.setText(couponProduct.status);
            coupon.setText(couponProduct.coupon);
            isHidden.setChecked(couponProduct.getHide() != null && couponProduct.getHide().equalsIgnoreCase("1"));
            description.setText(couponProduct.description);
            maxNumbers.setText(couponProduct.maxNumbers == null ? "1" : couponProduct.maxNumbers);
            minimumOrder.setText(couponProduct.minimumOrder == null ? "100" : couponProduct.minimumOrder);
            if (couponProduct.isPercent == null || couponProduct.isPercent.equalsIgnoreCase("0")) {
                percentage.setVisibility(View.GONE);
            } else {
                percentage.setVisibility(View.VISIBLE);
            }
            percentage.setText(couponProduct.getPercentage());

        } catch (Exception e) {
            Log.e("xxxxxxxxxxx", e.toString());
        }
    }

    private void registerUser() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Createing ...");
        showDialog();
        int method = Request.Method.POST;
        if (couponId != null) {
            method = Request.Method.PUT;
        }
        String url = COUPON;
        StringRequest strReq = new StringRequest(method,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String msg = jsonObject.getString("message");
                    if (success) {
                        finish();
                    }
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                if (couponProduct != null) {
                    localHashMap.put("id", couponId);
                }
                localHashMap.put("description", description.getText().toString());
                localHashMap.put("amt", amt.getText().toString());
                localHashMap.put("status", status.getText().toString());
                localHashMap.put("isPercent", percentBtn.isChecked() ? "1" : "0");
                localHashMap.put("isHide", isHidden.isChecked() ? "1" : "0");
                localHashMap.put("percentage", percentage.getText().toString());
                localHashMap.put("coupon", coupon.getText().toString());
                localHashMap.put("miniorder", minimumOrder.getText().toString());
                localHashMap.put("maxNumbers",maxNumbers.getText().toString());
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    protected void onPause() {
        super.onPause();
        hideDialog();
    }


}
