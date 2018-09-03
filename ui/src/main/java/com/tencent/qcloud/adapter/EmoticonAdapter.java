package com.tencent.qcloud.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tencent.qcloud.ui.DpUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2018/8/30.
 */

public class EmoticonAdapter extends PagerAdapter {
    private Context context;

    public EmoticonAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getEmoticonItemView(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public View getEmoticonItemView(int position) {
        int total = position * 5;
        LinearLayout rootLayout = new LinearLayout(context);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = total; i < 5 + total; ++i) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, 1f));
            for (int j = 0; j < 7; ++j) {

                try {
                    AssetManager am = context.getAssets();

                    final int index = 7 * i + j;
                    InputStream is = am.open(String.format("emoticon/%d.gif", index));
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    Matrix matrix = new Matrix();
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    matrix.postScale(0.8f, 0.8f);
                    final Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            width, height, matrix, true);
                    ImageView image = new ImageView(context);
                    image.setImageBitmap(resizedBitmap);
                    image.setLayoutParams(new LinearLayout.LayoutParams(DpUtil.dp2px(context, 24), DpUtil.dp2px(context, 24), 1f));
                    linearLayout.addView(image);
                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String content = String.valueOf(index);
                            SpannableString str = new SpannableString(String.valueOf(index));
                            ImageSpan span = new ImageSpan(context, resizedBitmap, ImageSpan.ALIGN_BOTTOM);
                            str.setSpan(span, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            editText.append(str);
                            if (emoticonSelectListener != null) {
                                emoticonSelectListener.onSelector(str);
                            }
                        }
                    });
                    is.close();
                } catch (IOException e) {

                }
            }
            rootLayout.addView(linearLayout);
        }
        return rootLayout;
    }

    private EmoticonSelectListener emoticonSelectListener;

    public void setEmoticonSelectListener(EmoticonSelectListener emoticonSelectListener) {
        this.emoticonSelectListener = emoticonSelectListener;
    }

    public interface EmoticonSelectListener {
        void onSelector(SpannableString str);
    }
}
