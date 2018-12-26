package app.blogapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.blogapp.config.server;


public class Edit extends AppCompatActivity {

    private Switch switch_password;

    MainActivity M = new MainActivity();
    private TextView text_password;
    private Button btn_save, btn_password;
    private EditText edit_username, edit_email, edit_phone, edit_password, edit_new, edit_confirm;
    private LinearLayout linear_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        btn_password = (Button) findViewById(R.id.btn_password);
        edit_username = (EditText) findViewById(R.id.edit_username);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_new = (EditText) findViewById(R.id.edit_new);
        edit_confirm = (EditText) findViewById(R.id.edit_confirm);
        text_password = (TextView) findViewById(R.id.text_password);
        btn_save = (Button) findViewById(R.id.btn_save);

        switch_password = (Switch) findViewById(R.id.switch_password);
        SwitchPassword();

        linear_password = (LinearLayout) findViewById(R.id.linear_password);

        edit_username.setText(M.username);
        edit_email.setText(M.email);
        edit_phone.setText(M.phone);
        edit_password.setText(M.password);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        btn_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit profile");
    }

    private void updateProfile(){
        if (edit_phone.getText().toString().equals("") || edit_email.getText().toString().equals("")){
            Toast.makeText(getApplication(), "Isi data dengan lengkap", Toast.LENGTH_LONG).show();
        } else if (!emailValidation(edit_email.getText().toString())){
            Toast.makeText(getApplication(), "Perbaiki format email", Toast.LENGTH_LONG).show();
            edit_email.requestFocus();
        } else if (edit_phone.length() < 5){
            Toast.makeText(getApplication(), "Nomer telpon minimal 5 karakter", Toast.LENGTH_LONG).show();
            edit_phone.requestFocus();
        } else {
            saveProfile();
        }
    }

    private void changePassword(){
        if (edit_password.getText().toString().equals("") || edit_new.getText().toString().equals("") || edit_confirm.getText().toString().equals("") ){
            Toast.makeText(getApplication(), "Isi data dengan lengkap", Toast.LENGTH_LONG).show();
        } else if (edit_new.getText().toString().equals(M.password) ){
            Toast.makeText(getApplication(), "Password baru tidak boleh sama dengan sebelumnya", Toast.LENGTH_LONG).show();
        } else if (edit_new.length() < 3 ){
            Toast.makeText(getApplication(), "Password minimal 3 karakter", Toast.LENGTH_LONG).show();
            edit_new.requestFocus();
        } else if (!edit_new.getText().toString().equals(edit_confirm.getText().toString()) ){
            Toast.makeText(getApplication(), "Konfirmasi password dengan benar", Toast.LENGTH_LONG).show();
        } else{
            savePass();
        }
    }


    private void SwitchPassword(){
        switch_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    text_password.setTextColor(getResources().getColor(R.color.colorPrimary));
                    linear_password.setVisibility(View.VISIBLE);
                } else {
                    text_password.setTextColor(Color.parseColor("#999999"));
                    linear_password.setVisibility(View.GONE);
                }
            }
        });
    }

    private void saveProfile(){
        AndroidNetworking.post(server.host + "update_profile.php")
                .addBodyParameter("email", edit_email.getText().toString())
                .addBodyParameter("phone", edit_phone.getText().toString())
                .addBodyParameter("user_id", M.user_id)
                .addBodyParameter("username", M.username)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.optString("response").equals("success")){
                            Toast.makeText(getApplication(), "Perubahan berhasil disimpan",
                                    Toast.LENGTH_SHORT).show();

                            sessProfile(
                                    edit_email.getText().toString(), edit_phone.getText().toString()
                            );

                        } else  if (response.optString("response").equals("failed")){
                            Toast.makeText(getApplication(), response.optString("response").toString(),
                                    Toast.LENGTH_SHORT).show();
                            Log.d("Log result", response.optString("response").toString());
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

    private void savePass(){
        AndroidNetworking.post(server.host+ "update_password.php")
                .addBodyParameter("password", edit_password.getText().toString())
                .addBodyParameter("user_id", M.user_id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.optString("response").equals("success")){
                            Toast.makeText(getApplication(), "Perubahan berhasil disimpan",
                                    Toast.LENGTH_SHORT).show();

                            sessPass(
                                    edit_password.getText().toString()
                            );

                        } else  if (response.optString("response").equals("failed")){
                            Toast.makeText(getApplication(), response.optString("response").toString(),
                                    Toast.LENGTH_SHORT).show();
                            Log.d("Log result", response.optString("response").toString());
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
    private void sessProfile(String email, String phone){

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);       M.email = email;
        editor.putString("phone", phone);       M.phone = phone;
        editor.apply();
    }

    //     Create session
    private void sessPass(String password){
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", password); M.password = password;
        editor.apply();
    }


    private boolean emailValidation(String emailInput) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailInput);
        return matcher.matches();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
