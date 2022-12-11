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
	
	public class Bomb extends Rocket {
		int SPEED = (score / 5) + 2;
		
		public Bomb(int posX, int posY, int size, Image image) {
			super(posX, posY, size, image);
		}
		
		public void update() {
			super.update();
			if(!exploding && !destroyed) posY += SPEED;
			if(posY > HEIGHT) destroyed = true;
		}
	}
	
	public class Shot {
		public boolean toRemove;
		
		int posX, posY = 10;
		int speed = 30;
		static final int size = 6;
		
		public Shot(int posX, int posY) {
			this.posX = posX;
			this.posY = posY;
		}
		
		public void update() {
			posY -= speed;
		}
		
		public void draw() {
			gc.setFill(Color.RED);
			if(score >= 50 && score <= 70 || score >= 120) {
				gc.setFill(Color.YELLOWGREEN);
				speed = 50;
				gc.fillRect(posX - 5, posY - 10, size + 10, size + 30);
			}
			else {
				gc.fillOval(posX, posY, size, size);
			}
		}
		
		public boolean colide(Rocket rocket) {
			int distance = distance(this.posX + size / 2, this.posY + size / 2,
					       Rocket.posX + Rocket.size / 2, Rocket.posY + Rocket.size / 2);
			return distance < Rocket.size / 2 + size / 2;
		}
	}
	
	public class Universe {
		int posX, posY;
		private int h, w, r, g, b;
		private double opacity;
		
		public Universe() {
			posX = RAND.nextInt(WIDTH);
			posY = 0;
			w = RAND.nextInt(5) + 1;
			h =  RAND.nextInt(5) + 1;
			r = RAND.nextInt(100) + 150;
			g = RAND.nextInt(100) + 150;
			b = RAND.nextInt(100) + 150;
			opacity = RAND.nextFloat();
			if(opacity < 0) opacity *= -1;
			if(opacity > 0.5) opacity = 0.5;
			
		}
		
		public void draw() {
			if(opacity > 0.8) opacity -= 0.01;
			if(opacity < 0.1) opacity += 0.01;
			gc.setFill(Color.rgb(r, g, b, opacity));
			gc.setOval(posX, posY, w, h);
			posY += 20;
		}
	}
	
	Bomb newBomb() {
		return new Bomb(50 + RAND.nextInt(WIDTH - 100), 0, PLAYER_SIZE, BOMBS_IMG[RAND.nextInt(BOMBS_IMG.length)]);
	}
	
	int distance(int x1, int y1, int x2, int y2) {
		return (int) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}
	
}
