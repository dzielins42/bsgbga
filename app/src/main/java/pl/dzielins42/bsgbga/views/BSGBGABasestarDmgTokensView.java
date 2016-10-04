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

public class BSGBGABasestarDmgTokensView extends View
{
	private float _tokenDim;
	private float _hOffset;
	private float _vOffset;
	private boolean[] _tokensFaceUp;
	private byte[] _tokensBitmap;
	private Bitmap[] _bitmaps;
	private int _selectedIndex;
	private Rect _selectionRect;
	
	public BSGBGABasestarDmgTokensView(Context context)
	{
		super(context);
		setFocusable(true);
		//setFocusableInTouchMode(true);
		_tokensFaceUp = new boolean[4];
		_tokensBitmap = new byte[4];
		_bitmaps = new Bitmap[5];
		for (int i=0;i<4;++i)
		{
			_tokensFaceUp[i]=false;
			_tokensBitmap[i]=(byte)i;
		}
		_bitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.token_basestar_dmg);
		_bitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.token_basestar_dmg_double);
		_bitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.token_basestar_dmg_raiders);
		_bitmaps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.token_basestar_dmg_rockets);
		_bitmaps[4] = BitmapFactory.decodeResource(getResources(), R.drawable.token_basestar_reverse);
		
		_selectedIndex = 0;
		randomizeBitmaps();
	}
	
	@Override
	protected void onSizeChanged(int w,int h,int oldw,int oldh)
	{
		_tokenDim = Math.min((2 * h) / 7,(2 * w) / 7);
		_hOffset = (w - (2 * _tokenDim)) / 3;
		_vOffset = (h - (2 * _tokenDim)) / 3;
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
		for(int i=0;i<4;++i)
		{
			point = tokenPosition(i);
			matrix = new Matrix();
			if (_tokensFaceUp[i]) bitmap = _bitmaps[_tokensBitmap[i]];
			else bitmap = _bitmaps[4];			
			matrix.postScale((_tokenDim)/bitmap.getWidth(),(_tokenDim)/bitmap.getHeight());
			matrix.postTranslate(point.x,point.y);
			canvas.drawBitmap(bitmap,matrix,new Paint());
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
			if (_selectedIndex<2) _selectedIndex = (_selectedIndex + 2);
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			if (_selectedIndex>=2) _selectedIndex = (_selectedIndex - 2);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if ((_selectedIndex + 1) % 2 != 0)_selectedIndex = (_selectedIndex + 1);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if (_selectedIndex % 2 != 0) _selectedIndex = (_selectedIndex - 1);
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
		r.x = Math.round(_hOffset+((index%2)*(_tokenDim+_hOffset)));
		r.y =Math.round(_vOffset+((float)Math.ceil(index/2)*(_tokenDim+_vOffset)));
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
		if (code=="") return;
		Log.d("pl.dzielins42.BSGBGA",code+":");
		String[] strings = code.split("\\.");
		for (int i=0;i<strings.length;++i)
		{
			if (strings[i]==":") continue;
			_tokensFaceUp[i] = Boolean.valueOf(strings[i].split("\\|")[0]).booleanValue();
			_tokensBitmap[i] = Byte.valueOf(strings[i].split("\\|")[1]).byteValue();
		}
	}
}
