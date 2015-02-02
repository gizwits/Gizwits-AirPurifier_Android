package com.gizwits.framework.views;

//import java.io.InputStream;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.v4.util.LruCache;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.gizwits.aircondition.R;
import com.gizwits.framework.utils.UIUtils;
//import android.content.res.Resources;
//import android.graphics.BitmapFactory;

/**
 * 环形进度条
 * 
 * @author Lien
 * 
 */
public class RoseBoxSeekBarView extends View  {

	private final static String TAG = "RoseBoxSeekBarView";
	private LruCache<String, Bitmap> mMemoryCache;

	/** 轨迹圆的半径 */
	private int circleRadius;
	/** 轨迹圆的圆心x坐标 */
	private int circleCx;
	/** 轨迹圆的圆心y坐标 */
	private int circleCy;
	/** 圆环Paint */
	private Paint circlePaint;
	private RectF circleRectf;
	/** 内部进度条Paint */
	private Paint progressPaint;
	/** 圆环内部镂空Paint */


	private Bitmap circleBgNormal;
	private Bitmap circleBgErr;
	private Bitmap circleRingNormal;
	private Bitmap circleRingErr;

	private int viewWidth;
	private int viewHeight;

	private Paint tipsTextPaint;
	private Paint percentNumPaint;
	private Paint percentTipPaint;

	private Xfermode xfermode;

	private int circleWidth;


	private int bigfonSize;
	private int smallfonSize;
	private int chinesefonSize;

	private int percent = 100;


	private static Context mContext;


	private Rect rect1;
	private RectF rectf1;
	private PaintFlagsDrawFilter drawFilter;
	private Resources mResources;
	private boolean isRoseboxErr = false;
	private Paint ringPaint;

	public RoseBoxSeekBarView(Context context) {
		this(context, null);

	}

	/**
	 * @param context
	 * @param attrs
	 */
	public RoseBoxSeekBarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public RoseBoxSeekBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		mResources = context.getApplicationContext().getResources();
		drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG);
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		int cacheSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// 重写此方法来衡量每张图片的大小，默认返回图片数量。
				return bitmap.getByteCount() / 1024;
			}
		};
		// typeface = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/HELVETICANEUELTSTD-THEX.OTF");

	}

	public void initParams(int width, int height) {

		setFocusable(true);
		setFocusableInTouchMode(true);
		viewWidth = width;
		viewHeight = height;
		circleCx = width >> 1;
		circleCy = height >> 1;
		// circleRadius = (int) Math.round(width / 13.5 * 6);
		circleRadius = circleCx;
		// circleRadius = (int) Math.round(circleBlueBg.getHeight() /2);
		bigfonSize = circleRadius / 2;
		smallfonSize = circleRadius / 4;
		chinesefonSize = bigfonSize / 5;

		circleWidth = (int) (circleRadius / 2);

		circleBgNormal = loadBitmap(R.drawable.rb_circle_bg_normal, width);
		circleBgErr = loadBitmap(R.drawable.rb_circle_bg_err, width);
		circleRingNormal = loadBitmap(R.drawable.rb_circle_ring_normal, width);
		circleRingErr = loadBitmap(R.drawable.rb_circle_ring_err, width);

		rect1 = new Rect(0, 0, circleBgNormal.getWidth(),
				circleBgNormal.getHeight());
		rectf1 = new RectF(circleCx - (int) ((width / 2) * 0.9), circleCy
				- (int) ((width / 2) * 0.9), circleCx
				+ (int) ((width / 2) * 0.9), circleCy
				+ (int) ((width / 2) * 0.9));

		// 调整按钮比例
		// matrix.postScale(0.5f, 0.5f);
		Matrix matrix = new Matrix();
		matrix.postScale(0.5f, 0.5f);

		initPaint();
	}

	private void initPaint() {
		xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

		circlePaint = new Paint();
		circlePaint.setAntiAlias(true);
		circlePaint.setColor(mResources.getColor(R.color.circle_color));
		circlePaint.setStyle(Paint.Style.STROKE);
		circlePaint.setStrokeWidth(circleWidth);

		circleRectf = new RectF();
		circleRectf.left = (circleCx - circleRadius) + circleWidth / 2;
		circleRectf.top = (circleCy - circleRadius) + circleWidth / 2;
		circleRectf.right = circleRadius * 2 + (circleCx - circleRadius)
				- circleWidth / 2;
		circleRectf.bottom = circleRadius * 2 + (circleCy - circleRadius)
				- circleWidth / 2;


		progressPaint = new Paint();
		progressPaint.setAntiAlias(true);
		progressPaint.setColor(Color.YELLOW);
		progressPaint.setStyle(Paint.Style.STROKE);
		progressPaint.setStrokeWidth(circleWidth / 2);

		tipsTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		tipsTextPaint.setTextSize(chinesefonSize);
		tipsTextPaint.setColor(Color.WHITE);
		tipsTextPaint.setTextAlign(Paint.Align.CENTER);

		percentNumPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		percentNumPaint.setTextSize(bigfonSize);
		percentNumPaint.setColor(Color.WHITE);
		percentNumPaint.setTextAlign(Paint.Align.CENTER);

		percentTipPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		percentTipPaint.setTextSize(smallfonSize);
		percentTipPaint.setColor(Color.WHITE);
		percentTipPaint.setTextAlign(Paint.Align.CENTER);

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		int layoutWidth = right - left;
		int layoutHeight = bottom - top;
		initParams(layoutWidth, layoutHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
		// 抗锯齿
		canvas.setDrawFilter(drawFilter);
		drawBg(canvas);
		drawTipsText(canvas);
		drawPercentNum(canvas);
		drawProgressCircle(canvas, percent);

	}

	private void drawBg(Canvas canvas) {
		if (isRoseboxErr) {
			canvas.drawBitmap(circleBgErr, rect1, rectf1, circlePaint);
		} else {
			canvas.drawBitmap(circleBgNormal, rect1, rectf1, circlePaint);
		}

	};

	/**
	 * 画进度内环
	 * 
	 * @param int template 温度
	 * */
	private void drawProgressCircle(Canvas canvas, float percent) {
		int sc = canvas.saveLayer(0, 0, viewWidth, viewHeight, null,
				Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
						| Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
						| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
						| Canvas.CLIP_TO_LAYER_SAVE_FLAG);

		if (isRoseboxErr) {
			canvas.drawBitmap(circleRingErr, rect1, rectf1, circlePaint);
		} else {
			canvas.drawBitmap(circleRingNormal, rect1, rectf1, circlePaint);
		}

		circlePaint.setXfermode(xfermode);
		// template=75;
		float dregress = (percent / 100) * 360;
		// canvas.drawArc(circleInnerRectF, 135,
		// ((float) (template - 35) / TOTAL_PREGRESS) * 270, false,
		// progressPaint);
		canvas.drawArc(circleRectf,90 + dregress, 360- dregress, false,
				circlePaint);
		circlePaint.setXfermode(null);
		canvas.restoreToCount(sc);
	}

	private void drawPercentNum(Canvas canvas) {
		if (percent == 100) {
			canvas.drawText(percent + "", circleCx - smallfonSize / 2, circleCy
					+ (int) (bigfonSize / 2), percentNumPaint);
		} else {
			canvas.drawText(percent + "", circleCx, circleCy
					+ (int) (bigfonSize / 2), percentNumPaint);
		}

	}

	/**
	 * 画提示文字
	 * */
	private void drawTipsText(Canvas canvas) {
		canvas.drawText("当前滤网寿命为", circleCx - (int) (chinesefonSize * 0.3),
				circleCy - (int) (bigfonSize / 2), tipsTextPaint);
		canvas.drawText("%", circleCx + (int) (circleRadius / 2.5), circleCy
				+ smallfonSize / 3, percentTipPaint);
	}

	public float finalangel(float x, float y, float angel) {

		if (x < circleCx && y < circleCy) {
			return 180 + angel;
		} else if (x == circleCx && y < circleCy) {
			return 270;
		} else if (x > circleCx && y < circleCy) {
			return 360 - angel;
		} else if (x > circleCx && y == circleCy) {
			return 360;
		} else if (x < circleCx && y == circleCy) {
			return 180;
		} else if (x > circleCx && y > circleCy) {
			if (angel < 1) {
				return 360;
			}
			return angel;
		} else {
			return 180 - angel;
		}

	}

	public float angel(float x, float y) {
		// 算出圆心到(x,y)的直线距离
		float b = (float) Math.sqrt((circleCx - x) * (circleCx - x)
				+ (circleCy - y) * (circleCy - y));
		// 垂直高
		float c = Math.abs(y - circleCy);
		// 水平长
		float a = Math.abs(x - circleCx);

		float cos = (a * a + b * b - c * c) / (2 * a * b);
		// 算出弧度
		float angel = (float) Math.acos(cos);
		// 转换为角度
		return (float) ((angel * 180 / Math.PI));

	}

	private int getTemplateFromDegree(float degree) {
		int template = 0;
		if (degree > 90) {
			template = (int) (degree - 90) / 8 + 30;
		} else if (degree <= 90 && degree >= 88) {
			template = 75;
		} else {
			template = (int) (degree + 270) / 8 + 30;
		}
		return template;
	}

	private int getDegresFromTemplate(int template) {
		int degree;
		if (template >= 30 && template <= 63) {
			degree = (template - 30) * 8 + 90;
		} else {
			degree = (template - 30) * 8 - 270;
		}
		return degree;
	}

	public int getFontHeight(float fontSize) {
		Paint paint = new Paint();
		paint.setTextSize(fontSize);
		// paint.setTypeface(typeface);
		FontMetrics fm = paint.getFontMetrics();
		return (int) Math.ceil(fm.descent - fm.ascent) + 2;
	}


	private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	private Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

	private Bitmap loadBitmap(int resId, int layoutWidth, int layoutHeight,
			int reqWidth, int reqHeight) {
		final String imageKey = String.valueOf(resId);
		Bitmap bitmap = getBitmapFromMemCache(imageKey);
		if (bitmap != null) {
			return bitmap;
		} else {
			Bitmap temp = UIUtils.decodeSampledBitmapFromResource(mContext,
					mResources, resId, layoutWidth, layoutHeight);
			bitmap = UIUtils.resizeImage(temp, reqWidth, reqHeight);
			addBitmapToMemoryCache(String.valueOf(resId), bitmap);
			temp.recycle();
			return bitmap;
		}
	}

	private Bitmap loadBitmap(int resId, int reqWidth) {
		final String imageKey = String.valueOf(resId);
		Bitmap bitmap = getBitmapFromMemCache(imageKey);
		if (bitmap != null) {
			return bitmap;
		} else {
			bitmap = UIUtils.decodeSampledBitmapFromResource(mContext,
					mResources, resId, reqWidth, reqWidth);
			addBitmapToMemoryCache(String.valueOf(resId), bitmap);
			return bitmap;
		}
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
		postInvalidate();
	}

	public boolean isRoseboxErr() {
		return isRoseboxErr;
	}

	public void setRoseboxErr(boolean isRoseboxErr) {
		this.isRoseboxErr = isRoseboxErr;
		postInvalidate();
	}

}
