/*
  AudioSerial.java - Audio serial library for communication from Android smartphone to Arduino
  Created by Guillaume Dupre, Friday 26, 2013.
  Released into the public domain.
*/

package com.audioserial.servocontrol;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AudioSerial {
	AudioTrack audiotrack;
	short[] low;
	short[] high;

	int bitlength;
	int startingbyte;
	// bps : bits per second, 20 recommended
	public AudioSerial(int bps,int startingbyte){
		bitlength=44100/bps;
		this.startingbyte=startingbyte;
		audiotrack= new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, 2000, AudioTrack.MODE_STREAM);
		// Initialization of low and high waveforms
		low=new short[bitlength];
		high=new short[bitlength];
		for(int i=0;i<bitlength;i++){
			low[i]=0;
			high[i]=Short.MAX_VALUE;   
		}
		audiotrack.play();
		send(startingbyte);
	}

	// Sending one byte starting from the least significant bit 
	public void send(int val){
		// Starting bit
		audiotrack.write(high, 0, bitlength);

		for(int i=0;i<8;i++){
			if(val%2==0){
				audiotrack.write(low, 0, bitlength);
			}else{
				audiotrack.write(high, 0, bitlength);
			}
			val=(val/2);
		}
	}
}