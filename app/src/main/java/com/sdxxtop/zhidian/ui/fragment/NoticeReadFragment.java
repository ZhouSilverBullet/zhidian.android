package com.sdxxtop.zhidian.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.entity.NoticeShow2PeopleBean;
import com.sdxxtop.zhidian.ui.base.BaseFragment;
import com.sdxxtop.zhidian.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeReadFragment extends BaseFragment {

    @BindView(R.id.notice_read_recycler)
    RecyclerView recyclerView;

    private List<NoticeShow2PeopleBean.DataBean.ReadBean> read;
    private ReadAdapter mAdapter;

    public NoticeReadFragment() {
    }

    public static NoticeReadFragment newInstance(int type, List<NoticeShow2PeopleBean.DataBean.ReadBean> read) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putSerializable("read", (ArrayList) read);
        NoticeReadFragment fragment = new NoticeReadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_notice_read;
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mAdapter = new ReadAdapter(R.layout.item_notice_read_recycler);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initVariables() {
        if (getArguments() != null) {
            read = (List<NoticeShow2PeopleBean.DataBean.ReadBean>) getArguments().getSerializable("read");
        }
    }

    @Override
    protected void initData() {
        if (read != null) {
            mAdapter.addData(read);
        }
    }

    class ReadAdapter extends BaseQuickAdapter<NoticeShow2PeopleBean.DataBean.ReadBean, BaseViewHolder> {

        public ReadAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, NoticeShow2PeopleBean.DataBean.ReadBean item) {
            CircleImageView image = helper.getView(R.id.item_notice_read_image);
            TextView name = helper.getView(R.id.item_notice_read_name);
            TextView bottomName = helper.getView(R.id.item_notice_read_bottom_name);

            String img = item.getImg();
            String nameValue = item.getName();
            if (!TextUtils.isEmpty(img)) {
                ViewUtil.setColorItemView(img, nameValue, name, image);
            }

            bottomName.setText(nameValue);
        }
    }
}
