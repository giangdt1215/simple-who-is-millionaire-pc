package com.nhom6.altp.server;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ServerView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JButton btnThoat;

	public void showMessage(String string) {
		System.out.println(string);
	}

	public ServerView() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		setTitle("Server");
		setLocationRelativeTo(null);
		setSize(250, 150);
		setResizable(false);
		
		btnThoat = new JButton("Tat server");
		btnThoat.setBounds(100, 50, 100, 20);

		JLabel lblTrangThai = new JLabel("Server dang chay...");
		lblTrangThai.setBounds(50, 20, 150, 20);
		lblTrangThai.setForeground(Color.BLACK);

		JPanel container = new JPanel();
		container.setSize(250, 150);
		container.setLayout(null);
		container.setBackground(Color.WHITE);
		container.add(lblTrangThai);
		container.add(btnThoat);
		
		setContentPane(container);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}

	public void addThoatListener(ActionListener al) {
		btnThoat.addActionListener(al);
	}

	public void close() {
		this.dispose();
	}
}
