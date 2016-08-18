package com.carriertesttool.toolatcommand;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.atcommandtool.com.atcommandtool.R;

/**
 * Created by Admin on 2016/8/17.
 */
public class ATCommandSend {
    public static final String TAG_AT_COMMAND = "AT_CMD";
    public static final String KEY_AT_COMMAND = "AT_CMD";
    public static final String KET_AT_RESUIT_MESSAGE = "AT_RESULT_MSG";
    private static final int MESSAGE_ID_AT_COMMAND_SEND_COMPLETE = 0x391;
    private static final int MESSAGE_ID_AT_COMMAND_SEND_ERROR     = 0x393;

    private static ATCommandSend mInstance = new ATCommandSend();
    private static IATCmdSend mCmdCbk = null;
    private ATCommandMessageHandler mMsgHandler = null;
    private String mATCmd = null;
    private Context mContext = null;

    private ATCommandSend() {}

    public static ATCommandSend getInstance()
    {
        if(null == mInstance)
        {
            mInstance = new ATCommandSend();
        }
        return mInstance;
    }

    public void init()
    {
        mMsgHandler = new ATCommandMessageHandler();
    }

    public void setContext(Context context)
    {
        mContext = context;
        if(null != mMsgHandler)
            mMsgHandler.setContext(context);
    }

    public void setCallback(IATCmdSend cmdCallback)
    {
        mCmdCbk = cmdCallback;
    }

    public void setProcessATCommand(String ATCmd)
    {
        mATCmd = ATCmd;
    }

    public void send()
    {
        if(null == mATCmd)
        {
            String errorMsg = mContext.getResources().getString(R.string.at_cmd_para_is_null);
            throw new NullPointerException(errorMsg);
        }
        else if(mATCmd.length() == 0)
        {
            String errorMsg = mContext.getResources().getString(R.string.at_cmd_msg_length_is_null);
            Log.e(TAG_AT_COMMAND, errorMsg);
            Message msg;

            msg = mMsgHandler.obtainMessage(MESSAGE_ID_AT_COMMAND_SEND_ERROR, errorMsg);
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
                    outputStr = mCmdCbk.ATCmdSend(mATCmd);
                    if(null != outputStr)
                        success = true;
                }

                if(success)
                {
                    Bundle outputData = new Bundle();

                    outputData.putString(KEY_AT_COMMAND, mATCmd);
                    outputData.putString(KET_AT_RESUIT_MESSAGE, outputStr);
                    msg = mMsgHandler.obtainMessage(MESSAGE_ID_AT_COMMAND_SEND_COMPLETE, outputData);

                }
                else
                {
                    msg = new Message();
                    msg.what = MESSAGE_ID_AT_COMMAND_SEND_ERROR;
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
        mATCmd = null;
        mContext = null;
    }

    private static class ATCommandMessageHandler extends Handler
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
                throw new NullPointerException(mContext.getResources().getString(R.string.at_cmd_cbk_para_is_null));
            }

            switch(msg.what)
            {
                case MESSAGE_ID_AT_COMMAND_SEND_COMPLETE:
                    Bundle outputData = (Bundle)msg.obj;

                    String outputStr[] = {outputData.getString(KEY_AT_COMMAND, mContext.getResources().getString(R.string.no_cmd)),
                                          outputData.getString(KET_AT_RESUIT_MESSAGE, mContext.getResources().getString(R.string.no_execute_msg))};

                    mCmdCbk.ATCommandSendComplete(outputStr);
                    break;

                case MESSAGE_ID_AT_COMMAND_SEND_ERROR:
                    String errorMsgStr = (String)msg.obj;

                    mCmdCbk.ATCommandSendError(errorMsgStr);
                    break;

                default:
                    break;
            }
        }
    }
}
