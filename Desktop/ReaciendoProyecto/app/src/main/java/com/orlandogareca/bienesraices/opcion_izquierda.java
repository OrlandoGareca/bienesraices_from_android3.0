package com.orlandogareca.bienesraices;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by HP on 2/11/2017.
 */
public class opcion_izquierda extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opciones_izquierda);
    }
    public void ejecutar_volver (View view){
        Intent f=new Intent(this,pagina_principal.class);
        startActivity(f);
    }

    public void ejecutar_terminos_y_condiciones (View view){
        Intent k=new Intent(this,terminos_y_condiciones.class);
        startActivity(k);
    }
}
