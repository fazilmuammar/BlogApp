package app.blogapp;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import app.blogapp.config.server;

import static app.blogapp.R.id.edit_content;

public class AddActivity extends AppCompatActivity {

    EditText cmt;
    MainActivity M = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        cmt = (EditText) findViewById(R.id.edit_content);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Comment");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send) {
            if (cmt.getText().length() >= 3) {
                FastAndroidNetworking();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void FastAndroidNetworking(){
        AndroidNetworking.post(server.host+ "create_comment.php")
                .addBodyParameter("user_id", M.user_id)
                .addBodyParameter("username", M.username)
                .addBodyParameter("content_id", M.content_id)
                .addBodyParameter("content", cmt.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.optString("response").equals("success")){
                            Toast.makeText(getApplication(), "Komentar berhasil dikirim",
                                    Toast.LENGTH_SHORT).show();
                            finish();

                        } else  if (response.optString("response").equals("failed")){
                            Toast.makeText(getApplication(), response.optString("response").toString(),
                                    Toast.LENGTH_SHORT).show();
                            Log.d("Log response", response.optString("response").toString());
                        } else {
                            Log.d("Error ", response.optString("response").toString());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }
}




