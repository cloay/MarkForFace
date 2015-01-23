package com.cloay.markforface;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.waps.AppConnect;

import com.cloay.markforface.FaceppDetect.DetectCallback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @ClassName: HomeActivity 
 * @author cloay Email:shangrody@gmail.com 
 * @date 2015-1-22 ����3:39:27 
 *
 */
public class HomeActivity extends Activity implements OnClickListener{
	private ImageView imageV;
	private FaceppDetect detect;
	
	private Button picBtn;
	private Button cameraBtn;
	private Bitmap img = null;
	
	private static final int SELECT_PIC_CODE = 1321;
	private static final int CAMERA_PIC_CODE = 1322;
	
	private Dialog dlg;
	
	private String fileSrc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.navbar_bg));
		setContentView(R.layout.home_layout);
		AppConnect.getInstance(this);
		AppConnect.getInstance(this).initPopAd(this);
		Util.creatDir();
		
		dlg = Util.createLoadingDialog(this, "���Ժ�...");
		
		imageV = (ImageView) findViewById(R.id.home_imageV);
		detect = new FaceppDetect();
		detect.setDetectCallback(new DetectCallback() {
			
			@Override
			public void detectResult(final JSONObject rst) {
				HomeActivity.this.runOnUiThread(new Runnable() {
					
					public void run() {
						//show the image
						drawResult(rst);
					}
				});
			}

			@Override
			public void compileResult(final JSONObject rst) {
				HomeActivity.this.runOnUiThread(new Runnable() {
					
					public void run() {
						dlg.dismiss();
						if(rst == null){
							Util.showShortToast(HomeActivity.this, "���������Ŷ��");
						}else{
							try {
								float similarity = (float) rst.getDouble("similarity") + new Random().nextInt(24)+1; //���Ͻ���ֵ
								Util.showLog("similarity = " + similarity);
								showResultDlg(similarity, !gender.equals("Male"));
							} catch (JSONException e) {
								e.printStackTrace();
								Util.showShortToast(HomeActivity.this, "���������Ŷ��");
							}
							
						}
					}
				});
			}
		});
		
		picBtn = (Button) findViewById(R.id.home_select_pic_btn);
		picBtn.setOnClickListener(this);
		cameraBtn = (Button) findViewById(R.id.home_camera_btn);
		cameraBtn.setOnClickListener(this);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    int id = item.getItemId();
	    //�������ͬ��menu item ��ִ�в�ͬ�Ĳ���
	    switch (id) {
	        case R.id.action_about:
	        	Intent intent = new Intent(this, AboutActivity.class);
	        	startActivity(intent);
	            break;
	        default:
	            break;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			doubleTapToExit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}



	private boolean isReadyToExit = false;
	/**
	* @Title: doubleTapToExit 
	* @Description:  ˫���˳�
	* @param context     
	* @return void     
	*/
	public void doubleTapToExit(){
		 Timer mTimer = null; 
		 if(isReadyToExit == false){
			 isReadyToExit = true; // ׼���˳�  
		     Util.showShortToast(this, "�ٰ�һ���˳�����");
		     mTimer = new Timer();  
		     mTimer.schedule(new TimerTask() {  
		    	 @Override 
		    	 public void run() {  
		    		 isReadyToExit = false; // ȡ���˳�  
		         } 
		     }, 2000); // ���2������û�а��·��ؼ�����������ʱ��ȡ�����ղ�ִ�е�����
		 }else{  
			 AppConnect.getInstance(this).close();
			 finish();
		 }
	}


	@Override
	public void onClick(View v) {
		if(v == picBtn){
			selectPic();
		}else if(v == cameraBtn){
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Util.getImageUri());  
			startActivityForResult(cameraIntent, CAMERA_PIC_CODE);
		}
		
	}

	private void selectPic(){
		//get a picture form your phone
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PIC_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK && data != null && requestCode == SELECT_PIC_CODE){
			Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
			cursor.moveToFirst();
			int idx = cursor.getColumnIndex(ImageColumns.DATA);
			fileSrc = cursor.getString(idx); 
			//Log.d(TAG, "Picture:" + fileSrc);
			getImage(fileSrc);
		}
		
		if(requestCode == CAMERA_PIC_CODE && resultCode == RESULT_OK){
			fileSrc = Util.DEFAULT_CAMERA_PATH + "temp.jpg";
			Util.showLog(fileSrc);
			getImage(fileSrc);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void getImage(String fileSrc){

		//just read size
		Options options = new Options();
		options.inJustDecodeBounds = true;
		
		img = BitmapFactory.decodeFile(fileSrc, options);
		//scale size to read
		options.inSampleSize = Math.max(1, (int)Math.ceil(Math.max((double)options.outWidth / 1024f, (double)options.outHeight / 1024f)));
		options.inJustDecodeBounds = false;
		img = BitmapFactory.decodeFile(fileSrc, options);
		imageV.setImageBitmap(img);
		
		if(!Util.isNetWorkAvailable(this)){
			Util.showShortToast(this, "��ǰ���粻���ã�");
			return;
		}
		dlg.show();
		detect.detect(img);
	}
	
	private void drawResult(JSONObject rst){
		if(rst == null){
			dlg.dismiss();
			Util.showShortToast(this, "���������Ŷ��");
			Util.showLog("ddddddd");
			return;
		}
		Util.showLog(rst.toString());
		JSONArray faceArr = rst.optJSONArray("face");
		if(faceArr.length() != 1){
			Util.showShortToast(this, "��ѡ��һ����������һ��ͷ�����Ƭ");
			dlg.dismiss();
			return;
		}
		
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		PathEffect effects = new DashPathEffect(new float[] { 12, 4}, 1);  
		paint.setPathEffect(effects); 
		paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(1);

		//create a new canvas
		Bitmap bitmap = Bitmap.createBitmap(img.getWidth(), img.getHeight(), img.getConfig());
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(img, new Matrix(), null);
		
		
		try {
			JSONObject face = faceArr.getJSONObject(0);
			float mouthRX, mouthRY, mouthLX, mouthLY;
			mouthRX = (float) face.getJSONObject("position").getJSONObject("mouth_right").getDouble("x");
			mouthRY = (float) face.getJSONObject("position").getJSONObject("mouth_right").getDouble("y");
			mouthLX = (float) face.getJSONObject("position").getJSONObject("mouth_left").getDouble("x");
			mouthLY = (float) face.getJSONObject("position").getJSONObject("mouth_left").getDouble("y");
			mouthLX = mouthLX/100*img.getWidth();
			mouthRX = mouthRX/100*img.getWidth();
			mouthLY = mouthLY/100*img.getHeight();
			mouthRY = mouthRY/100*img.getHeight();
			canvas.drawLine(mouthLX + 5, mouthLY, mouthRX - 5, mouthRY, paint);
			
			float eyeLX, eyeLY, eyeRX, eyeRY;
			eyeLX = (float) face.getJSONObject("position").getJSONObject("eye_left").getDouble("x");
			eyeLY = (float) face.getJSONObject("position").getJSONObject("eye_left").getDouble("y");
			eyeRX = (float) face.getJSONObject("position").getJSONObject("eye_right").getDouble("x");
			eyeRY = (float) face.getJSONObject("position").getJSONObject("eye_right").getDouble("y");
			eyeLX = eyeLX/100*img.getWidth();
			eyeRX = eyeRX/100*img.getWidth();
			eyeLY = eyeLY/100*img.getHeight();
			eyeRY = eyeRY/100*img.getHeight();
			//left eye
			canvas.drawLine(eyeLX- 12, eyeLY, eyeLX + 12, eyeLY, paint);
			//right eye
			canvas.drawLine(eyeRX- 12, eyeRY, eyeRX + 12, eyeRY, paint);
			
			//save new image
			img = bitmap;
			imageV.setImageBitmap(img);
			
			markFace(face);
		} catch (JSONException e) {
			e.printStackTrace();
			dlg.dismiss();
			Util.showShortToast(this, "���������Ŷ��");
		}
	}
	
	private String gender = "Male";
	private void markFace(JSONObject face){
		try {
			String faceId = face.getString("face_id");
			gender = face.getJSONObject("attribute").getJSONObject("gender").optString("value", "Male");
			detect.compare(faceId, gender.equals("Male") ? Util.DEFAULT_MALE_FACE_ID : Util.DEFAULT_FEMALE_FACE_ID);
		} catch (JSONException e) {
			e.printStackTrace();
			dlg.dismiss();
			Util.showShortToast(this, "���������Ŷ��" + e.getMessage());
		}
		
	}
	
	private Dialog resultDlg;
	@SuppressLint("InflateParams")
	private void showResultDlg(float score, boolean isFemale){
		final String shareText = "������ʹ�� ����ֵAndroid�� �ҵ���ֵ�ﵽ�˾��˵�" + String.format("%.1f", score) + "��, ���Ѿ������ˣ�С����ǿ����԰ɣ�" + 
				Util.APP_DOWNLOAD_URL; 
		resultDlg = new Dialog(this, R.style.CLoadingDialog);
    	View v = LayoutInflater.from(this).inflate(R.layout.msg_dlg_layout, null);
    	((TextView) v.findViewById(R.id.msg_title_textV)).setText("���Խ��");
    	((TextView) v.findViewById(R.id.msg_content_textV)).setText(Util.getMarkFaceResult(score, isFemale));
    	((Button) v.findViewById(R.id.alert_ok_btn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				resultDlg.dismiss();
				Intent intent = new Intent(Intent.ACTION_SEND); // ���������͵�����
				intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(fileSrc));
				intent.setType("image/*"); // �����͵���������
				intent.putExtra(Intent.EXTRA_TEXT, shareText); // ���������
				intent.putExtra("kdescription", shareText); // ���������
				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent, "����"));
			}
		});
    	
    	((Button) v.findViewById(R.id.alert_cancle_btn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				resultDlg.dismiss();
				AppConnect.getInstance(HomeActivity.this).showPopAd(HomeActivity.this);
			}
		});
    	resultDlg.setCancelable(true);
    	resultDlg.setContentView(v);
    	resultDlg.show();
	}
}
