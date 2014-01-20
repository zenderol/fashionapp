import java.util.ArrayList;
import java.util.List;


public class ClothFactory {
	
	public static List<Button> getAllPants(){
		List<Button> pants = new ArrayList<Button>();
		
		Button a = new Button(400, 0, 80, 80);
		a.setPath("..\\models\\pants\\pants_with_shoes.obj");
		a.setType(Button.Type.PANTS);
		a.setScale(0.35f);
		
		pants.add(a);
		
		return pants;
	}
	
	public static List<Button> getAllShirts(){
		List<Button> shirts = new ArrayList<Button>();
		
		Button a = new Button(500,60, 80, 80);
		a.setPath("..\\models\\shirts\\black_tshirt_nike.obj");
		a.setType(Button.Type.SHIRT);
		Button b = new Button(500, 160, 80, 80);
		b.setPath("..\\models\\shirts\\Polo_shirt_white.obj");
		b.setType(Button.Type.SHIRT);
		b.setScale(0.35f);	
		
		shirts.add(a);
		shirts.add(b);
		
		return shirts;
	}

}
