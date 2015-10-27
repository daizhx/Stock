package com.hengxuan.stock.widget;

import com.hengxuan.stock.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

public class CustomProgressDialog extends Dialog {

	private Context context = null;
	private static CustomProgressDialog customProgressDialog = null;
	
	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
	}
	
	public static CustomProgressDialog createDialog(Context c){
		customProgressDialog = new CustomProgressDialog(c, R.style.CustomDialog);
		customProgressDialog.setContentView(R.layout.custom_progress_dialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return customProgressDialog;
	}
	
	
}
