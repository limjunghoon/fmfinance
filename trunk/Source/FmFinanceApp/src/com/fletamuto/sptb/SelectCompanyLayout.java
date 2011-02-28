package com.fletamuto.sptb;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fletamuto.sptb.data.FinancialCompany;
import com.fletamuto.sptb.db.DBMgr;

public class SelectCompanyLayout extends SelectGridBaseLayout {
	public static final int ACT_COMPANY_NAME_EDIT = MsgDef.ActRequest.ACT_COMPANY_NAME_EDIT;
	protected ArrayList<FinancialCompany> mCompanyNames = null;
	private CompenyNameButtonAdpter mAdapterCompanyName;
	
	//편집 mode 인지 알기 위한 변수
	private boolean editCompanyMode = false;
	
	//지울 태그 
	FinancialCompany dCompany;
	
	final static int ACT_EDIT_COMPANY = 0;
	final static int ACT_ADD_COMPANY = 1;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      //편집 버튼 처리
        getGridRightBtn().setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				setEditCompanyMode(true);	
				setAdaper();
			}
		});
    
    }
	
	public void setEditCompanyMode (boolean mode) {
		editCompanyMode = mode;
	}
	public boolean getEditCompanyMode () {
		return editCompanyMode;
	}
	
	protected void getCompanyNameList() {
		mCompanyNames = DBMgr.getCompanys();
		
		//main category Add 버튼 추가 를 위한 
		FinancialCompany addButton = new FinancialCompany();
		addButton.setID(-2);
		addButton.setName("+");
		addButton.setImageIndex(49);
		
		mCompanyNames.add(addButton);
	}
	
	@Override
	public void getData() {
		getCompanyNameList();
	}

	@Override
	public void setAdaper() {
		if (mCompanyNames == null) return;
        
    	final GridView gridCategory = (GridView)findViewById(R.id.GVSelect);
    	mAdapterCompanyName = new CompenyNameButtonAdpter(this, R.layout.grid_category_select, mCompanyNames);
    	gridCategory.setAdapter(mAdapterCompanyName);
	}
	
	protected void onClickCompanyNameButton(FinancialCompany compeny) {
		
		//추가 버튼이 선택 됐을 때 처리
    	if (compeny.getID() == -2) {
			Intent intent = new Intent(SelectCompanyLayout.this, NewEditCategoryLayout.class);
			intent.putExtra("EDIT_TITLE", "금융사 추가");
			intent.putExtra("EDIT_MODE", "COMPANY_ADD");

			startActivityForResult(intent, ACT_ADD_COMPANY);
			return;
		}
    	
		if (getEditCompanyMode()) {
			//편집으로 넘어 가는 화면
			Intent intent = new Intent(SelectCompanyLayout.this, NewEditCategoryLayout.class);
			intent.putExtra("COMPANY_ID", compeny.getID());
			intent.putExtra("COMPANY_NAME", compeny.getName());
			intent.putExtra("COMPANY_IMAGE_INDEX", compeny.getImageIndex());
			intent.putExtra("EDIT_TITLE", "금융사 편집");
			intent.putExtra("EDIT_MODE", "COMPANY_EDIT");

			startActivityForResult(intent, ACT_EDIT_COMPANY);
			return;
		} else {
			Intent intent = new Intent();
			intent.putExtra(MsgDef.ExtraNames.COMPANY_ID, compeny.getID());
			setResult(RESULT_OK, intent);
			finish();
		}

	}

    View.OnClickListener compenyNameListener = new View.OnClickListener() {
		public void onClick(View v) {
			FinancialCompany compenyName = (FinancialCompany)v.getTag();
			onClickCompanyNameButton(compenyName);
		}
	};

	private class CompenyNameButtonAdpter extends ArrayAdapter<FinancialCompany> {
		int mResource;
    	LayoutInflater mInflater;
    	
		public CompenyNameButtonAdpter(Context context, int resource,
				 List<FinancialCompany> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FinancialCompany company = (FinancialCompany)getItem(position);

			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			FrameLayout fLayout = (FrameLayout) convertView.findViewById (R.id.BtnGridItemFL);
			ImageView button = (ImageView)convertView.findViewById(R.id.BtnGridItem);
			ImageView imgBtn = (ImageView) convertView.findViewById(R.id.deleteCategory);
			imgBtn.setOnClickListener(deleteCompanyListener);
			imgBtn.setTag(company);
			TextView tv = (TextView) convertView.findViewById (R.id.GridItemText);
			
			if (getEditCompanyMode()) {
				imgBtn.setVisibility(ImageButton.VISIBLE);
			}

			if (company.getID() < -1) {
				button.setImageResource(ConstantImagesArray.COMPANY_IMAGES[49]);
			} else {
				button.setImageResource(ConstantImagesArray.COMPANY_IMAGES[company.getImageIndex()-1]);
			}

			tv.setText(company.getName());
			button.setOnClickListener(compenyNameListener);
			button.setTag(company);

			//추가 버튼에는 Check 버튼 안 나타나게 방어 코드
			if (company.getID() < -1) {
				imgBtn.setVisibility(ImageButton.INVISIBLE);
			}
			/*
			Button button = (Button)convertView.findViewById(R.id.BtnGridItem);
			button.setText(FinancialCompany.getName());
			button.setOnClickListener(compenyNameListener);
			button.setTag(FinancialCompany);
			*/
			return convertView;
		}
	}

	@Override
	protected void onEditButtonClick() {
		Intent intent = new Intent(SelectCompanyLayout.this, EditSelecCompanyLayout.class);
		startActivityForResult(intent, ACT_COMPANY_NAME_EDIT);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
if (requestCode == ACT_EDIT_COMPANY || requestCode == ACT_ADD_COMPANY) {
    		
    		if (resultCode == RESULT_OK) {
    			updateCompanyAdapter();			
    		}
    		return;
    	} 
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void clearAdapter() {
		if (mAdapterCompanyName != null) {
			mAdapterCompanyName.clear();
		}
	}
	
	private void updateCompanyAdapter() {
    	
    	if (mAdapterCompanyName != null) {
    		mAdapterCompanyName.clear();
    	}
    	
    	getData();
    	setAdaper();
	}
	
	//태그 삭제 하는 부분
	View.OnClickListener deleteCompanyListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			
			dCompany = (FinancialCompany) v.getTag();
	    	
	    	new AlertDialog.Builder(SelectCompanyLayout.this)
	    	.setMessage("삭제 하시겠습니까?")
	    	.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					
					if (DBMgr.deleteCompany(dCompany.getID()) == 0) {
						return;
					}
					mCompanyNames.remove(dCompany);
				 
					setAdaper();				
				}
			})
			.setNegativeButton("취소", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
				}
			}).show();   	
			
		}
	};
	
	//Back 키 처리
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    	
    	if(keyCode==KeyEvent.KEYCODE_BACK){
    		if (getEditCompanyMode()) {
    			setEditCompanyMode(false);
    			setAdaper();
    		} else {
    			disAppearAnimation();
    			setResult(RESULT_CANCELED);
    		}
    	}
      
    	return true;
    }
}
