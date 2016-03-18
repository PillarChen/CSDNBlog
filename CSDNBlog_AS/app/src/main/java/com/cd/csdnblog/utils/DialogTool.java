package com.cd.csdnblog.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * 弹出框
 * 
 * @author TopSage
 * 
 */
public class DialogTool {

	public static ProgressDialog pDialog;
	private static AlertDialog aDialog;

	// 提示框
	public static void showDialog(Context context, String title, String msg) {

		pDialog = ProgressDialog.show(context, title, msg);
//		if (NetWorkTool.isNetworkConnected(context)
//				|| NetWorkTool.isWIFIConnected(context))
//			pDialog.setCancelable(false);
//		else
			pDialog.setCancelable(true);
		pDialog.show();
	}

	public static void dismissDialog() {

		if(pDialog!=null){
			pDialog.dismiss();
		}
	}

	// 确认框

}
