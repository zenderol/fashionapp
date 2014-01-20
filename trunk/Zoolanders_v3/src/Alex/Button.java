package Alex;

public class Button {
	
	private int width;
	
	private int height;
	
	private int posX;
	
	private int posY;
	
	private String path;
	private String type;
	private float scale;

	public Button(int posX, int posY) {
		this(100, 67, posX, posY);
	}
	
	public Button(int posX, int posY, int width, int height) {
		super();
		this.width = width;
		this.height = height;
		this.posX = posX;
		this.posY = posY;
		this.scale = 1.0f;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	protected void onClick() {
		System.out.println(this.toString());
	}
	
	public boolean isClicked(float clickX, float clickY, float clickZ) {
	      if (clickX >= getPosX() && clickX <= getPosX()+getWidth() && 
	    		  clickY >= getPosY() && clickY <= getPosY()+getHeight()) {
	            return true;
	          }
	      return false;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "Type: " + getType() + " Path: " + getPath();
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
}