package pro.network.nanjilmartadmin.categories;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import pro.network.nanjilmartadmin.R;
import pro.network.nanjilmartadmin.app.AndroidMultiPartEntity;
import pro.network.nanjilmartadmin.app.AppController;
import pro.network.nanjilmartadmin.app.Appconfig;
import pro.network.nanjilmartadmin.app.GlideApp;
import pro.network.nanjilmartadmin.app.Imageutils;

import static pro.network.nanjilmartadmin.app.Appconfig.CATEGORIES_CREATE;

public class CategoriesRegister extends AppCompatActivity implements Imageutils.ImageAttachmentListener{

        private ProgressDialog pDialog;

        EditText description;

        String studentId = null;

        TextView submit;
        Imageutils imageutils;
        private ImageView profiletImage;
        private String imageUrl = "";

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.categories_register);

            imageutils = new Imageutils(this);

            getSupportActionBar().setTitle("Categories Register");

            profiletImage = (ImageView) findViewById(R.id.profiletImage);
            profiletImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageutils.imagepicker(1);
                }
            });

            pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);

            description=(EditText) findViewById(R.id.description);
            submit = (TextView) findViewById(R.id.submit);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registerUser();
                }
            });


        }

        private void registerUser() {
            String tag_string_req = "req_register";
            pDialog.setMessage("Uploading ...");
             showDialog();
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    CATEGORIES_CREATE, new Response.Listener<String>() {
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
                    localHashMap.put("image", imageUrl);
                    localHashMap.put("title", description.getText().toString());
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
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            imageutils.request_permission_result(requestCode, permissions, grantResults);
        }

        @Override
        public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
            String path = getCacheDir().getPath() + File.separator + "ImageAttach" + File.separator;
            String storedPath=imageutils.createImage(file, filename, path, false);
            pDialog.setMessage("Uploading...");
            showDialog();
            new UploadFileToServer().execute(Appconfig.
                    compressImage(storedPath,CategoriesRegister.this));
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

        }
    }
