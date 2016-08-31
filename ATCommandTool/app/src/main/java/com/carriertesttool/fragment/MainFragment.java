package com.carriertesttool.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.atcommandtool.com.atcommandtool.R;
import com.carriertesttool.util.FragmentBase;

/**
 * Created by Admin on 2016/8/17.
 */
public class MainFragment extends FragmentBase {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater, container, R.layout.main_view);
    }
}
