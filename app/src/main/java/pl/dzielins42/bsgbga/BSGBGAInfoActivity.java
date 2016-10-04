package pl.dzielins42.bsgbga;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BSGBGAInfoActivity extends Activity implements OnClickListener
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.info);
	    ((Button)findViewById(R.id.button1)).setOnClickListener(this);
	}

	@Override
	public void onClick(View view)
	{
		if (view.getId()==R.id.button1)
		{
			finish();
		}
	}
	
}
