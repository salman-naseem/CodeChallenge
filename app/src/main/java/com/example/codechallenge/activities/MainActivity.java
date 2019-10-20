package com.example.codechallenge.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codechallenge.R;
import com.example.codechallenge.adapters.RecyclerAdapter;
import com.example.codechallenge.callbacks.ItemClickListener;
import com.example.codechallenge.utils.DialogBuilder;
import com.example.codechallenge.utils.HelperMethods;
import com.example.codechallenge.utils.MessageUtil;
import com.example.codechallenge.utils.Validations;
import com.example.codechallenge.viewmodels.MainActivityViewModel;
import com.example.trimulabstask.GetArtworksQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/*
 * This is our MainActivity which handles all UI related stuff
 */
public class MainActivity extends BaseActivity implements ItemClickListener {
    /*Main Activity is extended by Base Activity and implementing ItemClickListener which triggers when user
    * click on RecyclerView Item.*/

    @BindView(R.id.main_viewflipper)// Flipping Views when Network State Changed
    ViewFlipper viewFlipper;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_search_icon)
    ImageView ivSearchIcon;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private View rootView;
    private RecyclerAdapter mAdapter;
    private List<GetArtworksQuery.Artwork> adapterData;
    public MainActivityViewModel mMainActivityViewModel;
    private DialogBuilder dialogBuilder;
    private boolean isSearching;
    private String strSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);//Binding the ButterKnife instance with this activity
        initObjects();

        mMainActivityViewModel.getDataList().observe(this, articleList -> {
            //DataSource change will be observed on network call success, on search results and on restore Data source
            adapterData.clear();
            adapterData.addAll(articleList);
            mAdapter.notifyDataSetChanged();
        });

        mMainActivityViewModel.getIsUpdating().observe(this, aBoolean -> {//State change will be observed when network call will start and finish
            if(aBoolean){//when network call start show loading dialog
                showLoadingDialog();
            }
            else{//when network call finish hide the dialog
                hideLoadingDialog();
            }
        });

        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideLoadingDialog();//Hide dialog if user close activity when dialog was showing
        HelperMethods.hideKeyboard(this);//Closing Keyboard
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNetworkStartChange(int state) {//Base Activity will trigger this on Network state change
        flipView(state);
    }

    @OnTextChanged(R.id.et_search)void onTextChange(){
        if(fieldValidation()){//If user entered character, start search
            if(isAlphaNumericAndApaceOnly(etSearch.getText().toString())) {
                etSearch.setTextColor(getResources().getColor(R.color.dark));
                isSearching = true;
                strSearch = etSearch.getText().toString();
                mMainActivityViewModel.search(strSearch);//search the user entered string
                ivSearchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_close));//Changing icon
            }else{
                etSearch.setTextColor(getResources().getColor(R.color.red));
                MessageUtil.showSnackbarMessage(rootView, "Search can't contain special characters!");
            }
        }else{//if EditText is empty restore Data Source to original state
            isSearching = false;
            etSearch.setTextColor(getResources().getColor(R.color.dark));
            ivSearchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_search));//Changing icon
            mMainActivityViewModel.restoreOriginalData();//updating DataSource with original data
        }
    }

    @OnClick(R.id.iv_search_icon) void setIconState(){
        if(isSearching){ //This can be used to reset the search and DataSource list
            isSearching = false;
            strSearch = "";
            etSearch.setText(strSearch);
            etSearch.setTextColor(getResources().getColor(R.color.dark));
            HelperMethods.hideKeyboard(this);//Closing Keyboard
            ivSearchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_search));//Changing icon
            mMainActivityViewModel.restoreOriginalData();//updating DataSource with original data
        }
    }

    @Override
    public void onItemClick(int position, Object obj) {
        //This will trigger when user clicked from a list
    }

    private void initObjects() {
        rootView = findViewById(android.R.id.content);
        dialogBuilder = new DialogBuilder(this);//Initializing the DialofBuilder object
        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);//ViewModelProviders provides the MainActivityViewModel object
        adapterData = new ArrayList<>();
        mMainActivityViewModel.init();//init Method Calling
    }

    private boolean fieldValidation(){
        return Validations.isObjectNotEmpty(etSearch.getText().toString());
    }

    private boolean isAlphaNumericAndApaceOnly(String str){
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9 ]*$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    private void flipView(int state){//This method flips views between Network availability and unavailability screens
        viewFlipper.setDisplayedChild(state);
    }

    private void initRecyclerView(){
        if (mAdapter == null) {//If adapter is null instantiate it.
            mAdapter = new RecyclerAdapter(this, adapterData, this);//Initializing and providing Context, DataSource and CallbackListener
            recyclerView.setAdapter(mAdapter);//Setting adapter in recyclerView
        } else {
            mAdapter.notifyDataSetChanged();//update adapter with DataSource
        }
    }

    private void showLoadingDialog() {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(() -> dialogBuilder.loadingDialog(getResources().getString(R.string.loading)));
    }

    private void hideLoadingDialog() {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(() -> dialogBuilder.dismissDialog());
    }
}
