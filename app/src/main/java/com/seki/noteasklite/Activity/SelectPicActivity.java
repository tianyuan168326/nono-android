package com.seki.noteasklite.Activity;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seki.noteasklite.R;

public class SelectPicActivity extends Activity implements OnClickListener{

	
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	
	public static final String KEY_PHOTO_PATH = "photo_path";
	
	private static final String TAG = "SelectPicActivity";

	private LinearLayout dialogLayout;
	private TextView takePhotoBtn,pickPhotoBtn,cancelBtn;
	private String picPath;
	
	private Intent lastIntent ;
	
	private Uri photoUri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_pic_layout);
		getWindow().setGravity(Gravity.FILL_HORIZONTAL);
		initView();
	}
	
	private void initView() {
		dialogLayout = (LinearLayout) findViewById(R.id.select_pic_dialog_layout);
		dialogLayout.setOnClickListener(this);
		takePhotoBtn = (TextView) findViewById(R.id.select_pic_btn_take_photo);
		takePhotoBtn.setOnClickListener(this);
		pickPhotoBtn = (TextView) findViewById(R.id.select_pic_btn_pick_photo);
		pickPhotoBtn.setOnClickListener(this);
		cancelBtn = (TextView) findViewById(R.id.select_pic_btn_cancel);
		cancelBtn.setOnClickListener(this);
		
		lastIntent = getIntent();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_pic_dialog_layout:
			finish();
			break;
		case R.id.select_pic_btn_take_photo:
			takePhoto();
			break;
		case R.id.select_pic_btn_pick_photo:
			pickPhoto();
			break;
		default:
			finish();
			break;
		}
	}

	 
	private void takePhoto() {
		String SDState = Environment.getExternalStorageState();
		if(SDState.equals(Environment.MEDIA_MOUNTED))
		{
			
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"


			ContentValues values = new ContentValues();  
			photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			/**-----------------*/
			startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
		}else{
			Toast.makeText(this,getString(R.string.take_photo_erro), Toast.LENGTH_LONG).show();
		}
	}

	private void pickPhoto() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK)
		{
			doPhoto(requestCode,data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void doPhoto(int requestCode,Intent data)
	{
			if(requestCode == SELECT_PIC_BY_PICK_PHOTO )
		{
			if(data == null)
			{
				Toast.makeText(this,getString(R.string.select_pic_erro), Toast.LENGTH_LONG).show();
				return;
			}
			photoUri = data.getData();
			if(photoUri == null )
			{
				Toast.makeText(this, getString(R.string.select_pic_erro), Toast.LENGTH_LONG).show();
				return;
			}
		}
		String[] pojo = {MediaStore.Images.Media.DATA};
		Cursor cursor = managedQuery(photoUri, pojo, null, null,null);   
		if(cursor != null )
		{
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			cursor.close();
		}
		Log.i(TAG, "imagePath = "+picPath);
		if(picPath != null && ( picPath.endsWith(".png") || picPath.endsWith(".PNG") ||picPath.endsWith(".jpg") ||picPath.endsWith(".JPG")  ))
		{
			lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
			setResult(Activity.RESULT_OK, lastIntent);
			finish();
		}else{
			Toast.makeText(this, getString(R.string.select_pic_end_erro), Toast.LENGTH_LONG).show();
		}
	}
}
