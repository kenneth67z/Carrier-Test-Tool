package com.atcommandtool.com.atcommandtool.atcommand;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.atcommandtool.com.atcommandtool.R;
/**
 * Created by Admin on 2016/8/17.
 */
public class ATCommandSend {
    public static final String TAG_AT_COMMAND = "AT_CMD";
    private static final int MESSAGE_ID_AT_COMMAND_SEND_COMPLETE = 0x391;
    private static final int MESSAGE_ID_AT_COMMAND_SEND_ERROR     = 0x393;

    private static ATCommandSend mInstance = new ATCommandSend();
    private static IATCmdSend mCmdCbk = null;
    private ATCommandMessageHandler mMsgHandler = new ATCommandMessageHandler();
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

    public void setContext(Context context)
    {
        mContext = context;
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
                Message msg;

                if(null != mCmdCbk)
                {
                    success =  mCmdCbk.ATCmdSend(mATCmd);
                }

                if(success)
                {
                    msg = mMsgHandler.obtainMessage(MESSAGE_ID_AT_COMMAND_SEND_COMPLETE, mATCmd);
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

    private static class ATCommandMessageHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(null == mCmdCbk)
            {
                throw new NullPointerException("AT command callback parameter is null.");
            }

            switch(msg.what)
            {
                case MESSAGE_ID_AT_COMMAND_SEND_COMPLETE:
                    String atCmdMsgStr = (String)msg.obj;

                    mCmdCbk.ATCommandSendComplete(atCmdMsgStr);
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
