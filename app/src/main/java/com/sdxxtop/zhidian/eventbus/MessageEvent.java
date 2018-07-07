package com.sdxxtop.zhidian.eventbus;

import android.graphics.Bitmap;

import java.util.List;

/**
 * MessageEvent事件
 */
public class MessageEvent {

    public static class intentActivity {

        public String stringTitle;
        public String contentName;
        public List<String> contentList;
        public String stringReason;
        public List<Bitmap> photoBitmaps;
        public String stringCopyPerson;
        public String stringMe;
        public String stringApprover;
        public String stringPlace;
        public String stringPlaceName;

        public intentActivity(String stringTitle, String contentName, List<String> contentList, String stringReason, List<Bitmap> photoBitmaps, String stringCopyPerson, String stringMe, String stringApprover) {
            this.stringTitle = stringTitle;
            this.contentName = contentName;
            this.contentList = contentList;
            this.stringReason = stringReason;
            this.photoBitmaps = photoBitmaps;
            this.stringCopyPerson = stringCopyPerson;
            this.stringMe = stringMe;
            this.stringApprover = stringApprover;
        }

        public intentActivity(String stringTitle, String contentName, List<String> contentList, String stringReason, List<Bitmap> photoBitmaps, String stringCopyPerson, String stringMe, String stringApprover,String stringPlace,String stringPlaceName) {
            this.stringTitle = stringTitle;
            this.contentName = contentName;
            this.contentList = contentList;
            this.stringReason = stringReason;
            this.photoBitmaps = photoBitmaps;
            this.stringCopyPerson = stringCopyPerson;
            this.stringMe = stringMe;
            this.stringApprover = stringApprover;
            this.stringPlace = stringPlace;
            this.stringPlaceName = stringPlaceName;
        }
    }

    public static class permissionIsSuccess {

        public boolean permissionIsSuccess;

        public permissionIsSuccess(boolean permissionIsSuccess) {
            this.permissionIsSuccess = permissionIsSuccess;
        }
    }

    public static class approvalSuccess{

        public approvalSuccess() {
        }
    }


}
