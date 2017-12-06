package com.example.template.ui.TestRestApi.presenter.list;

import modules.basemvp.Base;

/**
 * Created by Net22 on 11/26/2017.
 */

public interface IRestApiListContract {
    public interface IRestApiListView {

    }


    public interface IRestApiListPresenter extends Base.IPresenter {

        void openFragment(String source);
    }
}
