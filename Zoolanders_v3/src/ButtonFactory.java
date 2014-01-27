import java.util.ArrayList;
import java.util.List;


public class ButtonFactory {
	
	private final static int XPOS = 460;
	
	public static List<Button> getAllPants(){
		List<Button> pants = new ArrayList<Button>();
		addReturnAndDeleteButton(pants);
		
		Button a = new Button(XPOS, 70, 80, 80);
		a.setPath("..\\models\\pants\\pants_with_shoes.obj");
		a.setType(Button.Type.PANTS);
		a.setScale(0.35f);
		a.setPrice(49.99);
		
		Button b = new Button(XPOS, 170, 80, 80);
		b.setPath("..\\models\\pants\\blue_jeans.obj");
		b.setType(Button.Type.PANTS);
		b.setScale(0.19f);
		b.setPrice(24.99);
		
		pants.add(a);
		pants.add(b);
		
		return pants;
	}
	
	public static List<Button> getAllShirts(){
		List<Button> shirts = new ArrayList<Button>();
		addReturnAndDeleteButton(shirts);
		
		Button a = new Button(XPOS, 70, 80, 80);
		a.setPath("..\\models\\shirts\\black_tshirt_nike.obj");
		a.setType(Button.Type.SHIRT);
		a.setPrice(19.99);
		Button b = new Button(XPOS, 170, 80, 80);
		b.setPath("..\\models\\shirts\\Polo_shirt_white.obj");
		b.setType(Button.Type.SHIRT);
		b.setScale(0.3f);
		b.setPrice(29.99);
		Button c = new Button(XPOS, 270, 80, 80);
		c.setPath("..\\models\\shirts\\red_tshirt_nike.obj");
		c.setType(Button.Type.SHIRT);
		c.setScale(0.4f);
		c.setPrice(19.99);
		Button d = new Button(XPOS, 370, 80, 80);
		d.setPath("..\\models\\shirts\\blue_t-shirt_nike.obj");
		d.setType(Button.Type.SHIRT);
		d.setScale(0.4f);
		d.setPrice(19.99);
		
		shirts.add(a);
		shirts.add(b);
		shirts.add(c);
		shirts.add(d);
		
		return shirts;
	}
	
	private static void addReturnAndDeleteButton(List<Button> list){		
		Button bReturn = new Button(XPOS, 10, 40, 40);
		bReturn.setPath("..\\images\\GUI\\return.png");
		bReturn.setType(Button.Type.RETURN);
		
		Button bReset = new Button(XPOS+60, 10, 40, 40);
		bReset.setPath("..\\images\\GUI\\reset.png");
		bReset.setType(Button.Type.RESET);
		
		list.add(bReturn);
		list.add(bReset);
	}
	
	public static List<Button> getAllCategories(){
		List<Button> categories = new ArrayList<Button>();
		
		Button bReset = new Button(XPOS+60, 10, 40, 40);
		bReset.setPath("..\\images\\GUI\\reset.png");
		bReset.setType(Button.Type.RESET);
		
		Button a = new Button(443 ,60, 186, 60);
		a.setCategoryName("shirts");
		a.setImgPreview("..\\images\\GUI\\tshirts-button.png");
		a.setType(Button.Type.CATEGORY);		
		Button b = new Button(443, 140, 186, 60);
		b.setCategoryName("pants");
		b.setImgPreview("..\\images\\GUI\\pants-button.png");
		b.setType(Button.Type.CATEGORY);
		Button c = new Button(443, 220, 186, 60);
		c.setCategoryName("dresses");
		c.setImgPreview("..\\images\\GUI\\dresses-button.png");
		c.setType(Button.Type.CATEGORY);
		Button d = new Button(443, 300, 186, 60);	
		d.setCategoryName("skirts");
		d.setImgPreview("..\\images\\GUI\\skirts-button.png");
		d.setType(Button.Type.CATEGORY);
		
		categories.add(bReset);
		categories.add(a);
		categories.add(b);
		categories.add(c);
		categories.add(d);
		
		return categories;
	}
	
	public static List<Button> getAllDresses(){
		List<Button> dresses = new ArrayList<Button>();
		addReturnAndDeleteButton(dresses);
		
		Button a = new Button(XPOS, 70, 80, 80);
		a.setPath("..\\models\\dresses\\black_dress_scale120.obj");
		a.setType(Button.Type.DRESSES);
		a.setScale(50);
		a.setPrice(39.99);
		
		Button b = new Button(XPOS, 170, 80, 80);
		b.setPath("..\\models\\dresses\\green_dress_scale120.obj");
		b.setType(Button.Type.DRESSES);
		b.setScale(50);
		b.setPrice(34.99);
		
		dresses.add(a);
		dresses.add(b);
		
		return dresses;
	}
	
	public static List<Button> getAllSkirts(){
		List<Button> skirts = new ArrayList<Button>();
		addReturnAndDeleteButton(skirts);
		
		Button a = new Button(XPOS, 70, 80, 80);
		a.setPath("..\\models\\skirts\\???.obj");
		a.setType(Button.Type.SKIRTS);
		a.setScale(0.35f);
		
		skirts.add(a);
		
		return skirts;
	}
	

}
