package com.fletamuto.sptb.db;

import java.util.ArrayList;

import com.fletamuto.sptb.data.BudgetItem;
import com.fletamuto.sptb.data.Category;
import com.fletamuto.sptb.data.ExpenseItem;

/**
 * 지출관련 DB를 관리
 * @author yongbban
 * @version 1.0.0.0
 */
public class BudgetDBConnector extends BaseDBConnector {

	public ArrayList<BudgetItem> getItem(final int year, final int month) {
		ArrayList<BudgetItem> budgetItems = new ArrayList<BudgetItem>();  
		budgetItems.add(new BudgetItem(year, month));
		
		ArrayList<Category> expenseCategories = DBMgr.getCategory(ExpenseItem.TYPE);
		int categorySize = expenseCategories.size();
		for (int categoryIndex = 0; categoryIndex < categorySize; categoryIndex++) {
			Category category = expenseCategories.get(categoryIndex);
			BudgetItem budgetItem = new BudgetItem(year, month);
			budgetItem.setExpenseCategory(category);
			budgetItem.setExpenseAmountMonth(DBMgr.getTotalAmountMonth(ExpenseItem.TYPE, category.getID(), year, month));
			budgetItems.add(budgetItem);
			
		}
		return budgetItems;
	}

}
