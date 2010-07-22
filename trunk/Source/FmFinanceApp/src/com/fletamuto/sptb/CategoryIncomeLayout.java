package com.fletamuto.sptb;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CategoryIncomeLayout extends CategoryBaseLayout {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_income);
 
        Button returnInputIncome = (Button)findViewById(R.id.ImageButtonBack);
        returnInputIncome.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(CategoryIncomeLayout.this, InputIncomeLayout.class);
				startActivity(intent);
			}
        });
        
        Button runIncomeCategoryEdit = (Button)findViewById(R.id.ImageButtonEdit);
        runIncomeCategoryEdit.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(CategoryIncomeLayout.this, CategoryIncomeEdit.class);
				startActivity(intent);
			}
        });        
        
        
    }
}
