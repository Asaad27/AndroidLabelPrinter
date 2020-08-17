package com.example.topdf;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder2 extends RecyclerView.ViewHolder
{
    private TextView field21, field22;
    public ViewHolder2(@NonNull View itemView)
    {
        super(itemView);
        field21 = (TextView) itemView.findViewById(R.id.field21);
        field22 = (TextView) itemView.findViewById(R.id.field22);
    }

    public TextView getField21()
    {
        return field21;
    }

    public void setField21(TextView field21)
    {
        this.field21 = field21;
    }

    public TextView getField22()
    {
        return field22;
    }

    public void setField22(TextView field22)
    {
        this.field22 = field22;
    }
}

