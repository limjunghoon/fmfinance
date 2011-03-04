package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class SavingsData {	//적금
	public static final String STRING_NAME = "적금";
	public static final String STRING_TITLE = "제목";
	public static final String STRING_FINANCIAL = "금융기관";
	public static final String STRING_AMOUNT = "납입금액";
	public static final String STRING_TOTALAMOUNT = "전체금액";
	public static final String STRING_DEPO_DATE = "예금일";
	public static final String STRING_EXPI_DATE = "만기일";
	public static final String STRING_ACCOUNT = "계좌";
	public static final String STRING_INTEREST = "이율";
	public static final String STRING_MEMO = "메모";
	
	private String title;
	private String finance;
	private long amount;
	private long totalAmount;
	private Date depoDate;
	private Date expiDate;
	private String account;
	private float interest;
	private String memo;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFinance() {
		return finance;
	}
	public void setFinance(String finance) {
		this.finance = finance;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public long getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Date getDepoDate() {
		return depoDate;
	}
	public void setDepoDate(Date depoDate) {
		this.depoDate = depoDate;
	}
	public Date getExpiDate() {
		return expiDate;
	}
	public void setExpiDate(Date expiDate) {
		this.expiDate = expiDate;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public float getInterest() {
		return interest;
	}
	public void setInterest(float interest) {
		this.interest = interest;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}


	public ArrayList<SavingsData> getSavingsDatas() {
		return getDatas();
	}
	
	protected ArrayList<SavingsData> getDatas() {
		ArrayList<FinanceItem> financeItems = DBMgr.getAllItems(AccountItem.SAVINGS);
		ArrayList<SavingsData> savingsDatas = new ArrayList<SavingsData>();
		
		for(int i = 0, size = financeItems.size(); i < size; i++) {
			com.fletamuto.sptb.data.AssetsSavingsItem savingsItem = DBMgr.getAssetsDBConnecter().getSavingsItem(financeItems.get(i).getID());
			SavingsData savingsData = new SavingsData();
			
			savingsData.setTitle(savingsItem.getTitle());
			savingsData.setFinance(savingsItem.getAccount().getCompany().getName());
			savingsData.setAmount(savingsItem.getAmount());
			savingsData.setDepoDate(savingsItem.getCreateDate().getTime());
			savingsData.setExpiDate(savingsItem.getExpiryDate().getTime());
			savingsData.setAccount(savingsItem.getAccount().getNumber());
			//depositData.setInterest(savingsItem);
			savingsData.setMemo(savingsItem.getMemo());
			
			savingsDatas.add(savingsData);
		}
		
		return savingsDatas;
	}
}