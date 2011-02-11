package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

public class StockData {	//�ֽ�
	public static final String STRING_NAME = "�ֽ�";
	public static final String STRING_TICKER = "����";
	public static final String STRING_DATE = "��¥";
	public static final String STRING_QUANTITY = "����";
	public static final String STRING_PRICE = "�ִ簡��";
	public static final String STRING_DEALER = "�Ǹ�ó";
	public static final String STRING_MEMO = "�޸�";
	
	public String ticker;
	public Date date;
	public int quantity;
	public long price;
	public String dealer;
	public String memo;
	
	public static ArrayList<StockData> getStockDatas() {	// FIXME ��ü�� ������ �κ�
		ArrayList<StockData> stockDatas = new ArrayList<StockData>();
		
		for(int i = 0; i < 10; i++) {	//��� Ȯ�ο� �ӽ� ������ �����
			StockData stockData = new StockData();
			
			stockData.ticker = "�׽�Ʈ";
			stockData.date = new Date();
			stockData.quantity = 1000;
			stockData.price = 10000;
			stockData.dealer = "�׽�Ʈ";
			stockData.memo = "";
			stockDatas.add(stockData);
		}
		
		return stockDatas;
	}
}