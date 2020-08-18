package com.ProjectSrc.LabelMaker;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
/**
 * ComplexRecyclerViewAdapter est une version modifiée du recyclerViewAdapter, permettant de manipuler plusieurs layouts
 */
public class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private DataList dataList;

    public ComplexRecyclerViewAdapter(DataList dataList)
    {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch(viewType)
        {
            case 1:
                View v1 = inflater.inflate(R.layout.layout1, parent, false);
                viewHolder = new ViewHolder1(v1);
                break;
            case 2:
                View v2 = inflater.inflate(R.layout.layout2, parent, false);
                viewHolder = new ViewHolder2(v2);
                break;
            case 3:
                View v3 = inflater.inflate(R.layout.layout3, parent, false);
                viewHolder = new ViewHolder3(v3);
                break;
            case 4:
                View v4 = inflater.inflate(R.layout.layout4, parent, false);
                viewHolder = new ViewHolder4(v4);
                break;
            case 5:
                View v5 = inflater.inflate(R.layout.layout5, parent, false);
                viewHolder = new com.ProjectSrc.LabelMaker.ViewHolder5(v5);
                break;
            case 0:
                View v = inflater.inflate(R.layout.layout, parent, false);
                viewHolder = new ViewHolder(v);
                break;
            default:
                viewHolder = new ViewHolder1(inflater.inflate(R.layout.layout1, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position)
    {

        switch (viewHolder.getItemViewType())
        {
            case 1:
                ViewHolder1 vh1 = (ViewHolder1) viewHolder;
                configureViewHolder1(vh1, position);
                break;
            case 2:
                ViewHolder2 vh2 = (ViewHolder2) viewHolder;
                configureViewHolder2(vh2, position);
                break;
            case 3:
                ViewHolder3 vh3 = (ViewHolder3) viewHolder;
                configureViewHolder3(vh3, position);
                break;
            case 4:
                ViewHolder4 vh4 = (ViewHolder4) viewHolder;
                configureViewHolder4(vh4, position);
                break;
            case 5:
                ViewHolder5 vh5 = (ViewHolder5) viewHolder;
                configureViewHolder5(vh5, position);
                break;
            case 0:
                ViewHolder vh = (ViewHolder) viewHolder;
                configureViewHolder(vh, position);
                break;
        }
    }




    @SuppressLint("SetTextI18n")
    private void configureViewHolder5(ViewHolder5 vh5, int position)
    {
        vh5.getField51().setText(Html.fromHtml(dataList.getFormattedAll(position, 0)));
        vh5.getField52().setText(Html.fromHtml(dataList.getFormattedAll(position, 1)));
        vh5.getField53().setText(Html.fromHtml(dataList.getFormattedAll(position, 2)));
        vh5.getField54().setText(Html.fromHtml(dataList.getFormattedAll(position, 3)));
        vh5.getField55().setText(Html.fromHtml(dataList.getFormattedAll(position, 4)));
    }

    @SuppressLint("SetTextI18n")
    private void configureViewHolder4(ViewHolder4 vh4, int position)
    {
        vh4.getField41().setText(Html.fromHtml(dataList.getFormattedAll(position, 0)));
        vh4.getField42().setText(Html.fromHtml(dataList.getFormattedAll(position, 1)));
        vh4.getField43().setText(Html.fromHtml(dataList.getFormattedAll(position, 2)));
        vh4.getField44().setText(Html.fromHtml(dataList.getFormattedAll(position, 3)));
    }

    @SuppressLint("SetTextI18n")
    private void configureViewHolder3(ViewHolder3 vh3, int position)
    {
        vh3.getField31().setText(Html.fromHtml(dataList.getFormattedAll(position, 0)));
        vh3.getField32().setText(Html.fromHtml(dataList.getFormattedAll(position, 1)));
        vh3.getField33().setText(Html.fromHtml(dataList.getFormattedAll(position, 2)));
    }

    @SuppressLint("SetTextI18n")
    private void configureViewHolder2(ViewHolder2 vh2, int position)
    {
        vh2.getField21().setText(Html.fromHtml(dataList.getFormattedAll(position, 0)));
        vh2.getField22().setText(Html.fromHtml(dataList.getFormattedAll(position, 1)));

    }

    @SuppressLint("SetTextI18n")
    private void configureViewHolder1(ViewHolder1 vh1, int position)
    {
        vh1.getField1().setText(Html.fromHtml(dataList.getFormattedTitle(position, 0)));
        vh1.getField1().append("          " + dataList.getSimpleUserText(position, 0));
    }

    private void configureViewHolder(ViewHolder vh, int position)
    {
        vh.getField().setImageBitmap( dataList.getBareCode(position) );
    }

    @Override
    public int getItemCount()
    {
        return dataList.getSize();
    }

    @Override
    public int getItemViewType(int position)
    {
        return dataList.getType(position);
    }
}
