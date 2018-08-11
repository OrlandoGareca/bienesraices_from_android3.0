package com.orlandogareca.bienesraices.restApi;

/**
 * Created by HP on 8/12/2017.
 */

import android.os.AsyncTask;
import android.view.View;

//import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class RestApi extends AsyncTask<String, String, String> {
    private ArrayList<Params> parameters;
    String method = "GET";
    private OnRestLoadListener listen;
    private int id;
    private File imgFile;
    private File fileTosend;
    public RestApi(String method){
        this.method = method;
        parameters = new ArrayList<Params>();
    }
    public void setOnRestLoadListener(OnRestLoadListener l, int id){
        this.listen = l;
        this.id = id;
    }
    public void setFile(File img) {
        this.imgFile = img;
    }
    private String getParameters() {
        String result = "";
        int l = parameters.size() - 1;
        for (int i = 0; i < l; i++) {
            result +=parameters.get(i).getKey() + "=" +parameters.get(i).getValue() + "&";
        }
        result +=parameters.get(l).getKey() + "=" +parameters.get(l).getValue();
        return result;
    }
    private String Post(String url) {
        String r = "";
        HttpURLConnection connection = null;
        try {
            URL service =new URL(url);
            connection = (HttpURLConnection) service.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            //name=Alfonso&email=alfonso@gmail.com&alfonso
            String cad = getParameters();
            byte[] params = cad.getBytes("UTF-8");

            connection.getOutputStream().write(params);
            InputStream result = connection.getInputStream();
            r = new Scanner(result, "UTF-8").useDelimiter("\\AA").next();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }
    private String Get(String url) {
        return "";
    }
    public void addParams (String key, String value) {
        parameters.add(new Params(key, value));
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = "";
        String url = strings[0];
        if(this.method.equals("POST")) {
            if(this.imgFile != null ) {
                result = PostImage(url);
            }else{
                result = Post(url);
            }
        }else if (this.method.equals("GET")) {
            result = Get(url);
        }
        return result;
    }
    private String PostImage(String url) {
        String result = "";
        try {
            MultipartUtility fileinParts = new MultipartUtility(url,"utf-8");
//            fileinParts.addFormField("idus","1");
            fileinParts.addFilePart("image", this.imgFile);
            result = fileinParts.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
    @Override
    protected void onPostExecute(String result){
        try {
            JSONObject rObject = new JSONObject(result);
            this.listen.onRestLoadComplete(rObject, this.id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
