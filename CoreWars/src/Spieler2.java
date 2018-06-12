public class Spieler2 extends Spieler {
	public Spieler2(Spiel spiel, int pos, String farbe) {
		super(spiel, pos, farbe);
		befehle.add("mov,-2");
	}

}
