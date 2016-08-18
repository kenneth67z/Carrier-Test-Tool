package com.carriertesttool.impls;

import android.content.Context;
import android.widget.TextView;
import com.atcommandtool.com.atcommandtool.R;
import com.carriertesttool.toolshellcommand.IShellCmdSend;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Admin on 2016/8/18.
 */
public class ShellCmdCallBackImpl implements IShellCmdSend {
    private TextView mTextViewResult = null;
    private Context mContext = null;

    public ShellCmdCallBackImpl(Context context)
    {
        mContext = context;
    }

    public void setTextViewItem(TextView textView)
    {
        mTextViewResult = textView;
    }

    /**
     *  Implement the SHELL Command Send behavior.
     *  @returns boolean true for success, false for failed.
     */
    @Override
    public String ShellCmdSend(String ShellCmd)
    {
        StringBuffer output = new StringBuffer();
        String response;
        Process process;

        try {
            process = Runtime.getRuntime().exec(ShellCmd);
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
     *  Implement the SHELL Command Send complete behavior.
     *  @returns void
     */
    @Override
    public void ShellCommandSendComplete(String[] outputData)
    {
        if(null != mTextViewResult)
        {
            StringBuilder outputStr = new StringBuilder();

            if(null != mContext)
            {
                outputStr.append(mContext.getResources().getText(R.string.shell_cmd_send_complete_msg))
                        .append("\n")
                        .append(mContext.getResources().getText(R.string.shell_cmd_string))
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
     *  Implement the SHELL Command Send failed behavior.
     *  @returns void
     */
    @Override
    public void ShellCommandSendError(String errorMsg)
    {
        if(null != mTextViewResult)
        {
            mTextViewResult.setText("AT Command send Failed!!!\n");
            mTextViewResult.setText(errorMsg);
        }
    }
}
