package com.example.topdf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class DialogEdit extends DialogFragment
{
    DataList dataList;
    EditText textIn, textIn2;
    Button buttonAdd, buttonValidate;
    LinearLayout containers;


    static DialogEdit newInstance()
    {
        return new DialogEdit();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(DialogEdit.STYLE_NORMAL, R.style.FullscreenDialogTheme);

        if (getArguments() != null)
        {
            dataList = (DataList) getArguments().get("data");
            assert dataList != null;
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialog_add, container, false);

        textIn = (EditText) view.findViewById(R.id.textin);
        textIn2 = (EditText) view.findViewById(R.id.textin2);
        buttonAdd = (Button) view.findViewById(R.id.addButton);
        buttonValidate = (Button) view.findViewById(R.id.valider);
        containers = (LinearLayout) view.findViewById(R.id.containerId);

        onCreateAddViews();
        buttonAdd.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view)
            {
                LayoutInflater layoutInflater = (LayoutInflater) Objects.requireNonNull(DialogEdit.this.getContext()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                @SuppressLint("InflateParams") final View addView = layoutInflater.inflate(R.layout.row, null);
                TextView textOut = (TextView) addView.findViewById(R.id.textout);
                TextView textOut2 = (TextView) addView.findViewById(R.id.textout2);
                textOut.setText(textIn.getText().toString());
                textOut2.setText(textIn2.getText().toString());
                Button buttonRemove = (Button) addView.findViewById(R.id.remove);
                final View.OnClickListener thisListener = new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        ((LinearLayout) addView.getParent()).removeView(addView);
                        listAllAddView();
                    }
                };
                buttonRemove.setOnClickListener(thisListener);
                textIn.setText("Titre");
                textIn2.setText("Texte");
                containers.addView(addView);
                listAllAddView();
            }
        });

        buttonValidate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int childCount = containers.getChildCount();
                TextView textOut, textOut2;
                String[] title = new String[childCount];
                String[] text = new String[childCount];
                int position = dataList.dialogEditRowPosition;

                for (int i = 0; i < childCount; i++)
                {
                    View thisChild = containers.getChildAt(i);
                    textOut = (TextView) thisChild.findViewById(R.id.textout);
                    textOut2 = (TextView) thisChild.findViewById(R.id.textout2);
                    title[i] = textOut.getText().toString();
                    text[i] = textOut2.getText().toString();
                }
                dataList.deleteNode(position);
                dataList.insertNode(position, childCount, title, text);
                MainActivity.complexRecyclerViewAdapter.notifyDataSetChanged();
                dismiss();
            }
        });


        return view;
    }

    private void onCreateAddViews()
    {
        int position = dataList.dialogEditRowPosition;
        int n = dataList.getType(position);

        for (int i = 0; i < n; i++)
        {
            LayoutInflater layoutInflater = (LayoutInflater) Objects.requireNonNull(DialogEdit.this.getContext()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.row, null);
            TextView textOut = (TextView) addView.findViewById(R.id.textout);
            TextView textOut2 = (TextView) addView.findViewById(R.id.textout2);
            textOut.setText(Html.fromHtml(dataList.getFormattedTitle(position, i)));
            textOut2.setText(dataList.getSimpleUserText(position, i));
            Button buttonRemove = (Button) addView.findViewById(R.id.remove);
            final View.OnClickListener thisListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ((LinearLayout) addView.getParent()).removeView(addView);
                    listAllAddView();
                }
            };
            buttonRemove.setOnClickListener(thisListener);
            containers.addView(addView);
            listAllAddView();
        }

    }

    private void listAllAddView()
    {
        int childCount = containers.getChildCount();

        for (int i = 0; i < childCount; i++)
        {
            View thisChild = containers.getChildAt(i);
        }
        if (containers.getChildCount() >= 5)
            buttonAdd.setVisibility(View.INVISIBLE);
        else
            buttonAdd.setVisibility(View.VISIBLE);
    }

}
