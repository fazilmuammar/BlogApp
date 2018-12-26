package app.blogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import app.blogapp.config.server;
import app.blogapp.maps.SelectMaps;

public class Create extends AppCompatActivity {

    MainActivity M = new MainActivity();

    EditText nama,judul,content;
    Spinner sp,sc;
    ImageView img;
    TextView tx1,tx3;
    FloatingActionButton fab;

    ImageView gambar;
    int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;

    String cat_id;

    public static EditText edit_lokasi;
    public static String LATITUDE, LONGITUDE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creative);
        LATITUDE = ""; LONGITUDE = "";

        cat_id = "";

        // buatkan Log
//        Log.d("nama", "Budi"); // 1
//
//        String nm = "Budi";
//        Log.d("nama", nm); // 2
//
//        Log.d("nama", getString(R.string.nama)); // 3

        nama = (EditText) findViewById(R.id.edit_name);
        judul = (EditText) findViewById(R.id.edit_title);
        content = (EditText) findViewById(R.id.edit_content);
        sc =(Spinner) findViewById(R.id.spin_category);
        sp = (Spinner) findViewById(R.id.spin_category_id);
        img = (ImageView) findViewById(R.id.image);
        tx1 = (TextView) findViewById(R.id.input1);
        tx3 = (TextView) findViewById(R.id.input3);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        edit_lokasi = (EditText) findViewById(R.id.edit_lokasi);
        edit_lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( Create.this, SelectMaps.class));
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Create();
                Log.d("fab", "testing");
            }
        });

        FAN();
    }

    public void FAN(){
        AndroidNetworking.post("http://192.168.100.5/BlogApp/list_category.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response

                       try {

                           ArrayList<String> category_id = new ArrayList<String>();
                           ArrayList<String> name = new ArrayList<String>();
                           category_id.add("");
                           name.add("Pilih Kategori");

                           JSONArray jsonArray = response.optJSONArray("result");
                           for (int i =0; i < jsonArray.length(); i++){
                               JSONObject jsonObject = jsonArray.getJSONObject(i);

                               category_id.add(jsonObject.optString("category_id"));
                               name.add(jsonObject.optString("name"));
                           }


                           // untuk memasukkan data arraylist ke dalam spinner menggunakan list_spinner.xml
                           ArrayAdapter arrayAdapter;
                           arrayAdapter = new ArrayAdapter<String>(Create.this, R.layout.list_spinner, category_id);
                           sp.setAdapter(arrayAdapter);
                           arrayAdapter = new ArrayAdapter<String>(Create.this, R.layout.list_spinner, name);
                           sc.setAdapter(arrayAdapter);

                           // mengambil id kategori ketika mengklik spinner
                           sc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                               @Override
                               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                   String idd = sp.getItemAtPosition(position).toString();
                                   cat_id = sp.getItemAtPosition(position).toString();
                                   Log.d("category_id", idd);
                               }

                               @Override
                               public void onNothingSelected(AdapterView<?> parent) {

                               }
                           });



                       } catch (JSONException e){

                       }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    // membuka gallery
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //convert image bitmap to string
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 15, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void Create(){

        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Updating...", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.host + "create_content.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("response").equals("success")){
                                Toast.makeText(getApplicationContext(), "Artikel berhasil disimpan",
                                        Toast.LENGTH_LONG).show();

                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject.optString("response"),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }},

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                    }})
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("category_id", cat_id);
                params.put("user_id", M.user_id);
                params.put("author", nama.getText().toString());
                params.put("title", judul.getText().toString());
                params.put("content", content.getText().toString());
                params.put("username", M.username);
                params.put("latitude", LATITUDE);
                params.put("longitude", LONGITUDE);
                params.put("image", image);

                //returning parameters
                return params;
            }
        };

        //handle twice request / twice image post
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

}



