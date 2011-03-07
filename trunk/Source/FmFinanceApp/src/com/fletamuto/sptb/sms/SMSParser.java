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

	private Hashtable<String, SMSCardData> hashtable = new Hashtable<String, SMSCardData>();	//DB처럼 사용하기 위해서 임시로 만든 해시테이블
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
		
		// TODO 위에서 얻은 데이터를 Set 하는 메소드를 추가 해야함
		insertDB(number, smsCardData);
		
		
		Toast.makeText(context, "금액 시작 행 번호 : " + smsCardData.getAmountStartRow() + "\n금액 마지막 문자열 : " + smsCardData.getAmountEndText() + "\n" +
								"할부 시작 행 번호 : " + smsCardData.getInstallmentStartRow() + "\n할부 마지막 문자열 : " + smsCardData.getInstallmentEndText() +  "\n" +
								"상호 시작 행 번호 : " + smsCardData.getShopStartRow() + "\n상호 마지막 문자열 : " + smsCardData.getShopEndText(), Toast.LENGTH_LONG).show();
		
		return false;
	}

	/** DB에 저장해주는 메소드 */
	private SMSCardData insertDB(String number, SMSCardData smsCardData) {	//TODO 구현 안되어 있음
		return hashtable.put(number, smsCardData);
	}
	/** DB에서 읽어오는 메소드 */
	private SMSCardData getDB(String number) {	//TODO 구현 안되어 있음
		return hashtable.get(number);
	}

	public boolean getParserData(String number, String inputText) {	//inputText는 SMS에서 얻은 MessageBody
		SMSCardData smsCardData = getDB(number);	//DB에서 읽어오는 메소드
		
		int amountStartRow = smsCardData.getAmountStartRow(), installmentStartRow = smsCardData.getInstallmentStartRow(), shopStartRow = smsCardData.getShopStartRow();
		String amountEndText = smsCardData.getAmountEndText(), installmentEndText = smsCardData.getInstallmentEndText(), shopEndText = smsCardData.getShopEndText();
		
		long resultAmount = 0;
		String resultInstallment = "일시불";
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

		Toast.makeText(context, "금액 : " + resultAmount + "\n할부기간 : " + resultInstallment + "\n상호 : " + resultShopName, Toast.LENGTH_LONG).show();
		
		return false;
	}
}