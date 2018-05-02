package com.lz.lztrain.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.lz.lztrain.bean.FragmentEntity;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> listFragment = new ArrayList<Fragment>();
    private List<String> labels = new ArrayList<String>();
    private List<FragmentEntity> listFragmentEntity = new ArrayList<>();

    public MyFragmentAdapter(FragmentActivity fm, List<FragmentEntity> mlistFragmentEntity) {
        super(fm.getSupportFragmentManager());
        if (mlistFragmentEntity != null) {
            listFragmentEntity = mlistFragmentEntity;
            addFragment();
        }
    }

    public MyFragmentAdapter(FragmentManager fm, List<FragmentEntity> mlistFragmentEntity) {
        super(fm);
        if (mlistFragmentEntity != null) {
            listFragmentEntity = mlistFragmentEntity;
            addFragment();
        }
    }

    @Override
    public Fragment getItem(int arg0) {
        return listFragment.get(arg0);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return labels.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        if (listFragment != null) {
            return listFragment.size();
        }
        return 0;
    }

    public void addFragment() {
        for (int i = 0, size = listFragmentEntity.size(); i < size; i++) {
            addTab(listFragmentEntity.get(i).getmFragment());
            labels.add(listFragmentEntity.get(i).getFragmentLabel());
        }
    }

    public void addTab(Fragment mFragment) {
        listFragment.add(mFragment);
        notifyDataSetChanged();
    }

    public void refresh(List<FragmentEntity> mlistFragmentEntity) {
        clear();
        for (int i = 0, size = mlistFragmentEntity.size(); i < size; i++) {
            listFragment.add(mlistFragmentEntity.get(i).getmFragment());
            labels.add(mlistFragmentEntity.get(i).getFragmentLabel());
        }
        notifyDataSetChanged();
    }

    public void clear() {
        labels.clear();
        listFragment.clear();
    }

}
