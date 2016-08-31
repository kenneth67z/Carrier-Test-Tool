package com.carriertesttool.toolautoexephonefunc;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.atcommandtool.com.atcommandtool.R;

/**
 * Created by Admin on 2016/8/25.
 */
public class AutoExePhoneFunc {
    public static final String TAG_AUTO_EXECUTE_PHONE_FUNCTION = "AUTO_EXECUTE_PHONE";
    public static final String KEY_AUTO_EXECUTE_PHONE_FUNCTION = "AUTO_EXECUTE_PHONE";
    public static final String KET_AUTO_EXECUTE_PHONE_FUNCTION_RESUIT_MESSAGE = "AUTO_EXECUTE_PHONE_RESULT_MSG";
    private static final int MESSAGE_ID_AUTO_EXECUTE_PHONE_FUNCTION_COMPLETE    = 0x1313;
    private static final int MESSAGE_ID_AUTO_EXECUTE_PHONE_FUNCTION_ERROR        = 0x1414;

    private static AutoExePhoneFunc mInstance = new AutoExePhoneFunc();
    private static IAutoExePhoneFunc mCmdCbk = null;
    private AutoExePhoneFuncdMessageHandler mMsgHandler = null;
    private String mExecuteMsg = null;
    private Context mContext = null;

    private AutoExePhoneFunc() {}

    public static AutoExePhoneFunc getInstance()
    {
        if(null == mInstance)
        {
            mInstance = new AutoExePhoneFunc();
        }
        return mInstance;
    }

    public void init()
    {
        mMsgHandler = new AutoExePhoneFuncdMessageHandler();
    }

    public void setContext(Context context)
    {
        mContext = context;
        if(null != mMsgHandler)
            mMsgHandler.setContext(context);
    }

    public void setCallback(IAutoExePhoneFunc cmdCallback)
    {
        mCmdCbk = cmdCallback;
    }

    public void setExecutePhoneNumber(String phoneNumber)
    {
        mExecuteMsg = phoneNumber;
    }

    public void dialPhoneCall(String phoneNum)
    {
        mCmdCbk.dialPhoneCall(phoneNum);
    }

    public void send()
    {
        if(null == mExecuteMsg)
        {
            String errorMsg = mContext.getResources().getString(R.string.auto_exe_phone_para_is_null);
            throw new NullPointerException(errorMsg);
        }
        else if(mExecuteMsg.length() == 0)
        {
            String errorMsg = mContext.getResources().getString(R.string.auto_exe_phone_msg_length_is_null);
            Log.e(TAG_AUTO_EXECUTE_PHONE_FUNCTION, errorMsg);
            Message msg;

            msg = mMsgHandler.obtainMessage(MESSAGE_ID_AUTO_EXECUTE_PHONE_FUNCTION_ERROR, errorMsg);
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
                    outputStr = mCmdCbk.sendCmd(mExecuteMsg);
                    if(null != outputStr)
                        success = true;
                }

                if(success)
                {
                    Bundle outputData = new Bundle();

                    outputData.putString(KEY_AUTO_EXECUTE_PHONE_FUNCTION, mExecuteMsg);
                    outputData.putString(KET_AUTO_EXECUTE_PHONE_FUNCTION_RESUIT_MESSAGE, outputStr);
                    msg = mMsgHandler.obtainMessage(MESSAGE_ID_AUTO_EXECUTE_PHONE_FUNCTION_COMPLETE, outputData);

                }
                else
                {
                    msg = new Message();
                    msg.what = MESSAGE_ID_AUTO_EXECUTE_PHONE_FUNCTION_ERROR;
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
        mExecuteMsg = null;
        mContext = null;
    }

    private static class AutoExePhoneFuncdMessageHandler extends Handler
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
                throw new NullPointerException(mContext.getResources().getString(R.string.auto_exe_phone_cbk_para_is_null));
            }

            switch(msg.what)
            {
                case MESSAGE_ID_AUTO_EXECUTE_PHONE_FUNCTION_COMPLETE:
                    Bundle outputData = (Bundle)msg.obj;

                    String outputStr[] = {outputData.getString(KEY_AUTO_EXECUTE_PHONE_FUNCTION, mContext.getResources().getString(R.string.no_cmd)),
                            outputData.getString(KET_AUTO_EXECUTE_PHONE_FUNCTION_RESUIT_MESSAGE, mContext.getResources().getString(R.string.no_execute_msg))};

                    mCmdCbk.dialPhoneCallComplete(outputStr);
                    break;

                case MESSAGE_ID_AUTO_EXECUTE_PHONE_FUNCTION_ERROR:
                    String errorMsgStr = (String)msg.obj;

                    mCmdCbk.dialPhoneCallError(errorMsgStr);
                    break;

                default:
                    break;
            }
        }
    }
}
