package com.example.ntpver1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;


import com.example.ntpver1.adapter.StoreAdapter;
import com.example.ntpver1.item.Store;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.SweetSheet;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {
    private static final String TAG = "MapActivity";
    private static final int SEARCH = 1;
    private static final int UPDATE = 2;
    private static final double TEST_LATITUDE_VALUE = 37.245347801;
    private static final double TEST_LONGITUDE_VALUE = 127.01442311;
    private static final int TEST_RADIUS_VALUE = 500;

    private MapActivity thisClass = this;
    private Activity mapAct;
    private Context mapContext;

    MyTool myTool;
    DBManager dbManager;
    MapManager mapManager;

    //상태
    static final int ON_SEARCH_SETTING_BUTTON = 1;
    static final int ON_SEARCH_SETTING_LAYOUT = 2;
    static final int ON_RESULT_LAYOUT = 3;

    int bottomLayoutState = ON_SEARCH_SETTING_BUTTON;

    //인풋매니저(키보드관리)
    InputMethodManager imm;

    //핸들러
    Handler handler = new Handler();

    //구글맵
    SupportMapFragment mapFragment;
    GoogleMap map;

    //리싸이클러뷰 어댑터
    StoreAdapter storeAdapter;

    //검색바
    MaterialSearchBar searchBar;

    //스윗시트
    SweetSheet sweetSheet;
    RelativeLayout sweetSheetRl;
    View ssView;

    //검색설정 테이블
    TableLayout payTableLayout;
    TableLayout storeTableLayout;
    ArrayList<String> payCategory;
    ArrayList<String> storeCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        mapAct = MapActivity.this;
        mapContext = getApplicationContext();

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        myTool = new MyTool();
        dbManager = DBManager.getInstance();

        setViews();
        setRecyclerView();

        //TEST
    }

    private void setViews() {
        setMap();
        setSearchBar();
        setSweetSheet();
        setSearchSettingTable();
    }

    //구글맵프래그먼트
    private void setMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d("Map", "Map is ready.");
                map = googleMap;

                mapManager = MapManager.getInstance(map, thisClass);
                mapManager.checkPermission();
                mapManager.getMyLocation();
                mapManager.showMyLocation();
                doSearch("",mapManager.getSearchCentermymakerlntlng().latitude, mapManager.getSearchCentermymakerlntlng().longitude , 500 ,  2);
            }
        });
    }

    
    //검색바
    private void setSearchBar() {
        searchBar = (MaterialSearchBar) findViewById(R.id.search_bar);
        searchBar.setOnSearchActionListener(this);
    }

    //스윗시트
    private void setSweetSheet() {
        sweetSheetRl = findViewById(R.id.sweet_sheet_rlayout);
        sweetSheet = new SweetSheet(sweetSheetRl);

        CustomDelegate customDelegate = new CustomDelegate(true,
                CustomDelegate.AnimationType.DuangLayoutAnimation);
        ssView = LayoutInflater.from(this).inflate(R.layout.map_bottom_layout, null, false);
        customDelegate.setCustomView(ssView);
        sweetSheet.setDelegate(customDelegate);

        sweetSheet.setBackgroundClickEnable(false);
    }

    //검색설정 테이블
    private void setSearchSettingTable() {
        payTableLayout = ssView.findViewById(R.id.pay_category_tlayout);
        storeTableLayout = ssView.findViewById(R.id.store_category_tlayout);
        payCategory = new ArrayList<>();
        storeCategory = new ArrayList<>();
    }

    //테이블 체크값 가져오기
    public void setSearchSettingTableValue() {
        CheckBox checkBox;

        payCategory.removeAll(payCategory);
        storeCategory.removeAll(storeCategory);

        ArrayList<View> payBoxes = myTool.getAllTableCheckBox(payTableLayout);

        for (View payBox : payBoxes) {
            checkBox = (CheckBox) payBox;
            if (checkBox.isChecked()) {
                payCategory.add(checkBox.getText().toString());
                Log.d("getTableValue", checkBox.getText().toString() + " is added.");
            }
        }

        ArrayList<View> storeBoxes = myTool.getAllTableCheckBox(storeTableLayout);
        for (View storeBox : storeBoxes) {
            checkBox = (CheckBox) storeBox;
            if (checkBox.isChecked()) {
                storeCategory.add(checkBox.getText().toString());
                Log.d("getTableValue", checkBox.getText().toString() + " is added.");
            }
        }

        //TEST
        Log.i("CheckCount", "Pay check count" + Integer.toString(payCategory.size()));
        Log.i("CheckCount", "Store check count" + Integer.toString(storeCategory.size()));
    }


    //리싸이클러뷰
    protected void setRecyclerView() {
        RecyclerView resultRecyclerView = ssView.findViewById(R.id.result_recycler_view);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        resultRecyclerView.setLayoutManager(layoutManager);

        storeAdapter = new StoreAdapter();

        resultRecyclerView.setAdapter(storeAdapter);
    }

    //결과데이터 추가
    private void addResult(Store store) {
        storeAdapter.addItem(store);
    }

    //검색설정 버튼
    public void searchSettingButtonListener(View view) throws InterruptedException {
        bottomLayoutState = ON_SEARCH_SETTING_LAYOUT;
        mySetVisibility(bottomLayoutState);

        if (!sweetSheet.isShow()) {
            try {
                sweetSheet.show();
            } catch (Exception e) {
                Toast.makeText(this, "Please, Wait a second", Toast.LENGTH_SHORT).show();
                bottomLayoutState = ON_SEARCH_SETTING_BUTTON;
                mySetVisibility(bottomLayoutState);
            }
        }
    }

    //검색설정완료 버튼
    public void searchSettingConfirmButtonListener(View view) {
        sweetSheet.dismiss();

        setSearchSettingTableValue();

        bottomLayoutState = ON_SEARCH_SETTING_BUTTON;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mySetVisibility(bottomLayoutState);
            }
        }, 500);
    }

    public void MyLocationButtonListener(View view) {

        mapManager.clickButton();
        mapManager.CheckMoveCamera();
    }

    //검색바 리스너
    @Override
    public void onSearchStateChanged(boolean enabled) {
        //TEST
        //Toast.makeText(this, "Search bar's onSearchStateChanged Method is called, parameter is " + enabled, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
        String keyWord = searchBar.getText();

        if (!keyWord.equals("")) {
            bottomLayoutState = ON_RESULT_LAYOUT;
            mySetVisibility(bottomLayoutState);

            doSearch(keyWord, TEST_LATITUDE_VALUE, TEST_LONGITUDE_VALUE, TEST_RADIUS_VALUE, SEARCH);
        }
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        //TEST
        //Toast.makeText(this, "Search bar onButtonClicked Method is called, code is " + buttonCode, Toast.LENGTH_SHORT).show();
    }

    //검색결과창 닫기 버튼
    public void resultCloseButtonListener(View view) {
        sweetSheet.dismiss();

        bottomLayoutState = ON_SEARCH_SETTING_BUTTON;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mySetVisibility(bottomLayoutState);
            }
        }, 500);
    }

    public void doSearch(String keyWord, double latitude, double longitude, int radius, int requestCode) {
        Log.d(TAG, "doSearch() Called");

        dbManager.setSearchValue(keyWord, payCategory, storeCategory, latitude, longitude, radius);
        dbManager.readData(new DBManager.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<Store> list) {
                Log.d(TAG, "getStoreList() is called");
                storeAdapter.setClean();
                mapManager.RemovePremarker();
                for (Store store : list) {
                    addResult(store);
                    mapManager.Marking(store);
                }

                if (requestCode == SEARCH) {
                    if (!sweetSheet.isShow()) {
                        try {
                            sweetSheet.show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Please, Wait a second", Toast.LENGTH_SHORT).show();
                            bottomLayoutState = ON_SEARCH_SETTING_BUTTON;
                            mySetVisibility(bottomLayoutState);
                        }
                    }
                }
            }
        });
    }
    //가시성 설정
    private void mySetVisibility(final int bottomLayoutState) {
        switch (bottomLayoutState) {
            case ON_SEARCH_SETTING_BUTTON:
                findViewById(R.id.search_setting_button).setVisibility(View.VISIBLE);
                ssView.findViewById(R.id.search_setting_rlayout).setVisibility(View.INVISIBLE);
                ssView.findViewById(R.id.result_rlayout).setVisibility(View.INVISIBLE);
                break;
            case ON_SEARCH_SETTING_LAYOUT:
                findViewById(R.id.search_setting_button).setVisibility(View.INVISIBLE);
                ssView.findViewById(R.id.search_setting_rlayout).setVisibility(View.VISIBLE);
                ssView.findViewById(R.id.result_rlayout).setVisibility(View.INVISIBLE);
                break;
            case ON_RESULT_LAYOUT:
                findViewById(R.id.search_setting_button).setVisibility(View.INVISIBLE);
                ssView.findViewById(R.id.search_setting_rlayout).setVisibility(View.INVISIBLE);
                ssView.findViewById(R.id.result_rlayout).setVisibility(View.VISIBLE);
                break;
        }
    }

    public Activity getActivity() {
        return mapAct;
    }

    public Context getContext() {
        return mapContext;
    }
}
