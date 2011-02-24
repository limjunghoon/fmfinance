package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseTag;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * �±׸� �����ϴ� ���̾ƿ�
 * @author yongbban
 * @version 1.0.0.1
 */
public class SelectTagLayout extends SelectGridBaseLayout {
	public static final int ACT_TAG_EDIT = MsgDef.ActRequest.ACT_TAG_EDIT;
	protected ArrayList<ExpenseTag> mArrTag = null;
	private TagButtonAdpter mAdapterTag;
	
	//���� mode ���� �˱� ���� ����
	private boolean editTagMode = false;
	
	//���� �±� 
	ExpenseTag dTag;
	
	final static int ACT_EDIT_TAG = 0;
	final static int ACT_ADD_TAG = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//���� ��ư ó��
        getGridRightBtn().setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				setEditTagMode(true);	
				setAdaper();
			}
		});
	}
	
	public void setEditTagMode (boolean mode) {
		editTagMode = mode;
	}
	public boolean getEditTagMode () {
		return editTagMode;
	}
	
	protected void getTagList() {
		mArrTag = DBMgr.getTag();
		
		//main category Add ��ư �߰� �� ���� 
		ExpenseTag addButton = new ExpenseTag();
		addButton.setID(-2);
		addButton.setName("+");
		addButton.setImageIndex(49);
		
		mArrTag.add(addButton);
	}
	
	@Override
	public void getData() {
		getTagList();
	}

	@Override
	public void setAdaper() {
		if (mArrTag == null) return;
        
    	final GridView gridCategory = (GridView)findViewById(R.id.GVSelect);
    	mAdapterTag = new TagButtonAdpter(this, R.layout.grid_category_select, mArrTag);
    	gridCategory.setAdapter(mAdapterTag);
	}
	
	
	View.OnClickListener tagListener = new View.OnClickListener() {
		public void onClick(View v) {
			ExpenseTag tag= (ExpenseTag)v.getTag();
			onClickTagButton(tag);
		}
	};
	
	protected void onClickTagButton(ExpenseTag tag) {
		
    	//�߰� ��ư�� ���� ���� �� ó��
    	if (tag.getID() == -2) {
			Intent intent = new Intent(SelectTagLayout.this, NewEditCategoryLayout.class);
			intent.putExtra("EDIT_TITLE", "�±� �߰�");
			intent.putExtra("EDIT_MODE", "TAG_ADD");

			startActivityForResult(intent, ACT_ADD_TAG);
			return;
		}
    	
		if (getEditTagMode()) {
			//�������� �Ѿ� ���� ȭ��
			Intent intent = new Intent(SelectTagLayout.this, NewEditCategoryLayout.class);
			intent.putExtra("TAG_ID", tag.getID());
			intent.putExtra("TAG_NAME", tag.getName());
			intent.putExtra("TAG_IMAGE_INDEX", tag.getImageIndex());
			intent.putExtra("EDIT_TITLE", "�±� ����");
			intent.putExtra("EDIT_MODE", "TAG_EDIT");

			startActivityForResult(intent, ACT_EDIT_TAG);
			return;
		} else {
			Intent intent = new Intent();
			intent.putExtra(MsgDef.ExtraNames.TAG_ID, tag.getID());
			intent.putExtra(MsgDef.ExtraNames.TAG_NAME, tag.getName());
			setResult(RESULT_OK, intent);
			finish();
		}
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
			
			FrameLayout fLayout = (FrameLayout) convertView.findViewById (R.id.BtnGridItemFL);
			ImageView button = (ImageView)convertView.findViewById(R.id.BtnGridItem);
			ImageView imgBtn = (ImageView) convertView.findViewById(R.id.deleteCategory);
			imgBtn.setOnClickListener(deleteTagListener);
			imgBtn.setTag(tag);
			TextView tv = (TextView) convertView.findViewById (R.id.GridItemText);
			
			if (getEditTagMode()) {
				imgBtn.setVisibility(ImageButton.VISIBLE);
			}
			
			if (tag.getID() < -1) {
				button.setImageResource(ConstantImagesArray.TAG_IMAGES[49]);
			} else {
				button.setImageResource(ConstantImagesArray.TAG_IMAGES[tag.getImageIndex()-1]);
			}
			
			tv.setText(tag.getName());
			button.setOnClickListener(tagListener);
			button.setTag(tag);
			
			//�߰� ��ư���� Check ��ư �� ��Ÿ���� ��� �ڵ�
			if (tag.getID() < -1) {
				imgBtn.setVisibility(ImageButton.INVISIBLE);
			}
			/*
			Button button = (Button)convertView.findViewById(R.id.BtnGridItem);
			button.setText(tag.getName());
			button.setOnClickListener(tagListener);
			button.setTag(tag);
			*/
			
			return convertView;
		}
	}


	@Override
	protected void onEditButtonClick() {
		Intent intent = new Intent(SelectTagLayout.this, EditSelecTagLayout.class);
		startActivityForResult(intent, ACT_TAG_EDIT);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == ACT_EDIT_TAG || requestCode == ACT_ADD_TAG) {
    		
    		if (resultCode == RESULT_OK) {
    			updateTagAdapter();			
    		}
    		return;
    	} 
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void clearAdapter() {
		if (mAdapterTag != null) {
			mAdapterTag.clear();
		}
	}
	
	private void updateTagAdapter() {
    	
    	if (mAdapterTag != null) {
    		mAdapterTag.clear();
    	}
    	
    	getData();
    	setAdaper();
	}
	
    
	//�±� ���� �ϴ� �κ�
	View.OnClickListener deleteTagListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			
			dTag = (ExpenseTag) v.getTag();
	    	
	    	new AlertDialog.Builder(SelectTagLayout.this)
	    	.setMessage("���� �Ͻðڽ��ϱ�?")
	    	.setPositiveButton("����", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					
					if (DBMgr.deleteTag(dTag.getID()) == 0) {
						return;
					}
					mAdapterTag.remove(dTag);
				 
					setAdaper();				
				}
			})
			.setNegativeButton("���", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
				}
			}).show();   	
			
		}
	};
	
	//Back Ű ó��
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	
    	if(keyCode==KeyEvent.KEYCODE_BACK){
    		if (getEditTagMode()) {
    			setEditTagMode(false);
    			setAdaper();
    		} else {
    			disAppearAnimation();
    			setResult(RESULT_CANCELED);
    		}
    	}
      
    	return true;
    }
}
