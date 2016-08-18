package com.carriertesttool.toolshellcommand;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.atcommandtool.com.atcommandtool.R;

/**
 * Created by Admin on 2016/8/18.
 */
public class ShellCommandSend {
    public static final String TAG_SHELL_COMMAND = "SHELL_CMD";
    public static final String KEY_SHELL_COMMAND = "SHELL_CMD";
    public static final String KET_SHELL_RESUIT_MESSAGE = "SHELL_RESULT_MSG";
    private static final int MESSAGE_ID_SHELL_COMMAND_SEND_COMPLETE    = 0x390;
    private static final int MESSAGE_ID_SHELL_COMMAND_SEND_ERROR        = 0x399;

    private static ShellCommandSend mInstance = new ShellCommandSend();
    private static IShellCmdSend mCmdCbk = null;
    private ShellCommandMessageHandler mMsgHandler = null;
    private String mShellCmd = null;
    private Context mContext = null;

    private ShellCommandSend() {}

    public static ShellCommandSend getInstance()
    {
        if(null == mInstance)
        {
            mInstance = new ShellCommandSend();
        }
        return mInstance;
    }

    public void init()
    {
        mMsgHandler = new ShellCommandMessageHandler();
    }

    public void setContext(Context context)
    {
        mContext = context;
        if(null != mMsgHandler)
            mMsgHandler.setContext(context);
    }

    public void setCallback(IShellCmdSend cmdCallback)
    {
        mCmdCbk = cmdCallback;
    }

    public void setProcessShellCommand(String shellCmd)
    {
        mShellCmd = shellCmd;
    }

    public void send()
    {
        if(null == mShellCmd)
        {
            String errorMsg = mContext.getResources().getString(R.string.shell_cmd_para_is_null);
            throw new NullPointerException(errorMsg);
        }
        else if(mShellCmd.length() == 0)
        {
            String errorMsg = mContext.getResources().getString(R.string.shell_cmd_msg_length_is_null);
            Log.e(TAG_SHELL_COMMAND, errorMsg);
            Message msg;

            msg = mMsgHandler.obtainMessage(MESSAGE_ID_SHELL_COMMAND_SEND_ERROR, errorMsg);
            mMsgHandler.sendMessage(msg);

            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = false;
                String outputStr = "";
                Message msg;

                if(null != mCmdCbk)
                {
                    outputStr = mCmdCbk.ShellCmdSend(mShellCmd);
                    if(null != outputStr)
                        success = true;
                }

                if(success)
                {
                    Bundle outputData = new Bundle();

                    outputData.putString(KEY_SHELL_COMMAND, mShellCmd);
                    outputData.putString(KET_SHELL_RESUIT_MESSAGE, outputStr);
                    msg = mMsgHandler.obtainMessage(MESSAGE_ID_SHELL_COMMAND_SEND_COMPLETE, outputData);

                }
                else
                {
                    msg = new Message();
                    msg.what = MESSAGE_ID_SHELL_COMMAND_SEND_ERROR;
                }
                mMsgHandler.sendMessage(msg);
            }
        }).start();
    }

    public void release()
    {
        mInstance = null;
        mCmdCbk = null;
        mMsgHandler = null;
        mShellCmd = null;
        mContext = null;
    }

    private static class ShellCommandMessageHandler extends Handler
    {
        private Context mContext = null;

        public void setContext(Context context)
        {
            mContext = context;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(null == mCmdCbk)
            {
                throw new NullPointerException(mContext.getResources().getString(R.string.shell_cmd_cbk_para_is_null));
            }

            switch(msg.what)
            {
                case MESSAGE_ID_SHELL_COMMAND_SEND_COMPLETE:
                    Bundle outputData = (Bundle)msg.obj;

                    String outputStr[] = {outputData.getString(KEY_SHELL_COMMAND, mContext.getResources().getString(R.string.no_cmd)),
                            outputData.getString(KET_SHELL_RESUIT_MESSAGE, mContext.getResources().getString(R.string.no_execute_msg))};

                    mCmdCbk.ShellCommandSendComplete(outputStr);
                    break;

                case MESSAGE_ID_SHELL_COMMAND_SEND_ERROR:
                    String errorMsgStr = (String)msg.obj;

                    mCmdCbk.ShellCommandSendError(errorMsgStr);
                    break;

                default:
                    break;
            }
        }
    }
}
