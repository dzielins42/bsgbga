package pl.dzielins42.bsgbga;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

public class LoyaltyDeckFragment extends Fragment implements View.OnClickListener, AdapterView
        .OnItemSelectedListener {

    private static final String TAG = LoyaltyDeckFragment.class.getSimpleName();

    private static final int MAX_PLAYERS = 7;
    private static final int DEFAULT_PLAYERS = 4;

    private CheckBox mGaiusCb, mCylonLeaderCb, mBoomerCb, mExodusCb, mDaybreakCb;
    private TextView mResultsTv;
    private int mPlayersCount = 0;

    public LoyaltyDeckFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loyalty_deck, container, false);

        setupView(rootView);

        return rootView;
    }

    private void setupView(@NonNull View rootView) {
        Button button = (Button) rootView.findViewById(R.id.btn_create_deck);
        if (button != null) {
            button.setOnClickListener(this);
        }

        Spinner spinner = (Spinner) rootView.findViewById(R.id.sp_players_count);
        if (spinner != null) {
            String[] items = new String[MAX_PLAYERS];
            for (int i = 0; i < MAX_PLAYERS; i++) {
                items[i] = getResources().getQuantityString(R.plurals.players_count, i + 1, i + 1);
            }
            spinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout
                    .simple_dropdown_item_1line, items));
            spinner.setSelection(DEFAULT_PLAYERS - 1);
            spinner.setOnItemSelectedListener(this);
        }

        mGaiusCb = ((CheckBox) rootView.findViewById(R.id.cb_baltar));
        mBoomerCb = ((CheckBox) rootView.findViewById(R.id.cb_boomer));
        mCylonLeaderCb = ((CheckBox) rootView.findViewById(R.id.cb_cylon_leader));
        mExodusCb = ((CheckBox) rootView.findViewById(R.id.cb_exodus));
        mDaybreakCb = ((CheckBox) rootView.findViewById(R.id.cb_daybreak));
        mResultsTv = (TextView) rootView.findViewById(R.id.tv_result);
        if (mGaiusCb == null || mBoomerCb == null || mCylonLeaderCb == null || mExodusCb == null
                || mDaybreakCb == null || mResultsTv == null) {
            Log.e(TAG, "setupView: Unable to find all widgets");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == null) {
            return;
        }

        switch (v.getId()) {
            case R.id.btn_create_deck:
                String loyaltyDeckString = createLoyaltyDeck();
                Log.d(TAG, "loyaltyDeckString=" + String.valueOf(loyaltyDeckString));
                if (mResultsTv != null) {
                    mResultsTv.setText(loyaltyDeckString);
                }
                break;

            default:
                break;
        }
    }

    private String createLoyaltyDeck() {
        // TODO: 2016-10-28 Instead of expansions use game options (more check boxes but alo more accurate)
        boolean gaiusInGame = mGaiusCb != null && mGaiusCb.isChecked();
        boolean boomerInGame = mBoomerCb != null && mBoomerCb.isChecked();
        boolean cylonLeaderInGame = mCylonLeaderCb != null && mCylonLeaderCb.isChecked();
        boolean exodusRules = mExodusCb != null && mExodusCb.isChecked();
        boolean daybreakRules = mDaybreakCb != null && mDaybreakCb.isChecked();

        int playersCount = mPlayersCount;
        // If Cylon Leader is in the game, there is less human/Cylon players
        if (cylonLeaderInGame) {
            playersCount--;
        }

        int youAreNotACylonCardsCount;
        int youAreACylonCardsCount;
        boolean sympathizerCard = false;

        // Base cards + Sympathizer
        if (playersCount >= 3) {
            youAreNotACylonCardsCount = (int) Math.ceil(playersCount * 2 * 9 / 12.0f);
            youAreACylonCardsCount = (playersCount * 2) - youAreNotACylonCardsCount;
            if (gaiusInGame) {
                youAreNotACylonCardsCount++;
            }
            if (boomerInGame) {
                youAreNotACylonCardsCount++;
            }
            // Sympathizer
            if (playersCount == 4 || playersCount == 6) {
                youAreACylonCardsCount--;
                sympathizerCard = true;
            }
            // Cylon Leader
            if (cylonLeaderInGame) {
                if (!daybreakRules && sympathizerCard) {
                    sympathizerCard = false;
                    youAreNotACylonCardsCount++;
                }
            }
            // Exodus
            if (exodusRules) {
                youAreNotACylonCardsCount++;
            }
            // Daybreak
            if (!daybreakRules && sympathizerCard) {
                youAreNotACylonCardsCount++;
            }
        } else {
            if (playersCount == 1) {
                youAreNotACylonCardsCount = 6;
                youAreACylonCardsCount = 1;
            } else {
                youAreNotACylonCardsCount = 2;
                youAreACylonCardsCount = 1;
            }
        }

        StringBuilder sb = new StringBuilder();

        sb.append(getString(R.string.loyalty_you_are_not_a_cylon, youAreNotACylonCardsCount))
                .append("\n").append(getString(R.string.loyalty_you_are_a_cylon,
                youAreACylonCardsCount));
        if (sympathizerCard) {
            sb.append("\n").append(getString(R.string.loyalty_you_are_a_sympathizer_variant));
        }
        if (cylonLeaderInGame) {
            sb.append("\n");
            if (daybreakRules) {
                sb.append(getString(R.string.loyalty_cylon_leader_motive));
            } else {
                if ((playersCount + 1) % 2 == 0) {
                    sb.append(getString(R.string.loyalty_cylon_leader_sympathetic_agenda));
                } else {
                    sb.append(getString(R.string.loyalty_cylon_leader_hostile_agenda));
                }
            }
        }

        return sb.toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        boolean boomerSelected = mBoomerCb != null && mBoomerCb.isChecked();
        boolean boomerEnabled = true;
        boolean cylonLeaderSelected = mCylonLeaderCb != null && mCylonLeaderCb.isChecked();
        boolean cylonLeaderEnabled = true;

        switch (position) {
            case 0:
            case 1:
                // Force Boomer not in game
                boomerEnabled = false;
                boomerSelected = false;
                // Force Cylon Leader not in game
                cylonLeaderEnabled = false;
                cylonLeaderSelected = false;
                break;
            case 2:
                // Force Cylon Leader not in game
                cylonLeaderEnabled = false;
                cylonLeaderSelected = false;
                break;
            case 6:
                // Force Cylon Leader in game
                cylonLeaderEnabled = false;
                cylonLeaderSelected = true;
                break;

            default:
                break;
        }

        mPlayersCount = position + 1;

        if (mBoomerCb != null) {
            mBoomerCb.setEnabled(boomerEnabled);
            mBoomerCb.setChecked(boomerSelected);
        }
        if (mCylonLeaderCb != null) {
            mCylonLeaderCb.setEnabled(cylonLeaderEnabled);
            mCylonLeaderCb.setChecked(cylonLeaderSelected);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Empty
    }

}
