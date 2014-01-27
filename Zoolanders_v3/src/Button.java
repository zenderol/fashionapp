public class Button {
	
	/*
	 * Button as a placeholder for cloth objects
	 */
	
	private int width;
	private int height;
	private int posX;
	private int posY;
	
	// data of cloth object
	private String path;
	private Type type;
	private float scale;
	private String imgPreview;
	
	private String categoryName;
	
	private double price;
	
	public static enum Type {
	    SHIRT, PANTS , SKIRTS, DRESSES,
	    RESET, CATEGORY, RETURN
	}

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
		this.price = -1;
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
		//System.out.println(this.toString());
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
//		System.out.println(path);
		this.imgPreview = path.substring(0, path.lastIndexOf(".")) + ".png";
//		System.out.println(imgPreview);
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
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

	public String getImgPreview() {
		return imgPreview;
	}

	public void setImgPreview(String imgPreview) {
		this.imgPreview = imgPreview;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}