package com.ventus.audiospi;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.util.Log;

/**
 * This class abstracts away the audio parts of the data transfer and gives simple methods
 * like read and write to any client using it.
 * @author abhin
 *
 */
public class SpiManager {
	private static final String TAG = SpiManager.class.getSimpleName();
	private static final short PCM_BIT_LOW = 0;	
	private static final short PCM_BIT_HIGH = 255;
	private static final int SOUND_SIZE = 200;
	private static final int PULSE_WIDTH = 5;
	private static final int HALF_CLOCK_WIDTH = 5;
	private static final int INITIAL_SILENCE = 50;	//the number of samples in the beginning that will be on PCM_BIT_LOW
	
	//Dynamically calculate the minimum buffer size depending on the encoding scheme
	//data rate etc.
	private static final Integer minBufferSize = AudioTrack.getMinBufferSize(
		48000, 
		AudioFormat.CHANNEL_OUT_STEREO, 
		AudioFormat.ENCODING_PCM_8BIT
	);
	//create the audio track
	private final AudioTrack audioTrack = new AudioTrack(
		AudioManager.STREAM_MUSIC, 
		48000,
		AudioFormat.CHANNEL_OUT_STEREO,
		AudioFormat.ENCODING_PCM_8BIT,
		minBufferSize,
		AudioTrack.MODE_STREAM
	);
	//create the buffer that will hold the data to be sent
	private short[] soundBuffer = new short[minBufferSize];
	
	/**
	 * This method merely plays whatever is currently on the sound buffer.
	 */
	private void playSoundBuffer(){
		audioTrack.play();
		audioTrack.write(soundBuffer, 0, minBufferSize);
		audioTrack.stop();
	}
	
	/**
	 * This method will be run by the client to execute some meaningless debugging sounds
	 * to check the voltage levels produced etc.
	 */
//	public void debug(){
//		if(minBufferSize < SOUND_SIZE){
//			Log.e(TAG, "Buffer size too small");
//		}
//		
//		//generate the initial silence
//		for (int index = 0; index < INITIAL_SILENCE; index ++){
//			soundBuffer[index] = PCM_BIT_LOW;
//		}
//		
//		//Generate rectangular pulses
//		short currentLevel = PCM_BIT_LOW;
//		for (int index = INITIAL_SILENCE; index < SOUND_SIZE; index++){
//			if (index % PULSE_WIDTH == 0) {
//				if (currentLevel == PCM_BIT_HIGH)
//					currentLevel = PCM_BIT_LOW;
//				else
//					currentLevel = PCM_BIT_HIGH;
//			}
//			soundBuffer[index] = currentLevel;
//		}
//		
//		//now play the sound
//		playSoundBuffer();
//		
//		//attempt reading (remove later)
//	}
	public void debug(){
		if(minBufferSize < SOUND_SIZE){
			Log.e(TAG, "Buffer size too small");
		}
		short currentLeftLevel = PCM_BIT_LOW;
		short currentRightLevel = PCM_BIT_HIGH;
		
		//generate the initial silence
		for (int index = 0; index < INITIAL_SILENCE; index ++){
			soundBuffer[index] = PCM_BIT_LOW;
		}
		
		for (int index = INITIAL_SILENCE; index < SOUND_SIZE; index++){
			if (index % 2 == 0){
				//maybe the left channel
				if ((index / 2) % HALF_CLOCK_WIDTH == 0){
					//it's time to toggle left level
					if (currentLeftLevel == PCM_BIT_HIGH){
						currentLeftLevel = PCM_BIT_LOW;
					} else {
						currentLeftLevel = PCM_BIT_HIGH;
					}
				}
				soundBuffer[index] = currentLeftLevel;
			} else {
				//maybe the right channel
				//keep the right channel to a steady high for now
				soundBuffer[index] = currentRightLevel;
			}
		}
		
		//now play sound
		playSoundBuffer();
	}

	public void destroy() {
		audioTrack.release();
	}
}
