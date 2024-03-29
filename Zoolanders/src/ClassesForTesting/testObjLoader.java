package ClassesForTesting;
import processing.core.PApplet;

import saito.objloader.*;


public class testObjLoader extends PApplet {
	OBJModel model ;

	float rotX, rotY;
	
	public void setup()
	{
	    size(800, 600, P3D);
	    frameRate(30);
	    model = new OBJModel(this, "..\\..\\models\\Shirts on hangers_only_black.obj", "absolute", TRIANGLES);
	    
	    model.enableMaterial();
	    model.enableTexture();
	    
	    
	    model.enableDebug();

	    model.scale(5);
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

	boolean bTexture = true;
	boolean bStroke = false;

	public void keyPressed()
	{
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

	    if(key == 's') {
	        if(!bStroke) {
	            stroke(255);
	            bStroke = true;
	        } 
	        else {
	            noStroke();
	            bStroke = false;
	        }
	    }

	    else if(key=='1')
	        model.shapeMode(POINTS);
	    else if(key=='2')
	        model.shapeMode(LINES);
	    else if(key=='3')
	        model.shapeMode(TRIANGLES);
	}

	public void mouseDragged()
	{
	    rotX += (mouseX - pmouseX) * 0.01;
	    rotY -= (mouseY - pmouseY) * 0.01;
	}
}
