package com.nhom6.altp.game;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.nhom6.altp.model.Question;

public class GameView extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH_GUI = 1000;
	private static final int HEIGHT_GUI = 600;
	private static final Font QUESTION_FONT = new Font("Tahoma", Font.PLAIN, 16);
	private static final Font ANSWER_FONT = new Font("Tahoma", Font.PLAIN, 16);
	private static final Font PRIZE_FONT = new Font("Tahoma", Font.BOLD, 16);

	private JPanel gamePanel;
	private JLabel lblPlayer;
	private JLabel lblPlayerTotalScore;
	private JLabel lblQuestion;
	private JButtonGame btnExit;
	private JButtonGame btnCaseA;
	private JButtonGame btnCaseB;
	private JButtonGame btnCaseC;
	private JButtonGame btnCaseD;
	private JButtonGame btn5050;
	private JButtonGame btnDoiCauHoi;

	/**
	 * lblPrize[0] - prize 1 lblPrize[1] - prize 2,...
	 */
	private JLabel lblPrize[];

	private JLabel lblCaseA;
	private JLabel lblCaseB;
	private JLabel lblCaseC;
	private JLabel lblCaseD;
	private JLabel lblTimer;
	private JLabel lblTime;

	private ImageIcon[] timerIcon;
	private ImageIcon dung, sai, thuong, normal5050, normalDoiCH;

	public GameView() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		initGUI();
		initIcon();
		setTitle("Game");
		setSize(WIDTH_GUI, HEIGHT_GUI);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);

	}

	private void initGUI() {
		gamePanel = new JPanel();
		setLayout(new CardLayout());
		gamePanel.setBounds(0, 0, WIDTH_GUI, HEIGHT_GUI);
		gamePanel.setSize(WIDTH_GUI, HEIGHT_GUI);
		gamePanel.setBackground(Color.WHITE);
		gamePanel.setLayout(null);
		add(gamePanel);

		initComponents();
		this.pack();
	}

	private void initComponents() {
		try {
			JLabel background = new JLabel(new ImageIcon(ImageIO.read(this
					.getClass().getResource("/resource/background.jpg"))));
			background.setBounds(0, 0, WIDTH_GUI, HEIGHT_GUI);

			JLabel lblLogo = new JLabel(new ImageIcon(ImageIO.read(this
					.getClass().getResource("/resource/logo.png"))));
			lblLogo.setBounds(WIDTH_GUI / 2 - 90, HEIGHT_GUI / 2 - 300, 300,
					235);
			JLabel lblPrizeBackground = new JLabel(new ImageIcon(
					ImageIO.read(this.getClass().getResource(
							"/resource/prizeBackground.png"))));
			lblPrizeBackground.setBounds(0, 0, 180, 600);
			lblPlayer = new JLabel("Username:");
			lblPlayer.setBounds(200, 25, 500, 20);
			lblPlayer.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblPlayer.setForeground(Color.WHITE);
			lblPlayerTotalScore = new JLabel("Total score:");
			lblPlayerTotalScore.setBounds(200, 50, 500, 20);
			lblPlayerTotalScore.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblPlayerTotalScore.setForeground(Color.WHITE);
			lblTime = new JLabel("30", SwingConstants.CENTER);
			lblTime.setBounds(533, 435, 58, 58);
			lblTime.setForeground(Color.WHITE);
			lblTime.setFont(ANSWER_FONT);
			lblTimer = new JLabel(new ImageIcon(ImageIO.read(this.getClass()
					.getResource("/resource/timer/timer-30.png"))));
			lblTimer.setBounds(533, 435, 58, 58);
			initLabelCase();
			initLabelPrize();
			initButton();

			addComps();
			gamePanel.add(lblLogo);
			gamePanel.add(lblPrizeBackground);
			gamePanel.add(lblPlayer);
			gamePanel.add(lblPlayerTotalScore);
			gamePanel.add(lblTime);
			gamePanel.add(lblTimer);
			gamePanel.add(background);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initLabelCase() {
		lblQuestion = new JLabel("Cau hoi", SwingConstants.CENTER);
		lblQuestion.setVerticalAlignment(SwingConstants.CENTER);
		lblQuestion.setFont(QUESTION_FONT);
		lblQuestion.setForeground(Color.WHITE);
		lblQuestion.setBounds(WIDTH_GUI / 2 - 280, HEIGHT_GUI / 2 - 100, 700,
				200);
		lblCaseA = new JLabel("A: ", SwingConstants.CENTER);
		lblCaseA.setBounds(200, 403, 334, 41);
		lblCaseA.setFont(ANSWER_FONT);
		lblCaseA.setForeground(Color.WHITE);
		lblCaseB = new JLabel("B: ", SwingConstants.CENTER);
		lblCaseB.setBounds(200, 484, 334, 41);
		lblCaseB.setFont(ANSWER_FONT);
		lblCaseB.setForeground(Color.WHITE);
		lblCaseC = new JLabel("C: ", SwingConstants.CENTER);
		lblCaseC.setBounds(586, 403, 334, 41);
		lblCaseC.setFont(ANSWER_FONT);
		lblCaseC.setForeground(Color.WHITE);
		lblCaseD = new JLabel("D: ", SwingConstants.CENTER);
		lblCaseD.setBounds(586, 484, 334, 41);
		lblCaseD.setFont(ANSWER_FONT);
		lblCaseD.setForeground(Color.WHITE);
	}

	private void initLabelPrize() {
		int x = 20, y = 445, width = 170, height = 30;
		lblPrize = new JLabel[15];
		lblPrize[0] = new JLabel();
		lblPrize[0].setFont(PRIZE_FONT);
		lblPrize[0].setForeground(Color.WHITE);
		lblPrize[0].setBounds(x, y, width, height);
		for (int i = 1; i < lblPrize.length; i++) {
			y -= height;
			lblPrize[i] = new JLabel();
			lblPrize[i].setFont(PRIZE_FONT);
			lblPrize[i].setBounds(x, y, width, height);
			if (i == 0 || i == 4 || i == 9 || i == 14) {
				lblPrize[i].setForeground(Color.WHITE);
			} else {
				lblPrize[i].setForeground(Color.YELLOW);
			}
		}

		lblPrize[14].setText("15  $2,000,000");
		lblPrize[13].setText("14  $1,500,000");
		lblPrize[12].setText("13  $1,250,000");
		lblPrize[11].setText("12  $1,000,000");
		lblPrize[10].setText("11  $500,000");
		lblPrize[9].setText("10  $250,000");
		lblPrize[8].setText(" 9  $150,000");
		lblPrize[7].setText(" 8  $75,000");
		lblPrize[6].setText(" 7  $50,000");
		lblPrize[5].setText(" 6  $20,000");
		lblPrize[4].setText(" 5  $10,000");
		lblPrize[3].setText(" 4  $5,000");
		lblPrize[2].setText(" 3  $2,000");
		lblPrize[1].setText(" 2  $1,000");
		lblPrize[0].setText(" 1  $500");
	}

	private void initButton() {
		btnCaseA = new JButtonGame("/resource/button-answer.png",
				"/resource/button-answer-over.png", 200, 403, 334, 41);
		btnCaseB = new JButtonGame("/resource/button-answer.png",
				"/resource/button-answer-over.png", 200, 484, 334, 41);
		btnCaseC = new JButtonGame("/resource/button-answer.png",
				"/resource/button-answer-over.png", 586, 403, 334, 41);
		btnCaseD = new JButtonGame("/resource/button-answer.png",
				"/resource/button-answer-over.png", 586, 484, 334, 41);
		btnExit = new JButtonGame("/resource/button-exit.png",
				"/resource/button-exit-over.png", 50, 500, 83, 52);
		btn5050 = new JButtonGame("/resource/button-5050.png",
				"/resource/button-5050-over.png", 900, 245, 83, 52);
		btnDoiCauHoi = new JButtonGame("/resource/button-change.png",
				"/resource/button-change-over.png", 900, 300, 83, 52);
	}

	private void addComps() {
		gamePanel.add(lblCaseA);
		gamePanel.add(lblCaseB);
		gamePanel.add(lblCaseC);
		gamePanel.add(lblCaseD);
		for (int i = 0; i < lblPrize.length; i++) {
			gamePanel.add(lblPrize[i]);
		}
		gamePanel.add(btnCaseA);
		gamePanel.add(btnCaseB);
		gamePanel.add(btnCaseC);
		gamePanel.add(btnCaseD);
		gamePanel.add(btnExit);
		gamePanel.add(btn5050);
		gamePanel.add(btnDoiCauHoi);
		gamePanel.add(lblQuestion);
	}

	private void initIcon() {
		try {
			timerIcon = new ImageIcon[31];
			for (int i = 0; i < timerIcon.length; i++) {
				timerIcon[i] = new ImageIcon(ImageIO.read(this.getClass()
						.getResource("/resource/timer/timer-" + i + ".png")));
			}
			dung = new ImageIcon(ImageIO.read(this.getClass().getResource(
					"/resource/button-answer-dung.png")));
			sai = new ImageIcon(ImageIO.read(this.getClass().getResource(
					"/resource/button-answer-sai.png")));
			thuong = new ImageIcon(ImageIO.read(this.getClass().getResource(
					"/resource/button-answer.png")));
			normal5050 = new ImageIcon(ImageIO.read(this.getClass()
					.getResource("/resource/button-5050.png")));
			normalDoiCH = new ImageIcon(ImageIO.read(this.getClass()
					.getResource("/resource/button-change.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateTimer(int timeCount) {
		lblTime.setText(String.valueOf(timeCount));
		lblTimer.setIcon(timerIcon[timeCount]);
	}

	public void addBtnCaseAListener(ActionListener al) {
		btnCaseA.addActionListener(al);
	}

	public void addBtnCaseBListener(ActionListener al) {
		btnCaseB.addActionListener(al);
	}

	public void addBtnCaseCListener(ActionListener al) {
		btnCaseC.addActionListener(al);
	}

	public void addBtnCaseDListener(ActionListener al) {
		btnCaseD.addActionListener(al);
	}

	public void addBtnExitListener(ActionListener al) {
		btnExit.addActionListener(al);
	}

	public void addBtn5050Listener(ActionListener al) {
		btn5050.addActionListener(al);
	}

	public void addBtnDoiCauHoiListener(ActionListener al) {
		btnDoiCauHoi.addActionListener(al);
	}

	public void updatePlayer(String user, int score) {
		lblPlayer.setText("User: " + user);
		lblPlayerTotalScore.setText(String.valueOf("Total score: " + score));
	}

	public void updatePrizeColumn(int questionIndex) {
		lblPrize[questionIndex].setForeground(Color.GREEN);
	}

	public void btn5050Used(int sai1, int sai2) {
		btn5050.setEnabled(false);
		if (sai1 == 1) {
			lblCaseA.setText("");
			btnCaseA.setEnabled(false);
		} else if (sai1 == 2) {
			lblCaseB.setText("");
			btnCaseB.setEnabled(false);
		} else if (sai1 == 3) {
			lblCaseC.setText("");
			btnCaseC.setEnabled(false);
		} else if (sai1 == 4) {
			lblCaseD.setText("");
			btnCaseD.setEnabled(false);
		}
		if (sai2 == 1) {
			lblCaseA.setText("");
			btnCaseA.setEnabled(false);
		} else if (sai2 == 2) {
			lblCaseB.setText("");
			btnCaseB.setEnabled(false);
		} else if (sai2 == 3) {
			lblCaseC.setText("");
			btnCaseC.setEnabled(false);
		} else if (sai2 == 4) {
			lblCaseD.setText("");
			btnCaseD.setEnabled(false);
		}
	}

	public void btnDoiCauHoiUsed() {
		btnDoiCauHoi.setEnabled(false);
	}

	public void updateQuestion(Question ques) {
		lblQuestion.setText("<html></div>" + ques.getQuestion()
				+ "</div></html>");
		lblCaseA.setText("A: " + ques.getCaseA());
		lblCaseB.setText("B: " + ques.getCaseB());
		lblCaseC.setText("C: " + ques.getCaseC());
		lblCaseD.setText("D: " + ques.getCaseD());
		btnCaseA.setEnabled(true);
		btnCaseA.setIcon(thuong);
		btnCaseB.setEnabled(true);
		btnCaseB.setIcon(thuong);
		btnCaseC.setEnabled(true);
		btnCaseC.setIcon(thuong);
		btnCaseD.setEnabled(true);
		btnCaseD.setIcon(thuong);
	}

	public void updateButton(int trueCase) {
		if (trueCase == 1) {
			btnCaseA.setIcon(dung);
			btnCaseB.setIcon(sai);
			btnCaseC.setIcon(sai);
			btnCaseD.setIcon(sai);
		} else if (trueCase == 2) {
			btnCaseB.setIcon(dung);
			btnCaseA.setIcon(sai);
			btnCaseC.setIcon(sai);
			btnCaseD.setIcon(sai);
		} else if (trueCase == 3) {
			btnCaseC.setIcon(dung);
			btnCaseB.setIcon(sai);
			btnCaseA.setIcon(sai);
			btnCaseD.setIcon(sai);
		} else {
			btnCaseA.setIcon(sai);
			btnCaseB.setIcon(sai);
			btnCaseC.setIcon(sai);
			btnCaseD.setIcon(dung);
		}
	}

	public void closeWindow() {
		dispose();
	}

	public void setWindowAdapter(WindowAdapter wd) {
		this.addWindowListener(wd);
	}

	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}

	public void reset() {
		btnCaseA.setEnabled(true);
		btnCaseA.setIcon(thuong);
		btnCaseB.setEnabled(true);
		btnCaseB.setIcon(thuong);
		btnCaseC.setEnabled(true);
		btnCaseC.setIcon(thuong);
		btnCaseD.setEnabled(true);
		btnCaseD.setIcon(thuong);
		btn5050.setEnabled(true);
		btn5050.setIcon(normal5050);
		btnDoiCauHoi.setEnabled(true);
		btnDoiCauHoi.setIcon(normalDoiCH);
		btnExit.setEnabled(true);
	}
	
	public void resetAnswerButton(){
		btnCaseA.setEnabled(true);
		btnCaseA.setIcon(thuong);
		btnCaseB.setEnabled(true);
		btnCaseB.setIcon(thuong);
		btnCaseC.setEnabled(true);
		btnCaseC.setIcon(thuong);
		btnCaseD.setEnabled(true);
		btnCaseD.setIcon(thuong);
	}
}
