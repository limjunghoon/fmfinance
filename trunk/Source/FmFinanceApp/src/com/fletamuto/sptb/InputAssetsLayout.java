package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 자산입력
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputAssetsLayout extends InputFinanceItemBaseLayout {
	private AssetsItem mAssetsItem;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_assets, true);
        
        updateChildView();
        setDateBtnClickListener(R.id.BtnAssetsDate); 
        setAmountBtnClickListener(R.id.BtnAssetsAmount);
        setSaveBtnClickListener(R.id.BtnAssetsSave);
        setCategoryClickListener(R.id.BtnAssetsCategory);
        setTitle(getResources().getString(R.string.input_assets_name));
    }
    
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnAssetsDate);
    } 
    
    protected void saveItem() {
    	if (mInputMode == InputMode.ADD_MODE) {
    		saveNewItem(ReportAssetsLayout.class);
    	}
    	else if (mInputMode == InputMode.EDIT_MODE){
    		saveUpdateItem();
    	}
    }

    @Override
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnAssetsAmount);
	}
    
	@Override
	protected void createItemInstance() {
		mAssetsItem = new AssetsItem();
		setItem(mAssetsItem);
	}
	
	@Override
	protected boolean getItemInstance(int id) {
		mAssetsItem = (AssetsItem) DBMgr.getItem(AssetsItem.TYPE, id);
		if (mAssetsItem == null) return false;
		return true;
	}

	@Override
	protected void onCategoryClick() {
		Intent intent = new Intent(InputAssetsLayout.this, SelectCategoryAssetsLayout.class);
		startActivityForResult(intent, ACT_CATEGORY);
	}

	@Override
	protected void updateCategory(int id, String name) {
		mAssetsItem.setCategory(id, name);
		updateBtnCategoryText(R.id.BtnAssetsCategory);
	}
	
	protected void updateBtnCategoryText(int btnID) {
		String categoryText = getResources().getString(R.string.input_select_category);
		if (mAssetsItem.isVaildCatetory()) {
			// categoryText = String.format("%s - %s", assetsItem.getCategory().getName(), assetsItem.getSubCategory().getName());
			categoryText = String.format("%s", mAssetsItem.getCategory().getName());
		}
		((Button)findViewById(btnID)).setText(categoryText);
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_CATEGORY) {
    		if (resultCode == RESULT_OK) {
    			mAssetsItem.setSubCategory(data.getIntExtra("SUB_CATEGORY_ID", 0), data.getStringExtra("SUB_CATEGORY_NAME"));
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void updateChildView() {
		updateDate();
		updateBtnCategoryText(R.id.BtnAssetsCategory);
		updateBtnAmountText(R.id.BtnAssetsAmount);
		updateEditTitleText(R.id.ETAssetsTitle);
	}

	@Override
	protected void updateItem() {
		String title = ((TextView)findViewById(R.id.ETAssetsTitle)).getText().toString();
    	getItem().setTitle(title);
	}
}
