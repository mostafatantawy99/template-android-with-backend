package com.example.template.model.bean.sqlite;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.template.model.db.SqliteCallBack;
import com.example.template.model.db.dbFlowDatabases.DatabaseModule;
import com.example.template.model.db.listener.IDataHelper;
import com.example.template.utils.GeneralUtil;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;
import java.util.List;

import static com.example.template.model.db.Constants.DBMethodGetAll;

/**
 * Created by Net22 on 11/19/2017.
 */

@Table(database = DatabaseModule.class)
public class Categories extends BaseModel implements IDataHelper<Categories> {

    public Categories() {

    }

    public Categories(long id, String name) {
        this.id = id;
        this.name = name;
    }


    @Override
    public String toString() {

        String itemsArrayListStr = "";
        if (itemsArrayList != null) {
            itemsArrayListStr = GeneralUtil.convertArrToStr(itemsArrayList);
        }

        return "Categories{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", itemsArrayList=" + itemsArrayListStr +
                '}';

    }

    public String toStringAll() {

        String itemsArrayListStr = "";
        if (itemsArrayList != null) {
            itemsArrayListStr = GeneralUtil.convertArrToStr(itemsArrayList);
        } else {

        }

        return "Categories{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", itemsArrayList=" + itemsArrayListStr +
                '}';
//
//        return "Categories{" +
//                "id=" + id +
//                ", name='" + name +
//                '}';
    }

    public String toStringWithoutItems() {

        return "Categories{" +
                "id=" + id +
                ", name='" + name +
                '}';
    }

    @Column
    @PrimaryKey(autoincrement = true)
    public long id = 0l;
    ;

    @Column
    public String name;

    public List<Items> itemsArrayList;


    public Categories(long id, String name, List<Items> itemsArrayList) {
        this.id = id;
        this.name = name;
        this.itemsArrayList = itemsArrayList;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Items> getItemsArrayList() {
        return itemsArrayList;
    }

    public void setItemsArrayList(List<Items> itemsArrayList) {
        this.itemsArrayList = itemsArrayList;
    }


    @OneToMany(methods = OneToMany.Method.ALL, variableName = "itemsArrayList")
    public List<Items> dbFlowOneTwoManyUtilMethod() {
        if (itemsArrayList == null) {
            itemsArrayList = SQLite.select()
                    .from(Items.class)
                    .where(Items_Table.category_id.eq(id))
                    .queryList();
        }
        return itemsArrayList;
    }

    @Override
    public void insertData(Categories categories, Context context, SqliteCallBack sqliteCallBack) {
        categories.insert();
        sqliteCallBack.onDBDataObjectLoaded(null, null, "insertCategory");
    }

    @Override
    public void updateData(Categories categories, Context context, SqliteCallBack sqliteCallBack) {
        categories.save();
        sqliteCallBack.onDBDataObjectLoaded(null, null, "updateCategory");

    }

    @Override
    public void deleteData(Categories categories, Context context, SqliteCallBack sqliteCallBack) {
        categories.delete();
        sqliteCallBack.onDBDataObjectLoaded(null, null, "deleteCategory");

    }

    @Override
    public Categories getDataByID(Categories categories, Context context, SqliteCallBack sqliteCallBack) {
        Categories categories1 = SQLite.select()
                .from(Categories.class)
                .where(Categories_Table.id.eq(id))
                .querySingle();

        return categories1;
    }

    @Override
    public void getAll(Categories categories, Context context, final SqliteCallBack sqliteCallBack) {

        //synch
//                List<Categories> categories1 = SQLite.select()
//                .from(Categories.class)
//                 .queryList();
//        sqliteCallBack.onDBDataListLoaded(categories1,DBMethodGetAll);

        SQLite.select().from(Categories.class)
                .async()
                .queryResultCallback(
                        new QueryTransaction.QueryResultCallback<Categories>() {
                            @Override
                            public void onQueryResult(final QueryTransaction<Categories> transaction, @NonNull final CursorResult<Categories> tResult) {
                                sqliteCallBack.onDBDataListLoaded(
                                        new ArrayList<Categories>(tResult.toListClose()),
                                        DBMethodGetAll
                                        , "getAllCategories"
                                );
                            }
                        }
                ).execute();
    }
}
