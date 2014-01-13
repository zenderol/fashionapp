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

				soni.getJointPositionSkeleton(userIDs[i],SimpleOpenNI.SKEL_HEAD, head3d);
				soni.convertRealWorldToProjective(head3d, head2d);

				float d = (float) (190e3 / head3d.z); // derive size of image,
														// using distance
				
				parent.imageMode(CENTER);
				parent.image(face, head2d.x, head2d.y, d, d);

				// System.out.println("Head added!");
				//parent.text("head3d: " + head3d, 10, 20);
				//parent.text("head2d: " + head2d, 10, 40);
				//parent.text("d : " + d, 10, 60);

			}

		}
	}

	public void add3DShirt(int[] userIDs, String modelPath, SimpleOpenNI soni, float scaling) {
		OBJModel model = new OBJModel(parent, modelPath, "absolute", TRIANGLES);
		model.translateToCenter();
//		model.scale(5);
		parent.lights();
		parent.stroke(0);


		for (int i = 0; i < userIDs.length; i++) {
			if (soni.isTrackingSkeleton(userIDs[i])) {

				PVector torso3d = new PVector();
				PVector torso2d = new PVector();
				
				PVector lshoulder3d = new PVector();
				PVector rshoulder3d = new PVector();

				soni.getJointPositionSkeleton(userIDs[i],SimpleOpenNI.SKEL_TORSO, torso3d);
				
				soni.getJointPositionSkeleton(userIDs[i],SimpleOpenNI.SKEL_LEFT_SHOULDER, lshoulder3d);
				soni.getJointPositionSkeleton(userIDs[i],SimpleOpenNI.SKEL_RIGHT_SHOULDER, rshoulder3d);
								
				soni.convertRealWorldToProjective(torso3d, torso2d);
				
				// scaling
				model.scale(scaling*10000/torso3d.z);
				
				// calculate angle y-axis
				float x_dist = Math.abs(lshoulder3d.x-rshoulder3d.x);
				float z_dist = lshoulder3d.z-rshoulder3d.z;
				float y_dist = lshoulder3d.y-rshoulder3d.y;
				z_dist *= -1;
				double angle_y = Math.atan((double) (z_dist/x_dist));
				double angle_z = Math.atan((double) (y_dist/x_dist));

				/* Movement of the 3d Model according to the user */
				parent.pushMatrix();

				// parent.translate(userposition.x, userposition.y);
				parent.translate(640-torso2d.x, torso2d.y);
				//parent.rotateZ((float)angle_z);
				parent.rotateY((float)-angle_y);
				// parent.rotateZ(radians(torso3d.x));
				model.draw();

				parent.popMatrix();
				


				// parent.text("user position: " + userposition, 10, 20);
				parent.text("user position: " + torso2d, 10, 20);
//				parent.text("lshoulder3d: " + lshoulder3d, 10, 100);
//				parent.text("rshoulder3d: " + rshoulder3d, 10, 120);
//				parent.text("x_dist: " + x_dist, 10, 160);
//				parent.text("z_dist: " + z_dist, 10, 180);
//				parent.text("angle_y: " + angle_y + " " + Math.toDegrees(angle_y), 10, 220);

			}

		}
	}
	
	public void add3DPants(int[] userIDs, String modelPath, SimpleOpenNI soni, float scaling) {
		OBJModel model = new OBJModel(parent, modelPath, "absolute", TRIANGLES);
		model.translateToCenter();
//		model.scale(5);
		parent.lights();
		parent.stroke(0);


		for (int i = 0; i < userIDs.length; i++) {
			if (soni.isTrackingSkeleton(userIDs[i])) {

				PVector torso3d = new PVector();
				PVector torso2d = new PVector();
				
				PVector lshoulder3d = new PVector();
				PVector rshoulder3d = new PVector();

				soni.getJointPositionSkeleton(userIDs[i],SimpleOpenNI.SKEL_LEFT_KNEE, torso3d);
				
				soni.getJointPositionSkeleton(userIDs[i],SimpleOpenNI.SKEL_LEFT_SHOULDER, lshoulder3d);
				soni.getJointPositionSkeleton(userIDs[i],SimpleOpenNI.SKEL_RIGHT_SHOULDER, rshoulder3d);
								
				soni.convertRealWorldToProjective(torso3d, torso2d);
				
				// scaling
				model.scale(scaling*10000/torso3d.z);
				
				// calculate angle y-axis
				float x_dist = Math.abs(lshoulder3d.x-rshoulder3d.x);
				float z_dist = lshoulder3d.z-rshoulder3d.z;
				float y_dist = lshoulder3d.y-rshoulder3d.y;
				z_dist *= -1;
				double angle_y = Math.atan((double) (z_dist/x_dist));
				double angle_z = Math.atan((double) (y_dist/x_dist));

				/* Movement of the 3d Model according to the user */
				parent.pushMatrix();

				// parent.translate(userposition.x, userposition.y);
				parent.translate(630-torso2d.x, torso2d.y);
				//parent.rotateZ((float)angle_z);
				parent.rotateY((float)-angle_y);
				// parent.rotateZ(radians(torso3d.x));
				model.draw();

				parent.popMatrix();
				


				// parent.text("user position: " + userposition, 10, 20);
				parent.text("user position: " + torso2d, 10, 20);
//				parent.text("lshoulder3d: " + lshoulder3d, 10, 100);
//				parent.text("rshoulder3d: " + rshoulder3d, 10, 120);
//				parent.text("x_dist: " + x_dist, 10, 160);
//				parent.text("z_dist: " + z_dist, 10, 180);
//				parent.text("angle_y: " + angle_y + " " + Math.toDegrees(angle_y), 10, 220);

			}

		}
	}
}
