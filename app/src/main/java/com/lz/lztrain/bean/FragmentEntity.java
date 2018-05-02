package com.lz.lztrain.bean;

import android.support.v4.app.Fragment;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * Created by RavenTian on 2016/5/25. Fragment实体类
 */
public class FragmentEntity implements CustomTabEntity {
    private Fragment mFragment;
    private String fragmentLabel;
    private String title;
    private int selectedIcon;
    private int unSelectedIcon;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FragmentEntity() {

    }

    public FragmentEntity(String title) {
        this.title = title;
    }

    public FragmentEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    public Fragment getmFragment() {
        return mFragment;
    }


    public void setmFragment(Fragment mFragment) {
        this.mFragment = mFragment;
    }

    public String getFragmentLabel() {
        return fragmentLabel;
    }

    public void setFragmentLabel(String fragmentLabel) {
        this.fragmentLabel = fragmentLabel;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}
