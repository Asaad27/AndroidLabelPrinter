package com.ProjectSrc.LabelMaker;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * View holder avec 1 champ
 */
public class ViewHolder1 extends RecyclerView.ViewHolder
{
    private TextView field1;
    public ViewHolder1(@NonNull View itemView)
    {
        super(itemView);
        field1 = (TextView) itemView.findViewById(R.id.field1);
    }

    public TextView getField1()
    {
        return field1;
    }

    public void setField1(TextView field1)
    {
        this.field1 = field1;
    }
}

