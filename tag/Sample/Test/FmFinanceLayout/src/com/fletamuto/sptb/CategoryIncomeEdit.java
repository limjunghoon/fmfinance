package com.fletamuto.sptb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CategoryIncomeEdit extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_income_edit);
 
        
        /*Fletamuto wskim 100721 ���� �з�(category)Edit back ��ư�� ������*/
        Button returnInputIncomeCategory = (Button)findViewById(R.id.ImageButtonBack);
        returnInputIncomeCategory.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(CategoryIncomeEdit.this, CategoryIncomeLayout.class);
				startActivity(intent);
			}
        });
        
        /*Fletamuto wskim 100721 ���� �з�(category)Edit save ��ư�� ������*/
        Button saveIncomeCategoryEdit = (Button)findViewById(R.id.ImageButtonSave);
        saveIncomeCategoryEdit.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(CategoryIncomeEdit.this, CategoryIncomeLayout.class);
				startActivity(intent);
			}
        });        
        
        
    }
}
