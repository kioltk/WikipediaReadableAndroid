package com.agcy.wikiread.Core.Parsing.Nodes;

import android.content.Context;
import android.text.Spannable;
import android.view.View;

import com.agcy.wikiread.Views.PictureView;

/**
 * Created by kiolt_000 on 07.03.14.
 */
public class PictureNode extends Node {

    String imageName;
    Spannable description;
    public PictureNode(String imageName, Spannable description){
        this.imageName = imageName;
        this.description = description;
    }

    @Override
    public View getView(Context context) {

        PictureView view = new PictureView(context);
        view.setImageName(imageName);
        view.setDescription(description);
        return view;
    }
}
