/*
    CrunchTime (Team 8)
    CPSC 4150 Main Project (Dec 2, 2019)
    Nikita Tran (nikitat@clemson.edu)
    Taylor Miller (tjm2@clemson.edu)
 */

package cpsc4150.projects.journal.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/*
    Used when RecyclerView uses GridLayoutManager with 2 columns.
    Defines spacing between 2 columns and below each list item.
 */
public class TwoColumnSpacingItemDecorator extends RecyclerView.ItemDecoration{

    private final int columnSpaceHeight, verticalSpaceHeight;

    public TwoColumnSpacingItemDecorator(int columnSpaceHeight, int verticalSpaceHeight) {
        this.columnSpaceHeight = columnSpaceHeight;
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view)%2 == 0) { //horizontal spacing for first column
            outRect.right = columnSpaceHeight;
        }
        if (parent.getChildLayoutPosition(view)%2 == 1) { //horizontal spacing for second column
            outRect.left = columnSpaceHeight;
        }
        outRect.bottom = verticalSpaceHeight; //vertical spacing
    }
}
