package com.fletamuto.sptb.sms;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		SmsMessage[] smsMessage = null;
		String number = "";
		String msg = "";
		Date date = null;
		
		if(!bundle.isEmpty()) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			smsMessage = new SmsMessage[pdus.length];
			
			for(int i = 0, size = smsMessage.length; i < size; i++) {
				smsMessage[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				number = smsMessage[i].getDisplayOriginatingAddress();
				msg = smsMessage[i].getMessageBody().toString();
				date = new Date(smsMessage[i].getTimestampMillis());
			}
		}
		
		
		//파싱 테스트 용
		String[] inputText = new String[4];
		inputText[0] = "[KB체크]\n" +
							"홍길동님\n" +
							"0*0*카드\n" +
							"00월00일00:00\n" +
							"123,456원\n" +
							"어딘가 사용";
		inputText[1] = "삼성카드\n" +
							"04/01 16:00\n" +
							"(주)롯데백화점\n" +
							"2,700,000원\n" +
							"일시불사용\n" +
							"감사합니다";
		inputText[2] = "신한카드정상승인\n" +
							"홍길동님\n" +
							"00/00 00:00\n" +
							"123,456원(일시불)\n" +
							"어딘가";
		inputText[3] = "롯데카드 홍길동님 123456원 할부(10) 00/00 00:00 어딘가";
		
		
		SMSParser smsParser = new SMSParser(context);
		//저장된 번호인지 확인
		if(smsParser.isNumber(number)) {
			try {
				switch(smsParser.getNumberType(number)) {
				case SMSParser.TYPE_NONE:
					break;
				case SMSParser.TYPE_CARD:
					smsParser.getParserData(number, SMSParser.TYPE_CARD, inputText[Integer.valueOf(number)]);	//수신된 SMS를 파싱하기 위한 메소드
					break;
				}

				//TODO 입력된 데이터로 expense_sms에 데이터를 채우는 메소드 구현
				//set---(new Data(), number, msg);
			} catch (Exception e) {

			}
		}
	}
}