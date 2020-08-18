package com.ProjectSrc.LabelMaker;

import android.graphics.Bitmap;

import java.io.Serializable;
/**
 * <h1>Label</h1>
 * cette classe continent les noeuds, qui sont utilisées par la liste chainée dans la classe DataList

 */
public class Label implements Serializable
{


    private int type;
    private String[] titles;
    private String[] userTexts;
    private ProxyBitmap bitmap;

    /**
     *Constructeur sans barcode
     *@param titles les titres du champ
     *@param userTexts les textes de chaque titre
     */
    public Label(int type, String[] titles, String[] userTexts)
    {
        this.type = type;
        this.titles = titles;
        this.userTexts = userTexts;

    }
    /**
     *Constructeur avec barcode
     *@param bitmap  Les objets Bitmap doivent etres convertis en ProxyBitmap, pour permettre la serialization de la classe Label
     */
    public Label(int type, String[] titles, String[] userTexts, ProxyBitmap bitmap)
    {
        this.type = type;
        this.titles = titles;
        this.userTexts = userTexts;
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap()
    {
        if (bitmap != null)
            return bitmap.getBitmap();
        else
            return null;
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
