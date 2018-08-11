package com.orlandogareca.bienesraices;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class contenedor_mapa_colegios extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private GoogleMap mMap;
    private Boolean isInadd = false;
    private TextView txt_view;
    private ArrayList<Marker> list_markers;
    //private int list_markers=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenedor_mapa_colegios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txt_view = (TextView)this.findViewById(R.id.text_aviso);
        FloatingActionButton storeBtn =(FloatingActionButton)this.findViewById(R.id.store);
        loadData();
        storeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestParams params = new RequestParams();
                String send = "";
                for (int i = 0 ;i< list_markers.size(); i++){
                    LatLng coor= list_markers.get(i).getPosition();
                    send +="{" + coor.latitude + "," +coor.longitude + "}";
                }
                params.put("coor",send);
                AsyncHttpClient client = new AsyncHttpClient();
                client.post("http://192.168.100.36:3000/sendcoordscole", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        showToast();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        errorToas();
                    }
                });
            }

        });
        list_markers = new ArrayList<Marker>();
        FloatingActionButton removeBtn = (FloatingActionButton) findViewById(R.id.remove);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list_markers.size() > 0){
                    Marker aux = list_markers.get(list_markers.size()-1);
                    aux.remove();
                    list_markers.remove(list_markers.size()-1);
                }
            }
        });
        FloatingActionButton addBtn = (FloatingActionButton) findViewById(R.id.add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInadd == true) {
                    isInadd = false;
                } else {
                    isInadd = true;
                }
                upDateMsnText();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment)this.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    public void showToast(){
        Toast.makeText(this,"se inserto con exito",Toast.LENGTH_SHORT).show();
    }
    public void errorToas(){
        Toast.makeText(this,"ERROR!!",Toast.LENGTH_SHORT).show();

    }
    private void upDateMsnText() {

        if(isInadd==true){
            txt_view.setText("Add Mark");
        }else {
            txt_view.setText("");

        }
    }
    public void loadData(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.100.36:3000/getCoorscole", null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
                JSONArray aux = timeline;
                ArrayList<LatLng> list_lat = new ArrayList<LatLng>();
                for (int i = 0; i < aux.length(); i++) {
                    try {
                        JSONObject obj = aux.getJSONObject(i);
                        double lat = Double.parseDouble(obj.get("latitud").toString());
                        double lng = Double.parseDouble(obj.get("longitud").toString());
                        list_lat.add(new LatLng(lat, lng));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                for (int i = 0; i < list_lat.size(); i++) {
                    setMark(list_lat.get(i));
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng potosi = new LatLng(-19.570121, -65.759534);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(potosi,15));
        mMap.setOnMapClickListener(this);
        LatLng casa = new LatLng(-19.564738, -65.766033);
        mMap.addMarker(new MarkerOptions().position(casa).title("mi casa").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        LatLng PICHINCHA = new LatLng(-19.588809, -65.753022);
        mMap.addMarker(new MarkerOptions().position(PICHINCHA).title("Col. Nal. PICHINCHA").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng lICEOSUCRE = new LatLng( -19.588648, -65.751093);
        mMap.addMarker(new MarkerOptions().position(lICEOSUCRE).title("lICEO SR. SUCRE").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng CALERO = new LatLng( -19.588100, -65.751061);
        mMap.addMarker(new MarkerOptions().position(CALERO).title("Col. J.M. CALERO").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng LICEOPOTOSI = new LatLng( -19.585395, -65.753372);
        mMap.addMarker(new MarkerOptions().position(LICEOPOTOSI).title("lICEO SR. POTOSI").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng LITORAL = new LatLng(  -19.581685, -65.754844);
        mMap.addMarker(new MarkerOptions().position(LITORAL).title("COLEGIO LITORAL").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng MEDINACELI = new LatLng( -19.582150, -65.757588);
        mMap.addMarker(new MarkerOptions().position(MEDINACELI).title("COLEGIO CARLOS MEDINACELI").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng SANTAMARIA = new LatLng( -19.582983, -65.758502);
        mMap.addMarker(new MarkerOptions().position(SANTAMARIA).title("COLEGIO CATOLIGO PARTICULAR SANTA MARIA").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng MARIAAXUILIADORA = new LatLng(-19.581794, -65.758528);
        mMap.addMarker(new MarkerOptions().position(MARIAAXUILIADORA).title("UNIDAD EDUCATIVA MARIA AUXILIADORA").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng COLEGIOBERRIOS = new LatLng(-19.580503, -65.761828);
        mMap.addMarker(new MarkerOptions().position(COLEGIOBERRIOS).title("COLEGIO DAVID BERRIOS").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng PABLO2 = new LatLng(-19.577122, -65.773056);
        mMap.addMarker(new MarkerOptions().position(PABLO2).title("UNIDAD EDUCATIVA JUAN PABLO II").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng FEYALEGRIA = new LatLng( -19.571809, -65.765447);
        mMap.addMarker(new MarkerOptions().position(FEYALEGRIA).title("UNIDAD EDUCATIVA FE Y ALEGRIA").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng DIVINOMAESTRO = new LatLng( -19.562238, -65.770786);
        mMap.addMarker(new MarkerOptions().position(DIVINOMAESTRO).title("UNIDAD EDUCATIVA DIVINO MAESTRO").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng KENYPRIETO = new LatLng(  -19.559342, -65.760185);
        mMap.addMarker(new MarkerOptions().position(KENYPRIETO).title("UNIDAD EDUCATIVA KENY PRIETO MELGAREJO").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng ECONOMIA = new LatLng(  -19.570666, -65.760964);
        mMap.addMarker(new MarkerOptions().position(ECONOMIA).title("CARRERA DE ECONOMIA").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng CIUDADELA = new LatLng(  -19.557607, -65.763333);
        mMap.addMarker(new MarkerOptions().position(CIUDADELA).title("CIUDADELA UNIVERSITARIA").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng UPDS = new LatLng(  -19.568886, -65.764434);
        mMap.addMarker(new MarkerOptions().position(UPDS).title("UNIVERSIDAD PRIVADA DOMINGO SAVIO").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng ANDESDESANTACRUZ = new LatLng(  -19.572438, -65.756017);
        mMap.addMarker(new MarkerOptions().position(ANDESDESANTACRUZ).title("ANDRES DE SANTA CRUZ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng DANIELCAMPOS = new LatLng(  -19.579576, -65.751216);
        mMap.addMarker(new MarkerOptions().position(DANIELCAMPOS).title("UNIDAD EDUCATIVA DANIEL CAMPOS").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng SSCC = new LatLng(  -19.576190, -65.749166);
        mMap.addMarker(new MarkerOptions().position(SSCC).title("SAGRADOS CORAZONES DE JESUS Y MARIA").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }
    public void setMark(LatLng latLng) {
        Marker m = mMap.addMarker(new MarkerOptions().position(latLng).title("No title").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

    }
    @Override
    public void onMapClick(LatLng latLng) {
        if(isInadd){
            EditText edit_name;
            edit_name = (EditText) findViewById(R.id.nombremarcador);
            String name = edit_name.getText().toString();
            Marker m = mMap.addMarker(new MarkerOptions().position(latLng).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            list_markers.add(m);
        }
    }
    public void entrar_formulario (View view){
        Intent a=new Intent(this,opciones_de_agentes.class);
        startActivity(a);
    }
}

