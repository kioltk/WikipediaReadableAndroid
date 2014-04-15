package com.agcy.wikiread.Core.Parsing.Nodes;

import android.content.Context;
import android.text.Spannable;
import android.view.View;

import com.agcy.wikiread.Models.Legend;
import com.agcy.wikiread.Views.PictureView;

import java.util.ArrayList;

/**
 * Created by kiolt_000 on 07.03.14.
 */
public class PictureNode extends Node {

    String imageName;
    Spannable description;
    ArrayList<Legend> legend;
    public PictureNode(String imageName, Spannable description, ArrayList<Legend> legend){
        this.imageName = imageName;
        this.description = description;
        this.legend = legend;
    }

    @Override
    public View getView(Context context) {

        PictureView view = new PictureView(context);
        view.setImageName(imageName);
        view.setDescription(description);
        view.setLegend(legend);
        return view;
    }
}
