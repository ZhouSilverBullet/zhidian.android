package com.tencent.qcloud.timchat.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.tencent.qcloud.timchat.MyApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表情工具
 */
public class EmoticonUtil {

    public static String[] emoticonData = {
            "[惊讶]", "[撇嘴]", "[色]", "[发呆]", "[得意]", "[流泪]", "[害羞]",
            "[闭嘴]", "[睡]", "[大哭]", "[尴尬]", "[发怒]", "[调皮]", "[呲牙]",
            "[微笑]", "[难过]", "[酷]", "[冷汗]", "[抓狂]", "[吐]", "[偷笑]",
            "[可爱]", "[白眼]", "[傲慢]", "[饥饿]", "[困]", "[惊恐]", "[流汗]",
            "[憨笑]", "[大兵]", "[奋斗]", "[咒骂]", "[疑问]", "[嘘]", "[晕]",
    };

    public static SpannableString getEmotionSpannable(String value) {
        Context context = MyApplication.getContext();
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        SpannableString spannableString = new SpannableString(value);
        List<String> emotionList = Arrays.asList(EmoticonUtil.emoticonData);
        try {
            dealExpression(context, spannableString, pattern, emotionList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return spannableString;
    }

    private static void dealExpression(Context context,
                                SpannableString spannableString, Pattern patten, List<String> emotionList, int start)
            throws Exception {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            // 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
            if (matcher.start() < start) {
                continue;
            }
            if (TextUtils.isEmpty(key)) {
                continue;
            }
            AssetManager am = context.getAssets();
            InputStream is = null;
            int indexOf = emotionList.indexOf(key);
            if (indexOf == -1) {
                continue;
            }
            int end = matcher.start() + key.length();
            try {
                is = am.open(String.format("emoticon/%d.gif", indexOf));
                if (is == null) continue;
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                Matrix matrix = new Matrix();
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                matrix.postScale(0.6f, 0.6f);
                Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        width, height, matrix, true);
                ImageSpan imageSpan = new ImageSpan(context, resizedBitmap, ImageSpan.ALIGN_BOTTOM);

                spannableString.setSpan(imageSpan, matcher.start(), end,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (end < spannableString.length()) {
                // 如果整个字符串还未验证完，则继续。。
                dealExpression(context, spannableString, patten, emotionList, end);
            }
            break;
        }
    }
}
