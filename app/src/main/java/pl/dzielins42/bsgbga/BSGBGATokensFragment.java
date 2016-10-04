package pl.dzielins42.bsgbga;

import pl.dzielins42.bsgbga.views.BSGBGABasestarDmgTokensView;
import pl.dzielins42.bsgbga.views.BSGBGAGalaPegDmgTokensView;
import pl.dzielins42.bsgbga.views.BSGBGAGalacticaDmgTokensView;
import pl.dzielins42.bsgbga.views.BSGBGAPegasusDmgTokensView;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class BSGBGATokensFragment extends Fragment implements OnItemSelectedListener
{
	private View[] _views;
	private Spinner _spinner;
	private LinearLayout _layout;
	private static final String TOKENS_STATE = "BSGBGATokensActivity.tokensState";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//BSGBGABasestarDmgTokensView view = new BSGBGABasestarDmgTokensView(this);
		
		_views = new View[4];
		_views[0] = new BSGBGAGalacticaDmgTokensView(getActivity());
		_views[1] = new BSGBGABasestarDmgTokensView(getActivity());
		_views[2] = new BSGBGAPegasusDmgTokensView(getActivity());
		_views[3] = new BSGBGAGalaPegDmgTokensView(getActivity());
		
		View view = inflater.inflate(R.layout.tokens, container, false);
		_spinner = (Spinner)view.findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.tokensModes,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spinner.setAdapter(adapter);
        _spinner.setOnItemSelectedListener(this);
        _layout = (LinearLayout)view.findViewById(R.id.tokensLayout);
        
        loadState(getActivity().getPreferences(Activity.MODE_PRIVATE).getString(TOKENS_STATE,""));
        //((BSGBGAGalacticaDmgTokensView)_views[0]).load(getPreferences(MODE_PRIVATE).getString(GALACTICA_SAVE,""));
        //((BSGBGAGalacticaDmgTokensView)_views[0]).load(getPreferences(MODE_PRIVATE).getString(BASESTAR_SAVE,""));
        
        return view;
	}
	
	private void loadState(String stateCode)
	{
		Log.d("pl.dzielins42.BSGBGA",stateCode);
		if (stateCode=="") return;
		String[] strings = stateCode.split(":");
		for (int i=0;i<strings.length;++i)
		{
			//strings[i] = strings[i].substring(0,strings[i].length());
		}
		Log.d("pl.dzielins42.BSGBGA",stateCode);
		((BSGBGAGalacticaDmgTokensView)_views[0]).load(strings[0]);
		((BSGBGABasestarDmgTokensView)_views[1]).load(strings[1]);
		((BSGBGAPegasusDmgTokensView)_views[2]).load(strings[2]);
		((BSGBGAGalaPegDmgTokensView)_views[3]).load(strings[3]);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		View view = _views[_spinner.getSelectedItemPosition()];
		_layout.removeAllViews();
		_layout.addView(view);
		view.requestFocus();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		String stateCode;
		stateCode = ((BSGBGAGalacticaDmgTokensView)_views[0]).save();
		stateCode += ":";
		stateCode += ((BSGBGABasestarDmgTokensView)_views[1]).save();
		stateCode += ":";
		stateCode += ((BSGBGAPegasusDmgTokensView)_views[2]).save();
		stateCode += ":";
		stateCode += ((BSGBGAGalaPegDmgTokensView)_views[3]).save();
		stateCode += ":";
		//getPreferences(MODE_PRIVATE).edit().putString(GALACTICA_SAVE,((BSGBGAGalacticaDmgTokensView)_views[0]).save()).commit();
		//getPreferences(MODE_PRIVATE).edit().putString(BASESTAR_SAVE,((BSGBGABasestarDmgTokensView)_views[1]).save()).commit();
		getActivity().getPreferences(Activity.MODE_PRIVATE).edit().putString(TOKENS_STATE,stateCode).commit();
	}
}
