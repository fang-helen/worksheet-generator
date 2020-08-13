
public class MainTest {

	public static void main(String[] args) {
		
		Worksheet w = new Worksheet(10,3,2,2, true);
		w.setTitle("July 26");
		System.out.println(w.printSheet());
		System.out.println(w.printKey());

		Worksheet w1 = new Worksheet(10,4,2,2,true);
		System.out.println(w1.printSheet());
		System.out.println(w1.printKey());

		
		
	}

}
