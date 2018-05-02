package com.lz.lztrain.handler;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lz.lztrain.fragment.BaseFragment;
import com.lz.lztrain.utils.Constants;
import com.lz.lztrain.utils.TrainApplication;
import com.lz.mylibrary.BaseActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by ljz on 2015/11/1.
 */
public class RemoteDataHandler {

    private static Map<String,SoapObject> sso = new HashMap<String, SoapObject>();

    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(6,
            30, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public interface CallBackXml {
        public void dataLoaded(SoapObject soapObject);
    }

    /**
     *
     * @param activity  BaseActivity
     * @param methodname  方法名
     * @param rpc  参数
     * @param callback  回调
     * @param showProgress  是否显示对话框
     */
    public static void asyncSoap(final BaseActivity activity, final String methodname, final SoapObject rpc, final CallBackXml callback, final boolean showProgress){
        if (showProgress)
            activity.showProgress();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        activity.closeProgress();
                        SoapObject soapObject = sso.get(msg.obj);
                        sso.clear();
                        callback.dataLoaded(soapObject);
                }
            }
        };

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                String nameSpace = Constants.NAME_SPACE; //命名空间
                String endPoint = Constants.END_POINT;
                Log.i("zhou","endPoint : "+endPoint);
                String soapAction = nameSpace+methodname; //Action
                // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                envelope.bodyOut = rpc;
                // 设置是否调用的是dotNet开发的WebService
                envelope.dotNet = true;
                // 等价于envelope.bodyOut = rpc;
                envelope.setOutputSoapObject(rpc);
                HttpTransportSE transport = new HttpTransportSE(endPoint);
                try {
                    // 调用WebService
                    transport.call(soapAction, envelope);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SoapObject object = (SoapObject) envelope.bodyIn;
                Log.i("soap","object : "+object+"");
                ////////////////////////////////////////////////////////
                sso.put(methodname, object);
                Message msg = handler.obtainMessage(0,methodname);
                handler.sendMessage(msg);
            }
        });

    }

    /**
     *
     * @param fragment  BaseFragment
     * @param methodname  方法名
     * @param rpc  参数
     * @param callback  回调
     * @param showProgress  是否显示对话框
     */
    public static void asyncSoapfromfragment(final BaseFragment fragment, final String methodname, final SoapObject rpc, final CallBackXml callback, final boolean showProgress){
        if (showProgress)
            TrainApplication.getCoreApplication().showProgressDialog(fragment.getContext());
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        TrainApplication.getCoreApplication().dismissProgressDialog();
                        SoapObject soapObject = sso.get(msg.obj);
                        callback.dataLoaded(soapObject);
                        sso.clear();
                }
            }
        };

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                String nameSpace = Constants.NAME_SPACE; //命名空间
                String endPoint = Constants.END_POINT;
                Log.i("soap","endPoint : "+endPoint);
                String soapAction = nameSpace+methodname;
                // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                envelope.bodyOut = rpc;
                // 设置是否调用的是dotNet开发的WebService
                envelope.dotNet = true;
                // 等价于envelope.bodyOut = rpc;
                envelope.setOutputSoapObject(rpc);
                HttpTransportSE transport = new HttpTransportSE(endPoint);
                try {
                    // 调用WebService
                    transport.call(soapAction, envelope);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SoapObject object = (SoapObject) envelope.bodyIn;
                ////////////////////////////////////////////////////////
                sso.put(methodname, object);
                Message msg = handler.obtainMessage(0,methodname);
                handler.sendMessage(msg);
            }
        });

    }

}
