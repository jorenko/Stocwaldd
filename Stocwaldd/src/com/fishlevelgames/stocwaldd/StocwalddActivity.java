package com.fishlevelgames.stocwaldd;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class StocwalddActivity extends Activity {
	private StocwalddView game = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(getGame());
    }
    
    private StocwalddView getGame() {
    	if (game == null) {
    		game = new StocwalddView(this, null);
    	}
    	return game;
    }
}