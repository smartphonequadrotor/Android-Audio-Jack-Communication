package com.ventus.audiospi;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	Button playSoundBtn;
	SpiManager spiManager;
	AudioManager audioManager;
	int oldRingerMode;
	
    /**
     * Called when the activity is first started. The tasks performed are
     * <ol>
     * 	<li>Creation of a {@link SpiManager} instance.</li>
     * 	<li>Acquisition of a {@link AudioManager} instance.</li>
     * 	<li>Setting of the listeners for the buttons.</li>
     * </ol>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        playSoundBtn = (Button) findViewById(R.id.playSoundBtn);
        spiManager = new SpiManager();
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        
        //set the listeners for the buttons
        setListeners();
    }
    
    @Override
    public void onResume(){
        super.onResume();
    	//set the ringer mode of the phone to silent
        oldRingerMode = audioManager.getRingerMode();	//so that the ringer mode can be re-instated once the application is done
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

	private void setListeners() {
		playSoundBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				spiManager.debug();
			}
		});
	}
	
	public void onPause(){
		//put the phone back to its original state
		audioManager.setRingerMode(oldRingerMode);
		super.onPause();
	}
	
	public void onDestroy(){
		spiManager.destroy();
	}
}