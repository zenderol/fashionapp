


import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import SimpleOpenNI.SimpleOpenNI;

public abstract class UserInterface {

	private PApplet app;
	
	private SimpleOpenNI context;
	
	private List<Button> buttons = new ArrayList<Button>();
	
	
	public UserInterface(PApplet app, SimpleOpenNI context) {
		this.app = app;
		this.context = context;
	}
	
	/**
	 * adds a button to the ui so it will be drawn when calling draw()
	 * @param button
	 */
	public void add(Button button) {
		buttons.add(button);
	}
	
	public void addList(List<Button> list){
		buttons.addAll(list);
	}
	
	public abstract void draw();

	/**
	 * process a click to the ui
	 * 
	 * the ui decides wheter a ui element was hit and invokes appropriate method (e.g. onClick() of Button)
	 * 
	 * @param clickX
	 * @param clickY
	 * @param clickZ
	 */
	public Button click(float clickX, float clickY, float clickZ) {
		
		for (Button button : buttons) {
			if (button.isClicked(clickX, clickY, clickZ)) {
				button.onClick();
				return button;
			}
		}
		return null;
	}
	
	
	public List<Button> getButtons() {
		return buttons;
	}
	
	public void clearGUI(){
		this.buttons.clear();
	}

	

	
}
