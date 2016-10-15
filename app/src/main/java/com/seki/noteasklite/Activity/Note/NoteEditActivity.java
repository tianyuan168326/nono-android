package com.seki.noteasklite.Activity.Note;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.seki.noteasklite.DBHelpers.NoteDBHelper;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.DataUtil.StaticFinalValue;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.AppPreferenceUtil;
import com.seki.therichedittext.EditView;
import com.seki.therichedittext.PhotoLayout;
import com.seki.therichedittext.RichEdit;
import com.seki.therichedittext.RichEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NoteEditActivity extends NoteEditBaseActivity {
	private int sel = -1;
	private int viewIndex = -1;
	EditView editTextWrapper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sel = getIntent().getIntExtra("sel",-1);
		viewIndex = getIntent().getIntExtra("viewIndex",-1);
		setContentView(R.layout.activity_note_edit, groupName);
		handleSend();
	}

	@Override
	protected IntentDataTuple getIntentData() {
		String groupName = getIntent().getStringExtra(StaticFinalValue.STATIC_STRING_GroupName);
		long sdfId = getIntent().getLongExtra("sdfId", -1);
		String Title = getIntent().getStringExtra(StaticFinalValue.STATIC_STRING_NoteTitle);
		String uuid = getIntent().getStringExtra("uuid");
		groupName = TextUtils.isEmpty(groupName)? AppPreferenceUtil.getDefaultGroup():groupName;
		boolean isNew = true;
		String content = null;
		if (sdfId >= 0) {
			isNew = false;
			content = NoteDBHelper.getInstance().getContentById(sdfId);
		}
		return new IntentDataTuple(sdfId,Title, content,  groupName,  uuid,  isNew);
	}

	public static Intent start(Context context, NoteAllArray data, int index, int sel){
		Intent intent = new Intent(context, NoteEditActivity.class)
				.putExtra("GroupName", data.group)
				.putExtra("noteTitle", data.title)
				.putExtra("uuid", data.uuid)
				.putExtra("sdfId",data.sdfId)
				.putExtra("viewIndex",index)
				.putExtra("sel",sel)
				;
		context.startActivity(intent);
		return intent;
	}
	@Override
	protected void handleIniCursor() {
		super.handleIniCursor();
		noteEditTitle.setText(Title);
		editTextWrapper.forceRefocus();
		if(content!=null){
			editTextWrapper.setHtml(content);
		}
		RichEdit richEdit  = null;
		ViewGroup realEditor =(ViewGroup) editTextWrapper.getChildAt(0);
		int wrapperChildNum = realEditor.getChildCount();
		for(int i = 0;i<wrapperChildNum;i++){
			View v  = realEditor.getChildAt(i);
			if(v instanceof ScrollView){
				ScrollView editWrapper = (ScrollView)v;
				int editChildIndex = editWrapper.getChildCount();
				for(int childIndex = 0;childIndex <editChildIndex;childIndex++){
					if(editWrapper.getChildAt(childIndex )  instanceof  RichEdit){
						richEdit = (RichEdit)editWrapper.getChildAt(childIndex );
					}
				}
			}
		}
		if(viewIndex>-1){
			View richTxt = richEdit.getChildAt(viewIndex);
			richTxt.requestFocus();
			if(sel>-1 && richTxt instanceof RichEditText){
				((RichEditText)richTxt).editText.setSelection(sel);
			}
		}
	}

	@Override
	public String getAutoSaveText() {
		return editTextWrapper.getHtmlText();
	}

	@Override
	public String getHtmlTextForTextNum() {
		return editTextWrapper.getHtmlText();
	}

	@Override
	public String getAffix() {
		return null;
	}

	private void handleSend() {
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();
		if (Intent.ACTION_SEND.equals(action) && type != null) {
			groupName = "默认分组";
			if ("text/plain".equals(type)) {
				handleSendText(intent); // Handle text being sent
			} else if (type.startsWith("image/")) {
				handleSendImage(intent); // Handle single image being sent
			}
		} else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
			if (type.startsWith("image/")) {
				handleSendMultipleImages(intent); // Handle multiple images being sent
			}
		} else {
			// Handle other intents, such as being started from the home screen
		}
	}

	private void handleSendMultipleImages(Intent intent) {
		final ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
		editTextWrapper.post(new Runnable() {
			@Override
			public void run() {
				try{

					if (imageUris != null) {
						// Update UI to reflect multiple images being shared
						//editTextWrapper
						for (Uri image:
								imageUris){
							PhotoLayout p =  new PhotoLayout(NoteEditActivity. this);
							p.setImage(new File(image.getPath()).getAbsolutePath());
							editTextWrapper.addView(p);
						}
					}
					content = editTextWrapper.getHtmlText();
				}catch (Exception e){
					Toast.makeText(NoteEditActivity.this,"分享图片失败！",Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	private void handleSendImage(Intent intent) {
		final Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
		editTextWrapper.post(new Runnable() {
			@Override
			public void run() {
				try{
					if (imageUri != null) {
						// Update UI to reflect image being shared
						PhotoLayout p =  new PhotoLayout(NoteEditActivity.this);
						String path = new File(imageUri.getPath()).getAbsolutePath();
						p.setImage(path);
						editTextWrapper.addView(p);
					}
					content = editTextWrapper.getHtmlText();
				}catch (Exception e){
					Toast.makeText(NoteEditActivity.this,"分享图片失败！",Toast.LENGTH_SHORT).show();
				}
			}
		});


	}

	private void handleSendText(Intent intent) {
		String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
		if (sharedText != null) {
			// Update UI to reflect text being shared
			content = sharedText;
		}
	}

	public class EditHistory{
		public String s;
		public int selection;
		public int focusIndex=-1;

		public EditHistory(String s, int selection) {
			this.s = s;
			this.selection = selection;
		}

		public EditHistory(String s, int selection,int focusIndex) {
			this.s = s;
			this.selection = selection;
			this.focusIndex=focusIndex;
		}

		@Override
		public boolean equals(Object o) {
			return ((EditHistory)o).s.equals(s);
		}
	}

	List<EditHistory > editTextHistory  = new ArrayList<>();

	private int maxHistoryNum = 20;

	private boolean preUndo=false;

	public void undoEdit(){
		preUndo=true;
		final EditHistory history = getCurHistory();
		if(history ==null){
			preUndo=false;
			return;
		}
		editTextWrapper.cancelAfterTextChangedCallback();
		editTextWrapper.setHtml(history.s);
		editTextWrapper.post(new Runnable() {
			@Override
			public void run() {
				if(history.focusIndex>=0){
					editTextWrapper.setSelection(history.focusIndex,history.selection);
				}
				editTextWrapper.setAfterTextChangedCallback(afterTextChangedCallback,layoutChangeListener);
				preUndo=false;
			}
		});
	}
	private void addHistory(EditHistory history){
		if(preUndo){
			return;
		}
		if(editTextHistory.size() ==0 &&history.selection ==0){
			history.selection = Html.fromHtml(history.s).toString().length()-1;
			editTextHistory.add(history);
			return ;
		}
		for (EditHistory h :
				editTextHistory) {
			if(h.equals(history))
				return ;
		}
		if(editTextHistory.size() == maxHistoryNum){
			editTextHistory.remove(0);
		}
		editTextHistory.add(history);
	}
	private EditHistory getCurHistory(){
		if(editTextHistory.size()<2){
			return null;
		}
		editTextHistory.remove(editTextHistory.size() - 1);
		EditHistory s = editTextHistory.get(editTextHistory.size()-1);
		editTextHistory.remove(editTextHistory.size() - 1);
		return s;
	}
	@Override
	protected void save() {
		super.save();

		saveText(editTextWrapper.getHtmlText(),titleEditText.getText().toString().trim());
		finish();
	}

	RichEdit.AfterTextChangedCallback afterTextChangedCallback=new RichEdit.AfterTextChangedCallback() {
		@Override
		public void afterTextChanged(Editable s) {
			addHistory(new EditHistory(editTextWrapper.getHtmlText(), editTextWrapper.getNowSel(), editTextWrapper.getNowFocusIndex()));
		}
	};

	View.OnLayoutChangeListener layoutChangeListener=new View.OnLayoutChangeListener() {
		@Override
		public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
			addHistory(new EditHistory(editTextWrapper.getHtmlText(), editTextWrapper.getNowSel(), editTextWrapper.getNowFocusIndex()));
		}
	};

	@Override
	protected void registerWidget() {

		title_zone = $(R.id.title_zone);
		noteEditTitle = $(R.id.note_edit_title);
		editTextWrapper =$(R.id.note_edit_editText);
		titleEditText = (AppCompatEditText) findViewById(R.id.note_edit_title);
		editTextWrapper.setFocusable(true);
		editTextWrapper.setFocusableInTouchMode(true);
		editTextWrapper.requestFocus();
		editTextWrapper.setAfterTextChangedCallback(afterTextChangedCallback,layoutChangeListener);
	}


	@Override
	protected void registerAdapters() {

	}

	@Override
	public void onClick(View view) {
		TypedValue typedValue = new TypedValue();
		getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		editTextWrapper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onStart() {
		super.onStart();
	}
	//may be buggy!

	@Override
	protected HashMap<Integer, String> setUpOptionMenu() {
		setMenuResId(R.menu.menu_note_edit);
		HashMap<Integer, String> idMethosNamePaire = new HashMap<Integer, String>();
		idMethosNamePaire.put(android.R.id.home, "save");
		idMethosNamePaire.put(R.id.action_save, "save");
		idMethosNamePaire.put(R.id.action_lable,"addLable");
		idMethosNamePaire.put(R.id.action_undo,"undoEdit");
		idMethosNamePaire.put(R.id.action_change_bg_color,"changeBgColor");
		//idMethosNamePaire.put(R.id.action_help,"showHelpWindow");
		return  idMethosNamePaire;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.action_help).setVisible(false);
		return true;
	}
}
