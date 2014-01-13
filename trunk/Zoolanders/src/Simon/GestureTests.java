package Simon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

public class GestureTests extends PApplet {

	SimpleOpenNI context;
	int handVecListSize = 20;
	Map<Integer, ArrayList<PVector>> handPathList = new HashMap<Integer, ArrayList<PVector>>();
	int[] userClr = new int[] { color(255, 0, 0), color(0, 255, 0),
			color(0, 0, 255), color(255, 255, 0), color(255, 0, 255),
			color(0, 255, 255) };

	public void setup() {
		// frameRate(200);
		size(640, 480);

		context = new SimpleOpenNI(this);
		if (context.isInit() == false) {
			println("Can't init SimpleOpenNI, maybe the camera is not connected!");
			exit();
			return;
		}

		// enable depthMap generation
		context.enableDepth();
//		context.enableRGB();

		// disable mirror
		context.setMirror(true);

		// enable hands + gesture generation
		// context.enableGesture();
		// context.enableUser();
		context.enableHand();
		// context.startGesture(SimpleOpenNI.NODE_GESTURE);
//		context.startGesture(SimpleOpenNI.GESTURE_WAVE);
//		 context.startGesture(SimpleOpenNI.GESTURE_HAND_RAISE);
		 context.startGesture(SimpleOpenNI.GESTURE_CLICK);

		// set how smooth the hand capturing should be
		// context.setSmoothingHand(5);
	}

	public void draw() {
		// update the cam
		context.update();

		image(context.depthImage(), 0, 0);

		// draw the tracked hands
		if (handPathList.size() > 0) {
			Iterator itr = handPathList.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry mapEntry = (Map.Entry) itr.next();
				int handId = (Integer) mapEntry.getKey();
				ArrayList<PVector> vecList = (ArrayList<PVector>) mapEntry
						.getValue();
				PVector p;
				PVector p2d = new PVector();

				stroke(userClr[(handId - 1) % userClr.length]);
				noFill();
				strokeWeight(1);
				Iterator itrVec = vecList.iterator();
				beginShape();
				while (itrVec.hasNext()) {
					p = (PVector) itrVec.next();

					context.convertRealWorldToProjective(p, p2d);
					vertex(p2d.x, p2d.y);
				}
				endShape();

				stroke(userClr[(handId - 1) % userClr.length]);
				strokeWeight(4);
				p = vecList.get(0);
				context.convertRealWorldToProjective(p, p2d);
				point(p2d.x, p2d.y);

			}
		}
	}

	// -----------------------------------------------------------------
	// hand events

	public void onNewHand(SimpleOpenNI curContext, int handId, PVector pos) {
		println("onNewHand - handId: " + handId + ", pos: " + pos);

		ArrayList<PVector> vecList = new ArrayList<PVector>();
		vecList.add(pos);

		handPathList.put(handId, vecList);
	}

	public void onTrackedHand(SimpleOpenNI curContext, int handId, PVector pos) {
		 println("onTrackedHand - handId: " + handId + ", pos: " + pos );

		ArrayList<PVector> vecList = handPathList.get(handId);
		if (vecList != null) {
			vecList.add(0, pos);
			if (vecList.size() >= handVecListSize)
				// remove the last point
				vecList.remove(vecList.size() - 1);
		}
	}

	public void onLostHand(SimpleOpenNI curContext, int handId) {
		println("onLostHand - handId: " + handId);
		handPathList.remove(handId);
	}

	// -----------------------------------------------------------------
	// gesture events

	public void onCompletedGesture(SimpleOpenNI curContext, int gestureType,
			PVector pos) {
		println("onCompletedGesture - gestureType: " + gestureType + ", pos: "
				+ pos);

		int handId = context.startTrackingHand(pos);
		println("hand stracked: " + handId);
	}

	// -----------------------------------------------------------------
	// Keyboard event
	public void keyPressed() {

		switch (key) {
		case ' ':
			context.setMirror(!context.mirror());
			break;
		case '1':
			context.setMirror(true);
			break;
		case '2':
			context.setMirror(false);
			break;
		}
	}
}