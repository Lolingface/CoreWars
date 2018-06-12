
public class Spieler1 extends Spieler {

	public Spieler1(Spiel spiel, int i, String farbe) {
		super(spiel, i, farbe);
		befehle.add("mov,1");
		befehle.add("add,1,1");
	}

}
