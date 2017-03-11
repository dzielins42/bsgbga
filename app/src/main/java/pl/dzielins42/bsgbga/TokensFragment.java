package pl.dzielins42.bsgbga;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import pl.dzielins42.bsgbga.views.FlipAnimation;

public class TokensFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = TokensFragment.class.getSimpleName();

    private static final String KEY_TOKEN_CONFIG = "pl.dzielins42.bsgbga.key.TOKEN_CONFIG";
    private static final String KEY_MODE_SELECTION = "pl.dzielins42.bsgbga.key.MODE_SELECTION";

    private enum TokenType {GALACTICA_DMG, BASESTAR_DMG, PEGASUS_DMG, GALACTICA_PEGASUS_DMG}

    private GridLayoutManager mGridLayoutManager;
    private Spinner mSpinner;
    private RecyclerView mRecyclerView;
    private ArrayList<Token> mTokens;
    private boolean mUseSavedTokenConfig = false;
    private int mSavedSelection = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_tokens, container, false);

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_TOKEN_CONFIG)) {
            mTokens = savedInstanceState.getParcelableArrayList(KEY_TOKEN_CONFIG);
            mUseSavedTokenConfig = true;
            mSavedSelection = savedInstanceState.getInt(KEY_MODE_SELECTION, mSavedSelection);
        }

        setupView(rootView);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save current configuration of tokens
        outState.putParcelableArrayList(KEY_TOKEN_CONFIG, mTokens);
        // Save currently selected mode
        if (mSpinner != null) {
            outState.putInt(KEY_MODE_SELECTION, mSpinner.getSelectedItemPosition());
        }
    }

    private void setupView(@NonNull View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        if (mRecyclerView != null) {
            mGridLayoutManager = new GridLayoutManager(getContext(), 4);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            mRecyclerView.setAdapter(new SimpleItemRecyclerViewAdapter());
        } else {
            Log.e(TAG, "setupView: Cannot find RecyclerView");
        }
        mSpinner = (Spinner) rootView.findViewById(R.id.sp_mode);
        if (mSpinner != null) {
            mSpinner.setSelection(mSavedSelection);
            mSpinner.setOnItemSelectedListener(this);
        } else {
            Log.e(TAG, "setupView: Cannot find RecyclerView");
        }
    }

    private void randomizeTokens() {
        // Get not fixed elements
        List<Token> unfixed = new ArrayList<>();
        for (Token token : mTokens) {
            if (!token.fixed) {
                unfixed.add(token);
            }
        }
        // Randomize list of unfixed tokens
        Collections.shuffle(unfixed);
        // Put back into main list
        ListIterator<Token> it1 = mTokens.listIterator();
        ListIterator<Token> it2 = unfixed.listIterator();
        Token token;
        while (it1.hasNext()) {
            token = it1.next();
            if (!token.fixed) {
                it1.previous();
                it1.set(it2.next());
                it1.next();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected: position=" + String.valueOf(position) + ", id=" + String.valueOf(id));
        TokenType tokenType = TokenType.values()[position];
        int cols = 4;
        switch (tokenType) {
            case BASESTAR_DMG:
            case PEGASUS_DMG:
                cols = 2;
                break;
            case GALACTICA_PEGASUS_DMG:
                cols = 3;
                break;
            case GALACTICA_DMG:
                cols = 4;
                break;
        }
        mGridLayoutManager.setSpanCount(cols);
        if (!mUseSavedTokenConfig) {
            mTokens = getTokens(tokenType);
            randomizeTokens();
        } else {
            mUseSavedTokenConfig = false;
        }
        mRecyclerView.setAdapter(new SimpleItemRecyclerViewAdapter());
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "onNothingSelected");
    }

    private ArrayList<Token> getTokens(TokenType type) {
        int back = 0;
        int[] fronts = null;
        switch (type) {
            case GALACTICA_DMG:
                fronts = new int[]{
                        R.drawable.token_battlestar_dmg_admiral,
                        R.drawable.token_battlestar_dmg_arms,
                        R.drawable.token_battlestar_dmg_bay,
                        R.drawable.token_battlestar_dmg_command,
                        R.drawable.token_battlestar_dmg_fire,
                        R.drawable.token_battlestar_dmg_ftl,
                        R.drawable.token_battlestar_dmg_res1,
                        R.drawable.token_battlestar_dmg_res2
                };
                back = R.drawable.token_battlestar_reverse;
                break;
            case BASESTAR_DMG:
                fronts = new int[]{
                        R.drawable.token_basestar_dmg,
                        R.drawable.token_basestar_dmg_double,
                        R.drawable.token_basestar_dmg_raiders,
                        R.drawable.token_basestar_dmg_rockets
                };
                back = R.drawable.token_basestar_reverse;
                break;
            case PEGASUS_DMG:
                fronts = new int[]{
                        R.drawable.token_battlestar_dmg_airlock,
                        R.drawable.token_battlestar_dmg_batteries,
                        R.drawable.token_battlestar_dmg_cic,
                        R.drawable.token_battlestar_dmg_engine
                };
                back = R.drawable.token_battlestar_reverse;
                break;
            case GALACTICA_PEGASUS_DMG:
                fronts = new int[]{
                        R.drawable.token_battlestar_dmg_admiral,
                        R.drawable.token_battlestar_dmg_arms,
                        R.drawable.token_battlestar_dmg_bay,
                        R.drawable.token_battlestar_dmg_command,
                        R.drawable.token_battlestar_dmg_fire,
                        R.drawable.token_battlestar_dmg_ftl,
                        R.drawable.token_battlestar_dmg_res1,
                        R.drawable.token_battlestar_dmg_res2,
                        R.drawable.token_battlestar_dmg_airlock,
                        R.drawable.token_battlestar_dmg_batteries,
                        R.drawable.token_battlestar_dmg_cic,
                        R.drawable.token_battlestar_dmg_engine
                };
                back = R.drawable.token_battlestar_reverse;
                break;
        }

        ArrayList<Token> list = new ArrayList<>(fronts.length);
        for (int front : fronts) {
            list.add(new Token(false, back, front));
        }

        return list;
    }

    private static class Token implements Parcelable {
        public static final Parcelable.Creator<Token> CREATOR
                = new Parcelable.Creator<Token>() {
            public Token createFromParcel(Parcel in) {
                return new Token(in);
            }

            public Token[] newArray(int size) {
                return new Token[size];
            }
        };


        int id;
        // If fixed is true then front image should be displayed
        boolean fixed;
        // All instances of Token have same back image
        static int backImgId;
        int frontImgId;

        private Token(Parcel in) {
            this.id = in.readInt();
            this.fixed = in.readInt() == 1;
            Token.backImgId = in.readInt();
            this.frontImgId = in.readInt();
        }

        public Token(boolean fixed, int backImgId, int frontImgId) {
            this(frontImgId, fixed, backImgId, frontImgId);
        }

        public Token(int id, boolean fixed, int backImgId, int frontImgId) {
            this.id = id;
            this.fixed = fixed;
            Token.backImgId = backImgId;
            this.frontImgId = frontImgId;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeInt(fixed ? 1 : 0);
            dest.writeInt(backImgId);
            dest.writeInt(frontImgId);
        }
    }

    public class SimpleItemRecyclerViewAdapter extends RecyclerView
            .Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        SimpleItemRecyclerViewAdapter() {
        }

        @Override
        public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_token, parent, false);

            return new SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SimpleItemRecyclerViewAdapter.ViewHolder holder, int
                position) {
            holder.setData();
        }

        @Override
        public int getItemCount() {
            return mTokens != null ? mTokens.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private final ImageView mFrontImageView, mBackImageView;
            private final ViewSwitcher mViewSwitcher;

            ViewHolder(View view) {
                super(view);

                mViewSwitcher = (ViewSwitcher) view.findViewById(R.id.vs_container);
                mFrontImageView = (ImageView) view.findViewById(R.id.iv_front);
                mBackImageView = (ImageView) view.findViewById(R.id.iv_back);

                if (mViewSwitcher == null || mFrontImageView == null || mBackImageView == null) {
                    throw new IllegalStateException("Cannot load View");
                }

                Animation in = FlipAnimation.leftIn(getContext());
                Animation out = FlipAnimation.leftOut(getContext());
                in.setDuration(400);
                out.setDuration(400);
                mViewSwitcher.setInAnimation(in);
                mViewSwitcher.setOutAnimation(out);
                mViewSwitcher.setOnClickListener(this);
            }

            void setData() {
                Token token = mTokens.get(getAdapterPosition());
                if (token.fixed) {
                    mFrontImageView.setImageResource(token.frontImgId);
                    // Temporary disable animations
                    Animation in = mViewSwitcher.getInAnimation();
                    Animation out = mViewSwitcher.getOutAnimation();
                    mViewSwitcher.setInAnimation(null);
                    mViewSwitcher.setOutAnimation(null);
                    mViewSwitcher.setDisplayedChild(1);
                    mViewSwitcher.setInAnimation(in);
                    mViewSwitcher.setOutAnimation(out);
                }else{
                    mFrontImageView.setImageResource(Token.backImgId);
                }
                mBackImageView.setImageResource(Token.backImgId);
            }

            @Override
            public void onClick(View v) {
                Token token = mTokens.get(getAdapterPosition());
                mFrontImageView.setImageResource(token.frontImgId);
                mViewSwitcher.showNext();
                token.fixed = !token.fixed;
                randomizeTokens();
            }

        }
    }

}
