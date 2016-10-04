package pl.dzielins42.bsgbga;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.SimpleExpandableListAdapter;

public class BSGBGARulesActivity extends ExpandableListActivity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    //setContentView(R.layout.rules);
	    
		getExpandableListView().setCacheColorHint(0);
		List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
		List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
		String[] sections = getResources().getStringArray(R.array.rulesSections);
		String[] sectionChildren;
		for (int i = 0;i<sections.length;i++)
		{
			Map<String, String> curGroupMap = new HashMap<String, String>();
			groupData.add(curGroupMap);
			curGroupMap.put("Text",getResources().getStringArray(R.array.rulesSections)[i]);
			// curGroupMap.put(IS_EVEN, (i % 2 == 0) ? "This group is even" :
			// "This group is odd");
			switch (i)
			{
			case 0:
				sectionChildren = getResources().getStringArray(R.array.rulesSection1);
				break;
			case 1:
				sectionChildren = getResources().getStringArray(R.array.rulesSection2);
				break;
			case 2:
				sectionChildren = getResources().getStringArray(R.array.rulesSection3);
				break;
			case 3:
				sectionChildren = getResources().getStringArray(R.array.rulesSection4);
				break;
			case 4:
				sectionChildren = getResources().getStringArray(R.array.rulesSection5);
				break;
			case 5:
				sectionChildren = getResources().getStringArray(R.array.rulesSection6);
				break;
			case 6:
				sectionChildren = getResources().getStringArray(R.array.rulesSection7);
				break;
			default:
				sectionChildren = new String[0];
			}
			List<Map<String, String>> children = new ArrayList<Map<String, String>>();
			for (int j = 0;j < sectionChildren.length;j++)
			{
				Map<String, String> curChildMap = new HashMap<String, String>();
				children.add(curChildMap);
				curChildMap.put("Text",sectionChildren[j]);
				// curChildMap.put(IS_EVEN, (j % 2 == 0) ? "This child is even"
				// : "This child is odd");
			}
			childData.add(children);
		}
	    
	    ExpandableListAdapter expandableListAdapter = new SimpleExpandableListAdapter(this,groupData,android.R.layout.simple_expandable_list_item_1,new String[] {"Text"},new int[] {android.R.id.text1 },childData,R.layout.rules_section,new String[] {"Text"},new int[] {android.R.id.text1});
	    setListAdapter(expandableListAdapter);

	}
	
}
