package com.cc.wbsp;

import android.annotation.SuppressLint;
import android.content.Context;

import com.core.base.http.HttpRequest;
import com.core.base.request.SRequestAsyncTask;
import com.core.base.utils.SStringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gan on 2018/5/12.
 */

public class WbspHelper {

    @SuppressLint("StaticFieldLeak")
    public static void asyncReport(final Context context, final String regId){

        new SRequestAsyncTask(){

            @Override
            protected String doInBackground(String... strings) {

                reportDevice(context,regId);
                return null;
            }
        }.asyncExcute();
    }


    public static void reportDevice(Context context,String regId){

        String url = "";
        Map<String,String> dataMap = new HashMap<>();
        //SDK 初次注册成功后，开发者通过在自定义的 Receiver 里监听 Action - cn.jpush.android.intent.REGISTRATION 来获取对应的 RegistrationID。注册成功后，也可以通过此函数获取
//        public static String getRegistrationID(Context context)
        dataMap.put("regId",regId);
        dataMap.put("imei",regId);
        dataMap.put("androidId",regId);
        dataMap.put("mac",regId);
        dataMap.put("packageName",context.getPackageName());

        for (int i = 0; i < 3; i++) {

            String result = HttpRequest.post(url,dataMap);

            if (SStringUtil.isNotEmpty(result)){
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    String code = jsonObject.optString("code","");
                    if ("1000".equals(code)){
                        break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
