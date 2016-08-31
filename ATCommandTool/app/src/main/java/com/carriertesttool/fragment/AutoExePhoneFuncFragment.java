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
import com.atcommandtool.com.atcommandtool.R;
import com.carriertesttool.impls.AutoExePhoneFuncImpl;
import com.carriertesttool.toolautoexephonefunc.AutoExePhoneFunc;
import com.carriertesttool.util.FragmentBase;

import java.io.DataOutputStream;
import java.io.OutputStream;

/**
 * Created by Admin on 2016/8/25.
 */
public class AutoExePhoneFuncFragment extends FragmentBase {
    private AutoExePhoneFuncTxtOnClickListener mTextClickListener = null;
    private AutoExePhoneFunc mAutoExePhone = AutoExePhoneFunc.getInstance();
    private AutoExePhoneFuncImpl mAutoExePhoneCbk = null;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAutoExePhone.release();
        mTextClickListener = null;
        mAutoExePhoneCbk = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView(inflater, container, R.layout.auto_exe_phone_func_tool);
        Context context = getContext();
        mTextClickListener = new AutoExePhoneFuncTxtOnClickListener(context);

        // add Shell Command Interface to fragment
        mAutoExePhoneCbk = new AutoExePhoneFuncImpl(context);
        if(mAutoExePhone == null)
        {
            mAutoExePhone = AutoExePhoneFunc.getInstance();
        }
        mAutoExePhone.init();
        mAutoExePhone.setCallback(mAutoExePhoneCbk);    // set the Shell Command implement call back function
        mAutoExePhone.setContext(context);        // it need context instance to get app resource

        // EditText widget for typing the Shell Command
        EditText executeDialCallText = (EditText)view.findViewById(R.id.auto_exe_phone_msg);
        mTextClickListener.setEditText(executeDialCallText);
        executeDialCallText.setOnClickListener(mTextClickListener);

        // Button widget for sending a specify SHELL Command
        Button btn = (Button)view.findViewById(R.id.dial_call_btn);
        btn.setOnClickListener(mTextClickListener);

        // To show the result for each sent SHELL Commands
        //TextView textView = (TextView)view.findViewById(R.id.shell_text_result);
        //mAutoExePhoneCbk.setTextViewItem(textView);

        return view;
    }

    private class AutoExePhoneFuncTxtOnClickListener implements View.OnClickListener
    {
        private EditText mTtxt = null;
        private Context mContext = null;

        public AutoExePhoneFuncTxtOnClickListener(Context context)
        {
            mContext = context;
        }

        public void setEditText(EditText txt)
        {
            mTtxt = txt;
        }

        @Override
        public void onClick(View view) {
            EditText executeDialCallText;
            if(view instanceof EditText)
            {
                executeDialCallText = (EditText)view;
                executeDialCallText.setSelectAllOnFocus(true);
            }
            else if(view instanceof Button)
            {
                if(null != mTtxt)
                {
                    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

                    //mAutoExePhone.setExecutePhoneNumber(mTtxt.getText().toString());
                    //execShellCmd("input keyevent 3");
                    mAutoExePhone.dialPhoneCall(mTtxt.getText().toString());
                    //mAutoExePhone.setExecutePhoneNumber("input keyevent 3");
                    //mAutoExePhone.send();
                    mTtxt.clearFocus();
                    mTtxt.setText("");
                    imm.hideSoftInputFromWindow(mTtxt.getWindowToken(),0);
                }
                else
                {
                    String error = mContext.getResources().getString(R.string.auto_exe_phone_is_null);
                    Log.e(AutoExePhoneFunc.TAG_AUTO_EXECUTE_PHONE_FUNCTION, error);
                }
            }
        }
    }

    private void execShellCmd(String cmd) {

        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
            Log.e(AutoExePhoneFunc.TAG_AUTO_EXECUTE_PHONE_FUNCTION, "Exception!!!");
        }
    }
}
