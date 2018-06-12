import java.util.ArrayList;

public abstract class Spieler {
	int pos;
	String farbe;
	int programmzaehler = 0;
	int durchlauf = 0;
	Spiel spiel;
	ArrayList<String> befehle = new ArrayList<String>();

	public Spieler(Spiel spiel, int pos, String farbe) {
		this.spiel = spiel;
		this.pos = pos;
		this.farbe = farbe;
	}

	public void doSomething() {
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
		if (befehl[0].equals("add"))
			add(Integer.parseInt(befehl[1]), Integer.parseInt(befehl[2]));
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
		spiel.neuerSpieler(this.getClass(), pos + z, farbe);
	}

	public void add(int pos, int z) {
		String aendern = befehle.get(pos);
		String[] befehl = aendern.split(",");
		int neu = Integer.parseInt(befehl[1]) + z;
		befehl[2] = Integer.toString(neu);
		befehle.add(pos, befehl[0] + "," + befehl[1]);
	}

}
