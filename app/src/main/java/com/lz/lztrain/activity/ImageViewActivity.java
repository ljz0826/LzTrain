package com.lz.lztrain.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.lz.lztrain.R;
import com.lz.mylibrary.BaseActivity;

/**
 * Created by lenovo on 2017/6/30.
 */

public class ImageViewActivity extends BaseActivity {

    private ImageView iv_pic;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);

        uri = Uri.parse(getIntent().getStringExtra("uri"));

        iv_pic = (ImageView) findViewById(R.id.iv_pic);
        iv_pic.setImageURI(uri);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//    }
}
