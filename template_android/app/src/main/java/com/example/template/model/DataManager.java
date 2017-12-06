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

package com.example.template.model;


import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import com.example.template.model.backend.ApiHelper;
import com.example.template.model.backend.RestApiCallBack;
import com.example.template.model.bean.sqlite.CacheApi;
import com.example.template.model.db.DbHelper;
import com.example.template.model.db.SqliteCallBack;
import com.example.template.model.shareddata.Prefs;
import com.example.template.utils.Constants;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.template.utils.Constants.ApiKey;
import static com.example.template.utils.Constants.Page;
import static com.example.template.utils.Constants.param_category_id;
import static com.example.template.utils.Constants.param_category_name;
import static com.example.template.utils.Constants.param_item_description;
import static com.example.template.utils.Constants.param_item_id;
import static com.example.template.utils.Constants.param_item_name;
import static com.example.template.utils.Constants.param_securitykey;

/**
 * Created by janisharali on 27/01/17.
 */

public class DataManager {
    String url;
    Map<String, String> params;
    private Context mContext;
    private DbHelper mDbHelper;
    private ApiHelper mApiHelper;
    private static DataManager INSTANCE = null;
    RestApiCallBack<Object> mRestApiCallBack;
    RestApiCallBack<Object> mPresenterRestApiCallBack;
    SqliteCallBack<Object> mSqliteCallBack;
    SqliteCallBack<Object> mPresenterSqliteCallBack;

    // Prevent direct instantiation.
    public static DataManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DataManager(context);
        }
        return INSTANCE;
    }

    private DataManager(Context context) {
        mContext = context;
        mApiHelper = ApiHelper.getInstance(context, new ISaveData() {
            @Override
            public void onRequireSaveData(String url, Map<String, String> params, String result, String type) {
                //testing
                //  if (type.equalsIgnoreCase(CacheApi.class.getName())) {
                CacheApi cacheApi = new CacheApi();
                cacheApi.setUrl(url);
                cacheApi.setParams(params.toString());
                cacheApi.setResponse(result);
                cacheApi.setBeanName(type);
                cacheApi.setDate(new Date().getTime());
                //    cacheApi.insertData(cacheApi,mContext,mSqliteCallBack);
                mDbHelper.insertData(cacheApi, mContext, mSqliteCallBack);
                // }
            }
        });
        mDbHelper = DbHelper.getInstance(context);
        setRestApiCallBack();
        setSqliteCallBack();
    }


    public void initData() {
        new Prefs.Builder()
                .setContext(mContext)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(mContext.getPackageName())
                .setUseDefaultSharedPreference(false)
                .build();


        FlowManager.init(FlowConfig.builder(mContext)
                .openDatabasesOnInit(true)
                .build());


    }


    private void setRestApiCallBack() {
        mRestApiCallBack = new RestApiCallBack<Object>() {
            @Override
            public void onDataListLoaded(ArrayList<Object> data, String url) {
                mPresenterRestApiCallBack.onDataListLoaded(data, url);
            }

            @Override
            public void onDataObjectLoaded(Object data, String url) {
                mPresenterRestApiCallBack.onDataObjectLoaded(data, url);
            }

            @Override
            public void onNetworkError(String message, String url) {
                mPresenterRestApiCallBack.onNetworkError(message, url);
            }

            @Override
            public void onNoInternet() {
                mPresenterRestApiCallBack.onNoInternet();
            }
        };
    }

    private void setSqliteCallBack() {

        mSqliteCallBack = new SqliteCallBack() {
            @Override
            public void onDBDataListLoaded(List data, String methodName, String localDbOperation) {
                mPresenterSqliteCallBack.onDBDataListLoaded(data, methodName, localDbOperation);
            }

            @Override
            public void onDBDataObjectLoaded(Object data, String methodName, String localDbOperation) {
                mPresenterSqliteCallBack.onDBDataObjectLoaded(data, methodName, localDbOperation);

            }


        };

    }

    public void setPresenterRestApiCallBack(RestApiCallBack<Object> callBack) {
        mPresenterRestApiCallBack = callBack;
    }


    public void setPresenterSqliteCallBack(SqliteCallBack<Object> callBack) {
        mPresenterSqliteCallBack = callBack;
    }

    // get from cache
    private void loadData(String url, Map<String, String> params) {
        CacheApi tmpCache = new CacheApi(url, params.toString());
        mDbHelper.getDataByID(tmpCache, mContext, mSqliteCallBack);
    }


    public DbHelper getDataHelper() {
        return mDbHelper;
    }


    public interface ISaveData {
        void onRequireSaveData(String url, Map<String, String> params, String result, String type);
    }

    public Object loadCacheData(String url, String params) {
        String result = null;
        String beanName = null;
        CacheApi tmpCache = new CacheApi(url, params);
        tmpCache = (CacheApi) mDbHelper.getDataByID(tmpCache, mContext, mSqliteCallBack);

        if (tmpCache != null) {
            beanName = tmpCache.getBeanName();
            result = tmpCache.getResponse();
            try {
                Class type = Class.forName(beanName);
                Gson gson = new Gson();
                Object ob = (gson.fromJson(result, type));
                return ob;
            } catch (Exception ex) {
                Log.e("Exception Cache", ex.toString());
            }
        }

        return null;
    }

    public Object loadCacheData(CacheApi tmpCache) {
        String result = null;
        String beanName = null;

        if (tmpCache != null) {
            beanName = tmpCache.getBeanName();
            result = tmpCache.getResponse();
            try {
                Class type = Class.forName(beanName);
                Gson gson = new Gson();
                Object ob = (gson.fromJson(result, type));
                return ob;
            } catch (Exception ex) {
                Log.e("Exception Cache", ex.toString());
            }
        }

        return null;
    }

    ///Backend  Rest  Api

    //Backend Categories and Items

    public void getRemoteCategories() {
        //check for connection if true go to apihelper and get model else check if these model cached so get it
        url = Constants.getAllCategoriesWithoutItems;
        params = new HashMap<>();
        params.put(param_securitykey, param_securitykey);
        if (ApiHelper.checkInternet(mContext)) {
            mApiHelper.getCategories(mRestApiCallBack, true, url, params);
        } else {
            //Toast.makeText(mContext, mContext.getString(R.string.noInternetConnection), Toast.LENGTH_LONG).show();
            mRestApiCallBack.onNoInternet();
            loadData(url, params);
        }

    }

    public void getRemoteItems() {
        //check for connection if true go to apihelper and get model else check if these model cached so get it
        url = Constants.getAllItemsOrderedByCategory;
        params = new HashMap<>();
        params.put(param_securitykey, param_securitykey);

        if (ApiHelper.checkInternet(mContext)) {
            mApiHelper.getItems(mRestApiCallBack, true, url, params);
        } else {
            //Toast.makeText(mContext, mContext.getString(R.string.noInternetConnection), Toast.LENGTH_LONG).show();
            mRestApiCallBack.onNoInternet();
            loadData(url, params);
        }

    }

    public void getRemoteItemsByCategoryId(long category_id) {
        //check for connection if true go to apihelper and get model else check if these model cached so get it
        url = Constants.getAllItemsByCategory;
        params = new HashMap<>();
        params.put(param_category_id, "" + category_id);

        if (ApiHelper.checkInternet(mContext)) {
            mApiHelper.getItemsByCategory(category_id, mRestApiCallBack, true, url, params);
        } else {
            //Toast.makeText(mContext, mContext.getString(R.string.noInternetConnection), Toast.LENGTH_LONG).show();
            mRestApiCallBack.onNoInternet();
            loadData(url, params);
        }

    }

    public void getRemoteCategoryById(long category_id) {
        //check for connection if true go to apihelper and get model else check if these model cached so get it
        url = Constants.getCategoryById;
        params = new HashMap<>();
        params.put(param_category_id, "" + category_id);

        if (ApiHelper.checkInternet(mContext)) {
            mApiHelper.getCategoryById(category_id, mRestApiCallBack, true, url, params);
        } else {
            //Toast.makeText(mContext, mContext.getString(R.string.noInternetConnection), Toast.LENGTH_LONG).show();
            mRestApiCallBack.onNoInternet();
            loadData(url, params);
        }

    }

    public void getRemoteItemById(long item_id) {
        //check for connection if true go to apihelper and get model else check if these model cached so get it
        url = Constants.getItemById;
        params = new HashMap<>();
        params.put(param_item_id, "" + item_id);

        if (ApiHelper.checkInternet(mContext)) {
            mApiHelper.getItemById(item_id, mRestApiCallBack, true, url, params);
        } else {
            //Toast.makeText(mContext, mContext.getString(R.string.noInternetConnection), Toast.LENGTH_LONG).show();
            mRestApiCallBack.onNoInternet();
            loadData(url, params);
        }

    }

    public void addRemoteCategory(String name) {
        //check for connection if true go to apihelper and get model else check if these model cached so get it
        url = Constants.addCategory;
        params = new HashMap<>();
        params.put(param_category_name, "" + name);

        if (ApiHelper.checkInternet(mContext)) {
            mApiHelper.addCategory(name, mRestApiCallBack, false, url, params);
        } else {
            mRestApiCallBack.onNoInternet();
        }

    }


    public void editRemoteCategory(String name, long category_id) {
        //check for connection if true go to apihelper and get model else check if these model cached so get it
        url = Constants.editCategory;
        params = new HashMap<>();
        params.put(param_category_name, "" + name);
        params.put(param_category_id, "" + category_id);

        if (ApiHelper.checkInternet(mContext)) {
            mApiHelper.editCategory(name, category_id, mRestApiCallBack, false, url, params);
        } else {
            mRestApiCallBack.onNoInternet();
        }

    }

    public void deleteRemoteCategory(long category_id) {
        //check for connection if true go to apihelper and get model else check if these model cached so get it
        url = Constants.deleteCategory;
        params = new HashMap<>();
        params.put(param_category_id, "" + category_id);

        if (ApiHelper.checkInternet(mContext)) {
            mApiHelper.deleteCategoryById(category_id, mRestApiCallBack, false, url, params);
        } else {
            mRestApiCallBack.onNoInternet();
        }

    }


    public void addRemoteItem(String name, String description, long category_id) {
        //check for connection if true go to apihelper and get model else check if these model cached so get it
        url = Constants.addItem;
        params = new HashMap<>();
        params.put(param_item_name, "" + name);
        params.put(param_item_description, "" + description);
        params.put(param_category_id, "" + category_id);

        if (ApiHelper.checkInternet(mContext)) {
            mApiHelper.addItem(name, description, category_id,
                    mRestApiCallBack, false, url, params);
        } else {
            mRestApiCallBack.onNoInternet();
        }

    }


    public void editRemoteItem(long item_id, String name, String description, long category_id) {
        //check for connection if true go to apihelper and get model else check if these model cached so get it
        url = Constants.editItem;
        params = new HashMap<>();
        params.put(param_item_id, "" + item_id);
        params.put(param_item_name, "" + name);
        params.put(param_item_description, "" + description);
        params.put(param_category_id, "" + category_id);

        if (ApiHelper.checkInternet(mContext)) {
            mApiHelper.editItem(item_id, name, description, category_id, mRestApiCallBack, false, url, params);
        } else {
            mRestApiCallBack.onNoInternet();
        }

    }

    public void deleteRemoteItem(long item_id) {
        //check for connection if true go to apihelper and get model else check if these model cached so get it
        url = Constants.deleteItem;
        params = new HashMap<>();
        params.put(param_item_id, "" + item_id);

        if (ApiHelper.checkInternet(mContext)) {
            mApiHelper.deleteItemById(item_id, mRestApiCallBack, false, url, params);
        } else {
            mRestApiCallBack.onNoInternet();
        }

    }

    ///Sqlite Dbflow

    ////Sqlite Dbflow  simpleItem


    public void addLocalSimpleItem() {

        mDbHelper.addSimpleItem(mSqliteCallBack);
    }

    public long getLocalSimpleItemsCount() {

        return mDbHelper.getSimpleItemsCount(mSqliteCallBack);
    }

    //////Sqlite Dbflow   categories and items


    public void getLocalCategoriesWithoutItems() {

        mDbHelper.getCategoriesWithoutItems(mSqliteCallBack);
    }

    public void getLocalItems() {

        mDbHelper.getItems(mSqliteCallBack);
    }

    public void getLocalCategoriesWithItems() {
        mDbHelper.getCategoriesWithItems(mSqliteCallBack);
    }

    public void getLocalItemsByCategoryId(long category_id) {
        mDbHelper.getItemsByCategoryId(category_id, mSqliteCallBack);
    }

    public void getLocalItemsByCategoriesIds(ArrayList<Long> ids) {
        mDbHelper.getItemsByCategoriesIds(ids, mSqliteCallBack);
    }


    public void getLocalCategoriesWhereIdInIds(ArrayList<Long> ids) {
        mDbHelper.getCategoriesWhereIdInIds(ids, mSqliteCallBack);
    }

    public void getLocalItemsWhereIdInCategoriesIds(ArrayList<Long> ids) {
        mDbHelper.getItemsWhereIdInCategoriesIds(ids, mSqliteCallBack);
    }

    public void getLocalCustomListCategoriesItemsJoin() {
        mDbHelper.getCustomListCategoriesItemsJoin(mSqliteCallBack);
    }


    public void getLocalCategoryById(long category_id) {
        mDbHelper.getCategoryById(category_id, mSqliteCallBack);
    }

    public void getLocalItemById(long item_id) {
        mDbHelper.getItemById(item_id, mSqliteCallBack);
    }

    public void addLocalCategory(String name) {
        mDbHelper.addCategory(name, mSqliteCallBack);
    }

    public void editLocalCategory(String name, long category_id) {
        mDbHelper.editCategory(name, category_id, mSqliteCallBack);
    }

    public void deleteLocalCategory(long category_id) {
        mDbHelper.deleteCategory(category_id, mSqliteCallBack);
    }

    public void addLocalItem(String name, String description, long category_id) {
        mDbHelper.addItem(name, description, category_id, mSqliteCallBack);
    }

    public void editLocalItem(long item_id, String name, String description, long category_id) {
        mDbHelper.editItem(item_id, name, description, category_id, mSqliteCallBack);
    }

    public void deleteLocalItem(long item_id) {
        mDbHelper.deleteItem(item_id, mSqliteCallBack);
    }


}
