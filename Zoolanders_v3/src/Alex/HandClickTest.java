package Alex;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

public class HandClickTest extends PApplet {

	private static final long serialVersionUID = 8063677203765751660L;

	SimpleOpenNI context;
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

	public void setup() {
		size(640, 480, P3D);
		context = new SimpleOpenNI(this);

		ui = new UserInterface(this, context) {

			@Override
			public void draw() {

				// draw buttons
				for (Button button : getButtons()) {
					stroke(0);
					rect(button.getPosX(), button.getPosY(), button.getWidth(),
							button.getHeight());
				}

			}

		};

		// disable mirror
		context.setMirror(true);

		// enable depthMap generation
		context.enableDepth();
		context.enableRGB();

		// enable hands + gesture generation
		context.enableHand();
		context.enableUser();

//		context.startGesture(SimpleOpenNI.GESTURE_WAVE);
		context.startGesture(SimpleOpenNI.GESTURE_CLICK);
//		context.startGesture(SimpleOpenNI.GESTURE_HAND_RAISE);

		lastGesture = SimpleOpenNI.GESTURE_CLICK;

		// set how smooth the hand capturing should be
		// context.setSmoothingHands(.5);

		stroke(127, 127, 127);
		smooth();

		// ui.add(new Button(20, 20));
		ui.add(new Button(0, 0, 100, 100) {
			@Override
			protected void onClick() {
				System.out.println("+++++++++ OBERER BUTTON ++++++++++");
			}

		});
//		ui.add(new Button(200, 140, 156, 100){
//			@Override
//			protected void onClick() {
//				System.out.println("+++++++++ UNTERER BUTTON ++++++++++");
//			}
//		});

	}

	public void draw() {
		context.update();

		// scale((float) -1.0, (float) 1.0);
		PImage img = context.rgbImage();

		image(img, 0, 0);
		// draw the tracked hand
		if (handsTrackFlag) {
			context.convertRealWorldToProjective(handVec, handVec2D);
			float diff = (handVec.z - lastZ);
			isPushing = diff < 0;
			if (diff > yourClickThreshold) {
				if (!wasPushing && isPushing)
					fill(255, 0, 0);
				if (wasPushing && !isPushing)
					fill(0, 255, 0);
			} else
				fill(255);
			lastZ = handVec.z;
			wasPushing = isPushing;
			ellipse(handVec2D.x, handVec2D.y, 30, 30);

		}
		// rect(10,10, 100, 100);
		ui.draw();

	}

	// -----------------------------------------------------------------
	// hand events

	public void onNewHand(SimpleOpenNI curContext, int handId, PVector pos) {
		println("onCreateHands - handId: " + handId + ", pos: " + pos);

		handsTrackFlag = true;
		handVec = pos;
	}

	public void onTrackedHand(SimpleOpenNI curContext, int handId, PVector pos) {
		println("onUpdateHandsCb - handId: " + handId + ", pos: " + pos);
		PVector pos2d = new PVector();
		context.convertRealWorldToProjective(pos, pos2d);
//		System.out.println("onUpdateHandsCb - handId: " + handId + ", pos2d: " + pos2d);
		handVec = pos;
		Button b = ui.click(pos2d.x, pos2d.y, pos2d.z);
		
			if(b != null){
				if(!b.equals(holdedButton))
					startTime = System.currentTimeMillis();
				else {
					long holdTime = System.currentTimeMillis() - startTime;
					if(holdTime >= 3000)
						println("3 Sekunden gehalten!!!!!!!!! HURRA");
				}
			}
		holdedButton = b;
	}
	

	public void onLostHand(SimpleOpenNI curContext, int handId) {
		println("onDestroyHandsCb - handId: " + handId);
		handsTrackFlag = false;
		context.startGesture(lastGesture);
	}

	// -----------------------------------------------------------------
	// gesture events

	public void onCompletedGesture(SimpleOpenNI curContext, int gestureType,
			PVector pos) {
		println("onCompletedGesture - gestureType: " + gestureType + ", pos: " + pos);
//
		int handId = context.startTrackingHand(pos);
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
//		lastGesture = gestureType;
		context.endGesture(gestureType);
//
//		context.startTrackingHand(pos);
	}

}
