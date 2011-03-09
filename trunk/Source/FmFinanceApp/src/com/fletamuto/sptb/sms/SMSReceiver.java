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
import com.fletamuto.sptb.data.ExpenseSMS;
import com.fletamuto.sptb.db.DBMgr;
import com.fletamuto.sptb.db.ExpenseDBConnector;
import com.fletamuto.sptb.db.SMSDBConnector;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		SmsMessage[] smsMessage = null;
		ExpenseSMS expenseSMS = new ExpenseSMS();
				
		if(!bundle.isEmpty()) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			smsMessage = new SmsMessage[pdus.length];
			
			for(int i = 0, size = smsMessage.length; i < size; i++) {
				smsMessage[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				expenseSMS.setNmuber(smsMessage[i].getDisplayOriginatingAddress());
				expenseSMS.setMessage(smsMessage[i].getMessageBody().toString());
				expenseSMS.setCreateDate(new Date(smsMessage[i].getTimestampMillis()));
			}
		}
		
		
		//�Ľ� �׽�Ʈ ��
		String[] inputText = new String[4];
		inputText[0] = "[KBüũ]\n" +
							"ȫ�浿��\n" +
							"0*0*ī��\n" +
							"00��00��00:00\n" +
							"123,456��\n" +
							"��� ���";
		inputText[1] = "�Ｚī��\n" +
							"04/01 16:00\n" +
							"(��)�Ե���ȭ��\n" +
							"2,700,000��\n" +
							"�Ͻúһ��\n" +
							"�����մϴ�";
		inputText[2] = "����ī���������\n" +
							"ȫ�浿��\n" +
							"00/00 00:00\n" +
							"123,456��(�Ͻú�)\n" +
							"���";
		inputText[3] = "�Ե�ī�� ȫ�浿�� 123456�� �Һ�(10) 00/00 00:00 ���";
		
		
		SMSParser smsParser = new SMSParser(context);
		//����� ��ȣ���� Ȯ��
		if(smsParser.isNumber(expenseSMS.getNmuber())) {
			try {
				switch(smsParser.getNumberType(expenseSMS.getNmuber())) {
				case SMSParser.TYPE_NONE:
					break;
				case SMSParser.TYPE_CARD:
					//smsParser.getParserData(number, SMSParser.TYPE_CARD, inputText[Integer.valueOf(number)]);	//���ŵ� SMS�� �Ľ��ϰ� ExpenseItem ��ü�� ���� �޴´�
					int notificationId = new Random(System.currentTimeMillis()).nextInt();
					Intent sendIntent = new Intent(context, InputExpenseLayout.class);
					sendIntent.putExtra("Action", InputExpenseLayout.ACTION_SMS_RECEIVE);
					sendIntent.putExtra("SMS", smsParser.getParserData(expenseSMS.getNmuber(), SMSParser.TYPE_CARD, inputText[Integer.valueOf(expenseSMS.getNmuber())]));	//TODO �׽�Ʈ�� - �Ľ��� ��ȭ��ȣ�� ���ľ� ��. DB �Է� ����� �����Ǹ� ���� 
					sendIntent.putExtra("NotificationID", notificationId);

					PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
					Notification notification = new Notification(R.drawable.category_001, "�׽�Ʈ", expenseSMS.getCreateDate().getTime().getTime());
					notification.setLatestEventInfo(context, "�׽�Ʈ", expenseSMS.getMessage(), pendingIntent);
					
					NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
					manager.notify(notificationId, notification);
					
					DBMgr.addSMSItem(expenseSMS);
					break;
				}
			} catch (Exception e) {
				Log.e("SMSParser", e.getMessage());
				Log.e("SMSParser", "SMS Parser Error");
			}
		}
	}
}