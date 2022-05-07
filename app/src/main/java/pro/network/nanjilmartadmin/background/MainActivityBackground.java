package pro.network.nanjilmartadmin.background;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pro.network.nanjilmartadmin.R;
import pro.network.nanjilmartadmin.app.AppController;
import pro.network.nanjilmartadmin.app.Appconfig;
import yuku.ambilwarna.AmbilWarnaDialog;

import static pro.network.nanjilmartadmin.app.Appconfig.CHANGEBG;

public class MainActivityBackground extends AppCompatActivity{
    LinearLayout  mLayout;
    TextView startColor,endColor;
    String topColor="0",bottomColor="0";
    int colorVal = 0;
     Button button;
    String colorId = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_background);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Background");

        button = findViewById(R.id.button);
        startColor = findViewById(R.id.startColor);
        endColor = findViewById(R.id.endColor);
        mLayout = findViewById(R.id.layout);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(topColor.equalsIgnoreCase("0"))||
                        !(bottomColor.equalsIgnoreCase("0"))){
                    updateColor();
                }else {
                    Toast.makeText(getApplicationContext(), "Select valid color", Toast.LENGTH_SHORT).show();
                }

            }
        });
        startColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker("start");
            }
        });
        endColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker("end");
            }
        });
        fetchColors();
    }

    private void fetchColors() {
        String tag_string_req = "req_register";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                CHANGEBG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");
                    if (success) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            colorId = jsonObject.getString("id");
                            if(colorId!=null){
//                                String start = jsonObject.getString("colorDark");
//                                int startInt = Color.parseColor(start);
//                                startColor.setBackgroundColor(startInt);
//                                String end = jsonObject.getString("colorDark");
//                                int endInt = Color.parseColor(end);
//                                endColor.setBackgroundColor(endInt);
                            }
                        }

                    } else {
                        Toast.makeText(MainActivityBackground.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(MainActivityBackground.this, "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(MainActivityBackground.this,
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



    private void updateColor() {
        String tag_string_req = "req_register";
        StringRequest strReq = new StringRequest(Request.Method.PUT,
                CHANGEBG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                if(colorId!=null){
                    localHashMap.put("id", colorId);
                }
                localHashMap.put("colorDark", topColor+"");
                localHashMap.put("colorLight", bottomColor+"");
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq);
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
    }


    public void openColorPicker(String colors) {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, colorVal,
                new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                int  n;
                if("start".equalsIgnoreCase(colors)){
                    n = color;
                    Log.e("xxxx_color_top","#"+Integer.toHexString(n));
                    startColor.setBackgroundColor(color);
                    topColor = "#"+Integer.toHexString(n);
                } else{
                    n = color;
                    Log.e("xxxx_color_bottom","#"+Integer.toHexString(n));
                    endColor.setBackgroundColor(color);
                    bottomColor = "#"+Integer.toHexString(n);
                }
            }
        });
        colorPicker.show();
    }
}