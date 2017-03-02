package com.onetoo.www.onetoo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.onetoo.www.onetoo.interfaces.OnNetStatusChangeListener;

/**
 * Created by longShun on 2017/2/17.
 * desc 网络相关接受者
 */
public class NetBroadCastReceiver extends BroadcastReceiver {

    private static final String TAG = "NetBroadCastReceiver";

    private OnNetStatusChangeListener onNetStatusChangeListener;

    public void setOnNetStatusChangeListener(OnNetStatusChangeListener onNetStatusChangeListener) {
        this.onNetStatusChangeListener = onNetStatusChangeListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //如果是在开启wifi连接和有网络状态下
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                Log.d(TAG, "onReceive:网络打开 ");
                onNetStatusChangeListener.netStatus(true);
            }
            else {
                Log.d(TAG, "onReceive:网络关闭 ");
                onNetStatusChangeListener.netStatus(false);
            }
        }
    }
}


	//注册广播接受者
	private void initNetReceiver() {
        //网络监听
        netBroadCastReceiver = new NetBroadCastReceiver();
        netBroadCastReceiver.setOnNetStatusChangeListener(new OnNetStatusChangeListener() {
            @Override
            public void netStatus(boolean isAvailable) {
                if (!isAvailable) {
                    //无网络
                    new SweetAlertDialog(BaseActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("网络异常")
                            .setContentText("请检查网络设置！")
                            .setConfirmText("确定")
                            .show();
                }
            }
        });
        IntentFilter intentFiler = new IntentFilter();
        intentFiler.addAction("android.net.conn.CONNECTIVITY_CHANGE");//只接收"android.net.conn.CONNECTIVITY_CHANGE"类型的广播
        registerReceiver(netBroadCastReceiver, intentFiler);
    }

	//取消注册
	 @Override
		protected void onDestroy() {
			unregisterReceiver(netBroadCastReceiver);
			super.onDestroy();
		}

