/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.example.template.model.backend;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.template.model.DataManager;
import com.example.template.model.bean.CategoriesRestApiResponse;
import com.example.template.model.bean.CategoryRestApi;
import com.example.template.model.bean.CategoryRestApiResponse;
import com.example.template.model.bean.EditRestApiResponse;
import com.example.template.model.bean.ItemRestApiResponse;
import com.example.template.model.bean.ItemsRestApiResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.template.utils.Constants.param_securitykey;

/**
 * Created by janisharali on 28/01/17.
 */

public class ApiHelper {


    private static final String TAG = "ApiHelper";
    private static ApiHelper INSTANCE = null;
    private Context mContext;
    public APIService mAPIService;
    static DataManager.ISaveData listener;


    private ApiHelper(Context context) {

        // this.mContext=context;
        this.mContext = context.getApplicationContext();
        mAPIService = ApiUtils.getAPIService();

    }

    // Prevent direct instantiation.
    public static ApiHelper getInstance(Context context, DataManager.ISaveData iSaveData) {
        if (INSTANCE == null) {
            INSTANCE = new ApiHelper(context);
        }
        listener = iSaveData;
        return INSTANCE;
    }

    public static boolean checkInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {

            return false;

        }
    }

    // save to cache

    public void saveToCache(String url,
                            Map<String, String> params, String cachedData, String beanName) {
        listener.onRequireSaveData(url, params, cachedData, beanName);
    }



    //CATEGORIES


    public void getCategories(
            @NonNull final RestApiCallBack callback, final boolean caching
            , final String url, final Map<String, String> params
    ) {


        //rxjava 2
//         mAPIService.getCategoriesRX(param_securitykey )
//                 .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                  .subscribeWith(new DisposableObserver<CategoriesRestApiResponse>() {
//                      @Override
//                      public void onNext(@io.reactivex.annotations.NonNull CategoriesRestApiResponse response) {

//                               callback.onDataObjectLoaded(response, url );
//                              if (caching) {
//                                  saveToCache(url, params, new Gson().toJson(response),
//                                          response.getClass().getName());
//                              }
//                      }

//                      @Override
//                      public void onError(@io.reactivex.annotations.NonNull Throwable e) {
//                          callback.onNetworkError(e.getMessage(), url);
//                          e.printStackTrace();
//                          Log.d(TAG, "onFailure: " + e.getMessage());
//                      }

//                      @Override
//                      public void onComplete() {

//                      }
//                  });


       mAPIService.getCategories(param_securitykey).enqueue(new Callback<CategoriesRestApiResponse>() {
           @Override
           public void onResponse(Call<CategoriesRestApiResponse> call, Response<CategoriesRestApiResponse> response) {
               completeWithResponse(response, callback, caching, url, params);
           }


           @Override
           public void onFailure(Call<CategoriesRestApiResponse> call, Throwable t) {

               callback.onNetworkError(t.getMessage(), url);
               t.printStackTrace();
               Log.d(TAG, "onFailure: " + t.getMessage());
           }
       });

    }


    public void getCategoryById(long category_id, @NonNull final RestApiCallBack callback, final boolean caching
            , final String url, final Map<String, String> params
    ) {


        mAPIService.getCategoryById(category_id).enqueue(new Callback<CategoryRestApiResponse>() {
            @Override
            public void onResponse(Call<CategoryRestApiResponse> call, Response<CategoryRestApiResponse> response) {
                completeWithResponse(response, callback, caching, url, params);
            }

            @Override
            public void onFailure(Call<CategoryRestApiResponse> call, Throwable t) {

                callback.onNetworkError(t.getMessage(), url);
                t.printStackTrace();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void addCategory(String name, @NonNull final RestApiCallBack callback, final boolean caching
            , final String url, final Map<String, String> params
    ) {


        mAPIService.addCategory(name).enqueue(new Callback<EditRestApiResponse>() {
            @Override
            public void onResponse(Call<EditRestApiResponse> call, Response<EditRestApiResponse> response) {
                completeWithResponse(response, callback, caching, url, params);
            }

            @Override
            public void onFailure(Call<EditRestApiResponse> call, Throwable t) {

                callback.onNetworkError(t.getMessage(), url);
                t.printStackTrace();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void editCategory(String name, long category_id, @NonNull final RestApiCallBack callback, final boolean caching
            , final String url, final Map<String, String> params
    ) {


        mAPIService.editCategory(name, category_id).enqueue(new Callback<EditRestApiResponse>() {
            @Override
            public void onResponse(Call<EditRestApiResponse> call, Response<EditRestApiResponse> response) {
                completeWithResponse(response, callback, caching, url, params);
            }

            @Override
            public void onFailure(Call<EditRestApiResponse> call, Throwable t) {

                callback.onNetworkError(t.getMessage(), url);
                t.printStackTrace();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void deleteCategoryById(long category_id, @NonNull final RestApiCallBack callback, final boolean caching
            , final String url, final Map<String, String> params
    ) {


        mAPIService.deleteCategory(category_id).enqueue(new Callback<EditRestApiResponse>() {
            @Override
            public void onResponse(Call<EditRestApiResponse> call, Response<EditRestApiResponse> response) {
                completeWithResponse(response, callback, caching, url, params);
            }

            @Override
            public void onFailure(Call<EditRestApiResponse> call, Throwable t) {

                callback.onNetworkError(t.getMessage(), url);
                t.printStackTrace();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    ///ITEMS

    public void getItems(@NonNull final RestApiCallBack callback, final boolean caching
            , final String url, final Map<String, String> params
    ) {


        mAPIService.getItems(param_securitykey).enqueue(new Callback<ItemsRestApiResponse>() {
            @Override
            public void onResponse(Call<ItemsRestApiResponse> call, Response<ItemsRestApiResponse> response) {
                completeWithResponse(response, callback, caching, url, params);
            }

            @Override
            public void onFailure(Call<ItemsRestApiResponse> call, Throwable t) {

                callback.onNetworkError(t.getMessage(), url);
                t.printStackTrace();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    public void getItemsByCategory(long category_id, @NonNull final RestApiCallBack callback, final boolean caching
            , final String url, final Map<String, String> params
    ) {


        mAPIService.getItemsByCategoryId(category_id).enqueue(new Callback<ItemsRestApiResponse>() {
            @Override
            public void onResponse(Call<ItemsRestApiResponse> call, Response<ItemsRestApiResponse> response) {
                completeWithResponse(response, callback, caching, url, params);
            }

            @Override
            public void onFailure(Call<ItemsRestApiResponse> call, Throwable t) {

                callback.onNetworkError(t.getMessage(), url);
                t.printStackTrace();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    public void getItemById(long item_id, @NonNull final RestApiCallBack callback, final boolean caching
            , final String url, final Map<String, String> params
    ) {


        mAPIService.getItemById(item_id).enqueue(new Callback<ItemRestApiResponse>() {
            @Override
            public void onResponse(Call<ItemRestApiResponse> call, Response<ItemRestApiResponse> response) {
                completeWithResponse(response, callback, caching, url, params);
            }

            @Override
            public void onFailure(Call<ItemRestApiResponse> call, Throwable t) {

                callback.onNetworkError(t.getMessage(), url);
                t.printStackTrace();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    public void addItem(String name, String description, long category_id,
                        @NonNull final RestApiCallBack callback, final boolean caching
            , final String url, final Map<String, String> params
    ) {


        mAPIService.addItem(name, description, category_id).enqueue(new Callback<EditRestApiResponse>() {
            @Override
            public void onResponse(Call<EditRestApiResponse> call, Response<EditRestApiResponse> response) {
                completeWithResponse(response, callback, caching, url, params);
            }

            @Override
            public void onFailure(Call<EditRestApiResponse> call, Throwable t) {

                callback.onNetworkError(t.getMessage(), url);
                t.printStackTrace();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void editItem(long item_id, String name, String description, long category_id, @NonNull final RestApiCallBack callback, final boolean caching
            , final String url, final Map<String, String> params
    ) {


        mAPIService.editItem(item_id, name, description, category_id).enqueue(new Callback<EditRestApiResponse>() {
            @Override
            public void onResponse(Call<EditRestApiResponse> call, Response<EditRestApiResponse> response) {
                completeWithResponse(response, callback, caching, url, params);
            }

            @Override
            public void onFailure(Call<EditRestApiResponse> call, Throwable t) {

                callback.onNetworkError(t.getMessage(), url);
                t.printStackTrace();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void deleteItemById(long item_id, @NonNull final RestApiCallBack callback, final boolean caching
            , final String url, final Map<String, String> params
    ) {


        mAPIService.deleteItem(item_id).enqueue(new Callback<EditRestApiResponse>() {
            @Override
            public void onResponse(Call<EditRestApiResponse> call, Response<EditRestApiResponse> response) {
                completeWithResponse(response, callback, caching, url, params);
            }

            @Override
            public void onFailure(Call<EditRestApiResponse> call, Throwable t) {
                callback.onNetworkError(t.getMessage(), url);
                t.printStackTrace();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void completeWithResponse(Response response, RestApiCallBack callback, boolean caching,
                                      final String url, final Map<String, String> params) {
        if (response.isSuccessful()) {
            callback.onDataObjectLoaded(response.body(), url);
            if (caching) {
                saveToCache(url, params, new Gson().toJson(response.body()), response.body().getClass().getName());
            }
        } else {
            try {
                callback.onNetworkError(response.errorBody().string(), url);
            } catch (IOException e) {
                callback.onNetworkError("Unknown error", url);
                e.printStackTrace();
            }
        }
    }


}




