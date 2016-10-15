package com.seki.noteasklite.Fragment.Ask;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.malinskiy.superrecyclerview.swipe.SparseItemRemoveAnimator;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.seki.noteasklite.Activity.Ask.CommunityEditActivity;
import com.seki.noteasklite.Activity.Ask.InnerQuestionActivity;
import com.seki.noteasklite.Adapter.QuestionItemClickListener;
import com.seki.noteasklite.Adapter.QuestionItemLongClickListener;
import com.seki.noteasklite.Adapter.QuestionRecycleViewAdapter;
import com.seki.noteasklite.Base.BaseFragment;
import com.seki.noteasklite.Controller.CommunityController;
import com.seki.noteasklite.Controller.ThemeController;
import com.seki.noteasklite.CustomControl.RecycleViewHelper;
import com.seki.noteasklite.CustomControl.TagSelector.AutoTagCompleteAdapter;
import com.seki.noteasklite.CustomControl.TagSelector.TagAutoCompleteTextView;
import com.seki.noteasklite.DataUtil.Bean.QuestionListBean;
import com.seki.noteasklite.DataUtil.Bean.TagSearchResult;
import com.seki.noteasklite.DataUtil.BusEvent.UpdateTagEvent;
import com.seki.noteasklite.DataUtil.GetQuestionDataParams;
import com.seki.noteasklite.DataUtil.QuestionItemData;
import com.seki.noteasklite.MyApp;
import com.seki.noteasklite.R;
import com.seki.noteasklite.ThirdWrapper.PowerListener;
import com.seki.noteasklite.Util.FuckBreaker;
import com.seki.noteasklite.Util.QuickaskErrorCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class AskFragment extends BaseFragment {

    private GetQuestionDataParams getQuestionDataParams = new GetQuestionDataParams();
    private List<QuestionItemData> questionItemDataList;
    private View rootView;
    private SuperRecyclerView askQuestionListView;
    private QuestionRecycleViewAdapter mQuestionListAdapter;
    LinearLayoutManager linearLayoutManager;
    Drawable rootViewDrawable;
    LinearLayout askQuestionTagsLinearLayout;
    View layout_nodata_empty_view;
    ProgressWheel loadingBg;
	public AskFragment()
	{
		super();
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getDefinition();
		registerEvent();
        loadQuestionData();
	}
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		rootView=inflater.inflate(R.layout.fragment_ask, null);
		return rootView;
	}
	private void getDefinition()
	{
        layout_nodata_empty_view = rootView.findViewById(R.id.empty_view);
        layout_nodata_empty_view.setVisibility(View.GONE);
		askQuestionListView = (SuperRecyclerView)rootView.findViewById(R.id.ask_question_list);
		setUpQuestionRecycleView();
		questionItemDataList = new ArrayList<>();
		mQuestionListAdapter=new QuestionRecycleViewAdapter(AskFragment.this.getActivity(),questionItemDataList);
		rootViewDrawable = rootView.getBackground();
		askQuestionTagsLinearLayout = (LinearLayout)rootView.findViewById(R.id.ask_question_tags);
        loadingBg = $(R.id.loading_bg);
        loadingBg.setBarColor(ThemeController.getCurrentColor().mainColor);
	}
    private SparseItemRemoveAnimator   mSparseAnimator;
	private void setUpQuestionRecycleView() {
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        askQuestionListView.setLayoutManager(linearLayoutManager);
        mSparseAnimator = new SparseItemRemoveAnimator();
        askQuestionListView.getRecyclerView().setItemAnimator(mSparseAnimator);
        askQuestionListView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int firstVisibleItemPosition;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView,dx,dy);
               // MainActivity.toggleFABAnimation(getActivity(), dy, ((MainActivity) getActivity()).getFloatingActionButton(),true);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int questionItemDataListIndex;
				firstVisibleItemPosition = RecycleViewHelper.getRecycleViewFirstViewPosition(recyclerView);
                questionItemDataListIndex = Math.min(firstVisibleItemPosition, questionItemDataList.size() - 1);
                if (questionItemDataListIndex >= 0) {
                    List<String> tagList = null;
                    String tagJson = questionItemDataList.get(questionItemDataListIndex).questionTagInJson;
                    try {
                        tagList = new Gson().fromJson(tagJson, new TypeToken<List<String>>() {
                        }.getType());
                    } catch (JsonSyntaxException jse) {
                        tagList = null;
                    }
                    int tagNum = 0;
                    int askQuestionTagsLinearLayoutChildNum = askQuestionTagsLinearLayout.getChildCount();
                    if (tagList == null || tagList.size()==0) {
                        //when numm,just remove all the childView
                        for (int i = askQuestionTagsLinearLayoutChildNum - 1; i > tagNum - 1; i--) {
                            askQuestionTagsLinearLayout.removeViewAt(i);
                        }
                    } else {
                        tagNum = tagList.size();
                        for (int i = askQuestionTagsLinearLayoutChildNum ; i < tagNum; i++) {
                            askQuestionTagsLinearLayout.inflate(getActivity(), R.layout.ask_question_tag, askQuestionTagsLinearLayout);
                        }

                        for (int i = 0; i < tagNum; i++) {
                            ((TextView) askQuestionTagsLinearLayout.getChildAt(i).findViewById(R.id.ask_question_tag_text)).setText(
                                    tagList.get(i)
                            );
                        }
                    }
                }
            }
        });
	}
	private void registerEvent()
	{

        askQuestionListView.setRefreshingColorResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimary, R.color.colorAccent);
        askQuestionListView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareQuestion();
                getQuestionDataParams.isRefresh = true;
                loadQuestionData();
            }
        });
        askQuestionListView.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                prepareLoadMoreQuestion();
                loadQuestionData();
            }
        }, 1);
        askQuestionListView.setAdapter(mQuestionListAdapter);

        mQuestionListAdapter.setOnItemClickListener(new QuestionItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                QuestionItemData questionItemData = questionItemDataList.get(position);
                final Intent intent = new Intent();
                intent.putExtra("questionId", questionItemData.questionItemId);
                intent.putExtra("question_title", questionItemData.questionItemTitle);
                intent.setClass(getActivity(), InnerQuestionActivity.class);
                getActivity().startActivity(intent);
            }
        });
		mQuestionListAdapter.setOnItemLongClickListener(new QuestionItemLongClickListener() {
			@Override
			public void onItemLongClick(View v, final int position) {
				ArrayList<String> items = new ArrayList<String>();
				if(MyApp.authorBean.author<3){
					items.add("移除条目");
                    items.add("更改讨论标签");
                    items.add("编辑条目");
					final String [] itemsArray = items.toArray(new String[1]);
					AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
							.setItems(itemsArray, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialogInterface, int i) {
									switch (itemsArray[i]){
										case "移除条目":
											CommunityController.removePost(questionItemDataList.get(position).questionItemId);
											questionItemDataList.remove(position);
											mQuestionListAdapter.notifyDataSetChanged();
											dialogInterface.dismiss();
											break;
                                        case "更改讨论标签":
                                            buildCategoryDialog(position);
                                            dialogInterface.dismiss();
                                            break;
                                        case "编辑条目":
                                            editPost(position);
                                            dialogInterface.dismiss();
                                            break;
									}
								}
							});
					b.show();
				}
			}
		});
	}

    private void editPost(int position) {
        QuestionItemData data = questionItemDataList.get(position);
        CommunityEditActivity.start(getActivity(),data.questionItemTitle,data.questionItemDetail,data.questionItemId);
    }

    private void buildCategoryDialog(final int position) {

        View categoryDialog = LayoutInflater.from(getActivity()).inflate(R.layout.layout_category_list, null,false);
        final LinearLayout tagSetLinearLayout = (LinearLayout) categoryDialog.findViewById(R.id.tag_set);
        final TagAutoCompleteTextView tagAutoCompleteTextView = (TagAutoCompleteTextView) categoryDialog.findViewById(R.id.tag_search_view);
        final List<String> tagSetFromServer = new ArrayList<>();
        List<String> temp = new Gson().fromJson(questionItemDataList.get(position).questionOuterCategory,new TypeToken<List<String>>(){}.getType());
        final List<String> tags ;
        if(temp == null){
            tags = new ArrayList<>();
        }else{
            tags = temp;
        }


        final AutoTagCompleteAdapter<String> tagAdapter = new AutoTagCompleteAdapter<String>(getActivity(), tagSetFromServer);
        //final ArrayAdapter<String> tagAdapter = new ArrayAdapter<String>(categoryDialog.getContext(),android.R.layout.simple_dropdown_item_1line,new String[]{"ddsdsd"});
        tagAutoCompleteTextView.setAdapter(tagAdapter);
        tagAutoCompleteTextView.setThreshold(1);
        tagAdapter.notifyDataSetChanged();
        tagAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //wait to limit the lenth to trigger the communication
                tagSetFromServer.add("loading...");
                tagAdapter.notifyDataSetChanged();
                final String tagKeyWordWrapper = s.toString();
                MyApp.getInstance().volleyRequestQueue.add(new StringRequest(Request.Method.POST, "http://2.diandianapp.sinaapp.com/NONo/search_tag.php"
                        , new PowerListener(){
                    @Override
                    public void onCorrectResponse(String s) {
                        super.onCorrectResponse(s);
                        final TagSearchResult tagSearchResult = new Gson().fromJson(s, new TypeToken<TagSearchResult>() {
                        }.getType());
                        if (tagSearchResult.state_code < 0) {
                            tagSetFromServer.clear();
                            tagSetFromServer.add("暂时没有相关Tag");
                            tagSetFromServer.add("+添加新的Tag");
                            tagAdapter.notifyDataSetChanged();
                            tagAutoCompleteTextView.setOnItemSelectedListener(new TagAutoCompleteTextView.OnItemSelectedListener() {
                                @Override
                                public void onSelect(CharSequence s) {
                                    switch (s.toString()) {
                                        case "+添加新的Tag":
                                            //post the tag to server
                                            MyApp.getInstance().volleyRequestQueue.add(new StringRequest(Request.Method.POST,
                                                    "http://2.diandianapp.sinaapp.com/NONo/add_tag.php"
                                                    , new Response.Listener<String>() {

                                                @Override
                                                public void onResponse(String s) {

                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {

                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<String, String>();
                                                    params.put("tag", tagKeyWordWrapper);
                                                    return params;
                                                }
                                            });
                                            //add the tag to view
                                            final LinearLayout tagText = (LinearLayout) tagSetLinearLayout.inflate(getActivity(), R.layout.layout_tag_view, tagSetLinearLayout);
                                            ((TextView) tagText.findViewById(R.id.text)).setText(tagKeyWordWrapper);
                                            ((TextView) tagText.findViewById(R.id.text)).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    tags.remove(((TextView) tagText.findViewById(R.id.text)).getText().toString());
                                                    tagSetLinearLayout.removeViewAt(0);
                                                    for (int i = 0; i < tagSetLinearLayout.getChildCount(); i++) {
                                                        ((TextView) tagSetLinearLayout.getChildAt(i).findViewById(R.id.text))
                                                                .setText(tags.get(i));
                                                    }
                                                }
                                            });
                                            tags.add(tagKeyWordWrapper);
                                            for (int i = 0; i < tagSetLinearLayout.getChildCount(); i++) {
                                                ((TextView) tagSetLinearLayout.getChildAt(i).findViewById(R.id.text))
                                                        .setText(tags.get(i));
                                            }
                                            break;
                                    }
                                }
                            });
                        } else if (tagSearchResult.state_code == 0) {
                            tagSetFromServer.clear();
                            for (String tag :
                                    tagSearchResult.tag_list) {
                                tagSetFromServer.add(tag);
                            }
                            tagSetFromServer.add("+添加新的Tag");
                            tagAdapter.notifyDataSetChanged();
                            tagAutoCompleteTextView.setOnItemSelectedListener(new TagAutoCompleteTextView.OnItemSelectedListener() {
                                @Override
                                public void onSelect(CharSequence s) {
                                    switch (s.toString()) {
                                        case "+添加新的Tag":
                                            //post the tag to server
                                            MyApp.getInstance().volleyRequestQueue.add(new StringRequest(Request.Method.POST,
                                                    "http://2.diandianapp.sinaapp.com/NONo/add_tag.php"
                                                    , new Response.Listener<String>() {

                                                @Override
                                                public void onResponse(String s) {

                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {

                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<String, String>();
                                                    params.put("tag", tagKeyWordWrapper);
                                                    return params;
                                                }
                                            });
                                            //add the tag to view
                                            final LinearLayout tagText = (LinearLayout) (tagSetLinearLayout.inflate(getActivity(), R.layout.layout_tag_view, tagSetLinearLayout));
                                            ((TextView) tagText.findViewById(R.id.text)).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    tags.remove(((TextView) tagText.findViewById(R.id.text)).getText().toString());
                                                    tagSetLinearLayout.removeViewAt(0);
                                                    for (int i = 0; i < tagSetLinearLayout.getChildCount(); i++) {
                                                        ((TextView) tagSetLinearLayout.getChildAt(i).findViewById(R.id.text))
                                                                .setText(tags.get(i));
                                                    }
                                                }
                                            });
                                            tags.add(tagKeyWordWrapper);
                                            ((TextView) (tagText.findViewById(R.id.text))).setText(tagKeyWordWrapper);
                                            for (int i = 0; i < tagSetLinearLayout.getChildCount(); i++) {
                                                ((TextView) tagSetLinearLayout.getChildAt(i).findViewById(R.id.text))
                                                        .setText(tags.get(i));
                                            }
                                            break;
                                        default:
                                            final LinearLayout tagText1 = (LinearLayout) (tagSetLinearLayout.inflate(getActivity(), R.layout.layout_tag_view, tagSetLinearLayout));
                                            ((TextView) tagText1.findViewById(R.id.text)).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    tags.remove(((TextView) tagText1.findViewById(R.id.text)).getText().toString());
                                                    tagSetLinearLayout.removeViewAt(0);
                                                    for (int i = 0; i < tagSetLinearLayout.getChildCount(); i++) {
                                                        ((TextView) tagSetLinearLayout.getChildAt(i).findViewById(R.id.text))
                                                                .setText(tags.get(i));
                                                    }
                                                }
                                            });
                                            tags.add(s.toString());
                                            ((TextView) (tagText1.findViewById(R.id.text))).setText(s.toString());
                                            for (int i = 0; i < tagSetLinearLayout.getChildCount(); i++) {
                                                ((TextView) tagSetLinearLayout.getChildAt(i).findViewById(R.id.text))
                                                        .setText(tags.get(i));
                                            }
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onJSONStringParseError() {
                        super.onJSONStringParseError();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("key", tagKeyWordWrapper);
                        return params;
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("整理讨论标签");
        builder.setView(categoryDialog);
        builder.setPositiveButton("整理完毕", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CommunityController.updatePostTags(questionItemDataList.get(position).questionItemId,tags);
                dialog.dismiss();
            }
        });
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
			}
		});
        builder.show();
    }
	private void decodequestionItemDataListFromString(List<QuestionItemData> questionItemDataList, String s) {
		QuestionListBean questionListBean = new Gson().fromJson(s,new TypeToken<QuestionListBean>(){}.getType());
		if(questionListBean.state_code !=0){
            afterDataLoaded();
			return ;
		}
		getQuestionDataParams.stateCode = questionListBean.state_code;
		getQuestionDataParams.maxQuestionId =
				Integer.valueOf(questionListBean.getData().getMin_question_id())
				;
		if (getQuestionDataParams.isRefresh) {
			questionItemDataList.clear();
		}

		for (QuestionListBean.DataEntity.QuestionListEntity questionListEntity:
				questionListBean.getData().getQuestion_list()) {
			QuestionItemData questionItemData = new QuestionItemData();
			questionItemData.questionItemType = QuestionItemData.QuestionItemType.TYPE_NORMAL;
			questionItemData.questionItemId = questionListEntity.getQuestion_id();
			questionItemData.questionItemTitle = questionListEntity.getQuestion_title();
			questionItemData.questionItemDetail = questionListEntity.getQuestion_detail()==null?"":questionListEntity.getQuestion_detail();
			questionItemData.questionRaiserUniversity = questionListEntity.getRaiser_university();
			questionItemData.questionRaiserSchool = questionListEntity.getRaiser_subject();
			questionItemData.questionRaiserRealname = questionListEntity.getRaiser_realname();
			questionItemData.questionRaiseTime =questionListEntity.getQuestion_raise_time();
			questionItemData.questionOuterCategory = questionListEntity.getQuestion_outer_category();
			questionItemData.questionInnerCategory = questionListEntity.getQuestion_inner_category();
			questionItemData.questionAnswerNum = String.valueOf(questionListEntity.getQuestion_answer_num()) ;
			questionItemData.questionHotDegree = questionListEntity.getQuestion_hot_degree();
			//questionItemData.questionAbstract = questionListEntity.getQuestion_abstract();
            String detail = FuckBreaker.fuckBreakerAndSpace(questionItemData.questionItemDetail);
			questionItemData.questionAbstract = TextUtils.substring(detail,0,
                    detail.length()<80?detail.length():80);
			questionItemData.questionRaiserHeadImg =questionListEntity.getRaiser_headimg();
			questionItemData.questionTagInJson = questionListEntity.getQuestion_tag_list();
			questionItemData.questionRaiserId = questionListEntity.getQuestion_raiser_id();
			questionItemDataList.add(questionItemData);
		}
        afterDataLoaded();
	}
	private void prepareLoadDataEnd() {
        if(loadingBg.isShown()){
            loadingBg.setVisibility(View.GONE);
        }
        if(getQuestionDataParams.isRefresh) {
            if(Build.VERSION.SDK_INT<=15) {
                rootView.setBackgroundDrawable(rootViewDrawable);
            }else {
                rootView.setBackground(rootViewDrawable);
            }
        }
	}
    //加载更多前的准备
	private void prepareLoadMoreQuestion() {
		getQuestionDataParams.isRefresh = false;
		getQuestionDataParams.stateCode = QuickaskErrorCode.OTHERERRO;
	}
    //日常刷新的准备
    private void prepareQuestion(){

        getQuestionDataParams.stateCode = QuickaskErrorCode.OTHERERRO;
        rootView.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
    }
	private void loadQuestionData() {
        layout_nodata_empty_view.setVisibility(View.GONE);
        if(questionItemDataList.size() == 0){
            loadingBg.setVisibility(View.VISIBLE);
        }
		StringRequest getQuestionListRequest = new StringRequest(Request.Method.POST,
				"http://diandianapp.sinaapp.com/fetch_question_data.php"
				, new PowerListener() {
			@Override
			public void onCorrectResponse(final String s) {
				super.onCorrectResponse(s);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prepareLoadDataEnd();
                        decodequestionItemDataListFromString(questionItemDataList, s);
                        if(questionItemDataList.isEmpty()){
                            layout_nodata_empty_view.setVisibility(View.VISIBLE);
                        }
                    }
                },100);
			}

			@Override
			public void onJSONStringParseError() {
				super.onJSONStringParseError();
                if(loadingBg.isShown()){
                    loadingBg.setVisibility(View.GONE);
                }
                if(questionItemDataList.isEmpty()){
                    layout_nodata_empty_view.setVisibility(View.VISIBLE);
                }
			}

            @Override
            public void onResponse(String s) {
                super.onResponse(s);
            }
        }, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
                prepareLoadDataEnd();
                if(questionItemDataList.isEmpty()){
                    layout_nodata_empty_view.setVisibility(View.VISIBLE);
                }
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("is_refresh", String.valueOf(getQuestionDataParams.isRefresh));
				map.put("start_page", String.valueOf(getQuestionDataParams.maxQuestionId));
				return map;
			}
		};
		MyApp.getInstance().volleyRequestQueue.add(getQuestionListRequest);
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	public void afterDataLoaded() {
		switch(getQuestionDataParams.stateCode)
		{
			case QuickaskErrorCode.OTHERERRO:
				mQuestionListAdapter.notifyDataSetChanged();
				break;
			case QuickaskErrorCode.OPERSUCCESS:
				mQuestionListAdapter.notifyDataSetChanged();
				break;
		}
        if(askQuestionListView.isLoadingMore()){
            askQuestionListView.setLoadingMore(false);
        }
	}

	@Override
	public void onResume() {
        super.onResume();
        prepareQuestion();
        getQuestionDataParams.isRefresh = true;

	}

	@Override
	protected void themePatch() {
		super.themePatch();
		$(R.id.banner).setBackgroundColor(ThemeController.getCurrentColor().mainColor);
        mQuestionListAdapter.setThemeColor(ThemeController.getCurrentColor().mainColor);
	}
    public void onEvent(UpdateTagEvent e){

        for(QuestionItemData data
                :questionItemDataList){
            if(TextUtils.equals(data.questionItemId,e.getQuestion_id())){
                data.questionTagInJson = new Gson().toJson(e.getTagList());
            }
        }
    }
}
