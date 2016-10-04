package pl.dzielins42.bsgbga.views;

import java.util.ArrayList;

import pl.dzielins42.bsgbga.R;
import pl.dzielins42.bsgbga.R.color;
import pl.dzielins42.bsgbga.R.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class BSGBGAGalaPegDmgTokensView extends View
{
	private float _tokenDim;
	private float _hOffset;
	private float _vOffset;
	private boolean[] _tokensFaceUp;
	private byte[] _tokensBitmap;
	private Bitmap[] _bitmaps;
	private boolean _horizontal;
	private int _selectedIndex;
	private Rect _selectionRect;
	
	public BSGBGAGalaPegDmgTokensView(Context context)
	{
		super(context);
		setFocusable(true);
		//setFocusableInTouchMode(true);
		_tokensFaceUp = new boolean[12];
		_tokensBitmap = new byte[12];
		_bitmaps = new Bitmap[13];
		for (int i=0;i<12;++i)
		{
			_tokensFaceUp[i]=false;
			_tokensBitmap[i]=(byte)i;
		}
		_bitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.token_battlestar_dmg_admiral);
		_bitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.token_battlestar_dmg_arms);
		_bitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.token_battlestar_dmg_bay);
		_bitmaps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.token_battlestar_dmg_command);
		_bitmaps[4] = BitmapFactory.decodeResource(getResources(), R.drawable.token_battlestar_dmg_fire);
		_bitmaps[5] = BitmapFactory.decodeResource(getResources(), R.drawable.token_battlestar_dmg_ftl);
		_bitmaps[6] = BitmapFactory.decodeResource(getResources(), R.drawable.token_battlestar_dmg_res1);
		_bitmaps[7] = BitmapFactory.decodeResource(getResources(), R.drawable.token_battlestar_dmg_res2);
		_bitmaps[8] = BitmapFactory.decodeResource(getResources(), R.drawable.token_battlestar_dmg_airlock);
		_bitmaps[9] = BitmapFactory.decodeResource(getResources(), R.drawable.token_battlestar_dmg_engine);
		_bitmaps[10] = BitmapFactory.decodeResource(getResources(), R.drawable.token_battlestar_dmg_cic);
		_bitmaps[11] = BitmapFactory.decodeResource(getResources(), R.drawable.token_battlestar_dmg_batteries);
		_bitmaps[12] = BitmapFactory.decodeResource(getResources(), R.drawable.token_battlestar_reverse);
		
		_selectedIndex = 0;
		randomizeBitmaps();
	}
	
	@Override
	protected void onSizeChanged(int w,int h,int oldw,int oldh)
	{
		if (h > w)
		{
			_tokenDim = Math.min((4*h)/13,(2*w)/10);
			_horizontal = false;
			_hOffset = (w - (3 * _tokenDim)) / 4;
			_vOffset = (h - (4 * _tokenDim)) / 5;
		}
		else
		{
			_tokenDim = Math.min((4*w)/13,(2*h)/10);
			_horizontal = true;
			_hOffset = (w - (4 * _tokenDim)) / 5;
			_vOffset = (h - (3 * _tokenDim)) / 4;
		}
		_selectionRect = getSelectionRect(_selectedIndex);
		super.onSizeChanged(w,h,oldw,oldh);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// draw tokens
		Matrix matrix;
		Bitmap bitmap;
		Point point;
		for(int i=0;i<12;++i)
		{
			point = tokenPosition(i);
			matrix = new Matrix();
			if (_tokensFaceUp[i]) bitmap = _bitmaps[_tokensBitmap[i]];
			else bitmap = _bitmaps[12];			
			//matrix.postScale((_tokenDim)/bitmap.getWidth(),(_tokenDim)/bitmap.getHeight());
			matrix.postTranslate(point.x,point.y);
			canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap,(int)_tokenDim,(int)_tokenDim,false),matrix,new Paint());
			// draw selection rectangle if in trackball mode
			if (hasFocus())
			{
				drawSelection(canvas);
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event)
	{
		invalidate(getInvalidateRect(_selectionRect));
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if (_horizontal)
			{
				if (_selectedIndex<8)
					_selectedIndex = (_selectedIndex + 4);
			}
			else
			{
				if (_selectedIndex<9)
					_selectedIndex = (_selectedIndex + 3);
			}
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			if (_horizontal)
			{
				if (_selectedIndex>=4)
					_selectedIndex = (_selectedIndex - 4);
			}
			else
			{
				if (_selectedIndex>=3)
					_selectedIndex = (_selectedIndex - 3);
			}
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (_horizontal)
			{
				if ((_selectedIndex + 1) % 4 != 0)
					_selectedIndex = (_selectedIndex + 1);
			}
			else
			{
				if ((_selectedIndex + 1) % 3 != 0)
					_selectedIndex = (_selectedIndex + 1);
			}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if (_horizontal)
			{
				if (_selectedIndex % 4 != 0)
					_selectedIndex = (_selectedIndex - 1);
			}
			else
			{
				if (_selectedIndex % 3 != 0)
					_selectedIndex = (_selectedIndex - 1);
			}
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			flip(_selectedIndex);
			break;
		default: return super.onKeyDown(keyCode,event);
		}
		_selectionRect = getSelectionRect(_selectedIndex);
		invalidate(getInvalidateRect(_selectionRect));
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return super.onTouchEvent(event);
		invalidate(getInvalidateRect(_selectionRect));
		int x = (int)event.getX();
		int y = (int)event.getY();
		for (int i = 0;i < _tokensFaceUp.length;++i)
			if (getSelectionRect(i).contains(x,y))
			{
				_selectedIndex = i;
				_selectionRect = getSelectionRect(i);
				flip(_selectedIndex);
				invalidate(getInvalidateRect(_selectionRect));
				break;
			}
		return true;
	}
	
	private void flip(int index)
	{
		_tokensFaceUp[index] = !_tokensFaceUp[index];
		if (!_tokensFaceUp[index]) randomizeBitmaps();
	}
	
	private Rect getInvalidateRect(Rect selectionRect)
	{
		Rect rect = new Rect(selectionRect);
		rect.top-=_vOffset/2;
		rect.bottom+=_vOffset/2;
		rect.right+=_hOffset/2;
		rect.left-=_hOffset/2;
		return rect;
	}
	
	private Rect getSelectionRect(int index)
	{
		Point point = tokenPosition(index);
		Rect rect = new Rect(point.x,point.y,Math.round(point.x+_tokenDim),Math.round(point.y+_tokenDim));
		return rect;
	}
	
	private void drawSelection(Canvas canvas)
	{
		Paint paint = new Paint();
		paint.setColor(getResources().getColor(R.color.tokenSelectColor));
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(_selectionRect,paint);
	}
	
	private Point tokenPosition(int index)
	{
		Point r = new Point();
		if (_horizontal)
		{
			r.x = Math.round(_hOffset+((index%4)*(_tokenDim+_hOffset)));
			r.y =Math.round(_vOffset+((float)Math.ceil(index/4)*(_tokenDim+_vOffset)));
		}
		else
		{
			r.x =Math.round(_hOffset+((index%3)*(_tokenDim+_hOffset)));
			r.y = Math.round(_vOffset+((float)Math.ceil(index/3)*(_tokenDim+_vOffset)));
		}
		return r;
	}
	
	private void randomizeBitmaps()
	{
		// creating list of avaible bitmaps
		ArrayList<Byte> list = new ArrayList<Byte>();
		int rand;
		for(int i=0;i<_tokensFaceUp.length;++i)
		{
			if (!_tokensFaceUp[i]) list.add(_tokensBitmap[i]);
		}
		for (int i=0;i<_tokensFaceUp.length;++i)
		{
			if (!_tokensFaceUp[i])
			{
				rand = (int)(list.size()*Math.random());
				_tokensBitmap[i] = list.get(rand);
				list.remove(rand);
			}
		}
	}
	
	public String save()
	{
		String r = "";
		for (int i=0;i<_tokensFaceUp.length;++i)
		{
			r+=String.valueOf(_tokensFaceUp[i])+"|"+String.valueOf(_tokensBitmap[i])+".";
		}
		return r;
	}
	
	public void load(String code)
	{
		if (code=="" || code.isEmpty()) return;
		Log.d("pl.dzielins42.BSGBGA",code+":");
		String[] strings = code.split("\\.");
		//Log.d("pl.dzielins42.BSGBGA",String.valueOf(strings.length));
		for (int i=0;i<strings.length;++i)
		{
			if (strings[i]==":") continue;
			_tokensFaceUp[i] = Boolean.valueOf(strings[i].split("\\|")[0]).booleanValue();
			_tokensBitmap[i] = Byte.valueOf(strings[i].split("\\|")[1]).byteValue();
			//Log.d("pl.dzielins42.BSGBGA",String.valueOf(_tokensFaceUp[i])+", "+String.valueOf(_tokensBitmap[i]));
		}
	}
}
