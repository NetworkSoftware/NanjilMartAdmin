package pro.network.nanjilmartadmin.product;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
import pro.network.nanjilmartadmin.app.ActivityMediaOnline;
import pro.network.nanjilmartadmin.app.AndroidMultiPartEntity;
import pro.network.nanjilmartadmin.app.AppController;
import pro.network.nanjilmartadmin.app.Appconfig;
import pro.network.nanjilmartadmin.app.Imageutils;

import static pro.network.nanjilmartadmin.app.Appconfig.CATEGORIES_GET_ALL;
import static pro.network.nanjilmartadmin.app.Appconfig.CATEGORY;
import static pro.network.nanjilmartadmin.app.Appconfig.DATA_FETCH_ALL_SHOP;
import static pro.network.nanjilmartadmin.app.Appconfig.PRODUCT_CREATE;
import static pro.network.nanjilmartadmin.app.Appconfig.PRODUCT_DELETE;
import static pro.network.nanjilmartadmin.app.Appconfig.PRODUCT_UPDATE;
import static pro.network.nanjilmartadmin.app.Appconfig.SHOPNAME;

/**
 * Created by user_1 on 11-07-2018.
 */

public class ProductUpdate extends AppCompatActivity implements Imageutils.ImageAttachmentListener, ImageClick {


    private final String[] STOCKUPDATE = new String[]{
            "In Stock", "Currently Unavailable",
    };

    AutoCompleteTextView brand;
    EditText model;
    EditText price;
    EditText description;
    AddImageAdapter maddImageAdapter;
    MaterialBetterSpinner category;
    MaterialBetterSpinner shopname;
    MaterialBetterSpinner stock_update;
    String studentId = null;
    TextView submit;
    Imageutils imageutils;
    ImageView image_placeholder, image_wallpaper;
    CardView itemsAdd;
    Map<String, String> shopIdName = new HashMap<>();
    private ProgressDialog pDialog;
    private RecyclerView imagelist;
    private ArrayList<String> samplesList = new ArrayList<>();
    private String imageUrl = "";
    private Product contact = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_register);
        imageutils = new Imageutils(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        itemsAdd = findViewById(R.id.itemsAdd);
        ImageView image_wallpaper = findViewById(R.id.image_wallpaper);
        image_wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageutils.imagepicker(1);
            }
        });
        samplesList = new ArrayList<>();
        imagelist = findViewById(R.id.imagelist);
        maddImageAdapter = new AddImageAdapter(this, samplesList, this);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        imagelist.setLayoutManager(addManager1);
        imagelist.setAdapter(maddImageAdapter);
        category = findViewById(R.id.category);


        model = findViewById(R.id.model);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);


        shopname = findViewById(R.id.shopname);
        ArrayAdapter<String> shopAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SHOPNAME);
        shopname.setAdapter(shopAdapter);
        shopname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(ProductUpdate.this,
                        android.R.layout.simple_dropdown_item_1line, Appconfig.getSubCatFromCat(SHOPNAME[position]));
                shopname.setAdapter(brandAdapter);
                shopname.setThreshold(1);
            }
        });

        stock_update = findViewById(R.id.stock_update);

        ArrayAdapter<String> stockAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, STOCKUPDATE);
        stock_update.setAdapter(stockAdapter);
        stock_update.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        brand = findViewById(R.id.brand);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, CATEGORY);
        category.setAdapter(categoryAdapter);
        category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                shopname.setVisibility(View.GONE);
                ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(ProductUpdate.this,
                        android.R.layout.simple_dropdown_item_1line, new String[0]);
                if (category.getText().toString().equalsIgnoreCase(("COSMETICS"))) {
                    brandAdapter = new ArrayAdapter<String>(ProductUpdate.this,
                            android.R.layout.simple_dropdown_item_1line, Appconfig.getSubCatFromCat(CATEGORY[i]));
                } else if (category.getText().toString().equalsIgnoreCase(("FOOD"))) {
                    shopname.setVisibility(View.VISIBLE);
                }
                brand.setAdapter(brandAdapter);
                brand.setThreshold(1);
                if (contact != null) {
                    brand.setText(contact.brand);
                } else {
                    brand.setText("");
                }
            }
        });

        submit = findViewById(R.id.submit);
        submit.setText("SUBMIT");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (category.getText().toString().length() <= 0) {
                    category.setError("Select the Category");
                } else if (brand.getText().toString().length() <= 0) {
                    brand.setError("Enter the Brand");
                } else if (model.getText().toString().length() <= 0) {
                    model.setError("Enter the Model");
                } else if (price.getText().toString().length() <= 0) {
                    price.setError("Enter the Price");
                } else if (stock_update.getText().toString().length() <= 0) {
                    stock_update.setError("Select the Sold or Not");
                } else if (samplesList.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "Upload the Images!", Toast.LENGTH_SHORT).show();
                } else {

                    registerUser();
                }
            }
        });


        try {
            //  submit.setText("UPDATE");
            //   getSupportActionBar().setTitle("Stock Update");
            contact = (Product) getIntent().getSerializableExtra("data");
            category.setText(contact.category);
            brand.setText(contact.brand);
            model.setText(contact.model);
            price.setText(contact.price);
            description.setText(contact.description);
            studentId = contact.id;
            stock_update.setText(contact.stock_update);
            shopname.setText(contact.shopname);
            imageUrl = contact.image;

            if (category.getText().toString().equalsIgnoreCase(("FOOD"))) {
                shopname.setVisibility(View.VISIBLE);
            } else {
                shopname.setVisibility(View.GONE);
            }

            if (imageUrl == null) {
                imageUrl = "";
            } else {
                samplesList = new Gson().fromJson(imageUrl, (Type) List.class);
            }
            maddImageAdapter.notifyData(samplesList);

        } catch (Exception e) {
            Log.e("xxxxxxxxxxx", e.toString());

        }
        getAllCategories();
        getAllShopname();

    }

    private void registerUser() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Updateing ...");
        showDialog();
        String url = PRODUCT_CREATE;
        if (contact != null) {
            url = PRODUCT_UPDATE;
        }
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                hideDialog();
                try {
                    String val = response.contains("0000") ? response.split("0000")[1] : response;
                    JSONObject jsonObject = new JSONObject(val);
                    boolean success = jsonObject.getBoolean("success");
                    String msg = jsonObject.getString("message");
                    if (success) {
                        final String shopname = model.getText().toString();
                        sendNotification(brand.getText().toString() + " " + price.getText().toString()
                                , shopname.length() > 30 ? shopname.substring(0, 29) + "..." :
                                        shopname, description.getText().toString());
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
                localHashMap.put("category", category.getText().toString());
                localHashMap.put("sub_category", "sub_category");
                localHashMap.put("shopname", shopIdName.containsKey(shopname)?
                        shopIdName.get(shopname.getText().toString()):"NA");
                localHashMap.put("brand", brand.getText().toString());
                localHashMap.put("model", model.getText().toString());
                localHashMap.put("price", price.getText().toString());
                localHashMap.put("stock_update", stock_update.getText().toString());
                if (contact != null) {
                    localHashMap.put("id", studentId);
                }
                localHashMap.put("image", new Gson().toJson(samplesList));
                localHashMap.put("description", description.getText().toString());
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq);
    }

    private void getAllCategories() {
        String tag_string_req = "req_register";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                CATEGORIES_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");

                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        CATEGORY = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CATEGORY[i] = jsonArray.getJSONObject(i).getString("title");
                        }
                        ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(ProductUpdate.this,
                                android.R.layout.simple_dropdown_item_1line, CATEGORY);
                        category.setAdapter(titleAdapter);
                    }
                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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

    private void deleteUser() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Processing ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                PRODUCT_DELETE, new Response.Listener<String>() {
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
                localHashMap.put("id", studentId);
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq);
    }

    private void sendNotification(String s, String title, String description) {
        String tag_string_req = "req_register";
        showDialog();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("to", "/topics/allDevices");
            jsonObject.put("priority", "high");
            JSONObject dataObject = new JSONObject();
            dataObject.put("title", title);
            dataObject.put("message", description);
            jsonObject.put("data", dataObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Register Response: ", response.toString());
                hideDialog();
                finish();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                return localHashMap;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("Content-Type", "application/json");
                hashMap.put("Authorization", "key=AAAAbHOKlYE:APA91bHduvDJpa5uS6oEtFH5y4-1Q2CK_3O0w4sJpaTRV4ALn2EAOpcKublZMKY1Qq7e-8M1hfM5rT0pJRErmg5790bjS82WGdXS_5rtBHZCbwQ-YLvMRPBjqn6LTL168tTjx6skLII_");
                return hashMap;
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        String path = getCacheDir().getPath() + File.separator + "ImageAttach" + File.separator;
        imageutils.createImage(file, filename, path, false);
        String storedPath = imageutils.createImage(file, filename, path, false);
        pDialog.setMessage("Uploading...");
        showDialog();
        new UploadFileToServer().execute(Appconfig.compressImage(storedPath, ProductUpdate.this));
    }

    @Override
    public void onImageClick(int position) {

        Intent localIntent = new Intent(ProductUpdate.this, ActivityMediaOnline.class);
        localIntent.putExtra("filePath", samplesList.get(position));
        localIntent.putExtra("isImage", true);
        startActivity(localIntent);
    }


    @Override
    public void onDeleteClick(int position) {
        samplesList.remove(position);
        maddImageAdapter.notifyData(samplesList);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageutils.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_delete:
                AlertDialog diaBox = AskOption();
                diaBox.show();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_delete_black_24dp)

                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        dialog.dismiss();
                        deleteUser();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }

    private void getAllShopname() {
        String tag_string_req = "req_register";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                DATA_FETCH_ALL_SHOP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");

                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        SHOPNAME = new String[jsonArray.length()];
                        shopIdName = new HashMap<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            SHOPNAME[i] = jsonArray.getJSONObject(i).getString("shop_name");
                            shopIdName.put(jsonArray.getJSONObject(i).getString("shop_name"),
                                    jsonArray.getJSONObject(i).getString("id"));
                        }
                        ArrayAdapter<String> shopAdapter = new ArrayAdapter<String>(ProductUpdate.this,
                                android.R.layout.simple_dropdown_item_1line, SHOPNAME);
                        shopname.setAdapter(shopAdapter);
                    }
                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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

    private class UploadFileToServer extends AsyncTask<String, Integer, String> {
        public long totalSize = 0;
        String filepath;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero

            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pDialog.setMessage("Uploading..." + (progress[0]));
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
                JSONObject jsonObject = new JSONObject(result);
                if (!jsonObject.getBoolean("error")) {
                    imageUrl = Appconfig.ip + "/images/" + imageutils.getfilename_from_path(filepath);
                    samplesList.add(imageUrl);
                    maddImageAdapter.notifyData(samplesList);
                } else {
                    imageUrl = null;
                }
                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (Error | Exception e) {
                Toast.makeText(getApplicationContext(), "Image not uploaded", Toast.LENGTH_SHORT).show();
            }
            hideDialog();
            // showing the server response in an alert dialog
            //showAlert(result);


            super.onPostExecute(result);
        }

    }

}



