import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Spiel {
	Feld spielfeld = new Feld();
	ArrayList<Spieler> spieler = new ArrayList<Spieler>();
	ArrayList<Spieler> hinzu = new ArrayList<Spieler>();
	ArrayList<Spieler> weg = new ArrayList<Spieler>();

	public static void main(String[] args) {
		Spiel myGame = new Spiel();
	}

	public Spiel() {
		start();
	}

	public void start() {
		spieler.add(new Spieler1(this, 50, "rot"));
		spieler.add(new Spieler2(this, 300, "blau"));

		for (int i = 0; i < 100; i++) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Spieler z : spieler) {
				z.mach();
			}
			spieler.addAll(hinzu);
			hinzu.clear();
			zeichne();
			int ergebnis = check();
			if (ergebnis == 1)
				System.out.println("Blau gewinnt");
			if (ergebnis == 2)
				System.out.println("Rot gewinnt");
		}
		System.out.println("unentschieden");
	}

	public void zeichne() {
		spielfeld.zeichne(spieler, spieler.get(1).getBomben());
	}

	public int check() {
		int anzBl = 0;
		int anzRo = 0;
		for (Spieler z : spieler) {
			if (z.getFarbe().equals("rot"))
				anzRo++;
			if (z.getFarbe().equals("blau"))
				anzBl++;
		}
		if (anzRo == 0)
			return 1;
		if (anzBl == 0)
			return 2;
		return 0;
	}

	public void neuerSpieler(Class sp, int pos, String farbe) {
		if (sp.getName().equals("Spieler1"))
			hinzu.add(new Spieler1(this, pos, farbe));
		if (sp.getName().equals("Spieler2"))
			hinzu.add(new Spieler2(this, pos, farbe));
	}

	public void SpielerLoeschen() {
		for (Spieler s : spieler) {
			if (s.istAufBombe() == true)
				weg.add(s);
		}
		for (Spieler s : spieler) {
			for (Spieler w : weg) {
				if (s == w)
					spieler.remove(s);
			}
		}

	}

}
