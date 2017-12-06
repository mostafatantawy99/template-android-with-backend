package com.example.template.model.db.listener;

import android.content.Context;

import com.example.template.model.db.SqliteCallBack;

/**
 * Created by Net15 on 24/09/2017.
 */

public interface IDataHelper<T> {
    void insertData(T t, Context context, SqliteCallBack sqliteCallBack);

    void updateData(T t, Context context, SqliteCallBack sqliteCallBack);

    void deleteData(T t, Context context, SqliteCallBack sqliteCallBack);

    //      void getDataByID(T t, Context context, SqliteCallBack sqliteCallBack);
    T getDataByID(T t, Context context, SqliteCallBack sqliteCallBack);

    void getAll(T t, Context context, SqliteCallBack sqliteCallBack);
//      List<T> getAll(T t, Context context, SqliteCallBack sqliteCallBack);
}