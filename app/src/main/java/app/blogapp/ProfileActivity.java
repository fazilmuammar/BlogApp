package app.blogapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import app.blogapp.config.server;
import de.hdodenhof.circleimageview.CircleImageView;

import static app.blogapp.R.id.img_picture;

public class ProfileActivity extends AppCompatActivity {

    MainActivity M = new MainActivity();

    TextView username,logout,articles,views,blog,more;
    Button update;
    View view;

    ImageView gambar;
    int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    Dialog myDialog;

    ListView lv_blog;
    ArrayList<HashMap<String, String>> blogs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = (TextView) findViewById(R.id.text_username);
        logout = (TextView) findViewById(R.id.text_logout);
        articles = (TextView) findViewById(R.id.text_articles);
        views = (TextView) findViewById(R.id.text_views);
        blog = (TextView) findViewById(R.id.text_blog);
        more = (TextView) findViewById(R.id.text_more);
        update = (Button) findViewById(R.id.btn_update);
        view = (View) findViewById(R.id.view);
        lv_blog =(ListView) findViewById(R.id.list_blog);
        gambar = (CircleImageView) findViewById(img_picture);
        logout = (TextView) findViewById(R.id.text_logout);



        Picasso.with(this)
                .load(server.pict + M.picture)
                .placeholder(R.drawable.no_profile)
                .error(R.drawable.no_profile)
                .into(gambar);
        Log.d("PICASSO_PROF_PICTURE", server.pict + M.picture);


        gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(ProfileActivity.this, Edit.class));
            }
        });

        //kasih nama buat username
        username.setText(M.username);





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "", Snackbar.LENGTH_LONG).show();
                startActivity(new Intent(ProfileActivity.this, Create.class));


            }
        });

       gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });


    }

    //jadi ketika mau lanjut lagi ke activity ini mau di apakan
    @Override
    public void onResume(){
        super.onResume();
        MyBlog();
    }

    private void MyBlog(){

        blogs.clear();  lv_blog.setAdapter(null);//

        AndroidNetworking.post(server.host + "read_profile.php?user_id=" + M.user_id )
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            JSONArray jsonArray = response.getJSONArray("result"); // array
                            for (int i =0; i < jsonArray.length(); i++){ //Menggunakan for / looping untuk mengambil semua datanya
                                JSONObject jsonObject = jsonArray.getJSONObject(i); // object
                                Log.d("category", jsonObject.optString("category"));
                                Log.d("title", jsonObject.optString("title"));

                                //menamppung seua response, kemudian di masukkan kedalam arraylist. dari database read.profle.php
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("content_id", jsonObject.optString("content_id"));
                                map.put("category_id", jsonObject.optString("category_id"));
                                map.put("category", jsonObject.optString("category"));
                                map.put("author", jsonObject.optString("author"));
                                map.put("title", jsonObject.optString("title"));
                                map.put("created", jsonObject.optString("created"));
                                map.put("updated", jsonObject.optString("updated"));
                                map.put("status", jsonObject.optString("status"));
                                map.put("view", jsonObject.optString("view") + "Views | ");
                                map.put("love", jsonObject.optString("love") + "Loves | ");
                                blogs.add(map);
//                                Toast.makeText(ProfileActivity.this, jsonObject.optString("created"),
//                                        Toast.LENGTH_LONG).show();

                            }

                            Adapter();
                        } catch (JSONException e){
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    // menampilkan data ArrayList ke ListView
    private void Adapter(){

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, blogs, R.layout.list_blog,//list_blog mencustome tampilan yang akan keluar
                new String[] {"content_id", "category_id", "category", "title", "created",
                        "updated", "status", "view", "love"},
                new int[] {R.id.text_content_id, R.id.text_category_id, R.id.text_category, R.id.text_title,
                        R.id.text_created, R.id.text_updated, R.id.text_status, R.id.text_views, R.id.text_loves} );
        lv_blog.setAdapter(simpleAdapter);//lv blog wadah untuk list_blog yang sudah di kustome
        Utility.setListViewHeightBasedOnChildren(lv_blog);

        lv_blog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                M.content_id = ((TextView) view.findViewById(R.id.text_content_id)).getText().toString();
                startActivity(new Intent(
                        ProfileActivity.this, ReadActivity.class
                ));
            }
        });

    }

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
                callNewDialog();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void callNewDialog() {

        myDialog = new Dialog(ProfileActivity.this);
        myDialog.setContentView(R.layout.update_picture);
        myDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        myDialog.setCancelable(true);

        ImageView img_profile_picture   = (ImageView) myDialog.findViewById(R.id.img_profile_picture);
        Button button_update_picture    = (Button) myDialog.findViewById(R.id.button_update_picture);
        myDialog.show();

        img_profile_picture.setImageBitmap(bitmap);
        button_update_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePicture();
                myDialog.cancel();
            }
        });
    }


    //convert image bitmap to string
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 15, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void updatePicture() {
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Updating...", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.host+ "update_picture.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("response").equals("success")){
                                Toast.makeText(getApplicationContext(), "Perubahan berhasil disimpan",
                                        Toast.LENGTH_LONG).show();
                                //memanggil session
                                sessPict(
                                        jsonObject.optString("picture"));
                                Log.d("PROFILE_PICTURE", jsonObject.optString("picture"));
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
                params.put("user_id", M.user_id);
                params.put("picture", image);

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

        //set ImageView
        gambar.setImageBitmap(bitmap);
    }

    //Create session
    private void sessPict(String picture){

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("picture", picture);
        M.picture = picture;
        editor.apply();
    }

    private void Logout(){
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id", "");
        editor.putString("username", "");
        editor.putString("email", "");
        editor.putString("password", "");
        editor.putString("picture", "");
        editor.putString("phone", "");
        editor.putString("created", "");
        editor.putString("updated", "");
        editor.apply();
        M.user_id = "";
        Toast.makeText(this, "Logout", Toast.LENGTH_LONG).show();
        startActivity(new Intent(ProfileActivity.this, Login.class));
        finish();

    }


}