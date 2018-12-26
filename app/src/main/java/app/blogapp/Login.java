package app.blogapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import app.blogapp.R;

import app.blogapp.config.server;

public class Login extends AppCompatActivity {


    MainActivity M = new MainActivity();
    String URL , jsonResult;

    EditText edit_email, edit_password;
    Button btn_login;
    TextView text_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        URL = ""; jsonResult = "";

        edit_email          = (EditText) findViewById(R.id.edit_email);
        edit_password       = (EditText) findViewById(R.id.edit_password);
        btn_login           = (Button) findViewById(R.id.btn_login);
        text_register       = (TextView) findViewById(R.id.text_register);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_email.getText().toString().equals("") ||
                        edit_password.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Isi data dengan lengkap",
                            Toast.LENGTH_SHORT).show();
                } else {
                    FastAndroidNetworking();
                }
            }
        });

        text_register = (TextView) findViewById(R.id.text_register);
//        text_register.setPaintFlags(text_register.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        text_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void FastAndroidNetworking(){

        Log.d("Log link",
                server.user + "login.php?email=" + edit_email.getText().toString() +
                        "&password=" + edit_password.getText().toString()
        );

        AndroidNetworking.post(server.host + "login.php")
                .addBodyParameter("email", edit_email.getText().toString())
                .addBodyParameter("password", edit_password.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.optString("response").equals("success")){
                            Toast.makeText(getApplicationContext(), "Data berhasil disimpan",
                                    Toast.LENGTH_SHORT).show();

                            M.user_id =  response.optString("user_id").toString();
                            M.password =  edit_password.getText().toString();
                            saveSession(
                                    response.optString("user_id").toString(),
                                    response.optString("username").toString(),
                                    response.optString("email").toString(),
                                    edit_password.getText().toString(),
                                    response.optString("phone").toString(),
                                    response.optString("picture").toString(),
                                    response.optString("created").toString(),
                                    response.optString("updated").toString()
                            );
                            Toast.makeText(getApplication(), "Login berhasil", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Login.this, ProfileActivity.class));
                            finish();

                        } else  if (response.optString("response").equals("failed")){
                            Snack("Login gagal"); Log.d("Log result", response.optString("response").toString());
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

    //     Create session
    private void saveSession(String user_id, String username, String email, String password, String phone,
                             String picture, String created, String updated){

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id", user_id);   M.user_id = user_id;
        editor.putString("username", username); M.username = username;
        editor.putString("email", email);       M.email = email;
        editor.putString("password", password); M.password = password;
        editor.putString("phone", phone);       M.phone = phone;
        editor.putString("picture", picture);   M.picture = picture;
        editor.putString("created", created);   M.created = created;
        editor.putString("updated", updated);   M.updated = updated;
        editor.apply();
    }

    private void Snack(String content){
        Snackbar snackbar = Snackbar.make(edit_email, content, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }
}

