package com.fletamuto.sptb;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

public abstract class SelectCategoryBaseLayout extends SelectGridBaseLayout {
	private static boolean mSelectSubCategory = true;
	protected ArrayList<Category> mArrCategory = null;
	CategoryButtonAdpter mAdapterCategory;
	private int mType = -1;
	
	//Adapter 내의 Sub Category 시작점과 갯수 저장을 위한 변수
	private int subCategoryStartPosition = -1;
	private int subCategoryCount = -1;
	
	//sub category 선택 모드 인지 알기 위한 변수
	private boolean subCategoryMode = false;
	
	//편집 mode 인지 알기 위한 변수
	private boolean editCategoryMode = false;
	
	public final static int ACT_SUB_CATEGORY = MsgDef.ActRequest.ACT_SUB_CATEGORY;
	public final static int ACT_EDIT_CATEGORY = MsgDef.ActRequest.ACT_EDIT_CATEGORY;
	
	public final static int[] CATEGORY_IMAGES = {R.drawable.category_001, R.drawable.category_002, R.drawable.category_003,
		R.drawable.category_004, R.drawable.category_005, R.drawable.category_006, R.drawable.category_007, R.drawable.category_008,
		R.drawable.category_009, R.drawable.category_010, R.drawable.category_011, R.drawable.category_012, R.drawable.category_013,
		R.drawable.category_014, R.drawable.category_015, R.drawable.category_016, R.drawable.category_017, R.drawable.category_018,
		R.drawable.category_019, R.drawable.category_020, R.drawable.category_021, R.drawable.category_022, R.drawable.category_023,
		R.drawable.category_024, R.drawable.category_025, R.drawable.category_026, R.drawable.category_027, R.drawable.category_028,
		R.drawable.category_029, R.drawable.category_030, R.drawable.category_031, R.drawable.category_032, R.drawable.category_033,
		R.drawable.category_034, R.drawable.category_035, R.drawable.category_036, R.drawable.category_037, R.drawable.category_038,
		R.drawable.category_039, R.drawable.category_040, R.drawable.category_041, R.drawable.category_042, R.drawable.category_043,
		R.drawable.category_044, R.drawable.category_045, R.drawable.category_046, R.drawable.category_047, R.drawable.category_048,
		R.drawable.category_049, R.drawable.category_050, R.drawable.category_051, R.drawable.category_052, R.drawable.category_053,
		R.drawable.category_054, R.drawable.category_055, R.drawable.category_056, R.drawable.category_057, R.drawable.category_058,
		R.drawable.category_059, R.drawable.category_060, R.drawable.category_061, R.drawable.category_062, R.drawable.category_063,
		R.drawable.category_064, R.drawable.category_065, R.drawable.category_066, R.drawable.category_067, R.drawable.category_068,
		R.drawable.category_069, R.drawable.category_070, R.drawable.category_071, R.drawable.category_072, R.drawable.category_073,
		R.drawable.category_074, R.drawable.category_075, R.drawable.category_076, R.drawable.category_077, R.drawable.category_078,
		R.drawable.category_079, R.drawable.category_080, R.drawable.category_081, R.drawable.category_082, R.drawable.category_083,
		R.drawable.category_084, R.drawable.category_085, R.drawable.category_086, R.drawable.category_087, R.drawable.category_088,
		R.drawable.category_089, R.drawable.category_090, R.drawable.category_091, R.drawable.category_092, R.drawable.category_093,
		R.drawable.category_094, R.drawable.category_095, R.drawable.category_096, R.drawable.category_097, R.drawable.category_098,
		R.drawable.category_099, R.drawable.category_100, R.drawable.category_101, R.drawable.category_102, R.drawable.category_103,
		R.drawable.category_104, R.drawable.category_105, R.drawable.category_106, R.drawable.category_107, R.drawable.category_108,
		R.drawable.category_109, R.drawable.category_110, R.drawable.category_111, R.drawable.category_112, R.drawable.category_113,
		R.drawable.category_114, R.drawable.category_115, R.drawable.category_116, R.drawable.category_117, R.drawable.category_118,
		R.drawable.category_119, R.drawable.category_120, R.drawable.category_121, R.drawable.category_122, R.drawable.category_123,
		R.drawable.category_124, R.drawable.category_125, R.drawable.category_126, R.drawable.category_127, R.drawable.category_128,
		R.drawable.category_129, R.drawable.category_130, R.drawable.category_131, R.drawable.category_132, R.drawable.category_133,
		R.drawable.category_134, R.drawable.category_135, R.drawable.category_136, R.drawable.category_137, R.drawable.category_138,
		R.drawable.category_139, R.drawable.category_140, R.drawable.category_141, R.drawable.category_142, R.drawable.category_143,
		R.drawable.category_144, R.drawable.category_145, R.drawable.category_146, R.drawable.category_147, R.drawable.category_148,
		R.drawable.category_149, R.drawable.category_150};
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        mSelectSubCategory = getIntent().getBooleanExtra(MsgDef.ExtraNames.SELECT_SUB_CATEGORY_IN_MAIN_CATEGORY, true);
    }
	
	protected void onEditButtonClick() {
		Intent intent = new Intent(SelectCategoryBaseLayout.this, EditCategoryLayout.class);
		intent.putExtra("CATEGORY_TYPE", mType);
		if (mType == ExpenseItem.TYPE || mType == AssetsItem.TYPE) {
			intent.putExtra("CATEGORY_HAS_SUB", true);
		}
		
		startActivityForResult(intent, ACT_EDIT_CATEGORY);
	}
	
	//Adapter 내의 Sub Category 시작점과 갯수를 set/get 하는 부분 Start
	public void setSubCategoryStartPosition (int startPosition) {
		subCategoryStartPosition = startPosition;
	}
	public int getSubCategoryStartPosition () {
		return subCategoryStartPosition;
	}
	
	public void setSubCategoryCount (int categoryCount) {
		subCategoryCount = categoryCount;
	}
	public int getSubCategoryCount () {
		return subCategoryCount;
	}
	//End
	
	public void setSubCategoryMode (boolean mode) {
		subCategoryMode = mode;
	}
	public boolean getSubCategoryMode () {
		return subCategoryMode;
	}
	
	public void setEditCategoryMode (boolean mode) {
		editCategoryMode = mode;
	}
	public boolean getEditCategoryMode () {
		return editCategoryMode;
	}
	
	@Override
	public void getData() {
		getCategoryList();
	}
	
	protected void getCategoryList() {
		if (mType == -1) {
			Log.e(LogTag.LAYOUT, "== invaild category type");
			return;
		}
		mArrCategory = DBMgr.getCategory(mType);
		
		//main category Add 버튼 추가 를 위한 
    	Category addButton = new Category(-2, "+");
        mArrCategory.add(addButton);
		
	}
	
	@Override
	public void setAdaper() {
		setCategoryAdaper();
	}
	
	protected void setCategoryAdaper() {
		if (mArrCategory == null) return;
        
    	final GridView gridCategory = (GridView)findViewById(R.id.GVSelect);
    	mAdapterCategory = new CategoryButtonAdpter(this, R.layout.grid_category_select, mArrCategory);
    	gridCategory.setAdapter(mAdapterCategory);
	}
	
	protected void updateAdapterCategory() {
		if (mAdapterCategory != null) {
			mAdapterCategory.clear();
		}
		getCategoryList();
		setCategoryAdaper();
	}
	
	protected void onClickCategoryButton(Category category) {
		Intent intent = new Intent();
		intent.putExtra("CATEGORY_ID", category.getID());
		intent.putExtra("CATEGORY_NAME", category.getName());
		setResult(RESULT_OK, intent);
		finish();
	}
	
	protected void onClickDeleteCategoryButton(Category category) {
		
	}

    View.OnClickListener categoryListener = new View.OnClickListener() {
		public void onClick(View v) {
			Category category = (Category)v.getTag();
			onClickCategoryButton(category);
		}
	};
	
	View.OnClickListener deleteCategoryListener = new View.OnClickListener() {
		
		public void onClick(View v) {
			Category category = (Category)v.getTag();
			onClickDeleteCategoryButton(category);
			
		}
	};
	
	
    
    public void setType(int type) {
		this.mType = type;
	}

	public int getType() {
		return mType;
	}
	
	private class CategoryButtonAdpter extends ArrayAdapter<Category> {
		int mResource;
    	LayoutInflater mInflater;
    	
		public CategoryButtonAdpter(Context context, int resource,
				 List<Category> objects) {
			super(context, resource, objects);
			this.mResource = resource;
			mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Category category = (Category)getItem(position);
			
			if (convertView == null) {
				convertView = mInflater.inflate(mResource, parent, false);
			}
			
			FrameLayout fLayout = (FrameLayout) convertView.findViewById (R.id.BtnGridItemFL);
			ImageView button = (ImageView)convertView.findViewById(R.id.BtnGridItem);
			ImageView imgBtn = (ImageView) convertView.findViewById(R.id.deleteCategory);
			imgBtn.setOnClickListener(deleteCategoryListener);
			imgBtn.setTag(category);
			TextView tv = (TextView) convertView.findViewById (R.id.GridItemText);
			
			//Sub Category 영역을 구분 하기 위한 부분
			if (position > getSubCategoryStartPosition()-1 && position < getSubCategoryStartPosition() + getSubCategoryCount()) {
				fLayout.setBackgroundColor(Color.BLACK);
				tv.setTextColor(Color.WHITE);
				if (getEditCategoryMode()) {
					imgBtn.setVisibility(ImageButton.VISIBLE);
				}
				
			} else {
				if (getEditCategoryMode() && getSubCategoryMode()) {
//					button.setText(category.getName());
					if (category.getID() < -1) {
						button.setImageResource(CATEGORY_IMAGES[149]);
					} else {
						button.setImageResource(CATEGORY_IMAGES[category.getImageIndex()-1]);
					}
					tv.setText(category.getName());
					button.setTag(category);
					button.setClickable(false);					
					return convertView;
				} else if (getEditCategoryMode() && getSubCategoryMode() == false) {
					imgBtn.setVisibility(ImageButton.VISIBLE);
				}
			}
						
			//Sub Category 빈공간 넣는 부분
			if (category == null) {
				button.setVisibility(Button.INVISIBLE);
				imgBtn.setVisibility(ImageButton.INVISIBLE);
				return convertView;
			}
			
//			button.setText(category.getName());

			if (category.getID() < -1) {
				button.setImageResource(CATEGORY_IMAGES[149]);
			} else {
				button.setImageResource(CATEGORY_IMAGES[category.getImageIndex()-1]);
			}
			
			tv.setText(category.getName());
			button.setOnClickListener(categoryListener);
			button.setTag(category);
			
			if (category.getID() < -1) {
				imgBtn.setVisibility(ImageButton.INVISIBLE);
			}

			return convertView;
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_EDIT_CATEGORY) {
			if (resultCode == RESULT_OK) {
				
				//Category 편집 된 내용 update 시키는 부분임
//				updateAdapterCategory();

    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void clearAdapter() {
		if (mAdapterCategory != null) {
			mAdapterCategory.clear();
		}
	}

	public static boolean isSelectSubCategory() {
		return mSelectSubCategory;
	}
}
