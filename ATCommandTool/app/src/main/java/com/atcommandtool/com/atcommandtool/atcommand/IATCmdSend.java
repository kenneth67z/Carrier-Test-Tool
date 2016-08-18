package com.atcommandtool.com.atcommandtool.atcommand;
/**
 * Created by Admin on 2016/8/17.
 */
public interface IATCmdSend {
    public boolean ATCmdSend(String ATCmd);
    public void ATCommandSendComplete(String ATCmd);
    public void ATCommandSendError(String errorMsg);
}
