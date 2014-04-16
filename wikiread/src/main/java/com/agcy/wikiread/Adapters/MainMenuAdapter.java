package com.agcy.wikiread.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.agcy.wikiread.R;

/**
 * Created by kiolt_000 on 16-Apr-14.
 */
public class MainMenuAdapter extends BaseAdapter {

    public static class MenuItem {
        public int drawableId;
        public String title;
        public View.OnClickListener onClickListener;
    }

    public MenuItem[] menuItems;

    private final Context context;

    public MainMenuAdapter(final Context context){
        this.context = context;

        menuItems = new MenuItem[]{
                new MenuItem(){{
                    title = "Feedback";
                    drawableId = R.drawable.heart;
                    onClickListener = new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
                            marketLaunch.setData(Uri.parse("market://details?id=com.agcy.wikiread"));
                            context.startActivity(marketLaunch);
                        }
                    };
                }},
                new MenuItem(){{
                    title = "Share the app";
                    drawableId = R.drawable.megaphone;
                    onClickListener = new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.agcy.wikiread");
                            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Really nice application for Wikipedia on android!");
                            context.startActivity(Intent.createChooser(intent, "Share"));
                        }
                    };
                }},
        };

    }

    @Override
    public int getCount() {


        return menuItems.length;
    }

    @Override
    public Object getItem(int position) {
        return menuItems[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View menuItemView = inflater.inflate(R.layout.list_item_drawer, parent, false);

        MenuItem item = (MenuItem) getItem(position);

        ((TextView) menuItemView.findViewById(R.id.title)).setText(item.title);
        ((ImageView) menuItemView.findViewById(R.id.img)).setImageDrawable(context.getResources().getDrawable(item.drawableId));
        ((ImageView) menuItemView.findViewById(R.id.img)).setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        menuItemView.setOnClickListener(item.onClickListener);

        return menuItemView;

    }


}
