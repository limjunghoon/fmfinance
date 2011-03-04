package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

import com.fletamuto.sptb.data.AssetsFundItem;
import com.fletamuto.sptb.data.AssetsInsuranceItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class InsuranceData {	//보험
	public static final String STRING_NAME = "보험";
	public static final String STRING_TITLE = "제목";
	public static final String STRING_OPENING = "개설일";
	public static final String STRING_MATURITY = "만기일";
	public static final String STRING_AMOUNT = "금액";
	public static final String STRING_INSURANCE = "보험사";
	public static final String STRING_MEMO = "메모";
	
	private String title;
	private Date opening;
	private Date maturity;
	private long amount;
	private String insurance;
	private String memo;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getOpening() {
		return opening;
	}
	public void setOpening(Date opening) {
		this.opening = opening;
	}
	public Date getMaturity() {
		return maturity;
	}
	public void setMaturity(Date maturity) {
		this.maturity = maturity;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public String getInsurance() {
		return insurance;
	}
	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public ArrayList<InsuranceData> getInsuranceDatas() {
		return getDatas();
	}
	
	private ArrayList<InsuranceData> getDatas() {
		ArrayList<FinanceItem> financeItems = DBMgr.getAllItems(AssetsInsuranceItem.TYPE);
		ArrayList<InsuranceData> insuranceDatas = new ArrayList<InsuranceData>();
		
		for(int i = 0, size = financeItems.size(); i < size; i++) {
			AssetsInsuranceItem insuranceItem = null;
			if(AssetsInsuranceItem.class.getName().equals(financeItems.get(i).getClass().getName()))
				insuranceItem = (AssetsInsuranceItem)financeItems.get(i);
			else
				continue;
			InsuranceData insuranceData = new InsuranceData();
			
			insuranceData.setTitle(insuranceItem.getTitle());
			insuranceData.setOpening(insuranceItem.getCreateDate().getTime());
			insuranceData.setMaturity(insuranceItem.getExpiryDate().getTime());
			insuranceData.setAmount(insuranceItem.getAmount());
			insuranceData.setInsurance(insuranceItem.getCompany());
			insuranceData.setMemo(insuranceItem.getMemo());
			
			insuranceDatas.add(insuranceData);
		}
		
		return insuranceDatas;
	}
}