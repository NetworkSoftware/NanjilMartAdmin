package pro.network.nanjilmartadmin.banner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.network.nanjilmartadmin.R;
import pro.network.nanjilmartadmin.app.AndroidMultiPartEntity;
import pro.network.nanjilmartadmin.app.AppController;
import pro.network.nanjilmartadmin.app.Appconfig;
import pro.network.nanjilmartadmin.app.GlideApp;
import pro.network.nanjilmartadmin.app.Imageutils;
import pro.network.nanjilmartadmin.categories.Categories;
import pro.network.nanjilmartadmin.categories.CategoriesUpdate;
import pro.network.nanjilmartadmin.categories.MainActivityCategories;
import pro.network.nanjilmartadmin.product.Product;

import static pro.network.nanjilmartadmin.app.Appconfig.BANNERS_CREATE;
import static pro.network.nanjilmartadmin.app.Appconfig.BANNERS_UPDATE;
import static pro.network.nanjilmartadmin.app.Appconfig.CATEGORIES_GET_ALL;
import static pro.network.nanjilmartadmin.app.Appconfig.PRODUCT_GET_ALL;
import static pro.network.nanjilmartadmin.app.Appconfig.PRODUCT_UPDATE;

public class  BannerRegister extends AppCompatActivity implements Imageutils.ImageAttachmentListener{


    String[] STOCKNAME = new String[]{"Loading"};
    private ProgressDialog pDialog;
    EditText description;
    MaterialBetterSpinner categories;
    String studentId = null;
    TextView submit;
    Imageutils imageutils;
    private ImageView profiletImage;
    private String imageUrl = "";
    TextInputEditText stock_name;
    private Banner banner = null;
    //
    String[] CATEGORY = new String[]{"Loading"};
    private Map<String, String> nameIdMap = new HashMap<>();
    private Map<String, String> idNameMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_register);

        imageutils = new Imageutils(this);
        getSupportActionBar().setTitle("Banner Register");
        profiletImage = (ImageView) findViewById(R.id.profiletImage);
        profiletImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageutils.imagepicker(1);
            }
        });

        categories = findViewById(R.id.categories);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, CATEGORY);
        categories.setAdapter(categoryAdapter);
        stock_name = findViewById(R.id.stock_name);
        stock_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    Appconfig.multiSelectionModule(BannerRegister.this,
                            "Select Stock Name", STOCKNAME, stock_name);
                }

            }
        });


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        getAllStocks();
        description=(EditText) findViewById(R.id.description);


        try {
            banner = (Banner) getIntent().getSerializableExtra("data");
            stock_name.setText(banner.stockname);
            description.setText(banner.description);
            studentId = banner.id;
            imageUrl=banner.banner;
            GlideApp.with(BannerRegister.this).load(banner.banner)
                    .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                    .into(profiletImage);

        } catch (Exception e) {
            Log.e("xxxxxxxxxxx", e.toString());
        }
        submit = (TextView) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (banner != null) {
                   updateUser();
                }else {
                    registerUser();
                }
            }
        });
        fetchCategory();

    }
    private void fetchCategory() {
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                CATEGORIES_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        CATEGORY = new String[jsonArray.length()];
                        nameIdMap = new HashMap<>();
                        idNameMap = new HashMap<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            nameIdMap.put(jsonObject.getString("title"),
                                    jsonObject.getString("id"));
                            idNameMap.put(jsonObject.getString("id"),
                                    jsonObject.getString("title"));
                            CATEGORY[i] = jsonObject.getString("title");
                        }
                        if(banner!=null){
                            categories.setText(idNameMap.get(banner.categories));
                        }


                        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(BannerRegister.this,
                                android.R.layout.simple_dropdown_item_1line, CATEGORY);
                        categories.setAdapter(stateAdapter);
                    } else {
                        Toast.makeText(BannerRegister.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(BannerRegister.this, "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(BannerRegister.this,
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq);
    }

    private void registerUser() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Uploading ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                BANNERS_CREATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
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
                localHashMap.put("banner", imageUrl);
                localHashMap.put("description", description.getText().toString());
                localHashMap.put("stockname",stock_name.getText().toString());
                localHashMap.put("categories", nameIdMap.get(categories.getText().toString()));
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq);
    }
    private void updateUser() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Uploading ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.PUT,
                BANNERS_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response.toString());
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
                localHashMap.put("id", studentId);
                localHashMap.put("banner", imageUrl);
                localHashMap.put("description", description.getText().toString());
                localHashMap.put("stockname",stock_name.getText().toString());
                localHashMap.put("categories", nameIdMap.get(categories.getText().toString()));
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq);
    }

    private void getAllStocks() {
        String tag_string_req = "req_register";
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                PRODUCT_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.d("Register Response: ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");

                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        STOCKNAME = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            STOCKNAME[i] = jsonObject.getString("id");
                        }

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
                localHashMap.put("user","ture");
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        String path = getCacheDir().getPath() + File.separator + "ImageAttach" + File.separator;
        imageutils.createImage(file, filename, path, false);
        String storedPath=imageutils.createImage(file, filename, path, false);
        pDialog.setMessage("Uploading...");
        showDialog();
        new UploadFileToServer().execute(Appconfig.compressImage(storedPath,BannerRegister.this));
    }

    private class UploadFileToServer extends AsyncTask<String, Integer, String> {
        String filepath;
        public long totalSize = 0;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero

            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pDialog.setMessage("Uploading..." + (String.valueOf(progress[0])));
        }

        @Override
        protected String doInBackground(String... params) {
            filepath = params[0];
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Appconfig.URL_IMAGE_UPLOAD);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filepath);
                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;

                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("Response from server: ", result);
            try {
                JSONObject jsonObject = new JSONObject(result.toString());
                if (!jsonObject.getBoolean("error")) {
                    GlideApp.with(getApplicationContext())
                            .load(filepath)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .skipMemoryCache(false)
                            .placeholder(R.drawable.nanjilmart)
                            .into(profiletImage);
                    imageUrl = Appconfig.ip + "/images/" + imageutils.getfilename_from_path(filepath);
                } else {
                    imageUrl = null;
                }
                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Image not uploaded", Toast.LENGTH_SHORT).show();
            }
            hideDialog();
            // showing the server response in an alert dialog
            //showAlert(result);


            super.onPostExecute(result);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageutils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
