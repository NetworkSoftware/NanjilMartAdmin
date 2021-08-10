package pro.network.nanjilmartadmin.shopreg;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import static pro.network.nanjilmartadmin.app.Appconfig.CREATE_SHOP;

/**
 * Created by user_1 on 11-07-2018.
 */

public class ShopRegister extends AppCompatActivity {


    EditText shop_name, phone, latlong;
    MaterialBetterSpinner stock_update;
    String studentId = null;
    TextView submit;
    private ProgressDialog pDialog;
    private final String[] STOCKUPDATE = new String[]{
            "Available", "Currently Unavailable",
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_reg);

        getSupportActionBar().setTitle("Shop Register");

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        latlong = findViewById(R.id.latlong);
        shop_name = findViewById(R.id.shop_name);
        phone = findViewById(R.id.phone);
        stock_update = findViewById(R.id.stock_update);
        ArrayAdapter<String> stockAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, STOCKUPDATE);
        stock_update.setAdapter(stockAdapter);
        stock_update.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shop_name.getText().toString().length() <= 0) {
                    shop_name.setError("Select the Shop Name");
                } else if (phone.getText().toString().length() <= 0) {
                    phone.setError("Select the Phone");
                } else if (latlong.getText().toString().length() <= 0) {
                    latlong.setError("Enter the correct Location");
                } else if (stock_update.getText().toString().length() <= 0) {
                    stock_update.setError("Select the Sold or Not");
                } else {
                    registerUser();
                }

            }
        });


    }

    private void registerUser() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Uploading ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                CREATE_SHOP, new Response.Listener<String>() {
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
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();

                localHashMap.put("shop_name", shop_name.getText().toString());
                localHashMap.put("phone", phone.getText().toString());
                localHashMap.put("latlong", latlong.getText().toString());
                localHashMap.put("stock_update", stock_update.getText().toString());
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
