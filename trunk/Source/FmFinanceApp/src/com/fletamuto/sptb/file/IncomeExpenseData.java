package com.fletamuto.sptb.file;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import com.fletamuto.sptb.data.AccountItem;
import com.fletamuto.sptb.data.CardItem;
import com.fletamuto.sptb.data.ExpenseItem;
import com.fletamuto.sptb.data.FinanceItem;
import com.fletamuto.sptb.db.DBMgr;

public class IncomeExpenseData {
	public static final String STRING_NAME = "����-����";
	public static final String STRING_DATE = "�⵵/��/��";
	public static final String STRING_TYPE = "����";
	public static final String STRING_CATEGORY = "�з�";
	public static final String STRING_AMOUNT = "�ݾ�";
	public static final String STRING_PAY_METHOD = "�������";
	//public static final String STRING_PAY_MONNEY_BALANCE = "�ܾ�";
	public static final String STRING_PAY_ACCOUNT = "��������";
	//public static final String STRING_PAY_ACCOUNT_BALANCE = "�����ܾ�";
	//public static final String STRING_PAY_BALANCE = "�����ܾ�";
	public static final String STRING_MEMO = "�޸�";
	public static final String STRING_TAG = "�±�";
	public static final String STRING_REPEAT = "�ݺ�";

	private Calendar date;
	/** ture�� ��� ����, false�� ��� ���� */
	private boolean type;
	private String category;
	private long amount;
	private String payMethod;
	//private long monneyBalance;
	private String account;
	//private long accountBalance;
	//private long balance;
	private String memo;
	private String tag;
	private String repeat;


	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
	public boolean getType() {
		return type;
	}
	public void setType(boolean type) {
		this.type = type;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
//	public long getMonneyBalance() {
//		return monneyBalance;
//	}
//	public void setMonneyBalance(long monneyBalance) {
//		this.monneyBalance = monneyBalance;
//	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
//	public long getAccountBalance() {
//		return accountBalance;
//	}
//	public void setAccountBalance(long accountBalance) {
//		this.accountBalance = accountBalance;
//	}
//	public long getBalance() {
//		return balance;
//	}
//	public void setBalance(long balance) {
//		this.balance = balance;
//	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getRepeat() {
		return repeat;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	
	public ArrayList<IncomeExpenseData> getIncomeExpenseDatas() {
		return getIncomeExpenseDatas(Calendar.getInstance(Locale.KOREA));
	}
	public ArrayList<IncomeExpenseData> getIncomeExpenseDatas(Calendar calendar) {
		ArrayList<IncomeExpenseData> incomeExpenseDatas = new GetIncomeExpenseDatas().getIncomeExpenseDatas(calendar);
		Collections.sort(incomeExpenseDatas, new TypeSort());
		Collections.sort(incomeExpenseDatas, new DateSort());
		return incomeExpenseDatas;
	}
	
	/** ��¥�� ������ ���� �� ���  */
	public static final DateSort getDateSortComparator() {
		return new DateSort();
	}
	private static final class DateSort implements Comparator<IncomeExpenseData> {
		public final int compare(IncomeExpenseData data1, IncomeExpenseData data2) {
			return data1.getDate().compareTo(data2.getDate());
		}
	}
	/** ���к� ������ ���� �� ���  */
	public static final TypeSort getTypeSortComparator() {
		return new TypeSort();
	}
	private static final class TypeSort implements Comparator<IncomeExpenseData> {
		public final int compare(IncomeExpenseData data1, IncomeExpenseData data2) {
			return (data1.getType() == data2.getType()) ? 1 : 0;
		}
	}
	
	/** �����͸� ������ ���� ���� Ŭ���� */
	private class GetIncomeExpenseDatas {
		private ArrayList<IncomeExpenseData> incomeExpendDatas;
		protected ArrayList<IncomeExpenseData> getIncomeExpenseDatas(Calendar calendar) {
			incomeExpendDatas = new ArrayList<IncomeExpenseData>();
			
			getIncomeDatas(calendar);
			getExpenseDatas(calendar);

			return incomeExpendDatas;
		}
		/** ���� �׸��� ����
		 *@param
		 *@return	ArrayList&lt;IncomeExpenseData&gt;
		 */
		private ArrayList<IncomeExpenseData> getIncomeDatas(Calendar calendar) {
			ArrayList<FinanceItem> financeMyPocketItems = DBMgr.getIncomeItemFromMypocket(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH)+1);

			for(int i = 0, size = financeMyPocketItems.size(); i < size; i++) {
				IncomeExpenseData data = new IncomeExpenseData();
				data.setDate(financeMyPocketItems.get(i).getCreateDate());
				data.setType(true);	//����
				data.setCategory(financeMyPocketItems.get(i).getCategory().getName());
				data.setAmount(financeMyPocketItems.get(i).getAmount());
				data.setPayMethod("����");
				//data.setMonneyBalance(0);
				data.setAccount("");
				//data.setAccountBalance(0);
				//data.setBalance(0);
				data.setMemo(financeMyPocketItems.get(i).getMemo());
				//data.setTag(financeMyPocketItems.get(i).getTag());
				data.setRepeat(financeMyPocketItems.get(i).getRepeat().getRepeatMessage());
				
				incomeExpendDatas.add(data);
			}
			return incomeExpendDatas;
		}
		/** ���� �׸��� ����
		 *@param
		 *@return	ArrayList&lt;IncomeExpenseData&gt;
		 */
		private ArrayList<IncomeExpenseData> getExpenseDatas(Calendar calendar) {
			ArrayList<FinanceItem> financeMyPocketItems = DBMgr.getExpenseItemFromMypocket(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH)+1);
			//AccountItem accountItem = DBMgr.getAccountMyPocket();

			for(int i = 0, size = financeMyPocketItems.size(); i < size; i++) {
				IncomeExpenseData data = new IncomeExpenseData();
				data.setDate(financeMyPocketItems.get(i).getCreateDate());
				data.setType(false);	//����
				data.setCategory(financeMyPocketItems.get(i).getCategory().getName() + " - " + financeMyPocketItems.get(i).getSubCategory().getName());
				data.setAmount(financeMyPocketItems.get(i).getAmount());
				data.setPayMethod("����");
				//data.setMonneyBalance(accountItem.getBalance());
				//data.setAccount("");
				//data.setAccountBalance(0);
				//data.setBalance(0);
				data.setMemo(financeMyPocketItems.get(i).getMemo());
				//data.setTag(financeMyPocketItems.get(i).getTag());
				data.setRepeat(financeMyPocketItems.get(i).getRepeat().getRepeatMessage());
				
				incomeExpendDatas.add(data);
			}
			
			getCardExpenseDatas(calendar);
			return incomeExpendDatas;
		}
		/**
		 * ī�� ���� �׸��� ����
		 * @param
		 * @return	ArrayList&lt;IncomeExpenseData&gt;
		 */
		private ArrayList<IncomeExpenseData> getCardExpenseDatas(Calendar calendar) {
			ArrayList<CardItem> cardItems = DBMgr.getCardItems();
			for(int i = 0, size = cardItems.size(); i < size; i++) {
				getCardExpenseDatas(calendar, cardItems.get(i).getID());
			}
			return incomeExpendDatas;
		}
		private ArrayList<IncomeExpenseData> getCardExpenseDatas(Calendar calendar, int cardId) {
			ArrayList<FinanceItem> financeCardItems = DBMgr.getCardExpenseItems(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH)+1, cardId);
			CardItem cardItem = DBMgr.getCardItem(cardId);
			AccountItem accountItem = DBMgr.getAccountItem(cardItem.getAccount().getID());
			
			for(int i = 0, size = financeCardItems.size(); i < size; i++) {
				IncomeExpenseData data = new IncomeExpenseData();
				data.setDate(financeCardItems.get(i).getCreateDate());
				data.setType(false);	//����
				data.setCategory(financeCardItems.get(i).getCategory().getName() + " - " + financeCardItems.get(i).getSubCategory().getName());
				data.setAmount(financeCardItems.get(i).getAmount());
				data.setPayMethod(cardItem.getCardTypeName());
				//data.setMonneyBalance(0);
				data.setAccount(accountItem.getCompany().getName() + " - " + accountItem.getNumber());
				//data.setAccountBalance(cardItem.getAccount().getBalance());
				//data.setBalance(cardItem.getBalance());
				data.setMemo(financeCardItems.get(i).getMemo());
				//data.setTag(financeCardItems.get(i).getTag());
				data.setRepeat(financeCardItems.get(i).getRepeat().getRepeatMessage());
				
				incomeExpendDatas.add(data);
			}
			return incomeExpendDatas;
		}
	}
}