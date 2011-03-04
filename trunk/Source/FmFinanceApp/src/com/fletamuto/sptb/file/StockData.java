package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

import com.fletamuto.sptb.data.AssetsExtendItem;
import com.fletamuto.sptb.data.AssetsItem;
import com.fletamuto.sptb.data.AssetsStockItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class StockData {	//주식
	public static final String STRING_NAME = "주식";
	public static final String STRING_TICKER = "종목";
	public static final String STRING_DATE = "날짜";
	public static final String STRING_QUANTITY = "수량";
	public static final String STRING_PRICE = "주당가격";
	public static final String STRING_DEALER = "판매처";
	public static final String STRING_MEMO = "메모";
	
	private String ticker;
	private Date date;
	private long quantity;
	private long price;
	private String dealer;
	private String memo;
	
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
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

	public ArrayList<StockData> getStockDatas() {
		return getDatas();
	}
	
	private ArrayList<StockData> getDatas() {
		ArrayList<FinanceItem> financeItems = DBMgr.getAllItems(AssetsStockItem.TYPE);
		ArrayList<StockData> stockDatas = new ArrayList<StockData>();
		
		for(int i = 0, size = financeItems.size(); i < size; i++) {
			AssetsStockItem stockItem = null;
			if(AssetsStockItem.class.getName().equals(financeItems.get(i).getClass().getName()))
				stockItem = (AssetsStockItem)financeItems.get(i);
			else
				continue;
			StockData stockData = new StockData();
			
			stockData.setTicker(stockItem.getTitle());
			stockData.setDate(stockItem.getCreateDate().getTime());
			stockData.setQuantity(stockItem.getTotalCount());
			stockData.setPrice(stockItem.getPrice());
			stockData.setDealer(stockItem.getStore());
			stockData.setMemo(stockItem.getMemo());
			
			stockDatas.add(stockData);
		}
		
		return stockDatas;
	}
}