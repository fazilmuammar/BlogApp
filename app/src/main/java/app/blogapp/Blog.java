package app.blogapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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


public class Blog extends AppCompatActivity {

    MainActivity H = new MainActivity();

    private EditText edit_search;
    private ListView list_blog;
    private SwipeRefreshLayout swipe_refresh;

    private String link = server.host+ "list_myblog.php?user_id=" + H.user_id;
    SimpleAdapter simpleAdapter;
    ArrayList<HashMap<String, String>> blog = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog2);

        edit_search     = (EditText) findViewById(R.id.edit_search);
        list_blog       = (ListView) findViewById(R.id.list_blog);
        swipe_refresh   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FastAndroidNetworking();
            }
        });

        Snack("Memuat data...");
        FastAndroidNetworking();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Blog.this, Create.class));
            }
        });
    }

    private void FastAndroidNetworking(){

        Log.d("Log link", link);
        blog.clear(); list_blog.setAdapter(null);

        AndroidNetworking.post(link)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.optJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject responses    = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("content_id",   responses.optString("content_id"));
                                map.put("category_id",  responses.optString("category_id"));
                                map.put("category",     responses.optString("category") + " ");
                                map.put("title",        responses.optString("title"));
                                map.put("created",      responses.optString("created"));
                                map.put("updated",      "Diperbarui, " + responses.optString("updated") );
                                map.put("status",       responses.optString("status"));
                                map.put("view",         responses.optString("view") + " views | ");
                                map.put("love",         responses.optString("love") + " loves");

                                blog.add(map);
                            }

                            Adapter();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void Adapter(){

        swipe_refresh.setRefreshing(false);

        simpleAdapter = new SimpleAdapter(this, blog, R.layout.list_blog,
                new String[] { "content_id", "category_id", "category", "title",
                        "created", "updated", "status", "view", "love"},
                new int[] {R.id.text_content_id, R.id.text_category_id, R.id.text_category, R.id.text_title,
                        R.id.text_created, R.id.text_updated, R.id.text_status, R.id.text_views, R.id.text_loves});

        list_blog.setAdapter(simpleAdapter);

        list_blog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                H.content_id = ((TextView) view.findViewById(R.id.text_content_id)).getText().toString();
            }
        });

        edit_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                // When user changed the Text
                ((SimpleAdapter) Blog.this.simpleAdapter).getFilter().filter(edit_search.getText());
            }
        });
    }

    private void Snack(String string){
        Snackbar snackbar = Snackbar.make(list_blog, string, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }
}
