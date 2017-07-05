package com.nhom6.altp.login;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class LoginView extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH_LOGIN_VIEW = 450;
	private static final int HEIGHT_LOGIN_VIEW = 200;

	private static final Font FONT_TEXT = new Font("Tahoma", Font.PLAIN, 14);

	private JPanel container;

	private JTextField txtUsername;
	private JPasswordField txtPassword;

	private JButton btnLogin;

	private JLabel lblUsername;
	private JLabel lblPassword;

	public LoginView() {
		initGUI();
		initPanel();
		addPanel();
	}

	private void initGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		setTitle("Login");
		setLayout(null);
		setResizable(false);
		setSize(WIDTH_LOGIN_VIEW, HEIGHT_LOGIN_VIEW);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void initPanel() {
		container = new JPanel();
		container.setSize(WIDTH_LOGIN_VIEW, HEIGHT_LOGIN_VIEW);
		container.setLayout(null);
		container.setBackground(Color.WHITE);
		lblUsername = new JLabel("Username:");
		lblPassword = new JLabel("Password:");
		txtUsername = new JTextField();
		txtPassword = new JPasswordField();
		btnLogin = new JButton("Login");

		FontMetrics metrics = getFontMetrics(FONT_TEXT);
		int x = WIDTH_LOGIN_VIEW / 5, y = HEIGHT_LOGIN_VIEW / 5;
		int widthText = metrics.stringWidth(lblUsername.getText());

		lblUsername.setBounds(x, y, widthText, metrics.getHeight() + 5);
		txtUsername.setBounds(x + widthText, y, 200, metrics.getHeight() + 5);

		y += metrics.getHeight() + 10;
		widthText = metrics.stringWidth(lblPassword.getText());
		lblPassword.setBounds(x, y, x + widthText, metrics.getHeight() + 5);
		txtPassword.setBounds(x + widthText + 3, y, 200,
				metrics.getHeight() + 5);
		txtPassword.setEchoChar('*');

		y += metrics.getHeight() + 20;
		btnLogin.setFont(FONT_TEXT);
		btnLogin.setBounds(WIDTH_LOGIN_VIEW / 2 - 50, y, 100, 30);
		btnLogin.addActionListener(this);
	}

	private void addPanel() {
		container.add(lblUsername);
		container.add(txtUsername);
		container.add(lblPassword);
		container.add(txtPassword);
		container.add(btnLogin);
		add(container);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	public String getPlayerInformation() {
		String inform = "/l/" + txtUsername.getText() + "|"
				+ String.valueOf(txtPassword.getPassword());
		return inform;
	}

	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}

	public void addLoginListener(ActionListener al) {
		btnLogin.addActionListener(al);
	}

	public void closeLoginWindow() {
		dispose();
	}
}
