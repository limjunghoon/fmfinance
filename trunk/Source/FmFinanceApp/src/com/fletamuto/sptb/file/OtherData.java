package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class OtherData {	//기타
	public static final String STRING_NAME = "기타";
	public static final String STRING_DATE = "날짜";
	public static final String STRING_TITLE = "제목";
	public static final String STRING_AMOUNT = "금액";
	public static final String STRING_MEMO = "메모";
	
	private Date date;
	private String title;
	private long amount;
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
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public ArrayList<OtherData> getOtherDatas() {
		return getDatas();
	}
	private ArrayList<OtherData> getDatas() {
		ArrayList<FinanceItem> financeItems = DBMgr.getAllItems(AssetsItem.TYPE);
		ArrayList<OtherData> otherDatas = new ArrayList<OtherData>();
		
		for(int i = 0, size = financeItems.size(); i < size; i++) {
			FinanceItem financeItem = null;
			if(financeItems.get(i).getCategory().getID() == 7)
				financeItem = financeItems.get(i);
			else
				continue;
			OtherData otherData = new OtherData();
			
			otherData.setDate(financeItem.getCreateDate().getTime());
			otherData.setTitle(financeItem.getTitle());
			otherData.setAmount(financeItem.getAmount());
			otherData.setMemo(financeItem.getMemo());
			
			otherDatas.add(otherData);
		}
		
		return otherDatas;
	}
}