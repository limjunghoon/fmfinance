package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

import com.fletamuto.sptb.data.AssetsInsuranceItem;
import com.fletamuto.sptb.data.AssetsRealEstateItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class RealEstateData {	//�ε���
	public static final String STRING_NAME = "�ε���";
	public static final String STRING_DATE = "��¥";
	public static final String STRING_TITLE = "����";
	public static final String STRING_AMOUNT = "�ݾ�";
	public static final String STRING_SCALE = "�Ը�";
	public static final String STRING_MEMO = "�޸�";
	
	private Date date;
	private String title;
	private long amount;
	private String scale;
	private String memo;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public ArrayList<RealEstateData> getRealEstateDatas() {
		return getDatas();
	}
	
	private ArrayList<RealEstateData> getDatas() {
		ArrayList<FinanceItem> financeItems = DBMgr.getAllItems(AssetsRealEstateItem.TYPE);
		ArrayList<RealEstateData> realEstateDatas = new ArrayList<RealEstateData>();
		
		for(int i = 0, size = financeItems.size(); i < size; i++) {
			AssetsRealEstateItem realEstateItem = null;
			if(AssetsRealEstateItem.class.getName().equals(financeItems.get(i).getClass().getName()))
				realEstateItem = (AssetsRealEstateItem)financeItems.get(i);
			else
				continue;
			RealEstateData realEstateData = new RealEstateData();
			
			realEstateData.setTitle(realEstateItem.getTitle());
			realEstateData.setDate(realEstateItem.getCreateDate().getTime());
			realEstateData.setAmount(realEstateItem.getAmount());
			realEstateData.setScale(realEstateItem.getScale());
			realEstateData.setMemo(realEstateItem.getMemo());
			
			realEstateDatas.add(realEstateData);
		}
		
		return realEstateDatas;
	}
}