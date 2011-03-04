package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

import com.fletamuto.sptb.data.AssetsFundItem;
import com.fletamuto.sptb.data.AssetsStockItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class FundData {	//�ݵ�
	public static final String STRING_NAME = "�ݵ�";
	public static final String STRING_BRAND = "��ǰ��";
	public static final String STRING_OPENING = "������";
	public static final String STRING_MATURITY = "������";
	public static final String STRING_PRICE = "���԰���";
	public static final String STRING_TYPE = "����";
	public static final String STRING_DEALER = "�Ǹ�ó";
	public static final String STRING_MEMO = "�޸�";
	public static final String STRING_REPEAT = "�ݺ�";
	
	private String brand;
	private Date opening;
	private Date maturity;
	private long price;
	private String type;
	private String dealer;
	private String memo;
	private String repeat;
	
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
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
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDealer() {
		return dealer;
	}
	public void setDealer(String dealer) {
		this.dealer = dealer;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getRepeat() {
		return repeat;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}

	public ArrayList<FundData> getFundDatas() {
		return getDatas();
	}
	
	private ArrayList<FundData> getDatas() {
		ArrayList<FinanceItem> financeItems = DBMgr.getAllItems(AssetsFundItem.TYPE);
		ArrayList<FundData> fundDatas = new ArrayList<FundData>();
		
		for(int i = 0, size = financeItems.size(); i < size; i++) {
			AssetsFundItem fundItem = null;
			if(AssetsFundItem.class.getName().equals(financeItems.get(i).getClass().getName()))
				fundItem = (AssetsFundItem)financeItems.get(i);
			else
				continue;
			FundData fundData = new FundData();
			
			fundData.setBrand(fundItem.getTitle());
			fundData.setOpening(fundItem.getCreateDate().getTime());
			fundData.setMaturity(fundItem.getExpiryDate().getTime());
			fundData.setPrice(fundItem.getAmount());
			fundData.setType(fundItem.getKindString());
			fundData.setDealer(fundItem.getStore());
			fundData.setMemo(fundItem.getMemo());
			fundData.setRepeat(fundItem.getRepeat().getRepeatMessage());
			
			fundDatas.add(fundData);
		}
		
		return fundDatas;
	}
}