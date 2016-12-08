// The main menu GameState.

package com.neet.DiamondHunter.GameState;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.neet.DiamondHunter.Manager.Content;
import com.neet.DiamondHunter.Manager.GameStateManager;
import com.neet.DiamondHunter.Manager.JukeBox;
import com.neet.DiamondHunter.Manager.Keys;

public class MenuState extends GameState {
	
	private BufferedImage bg;
	private BufferedImage diamond;
	
	private int currentOption = 0;
	private String[] options = {
		"START",
		"LOAD ITEMS",
		"QUIT"
	};
	
	public MenuState(GameStateManager gsm) {
		super(gsm);
	}
	
	public void init() {
		bg = Content.MENUBG[0][0];
		diamond = Content.DIAMOND[0][0];
		JukeBox.load("/SFX/collect.wav", "collect");
		JukeBox.load("/SFX/menuoption.wav", "menuoption");
	}
	
	public void update() {
		handleInput();
	}
	
	public void draw(Graphics2D g) {
		
		g.drawImage(bg, 0, 0, null);
		
		Content.drawString(g, options[0], 44, 90);
		Content.drawString(g, options[1], 24, 105);
		Content.drawString(g, options[2], 48, 120);
		
		if(currentOption == 0) g.drawImage(diamond, 25, 86, null);
		else if(currentOption == 1) g.drawImage(diamond, 8, 101, null);
		else if(currentOption == 2) g.drawImage(diamond, 25, 116, null);
		
	}
	
	public void handleInput() {
		if(Keys.isPressed(Keys.DOWN) && currentOption < options.length - 1) {
			JukeBox.play("menuoption");
			currentOption++;
		}
		if(Keys.isPressed(Keys.UP) && currentOption > 0) {
			JukeBox.play("menuoption");
			currentOption--;
		}
		if(Keys.isPressed(Keys.ENTER)) {
			JukeBox.play("collect");
			selectOption();
		}
	}
	
	private void selectOption() {
		if(currentOption == 0) {
			gsm.setState(GameStateManager.PLAY);
		}
		
		if(currentOption == 1) {
			Content.loadItems();
		}
		
		if(currentOption == 2) {
			System.exit(0);
		}
	}
	
}
