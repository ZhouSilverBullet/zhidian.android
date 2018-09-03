package com.sdxxtop.zhidian.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sdxxtop.zhidian.R;

public class ParentDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_detail);
    }

    public static void startParentDetailActivity(Context context) {
        Intent intent = new Intent(context, ParentDetailActivity.class);
        context.startActivity(intent);
    }
}
