package app.blogapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import app.blogapp.config.server;

public class ReadActivity extends AppCompatActivity implements OnMapReadyCallback {

    ArrayList<HashMap<String, String>> blogs = new ArrayList<>();
    MainActivity M = new MainActivity();
    ListView lv;

    String link = server.host + "read_article.php?content_id=" + M.content_id;
    TextView username, content, category, articles, views, created, update, ctn, love;
    FloatingActionButton fab;
    View view;

    ImageView gambar;
    int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    Dialog myDialog;
    String click = "";

    GoogleMap googleMap;
    SupportMapFragment mapFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = (TextView) findViewById(R.id.text_username);
        category = (TextView) findViewById(R.id.text_categor);
        content = (TextView) findViewById(R.id.text_content_id);
        articles = (TextView) findViewById(R.id.text_title);
        views = (TextView) findViewById(R.id.text_views);
        created = (TextView) findViewById(R.id.text_created);
        update = (TextView) findViewById(R.id.update);
        gambar = (ImageView) findViewById(R.id.img_image);
        ctn = (TextView) findViewById(R.id.content);
        love = (TextView) findViewById(R.id.text_loves);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if (M.user_id.toString().equals("")) {
                    startActivity(new Intent(ReadActivity.this, Login.class));
                } else {
                    fab.hide();
                    click = "love";
                    link = server.host + "update_love.php?content_id=" + M.content_id + "&user_id=" + M.user_id;
                   MyBlog();
                }
            }
        });

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_comment) {
            startActivity(new Intent(ReadActivity.this, CommentActivity.class));
            return true;
        } else if (id == R.id.action_share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Share your awasome story!");
            i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=app.tentangkamu");
            startActivity(Intent.createChooser(i, "Share URL"));
        }

        return super.onOptionsItemSelected(item);
    }


    private void MyBlog() {
        // bikin Log isinya link
        Log.d("link_article", server.host + "read_article.php?content_id=" + M.content_id);

        AndroidNetworking.post(link)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (click.toString().equals("love")) {

                            if (response.optString("response").equals("success")){

                                click = "";
                                Snackbar snackbar = Snackbar.make(content, "Artikel berhasil disukai", Snackbar.LENGTH_LONG);
                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                snackbar.show();

                            } else  if (response.optString("response").equals("failed")){
                                Toast.makeText(getApplicationContext(), response.optString("response").toString(),
                                        Toast.LENGTH_LONG).show();
                                Log.d("Log result", response.optString("response").toString());
                            } else {
                                Log.d("Error ", response.optString("response").toString());
                            }

                        } else {

                            // do anything with response
                            try {
                                JSONArray jsonArray = response.getJSONArray("result1"); // array
                                for (int i = 0; i < jsonArray.length(); i++) { //Menggunakan for / looping untuk mengambil semua datanya
                                    JSONObject jsonObject = jsonArray.getJSONObject(i); // object
                                    Log.d("author", jsonObject.optString("author"));
                                    username.setText(jsonObject.optString("author"));
                                    articles.setText(jsonObject.optString("title"));

                                    Log.d("read_image", server.img + jsonObject.optString("image"));
                                    Picasso.with(ReadActivity.this)
                                            .load(server.img + jsonObject.optString("image"))
                                            //.load(server.pict + "fazil.jpg" ) // mengambil dari URL
                                            // jika URL error
                                            .into(gambar);
                                    created.setText(jsonObject.optString("created"));
                                    update.setText(jsonObject.optString("updated"));
                                    content.setText(jsonObject.optString("content"));
                                    views.setText(jsonObject.optString("view") + " dilihat ");
                                    Log.d(
                                            "love", // menamai log yang akan dicari
                                            jsonObject.optString("love") // love adalah jsonbject dari jsonArray RESULT1
                                    );
                                    love.setText(jsonObject.optString("love") + " suka");
                                    category.setText(jsonObject.optString("category") + " | ");
                                    ctn.setText(jsonObject.optString("content"));

                                    setMap(
                                            jsonObject.optDouble("latitude"), jsonObject.optDouble("longitude"),
                                            jsonObject.optString("title")
                                    );

//                                Toast.makeText(ProfileActivity.this, jsonObject.optString("created"),
//                                        Toast.LENGTH_LONG).show();

                                }

//
                            } catch (JSONException e) {
                            }

                            try {
                                JSONArray jsonArray = response.optJSONArray("result2");
                                int res = 0;

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject responses = jsonArray.getJSONObject(0);

                                    if (M.user_id.toString().equals(responses.optString("user_id").toString())) {
                                        res += 1;
                                    }
                                    Log.e("user_id ", responses.optString("user_id"));
                                }
                                if (res == 0) {
                                    fab.show();
                                }

                            } catch (JSONException e){

                            }
                        }
                    }


                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });


    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        if (googleMap != null) {
            MyBlog();
        }
    }

    private void setMap(double lat, double lot, String title){
        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(lat, lot))
                .zoom(16)
                .bearing(0)
                .tilt(45)
                .build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lot))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_launcher_round)));
    }
}






