package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

public class StockData {	//주식
	public static final String STRING_NAME = "주식";
	public static final String STRING_TICKER = "종목";
	public static final String STRING_DATE = "날짜";
	public static final String STRING_QUANTITY = "수량";
	public static final String STRING_PRICE = "주당가격";
	public static final String STRING_DEALER = "판매처";
	public static final String STRING_MEMO = "메모";
	
	public String ticker;
	public Date date;
	public int quantity;
	public long price;
	public String dealer;
	public String memo;
	
	public static ArrayList<StockData> getStockDatas() {	// FIXME 객체를 얻어오는 부분
		ArrayList<StockData> stockDatas = new ArrayList<StockData>();
		
		for(int i = 0; i < 10; i++) {	//출력 확인용 임시 데이터 만들기
			StockData stockData = new StockData();
			
			stockData.ticker = "테스트";
			stockData.date = new Date();
			stockData.quantity = 1000;
			stockData.price = 10000;
			stockData.dealer = "테스트";
			stockData.memo = "";
			stockDatas.add(stockData);
		}
		
		return stockDatas;
	}
}