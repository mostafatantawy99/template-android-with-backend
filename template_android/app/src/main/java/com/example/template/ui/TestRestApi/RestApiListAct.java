package com.example.template.ui.TestRestApi;

import com.example.template.R;
import com.example.template.ui.TestRestApi.presenter.list.IRestApiListContract;
import com.example.template.ui.TestRestApi.presenter.list.RestApiListPresenter;
import com.example.template.ui.parentview.ParentActivity;
import com.example.template.ui.utils.general_listeners.ITitleListener;

import static com.example.template.utils.Constants.RestApiSourceCategories;
import static com.example.template.utils.Constants.RestApiSourceItems;

public class RestApiListAct extends ParentActivity<RestApiListPresenter> implements
        IRestApiListContract.IRestApiListView {

    String sourceFragment = "";

    @Override
    public void configureUI() {

        getCsTitle().hideSettings();
        getCsTitle().initalizeView(this, getString(R.string.test_rest_api_categories), new ITitleListener() {
            @Override
            public void onBackPressed() {
                onBack();
            }

            @Override
            public void onMenuPressed() {
                onMenuPress();
            }

            @Override
            public void onSettingsPressed() {
            }
        });
//        openFragment(RestApiSourceCategories);
    }

    @Override
    protected void onResume() {
        super.onResume();
        openFragment(sourceFragment);
    }

    @Override
    public RestApiListPresenter injectDependencies() {
        return new RestApiListPresenter(this, this);
    }

    public RestApiListPresenter getRestApiListPresenter() {
        return ((RestApiListPresenter) getPresenter());
    }

    public void openFragment(String sourceFragment) {
        this.sourceFragment = sourceFragment;
        if (sourceFragment.isEmpty()) {
            sourceFragment = RestApiSourceCategories;
        }
        if (sourceFragment.equals(RestApiSourceCategories)) {
            getCsTitle().updateTitle(getString(R.string.test_rest_api_categories));
        } else if (sourceFragment.equals(RestApiSourceItems))

        {
            getCsTitle().updateTitle(getString(R.string.test_rest_api_items));
        }

        getRestApiListPresenter().openFragment(sourceFragment);
    }
}
