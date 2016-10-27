package pl.dzielins42.bsgbga;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LinesOfSuccessionFragment extends Fragment {

    private static final String TAG = LinesOfSuccessionFragment.class.getSimpleName();

    private List<LineOfSuccessionFragment> mLosFragments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lines_of_succession, container, false);

        setupView(rootView);

        return rootView;
    }

    private void setupView(@NonNull View rootView) {
        ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
        if (pager != null) {
            mLosFragments = new ArrayList<>(3);
            LineOfSuccessionFragment presidentLosFragment, admiralLosFragment, cagLosFragment;
            presidentLosFragment = LineOfSuccessionFragment.newInstance(R.array.los_president, R
                    .string.los_title_president);
            admiralLosFragment = LineOfSuccessionFragment.newInstance(R.array.los_admiral, R
                    .string.los_title_admiral);
            cagLosFragment = LineOfSuccessionFragment.newInstance(R.array.los_cag, R.string
                    .los_title_cag);
            mLosFragments.add(presidentLosFragment);
            mLosFragments.add(admiralLosFragment);
            mLosFragments.add(cagLosFragment);
            FragmentStatePagerAdapter adapter = new ScreenSlidePagerAdapter(getActivity()
                    .getSupportFragmentManager());
            pager.setAdapter(adapter);
            TabLayout tabs = (TabLayout) rootView.findViewById(R.id.tabs);
            if (tabs != null) {
                tabs.setupWithViewPager(pager);
                if (tabs.getTabAt(0) != null) {
                    //noinspection ConstantConditions
                    setupTab(tabs.getTabAt(0), R.layout.los_tab, R.drawable.president_icon, R
                            .string.los_title_president);
                }
                if (tabs.getTabAt(1) != null) {
                    //noinspection ConstantConditions
                    setupTab(tabs.getTabAt(1), R.layout.los_tab, R.drawable.admiral_icon, R
                            .string.los_title_admiral);
                }
                if (tabs.getTabAt(2) != null) {
                    //noinspection ConstantConditions
                    setupTab(tabs.getTabAt(2), R.layout.los_tab, R.drawable.cag_icon, R.string
                            .los_title_cag);
                }
            }
        } else {
            Log.e(TAG, "setupView: Cannot retrieve ViewPager");
        }
    }

    private void setupTab(@NonNull TabLayout.Tab tab, int customViewResId, int iconResId, int
            titleResId) {
        tab.setCustomView(customViewResId).setIcon(iconResId).setText(titleResId);
    }

    public static class LineOfSuccessionFragment extends Fragment {

        private static final String ARG_CHARACTER_ARRAY_RES_ID = "CHARACTER_ARRAY_RES_ID";
        private static final String ARG_TITLE_RES_ID = "TITLE_RES_ID";

        public static LineOfSuccessionFragment newInstance(int losResId, int titleResId) {
            LineOfSuccessionFragment fragment = new LineOfSuccessionFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_CHARACTER_ARRAY_RES_ID, losResId);
            args.putInt(ARG_TITLE_RES_ID, titleResId);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
                savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_los, container, false);

            setupView(rootView);

            return rootView;
        }

        private void setupView(@NonNull View rootView) {
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
            if (recyclerView != null) {
                List<String> characters = null;
                int charArrayResId = getArguments().getInt(ARG_CHARACTER_ARRAY_RES_ID, 0);
                if (charArrayResId != 0) {
                    characters = loadLos(charArrayResId);
                } else {
                    Log.e(TAG, "setupView: Characters array resource id not provided");
                }
                recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(characters));
            } else {
                Log.e(TAG, "setupView: Cannot find RecyclerView");
            }
        }

        private List<String> loadLos(int losId) {
            Log.d(TAG, "loadLos() called with: " + "losId = [" + losId + "]");
            Resources res = getResources();
            TypedArray typedArray;
            List<String> result;
            typedArray = res.obtainTypedArray(losId);
            result = new ArrayList<>(typedArray.length());
            for (int i = 0; i < typedArray.length(); i++) {
                result.add(res.getString(R.string.los_format, i + 1, typedArray.getString(i)));
            }
            typedArray.recycle();

            return result;
        }

    }

    public static class SimpleItemRecyclerViewAdapter extends RecyclerView
            .Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<String> mValues;

        public SimpleItemRecyclerViewAdapter(List<String> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout
                    .simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SimpleItemRecyclerViewAdapter.ViewHolder holder, int
                position) {
            if (holder.mNameTextView != null) {
                holder.mNameTextView.setText(mValues.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public final TextView mNameTextView;

            public ViewHolder(View view) {
                super(view);
                mNameTextView = (TextView) view.findViewById(android.R.id.text1);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameTextView.getText() + "'";
            }

        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (mLosFragments != null && position < mLosFragments.size()) {
                return mLosFragments.get(position);
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return mLosFragments != null ? mLosFragments.size() : 0;
        }

    }

}
