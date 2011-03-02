package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class ExpenseData {
	public static final String STRING_DATE = "년도/월/일";
	public static final String STRING_CATEGORY = "분류";
	public static final String STRING_AMOUNT = "금액";
	public static final String STRING_PAY_METHOD = "결제방법";
	public static final String STRING_PAY_MONNEY_BALANCE = "잔액";
	public static final String STRING_PAY_ACCOUNT = "결제계좌";
	public static final String STRING_PAY_ACCOUNT_BALANCE = "계좌잔액";
	public static final String STRING_PAY_BALANCE = "충전잔액";
	public static final String STRING_MEMO = "메모";
	public static final String STRING_TAG = "태그";
	public static final String STRING_REPEAT = "반복";

	private Calendar date;
	private String category;
	private long amount;
	private String payMethod;
	private long monneyBalance;
	private String account;
	private long accountBalance;
	private long balance;
	private String memo;
	private String tag;
	private String repeat;


	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public long getMonneyBalance() {
		return monneyBalance;
	}
	public void setMonneyBalance(long monneyBalance) {
		this.monneyBalance = monneyBalance;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public long getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(long accountBalance) {
		this.accountBalance = accountBalance;
	}
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getRepeat() {
		return repeat;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	
	public ArrayList<ExpenseData> getExpenseData() {
		return new GetExpenseDatas().getExpenseDatas();
	}
}

class GetExpenseDatas {
	protected ArrayList<ExpenseData> getExpenseDatas() {
		Calendar calendar = Calendar.getInstance(Locale.KOREA);
		ArrayList<FinanceItem> financeMyPocketItems = DBMgr.getExpenseItemFromMypocket(calendar.get(Calendar.YEAR), calendar.get(Calendar.DATE));
		ArrayList<ExpenseData> expendDatas = new ArrayList<ExpenseData>();

		for(int i = 0, size = financeMyPocketItems.size(); i < size; i++) {
			ExpenseData data = new ExpenseData();
			data.setDate(financeMyPocketItems.get(i).getCreateDate());
			data.setCategory(financeMyPocketItems.get(i).getCategory().getName() + " - " + financeMyPocketItems.get(i).getSubCategory().getName());
			data.setAmount(financeMyPocketItems.get(i).getAmount());
			data.setPayMethod("현금");
			data.setMonneyBalance(0);
			data.setAccount("");
			data.setAccountBalance(0);
			data.setBalance(0);
			data.setMemo(financeMyPocketItems.get(i).getMemo());
			data.setTag(financeMyPocketItems.get(i).getMemo());
			data.setRepeat(financeMyPocketItems.get(i).getRepeat().getRepeatMessage());
			
			expendDatas.add(data);
		}
		return expendDatas;
	}
}
/*
 * private Calendar date;
	private String category;
	private long amount;
	private String payMethod;
	private long monneyBalance;
	private String account;
	private long accountBalance;
	private long balance;
	private String memo;
	private String tag;
	private String repeat;
 */