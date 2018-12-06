package de.techfunder.taigabugreport.network;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import de.techfunder.taigabugreport.pojo.events.ErrorUnauthorized;
import de.techfunder.taigabugreport.utils.ErrorParser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Qhash on 30.12.2016.
 */

public abstract class TaigaCallback<T> implements Callback<T> {

    public static final String TAG = "TaigaCallback";

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        try {

            if (response.isSuccessful()) {
                onSuccess(call, response);
            } else {
                onServerError(ErrorParser.getErrorMessage(response.errorBody()));
                Log.d(TAG, "ERROR CODE: " + response.code());
                if (response.code() == 401) {
                    EventBus.getDefault().post(new ErrorUnauthorized(ErrorParser.getErrorMessage(response.errorBody())));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onError(call, t);
    }

    public abstract void onSuccess(Call<T> call, Response<T> response);

    public abstract void onServerError(String error);

    public abstract void onError(Call<T> call, Throwable t);
}
