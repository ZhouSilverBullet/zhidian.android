package com.tencent.qcloud.timchat.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.timchat.R;
import com.tencent.qcloud.timchat.model.Message;
import com.tencent.qcloud.timchat.utils.SkipContactDetailActivityUtil;
import com.tencent.qcloud.timchat.utils.ViewUtil;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 聊天界面adapter
 */
public class ChatAdapter extends ArrayAdapter<Message> {

    private final String TAG = "ChatAdapter";

    private int resourceId;
    private View view;
    private ViewHolder viewHolder;

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        return view != null ? view.getId() : position;
    }

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ChatAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.leftMessage = (RelativeLayout) view.findViewById(R.id.leftMessage);
            viewHolder.rightMessage = (RelativeLayout) view.findViewById(R.id.rightMessage);
            viewHolder.leftPanel = (RelativeLayout) view.findViewById(R.id.leftPanel);
            viewHolder.rightPanel = (RelativeLayout) view.findViewById(R.id.rightPanel);
            viewHolder.sending = (ProgressBar) view.findViewById(R.id.sending);
            viewHolder.error = (ImageView) view.findViewById(R.id.sendError);
            viewHolder.sender = (TextView) view.findViewById(R.id.sender);
            viewHolder.rightDesc = (TextView) view.findViewById(R.id.rightDesc);
            viewHolder.systemMessage = (TextView) view.findViewById(R.id.systemMessage);

//            viewHolder.leftLayout = (RelativeLayout) view.findViewById(R.id.chat_left_layout);
            viewHolder.leftCircleImg = (CircleImageView) view.findViewById(R.id.chat_left_circle_img);
            viewHolder.leftShortNameText = (TextView) view.findViewById(R.id.chat_left_short_name);
//            viewHolder.leftLayout = (RelativeLayout) view.findViewById(R.id.chat_right_layout);
            viewHolder.rightCircleImg = (CircleImageView) view.findViewById(R.id.chat_right_circle_img);
            viewHolder.rightShortNameText = (TextView) view.findViewById(R.id.chat_right_short_name);
            viewHolder.isReadMessageText = (TextView) view.findViewById(R.id.message_is_read);
            view.setTag(viewHolder);
        }
        if (position < getCount()) {
            final Message data = getItem(position);

            data.showMessage(viewHolder, getContext());
            setMsgPublic(data, viewHolder);
        }
        return view;
    }

    private void setMsgPublic(final Message data, final ViewHolder viewHolder) {
        TIMUserProfile profile = data.getMessage().getSenderProfile();
        String faceUrl = "";
        String nickName = "";
        final String identifier;
        if (profile != null) {
            faceUrl = profile.getFaceUrl();
            nickName = profile.getNickName();
            identifier = profile.getIdentifier();
        } else {
            identifier = data.getSender();
        }

        Log.e(TAG, "nickname:" + nickName);

        viewHolder.isReadMessageText.setVisibility(View.GONE);
        viewHolder.isReadMessageText.setTag(data);

        nickName = handleName(identifier, nickName);
        if (data.isSelf()) {
//            if (TextUtils.isEmpty(faceUrl)) {
            TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onSuccess(TIMUserProfile timUserProfile) {
                    if (timUserProfile != null) {
                        if (viewHolder.isReadMessageText.getTag().equals(data)) {
                            String selfFaceUrl = timUserProfile.getFaceUrl();
                            String selfNickName = timUserProfile.getNickName();
                            selfNickName = handleName(identifier, selfNickName);
                            ViewUtil.setColorItemView(selfFaceUrl, selfNickName, viewHolder.rightShortNameText, viewHolder.rightCircleImg);
                        }
                    }
                }
            });
//            } else {
//                ViewUtil.setColorItemView(faceUrl, nickName, viewHolder.rightShortNameText, viewHolder.rightCircleImg);
//            }
        } else {
            if (!TextUtils.isEmpty(identifier)) {
                TIMFriendshipManager.getInstance().getUsersProfile(Collections.singletonList(identifier), new TIMValueCallBack<List<TIMUserProfile>>() {
                    @Override
                    public void onError(int i, String s) {
                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                        if (timUserProfiles != null && timUserProfiles.size() > 0) {
                            TIMUserProfile timUserProfile = timUserProfiles.get(0);
                            if (viewHolder.isReadMessageText.getTag().equals(data)) {
                                String faceUrl = timUserProfile.getFaceUrl();
                                String nickName = timUserProfile.getNickName();
                                nickName = handleName(identifier, nickName);
                                ViewUtil.setColorItemView(faceUrl, nickName, viewHolder.leftShortNameText, viewHolder.leftCircleImg);
                            }
                        }
                    }
                });
            } else {
                ViewUtil.setColorItemView(faceUrl, nickName, viewHolder.leftShortNameText, viewHolder.leftCircleImg);
            }
        }

        if (data.isSelf()) {
            viewHolder.rightCircleImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(identifier)) {
//                    if (FriendshipInfo.getInstance().isFriend(identifier)) {
//                        ProfileActivity.navToProfile(getContext(), identifier);
//                    } else {
                        SkipContactDetailActivityUtil.skip(getContext(), identifier);
//                    }
                    }
                }
            });
        } else {
            viewHolder.leftCircleImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(identifier)) {
//                    if (FriendshipInfo.getInstance().isFriend(identifier)) {
//                        ProfileActivity.navToProfile(getContext(), identifier);
//                    } else {
                        SkipContactDetailActivityUtil.skip(getContext(), identifier);
//                    }
                    }
                }
            });
        }
    }

    private boolean isParent(String identifier) {
        if (!TextUtils.isEmpty(identifier) && identifier.startsWith("p")) {
            return true;
        }
        return false;
    }

    private String handleName(String identifier, String nickname) {
        if (isParent(identifier) && !TextUtils.isEmpty(nickname) && nickname.length() > 0) {
            nickname = nickname.substring(0, nickname.length() - 1);
            if (!TextUtils.isEmpty(nickname)) {
                String[] split = nickname.split("\\(");
                if (split.length > 1) {
                    nickname = split[1];
                }
                return nickname;
            }
        }
        return nickname;
    }


    public class ViewHolder {
        public RelativeLayout leftMessage;
        public RelativeLayout rightMessage;
        public RelativeLayout leftPanel;
        public RelativeLayout rightPanel;
        public ProgressBar sending;
        public ImageView error;
        public TextView sender;
        public TextView systemMessage;
        public TextView rightDesc;

        //        public RelativeLayout leftLayout;
        public CircleImageView leftCircleImg;
        public TextView leftShortNameText;
        //        public RelativeLayout rightLayout;
        public CircleImageView rightCircleImg;
        public TextView rightShortNameText;
        public TextView isReadMessageText;
    }
}
