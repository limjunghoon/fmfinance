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
			
			//Toast.makeText(context, "�߽Ź�ȣ : " + number + /*"\n�����߽Ź�ȣ : " + originating_number +*/ "\n�޽��� : \n" + msg + "\n�ð� : " + date.toLocaleString(), Toast.LENGTH_LONG).show();
			//Toast.makeText(context, msg.split("\n")[4], Toast.LENGTH_LONG).show();
		}
		
		
		//�Ľ� �׽�Ʈ ��
		String parseText1 = "[KBüũ]\n" +
							"ȫ�浿��\n" +
							"0*0*ī��\n" +
							"00��00��00:00\n" +
							"<amount>��<installment>\n" +
							"<shop> ���";
		String parseText2 = "�Ｚī��\n" +
							"04/01 16:00\n" +
							"<shop>\n" +
							"<amount>��\n" +
							"<installment>���\n" +
							"�����մϴ�";
		String parseText3 = "����ī���������\n" +
							"ȫ�浿��\n" +
							"00/00 00:00\n" +
							"<amount>��<installment>\n" +
							"<shop>";
		
		String inputText1 = "[KBüũ]\n" +
							"ȫ�浿��\n" +
							"0*0*ī��\n" +
							"00��00��00:00\n" +
							"123,456��\n" +
							"��� ���";
		String inputText2 = "�Ｚī��\n" +
							"04/01 16:00\n" +
							"(��)�Ե���ȭ��\n" +
							"2,700,000��\n" +
							"�Ͻúһ��\n" +
							"�����մϴ�";
		String inputText3 = "����ī���������\n" +
							"ȫ�浿��\n" +
							"00/00 00:00\n" +
							"123,456��(�Ͻú�)\n" +
							"���";
		
		SMSParser smsParser = new SMSParser(context);
		smsParser.setParserData(number, parseText3);	//SMS �Ľ��� ���� ���� ���� �޼ҵ�
		smsParser.getParserData(number, inputText3);	//���ŵ� SMS�� �Ľ��ϱ� ���� �޼ҵ�
	}
}