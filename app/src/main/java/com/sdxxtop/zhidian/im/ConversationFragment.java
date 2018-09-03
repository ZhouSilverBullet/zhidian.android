package com.sdxxtop.zhidian.im;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sdxxtop.zhidian.App;
import com.sdxxtop.zhidian.R;
import com.sdxxtop.zhidian.adapter.ConversationRecyclerAdapter;
import com.sdxxtop.zhidian.adapter.MessageCenterRecyclerAdapter;
import com.sdxxtop.zhidian.entity.MsgIndexBean;
import com.sdxxtop.zhidian.eventbus.ChangeCompanyEvent;
import com.sdxxtop.zhidian.http.IRequestListener;
import com.sdxxtop.zhidian.http.Params;
import com.sdxxtop.zhidian.http.RequestCallback;
import com.sdxxtop.zhidian.http.RequestUtils;
import com.sdxxtop.zhidian.ui.activity.ContactSearchActivity;
import com.sdxxtop.zhidian.ui.activity.MainActivity;
import com.sdxxtop.zhidian.ui.activity.PartAndParentActivity;
import com.sdxxtop.zhidian.utils.ItemDivider;
import com.sdxxtop.zhidian.utils.StatusBarUtil;
import com.sdxxtop.zhidian.utils.ViewUtil;
import com.sdxxtop.zhidian.widget.SubTitleView;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;
import com.tencent.imsdk.ext.sns.TIMFriendFutureItem;
import com.tencent.qcloud.presentation.presenter.ConversationPresenter;
import com.tencent.qcloud.presentation.presenter.FriendshipManagerPresenter;
import com.tencent.qcloud.presentation.presenter.GroupManagerPresenter;
import com.tencent.qcloud.presentation.viewfeatures.ConversationView;
import com.tencent.qcloud.presentation.viewfeatures.FriendshipMessageView;
import com.tencent.qcloud.presentation.viewfeatures.GroupManageMessageView;
import com.tencent.qcloud.timchat.model.Conversation;
import com.tencent.qcloud.timchat.model.FriendshipConversation;
import com.tencent.qcloud.timchat.model.GroupManageConversation;
import com.tencent.qcloud.timchat.model.MessageFactory;
import com.tencent.qcloud.timchat.model.NomalConversation;
import com.tencent.qcloud.timchat.ui.ChatActivity;
import com.tencent.qcloud.timchat.utils.ConversationSortUtil;
import com.tencent.qcloud.timchat.utils.UnreadUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 会话列表界面
 */
public class ConversationFragment extends Fragment implements ConversationView, FriendshipMessageView, GroupManageMessageView {

    private final String TAG = "ConversationFragment";
    public static String group_id;
    public boolean isGroupCreate;

    private View view;
    private List<Conversation> conversationList = new LinkedList<>();
    private RecyclerView msgRecyclerView;
    private RecyclerView conversationRecyclerView;
    private SubTitleView titleView;
    private ConversationPresenter presenter;
    private FriendshipManagerPresenter friendshipManagerPresenter;
    private GroupManagerPresenter groupManagerPresenter;
    private List<String> groupList;
    private FriendshipConversation friendshipConversation;
    private GroupManageConversation groupManageConversation;
    private ConversationRecyclerAdapter adapter;
    private MessageCenterRecyclerAdapter msgAdapter;
    private View searchView;


    public ConversationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {

            eventRegister();

            view = inflater.inflate(R.layout.fragment_conversation1, container, false);
            initTitleView(view);
            conversationRecyclerView = (RecyclerView) view.findViewById(R.id.conversation_recycler);
            conversationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            conversationRecyclerView.addItemDecoration(new ItemDivider());
            conversationRecyclerView.setNestedScrollingEnabled(false);
            adapter = new ConversationRecyclerAdapter(R.layout.item_conversation, conversationList);
            conversationRecyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Conversation conversation = conversationList.get(position);
                    UnreadUtil.getInstance().setMessageCount(UnreadUtil.getInstance().getMessageCount() - conversation.getUnreadNum());
                    conversation.navToDetail(getActivity());
                    if (conversation instanceof GroupManageConversation) {
                        groupManagerPresenter.getGroupManageLastMessage();
                    }
                }
            });
//            adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    conversationList.get(position).navToDetail(getActivity());
//                    if (conversationList.get(position) instanceof GroupManageConversation) {
//                        groupManagerPresenter.getGroupManageLastMessage();
//                    }
//                }
//            });
            friendshipManagerPresenter = new FriendshipManagerPresenter(this);
            groupManagerPresenter = new GroupManagerPresenter(this);
            presenter = new ConversationPresenter(this);
            presenter.getConversation();
            registerForContextMenu(conversationRecyclerView);

            //消息
            msgRecyclerView = (RecyclerView) view.findViewById(R.id.msg_recycler);
            msgRecyclerView.addItemDecoration(new ItemDivider());
            msgRecyclerView.setNestedScrollingEnabled(false);
            msgRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            msgAdapter = new MessageCenterRecyclerAdapter(R.layout.item_message_center_recycler);
            msgRecyclerView.setAdapter(msgAdapter);
            msgAdapter.setMessageReadClickCallback(new MessageCenterRecyclerAdapter.MessageReadClickCallback() {
                @Override
                public void readClick() {
                    //只对数量进行刷新
                    if (getActivity() instanceof MainActivity)
                        ((MainActivity) getActivity()).setMsgUnread(getTotalUnreadNum());
                }
            });
            loadZhiDianMsg();
        }
        adapter.notifyDataSetChanged();
        return view;

    }

    private void initTitleView(View view) {
        titleView = (SubTitleView) view.findViewById(R.id.conversation_title_view);
        titleView.getRightImg().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选人的那里，加群用的
                Intent intent = new Intent(v.getContext(), PartAndParentActivity.class);
                intent.putExtra("conversation", "ConversationFragment");
                startActivityForResult(intent, 102);
            }
        });
        topViewPadding(titleView);

        statusBar(true);

        searchView = view.findViewById(R.id.conversation_search_view);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSearch = new Intent(getContext(), ContactSearchActivity.class);
                intentSearch.putExtra("conversation", true);
                startActivity(intentSearch);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
//        PushUtil.getInstance().reset();
        loadZhiDianMsg();
        //存储置顶的帖子
    }


    /**
     * 初始化界面或刷新界面
     *
     * @param conversationList
     */
    @Override
    public void initView(List<TIMConversation> conversationList) {
        this.conversationList.clear();
        groupList = new ArrayList<>();
        for (TIMConversation item : conversationList) {
            switch (item.getType()) {
                case C2C:
                case Group:
                    this.conversationList.add(new NomalConversation(item));
                    groupList.add(item.getPeer());
                    break;
            }
        }
        friendshipManagerPresenter.getFriendshipLastMessage();
        groupManagerPresenter.getGroupManageLastMessage();
    }

    /**
     * 更新最新消息显示
     *
     * @param message 最后一条消息
     */
    @Override
    public void updateMessage(TIMMessage message) {
        if (message == null) {
            adapter.notifyDataSetChanged();
            return;
        }
        if (message.getConversation().getType() == TIMConversationType.System) {
            groupManagerPresenter.getGroupManageLastMessage();
            return;
        }
//        if (MessageFactory.getMessage(message) instanceof CustomMessage) return;
        NomalConversation conversation = new NomalConversation(message.getConversation());
        Iterator<Conversation> iterator = conversationList.iterator();
        while (iterator.hasNext()) {
            Conversation c = iterator.next();
            if (conversation.equals(c)) {
                conversation = (NomalConversation) c;
                iterator.remove();
                break;
            }
        }
        conversation.setLastMessage(MessageFactory.getMessage(message));
        conversationList.add(conversation);
        ConversationSortUtil.sortForPlaceTop(conversationList);
//        Collections.sort(conversationList);
        if (!TextUtils.isEmpty(group_id) && group_id.equals(message.getConversation().getPeer())) {
            isGroupCreate = false;
            delaySkipGroup(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(group_id)) {
                        ChatActivity.navFriendUserGroupToChat(getActivity(), group_id, TIMConversationType.Group);
                        group_id = null;
                    }
                    Log.e(TAG, "updateMessage skip ChatActivity");
                }
            });
        } else {
            isGroupCreate = true;
        }

        refresh();
    }

    /**
     * 更新好友关系链消息
     */
    @Override
    public void updateFriendshipMessage() {
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    /**
     * 删除会话
     *
     * @param identify
     */
    @Override
    public void removeConversation(String identify) {
        Iterator<Conversation> iterator = conversationList.iterator();
        while (iterator.hasNext()) {
            Conversation conversation = iterator.next();
            if (conversation.getIdentify() != null && conversation.getIdentify().equals(identify)) {
                iterator.remove();
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    /**
     * 更新群信息
     *
     * @param info
     */
    @Override
    public void updateGroupInfo(TIMGroupCacheInfo info) {
        for (Conversation conversation : conversationList) {
            if (conversation.getIdentify() != null && conversation.getIdentify().equals(info.getGroupInfo().getGroupId())) {
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    /**
     * 刷新
     */
    @Override
    public void refresh() {
//        Collections.sort(conversationList);
        ConversationSortUtil.sortForPlaceTop(conversationList);
        adapter.notifyDataSetChanged();
        if (getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).setMsgUnread(getTotalUnreadNum());
    }


    /**
     * 获取好友关系链管理系统最后一条消息的回调
     *
     * @param message     最后一条消息
     * @param unreadCount 未读数
     */
    @Override
    public void onGetFriendshipLastMessage(TIMFriendFutureItem message, long unreadCount) {
        if (friendshipConversation == null) {
            friendshipConversation = new FriendshipConversation(message);
            conversationList.add(friendshipConversation);
        } else {
            friendshipConversation.setLastMessage(message);
        }
        friendshipConversation.setUnreadCount(unreadCount);
//        Collections.sort(conversationList);
        ConversationSortUtil.sortForPlaceTop(conversationList);
        refresh();
    }

    /**
     * 获取好友关系链管理最后一条系统消息的回调
     *
     * @param message 消息列表
     */
    @Override
    public void onGetFriendshipMessage(List<TIMFriendFutureItem> message) {
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    /**
     * 获取群管理最后一条系统消息的回调
     *
     * @param message     最后一条消息
     * @param unreadCount 未读数
     */
    @Override
    public void onGetGroupManageLastMessage(TIMGroupPendencyItem message, long unreadCount) {
        if (groupManageConversation == null) {
            groupManageConversation = new GroupManageConversation(message);
            conversationList.add(groupManageConversation);
        } else {
            groupManageConversation.setLastMessage(message);
        }
        groupManageConversation.setUnreadCount(unreadCount);
        ConversationSortUtil.sortForPlaceTop(conversationList);
//        Collections.sort(conversationList);
        refresh();
    }

    /**
     * 获取群管理系统消息的回调
     *
     * @param message 分页的消息列表
     */
    @Override
    public void onGetGroupManageMessage(List<TIMGroupPendencyItem> message) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
//        Conversation conversation = conversationList.get(info.position);
//        if (conversation instanceof NomalConversation) {
        int position = adapter.getPosition();
        Conversation conversation = conversationList.get(position);
        if (conversation instanceof NomalConversation) {
            menu.add(0, 1, Menu.NONE, getString(R.string.conversation_del));
        }
//        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = adapter.getPosition();
        NomalConversation conversation = (NomalConversation) conversationList.get(position);
        switch (item.getItemId()) {
            case 1:
                if (conversation != null) {
                    if (presenter.delConversation(conversation.getType(), conversation.getIdentify())) {
                        conversationList.remove(conversation);
                        adapter.notifyDataSetChanged();
                        if (getActivity() instanceof MainActivity)
                            ((MainActivity) getActivity()).setMsgUnread(getTotalUnreadNum());
                    }
                }
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private long getTotalUnreadNum() {
        long num = 0;
        for (Conversation conversation : conversationList) {
            num += conversation.getUnreadNum();
        }

        if (msgAdapter != null) {
            num += msgAdapter.getRedCount();
        }
        UnreadUtil.getInstance().setMessageCount(num);
        return num;
    }

    private void loadZhiDianMsg() {
        Params params = new Params();
        RequestUtils.createRequest().postMsgIndex(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<MsgIndexBean>() {
            @Override
            public void onSuccess(MsgIndexBean baseModel) {
                List<MsgIndexBean.DataBean> data = baseModel.getData();
                if (data != null) {
                    msgAdapter.replaceData(data);
                }
                if (getActivity() instanceof MainActivity)
                    ((MainActivity) getActivity()).setMsgUnread(getTotalUnreadNum());
            }

            @Override
            public void onFailure(int code, String errorMsg) {
//                ToastUtil.show(errorMsg);
            }
        }));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 102 && resultCode == PartAndParentActivity.CUSTOMER_RESULT_OK && data != null) {
            group_id = data.getStringExtra("group_id");
            if (!TextUtils.isEmpty(group_id)) {
                delaySkipGroup(new Runnable() {
                    @Override
                    public void run() {
                        if (isGroupCreate && !TextUtils.isEmpty(group_id)) {
                            ChatActivity.navFriendUserGroupToChat(getActivity(), group_id, TIMConversationType.Group);
                            isGroupCreate = false;
                            group_id = null;
                        }
                        Log.e(TAG, "onActivityResult skip ChatActivity");
                    }
                });
            }
//            ChatActivity.navFriendUserGroupToChat(getActivity(), group_id, TIMConversationType.Group);
//            HashSet<Integer> userSet = (HashSet<Integer>) data.getSerializableExtra(PartAndParentActivity.TEACHER_USER_SET);
//            HashSet<Integer> partSet = (HashSet<Integer>) data.getSerializableExtra(PartAndParentActivity.TEACHER_PART_SET);
//            HashSet<Integer> typeSet = (HashSet<Integer>) data.getSerializableExtra(PartAndParentActivity.PARENT_TYPE_SET);
//            String userSetString = CollectUtil.getIntegerSetToString(userSet);
//            String partSetString = CollectUtil.getIntegerSetToString(partSet);
//            String typeSetString = CollectUtil.getIntegerSetToString(typeSet);
//            HashMap<String, ParentUserBean> parentMap = (HashMap<String, ParentUserBean>) data.getSerializableExtra(PartAndParentActivity.PARENT_PARENT_MAP);
//
//            String parentMapString = CollectUtil.getParentMapToString(parentMap);
//            joinGroup(userSetString, partSetString, typeSetString,parentMapString);
        }
    }

    private void delaySkipGroup(final Runnable runnable) {
        if (view == null) {
            return;
        }

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null || getActivity().isFinishing()) {
                    return;
                }

                if (runnable != null) {
                    runnable.run();
                }
            }
        }, 300);
    }

//    private void joinGroup(String selectUserId, String selectPartId, String selectTypeId, String parentMapId) {
//        Params params = new Params();
//
//        //当前人的id
//        params.put("oi", params.getUserId());
//        params.put("ui", selectUserId);
//        params.put("pi", selectPartId);
//        params.put("ti", selectTypeId);
//        params.put("sui", parentMapId);
//
//        RequestUtils.createRequest().postTimCreateGroup(params.getData()).enqueue(new RequestCallback<>(new IRequestListener<BaseModel>() {
//            @Override
//            public void onSuccess(BaseModel baseModel) {
//                ToastUtil.show("成功");
//            }
//
//            @Override
//            public void onFailure(int code, String errorMsg) {
//                ToastUtil.show(errorMsg);
//            }
//        }));
//    }

    private void eventRegister() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe
    public void changeCompanyNotify(ChangeCompanyEvent event) {
        if (presenter != null) {
            groupManageConversation = null;
            friendshipConversation = null;
            presenter.getConversation();
            if (msgAdapter != null) {
                //置为空
                msgAdapter.replaceData(new ArrayList<MsgIndexBean.DataBean>());
            }
            loadZhiDianMsg();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    ///////////////////沉淀式加入//////////////////

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            statusBar(true);
            //加载数据
            loadZhiDianMsg();
//            initData();
            //只对数量进行刷新
            if (getActivity() instanceof MainActivity)
                ((MainActivity) getActivity()).setMsgUnread(getTotalUnreadNum());
        }
    }

    public void topViewPadding(View view) {
        if (isVersionMoreKitkat()) {
            view.setPadding(0, ViewUtil.getStatusHeight(App.getAppContext()), 0, 0);
        }
    }

    public boolean isVersionMoreKitkat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return true;
        } else {
            return false;
        }
    }

    public void statusBar(boolean isDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//Android6.0以上系统
            StatusBarUtil.setDarkStatusIcon(getActivity().getWindow(), isDark);
        }
    }
}
