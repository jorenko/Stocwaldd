package com.fishlevelgames.stocwaldd;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public abstract class Sprite {
	private Bitmap frames = null;
	private int num_frames;
	private Rect currentFrame = null;
	private RectF currentPos = null;
	
	public Sprite(Bitmap frames, int num_frames) {
		this.frames = frames;
		this.num_frames = num_frames;
		this.currentFrame = new Rect(0, 0, frames.getWidth()/num_frames, frames.getHeight());
		this.currentPos = new RectF(this.currentFrame);
	}
	
	public void move(float x, float y) {
		currentPos.offset(x,  y);
	}
	
	public int getWidth() {
		return (int)currentFrame.width();
	}
	
	public int getHeight() {
		return (int)currentFrame.height();
	}
	
	public float getX() {
		return currentPos.centerX();
	}
	
	public float getY() {
		return currentPos.centerY();
	}

	public void snap(int left, int top, int right, int bottom) {
		if (currentPos.left < left) {
			currentPos.offsetTo(left, currentPos.top);
		}
		if (currentPos.right > right) {
			currentPos.offsetTo(right - currentPos.width(), currentPos.top);
		}
		if (currentPos.top < top) {
			currentPos.offsetTo(currentPos.left, top);
		}
		if (currentPos.bottom > bottom) {
			currentPos.offsetTo(currentPos.left, bottom - currentPos.height());
		}
	}
	
	private RectF drawPos = new RectF();
	private Rect iPos = new Rect();
	public void draw(Canvas c, Rect view) {
		currentPos.round(iPos);
		drawPos.set(currentPos);
		if (iPos.intersect(view)) {
			drawPos.offset(-view.left, -view.top);
			c.drawBitmap(frames, currentFrame, drawPos, null);
		}
	}
}
