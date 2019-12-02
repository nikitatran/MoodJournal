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
    Used when RecyclerView uses LinearLayoutManager.
    Defines spacing below each list item.
 */
public class RowSpacingItemDecorator extends RecyclerView.ItemDecoration{

    private final int verticalSpaceHeight;

    public RowSpacingItemDecorator(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = verticalSpaceHeight;
    }
}
