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
	private Hashtable<String, SMSParseItem> mHashtable = new Hashtable<String, SMSParseItem>();	//���ο��� DBó�� ����ϱ� ���ؼ� �ӽ÷� ���� �ؽ����̺�
	
	private int dbCount;
	public int getDbCount() {
		return dbCount;
	}

	public SMSCardParser(Context context) {
		this.context = context;
		
		getDBAll();	//�ν��Ͻ��� ���� �� �� DB�� ������ �̸� �о��
	}
	
	/** DB�� �������ִ� �޼ҵ� */
	public SMSParseItem insertDB(SMSParseItem smsParseItem) {
		DBMgr.addSMSParseItem(smsParseItem);
		return reflash(smsParseItem);
	}
	/** DB�� �������� ������Ʈ �ϴ� �޼ҵ� */
	public SMSParseItem updateDB(SMSParseItem smsParseItem) {
		DBMgr.updateSMSParseItem(smsParseItem);
		return reflash(smsParseItem);
	}
	/** DB ���� ������ �ٽ� �о���� �޼ҵ� */
	private SMSParseItem reflash(SMSParseItem smsParseItem) {
		getDBAll();
		return mHashtable.put(smsParseItem.getNumber(), smsParseItem);
	}
	/** DB�� ��� ������ �о���� �޼ҵ� */
	private int getDBAll() {
		mHashtable.clear();
		ArrayList<SMSParseItem> items = DBMgr.getSMSParseItems();
		for(int i = 0, size = items.size(); i < size; i++) {
			mHashtable.put(items.get(i).getNumber(), items.get(i));
		}
		dbCount = mHashtable.size();
		return dbCount;
	}
	/** DB���� �о���� �޼ҵ� */
	public SMSParseItem getDB(String number) {
		return mHashtable.get(number);
	}
	/** ��ϵ� ��ȣ���� Ȯ�� ���ִ� �޼ҵ� */
	public boolean isNumber(String number) {
		//�Էµ� ��ȣ�� ��ϵ� ��ȣ���� Ȯ���ϴ� �޼ҵ�
		//�޼ҵ�� DB���� �о���� �޼ҵ�� �����ϰ� null���� �ƴ����� �� �� ������ ��
		return (mHashtable.get(number) != null);
	}
	
	/** SMS�� �Ľ��� �� ����� �����͸� �Ľ��ϰ� �����ϴ� �޼ҵ� */
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
		
		insertDB(smsParseItem);	//SMS �Ľ� ������ ����
		
		/*Toast.makeText(context, "�ݾ� ���� �� ��ȣ : " + smsCardData.getAmountStartRow() + "\n�ݾ� ������ ���ڿ� : " + smsCardData.getAmountEndText() + "\n" +
								"�Һ� ���� �� ��ȣ : " + smsCardData.getInstallmentStartRow() + "\n�Һ� ������ ���ڿ� : " + smsCardData.getInstallmentEndText() +  "\n" +
								"��ȣ ���� �� ��ȣ : " + smsCardData.getShopStartRow() + "\n��ȣ ������ ���ڿ� : " + smsCardData.getShopEndText(), Toast.LENGTH_LONG).show();*/
		
		return false;
	}

	/** ���ŵ� ��ȭ��ȣ�� �޽����� �Ľ� �����͸� ���ͼ� �޽����� �Ľ��ϴ� Ŭ����. ExpenseItem ��ü�� ������ �޼ҵ��� ����� �����Ѵ� */
	public ExpenseItem getParserData(String number, String inputText) {	//inputText�� SMS���� ���� MessageBody
		SMSParseItem smsParseItem = getDB(number);	//DB���� �о���� �޼ҵ�
		
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
			//�ݾװ� �Һΰ� ���� ��ū�� ������ ��쿡�� �ݾ��� ���� ����
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

		Toast.makeText(context, "�ݾ� : " + resultAmount + "\n�ҺαⰣ : " + resultInstallment + "\n��ȣ : " + resultShopName, Toast.LENGTH_LONG).show();
		
		return getExpenseData(resultAmount, resultInstallment, resultShopName, smsParseItem);
	}
	
	/** �� �ʵ带 ä���ִ� �޼ҵ� */
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
	
	/** �Էµ� ���ڿ��� ���ڸ� ��. ���ڰ� ���� ��� 0 */
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