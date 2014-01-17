import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;


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
	
	boolean is_holding_button = false;
	float time_2_hold = 2000; // in ms
	float r_circle = 30; // radius of circle (hand)
	float r_fill = 0;	// radius to fill circle
	int hand_color = color(255,255,0);
	
	int button_color = color(255,0,0);
	
	// choosen, active cloths
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
						fill(127,127,127); // grey reset button	
					else
						fill(button_color); // other buttons: red - no user detected || green - user found
					
					// draw buttons
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

//		stroke(255, 255, 255);
		smooth();
		
		// create buttons 'containing cloths'
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
		
		// create reset button (undress)
		Button reset = new Button(400, 0, 100, 100);
		reset.setPath("");
		reset.setType(Button.Type.RESET);
		reset.setScale(0.35f);
		
		// add buttons to gui (button list)
		ui.add(a);
		ui.add(b);
		ui.add(c);
		ui.add(reset);
		
//		activePants = c;
//		activeShirt = a;
		
		//frameRate(30); // fuer obj loader
	}
	
	public void draw() {
		soni.update();
		background(255);
//		imageMode(CORNER);
//		image(soni.rgbImage(), 0, 0);
		//image(soni.depthImage(), 0, 0);
		int[] userIDs = soni.getUsers();
		
		// if user exists
		noStroke();
		if(userIDs.length >0){			
			// change color of buttons to green
			button_color = color(150,150,255);		
			
			// load selected shirt
			if(activeShirt != null)				
				clothesAdder.add3DShirt(userIDs, activeShirt.getPath(), soni, activeShirt.getScale());
			
			// load selected pants
			if(activePants != null)				
				clothesAdder.add3DPants(userIDs, activePants.getPath(), soni, activePants.getScale());
			
			//clothesAdder.add2DHead(userIDs, "..\\images\\face.png", soni);
		} else
			button_color = color(255,0,0); // change color of buttons to red (no user)
		
		// draw buttons
		ui.draw();
		
		// draw ellipse around hand
		if (handsTrackFlag) {
			soni.convertRealWorldToProjective(handVec, handVec2D);
			strokeWeight(4);
			stroke(hand_color);
			noFill();
			ellipse(640-handVec2D.x, handVec2D.y, r_circle, r_circle);
			
			if(is_holding_button == true){
				noStroke();
				fill(hand_color);
				ellipse(640-handVec2D.x, handVec2D.y, r_fill, r_fill);
				
			}
		}
	}

/* HAND GESTURES */
	public void onTrackedHand(SimpleOpenNI curContext, int handId, PVector pos) {
//		println("onUpdateHandsCb - handId: " + handId + ", pos: " + pos);
		PVector pos2d = new PVector();
		soni.convertRealWorldToProjective(pos, pos2d);
//		System.out.println("onUpdateHandsCb - handId: " + handId + ", pos2d: " + pos2d);
		handVec = pos;
		
		// get selected button (hand over button)
		Button b = ui.click(640-pos2d.x, pos2d.y, pos2d.z);
		
		if (b != null) {
			// reset timer, if hand is over different button
			if (!b.equals(holdedButton)){
				startTime = System.currentTimeMillis();
				is_holding_button = false;
				hand_color = color(255,255,0);
			} else {
				// measure time (hand over same button)
				long heldTime = System.currentTimeMillis() - startTime;
				is_holding_button = true;

				// after 2000ms over one button: 'active' button
				if (heldTime >= time_2_hold) {
					hand_color = color(0,255,0);
					println(time_2_hold/1000+" Sekunden gehalten!!!!!!!!!");
					switch (b.getType()) {
					case SHIRT:
						activeShirt = b;
						break;
					case PANTS:
						activePants = b;
						break;
					case RESET:
						activeShirt = null;
						activePants = null;
						break;
					}
				}
				else
					r_fill = r_circle * (heldTime/time_2_hold);
			}
		} else {
			is_holding_button = false;
			hand_color = color(255,255,0);
		}
		holdedButton = b;
	}
	
	public void onNewHand(SimpleOpenNI curContext, int handId, PVector pos) {
		println("onCreateHands - handId: " + handId + ", pos: " + pos);
		handsTrackFlag = true;
		handVec = pos;
	}
	public void onLostHand(SimpleOpenNI curContext, int handId) {
		println("onDestroyHandsCb - handId: " + handId);
		handsTrackFlag = false;
		soni.startGesture(lastGesture);
	}
	public void onCompletedGesture(SimpleOpenNI curContext, int gestureType, PVector pos) {
		println("onCompletedGesture - gestureType: " + gestureType + ", pos: " + pos);
		int handId = soni.startTrackingHand(pos);
		println("hand stracked: " + handId);
		lastGesture = gestureType;
		soni.endGesture(gestureType);
	}
/* HAND GESTURES */

/* USER DETECTION */
	public void onNewUser(SimpleOpenNI context, int userId){
		context.startTrackingSkeleton(userId);
		println("NEW USER: " + userId + "   " + soni.getUsers().length);
	}	
	public void onLostUser(SimpleOpenNI context, int userId){
//		context.stopTrackingSkeleton(userId);
		println("LOST USER: " + userId + "   " + soni.getUsers().length);
	}
/* USER DETECTION */
	
}
