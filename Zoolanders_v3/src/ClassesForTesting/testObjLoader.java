package ClassesForTesting;
import processing.core.PApplet;

import saito.objloader.*;


public class testObjLoader extends PApplet {
	OBJModel model ;
	
	boolean bTexture = true;
	boolean bStroke = false;
	boolean bMaterial = true;

	float rotX, rotY;
	
	public void setup()
	{
	    size(800, 600, P3D);
	    frameRate(30);
//	    model = new OBJModel(this, "..\\..\\models\\dresses\\black_dress_scale120.obj", "absolute", TRIANGLES);
	    model = new OBJModel(this, "..\\..\\models\\pants\\pants_with_shoes.obj", "absolute", TRIANGLES);
//	    model = new OBJModel(this, "..\\..\\models\\pants\\blue_jeans.obj", "absolute", TRIANGLES);
	    
	    
	    model.enableMaterial();
	    model.enableTexture();
	    
	    
	    model.enableDebug();

	    model.scale(0.35f);
	    model.translateToCenter();

	    stroke(255);
	    noStroke();
	}
	public void draw()
	{
	    background(129);
	    lights();
	    pushMatrix();
	    translate(width/2, height/2, 0);
	    rotateX(rotY);
	    rotateY(rotX);

	    model.draw();

	    popMatrix();
	}


	public void keyPressed() {
	    // turns on and off the texture listed in .mtl file
	    if(key == 't') {
	        if(!bTexture) {
	            model.enableTexture();
	            bTexture = true;
	        } 
	        else {
	            model.disableTexture();
	            bTexture = false;
	        }
	    }

	    else if(key == 'm') {
	        // turns on and off the material listed in .mtl file
	        if(!bMaterial) {
	            model.enableMaterial();
	            bMaterial = true;
	        } 
	        else {
	            model.disableMaterial();
	            bMaterial = false;
	        }
	    }

	    else if(key == 's') {
	        if(!bStroke) {
	            stroke(10, 10, 10);
	            bStroke = true;
	        } 
	        else {
	            noStroke();
	            bStroke = false;
	        }
	    }

	    // the follwing changes the render modes
	    // POINTS mode is a little flakey in OPENGL (known processing bug)
	    // the best one to use is the one you exported the obj as
	    // when in doubt try TRIANGLES or POLYGON
	    else if(key=='1') {
	        stroke(10, 10, 10);
	        bStroke = true;
	        model.shapeMode(POINTS);
	    }

	    else if(key=='2') {
	        stroke(10, 10, 10);
	        bStroke = true;
	        model.shapeMode(LINES);
	    }

	    else if(key=='3') {
	        model.shapeMode(TRIANGLES);
	    }

	    else if(key=='4') {
	        model.shapeMode(POLYGON);
	    }

	    else if(key=='5') {
	        model.shapeMode(TRIANGLE_STRIP);
	    }

	    else if(key=='6') {
	        model.shapeMode(QUADS);
	    }

	    else if(key=='7') {
	        model.shapeMode(QUAD_STRIP);
	    }
	}


	public void mouseDragged()
	{
	    rotX += (mouseX - pmouseX) * 0.01;
	    rotY -= (mouseY - pmouseY) * 0.01;
	}
}
