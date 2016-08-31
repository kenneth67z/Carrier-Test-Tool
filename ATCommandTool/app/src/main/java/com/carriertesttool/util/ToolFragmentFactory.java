package com.carriertesttool.util;

import android.app.Fragment;

import com.carriertesttool.fragment.ATCommandFragment;
import com.carriertesttool.fragment.AutoExePhoneFuncFragment;
import com.carriertesttool.fragment.MainFragment;
import com.carriertesttool.fragment.ShellCommandFragment;

/**
 * Created by Admin on 2016/8/31.
 */
public class ToolFragmentFactory {
    public enum TOOL_TYPE
    {
        TOOL_AT_COMMAND,
        TOOL_SHELL_COMMAND,
        TOOL_AUTO_EXE_PHONE_FUNCTION,
        TOOL_MAIN_PAGE
    };

    protected FragmentBase getFragment(TOOL_TYPE type)
    {
        FragmentBase fragment = null;
        switch(type)
        {
            case TOOL_MAIN_PAGE:
                fragment = new MainFragment();
                break;

            case TOOL_AT_COMMAND:
                fragment = new ATCommandFragment();
                break;

            case TOOL_SHELL_COMMAND:
                fragment = new ShellCommandFragment();
                break;

            case TOOL_AUTO_EXE_PHONE_FUNCTION:
                fragment = new AutoExePhoneFuncFragment();
                break;

            default:
                break;
        }
        return fragment;
    }
}
