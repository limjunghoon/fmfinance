package com.fletamuto.sptb.sms;

import java.util.Hashtable;

import android.content.Context;
import android.widget.Toast;

public class SMSParser {
	static final String TAG_AMOUNT = "<amount>";
	static final String TAG_INSTALLMENT = "<installment>";
	static final String TAG_SHOP = "<shop>";
	
	private Context context;
	
	public SMSParser(Context context) {
		this.context = context;
	}

	private Hashtable<String, SMSCardData> hashtable = new Hashtable<String, SMSCardData>();	//DBó�� ����ϱ� ���ؼ� �ӽ÷� ���� �ؽ����̺�
	private SMSCardData smsCardData = new SMSCardData();
	
	public boolean setParserData(String number, String parseText) {
		String text[] = parseText.split("\n");
		for(int i = 0, size = text.length; i < size; i++) {
			if(text[i].indexOf(TAG_AMOUNT) != -1) {
				smsCardData.setAmountStartRow(i);
				int startPosition = text[i].indexOf(TAG_AMOUNT)+TAG_AMOUNT.getBytes().length;
				smsCardData.setAmountEndText(text[i].substring(startPosition, startPosition+1));
			}
			if(text[i].indexOf(TAG_INSTALLMENT) != -1) {
				smsCardData.setInstallmentStartRow(i);
				smsCardData.setInstallmentEndText(text[i].substring(text[i].indexOf(TAG_INSTALLMENT)+TAG_INSTALLMENT.getBytes().length));
			}
			if(text[i].indexOf(TAG_SHOP) != -1) {
				smsCardData.setShopStartRow(i);
				smsCardData.setShopEndText(text[i].substring(text[i].indexOf(TAG_SHOP)+TAG_SHOP.getBytes().length));
			}
		}
		
		// TODO ������ ���� �����͸� Set �ϴ� �޼ҵ带 �߰� �ؾ���
		insertDB(number, smsCardData);
		
		
		Toast.makeText(context, "�ݾ� ���� �� ��ȣ : " + smsCardData.getAmountStartRow() + "\n�ݾ� ������ ���ڿ� : " + smsCardData.getAmountEndText() + "\n" +
								"�Һ� ���� �� ��ȣ : " + smsCardData.getInstallmentStartRow() + "\n�Һ� ������ ���ڿ� : " + smsCardData.getInstallmentEndText() +  "\n" +
								"��ȣ ���� �� ��ȣ : " + smsCardData.getShopStartRow() + "\n��ȣ ������ ���ڿ� : " + smsCardData.getShopEndText(), Toast.LENGTH_LONG).show();
		
		return false;
	}

	/** DB�� �������ִ� �޼ҵ� */
	private SMSCardData insertDB(String number, SMSCardData smsCardData) {	//TODO ���� �ȵǾ� ����
		return hashtable.put(number, smsCardData);
	}
	/** DB���� �о���� �޼ҵ� */
	private SMSCardData getDB(String number) {	//TODO ���� �ȵǾ� ����
		return hashtable.get(number);
	}

	public boolean getParserData(String number, String inputText) {	//inputText�� SMS���� ���� MessageBody
		SMSCardData smsCardData = getDB(number);	//DB���� �о���� �޼ҵ�
		
		int amountStartRow = smsCardData.getAmountStartRow(), installmentStartRow = smsCardData.getInstallmentStartRow(), shopStartRow = smsCardData.getShopStartRow();
		String amountEndText = smsCardData.getAmountEndText(), installmentEndText = smsCardData.getInstallmentEndText(), shopEndText = smsCardData.getShopEndText();
		
		long resultAmount = 0;
		String resultInstallment = "�Ͻú�";
		String resultShopName = "";
		
		String[] resultText = inputText.split("\n");
		resultAmount = Long.valueOf(resultText[amountStartRow].substring(0, resultText[amountStartRow].indexOf(amountEndText)).replace(",", ""));
		if(amountStartRow == installmentStartRow) {
			resultInstallment = resultText[installmentStartRow].substring(resultText[amountStartRow].indexOf(amountEndText)+1).trim();
		} else {
			resultInstallment = resultText[installmentStartRow].substring(0, resultText[installmentStartRow].indexOf(installmentEndText)).trim();
		}
		if(!shopEndText.equals(""))
			resultShopName = resultText[shopStartRow].substring(0, resultText[shopStartRow].indexOf(shopEndText)).trim();
		else
			resultShopName = resultText[shopStartRow];

		Toast.makeText(context, "�ݾ� : " + resultAmount + "\n�ҺαⰣ : " + resultInstallment + "\n��ȣ : " + resultShopName, Toast.LENGTH_LONG).show();
		
		return false;
	}
}