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
 * @Description: ������
 * @author Cloay Email:shangrody@gmail.com
 * @date 2013-6-17 ����05:08:18 
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
	* @Description: ��ʾһ��toast 
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
	* @Description: ��ʾһ����toast
	* @param  context
	* @param  msg     
	* @return void    �������� 
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
	* @Description: ��ʾһ����toast
	* @param  context
	* @param  msg     
	* @return void    �������� 
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
	* @Description: ��ʾһ��������Ϣ��ʾ�ĶԻ���
	* @param  context
	* @param  title
	* @param  msg     
	* @return void    �������� 
	* @throws 
	*
	**/
	public static void showDlg(Context context, String title, String msg){
		AlertDialog dlg = new AlertDialog.Builder(context).setTitle(title)
		.setMessage(msg)
		.setCancelable(false)
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
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
	* @Description:  ��������Ƿ����
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
	* @Description:  �ж������Ƿ�ΪWiFi 
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
		View v = inflater.inflate(R.layout.loading_dialog, null);// �õ�����view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// ���ز���
		// main.xml�е�ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.loading_img);
		TextView tipTextView = (TextView) v.findViewById(R.id.loading_text_msg);// ��ʾ����
		// ���ض���
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_dialog_animation);
		// ʹ��ImageView��ʾ����
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// ���ü�����Ϣ

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// �����Զ�����ʽdialog

		loadingDialog.setCancelable(true);// �������á����ؼ���ȡ��
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// ���ò���
		return loadingDialog;

	}
	
	public static String getMarkFaceResult(float score, boolean isFemale){
		String result = "";
		if(score > 95){
			result = "Wow��" + (isFemale ? "��Ů":"˧��") + "������ֵ�Ѿ�������ϵͳ�趨�����ֵ��ϵͳ�ѱ���!!!~";
		}else if(score <= 95 && score > 80){
			result = "Wow��" + (isFemale ? "��Ů":"˧��") + "������ֵ�Ѿ������ˣ�ȫ��" + String.format("%.1f", score) + "%�������ѿ�Ϲ������";
		}else if(score <= 80 && score > 65){
			result = "Wow��" + (isFemale ? "��Ů":"˧��") + "������ֵ��ô������ô�ߣ�������ȫ��" + String.format("%.1f", score) + "%�����ѣ�����";
		}else if(score <= 65 && score > 40){
			result = "��ϲ������ֵ������ȫ��" + String.format("%.1f", score) + "%�����ѣ�������Ҫ����Ŷ��";
		}else{
			result = "�ף���������������磬������Ҫ����鰡������������ײǽ������";
		}
		return result;
	}
}
