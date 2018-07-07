package com.sdxxtop.zhidian.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.LocationBean;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AmapDialogFragment extends DialogFragment implements PoiSearch.OnPoiSearchListener {


    @BindView(R.id.dialog_search_back)
    ImageButton backBtn;
    @BindView(R.id.dialog_serach_btn_search)
    ImageButton searchBtn;
    @BindView(R.id.dialog_search_et)
    EditText searchEdit;
    @BindView(R.id.search_maps_bar)
    RelativeLayout searchMapsRl;
    Unbinder unbinder;
    private PoiSearch.Query query;
    private PoiSearch poiSearch;
    private ArrayList<LocationBean> datas;

    public AmapDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(STYLE_NO_TITLE, R.style.style_dialog);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_amap_dialog, container, false);
//        unbinder = ButterKnife.bind(this, view);
//        initView();
//        initEvent();
//        return view;
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_amap_dialog, null);
        unbinder = ButterKnife.bind(this, view);
        final Dialog dialog = new Dialog(getActivity(), R.style.style_dialog);
        dialog.setContentView(view);
        dialog.show();

        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        initView();
        initEvent();
        return dialog;

    }

    private void initEvent() {
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textChangeSearch(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initView() {
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLocationPoi();
            }
        });
    }

    private void searchLocationPoi() {
        String searchValue = searchEdit.getText().toString().trim();
        if (TextUtils.isEmpty(searchValue)) {
            ToastUtil.show("内容为空!");
        } else {
            query = new PoiSearch.Query(searchValue, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
            query.setPageSize(20);// 设置每页最多返回多少条poiitem
            query.setPageNum(0);// 设置查第一页
            poiSearch = new PoiSearch(getActivity(), query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int errCode) {
        if (errCode == 1000) {
            datas = new ArrayList<>();
            ArrayList<PoiItem> pois = poiResult.getPois();
            for (int i = 0; i < pois.size(); i++) {
                LocationBean locationBean = new LocationBean();
                locationBean.title = pois.get(i).getTitle();
                locationBean.snippet = pois.get(i).getSnippet();
                datas.add(locationBean);
            }
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }


    /**
     * 监听edittext内容的变化,去搜索
     */
    private void textChangeSearch(CharSequence charSequence) {
        String content = charSequence.toString().trim();//获取自动提示输入框的内容
        InputtipsQuery inputtipsQuery = new InputtipsQuery(content, "");//初始化一个输入提示搜索对象，并传入参数
        Inputtips inputtips = new Inputtips(getActivity(), inputtipsQuery);//定义一个输入提示对象，传入当前上下文和搜索对象
        inputtips.setInputtipsListener(new Inputtips.InputtipsListener() {
            @Override
            public void onGetInputtips(List<Tip> list, int errcode) {
                if (errcode == 1000 && list != null) {
                    datas = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        LocationBean locationBean = new LocationBean();
                        Tip tip = list.get(i);
                        locationBean.latitude = tip.getPoint().getLatitude();
                        locationBean.longitude = tip.getPoint().getLongitude();
                        locationBean.snippet = tip.getName();
                        locationBean.title = tip.getDistrict();
                        datas.add(locationBean);
                    }
//                    searchCarAdapter.setNewData(datas);
                }
            }
        });//设置输入提示查询的监听，实现输入提示的监听方法onGetInputtips()
        inputtips.requestInputtipsAsyn();//输入查询提示的异步接口实现
    }
}
