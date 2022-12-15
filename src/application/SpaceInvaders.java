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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SpaceInvaders extends Application {
	private static final Random RAND = new Random();
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final int PLAYER_SIZE = 60;
	

	static final Image PLAYER_IMG = new Image("file:src/application/img/player.png");
	static final Image EXPLOSION_IMG = new Image("file:src/application/img/ded.png");
	
	static final int EXPLOSION_W = 128;
	static final int EXPLOSION_H = 128;
	static final int EXPLOSION_ROWS = 3;
	static final int EXPLOSION_COL = 3;
	static final int EXPLOSION_STEPS = 15;

	static final Image BOMBS_IMG[] = {
		new Image("file:src/application/img/1.png"),
		new Image("file:src/application/img/2.png"),
		new Image("file:src/application/img/3.png"),
		new Image("file:src/application/img/4.png"),
		new Image("file:src/application/img/5.png"),
		new Image("file:src/application/img/6.png"),
		new Image("file:src/application/img/7.png"),
		new Image("file:src/application/img/8.png"),
		new Image("file:src/application/img/9.png"),
		new Image("file:src/application/img/10.png")
	};
	
	final int MAX_BOMBS = 5;
	final int MAX_SHOTS = MAX_BOMBS * 2;
	boolean gameOver = false;
	private GraphicsContext gc;
	
	Rocket player;
	Bomb bos;
	List<Shot> shots;
	List<Universe> univ;

	List<Bomb> bombs;

    private double mouseX;
    private int score;
    private int skorTinggi;
    private int batas;
    private int scoreThen;
    private File file = new File("skor_tinggi.txt");

//	mulai permainan
	@Override public void start(Stage stage) throws Exception {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);	
		gc = canvas.getGraphicsContext2D();
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> run(gc)));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
		canvas.setCursor(Cursor.MOVE);
		canvas.setOnMouseMoved(e -> mouseX = e.getX());
		canvas.setOnMouseClicked(e -> {
			if(shots.size() < MAX_SHOTS) shots.add(player.shoot());
			if(gameOver) { 
				gameOver = false;
				setup();
			}
		});
		setup();
		stage.setScene(new Scene(new StackPane(canvas)));
		stage.setTitle("Space Invaders");
		stage.show();
    }

//	pengaturan awal permainan
    private void setup() {
		univ = new ArrayList<>();
		shots = new ArrayList<>();
		bombs = new ArrayList<>();
		player = new Rocket(WIDTH / 2, HEIGHT - PLAYER_SIZE, PLAYER_SIZE, PLAYER_IMG);
		score = 0;
		IntStream.range(0, MAX_BOMBS).mapToObj(i -> this.newBomb()).forEach(bombs::add);
		batas = 1;
		scoreThen = 0;
        skorTinggi = 0;
        
//      baca file skor_tinggi
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                try {
                    int score = Integer.parseInt(line.trim());
                    if (score > skorTinggi) skorTinggi = score;
                } catch (NumberFormatException e1) {
                    System.err.println("Mengabaikan skor tidak sah: " + line);
                }
                line = reader.readLine();
            }
            reader.close();

        } catch (IOException ex) {
            System.err.println("Terjadi galat saat membaca skor dari file");
        }
=======
	List<Bomb> Bombs;
	
	private double mouseX;
    	private int score;

	private void setup() {
		univ = new ArrayList<>();
		shots = new ArrayList<>();
		Bombs = new ArrayList<>();
		player = new Rocket(WIDTH / 2, HEIGHT - PLAYER_SIZE, PLAYER_SIZE, PLAYER_IMG);
		score = 0;
		IntStream.range(0, MAX_BOMBS).mapToObj(i -> this.newBomb()).forEach(Bombs::add);
	}

//	program dinamis per frame
    private void run(GraphicsContext gc) {
//    	skor permainan berlangsung
        gc.setFill(Color.grayRgb(20));
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setFont(Font.font(20));
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + score, 5, 20);
        
//      skor tertinggi
        gc.setFont(Font.font(20));
        gc.setFill(Color.CYAN);
        gc.fillText("Skor Tinggi: " + skorTinggi, 5, 40);
//    
        if(gameOver) {
        	gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(Font.font(35));
            gc.setFill(Color.YELLOW);
            gc.fillText("Permainan berakhir \n Skor anda: " + score + "\n Skor tinggi: " + skorTinggi + 
            		" \n Klik untuk main lagi", WIDTH/2, HEIGHT/2.5);
            
//          tulis skor ke file setiap kali mati
            if (batas == 1) {
                try {
                    BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
                    output.newLine();
                    output.append("" + score);
                    output.close();

                } catch (IOException ex1) {
                    System.out.printf("Terjadi galat saat menulis skor ke file: %s\n", ex1);
                }
                
                batas--;
            }
        }

        univ.forEach(Universe :: draw);

        player.update();
        player.draw();
        player.posX = (int) mouseX - 30;

        bombs.stream().peek(Rocket :: update).peek(Rocket :: draw).forEach(e -> {
            if(player.collide(e) && !player.exploding) {
                player.explode();
            }
        });

        for (int i = shots.size() - 1; i >= 0; i--) {
            Shot shot = shots.get(i);
            if(shot.posY < 0 || shot.toRemove)  { 
				shots.remove(i);
				continue;
			}
            
			shot.update();
			shot.draw();
			
			for (Bomb bomb : bombs) {
				if(shot.collide(bomb) && !bomb.exploding) {
					score++;
					bomb.explode();
					shot.toRemove = true;
				}
			}
			
			if (bos != null) {
				if(shot.collide(bos) && !bos.exploding) {
					score += 3;
					bos.explode();
					shot.toRemove = true;
				}
			}
        }

        for (int i = bombs.size() - 1; i >= 0; i--){
			if(bombs.get(i).destroyed)  {
				bombs.set(i, newBomb());
			}
		}
	
		gameOver = player.destroyed;
		if(RAND.nextInt(10) > 2) {
			univ.add(new Universe());
		}
		
		for (int i = 0; i < univ.size(); i++) {
			if(univ.get(i).posY > HEIGHT)
				univ.remove(i);
		}

		if (score > 0 && score % 10 == 0) {
			if (!(scoreThen == score)) {
				bos = newBoss();
				scoreThen = score;
			}
		}
		
		if (bos != null) {
			if(player.collide(bos) && !player.exploding) {
                player.explode();
            }
			bos.update();
			bos.draw();
		}
    }
	
//	class untuk object pemain
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

//	class untuk object musuh
    public class Bomb extends Rocket{
        int kecepatanMusuh;

        public Bomb(int posX, int posY, int size, Image image, int kecepatan) {
            super(posX, posY, size, image);
            kecepatanMusuh = kecepatan;
        }

        public void update() {
            super.update();
            if(!exploding && !destroyed) posY += kecepatanMusuh;
            if(posY > HEIGHT) destroyed = true;
        }
    }

//	class untuk object amunisi
    public class Shot {
        public boolean toRemove;
	Image img=new Image("file:src/application/img/net.png");

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
		if (score >=50 && score<=70 || score>=120) {
			speed = 50;
			gc.drawImage(img, posX, posY,size+40,size+40);
		}
		else {
			gc.drawImage(img, posX, posY,size+25,size+25);
		}
	}

        public boolean collide(Rocket rocket) {
            int distance = distance(this.posX + size /2, this.posY + size /2,
                    rocket.posX + rocket.size /2, rocket.posY + rocket.size /2);
            return distance < rocket.size /2 + size /2;
        }
    }

//	class untuk object latar belakang
    public class Universe {
        int posX, posY;
        private int w, r, g, b;
        private double opacity;

        public Universe() {
            posX = RAND.nextInt(WIDTH);
            posY = 0;
            w = RAND.nextInt(5) +1;
            r = RAND.nextInt(100) +150;
            g = RAND.nextInt(100) +150;
            b = RAND.nextInt(100) +150;
            opacity = RAND.nextFloat();
            if(opacity < 0) opacity *= -1;
            if(opacity > 0.5) opacity = 0.5;
        }

        public void draw() {
            if(opacity > 0.8) opacity -= 0.01;
            if(opacity < 0.1) opacity += 0.01;
            gc.setFill(Color.rgb(r, g, b, opacity));
            gc.fillOval(posX, posY, w, b);
            posY += 20;
        }
    }

    Bomb newBomb() {
        return new Bomb(50 + RAND.nextInt(WIDTH - 100), 0, PLAYER_SIZE, BOMBS_IMG[RAND.nextInt(BOMBS_IMG.length)], (score/5)+2);
    }
    
    Bomb newBoss() {
        return new Bomb(50 + RAND.nextInt(WIDTH - 100), 0, PLAYER_SIZE, PLAYER_IMG, 20);
    }

    int distance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x1-x2, 2) + Math.pow((y1-y2), 2));
    }
    
    public static void main(String[]args) {
    	launch();
    }
}

	}
		
	public class Bomb extends Rocket{
		int SPEED = (score/5)+2;

		public Bomb(int posX, int posY, int size, Image image) {
		    super(posX, posY, size, image);
			//TODO Auto-generated constructor stub
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
                gc.fillRect(posX-5, posY-10, size+20, size+30);
            }
            else {
                gc.fillOval(posX, posY, size, size);
            }
        }

        public boolean collide(Rocket rocket) {
            int distance = distance(this.posX + size /2, this.posY + size /2,
                    rocket.posX + rocket.size /2, rocket.posY + rocket.size /2);
            return distance < rocket.size /2 + size /2;
        }
    }

    public class Universe {
        int posX, posY;
        private int h, w, r, g, b;
        private double opacity;

        public Universe() {
            posX = RAND.nextInt(WIDTH);
            posY = 0;
            w = RAND.nextInt(5) +1;
            h = RAND.nextInt(5) +1;
            r = RAND.nextInt(100) +150;
            g = RAND.nextInt(100) +150;
            b = RAND.nextInt(100) +150;
            opacity = RAND.nextFloat();
            if(opacity < 0) opacity *= -1;
            if(opacity > 0.5) opacity = 0.5;
        }

        public void draw() {
            if(opacity > 0.8) opacity -= 0.01;
            if(opacity < 0.1) opacity += 0.01;
            gc.setFill(Color.rgb(r, g, b, opacity));
            gc.fillOval(posX, posY, w, b);
            posY += 20;
        }
    }

    Bomb newBomb() {
        return new Bomb(50 + RAND.nextInt(WIDTH - 100), 0, PLAYER_SIZE, BOMBS_IMG[RAND.nextInt(BOMBS_IMG.length)]);
    }

    int distance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x1-x2, 2) + Math.pow((y1-y2), 2));
    }
		
     		//run graphics
		private void run(GraphicsContext gc) {
			gc.setFill(Color.ROYALBLUE);
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
			stage.setTitle("Underwater Invaders");
			stage.show();	
		}
	}
}
