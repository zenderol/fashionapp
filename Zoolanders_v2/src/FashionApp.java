import SimpleOpenNI.*;
import processing.core.*;


public class FashionApp extends PApplet {

	public SimpleOpenNI  soni;
	public PImage face;
	public ClothesAdder clothesAdder = new ClothesAdder(this);
	
	boolean handsTrackFlag = false;
	PVector handVec = new PVector();
	PVector handVec2D = new PVector();// just for drawing
	Integer lastGesture = null;
	float lastZ = 0;
	boolean isPushing, wasPushing;
	float yourClickThreshold = 20;// set this up as you see fit for your
									// interaction
	
	Button holdedButton;
	long startTime;
	UserInterface ui;
	
	boolean holded = false;
	int filled = color(255,0,0);
	private int i = 2000;
	
	Button activeShirt = null;
	Button activePants = null;

	

	public void setup() {
		size(640, 480,P3D);
		
		soni = new SimpleOpenNI(this);
		soni.enableDepth();
		soni.enableRGB();
		soni.enableUser();
		//size(soni.depthWidth(), soni.depthHeight()); //Original without obj loader
		//size(soni.depthWidth(), soni.depthHeight(), P3D); //3d renderer
		
		textSize(20);
		fill(255,255,255);
		
		ui = new UserInterface(this, soni) {
			@Override
			public void draw() {

				// draw buttons
				for (Button button : getButtons()) {					
					if(button.getType().equals(Button.Type.RESET))
						fill(127,127,127);					
					else
						fill(filled);
					
					rect(button.getPosX(), button.getPosY(), button.getWidth(),
							button.getHeight());
				}
			}
		};
		
		// disable mirror
		soni.setMirror(true);


		// enable hands + gesture generation
		soni.enableHand();

		soni.startGesture(SimpleOpenNI.GESTURE_WAVE);
		soni.startGesture(SimpleOpenNI.GESTURE_CLICK);
		soni.startGesture(SimpleOpenNI.GESTURE_HAND_RAISE);

		lastGesture = SimpleOpenNI.GESTURE_CLICK;

		// set how smooth the hand capturing should be
		// context.setSmoothingHands(.5);

		stroke(255, 255, 255);
		smooth();
		
		Button a = new Button(0, 0, 100, 100);
		a.setPath("..\\models\\shirts\\black_tshirt_nike.obj");
		a.setType(Button.Type.SHIRT);
		Button b = new Button(0, 150, 100, 100);
		b.setPath("..\\models\\shirts\\Polo_shirt_white.obj");
		b.setType(Button.Type.SHIRT);
		b.setScale(0.35f);
		Button c = new Button(150, 0, 100, 100);
		c.setPath("..\\models\\pants\\pants_with_shoes.obj");
		c.setType(Button.Type.PANTS);
		c.setScale(0.35f);
		
		Button reset = new Button(400, 0, 100, 100);
		reset.setPath("");
		reset.setType(Button.Type.RESET);
		reset.setScale(0.35f);
		
		ui.add(a);
		ui.add(b);
		ui.add(c);
		ui.add(reset);
		
//		activePants = c;
//		activeShirt = a;
		
		//frameRate(30); // fuer obj loader
	}
	
	public void onNewUser(SimpleOpenNI context, int userId){
		context.startTrackingSkeleton(userId);
	}
	
	public void draw() {
		soni.update();
		background(255);
//		imageMode(CENTER);
//		image(soni.rgbImage(), 0, 0);
		//image(soni.depthImage(), 0, 0);
		int[] userIDs = soni.getUsers();
		
		
//		System.out.println("Active shirt: " + activeShirt);
//		System.out.println("Active pants: " + activePants);
		
		if(userIDs.length >0){
			i++;
			filled = color(0,255,0);
			//clothesAdder.add2DHead(userIDs, "..\\images\\face.png", soni);
			if(activeShirt != null)				
				clothesAdder.add3DShirt(userIDs, activeShirt.getPath(), soni, activeShirt.getScale());
			
			if(activePants != null)				
				clothesAdder.add3DPants(userIDs, activePants.getPath(), soni, activePants.getScale());			
			
//			saveFrame("..\\screenshots\\image-#"+i+".png");
		}
		if (handsTrackFlag) {
			soni.convertRealWorldToProjective(handVec, handVec2D);
			ellipse(640-handVec2D.x, handVec2D.y, 30, 30);
		}
		
		ui.draw();
	}
//	
	public void onNewHand(SimpleOpenNI curContext, int handId, PVector pos) {
		println("onCreateHands - handId: " + handId + ", pos: " + pos);

		handsTrackFlag = true;
		handVec = pos;
	}

	public void onTrackedHand(SimpleOpenNI curContext, int handId, PVector pos) {
//		println("onUpdateHandsCb - handId: " + handId + ", pos: " + pos);
		PVector pos2d = new PVector();
		soni.convertRealWorldToProjective(pos, pos2d);
//		System.out.println("onUpdateHandsCb - handId: " + handId + ", pos2d: " + pos2d);
		handVec = pos;
		Button b = ui.click(640-pos2d.x, pos2d.y, pos2d.z);
		
			if(b != null){
				if(!b.equals(holdedButton))
					startTime = System.currentTimeMillis();
				else {
					long holdTime = System.currentTimeMillis() - startTime;
					println(holdTime);
					if(holdTime >= 2000){
						println("3 Sekunden gehalten!!!!!!!!! HURRA");
						holded = true;
						switch(b.getType()){
							case SHIRT : activeShirt = b; break;
							case PANTS : activePants = b; break;
							case RESET : activeShirt = null; activePants = null; break;
						}
					}
				}
			}
		holdedButton = b;
	}
	

	public void onLostHand(SimpleOpenNI curContext, int handId) {
		println("onDestroyHandsCb - handId: " + handId);
		handsTrackFlag = false;
		soni.startGesture(lastGesture);
	}

	// -----------------------------------------------------------------
	// gesture events

	public void onCompletedGesture(SimpleOpenNI curContext, int gestureType,
			PVector pos) {
		println("onCompletedGesture - gestureType: " + gestureType + ", pos: " + pos);
//
		int handId = soni.startTrackingHand(pos);
		println("hand stracked: " + handId);

//		if (gestureType == SimpleOpenNI.GESTURE_CLICK) {
//			println("+++++++CLICK++++++++");
//
//			PVector click2dPosition = new PVector();
//			context.convertRealWorldToProjective(pos, click2dPosition);
//			println(click2dPosition);
//
//			ui.click(click2dPosition.x, click2dPosition.y, click2dPosition.z);
//		}
//
		lastGesture = gestureType;
		soni.endGesture(gestureType);
//
//		soni.startTrackingHand(pos);
	}
}
