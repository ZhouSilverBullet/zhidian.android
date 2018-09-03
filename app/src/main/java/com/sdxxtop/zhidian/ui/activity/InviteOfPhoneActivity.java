package com.sdxxtop.zhidian.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.widget.DrawerLayout;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ShowOutPowerDrawerLayoutAdapter;
import com.sdxxtop.zhidian.entity.UcenterOutIndexBean;
import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.base.BaseActivity;
import com.sdxxtop.zhidian.utils.PhoneTextWatcher;
import com.sdxxtop.zhidian.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：13:24
 * 邮箱：15010104100@163.com
 * 描述：通过手机号邀请员工界面
 */
public class InviteOfPhoneActivity extends BaseActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_part)
    TextView tvPart;
    @BindView(R.id.invite_phone_list_view)
    ListView listView;
    @BindView(R.id.invite_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.rl_phone)
    RelativeLayout rlPhone;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.iv_contact)
    ImageView ivContact;

    private ShowOutPowerDrawerLayoutAdapter adapter;

    @Override
    protected int getActivityView() {
        return R.layout.activity_invite_of_phone;
    }

    @Override
    protected void initView() {
        //设置手机号格式
        etPhone.addTextChangedListener(new PhoneTextWatcher(etPhone));
        //设置EditText长度限制
        etPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});

        adapter = new ShowOutPowerDrawerLayoutAdapter(mContext);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UcenterOutIndexBean.DataEntity.PartEntity partEntity = adapter.getData().get(position);
                tvPart.setText(partEntity.getNo_prefix());
                drawerLayout.closeDrawer(Gravity.RIGHT);
                InviteOfPhoneActivity.this.partId = partEntity.getPart_id();
            }
        });
    }

    @Override
    protected void initData() {
        Params params = new Params();
        RequestUtils.createRequest().postOutGetPart(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<UcenterOutIndexBean>() {
            @Override
            public void onSuccess(UcenterOutIndexBean ucenterOutIndexBean) {
                UcenterOutIndexBean.DataEntity data = ucenterOutIndexBean.getData();
                if (data != null) {
                    List<UcenterOutIndexBean.DataEntity.PartEntity> part = data.getPart();
                    if (part != null) {
                        adapter.replaceData(part);
                        if (part.size() > 0) {
                            String no_prefix = part.get(0).getNo_prefix();
                            int partId = part.get(0).getPart_id();
                            tvPart.setText(no_prefix);
                            InviteOfPhoneActivity.this.partId = partId;
                        }
                    }
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {

            }
        }));
    }

    @OnClick({R.id.btn_add, R.id.iv_contact, R.id.rl_part})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_contact:
                if (EasyPermissions.hasPermissions(mContext, Manifest.permission.READ_CONTACTS)) {
                    Uri uri = ContactsContract.Contacts.CONTENT_URI;
                    Intent intent = new Intent(Intent.ACTION_PICK, uri);
                    startActivityForResult(intent, 100);
                } else {
                    EasyPermissions.requestPermissions(mContext, "请开启读取联系人权限",
                            REQUEST_PERMISSION_CONTACT, Manifest.permission.READ_CONTACTS);
                }

                break;
            case R.id.btn_add:
                postJoinByPhone();
                break;
            case R.id.rl_part:
                drawerLayout.openDrawer(Gravity.RIGHT);
                break;
        }
    }


    private int partId;

    /**
     * 手机号邀请员工
     */
    private void postJoinByPhone() {
        String name = etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show("请输入姓名");
            return;
        }
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || phone.length() < 13) {
            ToastUtil.show("请输入正确手机号");
            return;
        }

        Params params = new Params();
        params.put("mb", etPhone.getText().toString().replace(" ", ""));
        params.put("un", etName.getText().toString());
        params.put("pi", partId);
        showProgressDialog("");
        RequestUtils.createRequest().postJoinByMobile(params.getData()).enqueue(new RequestCallback<BaseModel>(new IRequestListener<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                closeProgressDialog();
                ToastUtil.show("添加成功");
                etName.setText("");
                etName.requestFocus();
                etPhone.setText("");
//                finish();
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                closeProgressDialog();
                showToast(errorMsg);
            }
        }));
    }

    /**
     * 跳转联系人列表的回调函数
     * Caused by: android.database.CursorIndexOutOfBoundsException: Index 0 requested, with a size of 0
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                Uri uri = data.getData();
                String[] contacts = getPhoneContacts(uri);
                if (contacts != null && contacts.length >= 2) {
                    etName.setText(contacts[0]);
                    etPhone.setText(contacts[1]);
                    Log.d("log", "姓名:" + contacts[0] + " " + "手机号:" + contacts[1]);
                }
            }
        }
    }

    //其中getPhoneContacts（uri）方法，因为手机的联系人和手机号并不再同一个数据库中，所以我们需要分别做处理
    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    //取得联系人姓名
                    int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    contact[0] = cursor.getString(nameFieldColumnIndex);
                } else {
                    contact[0] = "";
                }
                //取得电话号码
                String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
                if (phone != null) {
                    if (phone.moveToFirst()) {
                        contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    } else {
                        contact[1] = "";
                    }
                    phone.close();

                }
            } catch (Exception e) {
                e.printStackTrace();
                contact[0] = "";
                contact[1] = "";
            }
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }
}
