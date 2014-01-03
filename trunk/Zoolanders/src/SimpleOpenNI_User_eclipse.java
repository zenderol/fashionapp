import SimpleOpenNI.*;
import processing.core.*;

public class SimpleOpenNI_User_eclipse extends PApplet {

	public SimpleOpenNI  soni;
	public PImage face;


	public void setup() {
		soni = new SimpleOpenNI(this);
		soni.enableDepth();
		soni.enableRGB();
		soni.enableUser();
		size(soni.depthWidth(), soni.depthHeight());
		face = loadImage("..\\face.png");
		textSize(20);
		fill(0,255,0);
	}
	
	public void onNewUser(SimpleOpenNI context, int userId){
		context.startTrackingSkeleton(userId);
	}
	
	public void draw() {
		soni.update();
		imageMode(CORNER);
		image(soni.rgbImage(), 0, 0);
		int[] userIDs = soni.getUsers();
		for(int i=0; i<userIDs.length; i++){
			if(soni.isTrackingSkeleton(userIDs[i])){
				PVector head3d = new PVector();
				PVector head2d = new PVector();
				
				float confidence = soni.getJointPositionSkeleton(userIDs[i], SimpleOpenNI.SKEL_HEAD, head3d);
				soni.convertRealWorldToProjective(head3d, head2d);
				float d = (float) (190e3 / head3d.z); // derive size of image, using distance
				
				imageMode(CENTER);
				image(face, head2d.x, head2d.y, d, d);
				
				text("head3d: " + head3d, 10, 20);
				text("head2d: " + head2d, 10, 40);
				text("d : " + d, 10, 60);
				
			}
		}
	}
}
