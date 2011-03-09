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
		
		
		//�Ľ� �׽�Ʈ��
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
		
		if(smsCardParser.getDbCount() == 0) {
			//�ӽð�
			int typeId = 1, cardId = 1;
			//DB��� ����� �� �Է� �κ�
			for(int i = 0, size = parseText.length; i < size; i++)
				smsCardParser.setParserData(String.valueOf(i), parseText[i], typeId, cardId);	//SMS �Ľ��� ���� ���� ���� �޼ҵ� - DB�� ����	Integer.valueOf(msg) �׽�Ʈ��
		}
	}

	/** ��ϵ� ��ȣ���� Ȯ�� ���ִ� �޼ҵ� */
	public boolean isNumber(String number) {
		return smsCardParser.isNumber(number);
	}
	
	/** �Է��� ��ȣ�� ��� Ÿ������ */
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
