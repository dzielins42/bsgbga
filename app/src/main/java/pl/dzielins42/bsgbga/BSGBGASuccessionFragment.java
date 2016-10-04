package pl.dzielins42.bsgbga;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

public class BSGBGASuccessionFragment extends Fragment
{
	
	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		View view = inflater.inflate(R.layout.succession, container, false);

	    TabHost tabHost = (TabHost)view.findViewById(android.R.id.tabhost);
	    tabHost.setup();
	    tabHost.addTab(tabHost.newTabSpec("tab_test1").setIndicator(/*getResources().getString(R.string.presidentTitle)*/"",getResources().getDrawable(R.drawable.president_icon)).setContent(R.id.presidentLoS));
	    tabHost.addTab(tabHost.newTabSpec("tab_test2").setIndicator(/*getResources().getString(R.string.admiralTitle)*/"",getResources().getDrawable(R.drawable.admiral_icon)).setContent(R.id.admiralLoS));
	    tabHost.addTab(tabHost.newTabSpec("tab_test3").setIndicator(/*getResources().getString(R.string.CAGTitle)*/"",getResources().getDrawable(R.drawable.cag_icon)).setContent(R.id.cagLoS));
	    //tabHost.addTab(tabHost.newTabSpec("tab_test4").setIndicator(getResources().getString(R.string.infoTitle)).setContent(R.id.cagLoS));
	    //tabHost.addTab(tabHost.newTabSpec("tab_test4").setIndicator(getResources().getString(R.string.infoTitle)).setContent(R.id.cagLoS));
	    tabHost.setCurrentTab(0);
	    return view;
	}
	
}
