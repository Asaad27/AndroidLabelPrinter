package com.ProjectSrc.LabelMaker;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder
{
    private ImageView imageView;
    public ViewHolder(@NonNull View itemView)
    {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image_view);
    }

    public ImageView getField()
    {
        return imageView;
    }

    public void setField(ImageView imageView)
    {
        this.imageView = imageView;
    }
}
