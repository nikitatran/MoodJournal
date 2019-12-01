package com.codingwithmitch.journal.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ColumnSpacingItemDecorator extends RecyclerView.ItemDecoration{

    private final int columnSpaceHeight, verticalSpaceHeight;

    public ColumnSpacingItemDecorator(int columnSpaceHeight, int verticalSpaceHeight) {
        this.columnSpaceHeight = columnSpaceHeight;
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view)%2 == 0) { //add spacing to only first column
            outRect.right = columnSpaceHeight;
        }
        if (parent.getChildLayoutPosition(view)%2 == 1) { //add spacing to only second column
            outRect.left = columnSpaceHeight;
        }
        outRect.bottom = verticalSpaceHeight;
    }
}
