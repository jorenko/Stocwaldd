package com.fishlevelgames.stocwaldd;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class StocwalddView extends SurfaceView implements Callback {
	private GameThread thread = null;
	private long lastTime = 0;
	private PointF touch;
	private PlayArea area = null;
	private Player player = null;

	public StocwalddView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		area = new PlayArea(BitmapFactory.decodeResource(getResources(), R.drawable.test_area), getWidth(), getHeight());
		player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.test_player), 1);
		area.addSprite(player);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		area.setView(width, height);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		area.setView(getWidth(), getHeight());
		player.move((getWidth() - player.getWidth())/2, (getHeight() - player.getHeight())/2);
		thread = new GameThread();
		thread.setRunning(true);
		thread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		thread.setRunning(false);
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touch = new PointF(event.getX(), event.getY());
			return true;
		case MotionEvent.ACTION_MOVE:
			touch.x = event.getX();
			touch.y = event.getY();
			return true;
		case MotionEvent.ACTION_UP:
			touch = null;
			return true;
		}
		return false;
	}

	public void update(long ms) {
		long elapsed = ms - lastTime;
		
		if (touch != null) {
			// find the angle from the player to the touch point
			double x = area.getView().left + touch.x - player.getX();
			double y = area.getView().top + touch.y - player.getY();
			double angle = Math.atan2(y, x);

			// calculate the vector components of the movement speed at that angle
			double d = elapsed * player.getSpeed();
			x = d * Math.cos(angle);
			y = d * Math.sin(angle);

			
			// slide the player by that much
			player.move((float)x, (float)y);			
			// limit movement to within the play area
			player.snap(0, 0, area.getWidth(), area.getHeight());
			// re-center the screen on him
			area.center((int)player.getX(), (int)player.getY());
		}
		
		lastTime = ms;
	}
	
	public void draw(Canvas c) {
		c.drawRGB(0, 0, 0);
		area.draw(c);
	}

	/**
	 * Pauses the game.
	 */
	public void pause() {
		thread.setRunning(false);
		this.setVisibility(INVISIBLE);
	}
	
	/**
	 * Resumes a paused game.
	 */
	public void resume() {
		thread.setRunning(true);
		this.setVisibility(VISIBLE);
	}
	
	public class GameThread extends Thread {
		private boolean run = false;
		
		public void setRunning(boolean value) {
			lastTime = System.currentTimeMillis();
			run = value;
		}
		
		@Override
		public void run() {
			Canvas c;
			while (run) {
				c = null;
				update(System.currentTimeMillis());
				
				try {
					c = getHolder().lockCanvas(null);
					synchronized (getHolder()) {
						draw(c);
					}
				} finally {
					if (c != null) {
						getHolder().unlockCanvasAndPost(c);
					}
				}
			}
		}
	}
}
