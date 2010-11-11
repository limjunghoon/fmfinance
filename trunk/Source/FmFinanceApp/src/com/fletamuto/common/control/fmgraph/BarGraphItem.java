package com.fletamuto.common.control.fmgraph;

public class BarGraphItem {
	public long itemValue;
	public String itemAxisTitle;
	public int itemColor;
	public int itemIDX;
	public int itemHeight;
	public int itemWidth;
	
	
	
	public void setItemValue (long item_Value) {
		itemValue = item_Value;
	}
	public void setItemAxisTitle (String item_Axis_Titles) {
		itemAxisTitle = item_Axis_Titles;
	}
	public void setItemColor (int item_Color) {
		itemColor = item_Color;
	}
	public void setItemIDX (int item_Index) {
		itemIDX = item_Index;
	}
	public void setItemHeight (int item_Height) {
		itemHeight = item_Height;
	}
	public void setItemWidth (int item_Width) {
		itemWidth = item_Width;
	}
	
		
	public long getItemValue() {
		return itemValue;
	}
	public String getItemAxisTitle() {
		return itemAxisTitle;
	}
	public int getItemColor () {
		return itemColor;
	}
	public int getItemIDX() {
		return itemIDX;
	}
	public int getItemHeight() {
		return itemHeight;
	}
	public int getItemWidth() {
		return itemWidth;
	}
}

