package com.nhom6.altp.model;

import java.io.Serializable;
import java.net.InetAddress;

public class Player implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	private int score;
	
	private final int ID;
	private InetAddress ipAddress;
	private int port;

	public Player(){
		this.ID = 0;
	}
	
	public Player(final int ID, String username, String password, int score) {
		this.ID = ID;
		this.username = username;
		this.password = password;
		this.score = score;
	}
	
	public Player(String username, String password, int score) {
		this.ID = 0;
		this.username = username;
		this.password = password;
		this.score = score;
	}
	
	public Player(final int ID, String username, String password, int score, InetAddress address, int port) {
		this.ID = ID;
		this.username = username;
		this.password = password;
		this.score = score;
		this.ipAddress = address;
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getID() {
		return ID;
	}

	public InetAddress getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
