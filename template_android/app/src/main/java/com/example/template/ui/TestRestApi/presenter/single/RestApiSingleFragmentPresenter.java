package com.example.template.ui.TestRestApi.presenter.single;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.example.template.model.DataManager;
import com.example.template.model.backend.RestApiCallBack;
import com.example.template.model.bean.CategoriesRestApiResponse;
import com.example.template.model.bean.CategoryRestApi;
import com.example.template.model.bean.CategoryRestApiResponse;
import com.example.template.model.bean.EditRestApiResponse;
import com.example.template.model.bean.ItemRestApi;
import com.example.template.model.bean.ItemRestApiResponse;
import com.example.template.model.bean.sqlite.CacheApi;
import com.example.template.model.db.SqliteCallBack;
import com.example.template.ui.TestRestApi.fragments.RestApiSingleFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.template.model.db.Constants.DBMethodGetById;
import static com.example.template.utils.Constants.RestApiSourceCategories;
import static com.example.template.utils.Constants.RestApiSourceItems;
import static com.example.template.utils.Constants.addCategory;
import static com.example.template.utils.Constants.addItem;
import static com.example.template.utils.Constants.deleteCategory;
import static com.example.template.utils.Constants.deleteItem;
import static com.example.template.utils.Constants.editCategory;
import static com.example.template.utils.Constants.editItem;
import static com.example.template.utils.Constants.getAllCategoriesWithoutItems;
import static com.example.template.utils.Constants.getCategoryById;
import static com.example.template.utils.Constants.getItemById;

/**
 * Created by Net22 on 11/26/2017.
 */

public class RestApiSingleFragmentPresenter implements IRestApiSingleFragmentContract.IRestApiSingleFragmentPresenter
        , RestApiCallBack, SqliteCallBack {
    Context mContext;
    IRestApiSingleFragmentContract.IRestApiSingleFragmentView mView;
    private DataManager mDataManager;

    public RestApiSingleFragmentPresenter(Context context, IRestApiSingleFragmentContract.IRestApiSingleFragmentView view) {
        mView = view;
        mContext = context;
        mDataManager = DataManager.getInstance(mContext);
        mDataManager.setPresenterRestApiCallBack(this);
        mDataManager.setPresenterSqliteCallBack(this);
    }


    @Override
    public void loadElementById(String sourceFragment, long elementId) {

        mView.showProgress();
        if (sourceFragment.equals(RestApiSourceCategories)) {
            mDataManager.getRemoteCategoryById(elementId);
        } else if (sourceFragment.equals(RestApiSourceItems)) {
            mDataManager.getRemoteItemById(elementId);
        }

    }

    @Override
    public void loadCategories() {
        mView.showProgress();
        mDataManager.getRemoteCategories();
    }

    @Override
    public void addCategory(CategoryRestApi categoryRestApi) {

        mView.showProgress();
        mDataManager.addRemoteCategory(categoryRestApi.getName());
    }

    @Override
    public void editCategory(CategoryRestApi categoryRestApi) {
        mView.showProgress();
        mDataManager.editRemoteCategory(categoryRestApi.getName(), categoryRestApi.getId());
    }

    @Override
    public void deleteCategory(CategoryRestApi categoryRestApi) {
        mView.showProgress();
        mDataManager.deleteRemoteCategory(categoryRestApi.getId());

    }

    @Override
    public void addItem(ItemRestApi itemRestApi) {
        mView.showProgress();
        mDataManager.addRemoteItem(itemRestApi.getName(), itemRestApi.getDescription(), itemRestApi.getCategory_id());

    }

    @Override
    public void editItem(ItemRestApi itemRestApi) {
        mView.showProgress();
        mDataManager.editRemoteItem(itemRestApi.getId(), itemRestApi.getName(), itemRestApi.getDescription(), itemRestApi.getCategory_id());
    }

    @Override
    public void deleteItem(ItemRestApi itemRestApi) {
        mView.showProgress();
        mDataManager.deleteRemoteItem(itemRestApi.getId());
    }

    @Override
    public void onDataListLoaded(ArrayList data, String url) {

    }

    @Override
    public void onDataObjectLoaded(Object data, String url) {

        mView.hideProgress();
        if (url.equals(getAllCategoriesWithoutItems)) {

            if (((RestApiSingleFragment) mView).getSourceFragment().equals(RestApiSourceItems)) {
                ((RestApiSingleFragment) mView).showSpCategories(((CategoriesRestApiResponse) data).getResults());
            }

        } else if (url.equals(getCategoryById)) {
            ((RestApiSingleFragment) mView).showCategory(((CategoryRestApiResponse) data));
        } else if (url.equals(getItemById)) {
            ((RestApiSingleFragment) mView).showItem(((ItemRestApiResponse) data));
        } else if (url.equals(addCategory)) {
            ((RestApiSingleFragment) mView).showEditRestApiResponse(((EditRestApiResponse) data));
        } else if (url.equals(editCategory)) {
            ((RestApiSingleFragment) mView).showEditRestApiResponse(((EditRestApiResponse) data));
        } else if (url.equals(deleteCategory)) {
            ((RestApiSingleFragment) mView).showEditRestApiResponse(((EditRestApiResponse) data));
            ((AppCompatActivity) (mContext)).finish();
        } else if (url.equals(addItem)) {
            ((RestApiSingleFragment) mView).showEditRestApiResponse(((EditRestApiResponse) data));
        } else if (url.equals(editItem)) {
            ((RestApiSingleFragment) mView).showEditRestApiResponse(((EditRestApiResponse) data));
        } else if (url.equals(deleteItem)) {
            ((RestApiSingleFragment) mView).showEditRestApiResponse(((EditRestApiResponse) data));
            ((AppCompatActivity) (mContext)).finish();
        }


    }

    @Override
    public void onNetworkError(String message, String url) {

    }

    @Override
    public void onNoInternet() {

        mView.hideProgress();
        //Toast.makeText(mContext, mContext.getString(R.string.noInternetConnection), Toast.LENGTH_LONG).show();

    }

    @Override
    public void createView() {

    }

    @Override
    public void destroyView() {

    }


    @Override
    public void onDBDataListLoaded(List data, String methodName, String localDbOperation) {

    }

    @Override
    public void onDBDataObjectLoaded(Object data, String methodName, String localDbOperation) {

        mView.hideProgress();
        if (methodName.equals(DBMethodGetById)) {
            CacheApi cacheApi = ((CacheApi) data);

            if (cacheApi != null) {
                if (cacheApi.getUrl().equals(getAllCategoriesWithoutItems)) {
                    CategoriesRestApiResponse categoriesRestApiResponse = (CategoriesRestApiResponse) mDataManager.loadCacheData(cacheApi);

                    if (categoriesRestApiResponse != null) {
                        ((RestApiSingleFragment) mView).showSpCategories(
                                categoriesRestApiResponse.getResults());
                    }

                } else if (cacheApi.getUrl().equals(getCategoryById)) {
                    CategoryRestApiResponse categoryRestApiResponse = (CategoryRestApiResponse) mDataManager.loadCacheData(cacheApi);
                    if (categoryRestApiResponse != null) {
                        ((RestApiSingleFragment) mView).showCategory(
                                categoryRestApiResponse);
                    }
                } else if (cacheApi.getUrl().equals(getItemById)) {
                    ItemRestApiResponse itemRestApiResponse = (ItemRestApiResponse) mDataManager.loadCacheData(cacheApi);
                    if (itemRestApiResponse != null) {
                        ((RestApiSingleFragment) mView).showItem(
                                itemRestApiResponse);
                    }
                }
            }
        }
    }
}