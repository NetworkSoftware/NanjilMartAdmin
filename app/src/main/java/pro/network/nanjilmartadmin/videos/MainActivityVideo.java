package pro.network.nanjilmartadmin.videos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rahul.media.main.MediaFactory;

import net.alhazmy13.mediapicker.Video.VideoPicker;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.network.nanjilmartadmin.R;
import pro.network.nanjilmartadmin.app.AndroidMultiPartEntity;
import pro.network.nanjilmartadmin.app.AppController;
import pro.network.nanjilmartadmin.app.Appconfig;


public class MainActivityVideo extends AppCompatActivity implements AdClick {

    AdvideoAdapter shopAdapter;
    List<String> shopList = new ArrayList<>();
    ProgressDialog progressDialog;
    private MediaFactory mediaFactory;
    private ArrayList<String> all_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_img_main);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        final FloatingActionButton addShop = findViewById(R.id.addShop);

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        shopAdapter = new AdvideoAdapter(shopList, this, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(shopAdapter);
        addShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker();
            }
        });

        //getAllStaff();

    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


    private void getAllStaff() {
        String tag_string_req = "req_register";
        progressDialog.setMessage("Fetching ...");
        showDialog();
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Appconfig.VIDEO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                hideDialog();
                try {
                    shopList = new ArrayList<>();
                    JSONObject jObj = new JSONObject(response);
                    String files = jObj.getString("files");
                    String[] filesStrings = files.split(",");
                    for (int i = 0; i < filesStrings.length; i++) {
                        if (filesStrings[i] != null &&
                                (filesStrings[i].contains(".mp4")
                                        /*|| filesStrings[i].contains(".png")
                                        || filesStrings[i].contains(".PNG")
                                        || filesStrings[i].contains(".JPEG")*/)) {
                            shopList.add(filesStrings[i]);
                        }
                    }
                    shopAdapter.notifyData(shopList);

                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(MainActivityVideo.this, "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(MainActivityVideo.this,
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


    private void deleteFile(final int position) {
        String tag_string_req = "req_register";
        progressDialog.setMessage("Fetching ...");
        showDialog();
        // showDialog();
        StringRequest strReq = new StringRequest(Request.Method.DELETE,
                Appconfig.VIDEO + "?name=" + shopList.get(position), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (jObj.getInt("success") == 1) {
                        shopList.remove(position);
                        shopAdapter.notifyData(shopList);
                    }
                    Toast.makeText(MainActivityVideo.this, jObj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(MainActivityVideo.this, "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(MainActivityVideo.this,
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("name", shopList.get(position));
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    @Override
    protected void onStart() {
        super.onStart();
        getAllStaff();
    }


    @Override
    public void onDeleteClick(int position) {
        deleteFile(position);
    }

    public void imagePicker() {
        final CharSequence[] items;
        if (Appconfig.isDeviceSupportCamera(MainActivityVideo.this)) {
            items = new CharSequence[2];
            items[0] = "Camera";
            items[1] = "Gallery";
        } else {
            items = new CharSequence[1];
            items[0] = "Gallery";
        }

        android.app.AlertDialog.Builder alertdialog = new android.app.AlertDialog.Builder(MainActivityVideo.this);
        alertdialog.setTitle("Add Image");
        alertdialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {
                 /*   MediaFactory.MediaBuilder mediaBuilder = new MediaFactory.MediaBuilder(MainActivityVideo.this)
                            .fromCamera()
                            .setPickCount(1)
                            .isSquareCrop(false)
                            .withAspectRatio(9, 16)
                            .doCropping();
                    mediaFactory = MediaFactory.create().start(mediaBuilder);*/
                    new VideoPicker.Builder(MainActivityVideo.this)
                            .mode(VideoPicker.Mode.CAMERA)
                            .directory(VideoPicker.Directory.DEFAULT)
                            .extension(VideoPicker.Extension.MP4)
                            .enableDebuggingMode(true)
                            .build();
                } else if (items[item].equals("Gallery")) {
                  /*  MediaFactory.MediaBuilder mediaBuilder = new MediaFactory.MediaBuilder(MainActivityVideo.this)
                            .fromGallery()
                            .setPickCount(1)
                            .isSquareCrop(false)
                            .withAspectRatio(9, 16)
                            .doCropping();
                    mediaFactory = MediaFactory.create().start(mediaBuilder);*/
                    new VideoPicker.Builder(MainActivityVideo.this)
                            .mode(VideoPicker.Mode.GALLERY)
                            .directory(VideoPicker.Directory.DEFAULT)
                            .extension(VideoPicker.Extension.MP4)
                            .enableDebuggingMode(true)
                            .build();
                }
            }
        });
        alertdialog.show();
    }

    private void performUpload() {
        if (all_path.size() > 0) {
            String path = all_path.get(0);
            all_path.remove(0);
            showDialog();
            new MainActivityVideo.UploadFileToServer().execute(path);
        } else {
            hideDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
                all_path=new ArrayList<>();
                ArrayList<String> mPaths = data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);
                all_path=mPaths;
                performUpload();

            }
        } catch (Exception e) {
            Log.e("xxxxxxxx", e.toString());
        }

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
            progressDialog.setMessage("Uploading..." + (progress[0]));
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
            HttpPost httppost = new HttpPost(Appconfig.VIDEO);
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
                entity.addPart("video", new FileBody(sourceFile));

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
                getAllStaff();
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
}
