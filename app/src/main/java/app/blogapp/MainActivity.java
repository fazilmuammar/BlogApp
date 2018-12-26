package app.blogapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.blogapp.Adapter.*;
import app.blogapp.config.server;


public class MainActivity extends AppCompatActivity {

    public static String content_id, user_id, username, email, password, picture, phone, created, updated;

    final Context context = this;


    public static String android_id = "";
    private int closeapp = 2;
    private String link = server.host + "list_article.php?title=&category_id=";

    private ListView list_article;
    private GridView grid_article;




   Home simpleAdapter;


    private ImageView image_list, image_grid, image_map;

    ArrayList<HashMap<String, String>> article = new ArrayList<HashMap<String, String>>();

    boolean grid_list   = false;
    boolean device      = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        content_id = ""; user_id = ""; username =""; email =""; password =""; picture =""; phone =""; created = ""; updated = "";
        image_list = (ImageView) findViewById(R.id.image_list);
        image_grid = (ImageView) findViewById(R.id.image_grid);
        image_map = (ImageView) findViewById(R.id.image_map);
        grid_article = (GridView) findViewById(R.id.grid_article);
        list_article    = (ListView) findViewById(R.id.list_article);



        image_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

        image_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list_article.setVisibility(View.GONE);
                grid_article.setVisibility(View.VISIBLE);
                grid_list = true;
                article.clear();
                grid_article.setAdapter(null);
                link = server.host+ "list_article.php?title=&category_id=";
                Log.d("URL_GRID", link);
                FastAndroidNetworking();
                Snack("Memuat ulang data...");
                image_grid.setImageResource(R.drawable.menu_grid2);
                image_list.setImageResource(R.drawable.menu_list);
            }
        });
        image_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_article.setVisibility(View.VISIBLE);
                grid_article.setVisibility(View.GONE);
                grid_list = false;
                article.clear();
                list_article.setAdapter(null);
                link = server.host + "list_article.php?title=&category_id=";
                Log.d("URL_LIST", link);
                FastAndroidNetworking();
                Snack("Memuat ulang data...");
                image_list.setImageResource(R.drawable.menu_list2);
                image_grid.setImageResource(R.drawable.menu_grid);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        getSession();
        Log.d("user_id", user_id);
        Log.d("username", username);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Create.class));
//                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

        link = server.host + "list_article.php?title=&category_id=";
        Log.d("Log link", link);
        FastAndroidNetworking();
        Snack("Memuat ulang data...");
    }


    public void Adapter() {

        Log.d("_ADAPTER", "OKE");

        if(grid_list == true){

            simpleAdapter = new Home (this, article, R.layout.grid_article,
                    new String[] { "content_id", "category_id", "category", "title", "image", "author", "created", "view", "love"},
                    new int[] {R.id.text_content_id, R.id.text_category_id, R.id.text_category,
                            R.id.text_title, R.id.img_image, R.id.text_author, R.id.text_created, R.id.text_views, R.id.text_loves});

            grid_article.setAdapter(simpleAdapter);
            grid_article.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    content_id = ((TextView) view.findViewById(R.id.text_content_id)).getText().toString();
                    startActivity(new Intent(MainActivity.this, ReadActivity.class));
                }
            });

        }else{

            simpleAdapter = new Home (this, article, R.layout.list_article,
                    new String[] { "content_id", "category_id", "category", "title", "image", "author", "created", "view", "love"},
                    new int[] {R.id.text_content_id, R.id.text_category_id, R.id.text_category,
                            R.id.text_title, R.id.img_image, R.id.text_author, R.id.text_created, R.id.text_views, R.id.text_loves});

            list_article.setAdapter(simpleAdapter);
            Utility.setListViewHeightBasedOnChildren(list_article);
            list_article.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    content_id = ((TextView) view.findViewById(R.id.text_content_id)).getText().toString();
                    startActivity(new Intent(MainActivity.this, ReadActivity.class));
                }
            });
        }

        device = true;

        Log.e("Loh link ", link);
        FastAndroidNetworking();
    }



    private void ListMenu(){

        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.list_menu, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        final TextView text_login = (TextView) view
                .findViewById(R.id.text_login);
        final TextView text_profile = (TextView) view
                .findViewById(R.id.text_profile);
        final TextView text_about = (TextView) view
                .findViewById(R.id.text_about);

        if (user_id.equals("")){
            text_profile.setVisibility(View.GONE);

            text_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, Login.class));
                    alertDialog.dismiss();
                }
            });
        } else {
            text_login.setVisibility(View.GONE);
            text_profile.setText("Hi, " + username);

            text_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    alertDialog.dismiss();
                }
            });
        }

        text_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, About.class));
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

    @Override
    public void onResume(){
        super.onResume();
    Adapter();
    }



    private void FastAndroidNetworking(){

        Log.d("URL_ARTICLE", link);
        AndroidNetworking.post(link)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (device == false){
                            try {
                                JSONArray jsonArray = response.optJSONArray("result");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject responses    = jsonArray.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("content_id",     responses.optString("content_id"));
                                    map.put("category_id",     responses.optString("category_id"));
                                    map.put("category",     responses.optString("category") + ", ");
                                    map.put("title",    responses.optString("title"));
                                    map.put("image",    responses.optString("image"));
                                    map.put("author",    responses.optString("author"));
                                    map.put("created",    responses.optString("created"));
                                    map.put("view",    responses.optString("view") + " views ");
                                    map.put("love",    responses.optString("love") + " loves");
                                    article.add(map);
                                }

                                Adapter();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {

                                JSONArray jsonArray = response.optJSONArray("result");

                                int res = 0;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject responses    = jsonArray.getJSONObject(i);
                                    if (android_id.toString().equals(responses.optString("android_id"))){
                                        res += 1;
                                    }
                                }



                                device = false;
                            } catch (JSONException e) {
                                e.printStackTrace();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Main/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            startActivity(new Intent(MainActivity.this, Search.class));
            return true;
        } else if (id == R.id.action_menu) {
            ListMenu();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void getSession(){
        // 1. panggil nama session
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_APPEND);
        // 2. panggil nama variabel untuk mengambil nilainya
        user_id = sharedPreferences.getString("user_id", "");
        username = sharedPreferences.getString("username","");
        email = sharedPreferences.getString("email","");
        phone = sharedPreferences.getString("phone","");
        password = sharedPreferences.getString("password","");
        picture = sharedPreferences.getString("picture","");
        created = sharedPreferences.getString("created","");
        updated = sharedPreferences.getString("update","");

    }

    private void Snack(String string){
        Snackbar snackbar = Snackbar.make(list_article, string, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        closeapp -= 1;
        switch (closeapp) {
            case 1 : Toast.makeText(getApplication(), "Sekali lagi untuk menutup aplikasi",
                    Toast.LENGTH_LONG).show(); break;
            case 0 : finish(); break;
        }
    }
}
