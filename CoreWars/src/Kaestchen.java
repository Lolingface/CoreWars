
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * The Class Kaestchen.
 *
 * @author A. Eckert GMG Amberg Feb 2017
 */

public abstract class Kaestchen extends javax.swing.JFrame {
	private static final long serialVersionUID = 5566605613197470551L;
	private static int randLinks = 20;
	private static int randOben = 20;
	private static int randRechts = 20;
	private static int randUnten = 20;
	private static int kBreite = 20;
	private static int kHoehe = 20;
	private static int kAnzX = 50;
	private static int kAnzY = 50;
	private static int rahmenBreite = 1;
	private Color strichfarbe = Color.black;
	private JPanel jPanel1;
	private static int fensterhoehe;
	private static int fensterbreite;
	private Color[][] matrix;
	private String[][] text;
	private static TreeMap<Integer, MyText> texte = new TreeMap<Integer, MyText>();
	private Image[][] bild;
	private String[][] bildnamen;
	private Hashtable<String, Image> bilder = new Hashtable<String, Image>();
	private Image hintergrund = null;
	private static TreeMap<Integer, MyObject> objekte = new TreeMap<Integer, MyObject>();
	private static JFrame zeich;
	private boolean transparent=false;

	/**
	 * Konstruktoren, Standard erzeugt 20x20 Kästchen der Breite 20 und Höhe
	 * 20.
	 */
	public Kaestchen() {
		this(20, 20, 20, 20);
	}

	public Kaestchen(int kaeBreite, int kaeHoehe, int kaeAnzX, int kaeAnzY) {
		this(kaeBreite, kaeHoehe, kaeAnzX, kaeAnzY, false);
	}

	public Kaestchen(int kaeBreite, int kaeHoehe, int kaeAnzX, int kaeAnzY, boolean ohneRand) {
		zeich = this;
		if (!ohneRand) {
			strichfarbe = Color.black;
			rahmenBreite = 1;
		} else {
			strichfarbe = Color.white;
			rahmenBreite = 0;
			setzeRaender(0, 0, 0, 0);
		}
		Kaestchen.kAnzX = kaeAnzX;
		Kaestchen.kAnzY = kaeAnzY;
		Kaestchen.kBreite = kaeBreite;
		Kaestchen.kHoehe = kaeHoehe;
		fensterhoehe = kAnzY * kHoehe + randUnten + randOben;
		fensterbreite = kAnzX * kBreite + randLinks + randRechts;
		matrix = new Color[kAnzX][kAnzY];
		for (int i = 0; i < kAnzX; i++) {
			for (int j = 0; j < kAnzY; j++) {
				matrix[i][j] = Color.WHITE;
			}
		}
		text = new String[kAnzX][kAnzY];
		bildnamen = new String[kAnzX][kAnzY];
		bild = new Image[kAnzX][kAnzY];
		this.addKeyListener(new KeyAdapter() {
			private TreeSet<Integer> tasten = new TreeSet<Integer>();

			@Override
			public void keyReleased(KeyEvent evt) {
				tasten.remove(Integer.valueOf(evt.getKeyCode()));
			}

			@Override
			public void keyPressed(KeyEvent evt) {
				if (tasten.contains(Integer.valueOf(evt.getKeyCode()))) return;
				String taste = KeyEvent.getKeyText(evt.getKeyCode());
				tasten.add(Integer.valueOf(evt.getKeyCode()));
				tasteClick(taste);
			}

		});
		jPanel1 = new JPanel() {
			private static final long serialVersionUID = 1651104240296058955L;

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				if (hintergrund != null) {
					g.drawImage(hintergrund, 0, 0, fensterbreite, fensterhoehe, null);
				}
				g.setColor(strichfarbe);
				for (int x = 0; x <= kAnzX; x++) {
					g.drawLine(randLinks + x * kBreite, randOben, randLinks + x * kBreite, fensterhoehe - randUnten);
				}
				for (int y = 0; y <= kAnzY; y++) {
					g.drawLine(randLinks, randOben + y * kHoehe, fensterbreite - randRechts, randOben + y * kHoehe);
				}
				for (int x = 0; x < kAnzX; x++) {
					for (int y = 0; y < kAnzY; y++) {
						if (transparent && matrix[x][y]==Color.white) continue; 
						g.setColor(matrix[x][y]);
						g.fillRect(randLinks + x * kBreite + rahmenBreite, randOben + y * kHoehe + rahmenBreite, kBreite - rahmenBreite, kHoehe - rahmenBreite);
					}
				}
				g.setColor(Color.black);
				for (int x = 0; x < kAnzX; x++) {
					for (int y = 0; y < kAnzY; y++) {
						if (text[x][y] != null) {
							int breite = g.getFontMetrics().stringWidth(text[x][y]);
							int hoehe = g.getFontMetrics().getAscent();
							g.drawString(text[x][y], (int) (randLinks + (x + 0.5) * kBreite - 0.5 * breite),
									(int) (randOben + (y + 0.5) * kHoehe + 0.5 * hoehe));
						}
						if (bild[x][y] != null) {
							g.drawImage(bild[x][y], randLinks + 1 + x * kBreite, randOben + 1 + y * kHoehe, kBreite - 1, kHoehe - 1, null);
						}
					}
				}
				for (Map.Entry<Integer, MyObject> obj : objekte.entrySet()) {
					MyObject o = obj.getValue();
					g.drawImage(o.bild, (int) (randLinks + 1 + (o.x - 1) * kBreite), (int) (randOben + 1 + (o.y - 1) * kHoehe), (int) o.breite * kBreite - 1,
							(int) o.hoehe * kHoehe - 1, null);
				}
				for (Map.Entry<Integer, MyText> obj : texte.entrySet()) {
					MyText o = obj.getValue();
					g.setFont(new Font("Comic Sans", Font.BOLD, o.fontsize));
					g.drawString(o.text, o.x, o.y);
				}

			}
		};
		Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
		setIconImage(icon);
		getContentPane().add(jPanel1, BorderLayout.CENTER);
		jPanel1.setBackground(new java.awt.Color(255, 255, 255));
		jPanel1.setPreferredSize(new java.awt.Dimension(1044, 1042));
		jPanel1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent evt) {
				int x = (evt.getX() - randLinks + kBreite) / kBreite;
				int y = (evt.getY() - randOben + kHoehe) / kHoehe;
				if (x < 1 || x > kAnzX) x = -1;
				if (y < 1 || y > kAnzY) y = -1;
				mausClick(x, y);
				if (evt.getButton() == MouseEvent.BUTTON1) mausLeftClick(x, y);
				if (evt.getButton() == MouseEvent.BUTTON3) mausRightClick(x, y);
			}
		});
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Kaestchen 2017");
		getContentPane().setBackground(new java.awt.Color(255, 255, 255));
		getContentPane().setPreferredSize(new Dimension(fensterbreite, fensterhoehe));
		pack();
		setVisible(true);
		repaint();
	}

	public static void setzeRaender(int links, int unten, int rechts, int oben) {
		randLinks = links;
		randUnten = unten;
		randOben = oben;
		randRechts = rechts;
		fensterhoehe = kAnzY * kHoehe + randUnten + randOben;
		fensterbreite = kAnzX * kBreite + randLinks + randRechts;
		zeich.getContentPane().setPreferredSize(new Dimension(fensterbreite, fensterhoehe));
		zeich.pack();
		zeich.repaint();
	}

	/**
	 * Achtung: zu ueberschreibende Methode, muss genau so uebernommen werden:
	 * 
	 * public void mausClick(int x, int y) { ... }
	 * 
	 * @param x
	 * @param y
	 */
	public void mausClick(int x, int y) {
		// Noch leer
	}

	/**
	 * Achtung: zu ueberschreibende Methode, muss genau so uebernommen werden:
	 * public void mausClick(int x, int y) { ... }
	 * 
	 * @param x
	 * @param y
	 */
	public void mausLeftClick(int x, int y) {
		// Noch leer
	}

	/**
	 * Achtung: zu ueberschreibende Methode, muss genau so uebernommen werden:
	 * public void mausClick(int x, int y) { ... }
	 * 
	 * @param x
	 * @param y
	 */
	public void mausRightClick(int x, int y) {
		// Noch leer
	}

	/**
	 * Achtung: zu ueberschreibende Methode, muss genau so uebernommen werden:
	 * public void tasteClick(String taste) { ... }
	 * 
	 * @param taste
	 */
	public void tasteClick(String taste) {
		// Noch leer
	}

	/**
	 * Farbe setzen.
	 * 
	 * @param farbe
	 *            - weiss, gruen, rot, blau, schwarz, grau, hellgrau, cyan,
	 *            orange, pink, gelb
	 */
	public void farbeSetzen(int x, int y, String farbe) {
		if (x < 1 || x > kAnzX) return;
		if (y < 1 || y > kAnzY) return;
		if (farbe.equals("weiss")) matrix[x - 1][y - 1] = Color.WHITE;
		else if (farbe.equals("gruen")) matrix[x - 1][y - 1] = Color.GREEN;
		else if (farbe.equals("rot")) matrix[x - 1][y - 1] = Color.RED;
		else if (farbe.equals("blau")) matrix[x - 1][y - 1] = Color.BLUE;
		else if (farbe.equals("schwarz")) matrix[x - 1][y - 1] = Color.BLACK;
		else if (farbe.equals("grau")) matrix[x - 1][y - 1] = Color.GRAY;
		else if (farbe.equals("hellgrau")) matrix[x - 1][y - 1] = Color.LIGHT_GRAY;
		else if (farbe.equals("cyan")) matrix[x - 1][y - 1] = Color.CYAN;
		else if (farbe.equals("orange")) matrix[x - 1][y - 1] = Color.ORANGE;
		else if (farbe.equals("pink")) matrix[x - 1][y - 1] = Color.PINK;
		else if (farbe.equals("gelb")) matrix[x - 1][y - 1] = Color.YELLOW;
		else return;
		repaint();
	}

	public void farbeSetzen(int x, int y, Color farbe) {
		if (x < 1 || x > kAnzX) return;
		if (y < 1 || y > kAnzY) return;
		matrix[x - 1][y - 1] = farbe;
	}

	/**
	 * Farbe geben.
	 *
	 * @return weiss, gruen, blau, ...
	 */
	public String farbeGeben(int x, int y) {
		if (x < 1 || x > kAnzX) return "";
		if (y < 1 || y > kAnzY) return "";
		if (matrix[x - 1][y - 1] == Color.WHITE) return "weiss";
		else if (matrix[x - 1][y - 1] == Color.GREEN) return "gruen";
		else if (matrix[x - 1][y - 1] == Color.RED) return "rot";
		else if (matrix[x - 1][y - 1] == Color.blue) return "blau";
		else if (matrix[x - 1][y - 1] == Color.BLACK) return "schwarz";
		else if (matrix[x - 1][y - 1] == Color.GRAY) return "grau";
		else if (matrix[x - 1][y - 1] == Color.LIGHT_GRAY) return "hellgrau";
		else if (matrix[x - 1][y - 1] == Color.CYAN) return "cyan";
		else if (matrix[x - 1][y - 1] == Color.ORANGE) return "orange";
		else if (matrix[x - 1][y - 1] == Color.PINK) return "pink";
		else if (matrix[x - 1][y - 1] == Color.YELLOW) return "gelb";
		else return "";
	}

	/**
	 * Farbe loeschen.
	 */
	public void farbeLoeschen(int x, int y) {
		if (x < 1 || x > kAnzX) return;
		if (y < 1 || y > kAnzY) return;
		matrix[x - 1][y - 1] = Color.WHITE;
		repaint();
	}

	/**
	 * Text pixelgenau setzen
	 * 
	 * Jeder Text braucht eine eindeutige Nummer
	 */
	public void textSetzenPixel(int nr, int x, int y, String s) {
		texte.put(Integer.valueOf(nr), new MyText(x, y, 20, s));
		repaint();
	}

	/**
	 * Text pixelgenau setzen mit Schriftgroesse.
	 *
	 */
	public void textSetzenPixel(int nr, int x, int y, int fontsize, String s) {
		texte.put(Integer.valueOf(nr), new MyText(x, y, fontsize, s));
		repaint();
	}

	/**
	 * pixelgenauen Text loeschen
	 *
	 */
	public void textLoeschenPixel(int nr) {
		texte.remove(Integer.valueOf(nr));
		repaint();
	}

	/**
	 * Text setzen
	 *
	 */
	public void textSetzen(int x, int y, String s) {
		if (x < 1 || x > kAnzX) return;
		if (y < 1 || y > kAnzY) return;
		text[x - 1][y - 1] = s;
		repaint();
	}

	/**
	 * Text zurueckgeben
	 */
	public String textGeben(int x, int y) {
		if (x < 1 || x > kAnzX) return null;
		if (y < 1 || y > kAnzY) return null;
		return text[x - 1][y - 1];
	}

	/**
	 * Text loeschen
	 */
	public void textLoeschen(int x, int y) {
		if (x < 1 || x > kAnzX) return;
		if (y < 1 || y > kAnzY) return;
		text[x - 1][y - 1] = null;
		repaint();
	}

	/**
	 * Bild setzen
	 * 
	 * @param s
	 *            - dateiname
	 */
	public void bildSetzen(int x, int y, String s) {
		if (!new File(s).exists()) {
			meldung("Bildname unbekannt: " + s);
			System.exit(0);
		}
		if (x < 1 || x > kAnzX) return;
		if (y < 1 || y > kAnzY) return;
		if (bilder.containsKey(s)) {
			bild[x - 1][y - 1] = bilder.get(s);
			bildnamen[x - 1][y - 1] = s;
		} else try {
			Image b = new ImageIcon(s).getImage();
			// Image b = ImageIO.read(new File(s));
			bild[x - 1][y - 1] = b;
			bildnamen[x - 1][y - 1] = s;
			bilder.put(s, b);
		} catch (Exception e) {
			meldung("Bild laden fehlerhaft: " + s);
			System.exit(0);
		}
	}

	/**
	 * Bildname zurueckgeben
	 * 
	 * @return dateiname des Bildes
	 */
	public String bildnameGeben(int x, int y) {
		return bildnamen[x - 1][y - 1];
	}

	/**
	 * Bild loeschen
	 */
	public void bildLoeschen(int x, int y) {
		if (x < 1 || x > kAnzX) return;
		if (y < 1 || y > kAnzY) return;
		bild[x - 1][y - 1] = null;
		bildnamen[x - 1][y - 1] = null;
		repaint();
	}

	/**
	 * Meldung ausgeben
	 *
	 * @param text
	 *            - Auszugebender Text
	 */
	public static void meldung(String text) {
		JOptionPane.showMessageDialog(null, text);
	}

	/**
	 * warten
	 *
	 * @param ms
	 *            - zu wartenden Zeit in ms (kleiner/gleich 1000)
	 */
	public void warte(int ms) {
		jPanel1.paintImmediately(0, 0, jPanel1.getWidth(), jPanel1.getHeight());
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Hintergrundbild anzeigen.
	 *
	 * @param filename
	 *            - Dateiname
	 */
	public void hintergrundbildAnzeigen(String filename) {
		try {
			Image b = ImageIO.read(new File(filename));
			hintergrund = b;
		} catch (IOException e) {
			meldung("Bildname unbekannt: " + filename);
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * Sound erzeugen
	 *
	 * @param filename
	 *            - Dateiname
	 */
	public void sound(String filename) {
		Clip clip;
		try {
			clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(filename));
			clip.open(inputStream);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Objekt erzeugen
	 *
	 * @param nr
	 *            - eindeutige Nummer
	 */
	public void objektErzeugen(int nr, double x, double y, double breite, double hoehe, String filename) {
		new MyObject(nr, x, y, breite, hoehe, filename);
	}

	/**
	 * Objekt löschen
	 *
	 */
	public void objektLoeschen(int nr) {
		objekte.remove(Integer.valueOf(-nr));
		zeich.repaint();
	}

	/**
	 * Objekt bewegt sich
	 * 
	 * - return boolean
	 *
	 */
	public boolean objektBewegtSich(int nr) {
		MyObject wer = objekte.get(Integer.valueOf(-nr));
		return wer.started;
	}

	/**
	 * Objekt bewegen
	 *
	 */
	public void objektBewegen(int nr, int wohinX, int wohinY, int speed) {
		MyObject wer = objekte.get(Integer.valueOf(-nr));
		if (wer.isStarted()) return;
		Thread a = new Thread() {
			@Override
			public void run() {
				wer.setStarted(true);
				double disx = (wohinX - wer.x) / 10;
				double disy = (wohinY - wer.y) / 10;
				for (int i = 0; i < 10; i++) {
					wer.x = wer.x + disx;
					wer.y = wer.y + disy;
					zeich.repaint((int) (randLinks + (wer.x - 2) * kBreite), (int) (randOben + (wer.y - 1) * kHoehe), 3 * kBreite, 3 * kHoehe);
					try {
						Thread.sleep(speed);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				wer.setStarted(false);
			}
		};
		a.start();
		zeich.repaint();
	}

	/**
	 * Klasse MyText.
	 */
	private static class MyText {
		int x, y;
		int fontsize;
		String text;

		public MyText(int x, int y, int fontsize, String text) {
			super();
			this.x = x;
			this.y = y;
			this.fontsize = fontsize;
			this.text = text;
		}
	}

	/**
	 * Klasse MyObject.
	 */
	private static class MyObject {
		private boolean started = false;
		double x;
		double y;
		double breite;
		double hoehe;
		Image bild;

		public MyObject(int nr, double x, double y, double breite, double hoehe, String name) {
			this.x = x;
			this.y = y;
			this.breite = breite;
			this.hoehe = hoehe;
			try {
				Image b = new ImageIcon(name).getImage();
				this.bild = b;
				objekte.put(Integer.valueOf(-nr), this);
			} catch (Exception e) {
				meldung("Bildname unbekannt: " + name);
				System.out.println(e.getMessage());
				System.exit(0);
			}
			zeich.repaint();
		}

		public boolean isStarted() {
			return started;
		}

		public void setStarted(boolean started) {
			this.started = started;
		}

	}
}