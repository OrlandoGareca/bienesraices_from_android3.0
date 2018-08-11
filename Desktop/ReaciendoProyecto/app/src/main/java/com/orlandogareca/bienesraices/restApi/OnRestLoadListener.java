package com.orlandogareca.bienesraices.restApi;

/**
 * Created by HP on 8/12/2017.
 */


import android.content.Intent;

import org.json.JSONObject;

public interface OnRestLoadListener {
    public void onRestLoadComplete(JSONObject obj, int id);

    void onActionResult(int requestCode, int resultCode, Intent data);
}