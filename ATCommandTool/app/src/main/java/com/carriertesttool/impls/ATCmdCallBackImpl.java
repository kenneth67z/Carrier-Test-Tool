package com.carriertesttool.impls;

import android.content.Context;
import android.widget.TextView;

import com.carriertesttool.toolatcommand.IATCmdSend;
import com.atcommandtool.com.atcommandtool.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
    public String ATCmdSend(String ATCmd)
    {
        StringBuffer output = new StringBuffer();
        String response;
        Process process;

        try {
            process = Runtime.getRuntime().exec(ATCmd);
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        response = output.toString();


        return response;
    }

    /**
        *  Implement the AT Command Send complete behavior.
         *  @returns void
        */
    @Override
    public void ATCommandSendComplete(String[] outputData)
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
                .append(outputData[0])
                .append("\n\n")
                .append("Command result:\n\n")
                .append(outputData[1]);
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
