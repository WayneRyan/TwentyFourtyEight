import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class MainClass extends JFrame implements Runnable, KeyListener {
	public static final int WIDTH = 800;
	public static final int SHIFT = 30;
	public static final int HEIGHT = WIDTH + SHIFT;

	BufferedImage offscreen, background;
	Graphics bg;
	Tile[][] grid;

	public MainClass() {
		grid = new Tile[4][4];
		for (int i = 0; i < 2; i++) {
			int r = (int) (Math.random() * 4);
			int c = (int) (Math.random() * 4);
			grid[r][c] = new Tile(r, c);
		}
		offscreen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		try {
			background = ImageIO.read(MainClass.class
					.getResourceAsStream("/resources/2048.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		bg = offscreen.getGraphics();
		InputStream is = MainClass.class
				.getResourceAsStream("/resources/myFont.ttf");
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, is)
					.deriveFont(120f);
			bg.setFont(font);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.addKeyListener(this);
		new Thread(this).start();
	}

	public static void main(String[] args) {
		MainClass mc = new MainClass();
		mc.setSize(WIDTH, HEIGHT);
		mc.setResizable(false);
		mc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mc.setVisible(true);
	}

	public void paint(Graphics g) {
		bg.drawImage(background, 0, SHIFT, null);
		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < 4; c++) {
				if (grid[r][c] != null)
					grid[r][c].draw(bg);
			}
		}
		g.drawImage(offscreen, 0, 0, null);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(30);
				repaint();
			} catch (InterruptedException e) {
			}
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			shiftUp();
			combineVertical();
			shiftUp();			
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			shiftDown();
			combineVertical();
			shiftDown();
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			shiftLeft();
			combineHorizontal();
			shiftLeft();
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			shiftRight();
			combineHorizontal();
			shiftRight();
		}
		// add two new tiles
		int r = (int) (Math.random() * 4);
		int c = (int) (Math.random() * 4);
		int count = 0;
		while (grid[r][c] != null) {
			r = (int) (Math.random() * 4);
			c = (int) (Math.random() * 4);
			if (count++ > 10000)
				return;
		}
		grid[r][c] = new Tile(r, c);

	}

	private void combineVertical() {
		for (int c = 0; c < 4; c++) {
			for (int r = 0; r < 3; r++) {
				if (grid[r][c] != null && grid[r + 1][c] != null
						&& grid[r][c].value == grid[r + 1][c].value) {
					grid[r][c].value *= 2;
					grid[r + 1][c] = null;
				}
			}
		}
	}

	private void combineHorizontal() {
		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < 3; c++) {
				if (grid[r][c] != null && grid[r][c+1] != null
						&& grid[r][c].value == grid[r][c+1].value) {
					grid[r][c].value *= 2;
					grid[r][c+1] = null;
				}
			}
		}
	}

	private void shiftRight() {
		for (int r = 0; r < 4; r++) {
			for (int c = 3; c > 0; c--) {
				if (grid[r][c] == null) {
					int index = c - 1;
					while (index >= 0 && grid[r][index] == null)
						index--;
					if (index >= 0) {
						grid[r][c] = grid[r][index];
						grid[r][index] = null;
						grid[r][c].update(r, c);
					} else {
						break;
					}
				}
			}
		}
		repaint();
	}

	private void shiftLeft() {
		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < 3; c++) {
				if (grid[r][c] == null) {
					int index = c + 1;
					while (index < 4 && grid[r][index] == null)
						index++;
					if (index < 4) {
						grid[r][c] = grid[r][index];
						grid[r][index] = null;
						grid[r][c].update(r, c);
					} else {
						break;
					}
				}
			}
		}
		repaint();
	}

	private void shiftDown() {
		for (int c = 0; c < 4; c++) {
			for (int r = 3; r > 0; r--) {
				if (grid[r][c] == null) {
					int index = r - 1;
					while (index >= 0 && grid[index][c] == null)
						index--;
					if (index >= 0) {
						grid[r][c] = grid[index][c];
						grid[index][c] = null;
						grid[r][c].update(r, c);
					} else {
						break;
					}
				}
			}
		}
		repaint();
	}

	private void shiftUp() {
		for (int c = 0; c < 4; c++) {
			for (int r = 0; r < 3; r++) {
				if (grid[r][c] == null) {
					int index = r + 1;
					while (index < 4 && grid[index][c] == null)
						index++;
					if (index < 4) {
						grid[r][c] = grid[index][c];
						grid[index][c] = null;
						grid[r][c].update(r, c);
					} else {
						break;
					}
				}
			}
		}
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
