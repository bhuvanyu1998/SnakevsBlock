package Game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import Game.Destroy;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;;

public class SnakeGame extends Application implements Serializable {//the main SnakeGame class containing the mainpage, leaderboard and the gamepage(animation class)

	public static void main(String[] args) {//to launch the window
		launch(args);
	}

	// initialization
	Stage primaryStage;
	private Pane gamepage;
	private MainPage main;
	static int score, len, currency;
	int tempscore, tempcurr;
	String tempname;
	private static long activated;
	String Name = "NoName";
	Double snakeheadX;
	ChoiceBox<String> Menu;
	extralife ex = new extralife();
	
	//lists of each token maintaining them
	private List<Block> block = new ArrayList<>();
	private List<Ball> balls = new ArrayList<>();
	private List<Player> snake = new ArrayList<>();
	private List<Wall> walls = new ArrayList<>();
	private List<Shield> shields = new ArrayList<>();
	private List<Magnet> magnets = new ArrayList<>();
	private List<Destroy> Dblocks = new ArrayList<>();
	private List<Coin> coins = new ArrayList<>();
	private List<Danger> dangers = new ArrayList<>();
	private List<Animation> entities = new ArrayList<>();
	private List<Animation> bodyentities = new ArrayList<>();
	private List<Save> save = new ArrayList<>();
	private List<Data> data = new ArrayList<>();

	private AnimationTimer timer;
	private Player player;

	boolean activeshield = false, resume = false, exlife = false;

	;
	private Text mainscore;
	Image shieldimage = new Image("/Game/Shield.png");
	Image magnetimage = new Image("/Game/magnet.png");
	Image destroyblock = new Image("/Game/DestroyBlocks.png");
	Image coin = new Image("/Game/Coin.png");
	Image snakemain = new Image("/Game/snakemainpage.gif");
	Image crown = new Image("/Game/crown.png");
	Image danger = new Image("/Game/danger.png");
	Text keepscore, Length, text_coin;

	private Parent createContent() {// to create display for the gameplay

		score = 0;
		len = 1;
		currency = 0;

		if (exlife == true) {
			currency = tempcurr;
			score = tempscore;
			Name = tempname;
			exlife = false;
		}

		keepscore = new Text(320, 25, "Score: " + score);
		keepscore.setFont(Font.font("Century Gothic"));
		keepscore.setScaleX(1.5);
		keepscore.setScaleY(1.5);
		keepscore.setFill(Color.AQUA);

		Length = new Text(175, 25, "Length: " + len);
		Length.setFont(Font.font("Century Gothic"));
		Length.setScaleX(1.5);
		Length.setScaleY(1.5);
		Length.setFill(Color.AQUA);

		text_coin = new Text(190, 580, "Coins: " + currency);
		text_coin.setFont(Font.font("Century Gothic"));
		text_coin.setScaleX(1.5);
		text_coin.setScaleY(1.5);
		text_coin.setFill(Color.AQUA);

		gamepage = new Pane();
		gamepage.setPrefSize(400, 600);
		gamepage.getChildren().addAll(keepscore, Length, text_coin);
		gamepage.setStyle("-fx-background-color: #000000");
		Menu();
		player = new Player(0);
		snake.add(player);

		addAnimation(player, 200, 450);

		primaryStage.setOnCloseRequest(e -> {
			try {
				if (snake.size() > 0)
					;
				{
					timer.stop();
					serialize();
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
			
			}
		});
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {

				onUpdate();
			}
		};
		timer.start();

		return gamepage;
	}

	private Parent createResumeContent() {//to display the resumed gameplay that had previously been paused and its state had been saved
		generatestate(Name);

		keepscore = new Text(320, 25, "Score: " + score);
		keepscore.setFont(Font.font("Century Gothic"));
		keepscore.setScaleX(1.5);
		keepscore.setScaleY(1.5);
		keepscore.setFill(Color.AQUA);

		Length = new Text(175, 25, "Length: " + len);
		Length.setFont(Font.font("Century Gothic"));
		Length.setScaleX(1.5);
		Length.setScaleY(1.5);
		Length.setFill(Color.AQUA);

		text_coin = new Text(190, 580, "Coins: " + currency);
		text_coin.setFont(Font.font("Century Gothic"));
		text_coin.setScaleX(1.5);
		text_coin.setScaleY(1.5);
		text_coin.setFill(Color.AQUA);

		gamepage = new Pane();
		gamepage.setPrefSize(400, 600);
		gamepage.getChildren().addAll(keepscore, Length, text_coin);
		gamepage.setStyle("-fx-background-color: #000000");
		Menu();

		for (int i = 0; i < block.size(); i++) {
			resume = true;
			Block b = new Block(this);
			Color c = b.changecol();
			block.get(i).value = new Text();
			block.get(i).view = new Rectangle(50, 50, c);
			block.get(i).velocity = new Point2D(0, 0);
			addBlock(block.get(i), block.get(i).tranX, block.get(i).tranY);
		}

		for (int i = 0; i < balls.size(); i++) {
			resume = true;
			balls.get(i).value = new Text();
			balls.get(i).view = new Circle(10, 10, 10, Color.YELLOW);
			balls.get(i).velocity = new Point2D(0, 0);
			addBall(balls.get(i), balls.get(i).tranX, balls.get(i).tranY);

		}

		for (int i = 0; i < coins.size(); i++) {

			resume = true;
			ImageView img_coin = new ImageView(coin);
			img_coin.setFitHeight(35);
			img_coin.setFitWidth(35);
			coins.get(i).velocity = new Point2D(0, 0);
			coins.get(i).view = img_coin;
			addCoin(coins.get(i), coins.get(i).tranX, coins.get(i).tranY);
		}

		for (int i = 0; i < shields.size(); i++) {

			resume = true;
			ImageView img_shield = new ImageView(shieldimage);
			img_shield.setFitHeight(35);
			img_shield.setFitWidth(35);
			shields.get(i).velocity = new Point2D(0, 0);
			shields.get(i).view = img_shield;
			addShield(shields.get(i), shields.get(i).tranX, shields.get(i).tranY);
		}

		for (int i = 0; i < magnets.size(); i++) {

			resume = true;
			ImageView img_magnet = new ImageView(magnetimage);
			img_magnet.setFitHeight(35);
			img_magnet.setFitWidth(35);
			magnets.get(i).velocity = new Point2D(0, 0);
			magnets.get(i).view = img_magnet;
			addMagnet(magnets.get(i), magnets.get(i).tranX, magnets.get(i).tranY);
		}

		for (int i = 0; i < Dblocks.size(); i++) {

			resume = true;
			ImageView img_Dblocks = new ImageView(destroyblock);
			img_Dblocks.setFitHeight(35);
			img_Dblocks.setFitWidth(35);
			Dblocks.get(i).velocity = new Point2D(0, 0);
			Dblocks.get(i).view = img_Dblocks;
			Dblocks.get(i).value = new Text();
			addDestroy(Dblocks.get(i), Dblocks.get(i).tranX, Dblocks.get(i).tranY);
		}

		for (int i = 0; i < walls.size(); i++) {
			resume = true;
			walls.get(i).velocity = new Point2D(0, 0);
			walls.get(i).view = new Rectangle(5, 75 + Math.random() * 100, Color.WHITE);
			addWall(walls.get(i), walls.get(i).tranX, walls.get(i).tranY);
		}

		player = new Player(0);
		snake.add(player);
		addAnimation(player, snakeheadX, 450);
		for (int i = 0; i < len - 1; i++) {

			Player add = new Player();
			if (snake.size() > 1)
				addPlayer(add, snake.get(snake.size() - 1).getView().getTranslateX(),
						snake.get(snake.size() - 1).getView().getTranslateY() + 14);
			else
				addPlayer(add, snake.get(snake.size() - 1).getView().getTranslateX(),
						snake.get(snake.size() - 1).getView().getTranslateY() + 16);
		}

		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {

				onUpdate();
			}
		};
		timer.start();

		return gamepage;
	}

	private void Menu() {// dropdown menu in the gameplay
		Menu = new ChoiceBox<>();
		Menu.setStyle("-fx-background-color: #111111");
		Menu.getItems().addAll("Menu", "Pause", "Resume", "Return to Main Menu", "Restart", "Exit");
		Menu.setValue("Menu");

		Menu.getSelectionModel().selectedItemProperty().addListener((v, oldval, newval) -> {
			timer.stop();
			if (newval.toString().equals("Pause"))
				timer.stop();
			else if (newval.toString().equals("Exit"))
				Platform.exit();
			else if (newval.toString().equals("Resume"))
				timer.start();
			else if (newval.toString().equals("Restart")) {
				resume = false;
				clear();
				primaryStage.setScene(new Scene(createContent()));

				primaryStage.setTitle("Game Page");
				primaryStage.getScene().setOnKeyPressed(e1 -> {
					if (e1.getCode() == KeyCode.LEFT && player.getView().getTranslateX() >= 8) {
						left();
					} else if (e1.getCode() == KeyCode.RIGHT && player.getView().getTranslateX() <= 370) {
						right();
					}
				});

			} else if (newval.toString().equals("Return to Main Menu"))
				try {

					timer.stop();
					serialize();

					start(primaryStage);
				} catch (Exception e) {
					
				}

		});

		gamepage.getChildren().add(Menu);

	}

	private void clear() {// to clear all the tokens of each animation
		block.clear();
		balls.clear();
		walls.clear();
		shields.clear();
		Dblocks.clear();
		coins.clear();
		magnets.clear();
		snake.clear();
		entities.clear();
		dangers.clear();
	}

	private void addAnimation(Animation object, double x, double y) {// to add the respective token on the screen during gameplay
		object.getView().setTranslateX(x);
		object.getView().setTranslateY(y);
		gamepage.getChildren().add(object.getView());
	}

	private void collidesound() {// to add sounds to the gameplay during a collision
		String musicFile = "collide.mp3";

		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}

	private void destroysound() {// to add sounds to the gameplay when blocks are destroyed
		String musicFile = "Blast.mp3";

		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}

	private void coinsound() {// to add sounds to the gameplay when a coin is collected
		String musicFile = "coin.mp3";

		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}

	private void shieldsound() {// to add sounds to the gameplay when a shield is acquired
		String musicFile = "shield.mp3";

		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}

	private void magnetsound() {// to add sounds to the gameplay when a magnet is acquired
		String musicFile = "magnet.mp3";

		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}

	private void endsound() {// to add sounds to the gameplay when the game is ended
		String musicFile = "End.mp3";

		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}

	private void ballsound() {// to add sounds to the gameplay when a ball is added
		String musicFile = "ball.mp3";

		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}

	// initializing time
	int time = 1;

	Block collide;
	boolean stop = false;

	
		private void onUpdate()  {//method controlling the game play
			// for collision of the snake(player) with the respective tokens
			// and for refreshing the game
			// and setting the position, speed of the tokens
	try
	{

		if(snake.size()<=0)
		{
			throw new LengthException();
		}
		
		
		if (System.currentTimeMillis() - activated > 5000 || System.currentTimeMillis() - activated < 0) {
			activeshield = false;
			gamepage.setStyle("-fx-background-color: #000000");
			Menu.setStyle("-fx-background-color: #000000");
		}

		if (activeshield == true) {
			gamepage.setStyle("-fx-background-color: #33CC8C");
			Menu.setStyle("-fx-background-color: #33CC8C");
		}

		if (stop == true && snake.size() > 0) {
			double speed = 2;
			if (len > 10)
				speed = (0.3 * 10) + 1;
			if (snake.get(0).getView().getTranslateX() > collide.getView().getTranslateX()
					&& snake.get(0).getView().getTranslateX() < collide.getView().getTranslateX() + 50) {
				long count = System.currentTimeMillis();

				if (collide.val > 0) {
					timer.stop();
					while (System.currentTimeMillis() - count < 250) {

					}
					timer.start();

					for (int i = 0; i < block.size(); i++) {
						block.get(i).getView().setTranslateY(block.get(i).getView().getTranslateY() - speed);
					}
					for (int i = 0; i < coins.size(); i++) {
						coins.get(i).getView().setTranslateY(coins.get(i).getView().getTranslateY() - speed);
					}
					for (int i = 0; i < walls.size(); i++) {
						walls.get(i).getView().setTranslateY(walls.get(i).getView().getTranslateY() - speed);
					}
					for (int i = 0; i < shields.size(); i++) {
						shields.get(i).getView().setTranslateY(shields.get(i).getView().getTranslateY() - speed);
					}
					for (int i = 0; i < Dblocks.size(); i++) {
						Dblocks.get(i).getView().setTranslateY(Dblocks.get(i).getView().getTranslateY() - speed);
					}
					for (int i = 0; i < magnets.size(); i++) {
						magnets.get(i).getView().setTranslateY(magnets.get(i).getView().getTranslateY() - speed);
					}
					for (int i = 0; i < balls.size(); i++) {
						balls.get(i).getView().setTranslateY(balls.get(i).getView().getTranslateY() - speed);
					}
					score = score + 1;
					keepscore.setText("Score: " + score);
					len = len - 1;
					Length.setText("Length: " + len);
					collide.val = collide.val - 1;
					playdeathbody(snake.get(0));
					snake.get(snake.size() - 1).setAlive(false);
					gamepage.getChildren().remove(snake.get(snake.size() - 1).getView());
					snake.remove(snake.size() - 1);
					if(snake.size()==0)
						throw new LengthException();
					count = System.currentTimeMillis();
				}

				if (collide.val == 0) {
					collide.setAlive(false);
					gamepage.getChildren().removeAll(collide.getView(), collide.value);
					playdeath(collide);
					collidesound();
					stop = false;
				}

				if (len == 0) {
					collide.setAlive(false);
					player.setAlive(false);
					gamepage.getChildren().removeAll(collide.getView(), player.getView());
					timer.stop();
					try {
						player.setAlive(true);
						data.add(0, new Data(Name, score, new Date().toString().substring(0, 10)));
						endsound();

						if (currency >= 5) {
							ex = new extralife();

							primaryStage.setScene(ex.extra);
							ex.restart.setOnMouseClicked(e -> {

								clear();
								resume = false;
								tempcurr = currency - 5;
								tempscore = score;
								tempname = Name;
								exlife = true;
								primaryStage.setScene(new Scene(createContent()));

								primaryStage.setTitle("Game Page");
								primaryStage.getScene().setOnKeyPressed(e1 -> {
									if (e1.getCode() == KeyCode.LEFT && player.getView().getTranslateX() >= 8) {
										left();
									} else if (e1.getCode() == KeyCode.RIGHT
											&& player.getView().getTranslateX() <= 370) {
										right();
									}
								});
							});

							ex.main.setOnAction(e -> {
								try {
									start(primaryStage);
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									
								}
							});
						} else {
							try {
								start(primaryStage);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								
							}
						}
					}

					catch (Exception e) {
						e.printStackTrace();
					}
				}

			} else
				stop = false;

		} else {
			for (Block blocks : block) {

				if (blocks.isColliding(player)) {
					int temp = blocks.getVal();

					if (activeshield == true) {
						blocks.setAlive(false);
						gamepage.getChildren().removeAll(blocks.getView(), blocks.value);
						score = score + temp;
						keepscore.setText("Score: " + score);
						playdeath(blocks);
						collidesound();
					}

					else {

						if (temp >= len) {
							if (temp > 5) {
								stop = true;
								collide = blocks;
							} else {
								blocks.setAlive(false);
								player.setAlive(false);
								gamepage.getChildren().removeAll(blocks.getView(), player.getView());
								timer.stop();
								try {
									player.setAlive(true);
									data.add(0, new Data(Name, score, new Date().toString().substring(0, 10)));
									endsound();

									if (currency >= 5) {
										ex = new extralife();

										primaryStage.setScene(ex.extra);
										ex.restart.setOnMouseClicked(e -> {

											clear();
											resume = false;
											tempcurr = currency - 5;
											tempscore = score;
											tempname = Name;
											exlife = true;
											primaryStage.setScene(new Scene(createContent()));

											primaryStage.setTitle("Game Page");
											primaryStage.getScene().setOnKeyPressed(e1 -> {
												if (e1.getCode() == KeyCode.LEFT
														&& player.getView().getTranslateX() >= 8) {
													left();
												} else if (e1.getCode() == KeyCode.RIGHT
														&& player.getView().getTranslateX() <= 370) {
													right();
												}
											});
										});

										ex.main.setOnAction(e -> {
											try {
												start(primaryStage);
											} catch (Exception e1) {
												// TODO Auto-generated catch
												// block
											
											}
										});
									} else {
										try {
											start(primaryStage);
										} catch (Exception e1) {
											// TODO Auto-generated catch block
											
										}
									}
								} catch (Exception e) {
									
								}
							}
						} else {

							if (temp <= 5) {
								blocks.setAlive(false);
								gamepage.getChildren().removeAll(blocks.getView(), blocks.value);
								score = score + temp;
								keepscore.setText("Score: " + score);
								len = len - temp;
								Length.setText("Length: " + len);

								while (temp > 0) {
									snake.get(snake.size() - 1).setAlive(false);
									gamepage.getChildren().remove(snake.get(snake.size() - 1).getView());
									snake.remove(snake.size() - 1);
									if(snake.size()==0)
										throw new LengthException();
									temp--;
								}

								playdeath(blocks);
								collidesound();
							}

							else {
								stop = true;
								collide = blocks;

							}
						}

					}

				}
			}
		}

		for (Ball ball : balls) {

			if (ball.isColliding(player)) {
				ball.setAlive(false);
				len = len + ball.val;
				Length.setText("Length: " + len);
				ballsound();
				gamepage.getChildren().removeAll(ball.getView(), ball.value);

				for (int i = 0; i < ball.val; i++) {

					Player add = new Player();
					if (snake.size() > 1)
						addPlayer(add, snake.get(snake.size() - 1).getView().getTranslateX(),
								snake.get(snake.size() - 1).getView().getTranslateY() + 14);
					else
						addPlayer(add, snake.get(snake.size() - 1).getView().getTranslateX(),
								snake.get(snake.size() - 1).getView().getTranslateY() + 16);
				}

			}
		}

		for (Magnet mag : magnets) {

			if (mag.isColliding(player)) {

				for (Coin coin : coins) {
					if ((mag.getView().getTranslateX() <= mag.getView().getTranslateX() + 20)
							|| (coin.getView().getTranslateY() <= mag.getView().getTranslateY() + 20)
							|| (coin.getView().getTranslateX() <= mag.getView().getTranslateX() - 20)
							|| (coin.getView().getTranslateY() <= mag.getView().getTranslateY() - 20)) {
						coin.setAlive(false);
						currency++;
						text_coin.setText("Coins: " + (currency));
						magnetsound();
						gamepage.getChildren().removeAll(coin.getView());
					}
				}

				gamepage.getChildren().removeAll(mag.getView());

			}
		}

		for (Shield sh : shields) {

			if (sh.isColliding(player)) {
				activeshield = true;
				activated = System.currentTimeMillis();
				shieldsound();
				gamepage.getChildren().removeAll(sh.getView());

			}
		}

		for (Destroy destroy : Dblocks) {

			if (destroy.isColliding(player)) {

				for (Block blocks : block) {
					if (gamepage.getChildren().contains(blocks.getView()) && blocks.getView().getTranslateY() >= 0) {
						int temp = blocks.getVal();
						blocks.setAlive(false);
						gamepage.getChildren().removeAll(blocks.getView(), blocks.value);
						score = score + temp;
						keepscore.setText("Score: " + score);

						playdeath(blocks);
						destroysound();
					}
				}

				gamepage.getChildren().removeAll(destroy.getView());

			}
		}

		for (Coin coin : coins) {
			if (coin.isColliding(player)) {
				coin.setAlive(false);
				currency++;
				text_coin.setText("Coins: " + (currency));
				coinsound();
				gamepage.getChildren().removeAll(coin.getView());
			}
		}

		for (Danger danger : dangers) {
			if (danger.isColliding(player)) {
				danger.setAlive(false);

				player.setAlive(false);
				gamepage.getChildren().removeAll(danger.getView(), player.getView());
				timer.stop();
				try {
					player.setAlive(true);
					data.add(0, new Data(Name, score, new Date().toString().substring(0, 10)));
					endsound();
					if (currency >= 5) {
						ex = new extralife();

						primaryStage.setScene(ex.extra);
						ex.restart.setOnMouseClicked(e -> {

							clear();
							resume = false;
							tempcurr = currency - 5;
							tempscore = score;
							tempname = Name;
							exlife = true;
							primaryStage.setScene(new Scene(createContent()));

							primaryStage.setTitle("Game Page");
							primaryStage.getScene().setOnKeyPressed(e1 -> {
								if (e1.getCode() == KeyCode.LEFT && player.getView().getTranslateX() >= 8) {
									left();
								} else if (e1.getCode() == KeyCode.RIGHT && player.getView().getTranslateX() <= 370) {
									right();
								}
							});
						});

						ex.main.setOnAction(e -> {
							try {
								start(primaryStage);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								
							}
						});
					} else {
						try {
							start(primaryStage);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							
						}
					}
				} catch (Exception e) {
					
				}
			}
		}

		coins.removeIf(Animation::isDead);
		shields.removeIf(Animation::isDead);
		magnets.removeIf(Animation::isDead);
		Dblocks.removeIf(Animation::isDead);
		block.removeIf(Animation::isDead);
		balls.removeIf(Animation::isDead);
		dangers.removeIf(Animation::isDead);

		block.forEach(Animation::update);
		block.forEach(Animation::textupdate);

		balls.forEach(Animation::update);
		balls.forEach(Animation::textupdate);

		entities.forEach(Animation::update);
		bodyentities.forEach(Animation::update);

		player.update();

		walls.forEach(Animation::update);

		shields.forEach(Shield::update);

		magnets.forEach(Magnet::update);

		Dblocks.forEach(Destroy::update);

		coins.forEach(Coin::update);

		dangers.forEach(Danger::update);

		for (int i = 0; i < block.size(); i++) {
			if (block.get(i).getView().getTranslateY() > 600) {
				block.get(i).setAlive(false);
				gamepage.getChildren().remove(block.get(i).getView());
				block.get(i).value.setVisible(false);
				block.remove(i);
			}

		}
		for (int i = 0; i < balls.size(); i++) {
			if (balls.get(i).getView().getTranslateY() > 600) {
				balls.get(i).setAlive(false);
				gamepage.getChildren().remove(balls.get(i).getView());
				balls.get(i).value.setVisible(false);
				balls.remove(i);
			}

		}
		for (int i = 0; i < walls.size(); i++) {
			if (walls.get(i).getView().getTranslateY() > 600) {
				walls.get(i).setAlive(false);
				gamepage.getChildren().remove(walls.get(i).getView());
				walls.remove(i);
			}

		}
		for (int i = 0; i < shields.size(); i++) {
			if (shields.get(i).getView().getTranslateY() > 600) {
				shields.get(i).setAlive(false);
				gamepage.getChildren().remove(shields.get(i).getView());
				shields.remove(i);
			}

		}
		for (int i = 0; i < magnets.size(); i++) {
			if (magnets.get(i).getView().getTranslateY() > 600) {
				magnets.get(i).setAlive(false);
				gamepage.getChildren().remove(magnets.get(i).getView());
				magnets.remove(i);
			}

		}
		for (int i = 0; i < Dblocks.size(); i++) {
			if (Dblocks.get(i).getView().getTranslateY() > 600) {
				Dblocks.get(i).setAlive(false);
				gamepage.getChildren().remove(Dblocks.get(i).getView());
				Dblocks.remove(i);
			}

		}
		for (int i = 0; i < coins.size(); i++) {
			if (coins.get(i).getView().getTranslateY() > 600) {
				coins.get(i).setAlive(false);
				gamepage.getChildren().remove(coins.get(i).getView());
				coins.remove(i);
			}

		}
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).getView().getTranslateY() > 600) {
				entities.get(i).setAlive(false);
				gamepage.getChildren().remove(entities.get(i).getView());
				entities.remove(i);
			}

		}

		int counter = 160;
		if (len > 10)
			counter = 160 - (len * 2);

		time++;
		if (time - counter == 0) {
			double one, two, three, four, five;
			one = Math.random() % 0.1;
			two = 0.2 + Math.random() % 0.1;
			three = 0.4 + Math.random() % 0.1;
			four = 0.6 + Math.random() % 0.1;
			five = 0.8 + Math.random() % 0.05;

			if (Math.random() < 0.75)
				addBlock(new Block(this), one * gamepage.getPrefWidth(), -300);
			if (Math.random() < 0.75)
				addBlock(new Block(this), two * gamepage.getPrefWidth(), -300);
			if (Math.random() < 0.75)
				addBlock(new Block(this), three * gamepage.getPrefWidth(), -300);
			if (Math.random() < 0.75)
				addBlock(new Block(this), four * gamepage.getPrefWidth(), -300);
			if (Math.random() < 0.75)
				addBlock(new Block(this), five * gamepage.getPrefWidth(), -300);

			time = 1;

		}

		else if (time - (counter - 30) == 0) {
			ImageView img_shield = new ImageView(shieldimage);
			img_shield.setFitWidth(35);
			img_shield.setFitHeight(35);

			if (Math.random() < 0.3)
				addShield(new Shield(img_shield, this), Math.random() * (gamepage.getPrefWidth() - 20), -300);

			addWall(new Wall(this), Math.random() * (gamepage.getPrefWidth() - 20), -300);

			if (Math.random() < 0.5)
				addWall(new Wall(this), Math.random() * (gamepage.getPrefWidth() - 20), -300);

			if (Math.random() < 0.5)
				addWall(new Wall(this), Math.random() * (gamepage.getPrefWidth() - 20), -300);

			for (Wall w : walls) {
				for (Ball obj : balls) {
					if (w.isColliding(obj)) {
						obj.setAlive(false);
						gamepage.getChildren().removeAll(obj.getView(), obj.value);
					}
				}

				for (Wall obj : walls) {
					if (w.isColliding(obj) && (w.getView().getTranslateX() < obj.getView().getTranslateX() - 25
							|| w.getView().getTranslateX() > obj.getView().getTranslateX() + 25)) {
						obj.setAlive(false);
						walls.remove(obj);
						gamepage.getChildren().removeAll(obj.getView());
					}
				}

				for (Shield obj : shields) {
					if (w.isColliding(obj)) {
						obj.setAlive(false);
						gamepage.getChildren().removeAll(obj.getView());
					}
				}

				for (Destroy obj : Dblocks) {
					if (w.isColliding(obj)) {
						obj.setAlive(false);
						gamepage.getChildren().removeAll(obj.getView());
					}
				}

				for (Magnet obj : magnets) {
					if (w.isColliding(obj)) {
						obj.setAlive(false);
						gamepage.getChildren().removeAll(obj.getView());
					}
				}

				for (Coin obj : coins) {
					if (w.isColliding(obj)) {
						obj.setAlive(false);
						gamepage.getChildren().removeAll(obj.getView());
					}
				}

			}
		}

		else if (time - (counter - 45) == 0) {
			ImageView img_magnet = new ImageView(magnetimage);
			img_magnet.setFitWidth(35);
			img_magnet.setFitHeight(35);

			if (Math.random() < 0.3)
				addMagnet(new Magnet(img_magnet, this), Math.random() * (gamepage.getPrefWidth() - 20), -300);
		}

		else if (time - (counter - 60) == 0) {
			ImageView img_Dblock = new ImageView(destroyblock);
			img_Dblock.setFitHeight(35);
			img_Dblock.setFitWidth(35);
			if (Math.random() < 0.15)
				addDestroy(new Destroy(img_Dblock, this), Math.random() * (gamepage.getPrefWidth() - 20), -300);
		} else if (time - (counter - 70) == 0) {
			ImageView img_coin = new ImageView(coin);
			img_coin.setFitHeight(35);
			img_coin.setFitWidth(35);

			if (Math.random() < 0.2)
				addCoin(new Coin(img_coin, this), Math.random() * (gamepage.getPrefWidth() - 20), -300);

		}

		else if (time - (counter - 80) == 0) {
			if (Math.random() < 0.6)
				addBall(new Ball(this), Math.random() * (gamepage.getPrefWidth() - 20), -300);
		}

		else if (snake.size() > 10 && time - (counter - 90) == 0) {
			ImageView img_danger = new ImageView(danger);
			img_danger.setFitHeight(30);
			img_danger.setFitWidth(30);
			if (Math.random() < 0.5)
			addDanger(new Danger(img_danger, this), Math.random() * (gamepage.getPrefWidth() - 20), -300);
		}

	
	}
	catch (Exception e)
	{
		
		try {
			start(primaryStage);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			
		}
	}
		}

	private void resumeBlock(Block Block, double x, double y) {//method for adding blocks after being resumed, along with their values

		Block.value = new Text(x + 20, y - 40, "" + Block.getVal());
		Block.value.setScaleX(2.69);
		Block.value.setFont(Font.font("Century Gothic", 10));
		Block.value.setScaleY(2.69);
		Block.value.setFill(Color.WHITE);

		addAnimation(Block, x, y);
		gamepage.getChildren().add(Block.value);

	}

	private void addDanger(Danger danger, double x, double y) {//method for adding danger token
		danger.tranX = x;
		if (resume == false)
			dangers.add(danger);
		addAnimation(danger, x, y);
		resume = false;

	}

	private void addBlock(Block Block, double x, double y) {//method for adding blocks, along with their values
		Block.tranX = x;
		if (resume == false) {
			block.add(Block);
			Block.value = new Text(x + 20, Block.getView().getTranslateY() + 20, "" + Block.getVal());
		} else {
			Block.value = new Text(x + 20, Block.getView().getTranslateY() + 20, "" + Block.getVal());
		}
		Block.value.setScaleX(2.69);
		Block.value.setFont(Font.font("Century Gothic", 10));
		Block.value.setScaleY(2.69);
		Block.value.setFill(Color.WHITE);

		addAnimation(Block, x, y);
		gamepage.getChildren().add(Block.value);
		resume = false;

	}

	private void addBall(Ball Ball, double x, double y) {//method for adding balls, along with their values
		Ball.tranX = x;
		if (resume == false) {
			balls.add(Ball);
			Ball.value = new Text(x + 7, Ball.getView().getTranslateY() + 10, "" + Ball.val);
		} else {
			Ball.value = new Text(x + 7, Ball.getView().getTranslateY() + 10, "" + Ball.val);
		}
		Ball.value.setFont(Font.font("Century Gothic", 10));
		Ball.value.setScaleY(2);
		Ball.value.setScaleX(2);
		Ball.value.setFill(Color.RED);
		addAnimation(Ball, x, y);
		gamepage.getChildren().add(Ball.value);
		resume = false;
	}

	private void addPlayer(Player play, double x, double y) {//method for adding the player, that is, snake
		play.tranX = x;

		if (resume == false)
			snake.add(play);
		addAnimation(play, x, y);
		resume = false;

	}

	private void addWall(Wall wall, double x, double y) {//method for adding walls
		wall.tranX = x;
		if (resume == false)
			walls.add(wall);
		addAnimation(wall, x, y);
		resume = false;
	}

	private void addShield(Shield shield, double x, double y) {//method for adding shields
		shield.tranX = x;
		if (resume == false)
			shields.add(shield);
		addAnimation(shield, x, y);
		resume = false;
	}

	private void addCoin(Coin coin, double x, double y) {//method for adding coins
		coin.tranX = x;
		if (resume == false)
			coins.add(coin);
		addAnimation(coin, x, y);
		resume = false;
	}

	private void addMagnet(Magnet magnet, double x, double y) {//method for adding magnets
		magnet.tranX = x;
		if (resume == false)
			magnets.add(magnet);
		addAnimation(magnet, x, y);
		resume = false;
	}

	private void addDestroy(Destroy Dblock, double x, double y) {//method for adding destroy blocks
		Dblock.tranX = x;
		if (resume == false)
			Dblocks.add(Dblock);
		addAnimation(Dblock, x, y);
		resume = false;
	}

	private void playdeath(Block enemy) {//method for the bursting effect after the snake eats the blocks
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				Entity particle = new Entity();
				particle.getView().setTranslateX(j * 2 + enemy.getView().getTranslateX());
				particle.getView().setTranslateY(i * 2 + enemy.getView().getTranslateY());
				particle.setAlive(true);

				Point2D vector = new Point2D(Math.random() - 0.5, Math.random() - 0.8).multiply(2);
				particle.setVelocity(vector);

				addAnimation(particle, j * 2 + enemy.getView().getTranslateX(),
						i * 2 + enemy.getView().getTranslateY());

				entities.add(particle);
			}
		}

	}

	private void playdeathbody(Player enemy) {
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				Entity particle = new Entity(2, enemy);
				particle.getView().setTranslateX(enemy.getView().getTranslateX());
				particle.getView().setTranslateY(enemy.getView().getTranslateY());
				particle.setAlive(true);

				Point2D vector = new Point2D(Math.random() - 0.5, Math.random() - 0.8).multiply(2);
				particle.setVelocity(vector);

				addAnimation(particle, snake.get(0).getView().getTranslateX(), snake.get(0).getView().getTranslateY());

				bodyentities.add(particle);
			}
		}

	}

	// all token classes extending the animation parent class
	static class Player extends Animation {

		Player() {
			super(new Circle(10, 10, 7, Color.MEDIUMSLATEBLUE));
		}

		Player(int x) {
			super(new Circle(10, 10, 9, Color.LIGHTBLUE));

		}

		public void update() {
			getView().setTranslateY(getView().getTranslateY());
		}

	}

	private static class Entity extends Animation {

		Entity() {
			super(new Rectangle(2, 2, changecol()));
		}

		Entity(int x, Player p) {
			super(new Circle(p.getView().getTranslateX(), p.getView().getTranslateY(), x));
		}

		public static Color changecol() {
			double tempval = Math.random();
			if (tempval <= 0.2)
				return Color.AQUA;
			else if (tempval <= 0.4)
				return Color.GREENYELLOW;
			else if (tempval <= 0.6)
				return Color.GREEN;
			else if (tempval <= 0.8)
				return Color.ORANGE;
			else
				return Color.RED;
		}
	}

	public void savestate() {//method for saving the game state when it has been paused

		boolean found = false;
		for (Save itr : save) {
			if (itr.Name.equals(Name)) {
				found = true;
				itr.score = score;
				itr.length = len;
				itr.currency = currency;
				itr.len_blocks = block.size();
				itr.len_balls = balls.size();
				itr.len_coins = coins.size();
				itr.len_Dblocks = Dblocks.size();
				itr.len_magnets = magnets.size();
				itr.len_shields = shields.size();
				itr.len_walls = walls.size();
				itr.snakeX = snake.get(0).getView().getTranslateX();

				for (int i = 0; i < block.size(); i++) {
					itr.blockx = block;
				}

				for (int i = 0; i < balls.size(); i++) {
					itr.ballx = balls;
				}

				for (int i = 0; i < walls.size(); i++) {

					itr.wallx = walls;
				}

				for (int i = 0; i < shields.size(); i++) {
					itr.shieldx = shields;
				}

				for (int i = 0; i < magnets.size(); i++) {
					itr.magnetx = magnets;
				}

				for (int i = 0; i < coins.size(); i++) {
					itr.coinx = coins;
				}

				for (int i = 0; i < Dblocks.size(); i++) {
					itr.Dblockx = Dblocks;
				}
				break;
			}
		}

		if (found == false) {
			Save s = new Save();

			s.score = score;
			s.length = len;
			s.currency = currency;
			s.len_blocks = block.size();
			s.len_balls = balls.size();
			s.len_coins = coins.size();
			s.len_Dblocks = Dblocks.size();
			s.len_magnets = magnets.size();
			s.len_shields = shields.size();
			s.len_walls = walls.size();
			s.snakeX = snake.get(0).getView().getTranslateX();

			for (int i = 0; i < block.size(); i++) {
				s.blockx = block;
			}

			for (int i = 0; i < balls.size(); i++) {
				s.ballx = balls;
			}

			for (int i = 0; i < walls.size(); i++) {
				s.wallx = walls;
			}

			for (int i = 0; i < shields.size(); i++) {
				s.shieldx = shields;
			}

			for (int i = 0; i < magnets.size(); i++) {
				s.magnetx = magnets;
			}

			for (int i = 0; i < coins.size(); i++) {
				s.coinx = coins;
			}

			for (int i = 0; i < Dblocks.size(); i++) {
				s.Dblockx = Dblocks;
			}
			s.Name = Name;
			save.add(s);
		}

	}

	public void generatestate(String n) {//method for generating the saved/paused state after the game has been resumed
		for (Save itr : save) {
			if (itr.Name.equals(n)) {
				len = itr.length + 1;
				currency = itr.currency;
				score = itr.score;
				clear();
				block = itr.blockx;
				balls = itr.ballx;
				walls = itr.wallx;
				shields = itr.shieldx;
				coins = itr.coinx;
				magnets = itr.magnetx;
				Dblocks = itr.Dblockx;
				snakeheadX = itr.snakeX;
			}
		}
	}

	private void left() {//to detect left collision with wall
		double currentLoc = snake.get(0).getView().getTranslateX();
		snake.get(0).getView().setTranslateX(currentLoc - 10);
		boolean collision = false;
		double wallX = 0;
		for (Wall w : walls) {
			if (w.isColliding(snake.get(0))) {
				// means collision is happening on going left, do not move
				collision = collision || true;
				wallX = w.getView().getTranslateX();
			} else {
				// no collision, safe to move
				collision = false || collision;
			}
		}
		if (collision) {
			for (Player p : snake) {
				p.getView().setTranslateX(wallX + 6);
			}
		} else {
			for (Player p : snake) {
				p.getView().setTranslateX(currentLoc - 10);
			}
		}
	}

	public void serialize() throws Exception {//method to serialize token for saving the game's state
		savestate();

		FileOutputStream file = new FileOutputStream("input.txt");
		ObjectOutputStream out = new ObjectOutputStream(file);

		// Method for serialization of object
		out.writeObject(save);

		out.close();
		file.close();
	}

	public void deserialize() throws Exception {//method to deserialize data
		try {
			// Reading the object from a file
			FileInputStream file1 = new FileInputStream("input.txt");
			ObjectInputStream in = new ObjectInputStream(file1);

			// Method for deserialization of object
			ArrayList<Save> object1 = (ArrayList<Save>) in.readObject();

			in.close();
			file1.close();
			save = (ArrayList<Save>) object1;

		}

		catch (IOException ex) {
			System.out.println("IOException is caught");
		} catch (ClassNotFoundException ex) {
			System.out.println("ClassNotFoundException is caught");
		}
	}

	private void right() {//to detect collision with wall on right
		double currentLoc = snake.get(0).getView().getTranslateX();
		snake.get(0).getView().setTranslateX(currentLoc + 10);
		boolean collision = false;
		double wallX = 0;
		for (Wall w : walls) {
			if (w.isColliding(snake.get(0))) {
				// means collision is happening on going right, do not move
				collision = collision || true;
				wallX = w.getView().getTranslateX();
			} else {
				// no collision, safe to move
				collision = false || collision;
			}
		}
		if (collision) {
			for (Player p : snake) {
				p.getView().setTranslateX(wallX - 20);
			}
		} else {
			for (Player p : snake) {
				p.getView().setTranslateX(currentLoc + 10);
			}
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		// start function to start the game and the mainpage
		// it handles events that occur as a result of clicking the buttons

		data.add(0, new Data(Name, score, new Date().toString().substring(0, 10)));
		primaryStage = stage;
		main = new MainPage();
		deserialize();
		Leaderboard lead = new Leaderboard();

		primaryStage.setTitle("Main Page");
		primaryStage.setResizable(true);
		primaryStage.setScene(main.mainpage);

		main.fresh.setOnKeyReleased(e -> {
			Name = main.fresh.getText();
			main.ResumeBtn.setVisible(false);
			// System.out.println(Name);
		});
		main.exist.setOnKeyReleased(e -> {
			Name = main.exist.getText();
			for (Save itr : save) {
				if (itr.Name.compareTo(Name) == 0) {

					main.ResumeBtn.setVisible(true);
				}
			}

		});

		primaryStage.setOnCloseRequest(e -> {
			try {
				if (snake.size() > 0)
					;
				{
					timer.stop();
					serialize();
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				
			}
		});
		main.StartBtn.setOnAction(e -> {
			resume = false;
			clear();
			primaryStage.setScene(new Scene(createContent()));

			primaryStage.setTitle("Game Page");
			primaryStage.getScene().setOnKeyPressed(e1 -> {
				if (e1.getCode() == KeyCode.LEFT && player.getView().getTranslateX() >= 8) {
					left();
				} else if (e1.getCode() == KeyCode.RIGHT && player.getView().getTranslateX() <= 370) {
					right();
				}
			});
		});

		main.LeadBtn.setOnAction(e -> {
			try {
				main.serializeLead();
				main.deserialize();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				
			}
			primaryStage.setTitle("Leaderboard");
			primaryStage.setScene(lead.Lead);

			lead.btn.setOnAction(e1 -> {
				primaryStage.setScene(main.mainpage);
				;
			});
		});

		main.ExitBtn.setOnAction(e -> {
			try {
				main.serializeLead();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				
			}
			Platform.exit();
		});

		main.ResumeBtn.setOnAction(e -> {
			resume = true;
			try {
				deserialize();
				// System.out.println(save.get(0).Name);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				
			}
			primaryStage.setScene(new Scene(createResumeContent()));

			primaryStage.setTitle("Game Page");
			primaryStage.getScene().setOnKeyPressed(e1 -> {
				if (e1.getCode() == KeyCode.LEFT && player.getView().getTranslateX() >= 8) {
					left();
				} else if (e1.getCode() == KeyCode.RIGHT && player.getView().getTranslateX() <= 370) {
					right();
				}
			});
		});

		primaryStage.show();

	}

	private class MainPage implements Serializable {// class for the mainpage of the game
		protected Scene mainpage;
		protected Button StartBtn, ResumeBtn, LeadBtn, ExitBtn;
		protected Label fname, ename;
		protected TextField fresh, exist;

		void serializeLead() throws Exception {//to serialize leaderboard
			FileOutputStream nfile = null;
			ObjectOutputStream nout = null;

			String dfilename = "data.txt";
			nfile = new FileOutputStream(dfilename);

			nout = new ObjectOutputStream(nfile);
			nout.writeObject(data);

			nout.close();
			nfile.close();

		}

		void deserialize() throws Exception {//to deserialize leaderboard
			FileInputStream nfile = null;

			ObjectInputStream nin = null;

			String dfilename = "data.txt";
			nfile = new FileInputStream(dfilename);
			nin = new ObjectInputStream(nfile);
			data = (ArrayList<Data>) nin.readObject();
			nin.close();
			nfile.close();
		}

		MainPage() {//mainpage construstor
			try {
				deserialize();
			} catch (Exception e) {
				// TODO Auto-generated catch block
			
			}
			Data d = new Data(Name, score, new Date().toString().substring(0, 10));
			data.add(0, d);

			// Name Button
			fname = new Label("New");
			fname.setLayoutX(20);
			fname.setLayoutY(100);
			fname.setFont(new Font("Roboto", 15));
			fname.setTextFill(Color.SIENNA);
			fname.setStyle("-fx-background-color: #000000");

			ename = new Label("Existing");
			ename.setLayoutX(200);
			ename.setLayoutY(100);
			ename.setFont(new Font("Roboto", 15));
			ename.setTextFill(Color.SIENNA);
			ename.setStyle("-fx-background-color: #000000");

			fresh = new TextField();
			fresh.setLayoutX(85);
			fresh.setLayoutY(100);
			fresh.setPrefSize(100, 20);
			fresh.setStyle("-fx-background-color:#A0522D");

			exist = new TextField();
			exist.setPrefSize(100, 20);
			exist.setLayoutX(280);
			exist.setLayoutY(100);
			exist.setStyle("-fx-background-color:#A0522D");

			// Start Button
			StartBtn = new Button("New Game");
			StartBtn.setLayoutX(155);
			StartBtn.setLayoutY(325);
			StartBtn.setFont(new Font("Roboto", 15));
			StartBtn.setTextFill(Color.AQUAMARINE);
			StartBtn.setStyle("-fx-background-color: #000000");

			// Leaderboard Button
			LeadBtn = new Button("Leaderboard");
			LeadBtn.setLayoutX(150);
			LeadBtn.setLayoutY(350);
			LeadBtn.setFont(new Font("Roboto", 15));
			LeadBtn.setTextFill(Color.AQUAMARINE);
			LeadBtn.setStyle("-fx-background-color: #000000");

			// Exit Button
			ExitBtn = new Button("Exit");
			ExitBtn.setLayoutX(180);
			ExitBtn.setLayoutY(375);
			ExitBtn.setFont(new Font("Roboto", 15));
			ExitBtn.setTextFill(Color.AQUAMARINE);
			ExitBtn.setStyle("-fx-background-color: #000000");

			// Resume Button
			ResumeBtn = new Button("Resume Game");
			ResumeBtn.setLayoutX(145);
			ResumeBtn.setLayoutY(400);
			ResumeBtn.setFont(new Font("Roboto", 15));
			ResumeBtn.setTextFill(Color.AQUAMARINE);
			ResumeBtn.setStyle("-fx-background-color: #000000");
			ResumeBtn.setVisible(false);

			Group root = new Group();

			mainpage = new Scene(root, 400, 600, Color.BLACK);

			ImageView img_snakemain = new ImageView(snakemain);
			img_snakemain.setFitHeight(30);
			img_snakemain.setFitWidth(30);
			img_snakemain.setTranslateX(55);
			img_snakemain.setTranslateY(100);

			ImageView img_snakemain1 = new ImageView(snakemain);
			img_snakemain1.setFitHeight(30);
			img_snakemain1.setFitWidth(30);
			img_snakemain1.setTranslateX(255);
			img_snakemain1.setTranslateY(100);

			ImageView img_crown = new ImageView(crown);
			img_crown.setFitHeight(50);
			img_crown.setFitWidth(50);
			img_crown.setTranslateX(150);
			img_crown.setTranslateY(490);

			root.getChildren().addAll(img_snakemain, img_snakemain1, img_crown);

			root.getChildren().addAll(fname, ename, fresh, exist);
			root.getChildren().add(StartBtn);
			root.getChildren().add(ResumeBtn);
			root.getChildren().add(LeadBtn);
			root.getChildren().add(ExitBtn);

			Text text1 = new Text(178, 210, "Snake");
			text1.setFill(Color.WHITE);
			text1.setFont(Font.font("Adam", 20));
			text1.setScaleX(2);
			text1.setScaleY(2);

			Text text3 = new Text(180, 290, "Block");
			text3.setFill(Color.WHITE);
			text3.setFont(Font.font("Adam", 20));
			text3.setScaleX(2);
			text3.setScaleY(2);

			Text text2 = new Text(195, 250, "VS");
			text2.setFill(Color.AQUAMARINE);
			text2.setFont(Font.font("Century Gothic", 20));
			text2.setScaleX(2.5);
			text2.setScaleY(2.5);

			// Last Score Counter
			mainscore = new Text(235, 520, "" + score);
			mainscore.setFill(Color.WHITE);
			mainscore.setScaleX(4);
			mainscore.setScaleY(4);
			mainscore.setFont(Font.font("Century Gothic", 10));

			root.getChildren().addAll(text1, text2, text3);
			root.getChildren().add(mainscore);

		}

	}

	private class extralife implements Serializable {//class to bonus implementation of option of an extralife for the game if the coins gained are more than 5
		Scene extra;
		protected Pane el;
		Button restart, main;

		extralife() {//extralife consttructor
			ImageView img_coin = new ImageView(coin);
			img_coin.setFitHeight(50);
			img_coin.setFitWidth(50);
			img_coin.setTranslateX(155);
			img_coin.setTranslateY(280);

			el = new Pane();
			extra = new Scene(el);
			el.setPrefSize(400, 600);
			el.setStyle("-fx-background-color: #000000");
			restart = new Button("Extra Life");
			restart.setLayoutX(170);
			restart.setLayoutY(340);
			restart.setFont(new Font("Roboto", 15));
			restart.setTextFill(Color.RED);
			restart.setStyle("-fx-background-color: #222222");

			main = new Button("Main Menu");
			main.setLayoutX(163);
			main.setLayoutY(380);
			main.setFont(new Font("Roboto", 15));
			main.setTextFill(Color.RED);
			main.setStyle("-fx-background-color: #222222");

			Text text2 = new Text(235, 310, "" + currency);
			text2.setFill(Color.RED);
			text2.setFont(Font.font("Century Gothic", 20));
			text2.setScaleX(2.5);
			text2.setScaleY(2.5);

			el.getChildren().addAll(img_coin, restart, text2, main);

		}
	}

	private class Leaderboard implements Serializable {// class for the leaderboard page

		Scene Lead;
		protected Pane leader;
		protected Button btn;

		Leaderboard() {//leaderboard constructor
			leader = new Pane();
			Lead = new Scene(leader);
			btn = new Button("Back");

			checkLeaderboard();
		}

		private void checkLeaderboard() {// method to display contents of the leaderboard

			Label label = new Label("Leaderboard");
			label.setFont(new Font("Roboto", 40));
			label.setLayoutX(20);

			btn.setLayoutX(10);
			btn.setLayoutY(10);

			Label l11 = new Label("Name");
			Label l21 = new Label("Score");
			Label l31 = new Label("Date");
			l11.setFont(new Font("Roboto", 20));
			l21.setFont(new Font("Roboto", 20));
			l31.setFont(new Font("Roboto", 20));

			int x1 = 80;
			l11.setTranslateX(x1 + 0);
			l21.setTranslateX(x1 + 100);
			l31.setTranslateX(x1 + 200);

			int y1 = 120;
			l11.setTranslateY(y1);
			l21.setTranslateY(y1);
			l31.setTranslateY(y1);

			leader.getChildren().addAll(l11, l21, l31);

			for (int i = 0; i < data.size() - 1; i++) {
				for (int j = 0; j < data.size() - 1 - i; j++) {
					if (data.get(j).score < data.get(j + 1).score) {
						Data temp = data.get(j);
						data.set(j, data.get(j + 1));
						data.set(j + 1, temp);
					}

				}
			}

			for (int i = 0; (i < data.size() && i < 10); i++) {

				Label l1 = new Label(data.get(i).name);
				Label l2 = new Label(data.get(i).score + "");
				Label l3 = new Label(data.get(i).date);

				int x = 80;
				l1.setTranslateX(x + 0);
				l2.setTranslateX(x + 100);
				l3.setTranslateX(x + 200);

				int y = 170 + i * 20;
				l1.setTranslateY(y);
				l2.setTranslateY(y);
				l3.setTranslateY(y);

				leader.getChildren().addAll(l1, l2, l3);
			}

			HBox hbox = new HBox();
			hbox.setPadding(new Insets(40, 20, 50, 90));
			hbox.getChildren().addAll(label);
			leader.getChildren().addAll(hbox, btn);
			leader.setPrefSize(400, 600);
			leader.setStyle("-fx-background-color: #5f9ea0");
		}
	}

}
