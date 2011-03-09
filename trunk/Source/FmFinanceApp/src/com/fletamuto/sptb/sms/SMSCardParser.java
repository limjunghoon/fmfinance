package com.fletamuto.sptb.sms;

import java.util.Hashtable;
import java.util.StringTokenizer;

import android.content.Context;
import android.widget.Toast;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.PaymentCardMethod;
import com.fletamuto.sptb.data.PaymentMethod;
import com.fletamuto.sptb.db.DBMgr;

public class SMSCardParser {
	static final String TAG_AMOUNT = "<amount>";
	static final String TAG_INSTALLMENT = "<installment>";
	static final String TAG_SHOP = "<shop>";
	
	private Context context;
	
	public SMSCardParser(Context context) {
		this.context = context;
	}

	private Hashtable<String, SMSCardData> hashtable = new Hashtable<String, SMSCardData>();	//DB처럼 사용하기 위해서 임시로 만든 해시테이블

	
	/** DB에 저장해주는 메소드 */
	public SMSCardData insertDB(SMSCardData smsCardData) {	//TODO 구현 안되어 있음
		//insert로 전달할 인자 (smsCardData)
		//
		return hashtable.put(smsCardData.getNumber(), smsCardData);
	}
	/** DB에서 읽어오는 메소드 */
	public SMSCardData getDB(String number) {	//TODO 구현 안되어 있음
		return hashtable.get(number);
	}
	/** 등록된 번호인지 확인 해주는 메소드 */
	public boolean isNumber(String number) {
		//입력된 번호가 등록된 번호인지 확인하는 메소드
		//메소드는 DB에서 읽어오는 메소드와 동일하고 null인지 아닌지만 알 수 있으면 됨
		return (hashtable.get(number) != null);
	}
	
	public boolean setParserData(String number, String parseText, int typeId, int cardId) {
		SMSCardData smsCardData = new SMSCardData();
		
		StringTokenizer stringTokenizer = new StringTokenizer(parseText, " \n/");
		String text[] = new String[stringTokenizer.countTokens()];
		for(int i = 0, size = text.length; i < size; i++) {
			text[i] = stringTokenizer.nextToken();
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
		smsCardData.setNumber(number);
		smsCardData.setTypeId(typeId);
		smsCardData.setCardId(cardId);
		smsCardData.setParseSource(parseText);
		
		// TODO 위에서 얻은 데이터를 Set 하는 메소드를 추가 해야함
		insertDB(smsCardData);
		
		
		/*Toast.makeText(context, "금액 시작 행 번호 : " + smsCardData.getAmountStartRow() + "\n금액 마지막 문자열 : " + smsCardData.getAmountEndText() + "\n" +
								"할부 시작 행 번호 : " + smsCardData.getInstallmentStartRow() + "\n할부 마지막 문자열 : " + smsCardData.getInstallmentEndText() +  "\n" +
								"상호 시작 행 번호 : " + smsCardData.getShopStartRow() + "\n상호 마지막 문자열 : " + smsCardData.getShopEndText(), Toast.LENGTH_LONG).show();*/
		
		return false;
	}

	public ExpenseItem getParserData(String number, String inputText) {	//inputText는 SMS에서 얻은 MessageBody
		SMSCardData smsCardData = getDB(number);	//DB에서 읽어오는 메소드
		
		int amountStartRow = smsCardData.getAmountStartRow(), installmentStartRow = smsCardData.getInstallmentStartRow(), shopStartRow = smsCardData.getShopStartRow();
		String amountEndText = smsCardData.getAmountEndText(), installmentEndText = smsCardData.getInstallmentEndText(), shopEndText = smsCardData.getShopEndText();
		
		long resultAmount = 0;
		String resultInstallment = "";
		String resultShopName = "";
		
		StringTokenizer stringTokenizer = new StringTokenizer(inputText, " \n/");
		String[] resultText = new String[stringTokenizer.countTokens()];
		for(int i = 0, size = resultText.length; i < size; i++)
			resultText[i] = stringTokenizer.nextToken();
		
		resultAmount = Long.valueOf(resultText[amountStartRow].substring(0, resultText[amountStartRow].indexOf(amountEndText)).replace(",", ""));
		if(amountStartRow == installmentStartRow) {
			//금액과 할부가 같은 토큰에 나오는 경우에는 금액이 먼저 나옴
			resultInstallment = resultText[installmentStartRow].substring(resultText[amountStartRow].indexOf(amountEndText)+1).trim();
		} else {
			if(!installmentEndText.trim().equals(""))
				resultInstallment = resultText[installmentStartRow].substring(0, resultText[installmentStartRow].indexOf(installmentEndText)).trim();
			else
				resultInstallment = resultText[installmentStartRow].trim();
		}
		if(!shopEndText.trim().equals(""))
			resultShopName = resultText[shopStartRow].substring(0, resultText[shopStartRow].indexOf(shopEndText)).trim();
		else
			resultShopName = resultText[shopStartRow];

		Toast.makeText(context, "금액 : " + resultAmount + "\n할부기간 : " + resultInstallment + "\n상호 : " + resultShopName, Toast.LENGTH_LONG).show();
		
		return getExpenseData(resultAmount, resultInstallment, resultShopName, smsCardData);
	}
	
	private ExpenseItem getExpenseData(long resultAmount, String resultInstallment, String resultShopName, SMSCardData smsCardData) {
		ExpenseItem expenseItem = new ExpenseItem();
		CardItem cardItem = DBMgr.getCardItem(smsCardData.getCardId());
		expenseItem.setCard(cardItem);
		expenseItem.setAmount(resultAmount);
		expenseItem.setMemo(resultShopName);
		
		PaymentCardMethod paymentCardMethod = new PaymentCardMethod();
		paymentCardMethod.setCard(cardItem);
		paymentCardMethod.setInstallmentPlan(getInstallment(resultInstallment));
		paymentCardMethod.setType(PaymentMethod.CARD);
		
		expenseItem.setPaymentMethod(paymentCardMethod);
		
		return expenseItem;
	}
	
	private int getInstallment(String resultInstallment) {
		// TODO Auto-generated method stub
		return 0;
	}
}