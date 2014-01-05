import SimpleOpenNI.*;
import processing.core.*;

public class SimpleOpenNI_User_eclipse extends PApplet {

	public SimpleOpenNI  soni;
	public PImage face;
	public ClothesAdder clothesAdder = new ClothesAdder(this);


	public void setup() {
		soni = new SimpleOpenNI(this);
		soni.enableDepth();
		soni.enableRGB();
		soni.enableUser();
		size(soni.depthWidth(), soni.depthHeight());
		//face = loadImage("..\\images\\face.png");
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
		//image(soni.depthImage(), 0, 0);
		int[] userIDs = soni.getUsers();
		
		if(userIDs.length >0){
		clothesAdder.add2DHead(userIDs, "..\\images\\face.png", soni);
		
		
		}
	}
}
