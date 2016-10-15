package com.seki.noteasklite.Activity.Note;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.konifar.fab_transformation.FabTransformation;
import com.seki.noteasklite.Adapter.NoteGroupItemAdapter;
import com.seki.noteasklite.Base.BaseAcitivityWithRecycleView;
import com.seki.noteasklite.Controller.NoteController;
import com.seki.noteasklite.Controller.NoteReelsController;
import com.seki.noteasklite.DBHelpers.NoteDBAdapter;
import com.seki.noteasklite.DBHelpers.NoteDBHelper;
import com.seki.noteasklite.DataUtil.Bean.DeleteNoteBean;
import com.seki.noteasklite.DataUtil.BusEvent.ChangeNoteGroupEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteDeleteEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteInsertEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteReloadEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NoteUpdateEvent;
import com.seki.noteasklite.DataUtil.BusEvent.NotesDeleteEvent;
import com.seki.noteasklite.DataUtil.BusEvent.ThemeColorPairChangedEvent;
import com.seki.noteasklite.DataUtil.NoteAllArray;
import com.seki.noteasklite.DataUtil.NoteDatabaseArray;
import com.seki.noteasklite.DataUtil.NoteReelArray;
import com.seki.noteasklite.Delegate.EditNoteDelegate;
import com.seki.noteasklite.Delegate.OpenNoteDelegate;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.Util.FrescoImageloadHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import de.greenrobot.event.EventBus;

public class ReelNoteActivity extends BaseAcitivityWithRecycleView {
	private List<NoteReelItemArray> list = new ArrayList<>();
	FloatingActionButton note_add_fab;
	View overlay;
	private LinearLayout fab_op_pannel;
	private LinearLayout btn_md;
	private LinearLayout btn_text;
	CollapsingToolbarLayout collapse_layout;
	SimpleDraweeView bg_image;
    private boolean magicFlag=false;
	private String groupName;
	String reelImagePath;
	int mutedColor = -1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		openEventBus();
		super.onCreate(savedInstanceState);
		groupName  = getIntent().getStringExtra("GroupName");
        reelImagePath = getIntent().getStringExtra("reelImagePath");
        //installAlbum();

		setContentView(R.layout.activity_group_note, "");
		((TextView)findView(R.id.reel_abstract_tv)).setText("");
		((TextView)findView(R.id.reel_title)).setText(groupName);
		//getSupportActionBar().setSubtitle(getIntent().getIntExtra("Counts", 0) + " Notes");
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) {
                    if (magicFlag) {
                        magicFlag = !magicFlag;
                        note_add_fab.show();
                    }
                } else if (dy > 0) {
                    if (!magicFlag) {
                        magicFlag = !magicFlag;
						note_add_fab.hide();
                    }
                }
            }
        });
		((NoteGroupItemAdapter)recycleViewAdapter).setOnRecyclerViewListener(new NoteGroupItemAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(View v, int position) {
				OpenNoteDelegate.start(ReelNoteActivity.this, new NoteAllArray(list.get(position).title,
						list.get(position).detail,
						groupName,
						//getTitle().toString(),
						list.get(position).date,
						list.get(position).time,
						list.get(position).sdfId,
						list.get(position).isOnCloud,
						list.get(position).uuid),
						new OpenNoteDelegate.NoteUIControl(mutedColor)
				);
            }

            @Override
            public boolean onItemLongClick(View v, final int position) {
                final String[] items=new String[2];
                items[1]=getString(R.string.action_delete);
                items[0]=getString(R.string.edit);
                new android.app.AlertDialog.Builder(ReelNoteActivity.this).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (getTitle().toString().compareTo("问题小记") != 0) {
									NoteEditActivity.start(ReelNoteActivity.this,getTitle().toString(),
											list.get(position).sdfId,list.get(position).title,list.get(position).detail);
                                }
                                break;
                            case 1:
								NoteController.deleteNote(
										new DeleteNoteBean(
												list.get(position).sdfId,
												groupName,
												list.get(position).uuid
										)
								);
                                break;
                        }
                    }
                }).show();
                return false;
            }
        });
		refresh();
	}

    private void installAlbum() {
        final WindowManager wm = (WindowManager)
                getSystemService(Context.WINDOW_SERVICE);
        int mWidth = wm.getDefaultDisplay().getWidth();
        int mHeight  = wm.getDefaultDisplay().getHeight();
        final ImageView pageWidget = new ImageView(this);
		Bitmap srcBmp = BitmapFactory.decodeResource(getResources(),R.drawable.banner);
		Bitmap dstBmp;
		WeakReference<Bitmap> bitmap = new WeakReference<>(
				srcBmp
				) ;
		pageWidget.setImageBitmap(bitmap.get());
		pageWidget.setScaleType(ImageView.ScaleType.CENTER_CROP);
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(mWidth,mHeight);
		pageWidget.setLayoutParams(params);
		final WindowManager.LayoutParams parentParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.TYPE_APPLICATION_STARTING,
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
						WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
				PixelFormat.TRANSLUCENT);

		parentParams.width = ActionBar.LayoutParams.MATCH_PARENT;
		parentParams.height = ActionBar.LayoutParams.MATCH_PARENT;
		final FrameLayout mParentView = new FrameLayout(this);
		wm.addView(mParentView,parentParams);
		mParentView.addView(pageWidget);


		pageWidget.post(new Runnable() {
			@Override
			public void run() {
				Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
				animation.setDuration(500);
				animation.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						wm.removeView(mParentView);
					}


					@Override
					public void onAnimationRepeat(Animation animation) {

					}
				});
				pageWidget.startAnimation(animation);
			}
		});
		collapse_layout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
			@Override
			public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
				if(bottom-top < 2*(toolBar.getHeight())){
					setTitle(groupName);
					toolBar.setTitle(groupName);
				}
			}
		});
    }

    public static Intent start(boolean realStart,Context c,String reelName,String reelImagePath,int reelNoteNum){
		Intent intent = new Intent(c, ReelNoteActivity.class)
				.putExtra("GroupName", reelName)
				.putExtra("reelImagePath",reelImagePath)
				.putExtra("Counts", reelNoteNum);
		if(realStart){
			c.startActivity(intent);
		}
		return intent;
	}
	@Override
	protected void registerWidget() {
		note_add_fab=$(R.id.group_note_fab_add);
		fab_op_pannel=$(R.id.fab_op_pannel);
		btn_md = $(R.id.btn_md);
		btn_text = $(R.id.btn_text);
		btn_md.setOnClickListener(this);
		btn_text.setOnClickListener(this);
		overlay=$(R.id.overlay);
		bg_image=$(R.id.bg_image);
		collapse_layout=$(R.id.collapse_layout);
		bindViewsToOnClickListenerById(R.id.group_note_fab_add,R.id.fab_op_pannel,R.id.overlay);
		if(findViewById(R.id.recycle_view_refresher)!= null){
			((SwipeRefreshLayout)findViewById(R.id.recycle_view_refresher))
					.setEnabled(false);
		}
		if(
				MyApp.getCache().reelImageCache !=null
			&&
				MyApp.getCache().reelImageCache.get() !=null){
			bg_image.setImageBitmap(MyApp.getCache().reelImageCache.get());
		}
		bg_image.post(new Runnable() {
			@Override
			public void run() {
				reelImagePath = NoteReelArray.getUriString(reelImagePath);
				FrescoImageloadHelper.LoadImageFromURLAndCallBack(bg_image,
						reelImagePath,
						ReelNoteActivity.this, new BaseBitmapDataSubscriber() {
							@Override
							protected void onNewResultImpl(@Nullable Bitmap bitmap) {
//							 	Palette.Builder b =  Palette.from(bitmap);
//								Palette p =  b.generate();
//								final Palette.Swatch swatch = p.getVibrantSwatch();
//								 mutedColor = p.getMutedColor(ThemeController.getCurrentColor().getMainColor());
//								runOnUiThread(new Runnable() {
//									@Override
//									public void run() {
//										collapse_layout.setContentScrimColor(mutedColor);
//										((NoteGroupItemAdapter)recycleViewAdapter).setThemeMain(mutedColor);
//										if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//											getWindow().setStatusBarColor(mutedColor);
//										}
//										collapse_layout.setExpandedTitleColor(swatch.getTitleTextColor());
//									}
//								});

							}

							@Override
							protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {

							}
						});
			}
		});
	}

	@Override
	protected void registerAdapters() {

	}

	@Override
    protected HashMap<Integer,String> setUpOptionMenu() {
		setMenuResId(R.menu.menu_group_note);
		HashMap<Integer,String> idMethosNamePaire = new HashMap<Integer,String>();
		idMethosNamePaire.put(android.R.id.home, "onBackPressed");
		idMethosNamePaire.put(R.id.action_delete, "deleteNotes");
		return idMethosNamePaire;
    }

	private void deleteNotes(){
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setMessage("将会删除文集下所有笔记");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteNotesDlg();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	private void deleteNotesDlg() {
		String group = groupName;
		NoteReelsController.deleteReels(new String[]{groupName});
		EventBus.getDefault().post(new NoteReloadEvent());
		setResult(2);
		finish();
	}

    @Override
	protected RecyclerView.Adapter setRecyclerViewAdapter() {
		return  new NoteGroupItemAdapter(this,list);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode){
			case 1:
				if(resultCode==RESULT_OK){
					refresh();
					setResult(RESULT_OK);
					break;
				}else if(resultCode==2){
					refresh();
					setResult(2);
					break;
				}
		}
	}
	public void refresh(){
		new RefreshTask().execute(list);
	}

	class RefreshTask extends AsyncTask<List<NoteReelItemArray>,List<NoteReelItemArray>,Cursor> {
		@Override
		protected Cursor doInBackground(List<NoteReelItemArray>... params) {
			params[0].clear();
			NoteDBAdapter noteDBAdapter=new NoteDBAdapter(ReelNoteActivity.this);
			noteDBAdapter.open();
			Cursor cursor =noteDBAdapter.getTitleByReel(getIntent().getStringExtra("GroupName"));
			if(cursor.getCount()>0&&cursor.moveToLast()){
				do{
					params[0].add(new NoteReelItemArray(cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_TITLE))
							,cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_DATE)),cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_TIME))
							,cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_CONTENT))
							,cursor.getInt(cursor.getColumnIndex(NoteDBAdapter.KEY_ROWID))
					,cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_IS_ON_CLOUD)),
							cursor.getString(cursor.getColumnIndex(NoteDBAdapter.KEY_UUID)
							)));
				}while (cursor.moveToPrevious());
			}
			noteDBAdapter.close();
			return cursor;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Cursor cursor) {
			super.onPostExecute(cursor);
			recycleViewAdapter.notifyDataSetChanged();
			noteCounts = cursor.getCount();
			if(getSupportActionBar() !=null){
				getSupportActionBar().setSubtitle(cursor.getCount() + " Notes");
			}
		}
	}
	private int noteCounts = 0;
    @Override
    public void onClick(View v) {
		NoteAllArray array =  new NoteAllArray();
		array.group = toolBarTitle;
		switch(v.getId()){
			case R.id.group_note_fab_add:
				if(note_add_fab.isShown()){
					FabTransformation.with(note_add_fab).setOverlay(overlay). transformTo(fab_op_pannel);
				}
				//NoteEditActivity.start(ReelNoteActivity.this,toolBarTitle);
			break;
			case R.id.overlay:
				closeFab();
				break;
			case R.id.btn_md:
				array.title=".md";
				EditNoteDelegate.start(this,array);
				closeFab();
				break;
			case R.id.btn_text:
				array.title = "";
				EditNoteDelegate.start(this,array);
				closeFab();
				break;
		}
    }

	private void closeFab() {
		if(fab_op_pannel.isShown()) {
			FabTransformation.with(note_add_fab).setOverlay(overlay). transformFrom(fab_op_pannel);
		}
	}

	public static class NoteReelItemArray {
		public String title;
		public String detail;
        public String date;
		public String time;
		public long sdfId;
		public String isOnCloud;
		public String uuid;
		public NoteReelItemArray(String title, String date, String time, String detail, long sdfId, String isOnCloud, String uuid){
			this.title=title;
            this.date=date;
			this.isOnCloud=isOnCloud;
			this.detail=detail;
			this.time=time;
			this.sdfId=sdfId;
			this.uuid = uuid;
		}
		public NoteReelItemArray(NoteDatabaseArray noteDatabaseArray, long noteId){
			this.title=noteDatabaseArray.Title;
			this.date=noteDatabaseArray.date;
			this.isOnCloud=noteDatabaseArray.is_on_cloud;
			this.detail=noteDatabaseArray.content;
			this.time=noteDatabaseArray.time;
			this.sdfId=noteId;
			this.uuid = noteDatabaseArray.uuid;
		}

		public DeleteNoteBean createDeleteNoteBean(String groupname){
			return new DeleteNoteBean(sdfId,groupname,uuid);
		}
	}
	public void onEventMainThread(NoteDeleteEvent event){
		if(groupName.equals(event.getNoteDatabaseArray().group)){
			int count = list.size();
			for(int index= 0;index<count;index++){
				if(list.get(index).sdfId == event.getNoteId()){
					list.remove(index);
					recycleViewAdapter.notifyDataSetChanged();
					getSupportActionBar().setSubtitle(--noteCounts + " Notes");
				}
			}
		}
		//refresh();
	}
	public void onEventMainThread(NotesDeleteEvent event){
			int count = list.size();
			for(int index= 0;index<count;index++){
				for (long Id:
					event.iDs ) {
					if(list.get(index).sdfId == Id){
						list.remove(index);
						recycleViewAdapter.notifyDataSetChanged();
						getSupportActionBar().setSubtitle(--noteCounts + " Notes");
					}
				}
			}
		//refresh();
	}
	public void onEventMainThread(NoteInsertEvent event){
		if(groupName.equals(event.getNoteDatabaseArray().group)){
//			int count = list.size();
//			for(int index= 0;index<count;index++){
//				if(list.get(index).sdfId == event.getNoteId()){
					list.add(0,
							new NoteReelItemArray(event.getNoteDatabaseArray(),event.getNoteId())
							);
					recycleViewAdapter.notifyDataSetChanged();
					getSupportActionBar().setSubtitle(++noteCounts + " Notes");
//				}
//			}
		}
		//refresh();
	}
	private void addNoteCounts(){
		getSupportActionBar().setSubtitle(++noteCounts + " Notes");
	}
	private void subNoteCounts(){
		getSupportActionBar().setSubtitle(--noteCounts + " Notes");
	}
	public void onEventMainThread(NoteUpdateEvent event){
		if(groupName.equals(event.getNoteDatabaseArray().group)){
			int count = list.size();
			for(int index= 0;index<count;index++){
				if(list.get(index).sdfId == event.getOldNoteId()){
					list.get(index).sdfId = event.getNewNoteId();
					list.get(index).date = event.getNoteDatabaseArray().date;
					list.get(index).detail = event.getNoteDatabaseArray().content;
					list.get(index).isOnCloud = event.getNoteDatabaseArray().is_on_cloud;
					list.get(index).time = event.getNoteDatabaseArray().time;
					list.get(index).title = event.getNoteDatabaseArray().Title;
					list.get(index).uuid = event.getUuid();
					recycleViewAdapter.notifyDataSetChanged();
				}
			}
		}
		//refresh();
	}
	public void onEventMainThread(ChangeNoteGroupEvent event){
		if(groupName .equals(event.getOldGroup())){
			int size = list.size();
			for(int index = 0;index <size;index++){
				NoteReelItemArray array = list.get(index);
				if(array.sdfId == event.getSdfId()){
					list.remove(array);
					subNoteCounts();
				}
			}
		}else if(groupName .equals(event.getCurrentGroup())){
			NoteAllArray array =  NoteDBHelper.getInstance().getNoteById(event.getSdfId());
			NoteReelItemArray groupArray = new NoteReelItemArray(new NoteDatabaseArray(array),event.getSdfId());
			list.add(groupArray);
			subNoteCounts();
		}
		recycleViewAdapter.notifyDataSetChanged();
	}
	public void onEventMainThread(ThemeColorPairChangedEvent e){
		((NoteGroupItemAdapter)recycleViewAdapter).setThemeMain(e.getCurrentColorPair().mainColor);
		recyclerView.invalidate();
	}

	@Override
	protected void themePatch() {
		//super.themePatch();
//		collapse_layout.setExpandedTitleColor(getResources().getColor(R.color.md_text));
//		collapse_layout.setCollapsedTitleTextColor(getResources().getColor(R.color.md_text));
//		collapse_layout.setContentScrimColor(ThemeController.getCurrentColor().getMainColor());
		collapse_layout.setCollapsedTitleTextColor(getResources().getColor(R.color.md_text));
	}

	@Override
	protected void afterSetUpRecycleView() {
		((NoteGroupItemAdapter)recycleViewAdapter).setThemeMain(mutedColor);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (fab_op_pannel.isShown()) {
			FabTransformation.with(note_add_fab).setOverlay(overlay). transformFrom(fab_op_pannel);
		}
	}
}
