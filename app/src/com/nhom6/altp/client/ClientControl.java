package com.nhom6.altp.client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.nhom6.altp.game.GameControl;
import com.nhom6.altp.game.GameView;
import com.nhom6.altp.login.LoginControl;
import com.nhom6.altp.login.LoginView;
import com.nhom6.altp.model.BaseControl;
import com.nhom6.altp.model.Player;
import com.nhom6.altp.model.Question;

public class ClientControl extends BaseControl{

	private ClientView clientView;
	private boolean isAdmin = false;
	private Player player;
	private int serverPort = 8192;
	private String serverHost = "localhost";

	private List<Player> listPlayer = new ArrayList<>();

	public ClientControl(ClientView cv, Player player) {
		this.clientView = cv;
		this.player = player;
		if (player.getUsername().equals("admin")) {
			this.isAdmin = true;
		} else {
			this.isAdmin = false;
		}
		this.clientView.setID(player.getID());
		this.clientView.setUsername(player.getUsername());
		this.clientView.setScore(player.getScore());
		this.clientView.setWindowAdapter(windowAdapter);
		this.clientView.addChangePassListener(new ChangePassListener());
		if (isAdmin) {
			this.clientView.addAddAccListener(new AddAccountListener());
			this.clientView.addDeleteAccListener(new DeleteAccListener());
			openConnection();
			updateClientTable();
			closeConnection();
		}
		this.clientView.addPlayGameListener(new PlayGameListener());
	}

	private WindowAdapter windowAdapter = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			int result = JOptionPane.showConfirmDialog(clientView,
					"Ban co muon thoat khong?", "Xac nhan",
					JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				openConnection();
				sendData("/t/" + player.getID());
				closeConnection();
				clientView.dispose();
				new LoginControl(new LoginView());
			}
		}
	};

	private class ChangePassListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			openConnection();
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 2, 5, 5));
			JLabel matKhauCu = new JLabel("Nhap mat khau cu:");
			JPasswordField passCu = new JPasswordField(14);
			JLabel matKhauMoi = new JLabel("Nhap mat khau moi:");
			JPasswordField passMoi = new JPasswordField(14);
			panel.add(matKhauCu);
			panel.add(passCu);
			panel.add(matKhauMoi);
			panel.add(passMoi);
			String[] options = new String[] { "OK", "Cancel" };
			int option = JOptionPane.showOptionDialog(null, panel,
					"Doi mat khau", JOptionPane.NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
			if (option == 0) // pressing OK button
			{
				char[] temp = passCu.getPassword();
				if (player.getPassword().equals(new String(temp))) {
					char[] password = passMoi.getPassword();
					String msg = "/p/" + player.getUsername() + "|"
							+ String.valueOf(password);
					sendData(msg);
					String result = receiveData();
					if (result.equals("/p/ok")) {
						clientView.showMessage("Doi mat khau thanh cong");
					} else {
						clientView.showMessage("Doi mat khau that bai");
					}
				} else {
					clientView.showMessage("Nhap sai mat khau cu");
				}
			}
			closeConnection();
		}
	}

	private class AddAccountListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			openConnection();
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 2, 5, 5));
			JLabel lblUser = new JLabel("Username:");
			JTextField txtUser = new JTextField(14);
			JLabel lblPass = new JLabel("Password:");
			JPasswordField passMoi = new JPasswordField(14);
			panel.add(lblUser);
			panel.add(txtUser);
			panel.add(lblPass);
			panel.add(passMoi);
			String[] options = new String[] { "OK", "Cancel" };
			int option = JOptionPane.showOptionDialog(null, panel,
					"Them tai khoan", JOptionPane.NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (option == 0) {
				char[] temp = passMoi.getPassword();
				String pass = new String(temp);
				String user = txtUser.getText();
				if (user.contains(" ")) {
					clientView.showMessage("User ko co dau cach");
					return;
				}
				String msg = "/r/" + user + "|" + pass;
				sendData(msg);
				String result = receiveData();
				if (result.equals("/r/ok")) {
					clientView.showMessage("Them tai khoan thanh cong");
					updateClientTable();
				} else {
					clientView.showMessage("Khong them duoc tai khoan");
				}
			}
			closeConnection();
		}

	}

	private class DeleteAccListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			openConnection();
			String name = clientView.getAccountName();
			if (!name.equals("")) {
				int option = JOptionPane
						.showConfirmDialog(clientView, "Xoa tai khoan " + name
								+ " ?", "Xac nhan", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (option == JOptionPane.YES_OPTION) {
					String msg = "/d/" + name;
					sendData(msg);
					String result = receiveData();
					if (result.equals("/d/ok")) {
						clientView.showMessage("Xoa thanh cong");
						updateClientTable();
					} else {
						clientView.showMessage("Xoa that bai");
					}
				}
			} else {
				clientView.showMessage("Phai chon account o bang");
			}
			closeConnection();
		}
	}

	private class PlayGameListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			openConnection();
			String msg = "/h/";
			sendData(msg);
			List<Question> listQuestion = receiveListQuestion();
			new GameControl(new GameView(), player, listQuestion);
			closeConnection();
			clientView.dispose();
		}
	}

	private void updateClientTable() {
		sendData("/g/");
		listPlayer = receiveListPlayer();
		clientView.showTable(listPlayer);
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
			result = new String(packetReceived.getData(), 0,
					packetReceived.getLength(), "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private List<Player> receiveListPlayer() {
		List<Player> listPlayer = new ArrayList<Player>();
		byte[] data = new byte[4096];
		try {
			DatagramPacket packetReceived = new DatagramPacket(data,
					data.length);
			clientSocket.receive(packetReceived);
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			ObjectInputStream ois = new ObjectInputStream(bais);
			listPlayer = (ArrayList<Player>) ois.readObject();
			return listPlayer;
			// Player player = (Player) ois.readObject();
			// listPlayer.add(player);
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private List<Question> receiveListQuestion() {
		List<Question> listQuestion = new ArrayList<Question>();
		byte[] data = new byte[4096];
		try {
			DatagramPacket packetReceived = new DatagramPacket(data,
					data.length);
			clientSocket.receive(packetReceived);
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			ObjectInputStream ois = new ObjectInputStream(bais);
			listQuestion = (ArrayList<Question>) ois.readObject();
			return listQuestion;
			// Player player = (Player) ois.readObject();
			// listPlayer.add(player);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
