package com.roch.hzz_baidumap_demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.roch.hzz_baidumap_demo.R;

public class CircleImageView extends MaskedImage {

	public CircleImageView(Context paramContext) {
		super(paramContext);
	}

	public CircleImageView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	public CircleImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
	}

	public Bitmap createMask() {
		int i = getWidth();
		int j = getHeight();
		Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
		Bitmap localBitmap = Bitmap.createBitmap(i, j, localConfig);
		Canvas localCanvas = new Canvas(localBitmap);
		Paint localPaint = new Paint(1);
		localPaint.setColor(getResources().getColor(R.color.line_color)); // -16777216
		float f1 = getWidth();
		float f2 = getHeight();
		RectF localRectF = new RectF(0.0F, 0.0F, f1, f2);
//		localCanvas.drawOval(localRectF, localPaint);
		localCanvas.drawRoundRect(localRectF, 15, 15, localPaint);
		return localBitmap;
	}
}
