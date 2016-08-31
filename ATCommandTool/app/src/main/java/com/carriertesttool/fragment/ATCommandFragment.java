package com.carriertesttool.fragment;

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

import com.carriertesttool.impls.ATCmdCallBackImpl;
import com.carriertesttool.toolatcommand.ATCommandSend;
import com.atcommandtool.com.atcommandtool.R;
import com.carriertesttool.util.FragmentBase;

/**
 * Created by Admin on 2016/8/17.
 */
public class ATCommandFragment extends FragmentBase {
    private ATCmdTxtOnClickListener mTextClickListener = null;
    private ATCommandSend mATCmdSend = ATCommandSend.getInstance();
    private ATCmdCallBackImpl mATCmdCbk = null;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mATCmdSend.release();
        mTextClickListener = null;
        mATCmdCbk = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView(inflater, container, R.layout.atcommand_tool);
        Context context = getContext();

        mTextClickListener = new ATCmdTxtOnClickListener(context);

        // add AT Command Interface to fragment
        mATCmdCbk = new ATCmdCallBackImpl(context);
        if(mATCmdSend == null)
        {
            mATCmdSend = ATCommandSend.getInstance();
        }
        mATCmdSend.init();
        mATCmdSend.setCallback(mATCmdCbk);    // set the AT Command implement call back function
        mATCmdSend.setContext(context);        // it need context instance to get app resource

        // EditText widget for typing the AT Command
        EditText atCmdText = (EditText)view.findViewById(R.id.at_cmd_msg);
        TextView textView = (TextView)view.findViewById(R.id.at_text_result);
        mTextClickListener.setEditText(atCmdText);
        mTextClickListener.setTextView(textView);
        atCmdText.setText(context.getString(R.string.at_cmd_prefix));
        atCmdText.setOnClickListener(mTextClickListener);

        // Button widget for sending a specify AT Command
        Button btn = (Button)view.findViewById(R.id.at_send);
        btn.setOnClickListener(mTextClickListener);

        // To show the result for each sent AT Commands
        mATCmdCbk.setTextViewItem(textView);

        return view;
    }

    private class ATCmdTxtOnClickListener implements View.OnClickListener
    {
        private EditText mEditText = null;
        private TextView mTextView = null;
        private Context mContext = null;

        public ATCmdTxtOnClickListener(Context context)
        {
            mContext = context;
        }

        public void setEditText(EditText txt)
        {
            mEditText = txt;
        }
        public void setTextView(TextView txt)
        {
            mTextView = txt;
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
                if(null != mEditText)
                {
                    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

                    mATCmdSend.setProcessATCommand(mEditText.getText().toString());
                    mATCmdSend.send();
                    mEditText.clearFocus();
                    mEditText.setText(mContext.getString(R.string.at_cmd_prefix));

                    mTextView.setText(mContext.getResources().getString(R.string.please_wait_msg));
                    imm.hideSoftInputFromWindow(mEditText.getWindowToken(),0);
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