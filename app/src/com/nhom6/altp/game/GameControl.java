package com.nhom6.altp.game;

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
import java.util.Random;

import javax.swing.JOptionPane;

import com.nhom6.altp.client.ClientControl;
import com.nhom6.altp.client.ClientView;
import com.nhom6.altp.model.BaseControl;
import com.nhom6.altp.model.Player;
import com.nhom6.altp.model.Question;
import com.nhom6.altp.model.SoundManager;

public class GameControl extends BaseControl implements Runnable {

	private Player player;
	private GameView gameView;
	private int serverPort = 8192;
	private String serverHost = "localhost";

	private List<Question> listQuestions;
	private int questionIndex = 0, totalScore;
	private int questionScore[] = new int[] { 500, 1000, 2000, 5000, 10000,
			20000, 50000, 75000, 15000, 25000, 500000, 1000000, 1250000,
			1500000, 2000000 };
	private boolean dangChoi = false, tamDung = false, nextQuestion = false,
			helpButton = false;
	private int timeCount = 30;
	private SoundManager soundManager;
	private Random rand;

	public GameControl(GameView gameView, Player player,
			List<Question> listQuestion) {
		this.gameView = gameView;
		this.player = player;
		this.listQuestions = listQuestion;
		this.totalScore = this.player.getScore();
		this.gameView.setWindowAdapter(windowAdapter);
		this.gameView.updatePlayer(this.player.getUsername(), totalScore);
		this.gameView.updatePrizeColumn(questionIndex);
		this.gameView.updateQuestion(listQuestion.get(questionIndex));
		rand = new Random();
		addButtonListener();
		soundManager = new SoundManager();
		soundManager.playSound("nhacNen.wav", true);
		dangChoi = true;
		Thread timeThread = new Thread(this, "TimeThread");
		timeThread.start();
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

	private Question receiveQuestion() {
		byte[] data = new byte[1024];
		try {
			DatagramPacket packetReceived = new DatagramPacket(data,
					data.length);
			clientSocket.receive(packetReceived);
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (Question) ois.readObject();
			// Player player = (Player) ois.readObject();
			// listPlayer.add(player);
		} catch (IOException | ClassNotFoundException e) {
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

	private WindowAdapter windowAdapter = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			tamDung = true;
			int result = JOptionPane.showConfirmDialog(gameView,
					"Ban co muon thoat khong?", "Xac nhan",
					JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				openConnection();
				player.setScore(totalScore);
				String msg = "/s/" + player.getUsername() + "|" + totalScore;
				sendData(msg);
				closeConnection();
				dangChoi = false;
				boolean isAdmin;
				if (player.getUsername().equals("admin")) {
					isAdmin = true;
				} else {
					isAdmin = false;
				}
				dangChoi=false;
				soundManager.stopSound();
				new ClientControl(new ClientView(isAdmin), player);
				gameView.closeWindow();
			} else {
				dangChoi = true;
				tamDung = false;
			}
		}
	};

	private void addButtonListener() {
		this.gameView.addBtn5050Listener(new Btn5050Listener());
		this.gameView.addBtnDoiCauHoiListener(new BtnDoiCauHoiListener());
		this.gameView.addBtnExitListener(new BtnQuitListener());
		this.gameView.addBtnCaseAListener(new BtnCaseAListener());
		this.gameView.addBtnCaseBListener(new BtnCaseBListener());
		this.gameView.addBtnCaseCListener(new BtnCaseCListener());
		this.gameView.addBtnCaseDListener(new BtnCaseDListener());
	}

	private class Btn5050Listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			soundManager.playSound("50_50.wav", false);
			helpButton = true;
			int trueAnswer = listQuestions.get(questionIndex).getTruecase();
			int sai[] = new int[2];
			int count = 0, wrongAnswer;
			while (true) {
				wrongAnswer = rand.nextInt(4) + 1;
				if (count > 0) {
					if (wrongAnswer == sai[0]) {
						continue;
					}
				}
				if (wrongAnswer == trueAnswer) {
					continue;
				} else {
					sai[count] = wrongAnswer;
					count++;
				}
				if (count >= 2) {
					break;
				}
			}
			gameView.btn5050Used(sai[0], sai[1]);
		}
	}

	private class BtnDoiCauHoiListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			openConnection();
			Question tempQuestion = listQuestions.get(questionIndex);
			String msg = "/c/" + (tempQuestion.getId()) + "|"
					+ tempQuestion.getLevel();
			sendData(msg);
			Question questionMoi = receiveQuestion();
			gameView.updateQuestion(questionMoi);
			listQuestions.get(questionIndex).replaceQuestion(questionMoi);
			soundManager.playSound("doiCauHoi.wav", false);
			helpButton = true;
			gameView.btnDoiCauHoiUsed();
			closeConnection();
		}
	}

	private class BtnQuitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			tamDung = true;
			int option = JOptionPane.showConfirmDialog(gameView,
					"Ban muon thoat ko?", "Xac nhan",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (option == JOptionPane.YES_OPTION) {
				openConnection();
				player.setScore(totalScore);
				String msg = "/s/" + player.getUsername() + "|" + totalScore;
				sendData(msg);
				closeConnection();
				boolean isAdmin;
				if (player.getUsername().equals("admin")) {
					isAdmin = true;
				} else {
					isAdmin = false;
				}
				dangChoi = false;
				soundManager.stopSound();
				new ClientControl(new ClientView(isAdmin), player);
				gameView.closeWindow();
			} else {
				tamDung = false;
				dangChoi = true;
			}
		}
	}

	private class BtnCaseAListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int trueCase = listQuestions.get(questionIndex).getTruecase();
			if (trueCase == 1) {
				nextQuestion = true;
				gameView.updateButton(trueCase);
				soundManager.playSound("traLoiDung.wav", false);
				totalScore += questionScore[questionIndex];
				if (questionIndex == 14) {
					gameView.showMessage("Ban da tra loi het cac cau hoi");
					choiLai();
				}
				questionIndex++;
				timeCount = 30;
				gameView.updatePrizeColumn(questionIndex);
				gameView.updatePlayer(player.getUsername(), totalScore);
			} else {
				gameView.updateButton(trueCase);
				soundManager.playSound("traLoiSai.wav", false);
				choiLai();
			}
		}
	}

	private class BtnCaseBListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int trueCase = listQuestions.get(questionIndex).getTruecase();
			if (trueCase == 2) {
				nextQuestion = true;
				gameView.updateButton(trueCase);
				soundManager.playSound("traLoiDung.wav", false);
				totalScore += questionScore[questionIndex];
				if (questionIndex == 14) {
					gameView.showMessage("Ban da tra loi het cac cau hoi");
					choiLai();
				}
				timeCount = 30;
				questionIndex++;
				gameView.updatePrizeColumn(questionIndex);
				gameView.updatePlayer(player.getUsername(), totalScore);
			} else {
				soundManager.playSound("traLoiSai.wav", false);
				gameView.updateButton(trueCase);
				choiLai();
			}
		}
	}

	private class BtnCaseCListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int trueCase = listQuestions.get(questionIndex).getTruecase();
			if (trueCase == 3) {
				nextQuestion = true;
				gameView.updateButton(trueCase);
				soundManager.playSound("traLoiDung.wav", false);
				totalScore += questionScore[questionIndex];
				if (questionIndex == 14) {
					gameView.showMessage("Ban da tra loi het cac cau hoi");
					choiLai();
				}
				timeCount = 30;
				questionIndex++;
				gameView.updatePrizeColumn(questionIndex);
				gameView.updatePlayer(player.getUsername(), totalScore);
			} else {
				soundManager.playSound("traLoiSai.wav", false);
				gameView.updateButton(trueCase);
				choiLai();
			}
		}
	}

	private class BtnCaseDListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int trueCase = listQuestions.get(questionIndex).getTruecase();
			if (trueCase == 4) {
				nextQuestion = true;
				gameView.updateButton(trueCase);
				soundManager.playSound("traLoiDung.wav", false);
				totalScore += questionScore[questionIndex];
				if (questionIndex == 14) {
					gameView.showMessage("Ban da tra loi het cac cau hoi");
					choiLai();
				}
				timeCount = 30;
				questionIndex++;
				gameView.updatePrizeColumn(questionIndex);
				gameView.updatePlayer(player.getUsername(), totalScore);
			} else {
				soundManager.playSound("traLoiDung.wav", false);
				gameView.updateButton(trueCase);
				choiLai();
			}
		}
	}

	private void choiLai() {
		tamDung = true;
		soundManager.stopSound();
		int option = JOptionPane.showConfirmDialog(gameView,
				"Ban da thua cuoc, co muon choi lai ko?", "Xac nhan",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (option == JOptionPane.YES_OPTION) {
			reset();
		} else {
			openConnection();
			player.setScore(totalScore);
			String msg = "/s/" + player.getUsername() + "|" + totalScore;
			sendData(msg);
			closeConnection();
			boolean isAdmin;
			if (player.getUsername().equals("admin")) {
				isAdmin = true;
			} else {
				isAdmin = false;
			}
			dangChoi=false;
			soundManager.stopSound();
			new ClientControl(new ClientView(isAdmin), player);
			gameView.closeWindow();
		}
	}

	private void reset() {
		gameView.reset();
		soundManager.playSound("nhacNen.wav", true);
		questionIndex = 0;
		timeCount = 30;
		tamDung = false;
		dangChoi = true;
		gameView.updatePrizeColumn(questionIndex);
		openConnection();
		String msg = "/h/";
		sendData(msg);
		listQuestions = receiveListQuestion();
		gameView.updateQuestion(listQuestions.get(questionIndex));
		closeConnection();
	}

	@Override
	public void run() {
		while (dangChoi) {
			if (!tamDung) {
				gameView.updateTimer(timeCount);
				timeCount--;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.out.println("Loi thread time");
				}
				if (timeCount <= 0) {
					choiLai();
				}
				if (helpButton) {
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					helpButton = false;
					soundManager.playSound("nhacNen.wav", true);
				}
				if (nextQuestion) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					gameView.updateQuestion(listQuestions.get(questionIndex));
					gameView.resetAnswerButton();
					nextQuestion = false;
					soundManager.playSound("nhacNen.wav", true);
				}
			}
		}
	}

	@Override
	protected String receiveData() {
		return null;
	}

}
