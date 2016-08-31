package com.carriertesttool.toolshellcommand;

/**
 * Created by Admin on 2016/8/18.
 */
public interface IShellCmdSend {
    String ShellCmdSend(String ShellCmd);
    void ShellCommandSendComplete(String[] outputData);
    void ShellCommandSendError(String errorMsg);
}
