package com.iisi.customlayoutdemo.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;

import com.iisi.customlayoutdemo.R;


/**
 * Created by Sambow on 16/9/6.
 */
public class RoundImageView extends androidx.appcompat.widget.AppCompatImageView
{
	private enum ImageType
	{
		Normal(0),
		Round(1),
		Circle(2);

		private int key;
		ImageType(int key)
		{
			this.key = key;
		}

		public int getKey()
		{
			return key;
		}

		public static ImageType getType(int key)
		{
			ImageType[] alignments = values();
			for (ImageType alignment : alignments)
			{
				if (alignment.getKey() == key)
				{
					return alignment;
				}
			}
			return ImageType.Normal;
		}
	}

	private ImageType type = ImageType.Normal;
	private boolean border = false;
	private int borderWidth;
	private @ColorInt int borderColor = Color.TRANSPARENT;
	private Paint paint;

	public RoundImageView(Context context)
	{
		super(context);
		initialize(context, null);
	}

	public RoundImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initialize(context, attrs);
	}

	public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		initialize(context, attrs);
	}

	/*@TargetApi(Build.VERSION_CODES.LOLLIPOP)// api 21
	public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
	{
		super(context, attrs, defStyleAttr, defStyleRes);
		initialize(context, attrs);
	}*/

	private void initialize(Context context, AttributeSet attrs)
	{
		borderWidth = context.getResources().getDimensionPixelSize(R.dimen.dp_1);
		if (attrs != null)
		{
			TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundImageView, 0, 0);
			type = ImageType.getType(typedArray.getInt(R.styleable.RoundImageView_imageType, ImageType.Normal.getKey()));
			border = typedArray.getBoolean(R.styleable.RoundImageView_roundBorder, false);
			borderWidth = typedArray.getDimensionPixelSize(R.styleable.RoundImageView_roundBorderWidth, borderWidth);
			borderColor = typedArray.getColor(R.styleable.RoundImageView_roundBorderColor, Color.TRANSPARENT);
		}
		initialize(context);
	}


	private void initialize(Context context)
	{
		paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(borderColor);
		paint.setStrokeWidth(borderWidth);
		paint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		Drawable drawable = getDrawable();
		if (type == ImageType.Normal || drawable == null || getWidth() == 0 || getHeight() == 0)
		{
			super.onDraw(canvas);
			return;
		}
		Bitmap bitmap = null;
		if (drawable instanceof BitmapDrawable)
		{
			bitmap = ((BitmapDrawable) drawable).getBitmap();
		}
		else
		{
			bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
			Canvas cv = new Canvas(bitmap);
			drawable.setBounds(0, 0, cv.getWidth(), cv.getHeight());
			drawable.draw(cv);
		}

		if (bitmap == null)
		{
			super.onDraw(canvas);
			return;
		}

		int width = getWidth(), height = getHeight();
		int centerX = width / 2;
		int centerY = height / 2;
		int radius = width > height ? height / 2 : width / 2;
		int diameter = radius * 2;

		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();

		float scale = 1.f;

		BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		if (type == ImageType.Circle)
		{
			int size = Math.min(bitmapWidth, bitmapHeight);

			scale = diameter * 1.f / size;
		}
		else if (type == ImageType.Round)
		{
			scale = Math.max(width * 1.0f / bitmapWidth, height * 1.0f / bitmapHeight);

		}

		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

		float tTranslateX = 0;
		float tTranslateY = 0;

		tTranslateY = (height - bitmapHeight * scale) / 2;
		tTranslateX = (width - bitmapWidth * scale) / 2;

		int borderWidthOffset = borderWidth / 2;

		if (type == ImageType.Circle)
		{
			int realRadius = radius - borderWidthOffset;
			bitmapShader.setLocalMatrix(matrix);
			Bitmap dest = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
			Canvas cv = new Canvas(dest);
			paint.setStyle(Paint.Style.FILL);
			paint.setShader(bitmapShader);
			cv.drawCircle(radius, realRadius, realRadius, paint);
			canvas.drawBitmap(dest, centerX - radius, centerY - radius, paint);
			if (border)
			{
				paint.setStyle(Paint.Style.STROKE);
				paint.setShader(null);
				canvas.drawCircle(centerX, centerY, realRadius, paint);
			}
		}
		else if (type == ImageType.Round)
		{
			matrix.postTranslate(tTranslateX, tTranslateY);
			bitmapShader.setLocalMatrix(matrix);

			RectF rectF = new RectF(0, 0, width - borderWidthOffset * 2, height - borderWidthOffset * 2);

			Bitmap dest = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			Canvas cv = new Canvas(dest);
			paint.setStyle(Paint.Style.FILL);
			paint.setShader(bitmapShader);
			cv.drawRoundRect(rectF, radius, radius, paint);

			canvas.drawBitmap(dest, centerX - rectF.centerX(), centerY - rectF.centerY(), paint);
			if (border)
			{
				paint.setStyle(Paint.Style.STROKE);
				paint.setShader(null);
				canvas.drawRoundRect(rectF, radius, radius, paint);
			}
		}
	}
}