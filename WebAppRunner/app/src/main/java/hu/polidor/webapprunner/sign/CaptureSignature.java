package hu.polidor.webapprunner.sign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import hu.polidor.webapprunner.Const;
import hu.polidor.webapprunner.MainActivity;
import hu.polidor.webapprunner.R;

import java.io.ByteArrayOutputStream;

public class CaptureSignature extends Activity
{
    LinearLayout mContent;
    signature mSignature;
    Button mClear, mGetSign, mCancel;
    View mView;
    private Bitmap mBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signature);

		int orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Const.CONF_SIGN_ORIENTATION, false))
		{
			orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		}

		this.setRequestedOrientation(orientation);

        mContent = findViewById(R.id.linearLayout);
        mSignature = new signature(this, null);
        mSignature.setBackgroundColor(Color.WHITE);
        mContent.addView(mSignature, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mClear = findViewById(R.id.clear);
        mGetSign = findViewById(R.id.getsign);
        mGetSign.setEnabled(false);
        mCancel = findViewById(R.id.cancel);
        mView = mContent;

        mClear.setOnClickListener(new OnClickListener() {
				public void onClick(View v)
				{
					mSignature.clear();
					mGetSign.setEnabled(false);
				}
			});

        mGetSign.setOnClickListener(new OnClickListener() {
				public void onClick(View v)
				{
					mView.setDrawingCacheEnabled(true);
					String urlImage = mSignature.save(mView);
					Bundle b = new Bundle();
					b.putString(MainActivity.SIGNATURE_STATUS, MainActivity.SIGNATURE_STATUS_DONE);
					b.putString(MainActivity.SIGNATURE_URLIMAGE, urlImage);
					Intent intent = new Intent();
					intent.putExtras(b);
					setResult(RESULT_OK, intent);
					finish();
				}
			});

        mCancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v)
				{
					Bundle b = new Bundle();
					b.putString(MainActivity.SIGNATURE_STATUS, MainActivity.SIGNATURE_STATUS_CANCEL);
					Intent intent = new Intent();
					intent.putExtras(b);
					setResult(RESULT_OK, intent);
					finish();
				}
			});

    }

    public class signature extends View
	{
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private final RectF dirtyRect = new RectF();
        private Paint paint = new Paint();
        private Path path = new Path();
        private float lastTouchX;
        private float lastTouchY;

        public signature(Context context, AttributeSet attrs)
		{
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public String save(View v)
		{
            String retData = null;
            if (mBitmap == null)
			{
                mBitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(mBitmap);
            try
			{
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                v.draw(canvas);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
                byte[] b = baos.toByteArray();
                retData = Base64.encodeToString(b, Base64.DEFAULT);
            }
			catch (Exception e)
			{
                e.printStackTrace();
            }
            return retData;
        }

        public void clear()
		{
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas)
		{
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event)
		{
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);

            switch (event.getAction())
			{
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++)
					{
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;
                default:
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
					   (int) (dirtyRect.top - HALF_STROKE_WIDTH),
					   (int) (dirtyRect.right + HALF_STROKE_WIDTH),
					   (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void expandDirtyRect(float historicalX, float historicalY)
		{
            if (historicalX < dirtyRect.left)
			{
                dirtyRect.left = historicalX;
            }
			else if (historicalX > dirtyRect.right)
			{
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top)
			{
                dirtyRect.top = historicalY;
            }
			else if (historicalY > dirtyRect.bottom)
			{
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY)
		{
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }
}
