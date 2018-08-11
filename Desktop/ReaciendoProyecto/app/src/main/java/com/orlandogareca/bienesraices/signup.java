package com.orlandogareca.bienesraices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.orlandogareca.bienesraices.restApi.OnRestLoadListener;
import com.orlandogareca.bienesraices.restApi.RestApi;

import org.json.JSONObject;

//
/**
 * Created by HP on 13/12/2017.
 */

public class signup extends AppCompatActivity implements OnRestLoadListener {
    EditText edit_name;
    EditText edit_email;
    EditText edit_pass;
    Button btn_sign;
    Button btn_login;
    private static final String REGISTER_URL="http://192.168.100.36:3000/createuser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        edit_name = (EditText) findViewById(R.id.ret_username);
        edit_email = (EditText) findViewById(R.id.ret_email);
        edit_pass = (EditText) findViewById(R.id.ret_password);
        btn_sign = (Button) findViewById(R.id.btn_signup);

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //registerUser();
                checkRest();
            }
        });
        btn_login=(Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin=new Intent(signup.this,Login_usuarios.class);
                startActivity(intentLogin);
            }
        });
        }
    private void checkRest() {
        RestApi api = new RestApi("POST");
        api.setOnRestLoadListener(this, 10);
        String name = edit_name.getText().toString();
        String email = edit_email.getText().toString();
        String password = edit_pass.getText().toString();

        api.addParams("name", name);
        api.addParams("email", email);
        api.addParams("password", password);
        api.execute("http://192.168.100.36:3000/createuser");
    }

    @Override
    public void onRestLoadComplete(JSONObject obj, int id) {
        // EL POST
        if(id == 10){
            Toast.makeText(this,"reguistro exitoso",Toast.LENGTH_SHORT).show();
        }
        if(id == 11){

        }
        if(id == 100) {
            Toast.makeText(this,obj.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActionResult(int requestCode, int resultCode, Intent data) {

    }
}



