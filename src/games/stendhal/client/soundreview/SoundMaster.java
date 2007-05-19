package games.stendhal.client.soundreview;

import games.stendhal.client.WorldObjects;
import games.stendhal.client.WorldObjects.WorldListener;

import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundMaster implements Runnable,WorldListener {
private static SoundFileMap sfm=null;
	private static Cliplistener cliplisten=null;
	private static boolean isMute;
	public static ConcurrentHashMap<Object, Line> playingClips= new ConcurrentHashMap<Object, Line>();

	public void run() {
}
	public void init() {
		sfm = new SoundFileMap();

		cliplisten = new Cliplistener();
		WorldObjects.addWorldListener(this);
	}

	public static AudioClip play(String soundName,double x, double y){
		return play(soundName, x, y, false);
	}
	
	
	public static AudioClip play(String soundName,double x, double y,boolean loop){
		if (!(x==0&&y==0))
		if (HearingArea.contains(x, y)){
			
			return play(soundName);
		}
		return null;
	}
	
	 public static AudioClip play(String soundName) {
		boolean shallLoop=false; 
		return play(soundName, shallLoop);

	}
	public static  AudioClip play(String soundName, boolean shallLoop) {
		if(isMute) return null;
		if (soundName == null) {
	        return  null;
        }

		byte[] o;

		o = sfm.get(soundName);
		if (o == null) {
			//TODO: handle System.out.println("sound " + soundName+" was not got from sfm");
	        return null;
        }
		try {
		AudioClip ac = new AudioClip(AudioSystem.getMixer(null),  o, 100);

		Clip cl;
		
			cl = ac.openLine();
			
if (cl!=null){
	
			cl.addLineListener(cliplisten);
			playingClips.putIfAbsent((Object)cl, cl);
			if (shallLoop){
				cl.loop(Clip.LOOP_CONTINUOUSLY);
			}else{
				
			

			cl.start();
			}
			
			return ac;
			
}
		} catch (UnsupportedAudioFileException e) {
			
		} catch (IOException e) {
			
		} catch (LineUnavailableException e) {
			
		}
		return null;
	}

	class Cliplistener implements LineListener {
		// dont remove this please astriddemma 12.04.2007
		public void update(LineEvent event) {
			if (event.getType().equals(LineEvent.Type.START)) {
				
            }
			if (event.getType().equals(LineEvent.Type.CLOSE)) {
            }
			if (event.getType().equals(LineEvent.Type.STOP)) {
				event.getLine().close();
				
				playingClips.remove(event.getLine());
            }
			
			if (event.getType().equals(LineEvent.Type.OPEN)) {
	            
            }
		}

	}

	public void playerMoved() {
		// TODO Auto-generated method stub
		
	}
	//commented for release
	public void zoneEntered(String zoneName) {
//		System.out.println(zoneName);
//		bg = new Background(zoneName);
//		bg.run();
		
		
	}
	public void zoneLeft(String zoneName) {
//		System.out.println(zoneName);
//		bg.stop();
//		bg=null;
		
	}
	public static void setMute(boolean on) {
		
		if (on){
		Enumeration<Line> enu = playingClips.elements();
		while (enu.hasMoreElements()){
			Line lin = 	enu.nextElement();
			lin.close();
			
		}
		}
		isMute=on;
		
		
	}
}
