public class Spieler2 extends Spieler {
	public int pos;

	public Spieler2(Spiel spiel, int pos, String farbe) {
		super(spiel, pos, farbe);
		befehle.add("dat,-10");
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}
}
