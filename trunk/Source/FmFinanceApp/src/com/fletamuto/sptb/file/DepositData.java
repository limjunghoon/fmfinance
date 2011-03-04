package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.AssetsDepositItem;
import com.fletamuto.sptb.data.AssetsFundItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class DepositData {	//����	-	���� ���¿� ��� ����� ����
	public static final String STRING_NAME = "����";
	public static final String STRING_TITLE = "����";
	public static final String STRING_FINANCIAL = "�������";
	public static final String STRING_EXPECTAMOUNT = "������ͱ�";
	public static final String STRING_TOTALAMOUNT = "��ü�ݾ�";
	public static final String STRING_DEPO_DATE = "������";
	public static final String STRING_EXPI_DATE = "������";
	public static final String STRING_ACCOUNT = "����";
	public static final String STRING_INTEREST = "����";
	public static final String STRING_MEMO = "�޸�";
	
	private String title;
	private String finance;
	private long expectAmount;
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
	public long getExpectAmount() {
		return expectAmount;
	}
	public void setExpectAmount(long expectAmount) {
		this.expectAmount = expectAmount;
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


	public ArrayList<DepositData> getDepositDatas() {
		return getDatas();
	}
	
	protected ArrayList<DepositData> getDatas() {
		ArrayList<FinanceItem> financeItems = DBMgr.getAllItems(AccountItem.TIME_DEPOSIT);
		ArrayList<DepositData> depositDatas = new ArrayList<DepositData>();
		
		for(int i = 0, size = financeItems.size(); i < size; i++) {
			AssetsDepositItem depositItem = null;
			if(AssetsDepositItem.class.getName().equals(financeItems.get(i).getClass().getName()))
				depositItem = (AssetsDepositItem)financeItems.get(i);
			else
				continue;
			DepositData depositData = new DepositData();
			
			depositData.setTitle(depositItem.getTitle());
			depositData.setFinance(depositItem.getAccount().getCompany().getName());
			depositData.setExpectAmount(depositItem.getExpectAmount());
			depositData.setTotalAmount(depositItem.getTotalAmount());
			depositData.setDepoDate(depositItem.getCreateDate().getTime());
			depositData.setExpiDate(depositItem.getExpiryDate().getTime());
			depositData.setAccount(depositItem.getAccount().getNumber());
			depositData.setInterest(depositItem.getRate());
			depositData.setMemo(depositItem.getMemo());
			
			depositDatas.add(depositData);
		}
		
		return depositDatas;
	}
}