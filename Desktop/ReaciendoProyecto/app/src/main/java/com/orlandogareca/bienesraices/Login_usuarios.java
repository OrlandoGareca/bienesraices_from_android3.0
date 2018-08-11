package com.orlandogareca.bienesraices;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

public class Login_usuarios extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    public static final int SIGN_IN_CODE =777;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_usuarios);
        GoogleSignInOptions gso =new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        signInButton =(SignInButton)findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent= Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,SIGN_IN_CODE);
            }
        });
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== SIGN_IN_CODE){
            GoogleSignInResult result =Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            goMainScreen();
        }else{
            Toast.makeText(this, R.string.not_log_in, Toast.LENGTH_SHORT).show();
        }
    }
    private void goMainScreen(){
        Intent intent = new Intent(this,vista_previa.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void jsonCallback(String url, JSONObject json, AjaxStatus status){
        if(json != null){
            //satisfactoriamente
            try{
                String msg=json.getString("msg");
                if(msg=="1"){
                    Toast.makeText(this,"ingreso satisfactoriamente", Toast.LENGTH_SHORT).show();
                    loadcorrecto();
                }else if(msg=="2"){

                Toast.makeText(this,"usuario no valido", Toast.LENGTH_SHORT).show();
                loadnocorrecto();

                }

            }
            catch (Exception ex){}

        }else{

            //ajax error
        }
    }

    private void loadnocorrecto() {
        Intent a=new Intent(this,Login_usuarios.class);
        startActivity(a);
    }

    private void loadcorrecto() {
        Intent a=new Intent(this,ingresar_clase.class);
        startActivity(a);

    }

    public void login(View view){
        EditText userName=(EditText)findViewById(R.id.usuario);
        EditText Password=(EditText)findViewById(R.id.contraseña);
        AQuery aq = new AQuery(this);

            String url = "http://192.168.100.36:3000/users?name="+userName.getText()+"&password="+Password.getText();
            aq.ajax(url, JSONObject.class, this, "jsonCallback");


    }
    public void ejecutar_reguistro (View view){
        Intent a=new Intent(this,signup.class);
        startActivity(a);

    }
}
