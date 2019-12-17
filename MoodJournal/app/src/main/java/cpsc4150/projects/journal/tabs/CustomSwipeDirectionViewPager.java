package cpsc4150.projects.journal.tabs;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/*
    This is a ViewPager where you can set the swipeable directions of a tab.

    Look at SwipeDirection.java to see what SwipeDirection.DEFAULT, NONE, LEFT, RIGHT mean.

    Reference:
    https://stackoverflow.com/questions/19602369/how-to-disable-viewpager-from-swiping-in-one-direction/34076649#34076649
 */
public class CustomSwipeDirectionViewPager extends ViewPager {
    private float startingPosX;
    private SwipeDirection allowedDirection;

    public CustomSwipeDirectionViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        allowedDirection = SwipeDirection.DEFAULT;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isSwipeAllowed(event)) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (isSwipeAllowed(event)) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    private boolean isSwipeAllowed(MotionEvent event) {
        if(allowedDirection == SwipeDirection.DEFAULT) return true;
        if(allowedDirection == SwipeDirection.NONE) return false;

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            startingPosX = event.getX();
            return true;
        }

        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            float endingPosX = event.getX();
            float diffX = endingPosX - startingPosX;
            //Log.d("swipe", diffX+"");

            //ban swiping to right when only left swipes allowed
            if (diffX > 0 && allowedDirection == SwipeDirection.LEFT) return false;
            //ban swiping to left when only right swipes allowed
            if (diffX < 0 && allowedDirection == SwipeDirection.RIGHT) return false;
        }
        return true;
    }

    public void setAllowedSwipeDirection(SwipeDirection direction) {
        this.allowedDirection = direction;
    }
}
