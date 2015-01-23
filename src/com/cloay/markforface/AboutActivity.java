package com.cloay.markforface;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * 
 * @ClassName: AboutActivity 
 * @Description:关于 
 * @author cloay Email:shangrody@gmail.com 
 * @date 2014-11-28 下午4:58:05 
 *
 */
public class AboutActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.navbar_bg));
		actionBar.setTitle("关于");
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		TextView versionTextV = (TextView) findViewById(R.id.about_version_textV);
		versionTextV.setText("v" + Util.getAppVersion(this));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    int id = item.getItemId();
	    if(id == android.R.id.home){
	    	finish();
	    	return true;
	    }
	    	
	    return super.onOptionsItemSelected(item);
	}
}
