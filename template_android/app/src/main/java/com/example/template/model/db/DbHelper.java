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

package com.example.template.model.db;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.template.model.bean.sqlite.CacheApi;
import com.example.template.model.bean.sqlite.Categories;
import com.example.template.model.bean.sqlite.Categories_Table;
import com.example.template.model.bean.sqlite.Items;
import com.example.template.model.bean.sqlite.Items_Table;
import com.example.template.model.bean.sqlite.Note;
import com.example.template.model.bean.sqlite.NoteType;
import com.example.template.model.bean.sqlite.SimpleItem;
import com.example.template.model.bean.sqlite.querymodels.CategoriesItemsQueryModel;
import com.example.template.model.bean.sqlite.querymodels.CategoriesWithOutItemsQueryModel;
import com.example.template.model.db.listener.IDataHelper;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static com.example.template.model.db.Constants.DBMethodGetAll;
import static com.example.template.model.db.Constants.DBMethodGetById;

/**
 * Created by janisharali on 08/12/16.
 */

public class DbHelper
        implements IDataHelper<Object> {

    private static DbHelper INSTANCE = null;
    Context dbContext;

    private DbHelper(Context context) {
        dbContext = context;
    }


    // Prevent direct instantiation.
    public static DbHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DbHelper(context);
        }
        return INSTANCE;
    }

    public void clearCachedData() {

        SQLite.delete().from(CacheApi.class)
                .async()
                .queryResultCallback(new QueryTransaction.QueryResultCallback<CacheApi>() {
                    @Override
                    public void onQueryResult(QueryTransaction<CacheApi> transaction,
                                              @NonNull CursorResult<CacheApi> tResult) {

                    }
                }).execute();

//        new Delete().from(CacheApi.class).executeUpdateDelete();
        Delete.table(CacheApi.class);

//        SQLite.delete(CacheApi.class)
////                .where(CacheApi_Table.id.eq(someModel.getId()))
//                .executeUpdateDelete();
    }


    //Sqlite Dbflow notes not used functions as we choosed to  access notes table from notes presenter
    //of course we can use these methods from here

    public void getNotes(SqliteCallBack callback) {
        getAll(new Note(), dbContext, callback);
    }

    public void addNote(String noteText, SqliteCallBack callback)

    {
        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());
        Note note = new Note();
        note.setText(noteText);
        note.setComment(comment);
        note.setDate(new Date());
        note.setType(NoteType.TEXT);
        note.insertData(note, dbContext, callback);
        Log.e("dbflowExample Note", "Inserted new note, ID: " + note.getId());
    }

    public void deleteNote(Note note, SqliteCallBack callback) {
        deleteData(note, dbContext, callback);
    }

    //Sqlite Dbflow simple items
    public long getSimpleItemsCount(SqliteCallBack callback) {
        long count = SimpleItem.getCount();
        return count;
    }

    public void addSimpleItem(SqliteCallBack callback) {

        SimpleItem simpleItem = new SimpleItem();
        simpleItem.name = "simple item test name";
        simpleItem.insertData(simpleItem, dbContext, callback);
    }


    ///Sqlite Dbflow   complex dbflow operations


    public void getCategoryById(long category_id, final SqliteCallBack<Object> mSqliteCallBack) {

        SQLite.select().from(Categories.class)
                .where(Categories_Table.id.eq(category_id))
                .async()
                .queryResultCallback(
                        new QueryTransaction.QueryResultCallback<Categories>() {
                            @Override
                            public void onQueryResult(final QueryTransaction<Categories> transaction, @NonNull final CursorResult<Categories> tResult) {
                                mSqliteCallBack.onDBDataObjectLoaded((
                                                tResult.toModelClose()), DBMethodGetById
                                        , "getCategoryById");
                            }
                        }
                ).execute();

    }

    public void getItemById(long item_id, final SqliteCallBack<Object> mSqliteCallBack) {
        SQLite.select().from(Items.class)
                .where(Items_Table.id.eq(item_id))
                .async()
                .queryResultCallback(
                        new QueryTransaction.QueryResultCallback<Items>() {
                            @Override
                            public void onQueryResult(final QueryTransaction<Items> transaction, @NonNull final CursorResult<Items> tResult) {
                                mSqliteCallBack.onDBDataObjectLoaded(
                                        tResult.toModelClose(), DBMethodGetById
                                        , "getItemById");
                            }
                        }
                ).execute();
    }

    public void addCategory(String name, SqliteCallBack<Object> mSqliteCallBack) {
        Categories category = new Categories();
        category.name = name;
        category.insertData(category, dbContext, mSqliteCallBack);
    }

    public void editCategory(String name, long category_id, SqliteCallBack<Object> mSqliteCallBack) {
        Categories category = new Categories();
        category.id = category_id;
        category.name = name;
        category.updateData(category, dbContext, mSqliteCallBack);
    }

    public void deleteCategory(long category_id, SqliteCallBack<Object> mSqliteCallBack) {
        Categories category = new Categories();
        category.id = category_id;
        category.deleteData(category, dbContext, mSqliteCallBack);
    }

    public void addItem(String name, String description, long category_id, SqliteCallBack<Object> mSqliteCallBack) {
        Items item = new Items();
        item.name = name;
        item.description = description;
        item.category_id = category_id;
        item.insertData(item, dbContext, mSqliteCallBack);
    }

    public void editItem(long item_id, String name, String description, long category_id, SqliteCallBack<Object> mSqliteCallBack) {
        Items item = new Items();
        item.id = item_id;
        item.name = name;
        item.description = description;
        item.category_id = category_id;
        item.updateData(item, dbContext, mSqliteCallBack);
    }

    public void deleteItem(long item_id, SqliteCallBack<Object> mSqliteCallBack) {
        Items item = new Items();
        item.id = item_id;
        item.deleteData(item, dbContext, mSqliteCallBack);
    }


    public void getCategoriesWithItems(SqliteCallBack callback) {

        getAll(new Categories(), dbContext, callback);
    }

    public void getCategoriesWithoutItems(final SqliteCallBack sqliteCallBack) {

        //synch
        List<CategoriesWithOutItemsQueryModel> categoriesWithOutItemsQueryModels = SQLite.select(
                Categories_Table.id,
                Categories_Table.name)
                .from(Categories.class)
                .queryCustomList(CategoriesWithOutItemsQueryModel.class);

        sqliteCallBack.onDBDataListLoaded(categoriesWithOutItemsQueryModels, DBMethodGetAll
                , "getCategoriesWithoutItems");


        //asynch
//        FlowManager.getDatabaseForTable(CategoriesWithOutItemsQueryModel.class)
//                .beginTransactionAsync(new ITransaction() {
//                    @Override
//                    public void execute(DatabaseWrapper databaseWrapper) {
//
//                                List<CategoriesWithOutItemsQueryModel> categoriesWithOutItemsQueryModelsAsynch  =  SQLite.select(
//                Categories_Table.id,
//                Categories_Table.name)
//                .from(Categories.class)
//                .queryCustomList(CategoriesWithOutItemsQueryModel.class);
//
//                        //because of this we make runonuithread
//                        sqliteCallBack.onDBDataListLoaded( categoriesWithOutItemsQueryModelsAsynch
//                                ,DBMethodGetAll
//                                , "getCategoriesWithoutItems"
//                        );
//                    }
//                }).execute();

    }


    public void getItems(SqliteCallBack callback) {

        getAll(new Items(), dbContext, callback);
    }

    public void getItemsByCategoryId(long category_id, final SqliteCallBack callback) {

        //synch
//        List<Items> items = SQLite.select()
//                .from(Items.class)
//                .where(Items_Table.category_id.eq(category_id))
//                .queryList();

//        callback.onDBDataListLoaded(items,DBMethodGetAll);

        //asynch
        SQLite.select().from(Items.class)
                .where(Items_Table.category_id.eq(category_id))
                .async()
                .queryResultCallback(
                        new QueryTransaction.QueryResultCallback<Items>() {
                            @Override
                            public void onQueryResult(final QueryTransaction<Items> transaction, @NonNull final CursorResult<Items> tResult) {
                                callback.onDBDataListLoaded(new ArrayList<Items>(
                                                tResult.toListClose()), DBMethodGetAll
                                        , "getItemsByCategoryId");
                            }
                        }
                ).execute();
    }

    public void getItemsByCategoriesIds(ArrayList<Long> ids, final SqliteCallBack<Object> mSqliteCallBack) {


        //synch
//        List<Items> items =  SQLite.select().from(Items.class)
//                .where(Items_Table.category_id
//                        .in(ids)
//                )
//                .queryList();
//        mSqliteCallBack.onDBDataListLoaded( items,DBMethodGetAll);

        //asynch


        SQLite.select().from(Items.class)
                .where(Items_Table.category_id
                        .in(ids)
                )
                .async()
                .queryResultCallback(
                        new QueryTransaction.QueryResultCallback<Items>() {
                            @Override
                            public void onQueryResult(final QueryTransaction<Items> transaction, @NonNull final CursorResult<Items> tResult) {
                                mSqliteCallBack.onDBDataListLoaded(tResult.toListClose(),
                                        DBMethodGetAll
                                        , "getItemsByCategoriesIds");
                            }
                        }
                ).execute();


    }

    public void getCategoriesWhereIdInIds(ArrayList<Long> ids, final SqliteCallBack sqliteCallBack) {


        //synch
//        List<Categories> categories  =  SQLite.select().from(Categories.class)
//                .where(Categories_Table.id
//                        .in( ids ))
//                .queryList();
//
//        sqliteCallBack.onDBDataListLoaded(categories,DBMethodGetAll);

        //asynch

        SQLite.select().from(Categories.class)
                .where(Categories_Table.id
                        .in(ids))
                .async()
                .queryResultCallback(
                        new QueryTransaction.QueryResultCallback<Categories>() {
                            @Override
                            public void onQueryResult(final QueryTransaction<Categories> transaction, @NonNull final CursorResult<Categories> tResult) {
                                sqliteCallBack.onDBDataListLoaded(
                                        tResult.toListClose(), DBMethodGetAll
                                        , "getCategoriesWhereIdInIds");
                            }
                        }
                ).execute();


    }

    public void getItemsWhereIdInCategoriesIds(ArrayList<Long> ids, final SqliteCallBack sqliteCallBack) {


        //synch
//        List<Items> items =    SQLite.select().from(Items.class)
//                .where(Items_Table.category_id
//                        .in(SQLite.select(Categories_Table.id).from(Categories.class)
////                                        .where(Categories_Table.name.eq(""))
////                                        .and(Items_Table.name.eq(""))//                                          .where(OperatorGroup.clause()
////                                        .and(Categories_Table.name.eq(""))
////                                           )
////                                .where(OperatorGroup.clause()
////                                        .and(Categories_Table.name.withTable().eq(toUserId))
////                                        .or(Categories_Table.toUserId.withTable().eq(toUserId)))
//                                .orderBy(Items_Table.id, false)
//                                 ))
//                .queryList();


//        List<Items> items =    SQLite.select().from(Items.class)
//                .where(Items_Table.category_id
//                        .in(ids ))
//                .queryList();

//        sqliteCallBack.onDBDataListLoaded(items,DBMethodGetAll);

        //asynch

//        SQLite.select().from(Items.class)
//                .where(Items_Table.category_id
//                        .in(SQLite.select(Categories_Table.id).from(Categories.class)
//                                .orderBy(Items_Table.id, false)
//                        ))
//                .async()
//                .queryResultCallback(
//                        new QueryTransaction.QueryResultCallback<Items>() {
//                            @Override
//                            public void onQueryResult(final QueryTransaction<Items> transaction, @NonNull final CursorResult<Items> tResult) {
//                                sqliteCallBack.onDBDataListLoaded( new ArrayList<Items>(tResult.toListClose()),DBMethodGetAll);
//
//                            }
//                        }
//                ).execute();


        SQLite.select().from(Items.class)
                .where(Items_Table.category_id
                        .in(ids)
                )
                .async()
                .queryResultCallback(
                        new QueryTransaction.QueryResultCallback<Items>() {
                            @Override
                            public void onQueryResult(final QueryTransaction<Items> transaction, @NonNull final CursorResult<Items> tResult) {
                                sqliteCallBack.onDBDataListLoaded(
                                        new ArrayList<Items>(tResult.toListClose()),
                                        DBMethodGetAll
                                        , "getItemsWhereIdInIds");

                            }
                        }
                ).execute();

    }

    public void getCustomListCategoriesItemsJoin(final SqliteCallBack sqliteCallBack) {

        //synch

        List<CategoriesItemsQueryModel> categoriesItemsQueryModels = SQLite.select(
                Categories_Table.name.withTable().as("category_name"),
                Items_Table.name.withTable().as("item_name"),
                Categories_Table.id.withTable().as("category_id"),
                Items_Table.id.withTable().as("item_id"))
                .from(Categories.class)
                .leftOuterJoin(Items.class)
                .on(Categories_Table.id.withTable().eq(Items_Table.category_id.withTable()))
                .queryCustomList(CategoriesItemsQueryModel.class);

        sqliteCallBack.onDBDataListLoaded(categoriesItemsQueryModels, DBMethodGetAll
                , "getCustomListCategoriesItemsJoin");


        //asynch
//        FlowManager.getDatabaseForTable(CategoriesItemsQueryModel.class)
//                .beginTransactionAsync(new ITransaction() {
//                    @Override
//                    public void execute(DatabaseWrapper databaseWrapper) {
//
//
//        List<CategoriesItemsQueryModel> categoriesItemsQueryModelsAsynch  =  SQLite.select(
//                Categories_Table.name.withTable().as("category_name"),
//                Items_Table.name.withTable().as("item_name"),
//                Categories_Table.id.withTable().as("category_id"),
//                Items_Table.id.withTable().as("item_id"))
//                .from(Categories.class)
//                .leftOuterJoin(Items.class)
//                .on(Categories_Table.id.withTable().eq(Items_Table.category_id.withTable()))
//                .queryCustomList(CategoriesItemsQueryModel.class);
//
//
//        //because of this we make runonuithread
//           sqliteCallBack.onDBDataListLoaded( categoriesItemsQueryModelsAsynch,DBMethodGetAll
//                   , "getCustomListCategoriesItemsJoin"
//           );
//                    }
//                }).execute();

    }

    @Override
    public void insertData(Object o, Context context, SqliteCallBack sqliteCallBack) {
        ((IDataHelper) o).insertData(o, context, sqliteCallBack);
    }

    @Override
    public void updateData(Object o, Context context, SqliteCallBack sqliteCallBack) {
        ((IDataHelper) o).updateData(o, context, sqliteCallBack);
    }

    @Override
    public void deleteData(Object o, Context context, SqliteCallBack sqliteCallBack) {
        ((IDataHelper) o).deleteData(o, context, sqliteCallBack);
    }

    @Override
    public Object getDataByID(Object o, Context context, SqliteCallBack sqliteCallBack) {
        return ((IDataHelper) o).getDataByID(o, context, sqliteCallBack);
    }

    @Override
    public void getAll(Object o, Context context, SqliteCallBack sqliteCallBack) {
        ((IDataHelper) o).getAll(o, context, sqliteCallBack);
    }
}
