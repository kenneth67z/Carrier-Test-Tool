package com.carriertesttool.toolatcommand;
/**
 * Created by Admin on 2016/8/17.
 */
public interface IATCmdSend {
    String ATCmdSend(String ATCmd);
    void ATCommandSendComplete(String[] outputData);
    void ATCommandSendError(String errorMsg);
}
