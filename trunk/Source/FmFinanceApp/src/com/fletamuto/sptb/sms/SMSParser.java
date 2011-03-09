package com.fletamuto.sptb.sms;

import com.fletamuto.sptb.data.ExpenseItem;

import android.content.Context;

public class SMSParser {
	private Context context;
	private SMSCardParser smsCardParser;
	
	static final int TYPE_NONE = 0;
	static final int TYPE_CARD = 1;
	
	public SMSParser(Context context) {
		super();
		this.context = context;
		this.smsCardParser = new SMSCardParser(context);
		
		
		//파싱 테스트용
		String parseText[] = new String[4];
		parseText[0] = "[KB체크]\n" +
							"홍길동님\n" +
							"0*0*카드\n" +
							"00월00일00:00\n" +
							"<amount>원<installment>\n" +
							"<shop> 사용";
		parseText[1] = "삼성카드\n" +
							"04/01 16:00\n" +
							"<shop>\n" +
							"<amount>원\n" +
							"<installment>사용\n" +
							"감사합니다";
		parseText[2] = "신한카드정상승인\n" +
							"홍길동님\n" +
							"00/00 00:00\n" +
							"<amount>원<installment>\n" +
							"<shop>";
		parseText[3] = "롯데카드 홍길동님 <amount>원 <installment> 00/00 00:00 <shop>";
		
		if(smsCardParser.getDbCount() == 0) {
			//임시값
			int typeId = 1, cardId = 1;
			//DB대신 사용할 값 입력 부분
			for(int i = 0, size = parseText.length; i < size; i++)
				smsCardParser.setParserData(String.valueOf(i), parseText[i], typeId, cardId);	//SMS 파싱을 위한 형식 저장 메소드 - DB에 저장	Integer.valueOf(msg) 테스트용
		}
	}

	/** 등록된 번호인지 확인 해주는 메소드 */
	public boolean isNumber(String number) {
		return smsCardParser.isNumber(number);
	}
	
	/** 입력한 번호가 어느 타입인지 */
	public int getNumberType(String number) {
		if(smsCardParser.isNumber(number))
			return TYPE_CARD;
		else
			return TYPE_NONE;
	}

	public ExpenseItem getParserData(String number, int typeId, String inputText) {
		switch(typeId) {
		case TYPE_NONE:
			return null;
		case TYPE_CARD:
			return smsCardParser.getParserData(number, inputText);
		}
		return null;
	}
}
