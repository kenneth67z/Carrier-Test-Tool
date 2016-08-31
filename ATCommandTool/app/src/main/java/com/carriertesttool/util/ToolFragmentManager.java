package com.carriertesttool.util;

import android.app.Fragment;
import android.content.Context;

/**
 * Created by Admin on 2016/8/31.
 */
public class ToolFragmentManager {
    private static ToolFragmentManager mInstance = new ToolFragmentManager();
    private ToolFragmentFactory mFragmentFactory = new ToolFragmentFactory();
    private Context mContext = null;

    private ToolFragmentManager()
    {}

    static public ToolFragmentManager getInstance()
    {
        if(null == mInstance)
        {
            mInstance = new ToolFragmentManager();
        }
        return mInstance;
    }

    public void setContext(Context context)
    {
        mContext = context;
    }

    public Fragment getFragment(ToolFragmentFactory.TOOL_TYPE type)
    {
        FragmentBase fragment = mFragmentFactory.getFragment(type);
        fragment.setContext(mContext);
        return fragment;
    }

    public void release()
    {
        mFragmentFactory = null;
        mContext = null;
        mInstance = null;
    }
}
