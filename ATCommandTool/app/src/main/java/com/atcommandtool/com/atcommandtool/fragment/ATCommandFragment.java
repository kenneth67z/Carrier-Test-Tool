package com.atcommandtool.com.atcommandtool.fragment;

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
import com.atcommandtool.com.atcommandtool.atcommand.ATCommandSend;
import com.atcommandtool.com.atcommandtool.impls.ATCmdCallBackImpl;
/**
 * Created by Admin on 2016/8/17.
 */
public class ATCommandFragment extends Fragment {
    private ATCmdTxtOnClickListener mTextClickListener = new ATCmdTxtOnClickListener();
    private ATCommandSend mATCmdSend = ATCommandSend.getInstance();
    private ATCmdCallBackImpl mATCmdCbk = null;
    private Context mContext = null;

    public void setContext(Context context)
    {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView(inflater, container);

        // add AT Command Interface to fragment
        mATCmdCbk = new ATCmdCallBackImpl(mContext);
        if(mATCmdSend == null)
        {
            mATCmdSend = ATCommandSend.getInstance();
        }
        mATCmdSend.setCallback(mATCmdCbk);    // set the AT Command implement call back function
        mATCmdSend.setContext(mContext);        // it need context instance to get app resource

        // EditText widget for typing the AT Command
        EditText atCmdText = (EditText)view.findViewById(R.id.at_cmd_msg);
        mTextClickListener.setEditText(atCmdText);
        atCmdText.setOnClickListener(mTextClickListener);

        // Button widget for sending a specify AT Command
        Button btn = (Button)view.findViewById(R.id.at_send);
        btn.setOnClickListener(mTextClickListener);

        // To show the result for each sent AT Commands
        TextView textView = (TextView)view.findViewById(R.id.text_result);
        mATCmdCbk.setTextViewItem(textView);

        return view;
    }

    private View initView(LayoutInflater inflater, ViewGroup container)
    {
        View view = inflater.inflate(R.layout.atcommand_tool, container, false);
        return view;
    }

    private class ATCmdTxtOnClickListener implements View.OnClickListener
    {
        private EditText mTtxt = null;
        public ATCmdTxtOnClickListener()
        {
        }

        public void setEditText(EditText txt)
        {
            mTtxt = txt;
        }

        @Override
        public void onClick(View view) {
            EditText atCmdText;
            if(view instanceof EditText)
            {
                atCmdText = (EditText)view;
                atCmdText.setSelectAllOnFocus(true);
            }
            else if(view instanceof Button)
            {
                if(null != mTtxt)
                {
                    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

                    mATCmdSend.setProcessATCommand(mTtxt.getText().toString());
                    mATCmdSend.send();
                    mTtxt.clearFocus();
                    imm.hideSoftInputFromWindow(mTtxt.getWindowToken(),0);
                }
                else
                {
                    String error = mContext.getResources().getString(R.string.at_cmd_is_null);
                    Log.e(ATCommandSend.TAG_AT_COMMAND, error);
                }
            }
        }
    }
}