package com.example.template.ui.maps.presenter;

import modules.basemvp.Base;

/**
 * Created by Net22 on 11/13/2017.
 */

public interface IMapControlActivityContract {

    public interface IMapControlActivityContractView {
    }

    public interface IMapControlActivityContractPresenter extends Base.IPresenter {
        void openFragmentMap();
    }
}
