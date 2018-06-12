import java.util.ArrayList;

public interface Zelle {
	ArrayList<Zelle> s = new ArrayList<Zelle>();

	public int getPos();

	public void setPos(int pos);

	public String getFarbe();

	public void setFarbe(String Farbe);

	public void fuehreAus(String[] befehl);

	public void doSomething();

	public int getDurchlauf();

	// Befehle
	public void move(int z);

	public void copyto(int z);

	public void dest(int z);

}