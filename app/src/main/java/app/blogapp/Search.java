package app.blogapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

import app.blogapp.Adapter.Home;
import app.blogapp.config.server;


public class Search extends AppCompatActivity {

    MainActivity M = new MainActivity();

    Toolbar toolbar;
    ListView list_search;
    EditText edit_search;
    TextView text_category;
    Button btn_more;

    app.blogapp.Adapter.Search searchAdapter;
    Home homeAdapter;

    ArrayList<HashMap<String, String>> article = new ArrayList<HashMap<String, String>>();

    String link = "";
    String category_id  = "";
    String title        = "PILIH KATEGORI";
    int total_id        = 0;
    int page            = 0;

    boolean search  = false;
    boolean more    = false;
    boolean klik    = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        text_category   = (TextView) findViewById(R.id.text_category);
        btn_more        = (Button) findViewById(R.id.btn_more);
        list_search     = (ListView) findViewById(R.id.list_search);

        ListCategory();

        edit_search = (EditText) findViewById(R.id.edit_search);
        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    page = 0; klik = false;
                    category_id = "";
                    text_category.setText("SEMUA KATEGORI");
                    SearchArticles();
                    return true;
                }
                return false;
            }
        });

        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page += 3;
                MoreArticles();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (M.user_id.equals("")){
                    startActivity(new Intent(Search.this, Login.class));
                } else {
                    startActivity(new Intent(Search.this, Create.class));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (search == true){
            ListCategory();
        } else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    private void ListCategory(){
        text_category.setText("PILIH KATEGORI");
        search = false; more = false;
        article.clear(); list_search.setAdapter(null);
        link = server.host + "list_category.php";
        FastAndroidNetworking();
        Log.d("Log link", link);
    }

    private void SearchArticles(){
        search = true;
        article.clear(); list_search.setAdapter(null);
        link = server.host + "list_search.php?title=" + edit_search.getText().toString() + "&category_id="
                + category_id + "&page=" + String.valueOf(page);
        Log.d("Log link", link);
        FastAndroidNetworking();
        Snack("Memuat data...");
    }

    private void MoreArticles(){
        search = true; more = true;
        link = server.host + "list_search.php?title=" + edit_search.getText().toString() + "&category_id="
                + category_id + "&page=" + String.valueOf(page);
        Log.d("Log link", link);
        FastAndroidNetworking();
    }

    private void FastAndroidNetworking(){
        AndroidNetworking.post(link)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (search == true){

                            String content_count = response.optString("content_count");
                            total_id = Integer.parseInt(content_count.toString().trim());

                            try {
                                JSONArray jsonArray = response.optJSONArray("result");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject responses    = jsonArray.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("content_id",   responses.optString("content_id"));
                                    map.put("category_id",  responses.optString("category_id"));
                                    map.put("category",     responses.optString("category") + ", ");
                                    map.put("title",        responses.optString("title"));
                                    map.put("image",        responses.optString("image"));
                                    map.put("author",       responses.optString("author"));
                                    map.put("created",      responses.optString("created"));
                                    map.put("view",         responses.optString("view") + " views ");
                                    map.put("love",         responses.optString("love") + " loves");

                                    article.add(map);
                                }

                                SearchAdapter();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            try {
                                JSONArray jsonArray = response.optJSONArray("result");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject responses    = jsonArray.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("category_id",  responses.optString("category_id"));
                                    map.put("content_id",   responses.optString("content_id"));
                                    map.put("image",        responses.optString("image"));
                                    map.put("name",        responses.optString("name"));

                                    article.add(map);
                                }

                                CategoryAdapter();
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

    private void SearchAdapter(){
        homeAdapter = new Home(this, article, R.layout.list_article,
                new String[] { "content_id", "category_id", "category", "title", "image", "author", "created", "view", "love"},
                new int[] {R.id.text_content_id, R.id.text_category_id, R.id.text_category,
                        R.id.text_title, R.id.img_image, R.id.text_author, R.id.text_created, R.id.text_views, R.id.text_loves});

        list_search.setAdapter(homeAdapter);

        if (more == true){
            list_search.setSelection(homeAdapter.getCount() - 1);
        }

        list_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                M.content_id = ((TextView) view.findViewById(R.id.text_content_id)).getText().toString();
                startActivity(new Intent(Search.this, ReadActivity.class));
            }
        });

        list_search.setOnScrollListener(new ListView.OnScrollListener(){
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int position = firstVisibleItem+visibleItemCount;
                int limit = totalItemCount;

                // Check if bottom has been reached
                if (position >= limit && totalItemCount > 0) {
                    //scroll end reached, write your code here
                    Log.d("Log more", "More...");
                    btn_more.setVisibility(View.VISIBLE);
                } else {
                    btn_more.setVisibility(View.GONE);
                }

                if (page >= total_id){
                    btn_more.setVisibility(View.GONE);
                }

                if (klik == true){
                    btn_more.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) { }
        });
    }

    private void CategoryAdapter(){
        searchAdapter = new app.blogapp.Adapter.Search(this, article, R.layout.list_category,
                new String[] { "image", "content_id", "category_id", "name"},
                new int[] {R.id.img_image, R.id.content_id, R.id.category_id, R.id.category});
        list_search.setAdapter(searchAdapter);
        list_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                category_id = ((TextView) view.findViewById(R.id.category_id)).getText().toString();
                String content = ((TextView) view.findViewById(R.id.content_id)).getText().toString();

                Log.d("Log content", title);
                if (content.toString().trim().equals("0 ARTICLES")){
                    Toast.makeText(getApplicationContext(), "Belum ada artikel untuk kategori ini",
                            Toast.LENGTH_LONG).show();
                } else {
                    text_category.setText(
                            ((TextView) view.findViewById(R.id.category)).getText().toString().toUpperCase()
                    );
                    SearchArticles();
                    klik = true;
                }
            }
        });
    }

    private void Snack(String content){
        Snackbar snackbar = Snackbar.make(edit_search, content, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }

}
