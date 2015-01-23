package com.cloay.markforface;

import java.io.ByteArrayOutputStream;

import org.json.JSONObject;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class FaceppDetect {
	private DetectCallback callback = null;
	
	public FaceppDetect(){
	}
	
	public void setDetectCallback(DetectCallback detectCallback) { 
		callback = detectCallback;
	}

	public void detect(final Bitmap img) {
		
		new Thread(new Runnable() {
			
			public void run() {
				HttpRequests httpRequests = new HttpRequests(Util.FACE_PLUS_PLUS_APIKEY, 
						Util.FACE_PLUS_PLUS_SERCET, true, false);
	    		//Log.v(TAG, "image size : " + img.getWidth() + " " + img.getHeight());
	    		
	    		ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    		float scale = Math.min(1, Math.min(600f / img.getWidth(), 600f / img.getHeight()));
	    		Matrix matrix = new Matrix();
	    		matrix.postScale(scale, scale);

	    		Bitmap imgSmall = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, false);
	    		//Log.v(TAG, "imgSmall size : " + imgSmall.getWidth() + " " + imgSmall.getHeight());
	    		
	    		imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	    		byte[] array = stream.toByteArray();
	    		
	    		try {
	    			//detect
					JSONObject result = httpRequests.detectionDetect(new PostParameters().setImg(array));
					//finished , then call the callback function
					if (callback != null) {
						callback.detectResult(result);
					}
				} catch (FaceppParseException e) {
					e.printStackTrace();
					if (callback != null) {
						callback.detectResult(null);
					}
				}
				
			}
		}).start();
	}
	
	public void compare(final String faceId, final String modelFaceId) {
		
		new Thread(new Runnable() {
			
			public void run() {
				HttpRequests httpRequests = new HttpRequests(Util.FACE_PLUS_PLUS_APIKEY, 
						Util.FACE_PLUS_PLUS_SERCET, true, false);
	    		try {
	    			//detect
					JSONObject result = httpRequests.recognitionCompare(new PostParameters().setFaceId1(faceId).setFaceId2(modelFaceId));
					//finished , then call the callback function
					if (callback != null) {
						callback.compileResult(result);
					}
				} catch (FaceppParseException e) {
					e.printStackTrace();
					if (callback != null) {
						callback.compileResult(null);
					}
				}
				
			}
		}).start();
	}
	
	interface DetectCallback {
		void detectResult(JSONObject rst);
		void compileResult(JSONObject rst);
	}
}

