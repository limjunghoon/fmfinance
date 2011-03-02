/**
 * ������ ���ͼ� ���Ϸ� ����ϴ� �κ�
 * ���� ��ο� ��Ű�� �̸��� ���;� �ϱ� ������ Context ��ü�� �Ѱ� �־�� �� - ���� ���ڿ��� �Է��ϵ��� �����ϸ� ���ֵ� ����
 * 
 * �� ��� �κ��� ������ �޼ҵ�� �и��ؾ� ������ �׽�Ʈ�� ������ �и��� �ȵǾ� �ִ� ����
 * 
 * �ٸ� Ŭ�������� ȣ�� �� ��쿡�� POITest ��Ƽ��Ƽ Ŭ������ ��ġ�� �ʰ� �� Ŭ������ ����ϸ� ��
 * ����� �ϼ��Ǹ� ó���ϴµ� ���� ���� �ð��� �Ҹ�ǹǷ� ������ ó���� �����ؾ� ��
 */

package com.fletamuto.sptb.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

@SuppressWarnings("unused")
public class POIOutPut {
	static final String XLS_OUTPUT_DB_DIRECTORY = "/backup/";
	private Context mContext;
	
	HSSFWorkbook mWorkbook;
	HSSFSheet mHssfSheet;
	HSSFRow mHssfRow;
	
	CellStyle defaultStyle;
	
	public POIOutPut(Context context) {
		super();
		this.mContext = context;
	}
	
	protected boolean poiOutput() {
		try {
			makeWorkbook();
			makeSheet("�׽�Ʈ�� ���");
			return makeExpenseDataSheet();
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean makeExpenseDataSheet() {
		makeHead(new String[]{ExpenseData.STRING_DATE,
								ExpenseData.STRING_CATEGORY,
								ExpenseData.STRING_AMOUNT,
								ExpenseData.STRING_PAY_METHOD,
								ExpenseData.STRING_PAY_MONNEY_BALANCE,
								ExpenseData.STRING_PAY_ACCOUNT,
								ExpenseData.STRING_PAY_ACCOUNT_BALANCE,
								ExpenseData.STRING_PAY_BALANCE,
								ExpenseData.STRING_MEMO,
								ExpenseData.STRING_TAG,
								ExpenseData.STRING_REPEAT});
		ArrayList<ExpenseData> expenseDatas =  new GetExpenseDatas().getExpenseDatas();
		for(int i = 0, size = expenseDatas.size(); i < size; i++) {
			makeRow();
			makeCells(expenseDatas.get(i));
		}
		
		try {
			if(savePOI()) {
				Log.e("POI Export", "Successed");
				return true;
			} else {
				Log.e("POI Export", "Failed");
				return false;
			}
		} catch (Exception e) {
			Log.e("POI Export", "Error");
			return false;
		}
	}

	/**
	 * ��ũ���� �����
	 * @return
	 * <p><b>true</b><br/>��ũ���� �������</p>
	 * <p><b>false</b><br/>��ũ���� ��������� ����</p>
	 */
	private boolean makeWorkbook() {
		mWorkbook = new HSSFWorkbook();
		defaultStyle = mWorkbook.createCellStyle();
		defaultStyle.setAlignment(CellStyle.ALIGN_LEFT);
		return (mWorkbook != null);
	}
	/**
	 * ��Ʈ�� �����.<br/>
	 * ��Ʈ�� ����� ���ÿ� 0�� ���� �ڵ����� ����Ƿ� ù ��° ���� ������ �ʰ� �����ص� ��
	 * @return
	 * <p><b>true</b><br/>��Ʈ�� �������</p>
	 * <p><b>false</b><br/>��Ʈ�� ��������� ����</p>
	 */
	private boolean makeSheet(String title) {
		if(mWorkbook == null)
			return false;
		else {
			mHssfSheet = mWorkbook.createSheet(title);
			mHssfRow = mHssfSheet.createRow(0);
			return (mHssfSheet != null);
		}
	}
	/**
	 * ���� �����
	 * @return
	 * <p><b>true</b><br/>���� �������</p>
	 * <p><b>false</b><br/>���� ��������� ����</p>
	 */
	private boolean makeRow() {
		if(mWorkbook == null || mHssfSheet == null)
			return false;
		else {
			mHssfRow = mHssfSheet.createRow(mHssfSheet.getLastRowNum()+1);
			return (mHssfRow != null);
		}
	}
	/**
	 * ������ ���� �����
	 * @param
	 * @return
	 * <p><b>true</b><br/>���� �������</p>
	 * <p><b>false</b><br/>���� ��������� ����</p>
	 */
	private boolean makeRows(int rows) {
		if(mWorkbook == null || mHssfSheet == null)
			return false;
		else {
			mHssfRow = mHssfSheet.createRow(mHssfSheet.getLastRowNum() + rows);
			return (mHssfRow != null);
		}
	}
	
	/**
	 * ������ ���� �����
	 */
	private boolean makeCells(ExpenseData data) {
		try {
			makeCell(0, data.getDate());
			makeCell(1, data.getCategory());
			makeCell(2, data.getAmount());
			makeCell(3, data.getPayMethod());
			makeCell(4, data.getMonneyBalance());
			makeCell(5, data.getAccount());
			makeCell(6, data.getAccountBalance());
			makeCell(7, data.getBalance());
			makeCell(8, data.getMemo());
			makeCell(9, data.getTag());
			makeCell(10, data.getRepeat());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * ǥ�� �׸��� �����
	 */
	private boolean makeHead(String[] heads) {
		try {
			for(int i = 0, size = heads.length; i < size; i++)
				makeCell(i, heads[i]);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean makeCell(int cellIndex, String value) {
		return makeCell(cellIndex, value, defaultStyle);
	}
	private boolean makeCell(int cellIndex, String value, CellStyle style) {
		if(mWorkbook == null || mHssfSheet == null || mHssfRow == null)
			return false;
		else {
			mHssfRow.createCell(cellIndex, HSSFCell.CELL_TYPE_STRING).setCellValue(value);
			mHssfRow.getCell(cellIndex).setCellStyle(style);
			return true;
		}
	}
	
	private boolean makeCell(int cellIndex, double value) {
		return makeCell(cellIndex, value, defaultStyle);
	}
	private boolean makeCell(int cellIndex, double value, CellStyle style) {
		if(mWorkbook == null || mHssfSheet == null || mHssfRow == null)
			return false;
		else {
			mHssfRow.createCell(cellIndex, HSSFCell.CELL_TYPE_NUMERIC).setCellValue(value);
			mHssfRow.getCell(cellIndex).setCellStyle(style);
			return true;
		}
	}
	
	private boolean makeCell(int cellIndex, boolean value) {
		return makeCell(cellIndex, value, defaultStyle);
	}
	private boolean makeCell(int cellIndex, boolean value, CellStyle style) {
		if(mWorkbook == null || mHssfSheet == null || mHssfRow == null)
			return false;
		else {
			mHssfRow.createCell(cellIndex, HSSFCell.CELL_TYPE_BOOLEAN).setCellValue(value);
			mHssfRow.getCell(cellIndex).setCellStyle(style);
			return true;
		}
	}
	
	private boolean makeCell(int cellIndex, Date value) {
		return makeCell(cellIndex, value, defaultStyle);
	}
	private boolean makeCell(int cellIndex, Date value, CellStyle style) {
		if(mWorkbook == null || mHssfSheet == null || mHssfRow == null)
			return false;
		else {
			mHssfRow.createCell(cellIndex, HSSFCell.CELL_TYPE_STRING).setCellValue(value.toLocaleString());
			mHssfRow.getCell(cellIndex).setCellStyle(style);
			return true;
		}
	}
	
	private boolean makeCell(int cellIndex, Calendar value) {
		return makeCell(cellIndex, value, defaultStyle);
	}
	private boolean makeCell(int cellIndex, Calendar value, CellStyle style) {
		if(mWorkbook == null || mHssfSheet == null || mHssfRow == null)
			return false;
		else {
			mHssfRow.createCell(cellIndex, HSSFCell.CELL_TYPE_STRING).setCellValue(value.getTime().toLocaleString());
			mHssfRow.getCell(cellIndex).setCellStyle(style);
			return true;
		}
	}
	
	private boolean makeBlankCell(int cellIndex) {
		return makeBlankCell(cellIndex, defaultStyle);
	}
	private boolean makeBlankCell(int cellIndex, CellStyle style) {
		if(mWorkbook == null || mHssfSheet == null || mHssfRow == null)
			return false;
		else {
			mHssfRow.createCell(cellIndex, HSSFCell.CELL_TYPE_BLANK).setCellValue("");
			mHssfRow.getCell(cellIndex).setCellStyle(style);
			return true;
		}
	}
	
	private boolean makeErrorCell(int cellIndex) {
		return makeErrorCell(cellIndex, defaultStyle);
	}
	private boolean makeErrorCell(int cellIndex, CellStyle style) {
		if(mWorkbook == null || mHssfSheet == null || mHssfRow == null)
			return false;
		else {
			mHssfRow.createCell(cellIndex, HSSFCell.CELL_TYPE_ERROR).setCellErrorValue((byte)0);
			mHssfRow.getCell(cellIndex).setCellStyle(style);
			return true;
		}
	}
	
	private boolean makeFormulaCell(int cellIndex, String value) {
		return makeFormulaCell(cellIndex, value, defaultStyle);
	}
	private boolean makeFormulaCell(int cellIndex, String value, CellStyle style) {
		if(mWorkbook == null || mHssfSheet == null || mHssfRow == null)
			return false;
		else {
			mHssfRow.createCell(cellIndex, HSSFCell.CELL_TYPE_FORMULA).setCellFormula(value);
			mHssfRow.getCell(cellIndex).setCellStyle(style);
			return true;
		}
	}

	/**
	 * ������� ��ũ���� ���Ϸ� ���
	 * @return
	 * <b>boolean</b> ���� ����Ⱑ ���������� �̷�� �������� ���� ���. true�� ��� ����.
	 * @throws Exception
	 */
	protected boolean savePOI() throws Exception {
		if(!(new DBOutput(mContext)).packageMkDir())
			return false;
		File directory = new File(DBOutput.DB_OUTPUT_PACKAGE_PATH + XLS_OUTPUT_DB_DIRECTORY);
		if(!directory.isDirectory())
			directory.mkdir();
		Date date = new Date();
		String path =  directory.getPath() + "/backup_" + new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss").format(date) + ".xls";
		File file = new File(path);
		FileOutputStream out = new FileOutputStream(file);
		mWorkbook.write(out);
		out.close();
		
		return file.isFile();
	}
}