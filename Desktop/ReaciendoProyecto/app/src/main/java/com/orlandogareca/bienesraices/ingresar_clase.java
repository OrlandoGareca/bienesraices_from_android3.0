package com.orlandogareca.bienesraices;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by HP on 29/10/2017.
 */
public class ingresar_clase extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingresar);
    }
    public void ejecutar_pagina_principal(View view) {
        Intent c = new Intent(this, pagina_principal.class);
        startActivity(c);

    }
    public void login_agentes (View view){
        Intent c=new Intent(this,login_agente.class);
        startActivity(c);

    }
}
