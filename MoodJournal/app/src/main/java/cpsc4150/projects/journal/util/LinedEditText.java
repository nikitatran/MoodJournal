/*
    CrunchTime (Team 8)
    CPSC 4150 Main Project (Dec 2, 2019)
    Nikita Tran (nikitat@clemson.edu)
    Taylor Miller (tjm2@clemson.edu)

    References used:
        1. https://codingwithmitch.com/courses/sqlite-room-persistence-android/drawing-parallel-lines-edittext/
 */

package cpsc4150.projects.journal.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/*
    This is a custom EditText that imitates lined notepad paper.
    Reference 1 used
 */
public class LinedEditText extends android.support.v7.widget.AppCompatEditText {

    private static final String TAG = "LinedEditText";

    private Rect mRect;
    private Paint mPaint;

    //Necessary constructor for LayoutInflater
    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRect = new Rect();
        mPaint = new Paint();

        //Set paint properties
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(0xFFADC5FF); //Color of the lines on paper
        // use google color picker to find a color
        // and https://convertingcolors.com/hex-color-ADC5FF.html to convert the hex
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // get the height of the view
        int height = ((View)this.getParent()).getHeight();

        int lineHeight = getLineHeight();
        int numberOfLines = height / lineHeight;

        Rect r = mRect;
        Paint paint = mPaint;

        int baseline = getLineBounds(0, r);

        for (int i = 0; i < numberOfLines; i++) {
            canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
            baseline += lineHeight;
        }

        super.onDraw(canvas);
    }

}