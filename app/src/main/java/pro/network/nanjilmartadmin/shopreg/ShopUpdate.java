package pro.network.nanjilmartadmin.shopreg;

import static pro.network.nanjilmartadmin.app.Appconfig.DELETE_SHOP;
import static pro.network.nanjilmartadmin.app.Appconfig.UPDATE_SHOP;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pro.network.nanjilmartadmin.R;
import pro.network.nanjilmartadmin.app.AndroidMultiPartEntity;
import pro.network.nanjilmartadmin.app.AppController;
import pro.network.nanjilmartadmin.app.Appconfig;
import pro.network.nanjilmartadmin.app.GlideApp;
import pro.network.nanjilmartadmin.app.Imageutils;
import pro.network.nanjilmartadmin.product.ImageClick;

/**
 * Created by user_1 on 11-07-2018.
 */

public class ShopUpdate extends AppCompatActivity implements Imageutils.ImageAttachmentListener {

    private final String[] STOCKUPDATE = new String[]{
            "In Stock", "Currently Unavailable",
    };
    public Button addSize;
    EditText shop_name, phone, latlong, address, category, offerAmt, offerVal,estimateTime,rating;
    MaterialBetterSpinner stock_update;
    String studentId = null;
    TextView submit;
    Imageutils imageutils;
    CardView itemsAdd;
    ArrayList<Time> times = new ArrayList<>();
    TimeAdapter timeAdapter;
    CheckBox isEnable,freeDelivery;
    private ProgressDialog pDialog;
    private ImageView profiletImage;
    private String imageUrl = "";
    private RecyclerView sizelist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_reg);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        imageutils = new Imageutils(this);
        profiletImage = findViewById(R.id.profiletImage);
        isEnable = findViewById(R.id.isEnable);
        estimateTime = findViewById(R.id.estimateTime);
        rating = findViewById(R.id.rating);
        freeDelivery = findViewById(R.id.freeDelivery);
        profiletImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageutils.imagepicker(1);
            }
        });
        itemsAdd = findViewById(R.id.itemsAdd);

        shop_name = findViewById(R.id.shop_name);
        offerAmt = findViewById(R.id.offerAmt);
        offerVal = findViewById(R.id.offerVal);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        category = findViewById(R.id.category);
        stock_update = findViewById(R.id.stock_update);
        latlong = findViewById(R.id.latlong);
        ArrayAdapter<String> stockAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, STOCKUPDATE);
        stock_update.setAdapter(stockAdapter);
        stock_update.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        addSize = findViewById(R.id.addSize);
        addSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDaysBottom(-1);
            }
        });
        times = new ArrayList<>();
        timeAdapter = new TimeAdapter(this, times, new ImageClick() {
            @Override
            public void onImageClick(int position) {
                showDaysBottom(position);
            }

            @Override
            public void onDeleteClick(int position) {
                times.remove(position);
                timeAdapter.notifyData(times);
            }

            @Override
            public void onEditClick(int position) {

            }
        }, true);
        sizelist = findViewById(R.id.sizelist);
        final LinearLayoutManager sizeManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        sizelist.setLayoutManager(sizeManager);
        sizelist.setAdapter(timeAdapter);

        submit = findViewById(R.id.submit);
        submit.setText("UPDATE");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shop_name.getText().toString().length() <= 0) {
                    shop_name.setError("Enter the correct Shop Name");
                } else if (phone.getText().toString().length() <= 0) {
                    phone.setError("Enter the correct Phone");
                } else if (address.getText().toString().length() <= 0) {
                    address.setError("Enter the correct  Address");
                } else if (latlong.getText().toString().length() <= 0) {
                    latlong.setError("Enter the correct Location");
                } else if (category.getText().toString().length() <= 0) {
                    category.setError("Enter the Category");
                } else if (stock_update.getText().toString().length() <= 0) {
                    stock_update.setError("Select the Sold or Not");
                } else if (offerVal.getText().toString().length() <= 0) {
                    offerVal.setError("Enter valid offer value");
                }  else if (estimateTime.getText().toString().length() <= 0) {
                    estimateTime.setError("Enter valid Estimate Time");
                }  else if (rating.getText().toString().length() <= 0) {
                    rating.setError("Enter valid Rating");
                }else {
                    registerUser();
                }
            }
        });


        try {

            Shop contact = (Shop) getIntent().getSerializableExtra("data");
            shop_name.setText(contact.shop_name);
            imageUrl = contact.image;
            GlideApp.with(ShopUpdate.this).load(contact.image)
                    .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                    .into(profiletImage);
            phone.setText(contact.phone);
            latlong.setText(contact.latlong);
            category.setText(contact.category);
            address.setText(contact.address);
            estimateTime.setText(contact.estimateTime);
            rating.setText(contact.rating);

            offerAmt.setText(contact.offerAmt.contains("-") ? contact.offerAmt.split("-")[0] : contact.offerAmt);

            if(contact.offerAmt.contains("-")){
                offerVal.setText(contact.offerAmt.split("-")[1]);
            }else{
                offerVal.setText("");
            }
            isEnable.setChecked(contact.shop_enabled.equalsIgnoreCase("1"));
            freeDelivery.setChecked(contact.freeDelivery.equalsIgnoreCase("1"));
            studentId = contact.id;
            stock_update.setText(contact.stock_update);
            if (contact.getTime_schedule() == null || contact.getTime_schedule().equalsIgnoreCase("null")) {
                times = new ArrayList<>();
            } else {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Object listBeans = new Gson().fromJson(contact.getTime_schedule(),
                            Object.class);
                    times = mapper.convertValue(
                            listBeans,
                            new TypeReference<ArrayList<Time>>() {
                            }
                    );
                } catch (Exception e) {
                    Log.e("xxxxxxxxxx", e.toString());
                }
            }
            if (times == null) {
                times = new ArrayList<>();
            }
            timeAdapter.notifyData(times);
        } catch (Exception e) {
            Log.e("xxxxxxxxxxx", e.toString());

        }
    }

    private void showDaysBottom(int position) {
        final RoundedBottomSheetDialog mBottomSheetDialog = new RoundedBottomSheetDialog(ShopUpdate.this);
        LayoutInflater inflater = ShopUpdate.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.bottom_size_layout, null);
        final String[] TIMEING = new String[]{
                "Sunday", "Monday", "Tuesday", "wednesday", "Thursday", "Friday", "Saturday",
        };

        final MaterialBetterSpinner daySpinner = dialogView.findViewById(R.id.shop_time);
        final RadioButton openRadio = dialogView.findViewById(R.id.in_time);
        final RadioButton closeRadio = dialogView.findViewById(R.id.out_time);
        final EditText openHoursEdit = dialogView.findViewById(R.id.select_inTime);
        final EditText closeHoursEdit = dialogView.findViewById(R.id.select_outTime);
        if (position >= 0) {
            Time timeTemp = times.get(position);
            daySpinner.setText(timeTemp.getDayInWeek());
            openHoursEdit.setText(timeTemp.getOpenHours());
            closeHoursEdit.setText(timeTemp.getCloseHours());
            openRadio.setChecked(timeTemp.isOpen());
            closeRadio.setChecked(!timeTemp.isOpen());
        }

        ArrayAdapter<String> stockAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, TIMEING);
        daySpinner.setAdapter(stockAdapter);
        daySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        openRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    openRadio.setChecked(true);
                    closeRadio.setChecked(false);

                } else {
                    openRadio.setChecked(false);
                }
            }
        });

        closeRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    openRadio.setChecked(false);
                    closeRadio.setChecked(true);
                } else {
                    closeRadio.setChecked(false);
                }
            }
        });


        final Button submit = dialogView.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (daySpinner.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Valid ShopTime", Toast.LENGTH_LONG).show();
                    return;
                } else if (closeHoursEdit.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Valid OutTime", Toast.LENGTH_LONG).show();
                    return;
                }
                if (position >= 0) {
                    times.get(position).setOpen(openRadio.isChecked());
                    times.get(position).setDayInWeek(daySpinner.getText().toString());
                    times.get(position).setOpenHours(openHoursEdit.getText().toString());
                    times.get(position).setCloseHours(closeHoursEdit.getText().toString());
                } else {
                    times.add(new Time(
                            openRadio.isChecked(),
                            daySpinner.getText().toString(),
                            openHoursEdit.getText().toString(),
                            closeHoursEdit.getText().toString()));
                }
                timeAdapter.notifyData(times);
                mBottomSheetDialog.cancel();
            }
        });
        daySpinner.requestFocus();
        openRadio.requestFocus();
        closeRadio.requestFocus();
        openHoursEdit.requestFocus();
        closeHoursEdit.requestFocus();

        mBottomSheetDialog.setContentView(dialogView);
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

    private void registerUser() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Updating ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                UPDATE_SHOP, new Response.Listener<String>() {
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
                localHashMap.put("address", address.getText().toString());
                localHashMap.put("image", imageUrl);
                localHashMap.put("phone", phone.getText().toString());
                localHashMap.put("category", category.getText().toString());
                localHashMap.put("latlong", latlong.getText().toString());
                localHashMap.put("stock_update", stock_update.getText().toString());
                localHashMap.put("time_schedule", new Gson().toJson(times));
                localHashMap.put("offerAmt", offerAmt.getText().length() > 0 ? offerAmt.getText().toString() + "-" + offerVal.getText().toString() : "0-0");
                localHashMap.put("shop_enabled", isEnable.isChecked() ? "1" : "0");
                localHashMap.put("freeDelivery", freeDelivery.isChecked() ? "1" : "0");
                localHashMap.put("estimateTime", estimateTime.getText().toString());
                localHashMap.put("rating", rating.getText().toString());
                localHashMap.put("id", studentId);
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq);
    }

    private void deleteUser() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Processing ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                DELETE_SHOP, new Response.Listener<String>() {
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
    public void onPointerCaptureChanged(boolean hasCapture) {

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
        new ShopUpdate.UploadFileToServer().execute(Appconfig.compressImage(storedPath, ShopUpdate.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageutils.onActivityResult(requestCode, resultCode, data);

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
}



