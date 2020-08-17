package com.ProjectSrc.LabelMaker;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder5 extends RecyclerView.ViewHolder
{
    private TextView field51, field52, field53, field54, field55;

    public ViewHolder5(@NonNull View itemView)
    {
        super(itemView);
        field51 = (TextView) itemView.findViewById(R.id.field51);
        field52 = (TextView) itemView.findViewById(R.id.field52);
        field53 = (TextView) itemView.findViewById(R.id.field53);
        field54 = (TextView) itemView.findViewById(R.id.field54);
        field55 = (TextView) itemView.findViewById(R.id.field55);
    }

    public TextView getField51()
    {
        return field51;
    }

    public void setField51(TextView field51)
    {
        this.field51 = field51;
    }

    public TextView getField52()
    {
        return field52;
    }

    public void setField52(TextView field52)
    {
        this.field52 = field52;
    }

    public TextView getField53()
    {
        return field53;
    }

    public void setField53(TextView field53)
    {
        this.field53 = field53;
    }

    public TextView getField54()
    {
        return field54;
    }

    public void setField54(TextView field54)
    {
        this.field54 = field54;
    }

    public TextView getField55()
    {
        return field55;
    }

    public void setField55(TextView field55)
    {
        this.field55 = field55;
    }
}
