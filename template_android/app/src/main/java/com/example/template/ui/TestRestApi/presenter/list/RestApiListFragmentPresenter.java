package com.example.template.ui.TestRestApi.presenter.list;

import android.content.Context;

import com.example.template.model.DataManager;
import com.example.template.model.backend.RestApiCallBack;
import com.example.template.model.bean.CategoriesRestApiResponse;
import com.example.template.model.bean.ItemsRestApiResponse;
import com.example.template.model.bean.sqlite.CacheApi;
import com.example.template.model.db.SqliteCallBack;
import com.example.template.ui.TestRestApi.fragments.RestApiListFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.template.model.db.Constants.DBMethodGetById;
import static com.example.template.utils.Constants.RestApiSourceCategories;
import static com.example.template.utils.Constants.RestApiSourceItems;
import static com.example.template.utils.Constants.getAllCategoriesWithoutItems;
import static com.example.template.utils.Constants.getAllItemsByCategory;
import static com.example.template.utils.Constants.getAllItemsOrderedByCategory;

/**
 * Created by Net22 on 11/26/2017.
 */

public class RestApiListFragmentPresenter implements IRestApiListFragmentContract.IRestApiListFragmentPresenter
        , RestApiCallBack, SqliteCallBack {
    Context mContext;
    IRestApiListFragmentContract.IRestApiListFragmentView mView;
    private DataManager mDataManager;

    public RestApiListFragmentPresenter(Context context, IRestApiListFragmentContract.IRestApiListFragmentView view) {
        mView = view;
        mContext = context;
        mDataManager = DataManager.getInstance(mContext);
        mDataManager.setPresenterRestApiCallBack(this);
        mDataManager.setPresenterSqliteCallBack(this);
    }


    @Override
    public void loadList(String source) {

        mView.showProgress();
        if (source.equals(RestApiSourceCategories)) {
            mDataManager.getRemoteCategories();
        } else if (source.equals(RestApiSourceItems)) {
            mDataManager.getRemoteItems();
        }

    }

    @Override
    public void loadItemsByCategory(long category_id) {

        mView.showProgress();
        mDataManager.getRemoteItemsByCategoryId(category_id);
    }

    @Override
    public void onDataListLoaded(ArrayList data, String url) {

    }

    @Override
    public void onDataObjectLoaded(Object data, String url) {

        mView.hideProgress();
        if (url.equals(getAllCategoriesWithoutItems)) {
            if (((RestApiListFragment) mView).getSourceFragment().equals(RestApiSourceCategories)) {
                ((RestApiListFragment) mView).showList(((CategoriesRestApiResponse) data).getResults());
            } else if (((RestApiListFragment) mView).getSourceFragment().equals(RestApiSourceItems)) {
                ((RestApiListFragment) mView).showSpCategories(((CategoriesRestApiResponse) data).getResults());
            }

        } else if (url.equals(getAllItemsOrderedByCategory)) {
            ((RestApiListFragment) mView).showList(((ItemsRestApiResponse) data).getResults());
        } else if (url.equals(getAllItemsByCategory)) {
            ((RestApiListFragment) mView).showList(((ItemsRestApiResponse) data).getResults());
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
                        if (((RestApiListFragment) mView).getSourceFragment().equals(RestApiSourceCategories)) {
                            ((RestApiListFragment) mView).showList(categoriesRestApiResponse.getResults());
                        } else if (((RestApiListFragment) mView).getSourceFragment().equals(RestApiSourceItems)) {
                            ((RestApiListFragment) mView).showSpCategories(categoriesRestApiResponse.getResults());
                        }

                    }

                } else if (cacheApi.getUrl().equals(getAllItemsOrderedByCategory)) {
                    ItemsRestApiResponse itemsRestApiResponse = (ItemsRestApiResponse) mDataManager.loadCacheData(cacheApi);
                    if (itemsRestApiResponse != null) {
                        ((RestApiListFragment) mView).showList(
                                itemsRestApiResponse.getResults());
                    }
                } else if (cacheApi.getUrl().equals(getAllItemsByCategory)) {
                    ItemsRestApiResponse itemsRestApiResponse = (ItemsRestApiResponse) mDataManager.loadCacheData(cacheApi);
                    if (itemsRestApiResponse != null) {
                        ((RestApiListFragment) mView).showList(
                                itemsRestApiResponse.getResults());
                    }
                }
            }

        }

    }
}
