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
import javafx.util.Duration;

import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class SpaceInvader {
	//run graphics
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
	//start
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
