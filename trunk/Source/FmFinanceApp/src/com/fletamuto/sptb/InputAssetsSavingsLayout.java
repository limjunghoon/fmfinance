package com.fletamuto.sptb;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.AssetsSavingsItem;
import com.fletamuto.sptb.data.Repeat;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.util.LogTag;

/**
 * 자산입력
 * @author yongbban
 * @version  1.0.0.1
 */
public class InputAssetsSavingsLayout extends InputAssetsExtendLayout {
	public static final int ACT_ADD_ACCOUNT = MsgDef.ActRequest.ACT_ADD_ACCOUNT;
	public static final int ACT_EDIT_ACCOUNT = MsgDef.ActRequest.ACT_EDIT_ACCOUNT;
	private AssetsSavingsItem mSavings;
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.input_assets_savings, true);
    
    	
    	updateChildView();
    	
    	//달력을 이용한 날짜 입력을 위해
        LinearLayout linear = (LinearLayout) findViewById(R.id.inputAssetsSavings);
        View popupview = View.inflate(this, R.layout.monthly_calendar_popup, null);
        final Intent intent = getIntent();        
        monthlyCalendar = new MonthlyCalendar(this, intent, popupview, linear);
        
        setCreateDateBtnClickListener(R.id.BtnSavingsCreateDate); 
        setAmountBtnClickListener(R.id.BtnSavingsAmount);
        setExpiryBtnClickListener(R.id.BtnSavingsExpiryDate);
        setAccountBtnClickListener();
    }
    
    @Override
    public void finish() {
    	if (mSavings.getID() == -1 && mSavings.getAccount().getID() != -1) {
    		DBMgr.deleteAccount(mSavings.getAccount().getID());
    	}
    	
    	super.finish();
    }
 
    
    public boolean checkInputData() {
//    	if (mSavings.getAccount().getID() == -1) {
//    		displayAlertMessage("계좌가 선택되지 않았습니다.");
//    		return false;
//    	}
    	if (mSavings.getCreateDate().after(Calendar.getInstance())) {
    		displayAlertMessage("개설일 다시 지정해 주세요");
    		return false;
    	}
    	
    	if (mSavings.getCreateDate().after(mSavings.getExpiryDate())) {
    		displayAlertMessage("만기일을 다시 지정해 주세요");
    		return false;
    	}
    	
    	return super.checkInputData();
    };
    

	private void setExpiryBtnClickListener(int resource) {
		((Button)findViewById(resource)).setOnClickListener(new Button.OnClickListener() {
			
			public void onClick(View v) {

				monthlyCalendar.showMonthlyCalendarPopup();
				monthlyCalendar.getPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
					
					public void onDismiss() {
						if (monthlyCalendar.getSelectCalendar() == null) return;
						mSavings.getExpiryDate().set(Calendar.YEAR, monthlyCalendar.getSelectCalendar().get(Calendar.YEAR));
						mSavings.getExpiryDate().set(Calendar.MONTH, monthlyCalendar.getSelectCalendar().get(Calendar.MONTH));
						mSavings.getExpiryDate().set(Calendar.DAY_OF_MONTH, monthlyCalendar.getSelectCalendar().get(Calendar.DAY_OF_MONTH));
						updateExpiryDate();
					}
				});
			}
		 });
	}

	private void setCreateDateBtnClickListener(int resource) {
		setDateBtnClickListener(resource);
	}

	@Override
	protected void updateRepeat(int type, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateDate() {
		updateBtnDateText(R.id.BtnSavingsCreateDate);
	}
	
	protected void updateExpiryDate() {
		updateBtnExpiryDateText(R.id.BtnSavingsExpiryDate);
	}
	
	protected void updateBtnExpiryDateText(int btnID) {	
    	((Button)findViewById(btnID)).setText(mSavings.getExpriyDateString());
    }

	@Override
	protected void createItemInstance() {
		mSavings = new AssetsSavingsItem();
		setItem(mSavings);
	}

	@Override
	protected boolean getItemInstance(int id) {
//		mSalary = (IncomeSalaryItem) DBMgr.getItem(IncomeItem.TYPE, id);
		if (mSavings == null) return false;
		setItem(mSavings);
		return true;
	}

	@Override
	protected void updateChildView() {
		updateDate();
		updateExpiryDate();
		updateBtnAmountText(R.id.BtnSavingsAmount);
		updateEditMemoText(R.id.ETSavingsMemo);
		updateAccountText();
	}

	@Override
	protected void updateItem() {
		String memo = ((TextView)findViewById(R.id.ETSavingsMemo)).getText().toString();
    	getItem().setMemo(memo);
    	
    	String title = ((TextView)findViewById(R.id.ETSavingsTitle)).getText().toString();
    	getItem().setTitle(title);
	}
	
    @Override
	protected void updateAmount(Long amount) {
    	mSavings.setPayment(amount);
		super.updateAmount(amount);
		updateBtnAmountText(R.id.BtnSavingsAmount);
	}
    
    
    protected void setAccountBtnClickListener() {
    	Button btnAccount = (Button)findViewById(R.id.BtnSavingsAccount);
    	btnAccount.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(InputAssetsSavingsLayout.this, InputAccountLayout.class);
				intent.putExtra(MsgDef.ExtraNames.ACCOUNT_TYPE, AccountItem.SAVINGS);
				intent.putExtra(MsgDef.ExtraNames.EDIT_ITEM_ID, mSavings.getAccount().getID());
				startActivityForResult(intent, MsgDef.ActRequest.ACT_ADD_ACCOUNT);
			}
		});
    }
    
    private void updateAccount(AccountItem account) {
		if (account == null){
			return;
		}
		
		mSavings.setAccount(account);
		updateAccountText();
	}
    
    /**
	 *  계좌 이름을 갱신한다.
	 */
	private void updateAccountText() {
		if (mSavings.getAccount().getID() == -1) {
			((Button)findViewById(R.id.BtnSavingsAccount)).setText("계좌를 선택해 주세요");
		}
		else {
			((Button)findViewById(R.id.BtnSavingsAccount)).setText(String.format("%s : %s", mSavings.getAccount().getCompany().getName(), mSavings.getAccount().getNumber()));
		}
	}
   
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACT_ADD_ACCOUNT || requestCode == ACT_EDIT_ACCOUNT) {
    		if (resultCode == RESULT_OK) {
    			updateAccount( getAccount(data.getIntExtra(MsgDef.ExtraNames.ACCOUNT_ID, -1)));
    		}
    	}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	protected AccountItem getAccount(int id) {
		AccountItem account = DBMgr.getAccountItem(id);
		if (account == null) {
			Log.e(LogTag.LAYOUT, ":: not found account item ID :" + id);
		}
		return account;
	}
    
	
	@Override
	protected boolean saveNewItem(Class<?> cls) {
		if (DBMgr.addExtendAssetsSavings(mSavings) == -1) {
    		Log.e(LogTag.LAYOUT, "== NEW fail to the save item : " + mSavings.getID());
    		return false;
    	}
		
		if (mSavings.getAccount().getID() != -1) {
			DBMgr.updateAccount(mSavings.getAccount());
		}
    	
    	if (cls != null) {
    		Intent intent = new Intent(InputAssetsSavingsLayout.this, cls);
    		startActivity(intent);
    	}
    	else {
    		Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
    	}
    	
		return true;
	}
	
	protected void saveItem() {
		Repeat repeat = mSavings.getRepeat();
		repeat.setLastApplyDay(mSavings.getCreateDate());
		repeat.setMonthlyRepeat(mSavings.getCreateDate().get(Calendar.DAY_OF_MONTH));
		mSavings.setRepeat(repeat);
		super.saveItem();
	}
}
