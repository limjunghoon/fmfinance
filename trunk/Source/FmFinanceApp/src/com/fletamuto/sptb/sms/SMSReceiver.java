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
		if(smsParser.isNumber(number)) {
			try {
				switch(smsParser.getNumberType(number)) {
				case SMSParser.TYPE_NONE:
					break;
				case SMSParser.TYPE_CARD:
					smsParser.getParserData(number, SMSParser.TYPE_CARD, inputText[Integer.valueOf(number)]);	//���ŵ� SMS�� �Ľ��ϱ� ���� �޼ҵ�
					break;
				}

				//TODO �Էµ� �����ͷ� expense_sms�� �����͸� ä��� �޼ҵ� ����
				//set---(new Data(), number, msg);
			} catch (Exception e) {

			}
		}
	}
}