package com.fishlevelgames.stocwaldd;

import android.graphics.Bitmap;

public class Player extends Sprite {
	private float speed = 0.25f;
	public Player(Bitmap frames, int num_frames) {
		super(frames, num_frames);
	}
	
	public float getSpeed() {
		return speed;
	}
}
