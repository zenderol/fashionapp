package ClassesForTesting;
import processing.core.PApplet;
import processing.video.*;



public class simpleWebCamTest extends PApplet{
	// Variable für das Ansprechen 
	// der Videokamera in P5
	Capture videocam;
	
	
	public void setup () {
		  size (320, 240);
		  // Größe und Bildanzahl pro Sekunde für 
		  // den Kamara-Input festlegen
		  videocam = new Capture (this, width, height, 24);
		}
		 
		public void draw () {
		  // wenn Kamera-Input verfügbar
		  if (videocam.available ()) {
		    // liest momentanes Kamerabild aus
		    videocam.read ();
		  }
		  // Abbilden des mit 'read()' ausgelesenen Bildes 
		  // an der Position x=0, y=0 im Sketch
		  image (videocam, 0, 0);
		}

}
