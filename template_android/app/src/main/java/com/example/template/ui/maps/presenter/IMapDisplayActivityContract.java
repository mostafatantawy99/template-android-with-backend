package com.example.template.ui.maps.presenter;

import modules.basemvp.Base;

/**
 * Created by Net22 on 11/13/2017.
 */

public class IMapDisplayActivityContract {

    public interface IMapDisplayActivityContractView {
    }

    public interface IMapDisplayActivityContractPresenter extends Base.IPresenter {
        void openFragmentMap();
    }
}
