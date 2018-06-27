import java.util.ArrayList;

public abstract class Spieler {
	int pos;
	String farbe;
	int programmzaehler = 0;
	int durchlauf = 0;
	Spiel spiel;
	ArrayList<String> befehle = new ArrayList<String>();
	ArrayList<Integer> bomben = new ArrayList<Integer>();

	public Spieler(Spiel spiel, int pos, String farbe) {
		this.spiel = spiel;
		this.pos = pos;
		this.farbe = farbe;
	}

	public void mach() {
		String aktuell = befehle.get(programmzaehler);
		String[] aktuellgetrennt = aktuell.split(",");
		programmzaehler++;
		if (programmzaehler >= befehle.size()) {
			programmzaehler = 0;
			durchlauf++;
		}
		fuehreAus(aktuellgetrennt);

	}

	public void fuehreAus(String[] befehl) {
		if (befehl[0].equals("mov"))
			move(Integer.parseInt(befehl[1]));
		if (befehl[0].equals("cp"))
			copyTo(Integer.parseInt(befehl[1]));
		if (befehl[0].equals("plus"))
			plus(Integer.parseInt(befehl[1]), Integer.parseInt(befehl[2]));
		if (befehl[0].equals("dat"))
			Dat(Integer.parseInt(befehl[1]));
	}

	public boolean istAufBombe() {
		for (int b : bomben) {
			if (pos == b)
				return true;
			else
				return false;
		}
		return false;
	}

	public ArrayList<Integer> getBomben() {
		return bomben;
	}

	public void setBomben(ArrayList<Integer> bomben) {
		this.bomben = bomben;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;

	}

	public String getFarbe() {
		return farbe;
	}

	public void setFarbe(String farbe) {
		this.farbe = farbe;
	}

	public int getDurchlauf() {
		return durchlauf;
	}

	// Befehle
	public void move(int z) {
		pos = pos + z;
	}

	public void copyTo(int z) {
		spiel.neuerSpieler(this.getClass(), pos + z, this.farbe);
	}

	/**
	 * zerstört Zellen
	 * 
	 * @param i
	 *            von dort an wird die Bombe plaziert
	 */
	public void Dat(int i) {
		bomben.add(pos + i);
	}

	public void plus(int pos, int z) {
		String aendern = befehle.get(pos);
		String[] befehl = aendern.split(",");
		int neu = Integer.parseInt(befehl[1]) + z;
		befehl[1] = Integer.toString(neu);
		befehle.add(pos, befehl[0] + "," + befehl[1]);
	}

}
