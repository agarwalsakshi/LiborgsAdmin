package com.liborgsadmin.interfaces;

import org.json.JSONObject;

/**
 * Created by sakshiagarwal on 10/11/16.
 */

public interface ResponseInterface {
    void onSuccess(JSONObject jsonObject);

    void onFailure(String message);

}
