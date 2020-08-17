package com.ProjectSrc.LabelMaker;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder4 extends RecyclerView.ViewHolder
{
    private TextView field41, field42, field43, field44;

    public ViewHolder4(@NonNull View itemView)
    {
        super(itemView);
        field41 = (TextView) itemView.findViewById(R.id.field41);
        field42 = (TextView) itemView.findViewById(R.id.field42);
        field43 = (TextView) itemView.findViewById(R.id.field43);
        field44 = (TextView) itemView.findViewById(R.id.field44);
    }

    public TextView getField41()
    {
        return field41;
    }

    public void setField41(TextView field41)
    {
        this.field41 = field41;
    }

    public TextView getField42()
    {
        return field42;
    }

    public void setField42(TextView field42)
    {
        this.field42 = field42;
    }

    public TextView getField43()
    {
        return field43;
    }

    public void setField43(TextView field43)
    {
        this.field43 = field43;
    }

    public TextView getField44()
    {
        return field44;
    }

    public void setField44(TextView field44)
    {
        this.field44 = field44;
    }
}

