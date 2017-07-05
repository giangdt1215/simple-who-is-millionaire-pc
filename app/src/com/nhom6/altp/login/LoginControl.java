package com.nhom6.altp.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import com.nhom6.altp.client.ClientControl;
import com.nhom6.altp.client.ClientView;
import com.nhom6.altp.model.BaseControl;
import com.nhom6.altp.model.Player;

public class LoginControl extends BaseControl{

	private LoginView loginView;
	private int serverPort = 8192;
	private String serverHost = "localhost";

	public LoginControl(LoginView lv) {
		this.loginView = lv;
		this.loginView.addLoginListener(new LoginListener());
	}

	private class LoginListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			openConnection();
			String msg = loginView.getPlayerInformation();
			sendData(msg);
			String result = receiveData();
			if (result.equals("/l/false")) {
				loginView.showMessage("Dang nhap that bai");
			} else {
				// loginView.showMessage("Dang nhap thanh cong");
				String username = msg.substring(3, msg.indexOf("|"));
				String password = msg.substring(msg.indexOf("|") + 1);
				String id = result.substring(3, result.indexOf("|"));
				String score = result.substring(result.indexOf("|") + 1);
				System.out.println(id + ":" + username + ":" + password + ":"
						+ score);
				boolean isAdmin = username.equals("admin");
				new ClientControl(new ClientView(isAdmin), new Player(
						Integer.parseInt(id), username, password,
						Integer.parseInt(score)));
				loginView.closeLoginWindow();
			}
			closeConnection();
		}
	}

	protected void sendData(String msg) {
		try {
			// ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// ObjectOutputStream oos = new ObjectOutputStream(baos);
			// oos.writeObject(user);
			// oos.flush();

			InetAddress ia = InetAddress.getByName(serverHost);
			byte[] data = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length, ia,
					serverPort);
			clientSocket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected String receiveData() {
		String result = "";
		byte[] data = new byte[1024];
		try {
			DatagramPacket packetReceived = new DatagramPacket(data,
					data.length);
			clientSocket.receive(packetReceived);
			// ByteArrayInputStream bais = new ByteArrayInputStream(data);
			// ObjectInputStream ois = new ObjectInputStream(bais);
			result = new String(packetReceived.getData(), 0,
					packetReceived.getLength(), "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
