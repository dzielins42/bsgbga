package pl.dzielins42.bsgbga;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class BSGBGAMenuFragment extends ListFragment
{
	private BSGBGAListItem[] _items;
	private OnMenuItemSelectedListener listener;
	
	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.menu, container, false);

		BSGBGAListItem[] itemsArray = 
		{ 
				new BSGBGAListItem(getResources().getString(R.string.tokensTitle),BSGBGATokensFragment.class,getResources().getString(R.string.tokensDesc), false),
				new BSGBGAListItem(getResources().getString(R.string.successionTitle),BSGBGASuccessionFragment.class,getResources().getString(R.string.successionDesc), false),
				new BSGBGAListItem(getResources().getString(R.string.loyaltyTitle),BSGBGALoyaltyFragment.class,getResources().getString(R.string.loyaltyDesc), false),
				new BSGBGAListItem(getResources().getString(R.string.attacksTitle),BSGBGAAttacksFragment.class,getResources().getString(R.string.attacksDesc), false),
				new BSGBGAListItem(getResources().getString(R.string.infoTitle),BSGBGAInfoActivity.class,getResources().getString(R.string.infoDesc), true)
				//new BSGBGAListItem(getResources().getString(R.string.rulesTitle),BSGBGARulesActivity.class,getResources().getString(R.string.rulesDesc))
		};
		_items = itemsArray;
		ArrayList<HashMap<String, String>> itemsList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item;
		for (int i = 0;i < _items.length;++i)
		{
			item = new HashMap<String,String>();
			item.put("title",_items[i].title);
			item.put("desc",_items[i].description);
			itemsList.add(item);
		}		
		String[] columns = new String[] {"title","desc"};
		int[] renderTo = new int[] {android.R.id.text1,android.R.id.text2};
		ListAdapter listAdapter = new SimpleAdapter(getActivity(), itemsList, android.R.layout.simple_list_item_2, columns, renderTo);
		setListAdapter(listAdapter);
		
		TextView link = (TextView)view.findViewById(R.id.textView1);
		//linkView.setPadding(10, 0, 10, 20);
		//linkView.setTextSize(20);
		//linkView.setText(getString(R.string.github_url));
		//linkView.setTextColor(0xaaffffff);
		//linkView.setLinkTextColor(0xaaffffff);		
		//Linkify.addLinks(link, Linkify.WEB_URLS);
		link.setMovementMethod(LinkMovementMethod.getInstance());
		
		

		//Intent i = new Intent(this,BSGBGATokensActivity.class);
		//Intent i = new Intent(this,Obj3DView.class);
		//startActivity(i);
		return view;
	}
	
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id)
	{
		if(listener!=null)
			listener.onMenuItemSelected(_items[position]);
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnMenuItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMenuItemSelectedListener");
        }
    }
	
	public class BSGBGAListItem {
		public String title;
		public Class<?> activity;
		public String description;
		public boolean popUp;

		public BSGBGAListItem(String title, Class<?> activity, String description, boolean popUp) {
			this.title = title;
			this.activity = activity;
			this.description = description;
			this.popUp = popUp;
		}
	}
	
	public interface OnMenuItemSelectedListener {
        public void onMenuItemSelected(BSGBGAListItem item);
    }
}