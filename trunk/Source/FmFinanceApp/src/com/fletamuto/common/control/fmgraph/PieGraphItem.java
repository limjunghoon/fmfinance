package com.fletamuto.common.control.fmgraph;

public class PieGraphItem {
	
	public long itemValue;
	public float itemAngle;
	public int itemColor;
	public int itemIDX;
	
	
	public void setItemValue (long item_Value) {
		itemValue = item_Value;
	}
	public void setItemAngle (float item_Angle) {
		itemAngle = item_Angle;
	}
	public void setItemColor (int item_Color) {
		itemColor = item_Color;
	}
	public void setItemIDX (int item_Index) {
		itemIDX = item_Index;
	}
	
		
	public long getItemValue() {
		return itemValue;
	}
	public float getItemAngle() {
		return itemAngle;
	}
	public int getItemColor () {
		return itemColor;
	}
	public int getItemIDX() {
		return itemIDX;
	}
}
