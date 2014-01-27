import java.util.HashMap;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

public class FashionApp extends PApplet {
	
	public static final boolean DEBUG = true;
	
	int i = 0;

	HashMap<String, PImage> imgCacheMap;

	public SimpleOpenNI soni;
	public PImage face;
	public ClothesAdder clothesAdder = new ClothesAdder(this);

	PImage backgroundImg; // background image
	boolean bBackground = true;

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
	float r_fill = 0; // radius to fill circle
	int hand_color = color(255, 0, 0);

	int button_color = color(255, 0, 0);

	// choosen, active cloths
	Button activeShirt = null;
	Button activePants = null;
	Button activeDress = null;
	Button activeCategory = null;

	public void setup() {
		imgCacheMap = new HashMap<String, PImage>();

		size(640, 480, P3D);
		backgroundImg = loadImage("..\\images\\GUI\\background.png");

		soni = new SimpleOpenNI(this);
		soni.enableDepth();
		soni.enableRGB();
		soni.enableUser();
		// size(soni.depthWidth(), soni.depthHeight()); //Original without obj
		// loader
		// size(soni.depthWidth(), soni.depthHeight(), P3D); //3d renderer

		textSize(20);
		fill(255, 255, 255);

		ui = new UserInterface(this, soni) {
			@Override
			public void draw() {
				// draw buttons
				for (Button button : getButtons()) {
//					if (button.getType().equals(Button.Type.RESET))
//						fill(127, 127, 127); // grey reset button
//					else {
						noFill(); //other buttons: red - no user detected || green - user found
						
						if(activePants != null && activePants.getPath().equals(button.getPath()))
							strokeWeight(3);
						else if(activeShirt != null && activeShirt.getPath().equals(button.getPath()))
							strokeWeight(3);
						else if(activeDress != null && activeDress.getPath().equals(button.getPath()))
							strokeWeight(3);
						else
							strokeWeight(1);
						stroke(0);
//					}

					// draw buttons
					rect(button.getPosX(), button.getPosY(), button.getWidth(),
							button.getHeight());

					// draw image of button
					String sPreviewImagePath = button.getImgPreview();
					if (sPreviewImagePath != null
							&& sPreviewImagePath.length() > 0) {
						String imgPath = button.getImgPreview();
						PImage icon = null;
						if (imgCacheMap.containsKey(imgPath)) {
							if(DEBUG) println("Take image from cache: " + imgPath);
							icon = imgCacheMap.get(imgPath);
						} else {
							icon = loadImage(imgPath);
							if(DEBUG) println("Put image in cache: " + imgPath);
							imgCacheMap.put(imgPath, icon);
						}
						image(icon,
								button.getPosX() + (button.getWidth() - icon.width) / 2,
								button.getPosY() + (button.getHeight() - icon.height) / 2);
					}

					// draw price tage
					if (button.getPrice() >= 0) {
						strokeWeight(1);
						fill(255, 255, 255);
						rect(button.getPosX(),
								button.getPosY() + button.getHeight(), 52, 15);
						fill(255, 0, 0);
						textSize(14);
						text(button.getPrice() + "€", button.getPosX() + 1,
								button.getPosY() + button.getHeight() + 13);
					}
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

		// stroke(255, 255, 255);
		// smooth();

		// create buttons 'containing cloths'
		List<Button> buttonList = ButtonFactory.getAllCategories();
		ui.addList(buttonList);

		// activePants = c;
		// activeShirt = a;

		List<Button> pants = ButtonFactory.getAllPants();
		for (Button b : pants)
			clothesAdder.putObj2CacheMap(b.getPath());
		pants = ButtonFactory.getAllDresses();
		for (Button b : pants)
			clothesAdder.putObj2CacheMap(b.getPath());
		pants = ButtonFactory.getAllShirts();
		for (Button b : pants)
			clothesAdder.putObj2CacheMap(b.getPath());
		pants = null;

		if(DEBUG) println(clothesAdder.getObjCacheMap().entrySet());

		// frameRate(30); // fuer obj loader
	}

	public void draw() {

		soni.update();
		background(255);
		// imageMode(CORNER);
		image(soni.rgbImage(), 0, 0);

		if (bBackground) {
			pushMatrix(); // needed?
			image(backgroundImg, 0, 0);
			popMatrix(); // needed?
		}
		// image(soni.depthImage(), 0, 0);
		int[] userIDs = soni.getUsers();

		addPriceListing();
		// if user exists
		noStroke();
		if (userIDs.length > 0) {
			// change color of buttons to green
			button_color = color(150, 150, 255);

			// load selected shirt
			if (activeShirt != null)
				clothesAdder.add3DShirt(userIDs, activeShirt.getPath(), soni,
						activeShirt.getScale());

			if (activeDress != null)
				clothesAdder.add3DDress(userIDs, activeDress.getPath(), soni,
						activeDress.getScale());

			// load selected pants
			if (activePants != null)
				clothesAdder.add3DPants(userIDs, activePants.getPath(), soni,
						activePants.getScale());

			// clothesAdder.add2DHead(userIDs, "..\\images\\face.png", soni);
		} else
			button_color = color(255, 0, 0); // change color of buttons to red
												// (no user)

		// draw buttons
		ui.draw();

		// draw ellipse around hand
		if (handsTrackFlag) {
			soni.convertRealWorldToProjective(handVec, handVec2D);
			strokeWeight(4);
			stroke(hand_color);
			noFill();
			ellipse(640 - handVec2D.x, handVec2D.y, r_circle, r_circle);

			if (is_holding_button == true) {
				noStroke();
				fill(hand_color);
				ellipse(640 - handVec2D.x, handVec2D.y, r_fill, r_fill);
			}
		}

	}

	/* HAND GESTURES */
	public void onTrackedHand(SimpleOpenNI curContext, int handId, PVector pos) {
//		if(DEBUG) println("onUpdateHandsCb - handId: " + handId + ", pos: " + pos);
		PVector pos2d = new PVector();
		soni.convertRealWorldToProjective(pos, pos2d);
		handVec = pos;

		// get selected button (hand over button)
		Button b = ui.click(640 - pos2d.x, pos2d.y, pos2d.z);

		if (b != null) {
			// reset timer, if hand is over different button
			if (!b.equals(holdedButton)) {
				startTime = System.currentTimeMillis();
				is_holding_button = false;
				hand_color = color(255, 0, 0);
			} else {
				// measure time (hand over same button)
				long heldTime = System.currentTimeMillis() - startTime;
				is_holding_button = true;

				// after 2000ms over one button: 'active' button
				if (heldTime >= time_2_hold) {
					hand_color = color(0, 255, 0);
					if(DEBUG) println(time_2_hold / 1000 + " Sekunden gehalten!!!!!!!!!");
					switch (b.getType()) {
					case SHIRT:
						activeDress = null;
						activeShirt = b;
						break;
					case PANTS:
						activePants = b;
						break;
					case RESET:
						activeShirt = null;
						activePants = null;
						activeDress = null;
						break;
					case CATEGORY:
						setCategory(b);
						break;
					case RETURN:
						returnFromCategory();
						break;
					case DRESSES:
						activeDress = b;
						activeShirt = null;
						break;
					case SKIRTS:
						break;
					default:
						println("Button type does not exist!");
						break;
					}
				} else
					r_fill = r_circle * (heldTime / time_2_hold);
			}
		} else {
			is_holding_button = false;
			hand_color = color(255, 0, 0);
		}
		holdedButton = b;
	}

	private void setCategory(Button b) {
		activeCategory = b;
		ui.clearGUI();
		switch (activeCategory.getCategoryName()) {
		case "shirts":
			ui.addList(ButtonFactory.getAllShirts());
			break;
		case "pants":
			ui.addList(ButtonFactory.getAllPants());
			break;
		case "dresses":
			ui.addList(ButtonFactory.getAllDresses());
			break;
		case "skirts": // TODO
			ui.addList(ButtonFactory.getAllCategories());
			break;
		default:
			ui.addList(ButtonFactory.getAllCategories());
			break;
		}
	}

	private void returnFromCategory() {
		activeCategory = null;
		ui.clearGUI();
		ui.addList(ButtonFactory.getAllCategories());
	}

	public void onNewHand(SimpleOpenNI curContext, int handId, PVector pos) {
		if(DEBUG) println("onCreateHands - handId: " + handId + ", pos: " + pos);
		handsTrackFlag = true;
		handVec = pos;
	}

	public void onLostHand(SimpleOpenNI curContext, int handId) {
		if(DEBUG) println("onDestroyHandsCb - handId: " + handId);
		handsTrackFlag = false;
		soni.startGesture(lastGesture);
	}

	public void onCompletedGesture(SimpleOpenNI curContext, int gestureType, PVector pos) {
		if(DEBUG) println("onCompletedGesture - gestureType: " + gestureType + ", pos: " + pos);
		int handId = soni.startTrackingHand(pos);
		if(DEBUG) println("hand stracked: " + handId);
		lastGesture = gestureType;
		soni.endGesture(gestureType);
	}

	/* HAND GESTURES */

	/* USER DETECTION */
	public void onNewUser(SimpleOpenNI context, int userId) {
		context.startTrackingSkeleton(userId);
		if(DEBUG) println("NEW USER: " + userId + "   " + soni.getUsers().length);
	}

	public void onLostUser(SimpleOpenNI context, int userId) {
		context.stopTrackingSkeleton(userId);
		if(DEBUG) println("LOST USER: " + userId + "   " + soni.getUsers().length);
	}

	/* USER DETECTION */

	public void keyPressed() {
		// turns on and off the texture listed in .mtl file
		if (key == 'b') {
			if (!bBackground)
				bBackground = true;
			else
				bBackground = false;
		}
	}

	public void mouseClicked() {
		println("MOUSE: " + mouseX + " " + mouseY);
		Button b = ui.click(mouseX, mouseY, 0);
		if (b != null) {
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
				activeDress = null;
				break;
			case CATEGORY:
				setCategory(b);
				break;
			case RETURN:
				returnFromCategory();
				break;
			case DRESSES:
				activeDress = b;
				activeShirt = null;
				break;
			case SKIRTS:
				break;
			default:
				println("Button type does not exist!");
				break;
			}
		}
	}

	private void addPriceListing() {
		int y = 425;
		int x = 545;
		int size = 13;
		double total = 0;
		textSize(size);
		fill(0);
		if (activeShirt != null || activePants != null || activeDress != null) {
			text("Check", x, y);
			y += size;
		}
		if (activeShirt != null) {
			text("Shirt: " + activeShirt.getPrice() + "€", x, y);
			total += activeShirt.getPrice();
			y += size;
		}
		if (activePants != null) {
			text("Pants: " + activePants.getPrice() + "€", x, y);
			total += activePants.getPrice();
			y += size;
		}
		if (activeDress != null) {
			text("Dress: " + activeDress.getPrice() + "€", x, y);
			total += activeDress.getPrice();
			y += size;
		}
		if (total > 0) {
			text("Total: " + total + "€", x, y);
			y += size;
		}

	}
}