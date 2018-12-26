package app.blogapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
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

public class CommentActivity extends AppCompatActivity {

    MainActivity H = new MainActivity();

    private String link = server.user + "list_comment.php?content_id=" + H.content_id;

    private ListView list_comment;
    private TextView text_comment;

    ListAdapter simpleAdapter;
    ArrayList<HashMap<String, String>> comment = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list_comment = (ListView) findViewById(R.id.list_comment);
        text_comment = (TextView) findViewById(R.id.text_comment);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!H.user_id.equals("")) {
                    startActivity(new Intent(CommentActivity.this, AddActivity.class));
                } else {
                    startActivity(new Intent(CommentActivity.this, Login.class));
                }
            }
        });
    }

        @Override
        public void onResume() {
            super.onResume();
            comment.clear(); list_comment.setAdapter(null);
            FastAndroidNetworking();
        }

    private void FastAndroidNetworking(){
        AndroidNetworking.post(server.host +"list_comment.php?content_id=" + H.content_id)
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
                                map.put("comment_id",   responses.optString("comment_id"));
                                map.put("content_id",   responses.optString("content_id"));
                                map.put("user_id",      responses.optString("user_id"));
                                map.put("username",     responses.optString("username"));
                                map.put("content",      responses.optString("content"));
                                map.put("datetime",     responses.optString("datetime"));
                                comment.add(map);

                                text_comment.setVisibility(View.GONE);
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
        simpleAdapter = new SimpleAdapter(this, comment, R.layout.list_comment,
                new String[] { "comment_id", "content_id", "user_id", "username", "content", "datetime"},
                new int[] {R.id.text_comment_id, R.id.text_content_id, R.id.text_user_id, R.id.text_username,
                        R.id.text_content, R.id.text_datetime});

        list_comment.setAdapter(simpleAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Main/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            comment.clear(); list_comment.setAdapter(null);
            FastAndroidNetworking();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
