package com.orlandogareca.bienesraices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class opciones_de_agentes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_de_agentes);
    }
    public void ejecutar_salir (View view){
        Intent i=new Intent(this,ingresar_clase.class);
        startActivity(i);
    }
    public void reguistrar_nuevo (View view){
        Intent i=new Intent(this,contenedor_mi_ubicacion.class);
        startActivity(i);
    }
    public void colegio_nuevo (View view){
        Intent m=new Intent(this,contenedor_mapa_colegios.class);
        startActivity(m);
    }

}
