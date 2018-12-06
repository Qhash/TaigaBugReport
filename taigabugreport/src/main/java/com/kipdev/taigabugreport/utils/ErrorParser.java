package com.kipdev.taigabugreport.utils;

import com.google.gson.Gson;

import com.kipdev.taigabugreport.pojo.reponse.ErrorResponse;
import okhttp3.ResponseBody;

/**
 * Created by Qhash on 21.09.2016.
 */
public class ErrorParser {

    public static String getErrorMessage(ResponseBody responseBody) {
        Gson gson = new Gson();
        try {
            ErrorResponse errorResponse = gson.fromJson(responseBody.string(), ErrorResponse.class);
            return errorResponse.getError();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown error";
        }
    }
}
