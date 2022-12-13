package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.stream.IntStream;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
=======
import javafx.util.Duration;

import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
>>>>>>> ded4a4bda4c31f400cfc55de0f0fe21da4859825
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

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
		
<<<<<<< HEAD
=======
		//run graphics
>>>>>>> ded4a4bda4c31f400cfc55de0f0fe21da4859825
		private void run(GraphicsContext gc) {
			gc.setFill(Color.grayRgb(20));
			gc.fillRect(0, 0, WIDTH, HEIGHT);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setFont(Font.font(20));
			gc.setFill(Color.WHITE);
			gc.fillText("Score: " + score, 60, 20);
			
			if(gameOver) {
				gc.setFont(Font.font(35));
				gc.setFill(Color.YELLOW);
				gc.fillText("GameOver \n Your Score is: " + score + "\nClick to play again", WIDTH/2, HEIGHT/2.5);
			}
			univ.forEach(Universe::draw);
			
			player.update();
			player.draw();
			player.posX= (int) mouseX;
			
			Bombs.stream().peek(Rocket::update).peek(Rocket::draw).forEach(e ->{
				if(player.colide(e) && !player.exploding) {
					player.explode();
				}
			});
			
			for(int i = shots.size() - 1; i >= 0 ; i--) {
				Shot shot = shots.get(i);
				if(shot.posY <0 || shot.toRemove) {
					shots.remove(i);
					continue;
				}
				shot.update();
				shot.draw();
				for(Bomb bomb : Bombs) {
					if(shot.colide(bomb) && !bomb.exploding) {
						score++;
						bomb.explode();
						shot.toRemove= true;
					}
				}
			}
			
			for(int i = Bombs.size() - 1; i>=0; i--) {
				if(Bombs.get(i).destroyed) {
					Bombs.set(i,newBomb());
				}
			}
			
			gameOver = player.destroyed;
			if(RAND.nextInt(10)>2) {
				univ.add(new Univearse());
			}
			for(int i = 0; i< univ.size(); i++) {
				if(univ.get(i).posY > HEIGHT)
					univ.remove(i);
			}
		}
<<<<<<< HEAD

=======
		//start
>>>>>>> ded4a4bda4c31f400cfc55de0f0fe21da4859825
		public void start(Stage stage) throws Exception{
			Canvas canvas = new Canvas(WIDTH, HEIGHT);
			gc = canvas.getGraphicsContext2D();
			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e ->run(gc)));
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.play();
			canvas.setCursor(Cursor.MOVE);
			canvas.setOnMouseMoved(e -> mouseX = e.getX());
			canvas.setOnMouseClicked(e ->{
				if(shots.size() < MAX_SHOTS) shots.add(player.shoot());
				if(gameOver) {
					gameOver = false;
					setup();
				}
			});
			setup();
			stage.setScene(new Scene (new StackPane(canvas)));
			stage.setTitle("Space Invaders");
			stage.show();	
		}
	}
}