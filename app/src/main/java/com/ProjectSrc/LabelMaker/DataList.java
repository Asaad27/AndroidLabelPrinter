package com.ProjectSrc.LabelMaker;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.LinkedList;

public class DataList implements Serializable
{
    public int dialogEditRowPosition;

    public LinkedList<Label> mLinkedList;

    public DataList()
    {
        mLinkedList = new LinkedList<>();
    }

    public DataList(LinkedList<Label> mLinkedList)
    {
        this.mLinkedList = mLinkedList;
    }

    public void addNode(int type, String[] title, String[] userText)
    {
        Label label = new Label(type, title, userText);
        mLinkedList.add(label);
    }
    public void addBarcode(Label label)
    {
        mLinkedList.add(label);
    }
    public void insertNode(int index, int type, String[] title, String[] userText)
    {
        Label label = new Label(type, title, userText);
        mLinkedList.add(index, label);
    }

    public Bitmap getBareCode(int position)
    {
        return mLinkedList.get(position).getBitmap();
    }

    public void deleteNode(int position)
    {
        mLinkedList.remove(position);
    }

    public int getNumberOfFields(int position)
    {
        return mLinkedList.get(position).getNumberOfElements();
    }

    public String getSimpleTitle(int position, int index)
    {
        return mLinkedList.get(position).getTitles()[index];
    }

    public String getSimpleUserText(int position, int index)
    {
        return mLinkedList.get(position).getUserText()[index];
    }

    public String getFormattedTitle(int position, int index)
    {
        return "<b>" + mLinkedList.get(position).getTitles()[index] + "</b>";
    }

    public String getFormattedAll(int position, int index)
    {
        return getFormattedTitle(position, index) + "<br/>" + getSimpleUserText(position, index);
    }

    public int getSize()
    {
        return mLinkedList.size();
    }

    public int getType(int position)
    {
        return mLinkedList.get(position).getType();
    }

    public void setType(int position, int type) { mLinkedList.get(position).setType(type);}
}
