package com.carriertesttool.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.atcommandtool.com.atcommandtool.R;
import com.carriertesttool.impls.ShellCmdCallBackImpl;
import com.carriertesttool.toolshellcommand.ShellCommandSend;
import com.carriertesttool.util.FragmentBase;

/**
 * Created by Admin on 2016/8/18.
 */
public class ShellCommandFragment extends FragmentBase {
    private ShellCmdTxtOnClickListener mTextClickListener = null;
    private ShellCommandSend mShellCmdSend = ShellCommandSend.getInstance();
    private ShellCmdCallBackImpl mShellCmdCbk = null;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mShellCmdSend.release();
        mTextClickListener = null;
        mShellCmdCbk = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView(inflater, container, R.layout.shell_command_tool);
        Context context = getContext();
        mTextClickListener = new ShellCmdTxtOnClickListener(context);

        // add Shell Command Interface to fragment
        mShellCmdCbk = new ShellCmdCallBackImpl(context);
        if(mShellCmdSend == null)
        {
            mShellCmdSend = ShellCommandSend.getInstance();
        }
        mShellCmdSend.init();
        mShellCmdSend.setCallback(mShellCmdCbk);    // set the Shell Command implement call back function
        mShellCmdSend.setContext(context);        // it need context instance to get app resource

        // EditText widget for typing the Shell Command
        EditText shellCmdText = (EditText)view.findViewById(R.id.shell_cmd_msg);
        mTextClickListener.setEditText(shellCmdText);
        shellCmdText.setOnClickListener(mTextClickListener);

        // Button widget for sending a specify SHELL Command
        Button btn = (Button)view.findViewById(R.id.shell_send);
        btn.setOnClickListener(mTextClickListener);

        // To show the result for each sent SHELL Commands
        TextView textView = (TextView)view.findViewById(R.id.shell_text_result);
        mShellCmdCbk.setTextViewItem(textView);

        return view;
    }

    private class ShellCmdTxtOnClickListener implements View.OnClickListener
    {
        private EditText mTtxt = null;
        private Context mContext = null;

        public ShellCmdTxtOnClickListener(Context context)
        {
            mContext = context;
        }

        public void setEditText(EditText txt)
        {
            mTtxt = txt;
        }

        @Override
        public void onClick(View view) {
            EditText shellCmdText;
            if(view instanceof EditText)
            {
                shellCmdText = (EditText)view;
                shellCmdText.setSelectAllOnFocus(true);
            }
            else if(view instanceof Button)
            {
                if(null != mTtxt)
                {
                    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

                    mShellCmdSend.setProcessShellCommand(mTtxt.getText().toString());
                    mShellCmdSend.send();
                    mTtxt.clearFocus();
                    mTtxt.setText("");
                    imm.hideSoftInputFromWindow(mTtxt.getWindowToken(),0);
                }
                else
                {
                    String error = mContext.getResources().getString(R.string.shell_cmd_is_null);
                    Log.e(ShellCommandSend.TAG_SHELL_COMMAND, error);
                }
            }
        }
    }
}