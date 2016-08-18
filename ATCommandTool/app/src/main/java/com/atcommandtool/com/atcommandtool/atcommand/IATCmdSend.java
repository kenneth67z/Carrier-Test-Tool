package com.atcommandtool.com.atcommandtool.atcommand;
/**
 * Created by Admin on 2016/8/17.
 */
public interface IATCmdSend {
    public String ATCmdSend(String ATCmd);
    public void ATCommandSendComplete(String[] outputData);
    public void ATCommandSendError(String errorMsg);
}
