package pl.dzielins42.bsgbga;

import pl.dzielins42.bsgbga.BSGBGAMenuFragment.BSGBGAListItem;
import pl.dzielins42.bsgbga.BSGBGAMenuFragment.OnMenuItemSelectedListener;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.FrameLayout;

public class BSGBGAMainActivity extends FragmentActivity implements OnMenuItemSelectedListener
{
	private FrameLayout firstContainer, secondContainer;
	private boolean singleFragmentActivity = false;
	private final String INITIALIZED_KEY = "initialized";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// recognize screen
		setContentView(R.layout.main);
		firstContainer = (FrameLayout)findViewById(R.id.firstFragmentContainer);
		secondContainer = (FrameLayout)findViewById(R.id.secondFragmentContainer);
		if (savedInstanceState==null || !savedInstanceState.getBoolean(INITIALIZED_KEY,false))
		{
			if (firstContainer != null && secondContainer == null)
			// it is a handheld
			{
				singleFragmentActivity = true;
				Fragment menuFragment = new BSGBGAMenuFragment();
				menuFragment.setArguments(getIntent().getExtras());
				getSupportFragmentManager().beginTransaction().replace(R.id.firstFragmentContainer,menuFragment).commit();
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
			}
			else if (firstContainer != null && secondContainer != null)
			// it is a tablet
			{
				singleFragmentActivity = false;
				Fragment menuFragment = new BSGBGAMenuFragment();
				menuFragment.setArguments(getIntent().getExtras());
				getSupportFragmentManager().beginTransaction().replace(R.id.firstFragmentContainer,menuFragment).commit();
				Fragment loyaltyFragment = new BSGBGALoyaltyFragment();
				loyaltyFragment.setArguments(getIntent().getExtras());
				getSupportFragmentManager().beginTransaction().replace(R.id.secondFragmentContainer,loyaltyFragment).commit();
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
			}
			else
			// sum ting wong
			{
				finish();
			}
		}
	}
	
	@Override
	protected void onSaveInstanceState (Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(INITIALIZED_KEY,true);
		Log.d("TEST","saved");
	}

	@Override
	public void onMenuItemSelected(BSGBGAListItem item)
	{
		if (item.popUp)
		{
			Intent intent = new Intent(this, item.activity);
			this.startActivity(intent);
		}
		else
		{
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			try
			{
				Fragment newFragment;
				if (singleFragmentActivity)
				{
					newFragment = (Fragment)item.activity.newInstance();
					newFragment.setArguments(getIntent().getExtras());
					transaction.replace(R.id.firstFragmentContainer,newFragment);
					transaction.addToBackStack(null);
				}
				else
				{
					newFragment = (Fragment)item.activity.newInstance();
					newFragment.setArguments(getIntent().getExtras());
					transaction.replace(R.id.secondFragmentContainer,newFragment);
				}
			} catch (InstantiationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally
			{
				transaction.commit();
			}
		}
	}
}
