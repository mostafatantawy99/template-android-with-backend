package com.example.template.ui.TestSqliteDbflow.CategoriesAndItems.Simple.presenters.single;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.example.template.model.DataManager;
import com.example.template.model.bean.sqlite.Categories;
import com.example.template.model.bean.sqlite.Items;
import com.example.template.model.db.SqliteCallBack;
import com.example.template.ui.TestSqliteDbflow.CategoriesAndItems.Simple.fragments.SqliteSingleFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.template.utils.Constants.SqliteSourceCategories;
import static com.example.template.utils.Constants.SqliteSourceItems;

/**
 * Created by Net22 on 11/26/2017.
 */

public class SqliteSingleFragmentPresenter implements ISqliteSingleFragmentContract.ISqliteSingleFragmentPresenter
        , SqliteCallBack {
    Context mContext;
    ISqliteSingleFragmentContract.ISqliteSingleFragmentView mView;
    private DataManager mDataManager;

    public SqliteSingleFragmentPresenter(Context context, ISqliteSingleFragmentContract.ISqliteSingleFragmentView view) {
        mView = view;
        mContext = context;
        mDataManager = DataManager.getInstance(mContext);
        mDataManager.setPresenterSqliteCallBack(this);
        mDataManager.setPresenterSqliteCallBack(this);
    }


    @Override
    public void loadElementById(String sourceFragment, long elementId) {

        mView.showProgress();
        if (sourceFragment.equals(SqliteSourceCategories)) {
            mDataManager.getLocalCategoryById(elementId);
        } else if (sourceFragment.equals(SqliteSourceItems)) {
            mDataManager.getLocalItemById(elementId);
        }

    }

    @Override
    public void loadCategories() {
        mView.showProgress();
        mDataManager.getLocalCategoriesWithoutItems();
    }

    @Override
    public void addCategory(Categories categorySqlite) {

        mView.showProgress();
        mDataManager.addLocalCategory(categorySqlite.getName());
    }

    @Override
    public void editCategory(Categories categorySqlite) {
        mView.showProgress();
        mDataManager.editLocalCategory(categorySqlite.getName(), categorySqlite.getId());
    }

    @Override
    public void deleteCategory(Categories categorySqlite) {
        mView.showProgress();
        mDataManager.deleteLocalCategory(categorySqlite.getId());

    }

    @Override
    public void addItem(Items itemSqlite) {
        mView.showProgress();
        mDataManager.addLocalItem(itemSqlite.getName(), itemSqlite.getDescription(), itemSqlite.getCategory_id());

    }

    @Override
    public void editItem(Items itemSqlite) {
        mView.showProgress();
        mDataManager.editLocalItem(itemSqlite.getId(), itemSqlite.getName(), itemSqlite.getDescription(), itemSqlite.getCategory_id());
    }

    @Override
    public void deleteItem(Items itemSqlite) {
        mView.showProgress();
        mDataManager.deleteLocalItem(itemSqlite.getId());
    }


    @Override
    public void createView() {

    }

    @Override
    public void destroyView() {

    }


    @Override
    public void onDBDataListLoaded(List data, String methodName, String localDbOperation) {
        mView.hideProgress();

        ((SqliteSingleFragment) mView).showSpCategories(new ArrayList<Categories>(data));

    }

    @Override
    public void onDBDataObjectLoaded(Object data, String methodName, String localDbOperation) {

        mView.hideProgress();
        if (localDbOperation.equals("getCategoryById")) {
            ((SqliteSingleFragment) mView).showCategory(((Categories) data));
        } else if (localDbOperation.equals("getItemById")) {
            ((SqliteSingleFragment) mView).showItem(((Items) data));
        } else if (localDbOperation.equals("insertCategory")) {
            ((SqliteSingleFragment) mView).showSuccessSqliteResponse();
        } else if (localDbOperation.equals("updateCategory")) {
            ((SqliteSingleFragment) mView).showSuccessSqliteResponse();
        } else if (localDbOperation.equals("deleteCategory")) {
            ((SqliteSingleFragment) mView).showSuccessSqliteResponse();
            ((AppCompatActivity) (mContext)).finish();
        } else if (localDbOperation.equals("insertItem")) {
            ((SqliteSingleFragment) mView).showSuccessSqliteResponse();
        } else if (localDbOperation.equals("updateItem")) {
            ((SqliteSingleFragment) mView).showSuccessSqliteResponse();
        } else if (localDbOperation.equals("deleteItem")) {
            ((SqliteSingleFragment) mView).showSuccessSqliteResponse();
            ((AppCompatActivity) (mContext)).finish();
        }

    }
}