package com.nhom6.altp.model;

import java.net.DatagramSocket;
import java.net.SocketException;

public abstract class BaseControl {

	protected DatagramSocket clientSocket;
	
	protected void openConnection() {
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	protected void closeConnection(){
		clientSocket.close();
	}
	protected abstract void sendData(String msg);
	protected abstract String receiveData(); 
}
