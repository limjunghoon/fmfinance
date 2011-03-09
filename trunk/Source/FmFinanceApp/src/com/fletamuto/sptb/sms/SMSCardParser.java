package com.fletamuto.sptb.sms;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import android.content.Context;
import android.widget.Toast;

import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.PaymentCardMethod;
import com.fletamuto.sptb.data.PaymentMethod;
import com.fletamuto.sptb.data.SMSParseItem;
import com.fletamuto.sptb.db.DBMgr;

public class SMSCardParser {
	static final String TAG_AMOUNT = "<amount>";
	static final String TAG_INSTALLMENT = "<installment>";
	static final String TAG_SHOP = "<shop>";
	
	private Context context;
	private Hashtable<String, SMSParseItem> mHashtable = new Hashtable<String, SMSParseItem>();	//내부에서 DB처럼 사용하기 위해서 임시로 만든 해시테이블
	
	private int dbCount;
	public int getDbCount() {
		return dbCount;
	}

	public SMSCardParser(Context context) {
		this.context = context;
		
		getDBAll();	//인스턴스가 생성 될 때 DB의 내용을 미리 읽어옴
	}
	
	/** DB에 저장해주는 메소드 */
	public SMSParseItem insertDB(SMSParseItem smsParseItem) {
		DBMgr.addSMSParseItem(smsParseItem);
		return reflash(smsParseItem);
	}
	/** DB의 아이템을 업데이트 하는 메소드 */
	public SMSParseItem updateDB(SMSParseItem smsParseItem) {
		DBMgr.updateSMSParseItem(smsParseItem);
		return reflash(smsParseItem);
	}
	/** DB 갱신 내용을 다시 읽어오는 메소드 */
	private SMSParseItem reflash(SMSParseItem smsParseItem) {
		getDBAll();
		return mHashtable.put(smsParseItem.getNumber(), smsParseItem);
	}
	/** DB의 모든 내용을 읽어오는 메소드 */
	private int getDBAll() {
		mHashtable.clear();
		ArrayList<SMSParseItem> items = DBMgr.getSMSParseItems();
		for(int i = 0, size = items.size(); i < size; i++) {
			mHashtable.put(items.get(i).getNumber(), items.get(i));
		}
		dbCount = mHashtable.size();
		return dbCount;
	}
	/** DB에서 읽어오는 메소드 */
	public SMSParseItem getDB(String number) {
		return mHashtable.get(number);
	}
	/** 등록된 번호인지 확인 해주는 메소드 */
	public boolean isNumber(String number) {
		//입력된 번호가 등록된 번호인지 확인하는 메소드
		//메소드는 DB에서 읽어오는 메소드와 동일하고 null인지 아닌지만 알 수 있으면 됨
		return (mHashtable.get(number) != null);
	}
	
	/** SMS을 파싱할 때 사용할 데이터를 파싱하고 저장하는 메소드 */
	public boolean setParserData(String number, String parseText, int typeId, int cardId) {
		SMSParseItem smsParseItem = new SMSParseItem();
		
		StringTokenizer stringTokenizer = new StringTokenizer(parseText, " \n/");
		String text[] = new String[stringTokenizer.countTokens()];
		for(int i = 0, size = text.length; i < size; i++) {
			text[i] = stringTokenizer.nextToken();
			if(text[i].indexOf(TAG_AMOUNT) != -1) {
				smsParseItem.setAmountRow(i);
				int startPosition = text[i].indexOf(TAG_AMOUNT)+TAG_AMOUNT.getBytes().length;
				smsParseItem.setAmountEndText(text[i].substring(startPosition, startPosition+1));
			}
			if(text[i].indexOf(TAG_INSTALLMENT) != -1) {
				smsParseItem.setInstallmentRow(i);
				smsParseItem.setInstallmentEndText(text[i].substring(text[i].indexOf(TAG_INSTALLMENT)+TAG_INSTALLMENT.getBytes().length));
			}
			if(text[i].indexOf(TAG_SHOP) != -1) {
				smsParseItem.setShopRow(i);
				smsParseItem.setShopEndText(text[i].substring(text[i].indexOf(TAG_SHOP)+TAG_SHOP.getBytes().length));
			}
		}
		smsParseItem.setNumber(number);
		smsParseItem.setTypeId(typeId);
		smsParseItem.setCardId(cardId);
		smsParseItem.setParseSource(parseText);
		
		insertDB(smsParseItem);	//SMS 파싱 데이터 저장
		
		/*Toast.makeText(context, "금액 시작 행 번호 : " + smsCardData.getAmountStartRow() + "\n금액 마지막 문자열 : " + smsCardData.getAmountEndText() + "\n" +
								"할부 시작 행 번호 : " + smsCardData.getInstallmentStartRow() + "\n할부 마지막 문자열 : " + smsCardData.getInstallmentEndText() +  "\n" +
								"상호 시작 행 번호 : " + smsCardData.getShopStartRow() + "\n상호 마지막 문자열 : " + smsCardData.getShopEndText(), Toast.LENGTH_LONG).show();*/
		
		return false;
	}

	/** 수신된 전화번호와 메시지로 파싱 데이터를 얻어와서 메시지를 파싱하는 클래스. ExpenseItem 객체를 얻어오는 메소드의 결과를 리턴한다 */
	public ExpenseItem getParserData(String number, String inputText) {	//inputText는 SMS에서 얻은 MessageBody
		SMSParseItem smsParseItem = getDB(number);	//DB에서 읽어오는 메소드
		
		int amountStartRow = smsParseItem.getAmountRow(), installmentStartRow = smsParseItem.getInstallmentRow(), shopStartRow = smsParseItem.getShopRow();
		String amountEndText = smsParseItem.getAmountEndText(), installmentEndText = smsParseItem.getInstallmentEndText(), shopEndText = smsParseItem.getShopEndText();
		
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
		
		return getExpenseData(resultAmount, resultInstallment, resultShopName, smsParseItem);
	}
	
	/** 빈 필드를 채워주는 메소드 */
	private ExpenseItem getExpenseData(long resultAmount, String resultInstallment, String resultShopName, SMSParseItem smsParseItem) {
		ExpenseItem expenseItem = new ExpenseItem();
		CardItem cardItem = DBMgr.getCardItem(smsParseItem.getCardId());
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
	
	/** 입력된 인자에서 숫자만 얻어냄. 숫자가 없을 경우 0 */
	private int getInstallment(String resultInstallment) {
		StringTokenizer stringTokenizer = new StringTokenizer(resultInstallment, "0123456789");
		int size = stringTokenizer.countTokens();
		for(int i = 0; i < size; i++) {
			resultInstallment = resultInstallment.replace(stringTokenizer.nextToken(), "");
		}
		if(resultInstallment.trim().equals(""))
			return 0;
		return Integer.valueOf(resultInstallment);
	}
}