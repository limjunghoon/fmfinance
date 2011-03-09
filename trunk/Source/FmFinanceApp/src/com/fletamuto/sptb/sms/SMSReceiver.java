package com.fletamuto.sptb.sms;

import java.util.Date;
import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.fletamuto.sptb.InputExpenseLayout;
import com.fletamuto.sptb.R;

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
					//smsParser.getParserData(number, SMSParser.TYPE_CARD, inputText[Integer.valueOf(number)]);	//수신된 SMS를 파싱하고 ExpenseItem 객체를 돌려 받는다
					// TODO 테스트
					int notificationId = new Random(System.currentTimeMillis()).nextInt();
					Intent sendIntent = new Intent(context, InputExpenseLayout.class);
					sendIntent.putExtra("Action", InputExpenseLayout.ACTION_SMS_RECEIVE);
					sendIntent.putExtra("SMS", smsParser.getParserData(number, SMSParser.TYPE_CARD, inputText[Integer.valueOf(number)]));
					sendIntent.putExtra("NotificationID", notificationId);

					PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
					Notification notification = new Notification(R.drawable.category_001, "테스트", date.getTime());
					notification.setLatestEventInfo(context, "테스트", msg, pendingIntent);
					
					NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
					manager.notify(notificationId, notification);
					
					break;
				}
			} catch (Exception e) {
				Log.e("SMSParser", e.getMessage());
				Log.e("SMSParser", "SMS Parser Error");
			}
		}
	}
}