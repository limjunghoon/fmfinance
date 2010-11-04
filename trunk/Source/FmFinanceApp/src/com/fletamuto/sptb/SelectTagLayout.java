package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.fletamuto.sptb.data.ExpenseTag;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 태그를 선택하는 레이아웃
 * @author yongbban
 * @version 1.0.0.1
 */
public class SelectTagLayout extends SelectGridBaseLayout {
	public static final int ACT_TAG_EDIT = MsgDef.ActRequest.ACT_TAG_EDIT;
	protected ArrayList<ExpenseTag> mArrTag = null;
	private TagButtonAdpter mAdapterTag;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	protected void getTagList() {
		mArrTag = DBMgr.getTag();
		
	}
	
	@Override
	public void getData() {
		getTagList();
	}

	@Override
	public void setAdaper() {
		if (mArrTag == null) return;
        
    	final GridView gridCategory = (GridView)findViewById(R.id.GVSelect);
    	mAdapterTag = new TagButtonAdpter(this, R.layout.grid_select, mArrTag);
    	gridCategory.setAdapter(mAdapterTag);
	}
	
	
	View.OnClickListener tagListener = new View.OnClickListener() {
		public void onClick(View v) {
			ExpenseTag tag= (ExpenseTag)v.getTag();
			onClickTagButton(tag);
		}
	};
	
	protected void onClickTagButton(ExpenseTag tag) {
		Intent intent = new Intent();
		intent.putExtra(MsgDef.ExtraNames.TAG_ID, tag.getID());
		intent.putExtra(MsgDef.ExtraNames.TAG_NAME, tag.getName());
		setResult(RESULT_OK, intent);
		finish();
	}
	
	
	private class TagButtonAdpter extends ArrayAdapter<ExpenseTag> {
		int mResource;
    	LayoutInflater mInflater;
    	
		public TagButtonAdpter(Context context, int resource,
				 List<ExpenseTag> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ExpenseTag tag = (ExpenseTag)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			Button button = (Button)convertView.findViewById(R.id.BtnGridItem);
			button.setText(tag.getName());
			button.setOnClickListener(tagListener);
			button.setTag(tag);
			
			return convertView;
		}
	}


	@Override
	protected void onEditButtonClick() {
		Intent intent = new Intent(SelectTagLayout.this, EditSelecTagLayout.class);
		startActivityForResult(intent, ACT_TAG_EDIT);
		
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_TAG_EDIT) {
			if (resultCode == RESULT_OK) {
			
				updateAdapter();

    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void clearAdapter() {
		if (mAdapterTag != null) {
			mAdapterTag.clear();
		}
	}




	
}
