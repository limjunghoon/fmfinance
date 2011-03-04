package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

import com.fletamuto.sptb.data.AssetsFundItem;
import com.fletamuto.sptb.data.AssetsStockItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class FundData {	//펀드
	public static final String STRING_NAME = "펀드";
	public static final String STRING_BRAND = "상품명";
	public static final String STRING_OPENING = "개설일";
	public static final String STRING_MATURITY = "만기일";
	public static final String STRING_PRICE = "매입가격";
	public static final String STRING_TYPE = "유형";
	public static final String STRING_DEALER = "판매처";
	public static final String STRING_MEMO = "메모";
	public static final String STRING_REPEAT = "반복";
	
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