package com.fletamuto.sptb;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.ExpenseTag;
import com.fletamuto.sptb.data.PaymentMethod;
import com.fletamuto.sptb.db.DBMgr;

/**
 * 새로운 지출을 입력하거나 기존의 지출정보를 수정할때 보여주는 레이아웃 창
 * @author yongbban
 * @version 1.0.0.0
 */
public class InputExpenseLayout extends InputFinanceItemBaseLayout {
//	private int mPaymentSelected = PaymentMethod.CASH;
	protected final static int ACT_TAG_SELECTED = MsgDef.ActRequest.ACT_TAG_SELECTED;
	protected final static int ACT_REPEAT = MsgDef.ActRequest.ACT_REPEAT;
	protected final static int ACT_CARD_SELECT = MsgDef.ActRequest.ACT_CARD_SELECT;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_expense, true);
        
        updateChildView();
        setDateBtnClickListener(R.id.BtnExpenseDate);
        setAmountBtnClickListener(R.id.BtnExpenseAmount);
        setCategoryClickListener(R.id.BtnExpenseCategory);
        setPaymentToggleBtnClickListener();
        setTagButtonListener();
        setRepeatBtnClickListener();
        setTitle(getResources().getString(R.string.input_expense_name));
    }

	/**
     * 날짜 갱신
     */
	protected void updateDate() {
    	updateBtnDateText(R.id.BtnExpenseDate);
    } 
    
    protected void saveItem() {
    	if (mInputMode == InputMode.ADD_MODE) {
    		saveNewItem(ReportExpenseLayout.class);
    	}
    	else if (mInputMode == InputMode.EDIT_MODE){
    		saveUpdateItem();
    	}
    }

    /**
     * 금액을 갱신
     */
	protected void updateAmount(Long amount) {
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnExpenseAmount);
	}

	@Override
	protected void createItemInstance() {
		mItem = new ExpenseItem();
	}
	
	@Override
	protected boolean getItemInstance(int id) {
		mItem = DBMgr.getItem(ExpenseItem.TYPE, id);
		if (mItem == null) return false;
		return true;
	}

	@Override
	protected void onCategoryClick() {
		Intent intent = new Intent(InputExpenseLayout.this, SelectCategoryExpenseLayout.class);
		startActivityForResult(intent, ACT_CATEGORY);
	}
	
	@Override
	protected void updateCategory(int id, String name) {
		mItem.setCategory(id, name);
		updateBtnCategoryText(R.id.BtnExpenseCategory);
	}
	
	protected void updateBtnCategoryText(int btnID) {
		String categoryText = getResources().getString(R.string.input_select_category);
		ExpenseItem expenseItem = (ExpenseItem)mItem;
		if (expenseItem.isVaildCatetory()) {
			categoryText = String.format("%s - %s", expenseItem.getCategory().getName(), expenseItem.getSubCategory().getName());
		}
		 
    	((Button)findViewById(btnID)).setText(categoryText);
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_CATEGORY) {
    		if (resultCode == RESULT_OK) {
    			((ExpenseItem)mItem).setSubCategory(data.getIntExtra("SUB_CATEGORY_ID", -1), data.getStringExtra("SUB_CATEGORY_NAME"));
    		}
    	}
		
		if (requestCode == ACT_TAG_SELECTED) {
			if (resultCode == RESULT_OK) {
    			updateTag(data.getIntExtra(MsgDef.ExtraNames.TAG_ID, -1), data.getStringExtra(MsgDef.ExtraNames.TAG_NAME));
    		}
		}
		
		if (requestCode == ACT_CARD_SELECT) {
			if (resultCode == RESULT_OK) {
    			int selectedID = data.getIntExtra(MsgDef.ExtraNames.CARD_ID, -1);
    			if (selectedID == -1) return;
    			
    			CardItem selectedCard = DBMgr.getCardItem(selectedID);
    			if (selectedCard.getType() == CardItem.CREDIT_CARD) {
    				SelectedInstallmentPlan();
    			}
    			
    		}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 *  할부 방법을 선택한다.
	 */
	private void SelectedInstallmentPlan() {
		new AlertDialog.Builder(InputExpenseLayout.this)
	    .setTitle("할부선택")
	    .setSingleChoiceItems(R.array.select_installment_plan, 0, 
	      new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	          /* User clicked on a radio button do some stuff */
	        }
	      })
	      .setPositiveButton("확인", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	          /* User clicked Yes so do some stuff */
	        }
	      })
	      .setNegativeButton("취소", new DialogInterface.OnClickListener() {

	        public void onClick(DialogInterface dialog, int whichButton) {
	           /* User clicked No so do some stuff */
	        }
	     })
	     .create().show();

		
	}

	private void updateCard(int intExtra, String stringExtra) {
		// TODO Auto-generated method stub
		
	}

	private void updateTag(int id, String name) {
		((ExpenseItem)mItem).setTag(id, name);
		updateTagText();
	}
	
	protected void updateTagText() {
		String tagText = getResources().getString(R.string.input_expense_selected_tag);
		ExpenseTag tag = ((ExpenseItem)mItem).getTag();

		if (isValidTag()) {
			tagText = String.format("%s", tag.getName());
		}
		 
    	((Button)findViewById(R.id.BtnExpenseTag)).setText(tagText);
	}
	
	public boolean isValidTag() {
		ExpenseTag tag = ((ExpenseItem)mItem).getTag();
		return  !(tag == null || tag.getID() == -1);
	}


	/**
	 * 지불방법 토글버튼 클릭 시 
	 */
	protected void setPaymentToggleBtnClickListener() {
		
		((ToggleButton)findViewById(R.id.TBExpenseMethodCash)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				updatePaymentMethod(PaymentMethod.CASH);
			}
		});
		
		((ToggleButton)findViewById(R.id.TBExpenseMethodCard)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				updatePaymentMethod(PaymentMethod.CARD);
				
				Intent intent = new Intent(InputExpenseLayout.this, SelectCardLayout.class);
				startActivityForResult(intent, ACT_CARD_SELECT);
			}
		});
		
		((ToggleButton)findViewById(R.id.TBExpenseMethodAccount)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				updatePaymentMethod(PaymentMethod.ACCOUNT);
			}
		});
	}

	@Override
	protected void updateChildView() {
		updateDate();
		updateBtnCategoryText(R.id.BtnExpenseCategory);
		updateBtnAmountText(R.id.BtnExpenseAmount);
		updateEditMemoText(R.id.ETExpenseMemo);
		updatePaymentMethod(((ExpenseItem)mItem).getSelectedPaymentMethodType());
		updateTagText();
	}

	/**
	 * 지불정보 상태를 갱신한다.
	 */
	private void updatePaymentMethod(int paymentMethodSelected) {
		if (((ExpenseItem)mItem).createPaymentMethod(paymentMethodSelected) == false) {
			return;
		}
		
		ToggleButton btnCash = (ToggleButton)findViewById(R.id.TBExpenseMethodCash);
		ToggleButton btnCard = (ToggleButton)findViewById(R.id.TBExpenseMethodCard);
		ToggleButton btnAccount = (ToggleButton)findViewById(R.id.TBExpenseMethodAccount);
		
		if (paymentMethodSelected == PaymentMethod.CASH) {
			btnCash.setChecked(true);
			btnCard.setChecked(false);
			btnAccount.setChecked(false);
		}
		else if (paymentMethodSelected == PaymentMethod.CARD) {
			btnCash.setChecked(false);
			btnCard.setChecked(true);
			btnAccount.setChecked(false);
		}
		else if (paymentMethodSelected == PaymentMethod.ACCOUNT) {
			btnCash.setChecked(false);
			btnCard.setChecked(false);
			btnAccount.setChecked(true);
		}
		
		updatePaymentMethodText();
	}
	
    /**
     * 태그버튼 클릭 시
     */
    private void setTagButtonListener() {

    	((Button)findViewById(R.id.BtnExpenseTag)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputExpenseLayout.this, SelectTagLayout.class);
				
				startActivityForResult(intent, ACT_TAG_SELECTED);
			}
		});
    }
    
    /**
     * 반복버튼 클릭 시
     */
    private void setRepeatBtnClickListener() {
    	((Button)findViewById(R.id.BtnExpenseRepeat)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputExpenseLayout.this, RepeatLayout.class);
				startActivityForResult(intent, ACT_REPEAT);
			}
		});
	}

	private void updatePaymentMethodText() {
		TextView tvPaymentMethod = (TextView)findViewById(R.id.TVPaymentMethod);
		tvPaymentMethod.setText(((ExpenseItem)mItem).getPaymentMethod().getText());
	}

	@Override
	protected void updateItem() {
		String memo = ((TextView)findViewById(R.id.ETExpenseMemo)).getText().toString();
    	getItem().setMemo(memo);
	}
	
	
}
