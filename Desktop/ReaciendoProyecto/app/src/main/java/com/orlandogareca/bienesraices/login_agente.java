package com.orlandogareca.bienesraices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

public class login_agente extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_agente);
        /*Button boton =(Button)findViewById(R.id.button_ingresar_agentes);
        boton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String usuario=((EditText)findViewById(R.id.ingresa_usuario)).getText().toString();
                String contraseña=((EditText)findViewById(R.id.ingresa_contraseña)).getText().toString();
                if (usuario.equals("admin")&& contraseña.equals("admin")){
                    Intent nuevoform =new Intent(login_agente.this,opciones_de_agentes.class);
                    startActivity(nuevoform);
                }else{
                    Toast.makeText(getApplicationContext(), "Usuario Incorrecto", Toast.LENGTH_SHORT).show();
                }
            }
       });*/
    }
    public void jsonCallback(String url, JSONObject json, AjaxStatus status){
        if(json != null){
            //satisfactoriamente
            try{
                String msg=json.getString("msg");
                if (msg == "1") {
                    Toast.makeText(this,"ingreso exitoso", Toast.LENGTH_SHORT).show();
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
        Intent a=new Intent(this,login_agente.class);
        startActivity(a);
    }

    private void loadcorrecto() {
        Intent a=new Intent(this,opciones_de_agentes.class);
        startActivity(a);
    }

    public void login_age(View view){
        EditText user=(EditText)findViewById(R.id.ingresa_usuario);
        EditText Pass=(EditText)findViewById(R.id.ingresa_contraseña);
        AQuery aq = new AQuery(this);
        //String url = "http://192.168.100.36:3000/users/agente?nombre="+user.getText()+"&password="+Pass.getText();
        String url = "http://192.168.100.36:3000/users?name="+user.getText()+"&password="+Pass.getText();
        aq.ajax(url, JSONObject.class, this, "jsonCallback");

    }
    public void ejecutar_reguistro (View view){
        Intent a=new Intent(this,signup.class);
        startActivity(a);

    }

}
