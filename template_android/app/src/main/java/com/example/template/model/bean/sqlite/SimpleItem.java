package com.example.template.model.bean.sqlite;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.template.model.db.SqliteCallBack;
import com.example.template.model.db.dbFlowDatabases.DatabaseModule;
import com.example.template.model.db.listener.IDataHelper;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;

import static com.example.template.model.db.Constants.DBMethodGetAll;

@Table(database = DatabaseModule.class)
public class SimpleItem extends BaseModel implements IDataHelper<SimpleItem> {
    @Column
    @PrimaryKey(autoincrement = true)
    public long id = 0l;

    @Column
    public String name;

    public static long getCount() {
        return new Select(Method.count()).from(SimpleItem.class).count();
    }

    @Override
    public void insertData(SimpleItem simpleItem, Context context, SqliteCallBack sqliteCallBack) {
//        simpleItem.async().insert();
        simpleItem.insert();

    }

    @Override
    public void updateData(SimpleItem simpleItem, Context context, SqliteCallBack sqliteCallBack) {
//        simpleItem.async().save();
        simpleItem.save();

    }

    @Override
    public void deleteData(SimpleItem simpleItem, Context context, SqliteCallBack sqliteCallBack) {
//        simpleItem.async().delete();
        simpleItem.delete();

//        SQLite.select()
//                .from(SimpleItem.class)
//                .async()
//                .querySingleResultCallback(new QueryTransaction.QueryResultSingleCallback<SimpleItem>() {
//                    @Override
//                    public void onSingleQueryResult(QueryTransaction transaction, @Nullable SimpleItem simpleItem1) {
//
//                        //deleted from ram not sqlite
//                        simpleItem1.delete();
//                    }
//                })
//                .error(new Transaction.Error() {
//                    @Override
//                    public void onError(Transaction transaction, Throwable error) {
//
//                        Exception ex = new Exception(error);
//                    }
//                })
//                .success(new Transaction.Success() {
//                    @Override
//                    public void onSuccess(@NonNull Transaction transaction) {
//                    }
//                })
//                .execute();
    }

    @Override
    public SimpleItem getDataByID(SimpleItem simpleItem, Context context, final SqliteCallBack sqliteCallBack) {

        SimpleItem simpleItem1 = SQLite.select()
                .from(SimpleItem.class)
                .where(SimpleItem_Table.id.eq(id))
                .querySingle();

        //sqliteCallBack.onDBDataObjectLoaded( simpleItem1,DBMethodGetById,"getDataByIDSimpleItem");
        return simpleItem1;

//
//   async
//        SQLite.select().from(SimpleItem.class)
//                   .where(SimpleItem_Table.id.eq(id))
//                   .async()
//                .queryResultCallback(
//                new QueryTransaction.QueryResultCallback<SimpleItem>() {
//            @Override
//            public void onQueryResult(final QueryTransaction<SimpleItem> transaction, @NonNull final CursorResult<SimpleItem> tResult) {
        //   sqliteCallBack.onDBDataObjectLoaded( tResult.toModelClose(),DBMethodGetById
        // ,"getDataByIDSimpleItem");
//            }
//      }
//      ).execute();

        //equivalent to
//        FlowManager.getDatabaseForTable(SimpleItem.class)
//                .beginTransactionAsync(new QueryTransaction.Builder<>(
//                        SQLite.select()
//                                .from(SimpleItem.class)
//                                .where(SimpleItem_Table.id.eq(id)))
//                        .queryResult(new QueryTransaction.QueryResultCallback<SimpleItem>() {
//                            @Override
//                            public void onQueryResult(QueryTransaction<SimpleItem> transaction, @NonNull CursorResult<SimpleItem> tResult) {
//
//                            }
//                        }).build())
//                .build().execute();


    }

    @Override
    public void getAll(SimpleItem simpleItem, Context context, final SqliteCallBack sqliteCallBack) {


//        List<SimpleItem> simpleItems = SQLite.select()
//                .from(SimpleItem.class)
//                 .queryList();

        SQLite.select().from(SimpleItem.class)
                .async()
                .queryResultCallback(
                        new QueryTransaction.QueryResultCallback<SimpleItem>() {
                            @Override
                            public void onQueryResult(final QueryTransaction<SimpleItem> transaction, @NonNull final CursorResult<SimpleItem> tResult) {
                                sqliteCallBack.onDBDataListLoaded(
                                        new ArrayList<SimpleItem>(tResult.toListClose()),
                                        DBMethodGetAll
                                        , "getAllSimpleItem"
                                );

                            }
                        }
                ).execute();
    }
}
