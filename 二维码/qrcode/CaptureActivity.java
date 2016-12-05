package com.onetoo.www.onetoo.qrcode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.onetoo.www.onetoo.R;
import com.onetoo.www.onetoo.activity.my.FriendDetailsActivity;
import com.onetoo.www.onetoo.activity.order.PayByQRActivity;
import com.onetoo.www.onetoo.base.BaseActivity;
import com.onetoo.www.onetoo.bean.my.User;
import com.onetoo.www.onetoo.client.ClientCallBack;
import com.onetoo.www.onetoo.client.ClientResult;
import com.onetoo.www.onetoo.client.my.ClientMyAPI;
import com.onetoo.www.onetoo.qrcode.camera.CameraManager;
import com.onetoo.www.onetoo.qrcode.decoding.CaptureActivityHandler;
import com.onetoo.www.onetoo.qrcode.decoding.InactivityTimer;
import com.onetoo.www.onetoo.qrcode.view.ViewfinderView;
import com.onetoo.www.onetoo.utils.GlideLoader;
import com.onetoo.www.onetoo.utils.StringUtils;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

//二维码扫描
public class CaptureActivity extends BaseActivity implements Callback, ClientCallBack, View.OnClickListener {

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private SurfaceView surfaceView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    // private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    CameraManager cameraManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*
         * this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 * WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		 * 
		 * RelativeLayout layout = new RelativeLayout(this);
		 * layout.setLayoutParams(new
		 * ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,
		 * LayoutParams.FILL_PARENT));
		 * 
		 * this.surfaceView = new SurfaceView(this); this.surfaceView
		 * .setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,
		 * LayoutParams.FILL_PARENT));
		 * 
		 * layout.addView(this.surfaceView);
		 * 
		 * this.viewfinderView = new ViewfinderView(this);
		 * this.viewfinderView.setBackgroundColor(0x00000000);
		 * this.viewfinderView.setLayoutParams(new
		 * ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,
		 * LayoutParams.FILL_PARENT)); layout.addView(this.viewfinderView);
		 * 
		 * TextView status = new TextView(this); RelativeLayout.LayoutParams
		 * params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
		 * LayoutParams.WRAP_CONTENT);
		 * params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		 * params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		 * status.setLayoutParams(params);
		 * status.setBackgroundColor(0x00000000);
		 * status.setTextColor(0xFFFFFFFF); status.setText("请将条码置于取景框内扫描。");
		 * status.setTextSize(14.0f);
		 * 
		 * layout.addView(status); setContentView(layout);
		 */

        setContentView(R.layout.activity_capture);
        initUi();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    protected void initUi() {
        initTitle("二维码扫描");
        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinderview);
        TextView tvChosePhoto = (TextView) findViewById(R.id.tv_photo);
        tvChosePhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_photo:
                chosePhoto();
                break;
        }
    }

    private void chosePhoto() {
        ImageConfig config = new ImageConfig.Builder(new GlideLoader())
                // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
                .steepToolBarColor(getResources().getColor(R.color.zuti))
                // 标题的背景颜色 （默认黑色）
                .titleBgColor(getResources().getColor(R.color.zuti))
                // 提交按钮字体的颜色  （默认白色）
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                // 标题颜色 （默认白色）
                .titleTextColor(getResources().getColor(R.color.baishe))
                // 开启单选   （默认为多选）
                .singleSelect()
                .requestCode(ImageSelector.IMAGE_REQUEST_CODE)
                .build();
        ImageSelector.open(this,config);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case ImageSelector.IMAGE_REQUEST_CODE:
                    if (data != null) {
                        List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
                        Log.d("QR", "onActivityResult: "+pathList.get(0));
                        dealChosePhoto(pathList.get(0));
                    }
                    break;
            }
        }
    }

    private void dealChosePhoto(String url) {
        if (!TextUtils.isEmpty(url)) {
            Result result = ZxingUtil.scanningImage(url);
            if (result != null) {
                //String recode = ZxingUtil.recode(result.toString());
                showResult(result,null);
            }else {
                tips("提示：","请选择正确的二维码~");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        // CameraManager.init(getApplication());
        cameraManager = new CameraManager(getApplication());

        viewfinderView.setCameraManager(cameraManager);

        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        cameraManager.closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            // CameraManager.get().openDriver(surfaceHolder);
            cameraManager.openDriver(surfaceHolder);
        } catch (IOException | RuntimeException ioe) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    public void handleDecode(Result obj, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        showResult(obj, barcode);
    }
    //支付和好友
    private void showResult(final Result rawResult, Bitmap barcode) {
        String text = rawResult.getText();
        Log.d("QR", "showResult: " + text);
        if (!TextUtils.isEmpty(text)) {
            if (StringUtils.isUrl(text)) {
                int indexOf = text.indexOf("=");
                String storeId = text.substring(indexOf+1);
                Log.d("QR", "showResult: " + storeId);
                /*Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra(WebActivity.TITLE, "问兔支付");
                intent.putExtra(WebActivity.URL, text);*/
                if (!TextUtils.isEmpty(storeId)) {
                    Intent intent = new Intent(this, PayByQRActivity.class);
                    intent.putExtra(PayByQRActivity.STORE_ID, storeId);
                    startActivity(intent);
                    finish();
                }else {
                    tips("提示：","二维码信息出错!");
                }
            } else if (text.startsWith("mobile")) {
                String mobile = text.substring(6);
                searchUser(mobile);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                if (barcode != null) {
                    builder.setIcon(R.drawable.logo_01);
                }else {
                    Drawable drawable = new BitmapDrawable(barcode);
                    builder.setIcon(drawable);
                }

                builder.setTitle("类型:" + rawResult.getBarcodeFormat() + "\n 结果：" + rawResult.getText());
                builder.setPositiveButton("确定", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("result", rawResult.getText());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                builder.setNegativeButton("重新扫描", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        restartPreviewAfterDelay(0L);
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        }
        // Intent intent = new Intent();
        // intent.putExtra(QR_RESULT, rawResult.getText());
        // setResult(RESULT_OK, intent);
        // finish();

    }

    private void searchUser(String mobile) {
        showProgress("正在搜索用户...");
        ClientMyAPI clientMyAPI = new ClientMyAPI(this);
        clientMyAPI.searchUser(getApp().getMtoken(), mobile);
    }
    //加好友
    @Override
    public void onTaskFinished(ClientResult clientResult) {
        dismissProgress();
        if (clientResult.data != null) {
            JSONObject jsonObject = JSON.parseObject(clientResult.data);
            if (TextUtils.equals(jsonObject.getString("status"), "0")) {
                switch (clientResult.actionId) {
                    case ClientMyAPI.ACTION_SEARCH_USER:
                        dealUserData(clientResult.data);
                        break;
                }
            } else {
                tips("提示：", "暂时无此用户~");
            }
        } else {
            tips("提示：", "暂时无此用户~");
        }
    }
    //加好友
    private void dealUserData(String data) {
        User object = JSON.parseObject(data, User.class);
        if (object != null) {
            List<User.DataEntity> user = object.getData();
            if (user != null && user.size() > 0) {
                Intent intent = new Intent(this, FriendDetailsActivity.class);
                intent.putExtra("user", user.get(0));
                startActivity(intent);
                finish();
            } else {
                tips("提示：", "暂时无此用户~");
            }
        } else {
            tips("提示：", "暂时无此用户~");
        }
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(MessageIDs.restart_preview, delayMS);
        }
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            try {
                AssetFileDescriptor fileDescriptor = getAssets().openFd("qrbeep.ogg");
                this.mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                        fileDescriptor.getLength());
                this.mediaPlayer.setVolume(0.1F, 0.1F);
                this.mediaPlayer.prepare();
            } catch (IOException e) {
                this.mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_FOCUS || keyCode == KeyEvent.KEYCODE_CAMERA) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}