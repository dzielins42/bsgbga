package pl.dzielins42.bsgbga;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

public class BSGBGALoyaltyFragment extends Fragment implements OnClickListener, OnItemSelectedListener
{
	private CheckBox _gaiusChck;
	private CheckBox _sharonChck;
	private CheckBox _cylonLeaderChck;
	private CheckBox _exodusChck;
	private CheckBox _daybreakChck;
	private TextView _cardsTxtView;
	
	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.loyalty,container,false);
		view.findViewById(R.id.button1).setOnClickListener(this);
		Spinner spinner = (Spinner)view.findViewById(R.id.spinner1);
		spinner.setOnItemSelectedListener(this);
		_gaiusChck = ((CheckBox)view.findViewById(R.id.checkBox1));
		_sharonChck = ((CheckBox)view.findViewById(R.id.checkBox2));
		_cylonLeaderChck = ((CheckBox)view.findViewById(R.id.checkBox3));
		_exodusChck = ((CheckBox)view.findViewById(R.id.checkBox4));
		_daybreakChck = ((CheckBox)view.findViewById(R.id.checkBox5));
		_cardsTxtView = (TextView)view.findViewById(R.id.textView3);
		_cardsTxtView.setText("\n\n\n");
		return view;
	}
	
	private void createLoyaltyDeck()
	{
		
		int playersCount = ((Spinner)getView().findViewById(R.id.spinner1)).getSelectedItemPosition()+1;
		// if Cylon Leader is in game there is less human/cylon players
		if (_cylonLeaderChck.isChecked()) playersCount--;
		short spareLines = 0;
		
		int youAreNotACylonCardsCount = 0;
		int youAreACylonCardsCount = 0;
		boolean symphatizerCard = false;		
		boolean specialRules = false;
		
		if (playersCount >= 3)
		{
			youAreNotACylonCardsCount = (int)Math.ceil(playersCount * 2 * 9 / 12.0f);
			youAreACylonCardsCount = (playersCount * 2) - youAreNotACylonCardsCount;
			if (_gaiusChck.isChecked())
				youAreNotACylonCardsCount++;
			if (_sharonChck.isChecked())
				youAreNotACylonCardsCount++;
			// symphatizer
			if (playersCount == 4 || playersCount == 6)
			{
				youAreACylonCardsCount--;
				symphatizerCard = true;
			}
			// cylon leader
			if (_cylonLeaderChck.isChecked())
			{
				if (!_daybreakChck.isChecked() && symphatizerCard)
				{
					symphatizerCard = false;
					youAreNotACylonCardsCount++;
				}
			}
			// exodus
			if (_exodusChck.isChecked())
				youAreNotACylonCardsCount++;
			// daybreak
			if (_daybreakChck.isChecked() && symphatizerCard)
				youAreNotACylonCardsCount++;
		}
		else
		{
			specialRules = true;
			if (playersCount == 1)
			{
				youAreNotACylonCardsCount = 6;
				youAreACylonCardsCount = 1;
			}
			else
			{
				youAreNotACylonCardsCount = 2;
				youAreACylonCardsCount = 1;
			}
		}
		
		_cardsTxtView.setText(String.valueOf(youAreACylonCardsCount)+"x "+getResources().getString(R.string.youAreACylon)+"\n"+String.valueOf(youAreNotACylonCardsCount)+"x "+getResources().getString(R.string.youAreNotACylon));
		if (_cylonLeaderChck.isChecked())
		{
			if (_daybreakChck.isChecked())
			{
				_cardsTxtView.setText(_cardsTxtView.getText()+"\n"+getResources().getString(R.string.motive));
			}
			else 
			{
				if ((playersCount+1)%2==0)
					_cardsTxtView.setText(_cardsTxtView.getText()+"\n"+getResources().getString(R.string.agendaSympathetic));
				else 
					_cardsTxtView.setText(_cardsTxtView.getText()+"\n"+getResources().getString(R.string.agendaHostile));
			}
		}
		else spareLines++;
		if (symphatizerCard)
		{
			if (!_daybreakChck.isChecked())
				_cardsTxtView.setText(_cardsTxtView.getText()+"\n+1 "+getResources().getString(R.string.youAreASympathizer));
			else
				_cardsTxtView.setText(_cardsTxtView.getText()+"\n+1 "+getResources().getString(R.string.youAreAMutineer));
		}
		else spareLines++;
		for (int i=0;i<spareLines;++i) _cardsTxtView.setText(_cardsTxtView.getText()+"\n");
		
		if (specialRules)
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			alertDialogBuilder.setTitle(getResources().getString(R.string.specialRules));
			if (playersCount == 1)
				alertDialogBuilder.setMessage(getResources().getString(R.string.onePlayerSR));
			else if (playersCount == 2)
				alertDialogBuilder.setMessage(getResources().getString(R.string.twoPlayersSR));
			alertDialogBuilder.setNeutralButton("OK",new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					return;
				}
			});
			alertDialogBuilder.show();
		}
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
		case R.id.button1:
			createLoyaltyDeck();
			break;
		/*case R.id.checkBox4:
			_daybreakChck.setChecked(false);
			break;
		case R.id.checkBox5:
			_exodusChck.setChecked(false);
			break;*/
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		switch (arg2)
		{
		case 0:
		case 1:
			// force sharon valerii not in game
			_sharonChck.setChecked(false);
			_sharonChck.setEnabled(false);
			// force cylon leader not in game
			_cylonLeaderChck.setChecked(false);
			_cylonLeaderChck.setEnabled(false);
			break;
		case 2:
			_sharonChck.setChecked(false);
			_sharonChck.setEnabled(true);
			// force cylon leader not in game
			_cylonLeaderChck.setChecked(false);
			_cylonLeaderChck.setEnabled(false);
			break;
		case 6:
			_sharonChck.setChecked(false);
			_sharonChck.setEnabled(true);
			// force cylon leader in game
			_cylonLeaderChck.setChecked(true);
			_cylonLeaderChck.setEnabled(false);
			break;
		default:
			_cylonLeaderChck.setChecked(false);
			_cylonLeaderChck.setEnabled(true);
			_sharonChck.setChecked(false);
			_sharonChck.setEnabled(true);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
				
	}
	
}
