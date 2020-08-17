package com.example.topdf;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder3 extends RecyclerView.ViewHolder
{
    private TextView field31, field32, field33;

    public ViewHolder3(@NonNull View itemView)
    {
        super(itemView);
        field31 = (TextView) itemView.findViewById(R.id.field31);
        field32 = (TextView) itemView.findViewById(R.id.field32);
        field33 = (TextView) itemView.findViewById(R.id.field33);
    }

    public TextView getField31()
    {
        return field31;
    }

    public void setField31(TextView field31)
    {
        this.field31 = field31;
    }

    public TextView getField32()
    {
        return field32;
    }

    public void setField32(TextView field32)
    {
        this.field32 = field32;
    }

    public TextView getField33()
    {
        return field33;
    }

    public void setField33(TextView field33)
    {
        this.field33 = field33;
    }
}

