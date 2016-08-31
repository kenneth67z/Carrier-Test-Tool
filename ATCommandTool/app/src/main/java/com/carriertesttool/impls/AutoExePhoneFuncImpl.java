package com.carriertesttool.impls;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;
import com.carriertesttool.toolautoexephonefunc.AutoExePhoneFunc;
import com.carriertesttool.toolautoexephonefunc.IAutoExePhoneFunc;
import java.io.DataOutputStream;
import java.io.OutputStream;

/**
 * Created by Admin on 2016/8/25.
 */
public class AutoExePhoneFuncImpl implements IAutoExePhoneFunc {
    private TextView mTextViewResult = null;
    private Context mContext = null;

    public AutoExePhoneFuncImpl(Context context) {
        mContext = context;
    }

    public void setTextViewItem(TextView textView) {
        mTextViewResult = textView;
    }


    @Override
    public String sendCmd(String cmd) {
        StringBuffer output = new StringBuffer();
        String response = "";
        Process process;

        execShellCmd("input keyevent 3");
        /*try {
            process = Runtime.getRuntime().exec("su");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        response = output.toString();*/


        return response;
    }

    @Override
    public void dialPhoneCall(String phoneNumber) {
        String uri = "tel:" + phoneNumber;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        mContext.startActivity(intent);
    }

    @Override
    public void dialPhoneCallComplete(String[] outputData) {

    }

    @Override
    public void dialPhoneCallError(String errorMsg) {

    }

    private void execShellCmd(String cmd) {

        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
            Log.e(AutoExePhoneFunc.TAG_AUTO_EXECUTE_PHONE_FUNCTION, "Exception");
        }
    }
}
