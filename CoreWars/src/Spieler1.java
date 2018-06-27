
public class Spieler1 extends Spieler {
	public int pos;

	public Spieler1(Spiel spiel, int pos, String farbe) {
		super(spiel, pos, farbe);
		befehle.add("mov,1");

	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

}
