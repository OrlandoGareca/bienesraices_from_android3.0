package com.orlandogareca.bienesraices.Collection;

/**
 * Created by HP on 8/12/2017.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.orlandogareca.bienesraices.pagina_principal;
import com.orlandogareca.bienesraices.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {
    private Context context;
    private List<Item> items;
    public ListAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }
    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int i) {
        return this.items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return this.items.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflate = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflate.inflate(R.layout.item_ui, null);
        }
        TextView titulo = (TextView)view.findViewById(R.id.titletxt);
        ImageView image = (ImageView) view.findViewById(R.id.imageView2);
        String url = this.items.get(i).getUrl();
        try {
            URL route = new URL(url);
            InputStream in = route.openConnection().getInputStream();
            Bitmap img = BitmapFactory.decodeStream(in);
            image.setImageBitmap(img);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
        //InputStream in =
        //Bitmap img = BitmapFactory.decodeStream(in);
        //image.setImageBitmap();
        titulo.setText(this.items.get(i).getTitle());
        return view;
    }



}
