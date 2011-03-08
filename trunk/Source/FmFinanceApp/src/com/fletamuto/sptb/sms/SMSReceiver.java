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
		String parseText[] = new String[4];
		parseText[0] = "[KBüũ]\n" +
							"ȫ�浿��\n" +
							"0*0*ī��\n" +
							"00��00��00:00\n" +
							"<amount>��<installment>\n" +
							"<shop> ���";
		parseText[1] = "�Ｚī��\n" +
							"04/01 16:00\n" +
							"<shop>\n" +
							"<amount>��\n" +
							"<installment>���\n" +
							"�����մϴ�";
		parseText[2] = "����ī���������\n" +
							"ȫ�浿��\n" +
							"00/00 00:00\n" +
							"<amount>��<installment>\n" +
							"<shop>";
		parseText[3] = "�Ե�ī�� ȫ�浿�� <amount>�� <installment> 00/00 00:00 <shop>";
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
		
		//�ӽð�
		int typeId = 0, companyId = 0;
		
		SMSParser smsParser = new SMSParser(context);
		for(int i = 0, size = parseText.length; i < size; i++)
			smsParser.setParserData(String.valueOf(i), parseText[i], typeId, companyId);	//SMS �Ľ��� ���� ���� ���� �޼ҵ� - DB�� ����	Integer.valueOf(msg) �׽�Ʈ��
		smsParser.getParserData(number, inputText[Integer.valueOf(number)]);	//���ŵ� SMS�� �Ľ��ϱ� ���� �޼ҵ�	
	}
}