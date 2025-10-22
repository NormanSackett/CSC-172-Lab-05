import java.util.Iterator;

public class UnitTest {

	public static void main(String[] args) {
		URHashTable<String, String> hT = new URHashTable<String, String>(3);
		System.out.println("empty?: " + hT.isEmpty());
		hT.put("a", "e");
		hT.put("b", "f");
		hT.put("c", "g");
		System.out.print(hT.toString() + "\n");
		hT.put("d", "h");
		hT.put("i", "k");
		hT.put("j", "l");
		System.out.print(hT.toString());
		System.out.println(hT.get("d"));
		hT.delete("d");
		System.out.println(hT.toString());
		System.out.println("size: " + String.valueOf(hT.size()) + "\nempty?: " + hT.isEmpty() + "\ncontains 'i'?: " + hT.contains("i"));
		
		Iterator<String> iter = hT.iterator();
		while (true) {
			System.out.print(iter.next());
			if (iter.hasNext()) {
				System.out.print(", ");
			} else {
				break;
			}
		}
	}

}
