package com.atcommandtool.com.atcommandtool.impls;

import android.content.Context;
import android.widget.TextView;

import com.atcommandtool.com.atcommandtool.R;
import com.atcommandtool.com.atcommandtool.atcommand.IATCmdSend;

/**
 * Created by Admin on 2016/8/17.
 */
public class ATCmdCallBackImpl implements IATCmdSend
{
    private TextView mTextViewResult = null;
    private Context mContext = null;

    public ATCmdCallBackImpl(Context context)
    {
        mContext = context;
    }

    public void setTextViewItem(TextView textView)
    {
        mTextViewResult = textView;
    }

    /**
        *  Implement the AT Command Send behavior.
        *  @returns boolean true for success, false for failed.
        */
    @Override
    public boolean ATCmdSend(String ATCmd)
    {
        return true;
    }

    /**
        *  Implement the AT Command Send complete behavior.
         *  @returns void
        */
    @Override
    public void ATCommandSendComplete(String ATCmd)
    {
        if(null != mTextViewResult)
        {
            StringBuilder outputStr = new StringBuilder();

            if(null != mContext)
            {
                outputStr.append(mContext.getResources().getText(R.string.at_cmd_send_complete_msg))
                .append("\n")
                .append(mContext.getResources().getText(R.string.at_cmd_string))
                .append(" ")
                .append(ATCmd);
            }

            mTextViewResult.setText(outputStr);
        }
    }

    /**
        *  Implement the AT Command Send failed behavior.
        *  @returns void
        */
    @Override
    public void ATCommandSendError(String errorMsg)
    {
        if(null != mTextViewResult)
        {
            mTextViewResult.setText("AT Command send Failed!!!\n");
            mTextViewResult.setText(errorMsg);
        }
    }
}
