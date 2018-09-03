package com.tencent.qcloud.timchat.utils;

import com.tencent.qcloud.timchat.model.Conversation;
import com.tencent.qcloud.timchat.model.FriendshipConversation;
import com.tencent.qcloud.timchat.model.GroupManageConversation;
import com.tencent.qcloud.timchat.model.NomalConversation;
import com.tencent.qcloud.timchat.model.UserInfo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/3.
 */

public class ConversationSortUtil {
    public static void sortForPlaceTop(List<Conversation> conversationList) {
        String selfIdentify = UserInfo.getInstance().getId();
        LinkedList<Conversation> tempList = new LinkedList<>(conversationList);
        LinkedList<Conversation> list = new LinkedList<>();

        List<String> topIdentify = PlacedTopUtil.getTopIdentify(selfIdentify);
        if (topIdentify.size() == 0) {
            for (Conversation conversation : tempList) {
                if (conversation instanceof FriendshipConversation) {
                    conversationList.remove(conversation);
                }
            }
        } else {
            for (String s : topIdentify) {
                for (Conversation conversation : tempList) {
                    if (conversation instanceof NomalConversation || conversation instanceof GroupManageConversation) {
                        String identify = conversation.getIdentify();
                        if (s.equals(identify)) {
                            list.add(conversation);
                            conversation.setTop(true);
                            conversationList.remove(conversation);
                        }
                    } else {
                        conversationList.remove(conversation);
                    }
                }
            }
        }

        //剩余的都不是置顶，防止置顶被取消后，isTop的值还是true
        for (Conversation conversation : conversationList) {
            conversation.setTop(false);
        }

        Collections.sort(conversationList);
        //再跟时间排一次序
        Collections.sort(list);
        conversationList.addAll(0, list);
    }
}
