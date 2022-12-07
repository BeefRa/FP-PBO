package application;

import java.util.List;
import java.util.Random;
import javafx.application.Application;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SpaceInvaders extends Application{
	private static final Random RAND = new Random();
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final int PLAYER_SIZE = 60;
	
	static final Image PLAYER_IMG = new Image("file:src/application/img/pemain.png");
	static final Image EXPLOSION_IMG = new Image("file:src/application/img/ledakan.png");
	
	static final int EXPLOSION_W = 128;
	static final int EXPLOSION_ROWS = 3;
	static final int EXPLOSION_COL = 3;
	static final int EXPLOSION_H = 128;
	static final int EXPLOSION_STEPS = 15;
	
	static final Image BOMBS_IMG[] = {
		new Image("file:src/application/img/bom1.png"),
		new Image("file:src/application/img/bom2.png"),
		new Image("file:src/application/img/bom3.png")
	};
	
	final int MAX_BOMBS = 10;
	final int MAX_SHOTS = MAX_BOMBS * 2;
	boolean gameOver = false;
	private GraphicsContext gc;
	
	Rocket player;
	List<Shot> shots;
	List<Universe> univ;
	List<Bomb> Bombs;

	@Override public void start(Stage arg0) throws Exception {
	}
	
	public class Rocket {
		int posX;
		int posY;
		int size;
		boolean exploding;
		boolean destroyed;
		Image img;
		int explosionStep = 0;
		
		public Rocket(int posX, int posY, int size, Image image) {
			this.posX = posX;
			this.posY = posY;
			this.size = size;
			img = image;
		}
		
		public Shot shoot() {
			return new Shot(posX + size / 2 - Shot.size / 2, posY - Shot.size);
		}
		
		public void update() {
			if (exploding) explosionStep++;
			destroyed = explosionStep > EXPLOSION_STEPS;
		}
		
		public void draw() {
			if (exploding) {
				gc.drawImage(EXPLOSION_IMG, explosionStep % EXPLOSION_COL * EXPLOSION_W, 
						(explosionStep / EXPLOSION_ROWS) * EXPLOSION_H + 1, EXPLOSION_W, EXPLOSION_H, posX, posY, size, size);
			} else {
				gc.drawImage(img, posX, posY, size, size);
			}
		}
		
		public boolean collide(Rocket other) {
			int d = distance(this.posX + size / 2, this.posY + size / 2, 
					other.posX + other.size / 2, other.posY + other.size / 2);
			
			return d < other.size / 2 + this.size / 2;
		}
		
		public void explode() {
			exploding = true;
			explosionStep = -1;
		}
	}
}