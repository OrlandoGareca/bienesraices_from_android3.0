package com.orlandogareca.bienesraices;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.orlandogareca.bienesraices.Collection.Item;
import com.orlandogareca.bienesraices.restApi.OnRestLoadListener;
import com.orlandogareca.bienesraices.restApi.RestApi;
import com.orlandogareca.bienesraices.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by HP on 2/11/2017.
 */


public class pagina_principal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnRestLoadListener, View.OnClickListener {
    private Button boton_ordenar;
    private Button ubicarme;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina_principal);
        boton_ordenar = ( Button )findViewById( R.id.boton_ordenar);
        boton_ordenar.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        popupMenu();
                    }});
        // codifo para modo stricto

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        loadImages();
        //loadComponents();
        //checkRest();
    }
    private void loadImages() {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://192.168.100.36:3000/getimages", null, new JsonHttpResponseHandler() {
     @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<Item> image_list = new ArrayList<Item>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        String url = obj.getString("url");
                        String name = obj.getString("name");
                        Item item_list = new Item();
                        item_list.setTitle(name);
                        item_list.setUrl(url);
                        image_list.add(item_list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                loadListAdapter(image_list);
                //list.setAdapter(adapter);
            }
        });
    }

    private void loadListAdapter(ArrayList<Item> list) {
        ListView listUi = (ListView)this.findViewById(R.id.list_main);
        ListAdapter adapter = new com.orlandogareca.bienesraices.Collection.ListAdapter(this, list);
        listUi.setAdapter(adapter);
    }


    // cargar los componentes visuales que corresponden a la lista
    private void loadComponents() {
        ListView list = (ListView)this.findViewById(R.id.list_main);
        ArrayList<Item> list_data = new ArrayList<Item>();
        for (int i = 0; i < 100; i++ ) {
            Item p = new Item();
            p.id = i;
            p.title = "Titulo " + i;
            p.description = "Descripcion "+i;
            p.url = "image " + i;
            list_data.add(p);
        }

        ListAdapter adapter = new com.orlandogareca.bienesraices.Collection.ListAdapter(this, list_data);
        list.setAdapter(adapter);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Toast.makeText(this, Utils.email, Toast.LENGTH_SHORT).show();
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")


    @Override
    public void onRestLoadComplete(JSONObject obj, int id) {
        // EL POST
        if(id == 10){
            Toast.makeText(this, obj.toString(),Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View view) {
        Intent photo = new Intent(Intent.ACTION_PICK);
        photo.setType("image/");
        this.startActivityForResult(photo, 1);
        //super.onActivityResult();
    }
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if(requestCode == 1) {
            if(responseCode == Activity.RESULT_OK){
                Uri imagen = data.getData();
                String url = this.getPath(imagen);
                File myFile = new File(url);
                RequestParams params = new RequestParams();
                try {
                    params.put("image", myFile);
                } catch(FileNotFoundException e) {}

                // send request
                AsyncHttpClient client = new AsyncHttpClient();
                client.post("http://192.168.100.36:3000/sendimage", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                        // handle success response
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                        // handle failure response
                    }
                });
                //Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagen);
                    /*File fileimage = new File(this.getCacheDir(),"tmpImage");
                    fileimage.createNewFile();
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(fileimage));
                    bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.close();
                    RestApi api = new RestApi("POST");
                    api.setFile(fileimage);
                    api.setOnRestLoadListener(this,100);
                    api.execute("http://192.168.1.107:3000/sendimage");
                    */


                //String url = this.getPath(imagen);
                //File fileimage = new File(url);

            }
        }
    }
    public String getPath(Uri uri) {
        int column_index;
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public void ejecutar_mapa_cliente (View view){
        Intent g=new Intent(this,contener_mapa_clientes.class);
        startActivity(g);

    }
    public void ejecutar_opcion_izquierda (View view){
        Intent f=new Intent(this,opcion_izquierda.class);
        startActivity(f);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ordenar, menu);
        return true;
    }
    private void popupMenu()
    {
        //Crea instancia a PopupMenu
        PopupMenu popup = new PopupMenu(this, boton_ordenar);
        popup.getMenuInflater().inflate(R.menu.ordenar, popup.getMenu());
        //registra los eventos click para cada item del menu
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.accion1)
                {
                    Toast.makeText(pagina_principal.this,
                            "Buscar por : " + item.getTitle() ,Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId() == R.id.accion2)
                {
                    Toast.makeText(pagina_principal.this,
                            "Buscar por : " + item.getTitle() ,Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId() == R.id.accion3)
                {
                    Toast.makeText(pagina_principal.this,
                            "Buscar por : " + item.getTitle() ,Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        popup.show();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
