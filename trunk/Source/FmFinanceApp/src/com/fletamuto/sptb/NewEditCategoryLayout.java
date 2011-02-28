package com.fletamuto.sptb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fletamuto.sptb.data.CardCompanyName;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.ExpenseTag;
import com.fletamuto.sptb.data.FinancialCompany;
import com.fletamuto.sptb.data.IncomeItem;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;



public class NewEditCategoryLayout  extends Activity {  	
	
	private int mType = -1;
	
	//현재 모드 상수
	final static int MAIN_CATEGORY_ADD_MODE = 0;
	final static int MAIN_CATEGORY_EDIT_MODE = 1;
	final static int SUB_CATEGORY_ADD_MODE = 2;
	final static int SUB_CATEGORY_EDIT_MODE = 3;
	final static int INCOME_CATEGORY_ADD_MODE = 4;
	final static int INCOME_CATEGORY_EDIT_MODE = 5;
	final static int TAG_ADD_MODE = 6;
	final static int TAG_EDIT_MODE = 7;
	final static int COMPANY_ADD_MODE = 8;
	final static int COMPANY_EDIT_MODE = 9;
	final static int CARD_COMPANY_ADD_MODE = 10;
	final static int CARD_COMPANY_EDIT_MODE = 11;
	
	//선택된 이미지 위치
	private int beforeImagePosition = -1;
	//현재 모드
	private int editMode;
	
	//Editor
	EditText eName;

	//이미지 아답타
	ImageAdapter Adapter;
	
	private String mMainCategoryName;
	private int mMainCategoryID = -1;
	private String mName;
	private int mID = -1;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        getExtraInfo();
        Intent intent = getIntent();
        
        mType = intent.getIntExtra("EDIT_TYPE", -1);
        
        if (intent.getStringExtra("EDIT_MODE").contentEquals("MAIN_CATEGORY_ADD")) {
        	editMode = MAIN_CATEGORY_ADD_MODE;
        } else if (intent.getStringExtra("EDIT_MODE").contentEquals("MAIN_CATEGORY_EDIT")) {
        	editMode = MAIN_CATEGORY_EDIT_MODE;
        } else if (intent.getStringExtra("EDIT_MODE").contentEquals("SUB_CATEGORY_ADD")) {
        	editMode = SUB_CATEGORY_ADD_MODE;
        } else if (intent.getStringExtra("EDIT_MODE").contentEquals("SUB_CATEGORY_EDIT")) {
        	editMode = SUB_CATEGORY_EDIT_MODE;
        } else if (intent.getStringExtra("EDIT_MODE").contentEquals("INCOME_CATEGORY_ADD")) {
        	editMode = INCOME_CATEGORY_ADD_MODE;
        } else if (intent.getStringExtra("EDIT_MODE").contentEquals("INCOME_CATEGORY_EDIT")) {
        	editMode = INCOME_CATEGORY_EDIT_MODE;
        } else if (intent.getStringExtra("EDIT_MODE").contentEquals("TAG_ADD")) {
        	editMode = TAG_ADD_MODE;
        } else if (intent.getStringExtra("EDIT_MODE").contentEquals("TAG_EDIT")) {
        	editMode = TAG_EDIT_MODE;
        } else if (intent.getStringExtra("EDIT_MODE").contentEquals("COMPANY_ADD")) {
        	editMode = COMPANY_ADD_MODE;
        } else if (intent.getStringExtra("EDIT_MODE").contentEquals("COMPANY_EDIT")) {
        	editMode = COMPANY_EDIT_MODE;
        } else if (intent.getStringExtra("EDIT_MODE").contentEquals("CARD_COMPANY_ADD")) {
        	editMode = CARD_COMPANY_ADD_MODE;
        } else if (intent.getStringExtra("EDIT_MODE").contentEquals("CARD_COMPANY_EDIT")) {
        	editMode = CARD_COMPANY_EDIT_MODE;
        } else {
        	editMode = -1;
        }
        Log.d("jp test (New)", " editMode="+editMode);
        
       	setContentView(R.layout.category_new_edit);
       	
       	TextView title = (TextView) findViewById (R.id.EditCategoryTitle);
       	title.setText(intent.getStringExtra("EDIT_TITLE"));
       	
       	eName = (EditText) findViewById (R.id.EditCategoryEditor);
       	
       	if (editMode == MAIN_CATEGORY_EDIT_MODE || editMode == SUB_CATEGORY_EDIT_MODE || editMode == INCOME_CATEGORY_EDIT_MODE) {
        	beforeImagePosition = intent.getIntExtra("CATEGORY_IMAGE_INDEX", -1);
        	eName.setText(intent.getStringExtra("CATEGORY_NAME"));   
        	mName = intent.getStringExtra("CATEGORY_NAME");
            mID = intent.getIntExtra("CATEGORY_ID", -1);
        } else if (editMode == TAG_EDIT_MODE) {
        	beforeImagePosition = intent.getIntExtra("TAG_IMAGE_INDEX", -1);
        	eName.setText(intent.getStringExtra("TAG_NAME"));   
        	mName = intent.getStringExtra("TAG_NAME");
            mID = intent.getIntExtra("TAG_ID", -1);
        } else if (editMode == COMPANY_EDIT_MODE) {
        	beforeImagePosition = intent.getIntExtra("COMPANY_IMAGE_INDEX", -1);
        	eName.setText(intent.getStringExtra("COMPANY_NAME"));   
        	mName = intent.getStringExtra("COMPANY_NAME");
            mID = intent.getIntExtra("COMPANY_ID", -1);
        } else if (editMode == CARD_COMPANY_EDIT_MODE) {
        	beforeImagePosition = intent.getIntExtra("CARD_COMPANY_IMAGE_INDEX", -1);
        	eName.setText(intent.getStringExtra("CARD_COMPANY_NAME"));   
        	mName = intent.getStringExtra("CARD_COMPANY_NAME");
            mID = intent.getIntExtra("CARD_COMPANY_ID", -1);
        } else {
        	beforeImagePosition = -1;
        	eName.setHint("분류명을 입력하세요");
        }
       	
       	if (editMode == SUB_CATEGORY_ADD_MODE || editMode == SUB_CATEGORY_EDIT_MODE) {
       		mMainCategoryName = intent.getStringExtra("MAIN_CATEGORY_NAME");
       		mMainCategoryID = intent.getIntExtra("MAIN_CATEGORY_ID", -1);
       	} else {
       		mMainCategoryName = null;
       		mMainCategoryID = -1;
       	}
       	
       	GridView grid = (GridView) findViewById (R.id.EditCategoryGrid);
       	Adapter = new ImageAdapter(this, R.layout.edit_category_grid);
       	grid.setAdapter(Adapter);
       	
       	Button saveBtn = (Button) findViewById (R.id.BtnEditCategorySave);
       	saveBtn.setOnClickListener(saveBtnClickLintener);
    }

    class ImageAdapter extends BaseAdapter {
    	
    	private Context mContext;
    	int mResource;
    	LayoutInflater mInflater;
    	
    	public ImageAdapter (Context c, int resource) {		
    		this.mResource = resource;
    		mContext = c;
    		mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	}
    	
    	public int getCount() {
    		if (mType == ExpenseItem.TYPE) {
    			return 150;
    		} else if (mType == IncomeItem.TYPE) {
    			return 50;
    		} else if (editMode > INCOME_CATEGORY_EDIT_MODE) {
    			return 50;
    		}
    		return 50;
    	}
    	
    	public Object getItem (int position) {
    		if (mType == ExpenseItem.TYPE) {
    			return ConstantImagesArray.CATEGORY_IMAGES[position];
    		} else if (mType == IncomeItem.TYPE) {
    			return ConstantImagesArray.INCOME_CATEGORY_IMAGES[position];
    		} else if (editMode == TAG_ADD_MODE || editMode == TAG_EDIT_MODE) {
    			return ConstantImagesArray.TAG_IMAGES[position];
    		} else if (editMode == COMPANY_ADD_MODE || editMode == COMPANY_EDIT_MODE) {
    			return ConstantImagesArray.COMPANY_IMAGES[position];
    		} else if (editMode == CARD_COMPANY_ADD_MODE || editMode == CARD_COMPANY_EDIT_MODE) {
    			return ConstantImagesArray.COMPANY_IMAGES[position];
    		}
    		return null;
    	}
    	
    	public long getItemId (int position) {
    		return position;
    	}
    	
    	public View getView (int position, View convertView, ViewGroup parent) {
    		
    		if (convertView == null) {
    			convertView = mInflater.inflate(mResource, parent, false);
    		}
    		
    		FrameLayout fLayout = (FrameLayout) convertView.findViewById (R.id.EditCategoryGridFL);
    		ImageView imageView = (ImageView) convertView.findViewById(R.id.CategoryItemImage);
    		ImageView imageCheck = (ImageView) convertView.findViewById(R.id.CategoryCheckImage);
    		
    		if (mType == ExpenseItem.TYPE) {
    			imageView.setImageResource(ConstantImagesArray.CATEGORY_IMAGES[position]);
    		} else if (mType == IncomeItem.TYPE) {
    			imageView.setImageResource(ConstantImagesArray.INCOME_CATEGORY_IMAGES[position]);
    		} else if (editMode == TAG_ADD_MODE || editMode == TAG_EDIT_MODE){
    			imageView.setImageResource(ConstantImagesArray.TAG_IMAGES[position]);
    		} else if (editMode == COMPANY_ADD_MODE || editMode == COMPANY_EDIT_MODE) {
    			imageView.setImageResource(ConstantImagesArray.COMPANY_IMAGES[position]);
    		} else if (editMode == CARD_COMPANY_ADD_MODE || editMode == CARD_COMPANY_EDIT_MODE) {
    			imageView.setImageResource(ConstantImagesArray.COMPANY_IMAGES[position]);
    		}
    		imageCheck.setImageResource(R.drawable.openarrow);
    		
    		if (position == (beforeImagePosition-1)) {
    			imageCheck.setVisibility(ImageView.VISIBLE);
    		} else {
    			imageCheck.setVisibility(ImageView.INVISIBLE);
    		}
    		
    		imageView.setOnClickListener(imageViewClickLintener);
    		imageView.setTag(position+1);
    		return convertView;
    		
    	}
    }
    
    //이미지를 선택 했을 때 처리
    public View.OnClickListener imageViewClickLintener = new View.OnClickListener() {
		
		public void onClick(View v) {
			int selectedImagePosition = (Integer)v.getTag();
			
			beforeImagePosition = selectedImagePosition;
			Adapter.notifyDataSetInvalidated();
		}
	};
	
	//완료 버튼 선택 했을 때 처리
	public View.OnClickListener saveBtnClickLintener = new View.OnClickListener() {
		
		public void onClick(View v) {
			String name = eName.getText().toString();
			int imagePosition = beforeImagePosition;
			Category category;
			
			if (editMode == MAIN_CATEGORY_ADD_MODE || editMode == INCOME_CATEGORY_ADD_MODE) {
				category = createMainCategory(name, imagePosition);
				if (category == null) {
					Log.e(LogTag.LAYOUT, ":: Fail make the category");
					return;
				}
	        } else if (editMode == MAIN_CATEGORY_EDIT_MODE || editMode == INCOME_CATEGORY_EDIT_MODE) {
	        	if (updateMainCategory(mID, name, imagePosition) == 0) {
					Log.e(LogTag.LAYOUT, ":: Fail to update category");
					return;
				}
	        } else if (editMode == SUB_CATEGORY_ADD_MODE) {
	        	category = createSubCategory(name, imagePosition);
	        	if (category == null) {
					Log.e(LogTag.LAYOUT, ":: Fail make the category");
					return;
				}
	        } else if (editMode == SUB_CATEGORY_EDIT_MODE) {
	        	if (updateSubCategory(mID, name, imagePosition) == 0) {
					Log.e(LogTag.LAYOUT, ":: Fail to update category");
					return;
				}
	        } else if (editMode == TAG_ADD_MODE) {
	        	if (createTag(name, imagePosition) == null) {
	        		Log.e(LogTag.LAYOUT, ":: Fail make the tag");
					return;
	        	}
	        } else if (editMode == TAG_EDIT_MODE) {
	        	if (updateTag(mID, name, imagePosition) == null) {
	        		Log.e(LogTag.LAYOUT, ":: Fail to update tag");
					return;
	        	}
	        } else if (editMode == COMPANY_ADD_MODE) {
	        	if (createCompany(name, imagePosition) == null) {
	        		Log.e(LogTag.LAYOUT, ":: Fail make ths company");
					return;
	        	}
	        } else if (editMode == COMPANY_EDIT_MODE) {
	        	if (updateCompany(mID, name, imagePosition) == null) {
	        		Log.e(LogTag.LAYOUT, ":: Fail to update company");
					return;
	        	}
	        } else if (editMode == CARD_COMPANY_ADD_MODE) {
	        	if (createCardCompany(name, imagePosition) == null) {
	        		Log.e(LogTag.LAYOUT, ":: Fail make the card company");
					return;
	        	}
	        } else if (editMode == CARD_COMPANY_EDIT_MODE) {
	        	if (updateCardCompany(mID, name, imagePosition) == null) {
	        		Log.e(LogTag.LAYOUT, ":: Fail to update card company");
					return;
	        	}
	        } else {
	        	
	        }

			setResult(RESULT_OK);
			finish();
		}
	};
	
	private int updateMainCategory(int id, String name, int imagePosition) {
		return (DBMgr.updateCategory(mType, id, name, imagePosition));
	}
    
    private int updateSubCategory(int id, String name, int imagePosition) {
    	return (DBMgr.updateSubCategory(mType, id, name, imagePosition));
	}
    
	private Category createMainCategory(String categoryName, int imgPosition) {
    	String name = categoryName;
    	int imagePosition = imgPosition;
    	
		if (checkCategoryName(categoryName) == false || mType == -1 || checkCategoryImage(imagePosition) == false) {
			return null;
		}
		
		Category category = new Category(categoryName);
		category.setImageIndex(imagePosition);
		
		mMainCategoryID = DBMgr.addCategory(mType, category);
		category.setID(mMainCategoryID);
		
		if (mMainCategoryID == -1) {
			return null;
		}
		mMainCategoryName = name;
		
		return category;
	}
    
	private Category createSubCategory(String subCategoryName, int imgPosition) {
		if (mMainCategoryID == -1 || mType == -1) {
			Log.e(LogTag.LAYOUT, ":: Don't make sub category");
			return null;
		}
		
		if (checkCategoryName(subCategoryName) == false || checkCategoryImage(imgPosition) == false) {
			return null;
		}
		
		int subCategoryID = DBMgr.addSubCategory(mType, mMainCategoryID, subCategoryName, imgPosition);
		if (subCategoryID == -1) {
			return null;
		}
		
		return new Category(subCategoryID, subCategoryName);
	}
    
    private boolean checkCategoryName(String name) {
		if (name.length() == 0) {
			displayAlertMessage("이름이 입력되지 않았습니다.");
			return false;
		}
		return true;
	}
    
    private boolean checkCategoryImage(int imageIndex) {
		if (imageIndex < 1) {
			displayAlertMessage("이미지가 선택 되지 않았습니다.");
			return false;
		}
		return true;
	}
    
    public void displayAlertMessage(String msg) {
    	AlertDialog.Builder alert = new AlertDialog.Builder(NewEditCategoryLayout.this);
    	alert.setMessage(msg);
    	alert.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
    	alert.show();
    }
    
	protected ExpenseTag createTag(String itemName, int imgIndex) {
		
		if (checkCategoryName(itemName) == false || checkCategoryImage(imgIndex) == false) {
			return null;
		}
		
		ExpenseTag tag = new ExpenseTag();
		tag.setName(itemName);
		tag.setImageIndex(imgIndex);
		
		if (DBMgr.addTag(tag) == -1) {
			Log.e(LogTag.LAYOUT, ":: Fail to create TAG Item");
			return null;
		}
		return tag;
	}
	
	protected ExpenseTag updateTag(int editTagID, String itemName, int imgIndex) {

		if (checkCategoryName(itemName) == false || checkCategoryImage(imgIndex) == false) {
			return null;
		}
		
		ExpenseTag tag = DBMgr.getTag(editTagID);
		if (tag == null) {
			Log.e(LogTag.LAYOUT, ":: Fail to load tag");
			return null;
		}
		
		tag.setName(itemName);
		tag.setImageIndex(imgIndex);
		if (DBMgr.updateTag(tag) == false) {
			Log.e(LogTag.LAYOUT, ":: Fail update the tag");
			return null;
		}
		
		return tag;
	}
	
	protected FinancialCompany createCompany(String itemName, int imgIndex) {
		if (checkCategoryName(itemName) == false || checkCategoryImage(imgIndex) == false) {
			return null;
		}
		
		FinancialCompany financialCompany = new FinancialCompany();
		financialCompany.setName(itemName);
		financialCompany.setImageIndex(imgIndex);
		
		if (DBMgr.addCompany(financialCompany) == -1) {
			Log.e(LogTag.LAYOUT, ":: Fail to create COMPANY_NAME Item");
			return null;
		}
		return financialCompany;
	}
	

	protected FinancialCompany updateCompany(int editItemID, String itemName, int imgIndex) {
		if (checkCategoryName(itemName) == false || checkCategoryImage(imgIndex) == false) {
			return null;
		}
		
		FinancialCompany financialCompany = DBMgr.getCompany(editItemID);
		if (financialCompany == null) {
			Log.e(LogTag.LAYOUT, ":: Fail to load FinancialCompany");
			return null;
		}
		
		financialCompany.setName(itemName);
		financialCompany.setImageIndex(imgIndex);
		if (DBMgr.updateCompany(financialCompany) == false) {
			Log.e(LogTag.LAYOUT, ":: Fail update the FinancialCompany");
			return null;
		}
		return financialCompany;
	}
	
	protected CardCompanyName createCardCompany(String itemName, int imgIndex) {
		if (checkCategoryName(itemName) == false || checkCategoryImage(imgIndex) == false) {
			return null;
		}
		
		CardCompanyName cardCompanyName = new CardCompanyName();
		cardCompanyName.setName(itemName);
		cardCompanyName.setImageIndex(imgIndex);
		
		if (DBMgr.addCardCompanyName(cardCompanyName) == -1) {
			Log.e(LogTag.LAYOUT, ":: Fail to create COMPANY_CARD_NAME Item");
			return null;
		}
		return cardCompanyName;
	}
	
	protected CardCompanyName updateCardCompany(int editItemID, String itemName, int imgIndex) {
		if (checkCategoryName(itemName) == false || checkCategoryImage(imgIndex) == false) {
			return null;
		}
		
		CardCompanyName cardCompanyName = DBMgr.getCardCompanyName(editItemID);
		if (cardCompanyName == null) {
			Log.e(LogTag.LAYOUT, ":: Fail to load CardCompanyName");
			return null;
		}
		
		cardCompanyName.setName(itemName);
		cardCompanyName.setImageIndex(imgIndex);
		if (DBMgr.updateCardCompanyName(cardCompanyName) == false) {
			Log.e(LogTag.LAYOUT, ":: Fail update the CardCompanyName");
			return null;
		}
		return cardCompanyName;
	}

}

