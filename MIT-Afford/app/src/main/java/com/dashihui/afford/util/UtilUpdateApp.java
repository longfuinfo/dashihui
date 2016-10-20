package com.dashihui.afford.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.dashihui.afford.R;
import com.dashihui.afford.ui.widget.WgtAlertDialog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 下载工具
 * @author NiuFC
 * @date 2013-6-26 下午2:23:57
 * @version 1.0
 *
 */
public class UtilUpdateApp {

	private final static String TAG = "UpdateAppUtill";
	/**
	 * 文件的总大小
	 */
	private int fileSize ;
	/**
	 * 已经下载文件的大小
	 */
	private int downLoadFileSize ;
	/**
	 * 正在下载标记
	 */
	private static boolean downloading = false ;
	/**
	 * 控制减少更新Progressbar次数，大量减少内存消耗
	 */
	private static boolean notifiyProgressbar = false ;

	/**
	 * 上下文
	 */
	private Context mContext;
	/**
	 * 下载框管理者
	 */
	private  NotificationManager notificationManager;
	/**
	 * 下载框
	 */
	private  Notification notification;
	/**
	 * 通知栏标记ID
	 */
	private static  int NOTIFICATION_ID = 148634 ;

	/**
	 * 开始check是否需要更新
	 */
	public static final int CHECKVERSIONMSG = 100;
	/**
	 * 更新进度条
	 */
	public static final int UPDATAPB = 101;
	/**
	 * 下载完成
	 */
	public static final int DOWNLOADOVER = 102;
	/**
	 * 更新错误
	 */
	public static final int WRONG = 103;
	/**
	 * SD卡不可用 无法更新
	 */
	public static final int SDCARDNOTAVAILABLE = 104;
	/**
	 * 开始check是否需要更新
	 */
	public static final int NOVERSION = 105;
	/**
	 * 开始check是否需要更新
	 */
	public static final int NONETWORK = 106;
	/**
	 * 网络已连接上，但是网络状态不好
	 */
	public static final int NETUNWELL = 107;
	/**
	 * 正在加载dialog
	 */
//	private static ProgressDialog progressdialog;

	public static boolean isShowDialogOrToast = false ;


	private final static String URL_CHECKVERSION = "common/checkVersion";


	public UtilUpdateApp(Context context) {
		this.mContext = context;
		initNotification();
//		progressdialog = ProgressDialog.show(context, null, "正在检查新版本，请稍等......", true, false);
//		progressdialog.setCanceledOnTouchOutside(true);
//		if(progressdialog.isShowing()){
//			progressdialog.dismiss();
//		}
	}

	/**
	 * 初始化下载框
	 */
	private void initNotification() {
		notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification(R.mipmap.ic_launcher, "下载",
				System.currentTimeMillis());

		RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.y_content_view);
		notification.contentView = view;

		PendingIntent contentIntent = PendingIntent.getActivity(mContext,
				R.string.app_name, new Intent(),
				PendingIntent.FLAG_UPDATE_CURRENT);
		notification.contentIntent = contentIntent;
	}
	/**
	 * 安装APK
	 * @param file APK文件
	 */
	protected void installApk(File file) {

		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
		mContext.startActivity(intent);

	}

	/**
	 * 下载文件类
	 * @param path 下载地址
	 * @param name 保存文件名
	 * @return 下载的文件
	 * @throws Exception
	 */
	public File getFileFromServer(String path, String name)
			throws Exception {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			fileSize = conn.getContentLength();
			InputStream is = conn.getInputStream();
			if (this.fileSize <= 0) throw new RuntimeException("无法获知文件大小 ");
			if (is == null) throw new RuntimeException("stream is null");
			File file = new File(Environment.getExternalStorageDirectory(),
					name);
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len;
			int total = 0;
			downloading = true;
			notifiyProgressbar = true;
			new Thread(){
				public void run() {
					while (downloading) {
						try {
							sleep(300);
							notifiyProgressbar = !notifiyProgressbar;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
			}.start();
			while ((len = bis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				total += len;
				downLoadFileSize =  total;
				if(notifiyProgressbar){
					notifiyProgressbar = false;
					Log.i("bai","更新notification");
					handlerCheckVersion.sendEmptyMessage(UPDATAPB);
				}

			}
			fos.close();
			bis.close();
			is.close();
			handlerCheckVersion.sendEmptyMessage(DOWNLOADOVER);
			try {
				downloading = false ;
			} catch (Exception e) {
				e.printStackTrace();
			}

			return file;
		} else {
			handlerCheckVersion.sendEmptyMessage(SDCARDNOTAVAILABLE);
			return null;
		}
	}


	/**
	 * 启动线程去下载文件，下载完成后并安装
	 * @param url
	 */
	protected void downLoadApk(final String url) {
		new Thread() {
			@Override
			public void run() {
				try {
					File file = getFileFromServer(url,"dashihui_v"+ mContext.getResources().getString(R.string.versionName)+".apk");
					sleep(800);
					installApk(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	/**
	 * 显示下载更新dialog
	 * @param url
	 */

	protected void showUpdataDialog(final String url) {
		final WgtAlertDialog mDialog = new WgtAlertDialog();
		if(!isShowDialogOrToast){
			mDialog.show(mContext,
					null, "现在更新",
					"检测到新版本！",
					null, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							downLoadApk(url);
							mDialog.dismiss();
						}
					}, true, false, R.drawable.stat_sys_download, "版本升级");
		}else{
			mDialog.show(mContext,
					"以后再说", "现在更新",
					"检测到新版本！",
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							mDialog.dismiss();
						}
					}, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							downLoadApk(url);
							mDialog.dismiss();
						}
					}, false, false, R.drawable.stat_sys_download, "版本升级");
		}


	}
	/**
	 * 处理下载handler
	 */
	Handler handlerCheckVersion = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case CHECKVERSIONMSG://检测到新版本

					Log.e(TAG, "get checkVersion data :" + msg.obj.toString());
					String versionURL = msg.obj+"";
					if (versionURL != "null") {
						Log.i(TAG, "检测到新版本");
						showUpdataDialog(versionURL);
					} else {
						handlerCheckVersion.sendEmptyMessage(NOVERSION);
						Log.i(TAG, "没有检测到新版本");
					}
					break;
				case UPDATAPB:
					int result = downLoadFileSize * 100 / fileSize;
					Log.i("bai", "下载新版本:" + result);
					notification.contentView.setTextViewText(R.id.content_view_text1, result + "%");
					notification.contentView.setProgressBar(R.id.content_view_progress, 100, result, false);
					notificationManager.notify(NOTIFICATION_ID, notification);
					break;
				case DOWNLOADOVER:
					Toast.makeText(mContext, "文件下载完成", Toast.LENGTH_SHORT).show();
					notificationManager.cancel(NOTIFICATION_ID);
					break;

				case WRONG:
					if(!isShowDialogOrToast){
						return ;
					}
					Toast.makeText(mContext, "无法获取数据",Toast.LENGTH_SHORT).show();
					notificationManager.cancel(NOTIFICATION_ID);
					break;
				case SDCARDNOTAVAILABLE:
					showCenterToast(mContext, "当前SD卡不可用,无法加载");
					break;
				case NONETWORK:
					showCenterToast(mContext, "当前没有网络，请先打开网络");
					break;
				case NOVERSION :
					if(isShowDialogOrToast){
						showCenterToast(mContext, "当前已经是最新版本！");
						return ;
					}
					break;
				case NETUNWELL:
					showCenterToast(mContext, "亲，你的网络不给力哦！");
					break;
				default:
					break;
			}
		}
	};

	/**
	 * 开始进行版本更新
	 */
	public void startVersion(Map<String,Object> mapObject,boolean isShowDialog){
		Log.e(TAG, "开始进行版本更新=========startCheckVersion=====>"+mapObject );
		isShowDialogOrToast = isShowDialog ;
		if (mapObject!=null){
			Message message = new Message();
			String updateFlg = mapObject.get("UPDATEFLG")+"";
			if ("B".equals(updateFlg)){
				message.obj = mapObject.get("DOWNURL");
				message.what = CHECKVERSIONMSG;
			}else if("C".equals(updateFlg)){
				message.obj = mapObject.get("DOWNURL");
				message.what = CHECKVERSIONMSG;
			}else {
				message.what = NOVERSION;
			}
			handlerCheckVersion.sendMessage(message);
		}else {
			Log.e(TAG, "error==null=======mapObject=====>"+mapObject );
		}

	}

	/**
	 * 在关于我们里应用
	 * 检查新版本
	 */
	public void startCheckVersion(final Map<String,Object> mapObject,final boolean isShowDialog){

		Log.i("AlertDialog", "isShowDialogOrToast" + isShowDialogOrToast);
		if(!UtilCommon.isNetworkAvailable(mContext)){
			handlerCheckVersion.sendEmptyMessage(NONETWORK);
			return;
		}
		final WgtAlertDialog mAtDialog = new WgtAlertDialog();
		mAtDialog.show(mContext,
				"取消", "确定",
				"是否检查新版本?",
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mAtDialog.dismiss();
					}
				}, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						startVersion(mapObject,isShowDialog);
						mAtDialog.dismiss();
					}
				}, false, false, R.drawable.stat_sys_download, "检查新版本");
	}
	/**
	 * 显示中间位置toast
	 * @param context
	 * @param Text
	 */
	public void showCenterToast(Context context,String Text){
		Toast toast = Toast.makeText(context, Text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}




}
