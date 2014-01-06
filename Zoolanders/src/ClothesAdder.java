import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import SimpleOpenNI.*;

import saito.objloader.*;

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

				// System.out.println("Head added!");
				// text("head3d: " + head3d, 10, 20);
				// text("head2d: " + head2d, 10, 40);
				// text("d : " + d, 10, 60);

			}

		}
	}

	public void add3DShirt(int[] userIDs, String modelPath, SimpleOpenNI soni) {
		OBJModel model = new OBJModel(parent, modelPath, "absolute", TRIANGLES);
		model.translateToCenter();
		model.scale(5);
		parent.lights();
		// model.draw();//added to parent because of OBJModel model = new
		// OBJModel(parent,...

		for (int i = 0; i < userIDs.length; i++) {
			if (soni.isTrackingSkeleton(userIDs[i])) {

//				PVector userposition = new PVector();
//				soni.getCoM(userIDs[i], userposition);
				
				PVector torso3d = new PVector();
				PVector torso2d = new PVector();
				
				
				float confidence = soni.getJointPositionSkeleton(userIDs[i],
						SimpleOpenNI.SKEL_TORSO, torso3d);
				soni.convertRealWorldToProjective(torso3d, torso2d);

		
				

				//PVector modelrotation = new PVector(userposition.x, userposition.y, userposition.z);

				
				parent.pushMatrix();

				//parent.translate(userposition.x, userposition.y);
				parent.translate(torso2d.x, torso2d.y);

//				parent.rotateX(radians(torso3d.x));
				 parent.rotateY(radians(torso3d.y));
	          //  parent.rotateZ(radians(torso3d.z));
				model.draw();

				parent.popMatrix();

				//parent.text("user position: " + userposition, 10, 20);
				parent.text("user position: " + torso2d, 10, 20);

			}

		}
	}
}
