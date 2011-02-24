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

import com.fletamuto.sptb.data.CardCompanyName;
import com.fletamuto.sptb.db.DBMgr;

public class SelectCardCompanyLayout extends SelectGridBaseLayout {
	public static final int ACT_COMPANY_CARD_NAME_EDIT = MsgDef.ActRequest.ACT_COMPANY_CARD_NAME_EDIT;
	protected ArrayList<CardCompanyName> mCardCompenyNames = null;
	private CardCompenyNameButtonAdpter mAdapterCardCompenyName;
	
	//���� mode ���� �˱� ���� ����
	private boolean editCardCompanyMode = false;
	
	//���� �±� 
	CardCompanyName dCardCompany;
	
	final static int ACT_EDIT_CARDCOMPANY = 0;
	final static int ACT_ADD_CARDCOMPANY = 1;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      //���� ��ư ó��
        getGridRightBtn().setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				setEditCardCompanyMode(true);	
				setAdaper();
			}
		});
    }
	
	public void setEditCardCompanyMode (boolean mode) {
		editCardCompanyMode = mode;
	}
	public boolean getEditCardCompanyMode () {
		return editCardCompanyMode;
	}
	
	protected void getCardCompenyNameList() {
		mCardCompenyNames = DBMgr.getCardCompanyNames();
		
		//main category Add ��ư �߰� �� ���� 
		CardCompanyName addButton = new CardCompanyName();
		addButton.setID(-2);
		addButton.setName("+");
		addButton.setImageIndex(49);
		
		mCardCompenyNames.add(addButton);
	}
	
	@Override
	public void getData() {
		getCardCompenyNameList();
	}

	@Override
	public void setAdaper() {
		if (mCardCompenyNames == null) return;
        
    	final GridView gridCategory = (GridView)findViewById(R.id.GVSelect);
    	mAdapterCardCompenyName = new CardCompenyNameButtonAdpter(this, R.layout.grid_category_select, mCardCompenyNames);
    	gridCategory.setAdapter(mAdapterCardCompenyName);
	}
	
	protected void onClickCardCompenyNameButton(CardCompanyName cardCompenyName) {
		
		//�߰� ��ư�� ���� ���� �� ó��
    	if (cardCompenyName.getID() == -2) {
			Intent intent = new Intent(SelectCardCompanyLayout.this, NewEditCategoryLayout.class);
			intent.putExtra("EDIT_TITLE", "ī��� �߰�");
			intent.putExtra("EDIT_MODE", "CARD_COMPANY_ADD");

			startActivityForResult(intent, ACT_ADD_CARDCOMPANY);
			return;
		}
    	
		if (getEditCardCompanyMode()) {
			//�������� �Ѿ� ���� ȭ��
			Intent intent = new Intent(SelectCardCompanyLayout.this, NewEditCategoryLayout.class);
			intent.putExtra("CARD_COMPANY_ID", cardCompenyName.getID());
			intent.putExtra("CARD_COMPANY_NAME", cardCompenyName.getName());
			intent.putExtra("CARD_COMPANY_IMAGE_INDEX", cardCompenyName.getImageIndex());
			intent.putExtra("EDIT_TITLE", "ī��� ����");
			intent.putExtra("EDIT_MODE", "CARD_COMPANY_EDIT");

			startActivityForResult(intent, ACT_EDIT_CARDCOMPANY);
			return;
		} else {
			Intent intent = new Intent();
			intent.putExtra("CARD_COMPENY_NAME_ID", cardCompenyName.getID());
			setResult(RESULT_OK, intent);
			finish();
		}
		
	}

    View.OnClickListener cardCompenyNameListener = new View.OnClickListener() {
		public void onClick(View v) {
			CardCompanyName cardCompenyName = (CardCompanyName)v.getTag();
			onClickCardCompenyNameButton(cardCompenyName);
		}
	};
	


	private class CardCompenyNameButtonAdpter extends ArrayAdapter<CardCompanyName> {
		int mResource;
    	LayoutInflater mInflater;
    	
		public CardCompenyNameButtonAdpter(Context context, int resource,
				 List<CardCompanyName> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CardCompanyName cardCompanyName = (CardCompanyName)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			FrameLayout fLayout = (FrameLayout) convertView.findViewById (R.id.BtnGridItemFL);
			ImageView button = (ImageView)convertView.findViewById(R.id.BtnGridItem);
			ImageView imgBtn = (ImageView) convertView.findViewById(R.id.deleteCategory);
			imgBtn.setOnClickListener(deleteCardCompanyListener);
			imgBtn.setTag(cardCompanyName);
			TextView tv = (TextView) convertView.findViewById (R.id.GridItemText);
			
			if (getEditCardCompanyMode()) {
				imgBtn.setVisibility(ImageButton.VISIBLE);
			}

			if (cardCompanyName.getID() < -1) {
				button.setImageResource(ConstantImagesArray.COMPANY_IMAGES[49]);
			} else {
				button.setImageResource(ConstantImagesArray.COMPANY_IMAGES[cardCompanyName.getImageIndex()-1]);
			}

			tv.setText(cardCompanyName.getName());
			button.setOnClickListener(cardCompenyNameListener);
			button.setTag(cardCompanyName);

			//�߰� ��ư���� Check ��ư �� ��Ÿ���� ��� �ڵ�
			if (cardCompanyName.getID() < -1) {
				imgBtn.setVisibility(ImageButton.INVISIBLE);
			}
			/*
			Button button = (Button)convertView.findViewById(R.id.BtnGridItem);
			button.setText(cardCompanyName.getName());
			button.setOnClickListener(cardCompenyNameListener);
			button.setTag(cardCompanyName);
			*/
			return convertView;
		}
	}

	@Override
	protected void onEditButtonClick() {
		Intent intent = new Intent(SelectCardCompanyLayout.this, EditSelecCardCompanyNameLayout.class);
		startActivityForResult(intent, ACT_COMPANY_CARD_NAME_EDIT);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_EDIT_CARDCOMPANY || requestCode == ACT_ADD_CARDCOMPANY) {
    		
    		if (resultCode == RESULT_OK) {
    			updateCardCompanyAdapter();			
    		}
    		return;
    	} 
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void clearAdapter() {
		if (mAdapterCardCompenyName != null) {
			mAdapterCardCompenyName.clear();
		}
	}
	
	private void updateCardCompanyAdapter() {
    	
    	if (mAdapterCardCompenyName != null) {
    		mAdapterCardCompenyName.clear();
    	}
    	
    	getData();
    	setAdaper();
	}
	
	//�±� ���� �ϴ� �κ�
	View.OnClickListener deleteCardCompanyListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			
			dCardCompany = (CardCompanyName) v.getTag();
	    	
	    	new AlertDialog.Builder(SelectCardCompanyLayout.this)
	    	.setMessage("���� �Ͻðڽ��ϱ�?")
	    	.setPositiveButton("����", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					
					if (DBMgr.deleteCardCompanyName(dCardCompany.getID()) == 0) {
						return;
					}
					mCardCompenyNames.remove(dCardCompany);
				 
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
    		if (getEditCardCompanyMode()) {
    			setEditCardCompanyMode(false);
    			setAdaper();
    		} else {
    			disAppearAnimation();
    			setResult(RESULT_CANCELED);
    		}
    	}
      
    	return true;
    }
}
