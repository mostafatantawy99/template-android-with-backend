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
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;

import static com.example.template.model.db.Constants.DBMethodGetAll;

/**
 * Created by Net22 on 11/19/2017.
 */

@Table(database = DatabaseModule.class)
public class Items extends BaseModel implements IDataHelper<Items> {

    public Items() {

    }

    @Override
    public String toString() {
        return "Items{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category_id=" + category_id +
                '}';
    }

    @Column
    @PrimaryKey(autoincrement = true)
    public long id = 0l;

    @Column
    public String name;

    @Column
    public String description;

    @Column
    public long category_id = 0l;

    public Items(long id, String name, String description, long category_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category_id = category_id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    @Override
    public void insertData(Items items, Context context, SqliteCallBack sqliteCallBack) {
        items.insert();
        sqliteCallBack.onDBDataObjectLoaded(null, null, "insertItem");
    }

    @Override
    public void updateData(Items items, Context context, SqliteCallBack sqliteCallBack) {
        items.save();
        sqliteCallBack.onDBDataObjectLoaded(null, null, "updateItem");

    }

    @Override
    public void deleteData(Items items, Context context, SqliteCallBack sqliteCallBack) {
        items.delete();
        sqliteCallBack.onDBDataObjectLoaded(null, null, "deleteItem");

    }

    @Override
    public Items getDataByID(Items items, Context context, SqliteCallBack sqliteCallBack) {
        Items items1 = SQLite.select()
                .from(Items.class)
                .where(Items_Table.id.eq(id))
                .querySingle();

        return items1;

    }

    @Override
    public void getAll(Items items, Context context, final SqliteCallBack sqliteCallBack) {

        SQLite.select().from(Items.class)
                .async()
                .queryResultCallback(
                        new QueryTransaction.QueryResultCallback<Items>() {
                            @Override
                            public void onQueryResult(final QueryTransaction<Items> transaction, @NonNull final CursorResult<Items> tResult) {
                                sqliteCallBack.onDBDataListLoaded(new ArrayList<Items>(tResult.toListClose()), DBMethodGetAll
                                        , "getAllItems"
                                );
                            }
                        }
                ).execute();
    }
}
