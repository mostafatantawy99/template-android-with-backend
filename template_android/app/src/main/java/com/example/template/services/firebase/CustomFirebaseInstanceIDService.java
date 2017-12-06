package com.example.template.services.firebase;

/**
 * Created by aqsa on 7/3/2016.
 */

import android.util.Log;

import com.example.template.model.backend.APIService;
import com.example.template.model.backend.RetrofitClient;
import com.example.template.model.shareddata.Prefs;
import com.example.template.utils.Constants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.template.utils.UserInfo.FIREBASE_TOKEN;


public class CustomFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = CustomFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Token Value: " + refreshedToken);

        Prefs.putString(FIREBASE_TOKEN, refreshedToken);

        sendTheRegisteredTokenToWebServer(refreshedToken);
    }

    public void sendTheRegisteredTokenToWebServer(final String token) {

        //clearRetrofit();
        Retrofit retrofit;
        retrofit = RetrofitClient.getClient(Constants.SERVER_URL_PHP);
//          retrofit = RetrofitClient.getClient(Constants.SERVER_URL_NODE);
        //   retrofit = RetrofitClient.getClient(Constants.SERVER_URL_PYTHON);

        final APIService networkCall = retrofit.create(APIService.class);

        //rxjava 2
//       networkCall.updateTokenRX(token )
//                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                 .subscribeWith(new DisposableObserver<ResponseBody>() {
//                     @Override
//                     public void onNext(@io.reactivex.annotations.NonNull ResponseBody response) {
//
//                         try {
//                             Log.e(" sent token success" , " f "+response.string());
//                         } catch (IOException e) {
//                             e.printStackTrace();
//                         }
//
//                     }
//
//                     @Override
//                     public void onError(@io.reactivex.annotations.NonNull Throwable e) {
//
//                     }
//
//                     @Override
//                     public void onComplete() {
//
//                     }
//                 });

        networkCall.updateToken(token)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                Log.e(" sent token success", "" + response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {

                            Log.e("Registration Service", "Error :Send Token Failed");
//
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Registration Service", "Error :Send Token Failed");
                        t.printStackTrace();
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });


    }


}


