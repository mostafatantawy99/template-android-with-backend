package com.example.template.ui.TestSqliteDbflow.CategoriesAndItems.Simple.fragments;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.model.IsEmptyRule;
import com.basgeekball.awesomevalidation.model.Rule;
import com.example.template.R;
import com.example.template.model.bean.sqlite.Categories;
import com.example.template.model.bean.sqlite.Items;
import com.example.template.model.bean.sqlite.querymodels.CategoriesWithOutItemsQueryModel;
import com.example.template.ui.TestSqliteDbflow.CategoriesAndItems.Simple.SqliteSingleAct;
import com.example.template.ui.TestSqliteDbflow.CategoriesAndItems.Simple.presenters.single.ISqliteSingleFragmentContract;
import com.example.template.ui.TestSqliteDbflow.CategoriesAndItems.Simple.presenters.single.SqliteSingleFragmentPresenter;
import com.example.template.ui.utils.GUI.CustomSpinner;
import com.example.template.utils.validation.awesome.ValidationUtilAwesome;
import com.example.template.utils.validation.listeners.OnValidationCallBack;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import modules.basemvp.BaseSupportFragment;

import static com.example.template.utils.Constants.INPUT_KEY;
import static com.example.template.utils.Constants.INPUT_KEY_CATEGORY_ID;
import static com.example.template.utils.Constants.SqliteSingleAction;
import static com.example.template.utils.Constants.SqliteSingleActionAdd;
import static com.example.template.utils.Constants.SqliteSingleActionGet;
import static com.example.template.utils.Constants.SqliteSingleSource;
import static com.example.template.utils.Constants.SqliteSourceCategories;
import static com.example.template.utils.Constants.SqliteSourceItems;

//import com.example.template.model.bean.sqlite.Categories;

public class SqliteSingleFragment extends BaseSupportFragment<SqliteSingleFragmentPresenter>
        implements ISqliteSingleFragmentContract.ISqliteSingleFragmentView
        , CustomSpinner.SpinnerListener, OnValidationCallBack {

    Categories categorySqlite;
    Items itemSqlite;
    String sourceFragment;
    String sourceAction;
    long elementId = 0l;
    long elementCategoryId = 0l;
    @BindView(R.id.spCategories)
    CustomSpinner spCategories;
    @BindView(R.id.edName)
    EditText edName;
    @BindView(R.id.edDescription)
    EditText edDescription;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.btnRemove)
    Button btnRemove;
    @BindView(R.id.rlBottomBtns)
    RelativeLayout rlBottomBtns;
    @BindView(R.id.llEditDelete)
    LinearLayout llEditDelete;
    ProgressDialog progressDialog;

    public String getSourceFragment() {
        return sourceFragment;
    }

    ValidationUtilAwesome validationUtilAwesome;

    @Override
    public int getLayoutResource() {
        return R.layout.frg_test_sqlite_single;
    }

    @Override
    public void configureUI() {

        validationUtilAwesome = new ValidationUtilAwesome(getActivity(), this);

        sourceFragment = getArguments().getString(SqliteSingleSource);
        sourceAction = getArguments().getString(SqliteSingleAction);
        elementId = getArguments().getLong(INPUT_KEY);
        addValidationForEditText();

        if (sourceAction.equals(SqliteSingleActionGet)) {

            btnAdd.setVisibility(View.GONE);
            llEditDelete.setVisibility(View.VISIBLE);

        } else if (sourceAction.equals(SqliteSingleActionAdd))

        {
            llEditDelete.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
        }


        if (sourceFragment.equals(SqliteSourceCategories)) {

            ((SqliteSingleAct) getContainerActivity()).getSqliteSinglePresenter().updateTitle(getString(R.string.category_details));
            if (sourceAction.equals(SqliteSingleActionGet)) {

                getSqliteSingleFragmentPresenter().loadElementById(sourceFragment, elementId);
            }

        } else if (sourceFragment.equals(SqliteSourceItems))

        {

            ((SqliteSingleAct) getContainerActivity()).getSqliteSinglePresenter().updateTitle(getString(R.string.item_details));
            elementCategoryId = getArguments().getLong(INPUT_KEY_CATEGORY_ID);
            spCategories.setVisibility(View.VISIBLE);
            edDescription.setVisibility(View.VISIBLE);
            getPresenter().loadCategories();
        }


    }

    @Override
    public SqliteSingleFragmentPresenter injectDependencies() {
        return new SqliteSingleFragmentPresenter(getContainerActivity(), this);
    }

    private void addValidationForEditText() {
        ArrayList<Rule> rules = new ArrayList<>();
        rules.add(new IsEmptyRule(getString(R.string.empty_field)).build());
        validationUtilAwesome.getAwesomeValidation().addValidation(edName, rules);
        if (sourceFragment.equals(SqliteSourceItems)) {
            validationUtilAwesome.getAwesomeValidation().addValidation(edDescription, rules);
        }

        validationUtilAwesome.addOnFocusChangeListeners();
    }


    @OnClick(R.id.btnAdd)
    public void onBtnAddClicked() {
        validationUtilAwesome.validateAllFirstError();
    }

    @OnClick(R.id.btnSave)
    public void onBtnSaveClicked() {
        validationUtilAwesome.validateAllFirstError();
    }

    @OnClick(R.id.btnRemove)
    public void onBtnRemoveClicked() {
        if (sourceFragment.equals(SqliteSourceCategories)) {
            getSqliteSingleFragmentPresenter().deleteCategory(categorySqlite);
        } else if (sourceFragment.equals(SqliteSourceItems)) {
            getSqliteSingleFragmentPresenter().deleteItem(itemSqlite);
        }

    }

    @Override
    public void onSpinnerItemSelected(String tag, ArrayList<Object> items, int pos) {

    }

    public SqliteSingleFragmentPresenter getSqliteSingleFragmentPresenter() {
        return ((SqliteSingleFragmentPresenter) getPresenter());
    }

    @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContainerActivity());
        }

        if (!getContainerActivity().isFinishing()) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            }
        }

    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            } else {
                progressDialog = null;
            }
        } else {
            progressDialog = null;
        }
    }


    @Override
    public void showSpCategories(ArrayList arrayList) {
        int initialPos = 0;
        ArrayList<CategoriesWithOutItemsQueryModel> categorySqlitesSp = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            categorySqlitesSp.add((CategoriesWithOutItemsQueryModel) arrayList.get(i));
            if (sourceAction.equals(SqliteSingleActionGet)) {
                if (initialPos == 0) {
                    if (Long.valueOf(elementCategoryId).compareTo(((CategoriesWithOutItemsQueryModel) arrayList.get(i)).getId()) == 0) {
                        initialPos = i;
                    }
                }
            }
        }
        spCategories.setData(spCategories, categorySqlitesSp
                , this
                , R.layout.spinner_item_chosers_white_black_arrow, R.layout.dropdown_spinner,
                1
                , false);
        spCategories.setSelectedObject(initialPos);

        if (sourceAction.equals(SqliteSingleActionGet)) {

            getSqliteSingleFragmentPresenter().loadElementById(sourceFragment, elementId);

        }

    }

    @Override
    public void showCategory(Categories categorySqlite) {

        this.categorySqlite = categorySqlite;
        edName.setText(categorySqlite.getName());
    }

    @Override
    public void showItem(Items itemSqlite) {
        this.itemSqlite = itemSqlite;
        edName.setText(itemSqlite.getName());
        edDescription.setText(itemSqlite.getDescription());
    }

    @Override
    public void showSuccessSqliteResponse() {
        Toast.makeText(getContainerActivity(), getString(R.string.done_successfully), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onValidationSucceeded() {

        if (sourceAction.equals(SqliteSingleActionGet)) {

            if (sourceFragment.equals(SqliteSourceCategories)) {

                categorySqlite.setName(edName.getText().toString());
                getSqliteSingleFragmentPresenter().editCategory(categorySqlite);

            } else if (sourceFragment.equals(SqliteSourceItems))

            {

                itemSqlite.setName(edName.getText().toString());
                itemSqlite.setDescription(edDescription.getText().toString());
                itemSqlite.setCategory_id(((CategoriesWithOutItemsQueryModel) spCategories.getSelectedItem()).getId());
                getSqliteSingleFragmentPresenter().editItem(itemSqlite);


            }

        } else if (sourceAction.equals(SqliteSingleActionAdd))

        {
            if (sourceFragment.equals(SqliteSourceCategories)) {

                Categories categorySqlite = new Categories();
                categorySqlite.setName(edName.getText().toString());
                getSqliteSingleFragmentPresenter().addCategory(categorySqlite);

            } else if (sourceFragment.equals(SqliteSourceItems))

            {


                Items itemSqlite = new Items();
                itemSqlite.setName(edName.getText().toString());
                itemSqlite.setDescription(edDescription.getText().toString());
                itemSqlite.setCategory_id(((CategoriesWithOutItemsQueryModel) spCategories.getSelectedItem()).getId());
                getSqliteSingleFragmentPresenter().addItem(itemSqlite);


            }
        }

    }


}
