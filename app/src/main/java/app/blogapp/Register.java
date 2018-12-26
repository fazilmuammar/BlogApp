package app.blogapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.androidnetworking.AndroidNetworking;
//import com.androidnetworking.common.Priority;
//import com.androidnetworking.error.ANError;
//import com.androidnetworking.interfaces.JSONObjectRequestListener;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import app.blogapp.config.server;

public class Register extends AppCompatActivity {

    public EditText username, email, nomorku, password, confirm;

    public Button daftar;
    public TextView masuk;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username    = (EditText) findViewById(R.id.user);
        email       = (EditText) findViewById(R.id.email);
        nomorku     = (EditText) findViewById(R.id.nomor);
        password    = (EditText) findViewById(R.id.passwordku);
        confirm     = (EditText)findViewById(R.id.confirmpwd);

        daftar = (Button) findViewById(R.id.btn_masuk);
        masuk = (TextView) findViewById(R.id.enter);


        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (username.getText().toString().equals("") || email.getText().toString().equals("")|| nomorku.getText().toString().equals("")
                        || password.getText().toString().equals("")|| confirm.getText().toString().equals("")) {
                    Toast.makeText(Register.this, "Data tidak boleh kosong", // context, content
                            Toast.LENGTH_LONG).show();

                }  else if (!password.getText().toString().equals( confirm.getText().toString() )){
                    Toast.makeText(Register.this, "Konfirmasi password dengan benar", // context, content
                            Toast.LENGTH_LONG).show();
                } else {
                    Register();
                }

            }
        });



        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void Register(){
        AndroidNetworking.post(server.host + "register.php" )
                .addBodyParameter("username",   username.getText().toString())
                .addBodyParameter("email",      email.getText().toString())
                .addBodyParameter("phone",      nomorku.getText().toString())
                .addBodyParameter("password",  password.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        if (response.optString("response").equals("success")){
                            Toast.makeText(Register.this, "Registrasi Berhasil", // context, content
                                    Toast.LENGTH_LONG).show();

                            finish();
                        } else {
                            Toast.makeText(Register.this, "Registrasi Gagal", // context, content
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

    }



    public boolean onSupportNavigateUp(){

        finish(); // menutup activity

        return true;
    }




}

