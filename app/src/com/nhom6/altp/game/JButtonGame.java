package com.nhom6.altp.game;

import java.awt.Cursor;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class JButtonGame extends JButton{
	
	private static final long serialVersionUID = 1L;
	
	private ImageIcon buttonOver;
	private ImageIcon buttonNormal;
	
	public JButtonGame(String buttonNormal, String buttonOver, int x, int y, int width, int height){
		try {
			this.buttonOver = new ImageIcon(ImageIO.read(this.getClass().getResource(buttonOver)));
			this.buttonNormal = new ImageIcon(ImageIO.read(this.getClass().getResource(buttonNormal)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				mouseEnter();
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				mouseLeave();
			}
		});
		
		mouseLeave();
		this.setBounds(x, y, width, height);
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
	}
	
	private void mouseEnter(){
		this.setIcon(buttonOver);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	
	private void mouseLeave(){
		this.setIcon(buttonNormal);
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}
