package com.carriertesttool.util;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atcommandtool.com.atcommandtool.R;

/**
 * Created by Admin on 2016/8/31.
 */
public class FragmentBase extends Fragment{
    private Context mContext = null;

    public void setContext(Context context)
    {
        mContext = context;
    }

    @Override
    public Context getContext()
    {
        if(null == mContext)
            throw new NullPointerException(getActivity().getResources().getString(R.string.error_context_is_null_msg));

        return mContext;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContext = null;
    }

    protected View initView(LayoutInflater inflater, ViewGroup container, int layoutID)
    {
        return inflater.inflate(layoutID, container, false);
    }
}
