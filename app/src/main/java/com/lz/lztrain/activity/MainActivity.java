package com.lz.lztrain.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lz.lztrain.R;
import com.lz.lztrain.adapter.MyFragmentAdapter;
import com.lz.lztrain.bean.FragmentEntity;
import com.lz.lztrain.fragment.UploadFragment;
import com.lz.lztrain.fragment.ZxingFragment;
import com.lz.lztrain.utils.PrefsUtil;
import com.lz.mylibrary.BaseActivity;
import com.lz.mylibrary.HackyViewPager;
import com.lz.mylibrary.TrainToast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private final static int MATERIAL_LENGTH = 12;
    public SlidingTabLayout mTabLayout;
    public static DrawerLayout mDrawerLayout;
    private HackyViewPager mViewPager;
    private long exitTime = 0;
    private final static int REQUEST_CODE_CAMERA_IMG = 101;
    private final static int REQUEST_CODE_CAMERA_ZXING = 102;
    List<FragmentEntity> fragmentEntities = new ArrayList<>();
    private Menu mMenu;
//    private StuffEntity stuffEntity;

    private String name, number, position, department;

    //slide
    private TextView tv_name, tv_position, tv_department, tv_num;

    //unlogin
    private Button unlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        fillView();
        addListener();
    }

    private void initView() {
        if (getIntent() != null) {
//            stuffEntity = (StuffEntity) getIntent().getSerializableExtra("stuffEntity");
            name = getIntent().getStringExtra("name");
            number = getIntent().getStringExtra("number");
            position = getIntent().getStringExtra("position");
            department = getIntent().getStringExtra("department");
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (SlidingTabLayout) findViewById(R.id.tab_layout);
        ////// TODO: 2017/8/14
        mToolbar.inflateMenu(R.menu.menu_main);
        mMenu = mToolbar.getMenu();
        mViewPager = (HackyViewPager) findViewById(R.id.viewPager);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        unlogin = (Button) findViewById(R.id.bt_unlogin);
        //slide
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_position = (TextView) findViewById(R.id.tv_position);
        tv_department = (TextView) findViewById(R.id.tv_department);
        tv_num = (TextView) findViewById(R.id.tv_num);
    }

    private void fillView() {

        FragmentEntity fe1 = new FragmentEntity();
        fe1.setFragmentLabel("1");
        ZxingFragment zxingFragment = ZxingFragment.getInstance();
        fe1.setmFragment(zxingFragment);
        fragmentEntities.add(fe1);
        FragmentEntity fe2 = new FragmentEntity();
        fe2.setFragmentLabel("2");
        UploadFragment uploadFragment = UploadFragment.getInstance();
        fe2.setmFragment(uploadFragment);
        fragmentEntities.add(fe2);

        MyFragmentAdapter fragmentAdapter = new MyFragmentAdapter(MainActivity.this, fragmentEntities);
        mViewPager.setAdapter(fragmentAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setLocked(false);
        String[] mTitles = {getString(R.string.zxing_fragment), getString(R.string.pic_fragment)};
        mTabLayout.setViewPager(mViewPager, mTitles);

        tv_name.setText(name);
        tv_num.setText(number);
        tv_department.setText(department);
        tv_position.setText(position);

    }

    private void addListener() {
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position, true);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
                if(position==0){
                    for (int i = 0; i < mMenu.size(); i++){
                        mMenu.getItem(i).setVisible(true);
                        mMenu.getItem(i).setEnabled(true);
                    }
                }else{
                    for (int i = 0; i < mMenu.size(); i++){
                        mMenu.getItem(i).setVisible(false);
                        mMenu.getItem(i).setEnabled(false);
                    }
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_zxing) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_ZXING);
                    } else {
                        ZxingFragment zxingFragment = (ZxingFragment) fragmentEntities.get(0).getmFragment();
                        zxingFragment.startZxing();
//                        startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class),0);
                    }
                }
                if (item.getItemId() == R.id.action_write) {
                    showWirteWindow(unlogin);
                }
                return true;
            }
        });

        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_top_menu));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        unlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupwindow(unlogin);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CAMERA_ZXING:
                startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class), 0);
                return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void showPopupwindow(View parent) {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        LinearLayout layout = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog, null);
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
//        params.dimAmount=0.5f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = MainActivity.this.getWindow().getAttributes();
//                params.dimAmount=1.0f;
                params.alpha = 1f;
                MainActivity.this.getWindow().setAttributes(params);
            }
        });
        TextView textView_yes = (TextView) layout.findViewById(R.id.yes);
        TextView textView_no = (TextView) layout.findViewById(R.id.no);
        textView_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefsUtil.getPrefs().setString(PrefsUtil.SP_NAME, "");
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
        textView_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void showWirteWindow(View parent) {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        LinearLayout layout = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_write, null);
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
//        params.dimAmount=0.5f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = MainActivity.this.getWindow().getAttributes();
//                params.dimAmount=1.0f;
                params.alpha = 1f;
                MainActivity.this.getWindow().setAttributes(params);
            }
        });
        TextView textView_yes = (TextView) layout.findViewById(R.id.yes);
        TextView textView_no = (TextView) layout.findViewById(R.id.no);
        final EditText write_num = (EditText) layout.findViewById(R.id.write_num);
        textView_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(write_num.getText().toString().trim().length()!=MATERIAL_LENGTH){
                    TrainToast.makeText(MainActivity.this,getResources().getString(R.string.error_material),Toast.LENGTH_SHORT).show();
                    return;
                }
                popupWindow.dismiss();
                ZxingFragment zxingFragment = (ZxingFragment) fragmentEntities.get(0).getmFragment();
                zxingFragment.getMaterialMessage(write_num.getText().toString().trim());
            }
        });
        textView_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("zhou","ffffffffffffffffuck");
    }
}
