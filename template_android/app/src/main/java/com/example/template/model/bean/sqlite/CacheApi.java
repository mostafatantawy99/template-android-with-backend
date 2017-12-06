package com.example.template.model.bean.sqlite;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.template.model.db.SqliteCallBack;
import com.example.template.model.db.dbFlowDatabases.DatabaseModule;
import com.example.template.model.db.listener.IDataHelper;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;

import static com.example.template.model.db.Constants.DBMethodGetAll;
import static com.example.template.model.db.Constants.DBMethodGetById;

/**
 * Created by Net22 on 9/24/2017.
 */


@Table(database = DatabaseModule.class)
public class CacheApi extends BaseModel implements IDataHelper<CacheApi>, Parcelable {
    @Column
    @PrimaryKey(autoincrement = true)
    private Long id = 0l;
    @Column
    private String url;
    @Column
    private String params;
    @Column
    private String response;
    @Column
    private String beanName;
    private long date;

    public CacheApi() {
    }

    public CacheApi(Long id) {
        this.id = id;
    }

    public CacheApi(Long id, String url, String params, String response, String beanName
            , long date) {
        this.id = id;
        this.url = url;
        this.params = params;
        this.response = response;
        this.beanName = beanName;
        this.date = date;
    }

    public CacheApi(String url, String params) {
        this.url = url;
        this.params = params;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
        parcel.writeString(params);
        parcel.writeString(response);
        parcel.writeString(beanName);
        parcel.writeLong(date);
    }


    protected CacheApi(Parcel in) {
        url = in.readString();
        params = in.readString();
        response = in.readString();
        beanName = in.readString();
        date = in.readLong();
    }

    public static final Creator<CacheApi> CREATOR = new Creator<CacheApi>() {
        @Override
        public CacheApi createFromParcel(Parcel in) {
            return new CacheApi(in);
        }

        @Override
        public CacheApi[] newArray(int size) {
            return new CacheApi[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }


    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }


    @Override
    public CacheApi getDataByID(CacheApi cacheApi, Context context, final SqliteCallBack sqliteCallBack) {


        CacheApi cacheApi1 = SQLite.select()
                .from(CacheApi.class)
                .where(CacheApi_Table.params.eq(cacheApi.getParams()))
                .and(CacheApi_Table.url.eq(cacheApi.getUrl()))
                .querySingle();

        sqliteCallBack.onDBDataObjectLoaded(cacheApi1, DBMethodGetById
                , "getDataByIDCacheApi");

//        SQLite.select().from(CacheApi.class)
//                .where(CacheApi_Table.params.eq(cacheApi.getParams()))
//                .and(CacheApi_Table.url.eq(cacheApi.getUrl()))
//                .async()
//                .querySingleResultCallback(
//                        new QueryTransaction.QueryResultSingleCallback<CacheApi>() {
//                            @Override
//                            public void onSingleQueryResult(QueryTransaction transaction, @Nullable CacheApi cacheApi) {
//                                sqliteCallBack.onDBDataObjectLoaded(
//                                        cacheApi
//                                        ,DBMethodGetById);
//                            }
//                        }
//                ).execute();

        return cacheApi1;

    }

    @Override
    public void getAll(CacheApi cacheApi, Context context, final SqliteCallBack sqliteCallBack) {

        SQLite.select().from(CacheApi.class)
                .async()
                .queryResultCallback(
                        new QueryTransaction.QueryResultCallback<CacheApi>() {
                            @Override
                            public void onQueryResult(final QueryTransaction<CacheApi> transaction, @NonNull final CursorResult<CacheApi> tResult) {
                                sqliteCallBack.onDBDataListLoaded(
                                        new ArrayList<CacheApi>(tResult.toListClose())
                                        , DBMethodGetAll
                                        , "getAllCacheApi"
                                );
                            }
                        }
                ).execute();

    }


    @Override
    public void insertData(CacheApi cacheApi, Context context, SqliteCallBack sqliteCallBack) {

        cacheApi.insert();
    }

    @Override
    public void updateData(CacheApi cacheApi, Context context, SqliteCallBack sqliteCallBack) {

        cacheApi.save();
    }

    @Override
    public void deleteData(CacheApi cacheApi, Context context, SqliteCallBack sqliteCallBack) {

        cacheApi.delete();
    }


}