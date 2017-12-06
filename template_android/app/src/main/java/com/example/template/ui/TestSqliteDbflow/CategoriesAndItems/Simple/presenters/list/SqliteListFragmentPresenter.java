package com.example.template.ui.TestSqliteDbflow.CategoriesAndItems.Simple.presenters.list;

import android.content.Context;

import com.example.template.model.DataManager;
import com.example.template.model.bean.sqlite.Items;
import com.example.template.model.bean.sqlite.querymodels.CategoriesWithOutItemsQueryModel;
import com.example.template.model.db.SqliteCallBack;
import com.example.template.ui.TestSqliteDbflow.CategoriesAndItems.Simple.fragments.SqliteListFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.template.utils.Constants.SqliteSourceCategories;
import static com.example.template.utils.Constants.SqliteSourceItems;

/**
 * Created by Net22 on 11/26/2017.
 */

public class SqliteListFragmentPresenter implements ISqliteListFragmentContract.ISqliteListFragmentPresenter
        , SqliteCallBack {
    Context mContext;
    ISqliteListFragmentContract.ISqliteListFragmentView mView;
    private DataManager mDataManager;

    public SqliteListFragmentPresenter(Context context, ISqliteListFragmentContract.ISqliteListFragmentView view) {
        mView = view;
        mContext = context;
        mDataManager = DataManager.getInstance(mContext);
        mDataManager.setPresenterSqliteCallBack(this);
    }


    @Override
    public void loadList(String source) {

        mView.showProgress();
        if (source.equals(SqliteSourceCategories)) {
            mDataManager.getLocalCategoriesWithoutItems();
        } else if (source.equals(SqliteSourceItems)) {
            mDataManager.getLocalItems();
        }

    }

    @Override
    public void loadItemsByCategory(long category_id) {

        mView.showProgress();
        mDataManager.getLocalItemsByCategoryId(category_id);
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
        if (localDbOperation.equals("getCategoriesWithoutItems")) {
            if (((SqliteListFragment) mView).getSourceFragment().equals(SqliteSourceCategories)) {
                ((SqliteListFragment) mView).showList(new ArrayList<CategoriesWithOutItemsQueryModel>(data));
            } else if (((SqliteListFragment) mView).getSourceFragment().equals(SqliteSourceItems)) {
                ((SqliteListFragment) mView).showSpCategories(new ArrayList<CategoriesWithOutItemsQueryModel>(data));
            }

        } else if (localDbOperation.equals("getAllItems")) {
            ((SqliteListFragment) mView).showList(new ArrayList<Items>(data));
        } else if (localDbOperation.equals("getItemsByCategoryId")) {
            ((SqliteListFragment) mView).showList(new ArrayList<Items>(data));
        }


    }

    @Override
    public void onDBDataObjectLoaded(Object data, String methodName, String localDbOperation) {

    }
}
