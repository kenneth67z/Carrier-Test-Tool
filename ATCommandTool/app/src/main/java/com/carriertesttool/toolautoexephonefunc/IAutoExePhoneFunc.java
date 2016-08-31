package com.carriertesttool.toolautoexephonefunc;

/**
 * Created by Admin on 2016/8/25.
 */
public interface IAutoExePhoneFunc {
    String sendCmd(String cmd);
    void dialPhoneCall(String phoneNumber);
    void dialPhoneCallComplete(String[] outputData);
    void dialPhoneCallError(String errorMsg);
}
