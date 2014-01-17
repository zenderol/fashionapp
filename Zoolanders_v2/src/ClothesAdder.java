import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import SimpleOpenNI.*;

import saito.objloader.*;

public class ClothesAdder extends PApplet { // extends Papplet because
											// loadImage() needs it

	PApplet parent; // The parent PApplet that we will render ourselves onto
	public PImage face;
	
	// data of active cloths
	OBJModel _Shirtmodel = null;
	String _ShirtPath = "";
	OBJModel _Pantsmodel = null;
	String _PantsPath = "";

	public ClothesAdder(PApplet p) {
		parent = p;
	}

	public void add3DShirt(int[] userIDs, String modelPath, SimpleOpenNI soni, float scaling) {
		if(_Shirtmodel == null || !_ShirtPath.equals(modelPath)){
			_Shirtmodel = null;
			_ShirtPath = modelPath;
			_Shirtmodel = new OBJModel(parent, modelPath, "absolute", TRIANGLES);
			_Shirtmodel.translateToCenter();
		}
		
//		model.scale(5);
//		parent.lights();
//		parent.stroke(0);


		for (int i = 0; i < userIDs.length; i++) {
			if (soni.isTrackingSkeleton(userIDs[i])) {

				// init
				PVector torso3d = new PVector();
				PVector torso2d = new PVector();				
				PVector lshoulder3d = new PVector();
				PVector rshoulder3d = new PVector();
				
				// read skeletion positions
				soni.getJointPositionSkeleton(userIDs[i],SimpleOpenNI.SKEL_TORSO, torso3d);				
				soni.getJointPositionSkeleton(userIDs[i],SimpleOpenNI.SKEL_LEFT_SHOULDER, lshoulder3d);
				soni.getJointPositionSkeleton(userIDs[i],SimpleOpenNI.SKEL_RIGHT_SHOULDER, rshoulder3d);								
				soni.convertRealWorldToProjective(torso3d, torso2d);
				
				// scaling
				float fScale = scaling*10000/torso3d.z;
				_Shirtmodel.scale(fScale);
				
				// calculate angles
				float x_dist = Math.abs(lshoulder3d.x-rshoulder3d.x);
				float z_dist = lshoulder3d.z-rshoulder3d.z;
				float y_dist = lshoulder3d.y-rshoulder3d.y;
				z_dist *= -1;
				double angle_y = Math.atan((double) (z_dist/x_dist));
				double angle_z = Math.atan((double) (y_dist/x_dist));

				/* Movement of the 3d Model according to the user */
				parent.pushMatrix();

				// parent.translate(userposition.x, userposition.y);
				parent.translate(640-torso2d.x, torso2d.y); // 640-... wegen Mirrowing
				//parent.rotateZ((float)angle_z);
				parent.rotateY((float)-angle_y);
				// parent.rotateZ(radians(torso3d.x));
				_Shirtmodel.draw();

				parent.popMatrix();
				
				// reverse scale
				_Shirtmodel.scale(1/fScale);
			}

		}
	}
	
	public void add3DPants(int[] userIDs, String modelPath, SimpleOpenNI soni, float scaling) {
		if(_Pantsmodel == null || !_PantsPath.equals(modelPath)){
			_Pantsmodel = null;
			_PantsPath = modelPath;
			_Pantsmodel = new OBJModel(parent, modelPath, "absolute", TRIANGLES);
			_Pantsmodel.translateToCenter();
		}
		
		// ???
		parent.lights();
		parent.noStroke();

		for (int i = 0; i < userIDs.length; i++) {
			if (soni.isTrackingSkeleton(userIDs[i])) {
				
				// init
				PVector leftknee3d = new PVector();
				PVector leftknee2d = new PVector();				
				PVector lshoulder3d = new PVector();
				PVector rshoulder3d = new PVector();
				
				// read skeleton positions
				soni.getJointPositionSkeleton(userIDs[i],SimpleOpenNI.SKEL_LEFT_KNEE, leftknee3d);				
				soni.getJointPositionSkeleton(userIDs[i],SimpleOpenNI.SKEL_LEFT_SHOULDER, lshoulder3d);
				soni.getJointPositionSkeleton(userIDs[i],SimpleOpenNI.SKEL_RIGHT_SHOULDER, rshoulder3d);								
				soni.convertRealWorldToProjective(leftknee3d, leftknee2d);
				
				// scaling
				float fScale = scaling*10000/leftknee3d.z;
				_Pantsmodel.scale(fScale);
				
				// calculate angles
				//TODO angles via left_hip and right_hip
				float x_dist = Math.abs(lshoulder3d.x-rshoulder3d.x);
				float z_dist = lshoulder3d.z-rshoulder3d.z;
				float y_dist = lshoulder3d.y-rshoulder3d.y;
				z_dist *= -1;
				double angle_y = Math.atan((double) (z_dist/x_dist));
				double angle_z = Math.atan((double) (y_dist/x_dist));

				/* Movement of the 3d Model according to the user */
				parent.pushMatrix();

				parent.translate(600-leftknee2d.x, leftknee2d.y); // adjust position and translate to pants
				//parent.rotateZ((float)angle_z);
				parent.rotateY((float)-angle_y);
				// parent.rotateZ(radians(torso3d.x));
				_Pantsmodel.draw();

				parent.popMatrix();
				
				_Pantsmodel.scale(1/fScale); // reverse scale
				
				// parent.text("user position: " + userposition, 10, 20);
//				parent.text("user position: " + torso2d, 10, 20);
//				parent.text("lshoulder3d: " + lshoulder3d, 10, 100);
//				parent.text("rshoulder3d: " + rshoulder3d, 10, 120);
//				parent.text("x_dist: " + x_dist, 10, 160);
//				parent.text("z_dist: " + z_dist, 10, 180);
//				parent.text("angle_y: " + angle_y + " " + Math.toDegrees(angle_y), 10, 220);

			}

		}
	}
	
	public void add2DHead(int[] userIDs, String imagePath, SimpleOpenNI soni) {
		face = loadImage(imagePath);
		for (int i = 0; i < userIDs.length; i++) {
			if (soni.isTrackingSkeleton(userIDs[i])) {
				PVector head3d = new PVector();
				PVector head2d = new PVector();

				soni.getJointPositionSkeleton(userIDs[i],SimpleOpenNI.SKEL_HEAD, head3d);
				soni.convertRealWorldToProjective(head3d, head2d);

				float d = (float) (190e3 / head3d.z); // derive size of image, using distance
				
				parent.imageMode(CENTER);
				parent.image(face, head2d.x, head2d.y, d, d);
			}

		}
	}
}
