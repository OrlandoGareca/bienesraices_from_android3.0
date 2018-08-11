package com.orlandogareca.bienesraices;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelUuid;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.frosquivel.magicalcamera.MagicalCamera;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orlandogareca.bienesraices.restApi.OnRestLoadListener;
import com.orlandogareca.bienesraices.restApi.RestApi;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

import static com.orlandogareca.bienesraices.R.id.btntakephoto;

public class reguistrar_nuevo extends AppCompatActivity implements View.OnClickListener, OnRestLoadListener {
    EditText ethabitaciones;
    EditText etsalas;
    EditText etbaños;
    EditText etgarages;
    EditText etsuperficie;
    EditText etdireccion;
    EditText etprecio;
    EditText etestado;
    EditText etdescripcion;
    Button btninfo;

    ImageView imageView;
    Button btnfoto;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    EditText Detalles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reguistrar_nuevo);
        LoadEvents();
        ethabitaciones = (EditText)findViewById(R.id.habitaciones);
        etsalas = (EditText)findViewById(R.id.salas);
        etbaños = (EditText)findViewById(R.id.baños);
        etgarages = (EditText)findViewById(R.id.garage);
        etsuperficie = (EditText)findViewById(R.id.superficie);
        etdireccion = (EditText)findViewById(R.id.direccion);
        etprecio = (EditText)findViewById(R.id.precio);
        etestado = (EditText)findViewById(R.id.tipo);
        etdescripcion = (EditText)findViewById(R.id.descripcion);
        btninfo=(Button)findViewById(R.id.enviarinfo);
        btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestApi api = new RestApi("POST");
                api.setOnRestLoadListener(reguistrar_nuevo.this, 11);
                String datohabitaciones = ethabitaciones.getText().toString();
                String datosalas = etsalas.getText().toString();
                String datobaños = etbaños.getText().toString();
                String datogarage = etgarages.getText().toString();
                String datosuperficie = etsuperficie.getText().toString();
                String datodireccion = etdireccion.getText().toString();
                String datoprecio = etprecio.getText().toString();
                String datoestado = etestado.getText().toString();
                String datodescripcion = etdescripcion.getText().toString();
                api.addParams("cuartos", datohabitaciones);
                api.addParams("salas", datosalas);
                api.addParams("baños", datobaños);
                api.addParams("garage", datogarage);
                api.addParams("direccion", datodireccion);
                api.addParams("precio", datoprecio);
                api.addParams("tipo", datoestado);
                api.addParams("descripcion", datodescripcion);
                api.addParams("superficie", datosuperficie);

                api.execute("http://192.168.100.36:3000/inmueble");
            }
        });

        btnfoto=(Button)findViewById(R.id.btntakephoto);
        btnfoto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                llamarIntent();
            }
        });
    }
    public void llamarIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
    }
    public void volver_atras (View view){
        Intent d=new Intent(this,ingresar_clase.class);
        startActivity(d);
    }
    private void LoadEvents() {
        Button btn =(Button)this.findViewById(R.id.btnselectedphoto);
        btn.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        Intent camera = new Intent(Intent.ACTION_PICK);
        camera.setType("image/");
        this.startActivityForResult(camera, 1);
    }
    @Override
    public void onActivityResult(int code, int res, Intent data){

        if(res == Activity.RESULT_OK){
            Uri imagen = data.getData();
            String url = this.getPath(imagen);
            File file = new File(url);
            RequestParams params = new RequestParams();
            try {
                params.put("image", file);
                AsyncHttpClient client = new AsyncHttpClient();
                client.post("http://192.168.100.36:3000/sendimage", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        showToast();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        errorToas();
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
    public void showToast(){
        Toast.makeText(this,"se inserto con exito",Toast.LENGTH_SHORT).show();
    }
    public void errorToas(){
        Toast.makeText(this,"ERROR!!",Toast.LENGTH_SHORT).show();

    }
    private String getPath(Uri uri) {
        int column_index;
        String[] projection = {MediaStore.MediaColumns.DATA};

        Cursor cursor = managedQuery(uri,projection,null,null,null);
        column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    @Override
    public void onRestLoadComplete(JSONObject obj, int id) {
        //El post de inmueble
        if(id == 11){
            Toast.makeText(this, "se inserto correctamente",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActionResult(int requestCode, int resultCode, Intent data) {

    }
}

