package com.seki.noteasklite.Adapter;

import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.seki.noteasklite.Activity.ChatActivity;
import com.seki.noteasklite.R;

import java.io.File;
import java.util.List;

/**
 * Created by 七升 on 2015/7/26.
 */
public class ChatAdapter
//		extends RecyclerView.Adapter
{
//
//	//private MediaPlayer mediaPlayer;
//	Html.ImageGetter imageGetter = new Html.ImageGetter() {
//
//		public Drawable getDrawable(String source) {
//			Drawable drawable=null;
//			drawable= Drawable.createFromPath(source);
//			    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//			return drawable;};
//	};
//
//	public static interface OnRecyclerViewListener {
//		void onItemClick(View v, int position);
//
//		boolean onItemLongClick(View v, int position);
//	}
//
//	private OnRecyclerViewListener onRecyclerViewListener;
//
//	public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
//		this.onRecyclerViewListener = onRecyclerViewListener;
//	}
//
//	private List<ChatActivity.ChatArray> list;
//
//	public ChatAdapter(List<ChatActivity.ChatArray> list) {
//		this.list = list;
//	}
//
//	@Override
//	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
//
//		final ChatViewHolder holder = (ChatViewHolder) viewHolder;
//		holder.position = i;
//		final ChatActivity.ChatArray result = list.get(i);
//			holder.msgTv.setText(Html.fromHtml(result.text));
//		if(result.video.length()>0) {
//			holder.frameLayout.setVisibility(View.VISIBLE);
//			holder.path=result.video;//取得视频本地地址
//			holder.play(0, result.video);
//		}
//		else {
//			holder.frameLayout.setVisibility(View.GONE);
//		}
//	}
//
//	@Override
//	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//		ChatActivity.ChatArray array=list.get(i);
//		if(array.who) {
//			View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_chat_local, null,false);
//			//LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//			//view.setLayoutParams(lp);
//			return new ChatViewHolder(view);
//		}else {
//			View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_chat_remote,null,false);
//			return new ChatViewHolder(view);
//		}
//	}
//
//	@Override
//	public int getItemViewType(int position) {
//		return position;
//	}
//
//	@Override
//	public int getItemCount() {
//		return list.size();
//	}
//
//	class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
//		public View rootView;
//		public TextView msgTv;
//		//public TextView msgSelfTv;
//		//public RelativeLayout relativeLayout;
//		//public RelativeLayout relativeLayoutSelf;
//		public SurfaceView surfaceView;
//		public SurfaceHolder surfaceHolder;
//		public FrameLayout frameLayout;
//		public ImageButton imageButton;
//		public SeekBar seekBar;
//		public int position;
//
//		private String path="";//视频本地地址
//		private MediaPlayer mediaPlayer;
//		private int currentPosition = 0;
//		//private boolean isPlaying;
//
//		private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
//			// SurfaceHolder被修改的时候回调
//			@Override
//			public void surfaceDestroyed(SurfaceHolder holder) {
//				// 销毁SurfaceHolder的时候记录当前的播放位置并停止播放
//				if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//					currentPosition = mediaPlayer.getCurrentPosition();
//					mediaPlayer.stop();
//				}
//			}
//
//			@Override
//			public void surfaceCreated(SurfaceHolder holder) {
//				if (currentPosition > 0) {
//					// 创建SurfaceHolder的时候，如果存在上次播放的位置，则按照上次播放位置进行播放
//					play(currentPosition,path);
//					currentPosition = 0;
//				}
//			}
//
//			@Override
//			public void surfaceChanged(SurfaceHolder holder, int format, int width,
//									   int height) {
//				if (mediaPlayer != null) {
//					mediaPlayer.setDisplay(holder);
//					imageButton.setImageResource(R.mipmap.ic_play_circle_fill_grey600_48dp);
//					imageButton.setVisibility(View.VISIBLE);
//				}
//			}
//
//		};
//
//
//		/**
//		 * 开始播放
//		 *
//		 * @param msec 播放初始位置
//		 */
//		protected void play(final int msec,final String path) {
//			// 获取视频文件地址
//			File file = new File(path);
//			if (!file.exists()) {
//				return;
//			}
//			try {
//				mediaPlayer = new MediaPlayer();
//				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//				// 设置播放的视频源
//				mediaPlayer.setDataSource(file.getAbsolutePath());
//				// 设置显示视频的SurfaceHolder
//
//				mediaPlayer.prepareAsync();
//				mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//
//					@Override
//					public void onPrepared(MediaPlayer mp) {
//						//mediaPlayer.setDisplay(surfaceView.getHolder());
//						//mediaPlayer.start();
//						// 按照初始位置播放
//						mediaPlayer.seekTo(msec);
//						seekBar.setMax(mediaPlayer.getDuration());
//						new uiHandler().sendEmptyMessage(1);
//						// 设置进度条的最大进度为视频流的最大播放时长
//						// 开始线程，更新进度条的刻度
//
//					}
//				});
//				mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//					@Override
//					public void onCompletion(MediaPlayer mp) {
//						// 在播放完毕被回调
//						imageButton.setImageResource(R.mipmap.ic_play_circle_fill_grey600_48dp);
//						imageButton.setVisibility(View.VISIBLE);
//						mp.seekTo(0);
//					}
//				});
//
//				mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//
//					@Override
//					public boolean onError(MediaPlayer mp, int what, int extra) {
//						// 发生错误重新播放
//						play(0,path);
//						//isPlaying = false;
//						return false;
//					}
//				});
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		public ChatViewHolder(View itemView) {
//			super(itemView);
//			seekBar=(SeekBar)itemView.findViewById(R.id.chat_seekbar);
//			seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//				@Override
//				public void onStopTrackingTouch(SeekBar seekBar) {
//					// 当进度条停止修改的时候触发
//					// 取得当前进度条的刻度
//					int progress = seekBar.getProgress();
//					if (mediaPlayer != null) {
//						// 设置当前播放的位置
//						mediaPlayer.seekTo(progress);
//					}
//				}
//
//				@Override
//				public void onStartTrackingTouch(SeekBar seekBar) {
//
//				}
//
//				@Override
//				public void onProgressChanged(SeekBar seekBar, int progress,
//											  boolean fromUser) {
//
//				}
//			});
//			rootView=(RelativeLayout)itemView.findViewById(R.id.chat_root);
//			msgTv = (TextView) itemView.findViewById(R.id.chat_text);
//			surfaceView = (SurfaceView) itemView.findViewById(R.id.chat_video);
//			surfaceView.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					imageButton.setVisibility(View.VISIBLE);
//				}
//			});
//			surfaceHolder=surfaceView.getHolder();
//			surfaceHolder.addCallback(callback);
//			imageButton=(ImageButton)itemView.findViewById(R.id.chat_video_control);
//			imageButton.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					if(mediaPlayer.isPlaying()){
//						mediaPlayer.pause();
//						imageButton.setImageResource(R.mipmap.ic_play_circle_fill_grey600_48dp);
//					}else {
//						mediaPlayer.start();
//						new Thread() {
//
//							@Override
//							public void run() {
//								try {
//									//isPlaying = true;
//									while (mediaPlayer.isPlaying()) {
//										int current = mediaPlayer.getCurrentPosition();
//										seekBar.setProgress(current);
//
//										sleep(500);
//									}
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//							}
//						}.start();
//						imageButton.setImageResource(R.mipmap.ic_pause_circle_fill_grey600_48dp);
//						new Handler().postDelayed(new Runnable() {
//							@Override
//							public void run() {
//								if(mediaPlayer.isPlaying()) {
//									imageButton.setVisibility(View.GONE);
//								}
//							}
//						},1000);
//					}
//				}
//			});
//			frameLayout=(FrameLayout)itemView.findViewById(R.id.chat_video_frm);
//			msgTv.setOnClickListener(this);
//			msgTv.setOnLongClickListener(this);
//		}
//
//		@Override
//		public void onClick(View v) {
//			if (null != onRecyclerViewListener) {
//				onRecyclerViewListener.onItemClick(v, position);
//			}
//		}
//
//		@Override
//		public boolean onLongClick(View v) {
//			if (null != onRecyclerViewListener) {
//				return onRecyclerViewListener.onItemLongClick(v, position);
//			}
//			return false;
//		}
//
//		class uiHandler extends Handler {
//
//			public uiHandler(){
//
//			}
//
//			public uiHandler(Looper L){
//				super(L);
//			}
//
//			@Override
//			public void handleMessage(Message msg) {
//				super.handleMessage(msg);
//				int mVideoWidth = mediaPlayer.getVideoWidth();
//				int mVideoHeight = mediaPlayer.getVideoHeight();
//				int width=rootView.getWidth();
//				int height=rootView.getRootView().getHeight()/2;
//				ViewGroup.LayoutParams sufaceviewParams = (ViewGroup.LayoutParams) surfaceView.getLayoutParams();
//				if ( mVideoWidth > width  ) {
//					sufaceviewParams.width=width;
//					sufaceviewParams.height = width * mVideoHeight / mVideoWidth;
//					if(sufaceviewParams.height>height){
//						sufaceviewParams.width=mVideoWidth*height/mVideoHeight;
//						sufaceviewParams.height=height;
//					}
//				} else {
//					sufaceviewParams.width = width;
//					sufaceviewParams.height=mVideoHeight;
//					if(sufaceviewParams.height>height){
//						sufaceviewParams.width=mVideoWidth*height/mVideoHeight;
//						sufaceviewParams.height=height;
//					}
//				}
//				surfaceView.setLayoutParams(sufaceviewParams);
//			}
//		}
//	}
}
