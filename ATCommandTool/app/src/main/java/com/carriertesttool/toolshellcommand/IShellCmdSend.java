package com.carriertesttool.toolshellcommand;

/**
 * Created by Admin on 2016/8/18.
 */
public interface IShellCmdSend {
    public String ShellCmdSend(String ShellCmd);
    public void ShellCommandSendComplete(String[] outputData);
    public void ShellCommandSendError(String errorMsg);
}
