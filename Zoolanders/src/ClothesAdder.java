import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import SimpleOpenNI.*;

public class ClothesAdder extends PApplet { // extends Papplet because
											// loadImage() needs it

	PApplet parent; // The parent PApplet that we will render ourselves onto
	public PImage face;

	public ClothesAdder(PApplet p) {
		parent = p;
	}

	public void add2DHead(int[] userIDs, String imagePath, SimpleOpenNI soni) {
		face = loadImage(imagePath);
		for (int i = 0; i < userIDs.length; i++) {
			if (soni.isTrackingSkeleton(userIDs[i])) {
				PVector head3d = new PVector();
				PVector head2d = new PVector();
				
				
				float confidence = soni.getJointPositionSkeleton(userIDs[i],
						SimpleOpenNI.SKEL_HEAD, head3d);
				soni.convertRealWorldToProjective(head3d, head2d);

				float d = (float) (190e3 / head3d.z); // derive size of image,
														// using distance

				parent.imageMode(CENTER);
				parent.image(face, head2d.x, head2d.y, d, d);

//				text("head3d: " + head3d, 10, 20);
//				text("head2d: " + head2d, 10, 40);
//				text("d : " + d, 10, 60);

			}

		}
	}
	
	public void add3DShirt(int[] userIDs, String imagePath, SimpleOpenNI soni) {
		face = loadImage(imagePath);
		for (int i = 0; i < userIDs.length; i++) {
			if (soni.isTrackingSkeleton(userIDs[i])) {
				PVector head3d = new PVector();
				PVector head2d = new PVector();
				
				
				float confidence = soni.getJointPositionSkeleton(userIDs[i],
						SimpleOpenNI.SKEL_HEAD, head3d);
				soni.convertRealWorldToProjective(head3d, head2d);

				float d = (float) (190e3 / head3d.z); // derive size of image,
														// using distance

				parent.imageMode(CENTER);
				parent.image(face, head2d.x, head2d.y, d, d);

//				text("head3d: " + head3d, 10, 20);
//				text("head2d: " + head2d, 10, 40);
//				text("d : " + d, 10, 60);

			}

		}
	}
}
