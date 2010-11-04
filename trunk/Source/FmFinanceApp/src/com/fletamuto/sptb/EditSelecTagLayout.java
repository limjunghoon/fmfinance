package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fletamuto.sptb.data.ExpenseTag;
import com.fletamuto.sptb.db.DBMgr;

/**
 * @author yongbban
 *
 */
public class EditSelecTagLayout extends EditSelectItemBaseLayout {  	
	public static final int ACT_TAG_ADD = MsgDef.ActRequest.ACT_TAG_ADD;
	protected ArrayList<ExpenseTag> mArrTag = null;
	private TagButtonAdpter mAdapterTag;
	
	

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   
    }

	@Override
	protected void createItem(String itemName) {
		if (itemName.length() == 0) return;
		
		ExpenseTag tag = new ExpenseTag();
		tag.setName(itemName);
		
		if (DBMgr.addTag(tag) == -1) {
			Log.e(LogTag.LAYOUT, ":: Fail to create TAG Item");
		}
		updateTagAdapter();
		
	}
	
	@Override
	protected void updateItem(int editTagID, String itemName, int editPosition) {
		if (itemName.length() == 0) return;
		
		ExpenseTag tag = DBMgr.getTag(editTagID);
		if (tag == null) {
			Log.e(LogTag.LAYOUT, ":: Fail to load tag");
		}
		
		tag.setName(itemName);
		if (DBMgr.updateTag(tag) == false) {
			Log.e(LogTag.LAYOUT, ":: Fail update the tag");
			return;
		}

		
		mArrTag.set(editPosition, tag);
		mAdapterTag.notifyDataSetChanged();
	}
	
    private void updateTagAdapter() {
    	
    	if (mAdapterTag != null) {
    		mAdapterTag.clear();
    	}
    	
    	getData();
    	setAdaper();
	}
	

	@Override
	protected void onDeleteButtonClick(int deleteItemID, int deletePosition) {
		if (DBMgr.deleteTag(deleteItemID) == 0) {
			return;
		}
	
		mArrTag.remove(deletePosition);
		mAdapterTag.notifyDataSetChanged();
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
        
    	final ListView listEditSelectItem = (ListView)findViewById(R.id.ListViewEditSelect);
    	mAdapterTag = new TagButtonAdpter(this, R.layout.edit_select_item, mArrTag);
    	listEditSelectItem.setAdapter(mAdapterTag);
    	
    	setListViewListener(listEditSelectItem);
    	
	}
	
	
	View.OnClickListener tagListener = new View.OnClickListener() {
		public void onClick(View v) {
			ExpenseTag tag= (ExpenseTag)v.getTag();
			onClickTagButton(tag);
		}
	};
	
	protected void onClickTagButton(ExpenseTag tag) {
//		Intent intent = new Intent();
//		intent.putExtra(MsgDef.ExtraNames.TAG_ID, tag.getID());
//		intent.putExtra(MsgDef.ExtraNames.TAG_NAME, tag.getName());
//		setResult(RESULT_OK, intent);
//		finish();
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
			
			TextView tagName = (TextView)convertView.findViewById(R.id.TVEditSelectItem);
			tagName.setText(tag.getName());
			
			setDeleteBtnListener(convertView, tag.getID(), position);
			setEditBtnListener(convertView, tag, position);
			
			return convertView;
		}
	}









	

	
	
}
