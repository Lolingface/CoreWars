import java.util.ArrayList;

public class Feld extends Kaestchen {
	int[] zellen = new int[513];

	public Feld() {
		super(2, 20, 512, 1, true);
	}

	public void zeichne(ArrayList<Spieler> spieler) {
		for (int i = 0; i < zellen.length; i++) {
			farbeSetzen(i, 1, "weiss");
		}
		for (Spieler z : spieler)
			farbeSetzen(z.getPos(), 1, z.getFarbe());
	}

}
