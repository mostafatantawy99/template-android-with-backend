package com.example.template.ui.TestSqliteDbflow.CategoriesAndItems.ComplexOperations.presenter;

import android.content.Context;

import com.example.template.model.DataManager;
import com.example.template.model.bean.sqlite.Categories;
import com.example.template.model.bean.sqlite.Items;
import com.example.template.model.bean.sqlite.querymodels.CategoriesItemsQueryModel;
import com.example.template.model.bean.sqlite.querymodels.CategoriesWithOutItemsQueryModel;
import com.example.template.model.db.SqliteCallBack;

import java.util.ArrayList;
import java.util.List;

import static com.example.template.model.db.Constants.DBMethodGetAll;

/**
 * Created by Net22 on 9/13/2017.
 */

public class ComplexOperationsPresenter
        implements ComplexOperationsContract.IPresenter, SqliteCallBack {

    Context mContext;
    ComplexOperationsContract.IView mView;
    private DataManager mDataManager;

    public ComplexOperationsPresenter(Context context, ComplexOperationsContract.IView view) {
        mView = view;
        mContext = context;
        mDataManager = DataManager.getInstance(mContext
        );
        mDataManager.setPresenterSqliteCallBack(this);

    }

    @Override
    public void onGetCategoriesWithoutItems() {
        mDataManager.getLocalCategoriesWithoutItems();

    }

    @Override
    public void onGetCategoriesWithItems() {
        mDataManager.getLocalCategoriesWithItems();
    }

    @Override
    public void onGetItemsByCategoryId(long category_id) {
        mDataManager.getLocalItemsByCategoryId(category_id);
    }

    @Override
    public void onGetItemsByCategoriesIds(ArrayList<Long> ids) {
        mDataManager.getLocalItemsByCategoriesIds(ids);
    }

    @Override
    public void onGetCategoriesWhereIdInIds(ArrayList<Long> ids) {
        mDataManager.getLocalCategoriesWhereIdInIds(ids);
    }

    @Override
    public void onGetItemsWhereIdInCategoriesIds(ArrayList<Long> ids) {
        mDataManager.getLocalItemsWhereIdInCategoriesIds(ids);
    }

    @Override
    public void onGetCustomListCategoriesItemsJoin() {
        mDataManager.getLocalCustomListCategoriesItemsJoin();

    }


    public void showResult(String result) {
        //mView.showResult(result);
    }


    @Override
    public void createView() {

    }

    @Override
    public void destroyView() {

    }


    @Override
    public void onDBDataListLoaded(List data, String methodName, String localDbOperation) {

        if (data.size() > 0) {
            if ((methodName.equals(DBMethodGetAll)) && (data.get(0) instanceof Categories)) {
                mView.showResult(data, Categories.class.getName());
            } else if ((methodName.equals(DBMethodGetAll)) && (data.get(0) instanceof Items)) {
//                for (int i = 0; i <data.size() ; i++) {
//
//                }
//                GeneralUtil.convertToClazz(data,Object.class,Items.class);
                mView.showResult(data, Items.class.getName());
            } else if ((methodName.equals(DBMethodGetAll)) && (data.get(0) instanceof CategoriesItemsQueryModel)) {
                mView.showResult(data, CategoriesItemsQueryModel.class.getName());
            } else if ((methodName.equals(DBMethodGetAll)) && (data.get(0) instanceof CategoriesWithOutItemsQueryModel)) {
                mView.showResult(data, CategoriesWithOutItemsQueryModel.class.getName());
            }
        } else {
            mView.showResult("empty");
        }


    }

    @Override
    public void onDBDataObjectLoaded(Object data, String methodName, String localDbOperation) {

    }
}
