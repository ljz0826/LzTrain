package com.lz.lztrain.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.lz.lztrain.interfaces.IWindow;
import com.lz.lztrain.utils.TrainApplication;

public abstract class BaseFragment extends Fragment implements IWindow {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void showProgress() {
        TrainApplication.getCoreApplication().showProgressDialog(getActivity());
    }

    @Override
    public void hideProgress() {
        TrainApplication.getCoreApplication().dismissProgressDialog();
    }

}
