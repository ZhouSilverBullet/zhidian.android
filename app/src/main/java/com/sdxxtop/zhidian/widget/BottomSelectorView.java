package com.sdxxtop.zhidian.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.SelectBean;
import com.sdxxtop.zhidian.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/18.
 */

public class BottomSelectorView extends LinearLayout {

    private RecyclerView recyclerView;
    private Button submitBtn;
    private BottomSelectorAdapter adapter;

    public BottomSelectorView(Context context) {
        this(context, null);
    }

    public BottomSelectorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomSelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_bottom_selector, this, true);
        recyclerView = (RecyclerView) findViewById(R.id.bottom_select_recycler);
        submitBtn = (Button) findViewById(R.id.bottom_select_sure);

        adapter = new BottomSelectorAdapter(new ArrayList<SelectBean>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    public void refreshData(List<SelectBean> selectData) {
        adapter.replaceData(selectData);
    }

    public void refreshData() {
        adapter.notifyDataSetChanged();
    }

    public List<SelectBean> getList() {
        return adapter.getData();
    }

    public Button getSubmitBtn() {
        return submitBtn;
    }

    class BottomSelectorAdapter extends BaseMultiItemQuickAdapter<SelectBean, BaseViewHolder> {

        public BottomSelectorAdapter(@Nullable List<SelectBean> data) {
            super(data);

            addItemType(SelectBean.TYPE_PART, R.layout.item_notice_recve_part_recycler);
            addItemType(SelectBean.TYPE_USER, R.layout.item_notice_recve_user_recycler);
        }

        @Override
        protected void convert(BaseViewHolder helper, final SelectBean item) {
            switch (item.type) {
                case SelectBean.TYPE_USER:
                    ImageView imageView = helper.getView(R.id.item_user_img);
                    TextView textView = helper.getView(R.id.item_user_text);
                    String color = item.color;
                    if (color.startsWith("#")) {
                        textView.setText(StringUtil.stringSubName(item.name));
                        imageView.setImageDrawable(new ColorDrawable(Color.parseColor(color)));
                    } else {
                        textView.setText("");
                        Glide.with(mContext).load(color).into(imageView);
                    }
                    final Integer id = item.id;
                    helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (item.isParent) {
                                if (bottomRemoveParentClickListener != null) {
                                    bottomRemoveParentClickListener.onRemoveClick(SelectBean.TYPE_USER, id, item.identify);
                                }
                            } else {
                                if (bottomRemoveClickListener != null) {
                                    bottomRemoveClickListener.onRemoveClick(SelectBean.TYPE_USER, id);
                                }
                            }

                            if (bottomImRemoveClickListener != null) {
                                bottomImRemoveClickListener.onRemoveClick(item.identify);
                            }
                        }
                    });

                    break;
                case SelectBean.TYPE_PART:
                    helper.setText(R.id.item_part_text, item.name);
                    final Integer ids = item.id;
                    helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (item.isParent) {
                                if (bottomRemoveParentClickListener != null) {
                                    bottomRemoveParentClickListener.onRemoveClick(SelectBean.TYPE_PART, ids, item.identify);
                                }
                            } else {
                                if (bottomRemoveClickListener != null) {
                                    bottomRemoveClickListener.onRemoveClick(SelectBean.TYPE_PART, ids);
                                }
                            }
                        }
                    });
                    break;
            }
        }
    }

    BottomRemoveClickListener bottomRemoveClickListener;

    public void setBottomRemoveClickListener(BottomRemoveClickListener bottomRemoveClickListener) {
        this.bottomRemoveClickListener = bottomRemoveClickListener;
    }

    BottomRemove2ClickListener bottomRemoveParentClickListener;

    public void setBottomRemoveParentClickListener(BottomRemove2ClickListener bottomRemoveClickListener) {
        this.bottomRemoveParentClickListener = bottomRemoveClickListener;
    }

    public interface BottomRemoveClickListener {
        void onRemoveClick(int type, Integer id);
    }

    public interface BottomRemove2ClickListener {
        void onRemoveClick(int type, Integer id, String userStudentId);
    }

    BottomImRemoveClickListener bottomImRemoveClickListener;

    public void setBottomImRemoveClickListener(BottomImRemoveClickListener bottomImRemoveClickListener) {
        this.bottomImRemoveClickListener = bottomImRemoveClickListener;
    }

    public interface BottomImRemoveClickListener {
        void onRemoveClick(String identify);
    }
}
