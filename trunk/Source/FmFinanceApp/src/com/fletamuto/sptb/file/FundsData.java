package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

public class FundsData {	//펀드
	public static final String STRING_NAME = "펀드";
	public static final String STRING_BRAND = "상품명";
	public static final String STRING_OPENING = "개설일";
	public static final String STRING_MATURITY = "만기일";
	public static final String STRING_PRICE = "매입가격";
	public static final String STRING_TYPE = "유형";
	public static final String STRING_DEALER = "판매처";
	public static final String STRING_MEMO = "메모";
	public static final String STRING_REPEAT = "반복";
	
	public String brand;
	public Date opening;
	public Date maturity;
	public long price;
	public String type;
	public String dealer;
	public String memo;
	public String repeat;
	
	public static ArrayList<FundsData> getFundsDatas() {	// FIXME 객체를 얻어오는 부분
		ArrayList<FundsData> fundsDatas = new ArrayList<FundsData>();
		
		for(int i = 0; i < 10; i++) {	//출력 확인용 임시 데이터 만들기
			FundsData fundsData = new FundsData();
			
			fundsData.brand = "테스트 펀드" + i;
			fundsData.opening = new Date();
			fundsData.maturity = new Date();
			fundsData.price = 1000;
			fundsData.type = "테스트" + i;
			fundsData.dealer = "테스트" + i;
			fundsData.memo = "";
			fundsData.repeat = "매달 " + i + "일";
			fundsDatas.add(fundsData);
		}
		
		return fundsDatas;
	}
}