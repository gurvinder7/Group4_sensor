package com.example.group4_sensor;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.TextView;

public class GestureMain extends AppCompatActivity {
    private ImageView img;
    private Matrix matrix = new Matrix();
    private float scale = 1f;
    private ScaleGestureDetector detector;
    private GestureDetector gestureDetector;
    TextView txtmsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_main);
        img = (ImageView)findViewById(R.id.imgvw);
        Drawable d = img.getDrawable();
        RectF imageRectF = new RectF(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        RectF viewRectF = new RectF(0, 0, img.getWidth(), img.getHeight());
        matrix.setRectToRect(imageRectF, viewRectF, Matrix.ScaleToFit.CENTER);
        img.setImageMatrix(matrix);
        txtmsg=findViewById(R.id.txtMsg);
        detector = new ScaleGestureDetector(GestureMain.this,new ScaleListener());
        gestureDetector=new GestureDetector(GestureMain.this,new gestListener());

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        detector.onTouchEvent(event);
        return true;
    }
    private class gestListener extends GestureDetector.SimpleOnGestureListener{
        public boolean onDoubleTap(MotionEvent event) {
            Log.d("---TOUCH--","DoubleTap");
            txtmsg.setText("DOUBLE TAPPED");
            return true;
        }
        public void onLongPress(MotionEvent event) {
            Log.d("---TOUCH--","LongPress");
            txtmsg.setText("LONG PRESSED");
        }
        /*public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        }
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        }*/
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Drawable d = img.getDrawable();

            float offsetX = (img.getWidth() - d.getIntrinsicWidth()) / 2F;
            float offsetY = (img.getHeight() - d.getIntrinsicHeight()) / 2F;

            float centerX = img.getWidth() / 2F;
            float centerY = img.getHeight() / 2F;
            scale *= detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale, 5.0f));
            matrix.setScale(scale, scale,centerX, centerY);
            matrix.preTranslate(offsetX, offsetY);
            img.setImageMatrix(matrix);
            return true;
        }
    }
}
