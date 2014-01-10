import SimpleOpenNI.*;
import processing.core.*;


public class FashionApp extends PApplet {

	public SimpleOpenNI  soni;
	public PImage face;
	public ClothesAdder clothesAdder = new ClothesAdder(this);

	

	public void setup() {
		size(640, 480,P3D);
		
		soni = new SimpleOpenNI(this);

		soni.enableDepth();
		soni.enableRGB();
		soni.enableUser();
		//size(soni.depthWidth(), soni.depthHeight()); //Original without obj loader
		//size(soni.depthWidth(), soni.depthHeight(), P3D); //3d renderer

		
		
		textSize(20);
		fill(0,255,0);
		
		//frameRate(30); // fuer obj loader
	}
	
	public void onNewUser(SimpleOpenNI context, int userId){
		context.startTrackingSkeleton(userId);
	}
	
	public void draw() {
		soni.update();
		 background(0,0,0);
		imageMode(CORNER);
		image(soni.rgbImage(), 0, 0);
		//image(soni.depthImage(), 0, 0);
		int[] userIDs = soni.getUsers();
		
		
		if(userIDs.length >0){
//			clothesAdder.add2DHead(userIDs, "..\\images\\face.png", soni);
			clothesAdder.add3DShirt(userIDs, "..\\models\\black_tshirt_nike.obj", soni);
		
		
		}
	}
}
