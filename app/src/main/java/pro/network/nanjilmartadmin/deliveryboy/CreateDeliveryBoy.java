package pro.network.nanjilmartadmin.deliveryboy;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import pro.network.nanjilmartadmin.R;
import pro.network.nanjilmartadmin.app.AndroidMultiPartEntity;
import pro.network.nanjilmartadmin.app.AppController;
import pro.network.nanjilmartadmin.app.Appconfig;
import pro.network.nanjilmartadmin.app.BaseActivity;
import pro.network.nanjilmartadmin.app.GlideApp;
import pro.network.nanjilmartadmin.app.Imageutils;

import static pro.network.nanjilmartadmin.app.Appconfig.CREATE_DELIVERYBOY;
import static pro.network.nanjilmartadmin.app.Appconfig.UPDATE_DELIVERYBOY;

public class CreateDeliveryBoy extends BaseActivity implements Imageutils.ImageAttachmentListener {
    private static final int FINE_LOCATION_CODE = 199;
    private final String TAG = getClass().getSimpleName();
    Imageutils imageutils;
    ImageView image_adharcard, image_placeholder_adharcard;
    ImageView image_license, image_placeholder_license;
    ImageView image_profile, image_placeholder_profile;
    TextInputEditText name;
    TextInputEditText phone;
    TextInputEditText password;
    TextInputLayout nameText;
    TextInputLayout phoneText;
    TextInputLayout passwordText;
    CardView itemsAddprofile;
    CardView itemsAddlicense;
    CardView itemsAddadharcard;
    private String imageUrlProfile = "";
    private String imageUrllicense = "";
    private String imageUrladharcard = "";
    String studentId = null;
    ProgressDialog pDialog;

    @Override
    protected void startDemo() {
        setContentView(R.layout.delivery_register);

        if (ContextCompat.checkSelfPermission(CreateDeliveryBoy.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CreateDeliveryBoy.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
        }
        imageutils = new Imageutils(this);

        getSupportActionBar().setTitle("Delivery Boy - Register");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        image_adharcard = findViewById(R.id.image_adharcard);
        image_license = findViewById(R.id.image_license);
        image_profile = findViewById(R.id.image_profile);
        image_placeholder_adharcard = findViewById(R.id.image_placeholder_adharcard);
        image_placeholder_license = findViewById(R.id.image_placeholder_license);
        image_placeholder_profile = findViewById(R.id.image_placeholder_profile);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);

        itemsAddprofile = findViewById(R.id.itemsAddprofile);
        itemsAddlicense = findViewById(R.id.itemsAddlicense);
        itemsAddadharcard = findViewById(R.id.itemsAddadharcard);


        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageutils.imagepicker(1);
            }
        });

        image_license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageutils.imagepicker(2);
            }
        });

        image_adharcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageutils.imagepicker(3);
            }
        });


        ExtendedFloatingActionButton submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().length() > 0 &&
                        phone.getText().toString().length() > 0 &&
                        password.getText().toString().length() > 0
                        && imageUrladharcard != null
                        && imageUrllicense != null
                        && imageUrlProfile != null
                ) {
                    registerUser();
                }
            }

        });
        try {

            DeliveryBean contact = (DeliveryBean) getIntent().getSerializableExtra("data");

            studentId = contact.id;
            name.setText(contact.name);
            phone.setText(contact.phone);
            password.setText(contact.password);
            GlideApp.with(getApplicationContext())
                    .load(contact.profileImage)
                    .into(image_profile);
            imageUrlProfile = contact.profileImage;
            GlideApp.with(getApplicationContext())
                    .load(contact.license)
                    .into(image_license);
            imageUrllicense = contact.license;
            GlideApp.with(getApplicationContext())
                    .load(contact.adharcard)
                    .into(image_adharcard);
            imageUrladharcard = contact.adharcard;

        } catch (Exception e) {
            Log.e("xxxxxxxxxxx", e.toString());
        }

    }


    private void registerUser() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Processing ...");
        String url = CREATE_DELIVERYBOY;
        if (studentId != null) {
            url = UPDATE_DELIVERYBOY;
        }
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
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
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                if (studentId != null) {
                    localHashMap.put("id", studentId);
                }
                localHashMap.put("name", name.getText().toString());
                localHashMap.put("phone", phone.getText().toString());
                localHashMap.put("image", imageUrlProfile);
                localHashMap.put("license", imageUrllicense);
                localHashMap.put("adharcard", imageUrladharcard);
                localHashMap.put("password", password.getText().toString());
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            return;
        }
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        imageutils.createImage(file, filename, path, false);
        pDialog.setMessage("Uploading...");
        showDialog();
        new UploadFileToServer().execute(imageutils.getPath(uri) + "@" + from);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageutils.onActivityResult(requestCode, resultCode, data);

    }

    private class UploadFileToServer extends AsyncTask<String, Integer, String> {
        public long totalSize = 0;
        String filepath;
        String type;

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
            filepath = params[0].split("@")[0];
            type = params[0].split("@")[1];
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Appconfig.URL_IMAGE_UPLOAD);

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

            } catch (Exception e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("Response from server: ", result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (type.equalsIgnoreCase("1")) {
                    if (!jsonObject.getBoolean("error")) {
                        GlideApp.with(getApplicationContext())
                                .load(filepath)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .skipMemoryCache(false)
                                .into(image_profile);
                        imageUrlProfile = Appconfig.IMAGE_URL + imageutils.getfilename_from_path(filepath);
                    } else {
                        imageUrlProfile = null;
                    }
                } else if (type.equalsIgnoreCase("2")) {
                    if (!jsonObject.getBoolean("error")) {
                        GlideApp.with(getApplicationContext())
                                .load(filepath)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .skipMemoryCache(false)
                                .into(image_license);
                        imageUrllicense = Appconfig.IMAGE_URL + imageutils.getfilename_from_path(filepath);
                    } else {
                        imageUrllicense = null;
                    }
                } else if (type.equalsIgnoreCase("3")) {
                    if (!jsonObject.getBoolean("error")) {
                        GlideApp.with(getApplicationContext())
                                .load(filepath)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .skipMemoryCache(false)
                                .into(image_adharcard);
                        imageUrladharcard = Appconfig.IMAGE_URL + imageutils.getfilename_from_path(filepath);
                    } else {
                        imageUrladharcard = null;
                    }
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
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}


