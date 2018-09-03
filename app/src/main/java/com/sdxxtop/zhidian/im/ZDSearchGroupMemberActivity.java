package com.sdxxtop.zhidian.im;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.GroupMemberAdapter;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindView;

public class ZDSearchGroupMemberActivity extends BaseActivity {

    private String editChangeTextValue;

    @BindView(R.id.group_search_edit)
    EditText searchEdit;
    @BindView(R.id.group_search_cancel)
    TextView searchCancelText;
    @BindView(R.id.group_recycler)
    RecyclerView mRecyclerView;
    private ArrayList<UserProfile> userProfiles;
    private GroupMemberAdapter mAdapter;
    private boolean showSelect;
    private String identify;
    private boolean changeOwner;

    @Override
    protected int getActivityView() {
        return R.layout.activity_zdsearch_group_member;
    }

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            userProfiles = (ArrayList<UserProfile>) getIntent().getSerializableExtra("userProfiles");
            identify = getIntent().getStringExtra("identify");
            showSelect = getIntent().getBooleanExtra("show_select", false);
            changeOwner = getIntent().getBooleanExtra("change_owner", false);
        }
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new ItemDivider());

        mAdapter = new GroupMemberAdapter(R.layout.item_group_member_recycler);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setShowSelect(showSelect);
        mAdapter.setChangeOwner(changeOwner);
        mAdapter.setGroupId(identify);
    }

    @Override
    protected void initEvent() {
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //加入0.4s的间隔
                editChangeTextValue = "";
                if (!StringUtil.isEmptyWithTrim(s.toString())) {
                    editChangeTextValue = s.toString().trim();
//                    searchCancelText.setVisibility(View.VISIBLE);
                } else {
//                    searchCancelText.setVisibility(View.GONE);
                }
                refreshRecycler(editChangeTextValue);
            }
        });

        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String s = searchEdit.getText().toString();
                    refreshRecycler(s);
                }
                return false;
            }
        });

        searchCancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                searchEdit.setText("");
//                editChangeTextValue = "";
//                refreshFragment(editChangeTextValue);
            }
        });

        mAdapter.setChangeOwnerListener(new GroupMemberAdapter.ChangeOwnerListener() {
            @Override
            public void onChange() {
                setResult(121);
                finish();
            }
        });

        mAdapter.setSelectAdminListener(new GroupMemberAdapter.SelectAdminListener() {
            @Override
            public void onSelect() {
                HashSet<UserProfile> cacheSet = mAdapter.getCacheSet();
                if (cacheSet.size() == 1) {
                    for (UserProfile profile : cacheSet) {
                        String identifier = profile.getIdentifier();
                        Intent intent = new Intent();
                        intent.putExtra("identifier", identifier);
                        setResult(122, intent);
                        break;
                    }
                    finish();
                }
            }
        });
    }

    ArrayList<UserProfile> emptyList = new ArrayList<>(0);
    ArrayList<UserProfile> tempList = new ArrayList<>(0);


    private void refreshRecycler(String editChangeTextValue) {
        if (TextUtils.isEmpty(editChangeTextValue)) {
            mAdapter.replaceData(emptyList);
            return;
        }

        if (userProfiles == null || userProfiles.size() == 0) {
            mAdapter.replaceData(emptyList);
            return;
        }

        tempList.clear();
        for (UserProfile userProfile : userProfiles) {
            String nickName = userProfile.getNickName();
            if (!TextUtils.isEmpty(nickName) && nickName.contains(editChangeTextValue)) {
                tempList.add(userProfile);
            }
        }

        mAdapter.replaceData(tempList);
    }
}
