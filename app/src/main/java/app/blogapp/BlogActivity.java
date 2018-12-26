package app.blogapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.blogapp.config.server;

public class BlogActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<HashMap<String, String>> blogs = new ArrayList<>();
    MainActivity M = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        lv= (ListView) findViewById(R.id.lv_blog);


        MyBlog();


    }



    private void MyBlog(){
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

                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("content_id", jsonObject.optString("content_id"));
                                map.put("category_id", jsonObject.optString("category_id"));
                                map.put("category", jsonObject.optString("category"));
                                map.put("author", jsonObject.optString("author"));
                                map.put("title", jsonObject.optString("title"));
                                map.put("created", jsonObject.optString("created"));
                                map.put("updated", jsonObject.optString("updated"));
                                map.put("status", jsonObject.optString("status"));
                                map.put("view", jsonObject.optString("view"));
                                map.put("love", jsonObject.optString("love"));
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

    private void Adapter(){
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, blogs, R.layout.list_blog,
                new String[] {"content_id", "category_id", "category", "title", "created",
                        "updated", "status", "view", "love"},
                new int[] {R.id.text_content_id, R.id.text_category_id, R.id.text_category, R.id.text_title,
                        R.id.text_created, R.id.text_updated, R.id.text_status, R.id.text_views, R.id.text_loves} );
        lv.setAdapter(simpleAdapter);

    }
    }
