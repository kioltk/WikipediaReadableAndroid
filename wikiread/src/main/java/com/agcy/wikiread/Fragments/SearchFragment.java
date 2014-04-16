package com.agcy.wikiread.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.agcy.wikiread.Core.Api.SearchItem;
import com.agcy.wikiread.PageActivity;
import com.agcy.wikiread.R;

import java.util.ArrayList;

/**
 * Created by kiolt_000 on 19.03.14.
 */
public class SearchFragment extends Fragment {
    private final Context context;
    private ArrayList<SearchItem> items;
    private ListView rootView;

    public SearchFragment(ArrayList<SearchItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        rootView = (ListView) inflater.inflate(R.layout.fragment_search, container, false);

        rootView.setAdapter(new ListAdapter() {

            @Override
            public boolean areAllItemsEnabled() {
                return true;
            }

            @Override
            public boolean isEnabled(int position) {
                return true;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return items.size();
            }

            @Override
            public Object getItem(int position) {
                return items.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View itemView = inflater.inflate(R.layout.search_item, parent, false);

                TextView titleView = (TextView) itemView.findViewById(R.id.title);
                TextView descriptionView = (TextView) itemView.findViewById(R.id.description);

                SearchItem item = (SearchItem) getItem(position);
                if (item.image != null) {
                    //todo: image?
                }
                titleView.setText(String.valueOf(item.title.content));
                if (item.description.content != null)
                    descriptionView.setText(String.valueOf(item.description.content));
                else
                    descriptionView.setVisibility(View.GONE);
                return itemView;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        });
        rootView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, PageActivity.class);
                intent.setData(Uri.parse(items.get(0).url.content));
                startActivity(intent);
            }
        });
        return rootView;
    }

}
