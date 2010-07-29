package com.fletamuto.sptb;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.db.DBMgr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

/**
 * 자산입력
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputAssetsLayout extends InputBaseLayout {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_assets);
        
        updateDate();
        SetDateBtnClickListener(R.id.BtnAssetsDate); 
        SetAmountBtnClickListener(R.id.BtnAssetsAmount);
        SetSaveBtnClickListener(R.id.BtnAssetsSave);
        SetCategoryClickListener(R.id.BtnAssetsCategory);
    }
    
    protected void updateDate() {
    	updateBtnDateText(R.id.BtnAssetsDate);
    } 
    
    protected void saveData() {
    	String title = ((TextView)findViewById(R.id.ETAssetsTitle)).getText().toString();
    	getItem().setTitle(title);
    	
    	if (DBMgr.getInstance().addFinanceItem(item) == true) {
    		
    	}
    	else {
    		
    	}
    	
    	Intent intent = new Intent(InputAssetsLayout.this, ReportAssetsLayout.class);
		startActivity(intent);
    }

    @Override
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnAssetsAmount);
	}
    
	@Override
	protected void createInfoDataInstance() {
		item = new AssetsItem();
	}

	@Override
	protected void onCategoryClick() {
		Intent intent = new Intent(InputAssetsLayout.this, SelectCategoryAssetsLayout.class);
		startActivityForResult(intent, ACT_CATEGORY);
	}

	@Override
	protected void updateCategory(int id, String name) {
		// TODO Auto-generated method stub
		item.setCategory(new Category(id, name));
		updateBtnCategoryText(R.id.BtnAssetsCategory);
	}
	
	protected void updateBtnCategoryText(int btnID) {
		AssetsItem assetsItem = (AssetsItem)item;
		String categoryText = String.format("%s - %s", assetsItem.getCategory().getName(), assetsItem.getSubCategory().getName());
    	((Button)findViewById(btnID)).setText(categoryText);
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_CATEGORY) {
    		if (resultCode == RESULT_OK) {
    			((AssetsItem)item).setSubCategory(new Category(data.getIntExtra("SUB_CATEGORY_ID", 0), data.getStringExtra("SUB_CATEGORY_NAME")));
    		}
    	}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
