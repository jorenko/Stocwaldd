package com.fishlevelgames.stocwaldd;

import java.util.ArrayList;
import java.util.Collection;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class PlayArea {
	private Bitmap background = null;
	private Collection<Sprite> sprites = null;
	Rect view = null;
	Rect dest = null;
	
	public PlayArea(Bitmap background, int width, int height) {
		this.background = background;
		this.sprites = new ArrayList<Sprite>();
		this.view = new Rect(0, 0, width, height);
		this.dest = new Rect(0, 0, width, height);
	}
	
	public void setView(int width, int height) {
		view.right += width - dest.right;
		view.bottom += height - dest.bottom;
		dest.right = width;
		dest.bottom = height;
	}
	
	public void addSprite(Sprite s) {
		sprites.add(s);
	}
	
	public void removeSprite(Sprite s) {
		sprites.remove(s);
	}
	
	public void center(int x, int y) {
		view.offsetTo(x - view.width()/2, y - view.height()/2);
	}
	
	public void draw(Canvas c) {
		c.drawBitmap(background, view, dest, null);
		for (Sprite s : sprites) {
			s.draw(c, view);
		}
	}
	
	public Rect getView() {
		return view;
	}

	public int getWidth() {
		return background.getWidth();
	}

	public int getHeight() {
		return background.getHeight();
	}
}
