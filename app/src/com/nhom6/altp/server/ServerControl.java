package com.nhom6.altp.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.nhom6.altp.model.DBUtils;
import com.nhom6.altp.model.IDProvider;
import com.nhom6.altp.model.Player;
import com.nhom6.altp.model.Question;

public class ServerControl implements Runnable {

	private ServerView serverView;
	private DatagramSocket serverSocket;
	private int serverPort = 8192;

	private Thread run, send, receive;
	private boolean running = false;

	private List<Player> listClient = new ArrayList<>();

	public ServerControl(ServerView sv) {
		this.serverView = sv;
		DBUtils.getDBConnection("altp", "root", "123456");
		openServer(serverPort);
		run = new Thread(this, "ServerThread");
		running = true;
		run.start();
		serverView.addThoatListener(new BtnThoatListener());
	}

	private void openServer(int port) {
		try {
			serverSocket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		serverView.showMessage("server is running at port " + serverPort);
		receive();
	}

	private void receive() {
		receive = new Thread("serverReceivingThread") {
			@Override
			public void run() {
				while (running) {
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data,
							data.length);
					try {
						serverSocket.receive(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
					process(packet);
				}
			}
		};
		receive.start();
	}

	private void process(DatagramPacket packet) {
		String result = null;
		try {
			result = new String(packet.getData(), 0, packet.getLength(),
					"UTF-8");
			if (result.startsWith("/l/")) {
				// Check login
				checkLogin(result, packet);
			} else if (result.startsWith("/p/")) {
				// doi pass
				changePass(result, packet);
			} else if (result.startsWith("/r/")) {
				// Them account
				addAccount(result, packet);
			} else if (result.startsWith("/g/")) {
				// gui thong tin bang table player
				getListPlayer(packet);
			} else if (result.startsWith("/t/")) {
				// remove active account
				String id = result.substring(3);
				for (Player player : listClient) {
					if (player.getID() == Integer.parseInt(id)) {
						listClient.remove(player);
						break;
					}
				}
			} else if (result.startsWith("/d/")) {
				// xoa tai khoan
				deleteAccount(result, packet);
			} else if (result.startsWith("/c/")) {
				// doi cau hoi
				doiCauHoi(result, packet);
			} else if (result.startsWith("/h/")) {
				// lay danh sach cau hoi
				getListCauHoi(packet);
			} else if (result.startsWith("/s/")) {
				// update diem so
				updateDiem(result, packet);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendData(final byte[] data, final InetAddress address,
			final int port) {
		send = new Thread("Send") {
			@Override
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length,
						address, port);
				try {
					serverSocket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}

	private void checkLogin(String result, DatagramPacket packet) {
		// Kiem tra login
		String username = result.substring(3, result.indexOf("|"));
		String password = result.substring(result.indexOf("|") + 1);
		// Kiem tra dang nhap chua?
		for (Player player : listClient) {
			if (player.getUsername().equals(username)) {
				String msg = "/l/false";
				sendData(msg.getBytes(), packet.getAddress(), packet.getPort());
				break;
			}
		}
		// check db
		if (DBUtils.checkUser(username, password)) {
			int ID = IDProvider.getId();
			int score = DBUtils.getPlayerScore(username);
			listClient.add(new Player(ID, username, password, score, packet
					.getAddress(), packet.getPort()));
			String msg = "/l/" + ID + "|" + score;
			sendData(msg.getBytes(), packet.getAddress(), packet.getPort());
		} else {
			String msg = "/l/false";
			sendData(msg.getBytes(), packet.getAddress(), packet.getPort());
		}
	}

	private void changePass(String result, DatagramPacket packet) {
		String username = result.substring(3, result.indexOf("|"));
		String newPass = result.substring(result.indexOf("|") + 1);
		if (DBUtils.changePass(username, newPass)) {
			String msg = "/p/ok";
			sendData(msg.getBytes(), packet.getAddress(), packet.getPort());
		} else {
			String msg = "/p/false";
			sendData(msg.getBytes(), packet.getAddress(), packet.getPort());
		}
	}

	private void addAccount(String result, DatagramPacket packet) {
		String username = result.substring(3, result.indexOf("|"));
		String newPass = result.substring(result.indexOf("|") + 1);
		if (DBUtils.checkUser(username, newPass)) {
			String msg = "/r/false";
			sendData(msg.getBytes(), packet.getAddress(), packet.getPort());
		} else {
			if (DBUtils.addUser(username, newPass)) {
				String msg = "/r/ok";
				sendData(msg.getBytes(), packet.getAddress(), packet.getPort());
			} else {
				String msg = "/r/false";
				sendData(msg.getBytes(), packet.getAddress(), packet.getPort());
			}
		}
	}

	private void deleteAccount(String result, DatagramPacket packet) {
		String user = result.substring(3);
		if (DBUtils.deleteUser(user)) {
			String msg = "/d/ok";
			sendData(msg.getBytes(), packet.getAddress(), packet.getPort());
		} else {
			String msg = "/d/false";
			sendData(msg.getBytes(), packet.getAddress(), packet.getPort());
		}
	}

	private void doiCauHoi(String result, DatagramPacket packet) {
		String id = result.substring(3, result.indexOf("|"));
		String level = result.substring(result.indexOf("|") + 1);
		Question question = DBUtils.doiCauHoi(Integer.parseInt(id),
				Integer.parseInt(level));
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(question);
			byte[] data = baos.toByteArray();
			sendData(data, packet.getAddress(), packet.getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void getListPlayer(DatagramPacket packet) {
		try {
			List<Player> listPlayer = DBUtils.getListPlayer();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			// for (Player player : listPlayer) {
			// oos.writeObject(player);
			// }
			// oos.flush();
			oos.writeObject(listPlayer);
			byte[] data = baos.toByteArray();
			sendData(data, packet.getAddress(), packet.getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getListCauHoi(DatagramPacket packet) {
		try {
			List<Question> listQuestion = DBUtils.getListQuestions();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			// for (Player player : listPlayer) {
			// oos.writeObject(player);
			// }
			// oos.flush();
			oos.writeObject(listQuestion);
			byte[] data = baos.toByteArray();
			sendData(data, packet.getAddress(), packet.getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateDiem(String result, DatagramPacket packet) {
		String username = result.substring(3, result.indexOf("|"));
		String score = result.substring(result.indexOf("|") + 1);
		if (DBUtils.updateScore(username, Integer.parseInt(score))) {
			System.out.println("Cap nhat diem");
		} else {
			System.out.println("ko cap nhat duoc");
		}
	}

	private class BtnThoatListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int option = JOptionPane.showConfirmDialog(serverView,
					"Ban co muon tat server ko?", "Xac nhan",
					JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				serverView.close();
			}
		}
	}
}
