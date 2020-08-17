package com.example.topdf;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.net.URI;

public class Label implements Serializable
{
    private int numberOfElements;
    private int type;
    private String[] titles;
    private String[] userTexts;
    private ProxyBitmap bitmap;

    public Label(int type, String[] titles, String[] userTexts)
    {
        this.type = type;
        this.titles = titles;
        this.userTexts = userTexts;

    }

    public Label(int type, String[] titles, String[] userTexts, ProxyBitmap bitmap)
    {
        this.type = type;
        this.titles = titles;
        this.userTexts = userTexts;
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap()
    {
        assert bitmap != null;
        return bitmap.getBitmap();
    }

    public void setBitmap(ProxyBitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    public int getType()
    {
        return type;
    }

    public int getNumberOfElements()
    {
        return titles.length;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String[] getTitles()
    {
        return titles;
    }

    public void setTitles(String[] titles)
    {
        this.titles = titles;
    }

    public String[] getUserText()
    {
        return userTexts;
    }

    public void setUserText(String[] userText)
    {
        this.userTexts = userText;
    }
}
