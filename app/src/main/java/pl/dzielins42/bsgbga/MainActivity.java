package pl.dzielins42.bsgbga;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import pl.dzielins42.masterdetailflowrevised.AbsMasterDetailActivity;

public class MainActivity extends AbsMasterDetailActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected Fragment getListFragment() {
        return new MenuFragment();
    }

    @Override
    protected Fragment getDetailFragment(String itemId) {
        // getDetailFragment(long) is used
        throw new UnsupportedOperationException();
    }

    @Override
    protected Fragment getDetailFragment(long itemId) {
        int id = (int) itemId;
        switch (id) {
            case 0:
                return new TokensFragment();
            case 1:
                return new LinesOfSuccessionFragment();
            case 2:
                return new LoyaltyDeckFragment();
            case 3:
                return new BSGBGAAttacksFragment();

            default:
                Log.w(TAG, "getDetailFragment: unknown menu item" + String.valueOf(itemId));
                return null;
        }
    }

    @Override
    protected int getMainPanelId() {
        return R.id.container_a;
    }

    @Override
    protected int getDetailPanelId() {
        return R.id.container_b;
    }

}