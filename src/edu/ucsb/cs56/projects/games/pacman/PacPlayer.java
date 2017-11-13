package edu.ucsb.cs56.projects.games.pacman;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Player controlled pacman character.
 *
 * @author Dario Castellanos
 * @author Daniel Ly
 * @author Kelvin Yang
 * @author Joseph Kompella
 * @author Kekoa Sato
 * @version CS56 F16
 */
public class PacPlayer extends Character {

	public final static String PATH_IMAGE_PACMAN = "assets/pacman/";
	public final static String PATH_IMAGE_MSPACMAN = "assets/mspacman/";
	public final static String PATH_AUDIO = "assets/audio/";
	public final static int PACMAN = 1;
	public final static int MSPACMAN = 2;
	private final int pacanimdelay = 2;
	private final int pacmananimcount = 4;
	private final int pacmanspeed = 4;
	int pacanimcount = pacanimdelay;
	int pacanimdir = 1;
	int pacmananimpos = 0;

	// need these so that when pacman collides with wall and stops moving
	// he keeps facing wall instead of facing default position
	public int direction;	

	private Image[] pacmanUp, pacmanDown, pacmanLeft, pacmanRight;
	private Audio[] pacmanAudio;
	private String assetAudioPath;
	private Grid grid;

	/**
	 * Constructor for PacPlayer class
	 *
	 * @param x the starting x coordinate of pacman
	 * @param y the starting y coordinate of pacman
	 */
	public PacPlayer(int x, int y) {
		this(x, y, PACMAN, null);
	}

	/**
	 * Constructor for PacPlayer class
	 *
	 * @param x         the starting x coordinate of pacman
	 * @param y         the starting y coordinate of pacman
	 * @param playerNum int representing who the player is controlling
	 * @param grid      the grid in which PacPlayer is part of.
	 */
	public PacPlayer(int x, int y, int playerNum, Grid grid) {
		super(x, y, playerNum);
		speed = pacmanspeed;
		this.grid = grid;
		lives = 3;
		direction = Direction.RIGHT;
		if (playerNum == PACMAN) 
			assetImagePath = PATH_IMAGE_PACMAN;
		else if (playerNum == MSPACMAN) 
			assetImagePath = PATH_IMAGE_MSPACMAN;
		assetAudioPath = PATH_AUDIO;
		loadImages();
		loadAudio();
	}

	public void resetPos() {
		super.resetPos();
		direction = Direction.RIGHT;
	}

	public void death() {
		if (deathTimer <= 0) {
			lives--;
			deathTimer = 40;
			resetPos();
		}
		if (lives <= 0) {
			alive = false;
		}
	}

	/**
	 * Moves character's current position with the board's collision
	 *
	 * @param grid The Grid to be used for collision
	 */
	public void move(Grid grid) {
		if (deathTimer > 0) deathTimer--;
		short ch;

		//allows you to switch directions even when you are not in a grid
		if (reqdx == -dx && reqdy == -dy) {
			dx = reqdx;
			dy = reqdy;
			if(dx != 0 || dy != 0)
				direction = ((direction + 1) % 4) + 1;
		}
	
		if (x % Board.BLOCKSIZE == 0 && y % Board.BLOCKSIZE == 0) {

			//Tunnel effect
			x = ((x / Board.BLOCKSIZE + Board.NUMBLOCKS) % Board.NUMBLOCKS) * Board.BLOCKSIZE;
			y = ((y / Board.BLOCKSIZE + Board.NUMBLOCKS) % Board.NUMBLOCKS) * Board.BLOCKSIZE;

			ch = grid.screenData[y / Board.BLOCKSIZE][x / Board.BLOCKSIZE];

			//if pellet, eat and increase score
			if ((ch & GridData.GRID_CELL_PELLET) != 0) {
				//Toggles pellet bit
				grid.screenData[y / Board.BLOCKSIZE][x / Board.BLOCKSIZE] = (short) (ch ^ GridData.GRID_CELL_PELLET);
				playAudio(0);
				Board.score += Board.SCORE_PELLET;
				speed = 3;
			}
			//if fruit, eat and increase score
			else if ((ch & GridData.GRID_CELL_FRUIT) != 0) {
				//Toggles fruit bit
				grid.screenData[y / Board.BLOCKSIZE][x / Board.BLOCKSIZE] = (short) (ch ^ GridData.GRID_CELL_FRUIT);
				Board.score += Board.SCORE_FRUIT;
				playAudio(1);
				speed = 3;
			}
			else if((ch & GridData.GRID_CELL_POWER_PILL) != 0) {
				//Toggles pill bit
				grid.screenData[y / Board.BLOCKSIZE][x / Board.BLOCKSIZE] = (short) (ch ^ GridData.GRID_CELL_POWER_PILL);
				playAudio(1);
				Board.score += Board.SCORE_POWER_PILL;
				speed = 3;
			}
			else
				speed = 4;

			//passes key commands to movement
			if(reqdx != 0 || reqdy != 0) {
				if ( Character.moveable(reqdx, reqdy, ch) ) {
					dx = reqdx;
					dy = reqdy;
					if(reqdx == -1 && reqdy == 0 && (ch & GridData.GRID_CELL_BORDER_LEFT) == 0)
						direction = Direction.LEFT;
					if(reqdx == 0 && reqdy == -1 && (ch & GridData.GRID_CELL_BORDER_TOP) == 0)
						direction = Direction.UP;
					if(reqdx == 1 && reqdy == 0 && (ch & GridData.GRID_CELL_BORDER_RIGHT) == 0)
						direction = Direction.RIGHT;
					if(reqdx == 0 && reqdy == 1 && (ch & GridData.GRID_CELL_BORDER_BOTTOM) == 0)
						direction = Direction.DOWN;
				}
			}

			// Check for standstill, stop movement if hit wall
			if ( !Character.moveable(dx, dy, ch) ) {
				dx = 0;
				dy = 0;
			}
		}
		move();
	}

	/**
	 * Calls the appropriate draw method for the direction Pacman is facing
	 *
	 * @param g2d    a Graphics2D object
	 * @param canvas A Jcomponent object to be drawn on
	 */
	public void draw(Graphics2D g2d, JComponent canvas) {
		if (deathTimer % 5 > 3) return; // Flicker while invincibile
		doAnim();
		if (direction == Direction.LEFT)
			g2d.drawImage(pacmanLeft[pacmananimpos], x + 4, y + 4, canvas);
		else if (direction == Direction.UP)
			g2d.drawImage(pacmanUp[pacmananimpos], x + 4, y + 4, canvas);
		else if (direction == Direction.DOWN)
			g2d.drawImage(pacmanDown[pacmananimpos], x + 4, y + 4, canvas);
		else 
			g2d.drawImage(pacmanRight[pacmananimpos], x + 4, y + 4, canvas);
	}

	/**
	 * Moves character's current position with the board's collision
	 *
	 * @param grid The Grid to be used for collision
	 */
	@Override
	public void moveAI(Grid grid, Character[] c) {
	}

	/**
	 * Animates the Pacman sprite's direction as well as mouth opening and closing
	 */
	public void doAnim() {
		pacanimcount--;
		if (pacanimcount <= 0) {
			pacanimcount = pacanimdelay;
			pacmananimpos = pacmananimpos + pacanimdir;
			if (pacmananimpos == (pacmananimcount - 1) || pacmananimpos == 0)
				pacanimdir = -pacanimdir;
		}
	}

	/**
	 * Handles key presses for game controls
	 *
	 * @param key Integer representing the key pressed
	 */
	public void keyPressed(int key) {
		if (playerNum == PACMAN) {
			switch (key) {
				case KeyEvent.VK_LEFT:
					reqdx = -1;
					reqdy = 0;
					break;
				case KeyEvent.VK_RIGHT:
					reqdx = 1;
					reqdy = 0;
					break;
				case KeyEvent.VK_UP:
					reqdx = 0;
					reqdy = -1;
					break;
				case KeyEvent.VK_DOWN:
					reqdx = 0;
					reqdy = 1;
					break;
				default:
					break;
			}
		} else if (playerNum == MSPACMAN) {
			switch (key) {
				case KeyEvent.VK_A:
					reqdx = -1;
					reqdy = 0;
					break;
				case KeyEvent.VK_D:
					reqdx = 1;
					reqdy = 0;
					break;
				case KeyEvent.VK_W:
					reqdx = 0;
					reqdy = -1;
					break;
				case KeyEvent.VK_S:
					reqdx = 0;
					reqdy = 1;
					break;
				default:
					break;
			}
		}
	}

	@Override
	public void keyReleased(int key) {
		move(this.grid);
		if (playerNum == PACMAN) {
			switch (key) {
				case KeyEvent.VK_LEFT:
					reqdx = 0;
					break;
				case KeyEvent.VK_RIGHT:
					reqdx = 0;
					break;
				case KeyEvent.VK_UP:
					reqdy = 0;
					break;
				case KeyEvent.VK_DOWN:
					reqdy = 0;
					break;
				default:
					break;
			}
		} else if (playerNum == MSPACMAN) {
			switch (key) {
				case KeyEvent.VK_A:
					reqdx = 0;
					break;
				case KeyEvent.VK_D:
					reqdx = 0;
					break;
				case KeyEvent.VK_W:
					reqdy = 0;
					break;
				case KeyEvent.VK_S:
					reqdy = 0;
					break;
				default:
					break;
			}
		}
	}

	/**
	 * Load game sprites from images folder
	 */
	@Override
	public void loadImages()
	{
		pacmanUp = new Image[4];
		pacmanDown = new Image[4];
		pacmanLeft = new Image[4];
		pacmanRight = new Image[4];

		try {
			pacmanUp[0] = ImageIO.read(getClass().getResource(assetImagePath + "pacmanup.png"));
			pacmanUp[1] = ImageIO.read(getClass().getResource(assetImagePath + "up1.png"));
			pacmanUp[2] = ImageIO.read(getClass().getResource(assetImagePath + "up2.png"));
			pacmanUp[3] = ImageIO.read(getClass().getResource(assetImagePath + "up3.png"));
			pacmanDown[0] = ImageIO.read(getClass().getResource(assetImagePath + "pacmandown.png"));
			pacmanDown[1] = ImageIO.read(getClass().getResource(assetImagePath + "down1.png"));
			pacmanDown[2] = ImageIO.read(getClass().getResource(assetImagePath + "down2.png"));
			pacmanDown[3] = ImageIO.read(getClass().getResource(assetImagePath + "down3.png"));
			pacmanLeft[0] = ImageIO.read(getClass().getResource(assetImagePath + "pacmanleft.png"));
			pacmanLeft[1] = ImageIO.read(getClass().getResource(assetImagePath + "left1.png"));
			pacmanLeft[2] = ImageIO.read(getClass().getResource(assetImagePath + "left2.png"));
			pacmanLeft[3] = ImageIO.read(getClass().getResource(assetImagePath + "left3.png"));
			pacmanRight[0] = ImageIO.read(getClass().getResource(assetImagePath + "pacmanright.png"));
			pacmanRight[1] = ImageIO.read(getClass().getResource(assetImagePath + "right1.png"));
			pacmanRight[2] = ImageIO.read(getClass().getResource(assetImagePath + "right2.png"));
			pacmanRight[3] = ImageIO.read(getClass().getResource(assetImagePath + "right3.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load game audio from audio folder
	 */
	public void loadAudio()
	{
		try {
			String[] sounds = {"chomp.wav", "eatfruit.wav"};
			pacmanAudio = new Audio[sounds.length];
			for(int i = 0; i < sounds.length; i++) {
				pacmanAudio[i] = new Audio(getClass().getResourceAsStream(assetAudioPath + sounds[i]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Plays a sound from pacman audio array.
	 *
	 * @param sound sound effect ID
	 */
	public void playAudio(int sound)
	{
		try {
			pacmanAudio[sound].play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the image used for displaying remaining lives
	 *
	 * @return image of pacman facing left
	 */
	@Override
	public Image getLifeImage() {
		return pacmanRight[3];
	}
}
