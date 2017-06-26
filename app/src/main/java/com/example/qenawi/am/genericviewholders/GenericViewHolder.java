package com.example.qenawi.am.genericviewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by QEnawi on 3/18/2017.
 */

public abstract class GenericViewHolder extends RecyclerView.ViewHolder
{
    public GenericViewHolder(View itemView) {
        super(itemView);
    }

    public abstract  void bind(int position);
}
