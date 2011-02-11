package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Date;

public class ExpendData {
	//년도/월/일	수입/지출	구분	금액	결제	메모	태그	반복
	public static final String STRING_DATE = "년도/월/일";
	public static final String STRING_EXPEND = "수입/지출";
	public static final String STRING_TYPE = "구분";
	public static final String STRING_COAST = "금액";
	public static final String STRING_PAY = "결제";
	public static final String STRING_MEMO = "메모";
	public static final String STRING_TAG = "태그";
	public static final String STRING_REPEAT = "반복";
	
	public Date date;
	public String expend;
	public String type;
	public long coast;
	public String pay;
	public String memo;
	public String tag;
	public String repeat; 
}

class ExpendDatas {	//임의로 만든 데이터 - 엑셀 화면에 뿌려지게만 하는 것이 목적
	public ArrayList<ExpendData> expendDatas;
	
	public ExpendDatas() {
		ArrayList<ExpendData> expendDatas = new ArrayList<ExpendData>();	// FIXME 객체를 얻어오는 부분
		ExpendData expendData = new ExpendData();
		expendData.date = new Date();
		expendData.expend = "수입";
		expendData.type = "급여";
		expendData.coast = 2000000;
		expendData.pay = "";
		expendData.memo = "";
		expendData.tag = "급여";
		expendData.repeat = "매월 8일 반복";
		for(int i = 0; i < 10; i++) {
			expendDatas.add(expendData);
		}
		this.expendDatas = expendDatas;
	}
}