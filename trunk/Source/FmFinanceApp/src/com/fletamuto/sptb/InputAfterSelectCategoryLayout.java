package com.fletamuto.sptb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fletamuto.sptb.data.Category;

public abstract class InputAfterSelectCategoryLayout extends SelectCategoryBaseLayout implements InputAfterSelected {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
	
	protected void onClickCategoryButton(Category category) {
		selectedCategory(category);
	}
	

	private void selectedCategory(Category category) {
//		Class<?> changeClass = null;
//		
//		if (id == R.id.BtnInputExpense)	changeClass = InputExpenseLayout.class;
//		else if (id == R.id.BtnInputIncome) changeClass = SelectCategoryIncomeLayout.class;
//		else if (id == R.id.BtnAssetsLiability) changeClass = InputAssetsOrLiability.class;
//		else if (id == R.id.BtnReport) 	changeClass = MainReportLayout.class;
//		else if (id == R.id.BtnTodayExpense) changeClass = ReportCurrentExpenseLayout.class;
//		else if (id == R.id.BtnTodayIncome) changeClass = ReportCurrentIncomeLayout.class;
//		else if (id == R.id.BtnSetting) changeClass = MainSettingLayout.class;
//		else if (id == R.id.BtnCard) changeClass = CardLayout.class;
//		else if (id == R.id.BtnAccount) changeClass = AccountLayout.class;
//		else if (id == R.id.BtnBudget) changeClass = BudgetLayout.class;
//		else if (id == R.id.BtnPurpose) changeClass = PurposeLayout.class;
//		else if (id == R.id.BtnSearch) changeClass = SearchLayout.class;
//		else {
//			Log.e(LogTag.LAYOUT, "== unregistered event hander ");
//			return;
//		}
//		
//    	Intent intent = new Intent(FmFinanceLayout.this, changeClass);
//    	intent.putExtra(MsgDef.ExtraNames.CATEGORY_ID, category.getId());
//		intent.putExtra(MsgDef.ExtraNames.CATEGORY_NAME, category.getName());
//		startActivity(intent);
    }
    

}
