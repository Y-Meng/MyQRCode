package com.qrcode;

import com.esri.android.map.popup.PopupContainer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class PopupDialog extends Dialog {

		private PopupContainer popupContainer;
		private LinearLayout layout;
		public PopupDialog(Context context, PopupContainer popupContainer) {
			super(context);
			this.popupContainer = popupContainer;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layout = new LinearLayout(getContext());
			layout.addView(popupContainer.getPopupContainerView(),
					android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
					android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
			setContentView(layout, params);
		}

		@Override
		public void dismiss() {
			// TODO Auto-generated method stub
			layout.removeAllViews();
			super.dismiss();
		}
		
}
