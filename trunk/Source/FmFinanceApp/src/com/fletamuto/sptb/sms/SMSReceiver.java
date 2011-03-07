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
		//String originating_number = "";
		String msg = "";
		Date date = null;
		
		if(!bundle.isEmpty()) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			smsMessage = new SmsMessage[pdus.length];
			
			for(int i = 0, size = smsMessage.length; i < size; i++) {
				smsMessage[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				number = smsMessage[i].getDisplayOriginatingAddress();
				//originating_number = smsMessage[i].getOriginatingAddress();
				msg = smsMessage[i].getMessageBody().toString();
				date = new Date(smsMessage[i].getTimestampMillis());
			}
			
			//Toast.makeText(context, "발신번호 : " + number + /*"\n숨은발신번호 : " + originating_number +*/ "\n메시지 : \n" + msg + "\n시간 : " + date.toLocaleString(), Toast.LENGTH_LONG).show();
			//Toast.makeText(context, msg.split("\n")[4], Toast.LENGTH_LONG).show();
		}
		
		
		//파싱 테스트 용
		String parseText1 = "[KB체크]\n" +
							"홍길동님\n" +
							"0*0*카드\n" +
							"00월00일00:00\n" +
							"<amount>원<installment>\n" +
							"<shop> 사용";
		String parseText2 = "삼성카드\n" +
							"04/01 16:00\n" +
							"<shop>\n" +
							"<amount>원\n" +
							"<installment>사용\n" +
							"감사합니다";
		String parseText3 = "신한카드정상승인\n" +
							"홍길동님\n" +
							"00/00 00:00\n" +
							"<amount>원<installment>\n" +
							"<shop>";
		
		String inputText1 = "[KB체크]\n" +
							"홍길동님\n" +
							"0*0*카드\n" +
							"00월00일00:00\n" +
							"123,456원\n" +
							"어딘가 사용";
		String inputText2 = "삼성카드\n" +
							"04/01 16:00\n" +
							"(주)롯데백화점\n" +
							"2,700,000원\n" +
							"일시불사용\n" +
							"감사합니다";
		String inputText3 = "신한카드정상승인\n" +
							"홍길동님\n" +
							"00/00 00:00\n" +
							"123,456원(일시불)\n" +
							"어딘가";
		
		SMSParser smsParser = new SMSParser(context);
		smsParser.setParserData(number, parseText3);	//SMS 파싱을 위한 형식 저장 메소드
		smsParser.getParserData(number, inputText3);	//수신된 SMS를 파싱하기 위한 메소드
	}
}