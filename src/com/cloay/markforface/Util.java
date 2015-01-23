package com.cloay.markforface;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/** 
 * @ClassName: Utils 
 * @Description: 工具类
 * @author Cloay Email:shangrody@gmail.com
 * @date 2013-6-17 下午05:08:18 
 * 
 **/
public class Util {
	
	public static final String FACE_PLUS_PLUS_APIKEY = "e0437691540179e2c52a5d85bd486ffc";
	public static final String FACE_PLUS_PLUS_SERCET = "Ohw82k_IsoFH5l1i9FiucmbNHQ7qd8LY";
	public static final String DEFAULT_CAMERA_PATH = Environment.getExternalStorageDirectory() + 
			"/markforface/";
	
	public static final String DEFAULT_FEMALE_FACE_ID="429294da13798f5eb2b6004a73945468";
	public static final String DEFAULT_MALE_FACE_ID="7bda5cdfabcec187f1fb8f1d3b7cd61a";
	
	public static final String APP_DOWNLOAD_URL ="http://7u2ov9.com1.z0.glb.clouddn.com/MarkForFace.apk";
	
	/**
	* @Title: showToast 
	* @Description: 显示一个toast 
	* @param  context
	* @param  msg
	* @param  isLong 
	**/
	public static void showToast(Context context, String msg, boolean isLong){
		Toast.makeText(context, msg, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
	}
	
	public static Uri getImageUri() {  
        return Uri.fromFile(new File(DEFAULT_CAMERA_PATH,  
                "temp.jpg"));  
	}

	
	/**
	* 
	* @Title: showLongToast 
	* @Description: 显示一个长toast
	* @param  context
	* @param  msg     
	* @return void    返回类型 
	* @throws 
	*
	**/
	public static void showLongToast(Context context, String msg){
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	/**
	* 
	* @Title: showShortToast 
	* @Description: 显示一个短toast
	* @param  context
	* @param  msg     
	* @return void    返回类型 
	* @throws 
	*
	**/
	public static void showShortToast(Context context, String msg){
		if(msg == null)
			return;
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	public static void creatDir(){
		if(isSDCardAvailable()){
			createDir(DEFAULT_CAMERA_PATH);
		}
	}
	
	private static void createDir(String dirPath) {
		File file = new File(dirPath);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
	private static boolean isSDCardAvailable(){
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}
	/**
	** 
	* @Title: showDlg 
	* @Description: 显示一个用于信息提示的对话框
	* @param  context
	* @param  title
	* @param  msg     
	* @return void    返回类型 
	* @throws 
	*
	**/
	public static void showDlg(Context context, String title, String msg){
		AlertDialog dlg = new AlertDialog.Builder(context).setTitle(title)
		.setMessage(msg)
		.setCancelable(false)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.create();
		dlg.show();
	}
	
	
	/**
	* @Title: isNetWorkAvailable 
	* @Description:  检查网络是否可用
	* @param context
	* @return boolean     
	*  
	**/
	public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
                return false;
        }
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return (info != null) && info.isAvailable();
	}
	
	/**
	* @Title: isWifiNetwork 
	* @Description:  判断网络是否为WiFi 
	* @param context
	* @return boolean     
	* */
	public static boolean isWifiNetwork(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager)
		context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if(info.getType() == ConnectivityManager.TYPE_WIFI)
			return true;
		return false;
	}
	
	
	/**
	 * 
	* @Title: showXgtLog 
	* @Description:  
	* @param log     
	* @return void     
	*  
	*
	 */
	public static void showLog(String log){
		if(BuildConfig.DEBUG)
			Log.v("MarkForFace", log);
	}
	
	public static String getAppVersion(Context context){
    	try {
			return context.getPackageManager().getPackageInfo("com.cloay.markforface", 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
    	return "" + 1.0;
    }
	
	@SuppressLint("InflateParams")
	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.loading_img);
		TextView tipTextView = (TextView) v.findViewById(R.id.loading_text_msg);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_dialog_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(true);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		return loadingDialog;

	}
	
	public static String getMarkFaceResult(float score, boolean isFemale){
		String result = "";
		if(score > 95){
			result = "Wow，" + (isFemale ? "美女":"帅哥") + "您的颜值已经超出了系统设定的最高值，系统已崩溃!!!~";
		}else if(score <= 95 && score > 80){
			result = "Wow，" + (isFemale ? "美女":"帅哥") + "您的颜值已经爆棚了，全国" + String.format("%.1f", score) + "%的网友已哭瞎！！！";
		}else if(score <= 80 && score > 65){
			result = "Wow，" + (isFemale ? "美女":"帅哥") + "您的颜值怎么可以这么高，击败了全国" + String.format("%.1f", score) + "%的网友！！！";
		}else if(score <= 65 && score > 40){
			result = "恭喜您的颜值击败了全国" + String.format("%.1f", score) + "%的网友！！！需要加油哦！";
		}else{
			result = "亲，在这个看脸的世界，我们需要多读书啊！捂脸。。。撞墙。。。";
		}
		return result;
	}
}
