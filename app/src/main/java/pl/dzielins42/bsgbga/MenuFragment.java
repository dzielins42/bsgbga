package pl.dzielins42.bsgbga;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.dzielins42.masterdetailflowrevised.AbsMasterDetailActivity;

public class MenuFragment extends Fragment {

    private static final String TAG = MenuFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);

        View recyclerView = rootView.findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        return rootView;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(getMenuItems()));
    }

    private List<MenuItem> getMenuItems() {
        List<MenuItem> result = new ArrayList<>();

        Resources res = getResources();
        TypedArray typedArray;
        String[] titles, descriptions;
        // Get titles
        typedArray = res.obtainTypedArray(R.array.menu_titles);
        titles = new String[typedArray.length()];
        for (int i = 0; i < typedArray.length(); i++) {
            titles[i] = typedArray.getString(i);
        }
        typedArray.recycle();
        // Get Descriptions
        typedArray = res.obtainTypedArray(R.array.menu_descriptions);
        descriptions = new String[typedArray.length()];
        for (int i = 0; i < typedArray.length(); i++) {
            descriptions[i] = typedArray.getString(i);
        }
        typedArray.recycle();

        if (titles.length != descriptions.length) {
            Log.w(TAG, "getMenuItems: lengths of arrays not equal");
        }

        int n = titles.length > descriptions.length ? descriptions.length : titles.length;
        for (int i = 0; i < n; i++) {
            result.add(new MenuItem(i, titles[i], descriptions[i]));
        }

        return result;
    }

    public class SimpleItemRecyclerViewAdapter extends RecyclerView
            .Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<MenuItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<MenuItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout
                    .simple_list_item_2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SimpleItemRecyclerViewAdapter.ViewHolder holder, int
                position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).mTitle);
            holder.mContentView.setText(mValues.get(position).mDescription);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = getActivity();
                    if (activity != null && activity instanceof AbsMasterDetailActivity) {
                        ((AbsMasterDetailActivity) getActivity()).onItemSelected(holder.mItem.mId);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public MenuItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(android.R.id.text1);
                mContentView = (TextView) view.findViewById(android.R.id.text2);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    private class MenuItem {
        String mTitle, mDescription;
        int mId;

        public MenuItem(int id, String title, String description) {
            this.mId = id;
            this.mTitle = title;
            this.mDescription = description;
        }
    }

}