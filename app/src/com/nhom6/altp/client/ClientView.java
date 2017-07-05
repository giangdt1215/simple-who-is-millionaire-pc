package com.nhom6.altp.client;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.nhom6.altp.model.Player;

public class ClientView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblIdenNum;
	private JLabel lblUsername;
	private JTable tblAccount;
	private JButton btnXoaAccount;
	private JButton btnThemAccount;
	private JButton btnChangePass;

	private static final Font FONT = new Font("Tahoma", Font.PLAIN, 12);
	private JLabel lblTotalScore;
	private JButton btnPlayGame;

	public ClientView(boolean isAdmin) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// addWindowListener(windowAdapter);
		setBounds(100, 100, 545, 394);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblIdenNum = new JLabel("ID: ");
		lblIdenNum.setFont(FONT);
		lblIdenNum.setBounds(201, 21, 110, 14);
		contentPane.add(lblIdenNum);

		lblUsername = new JLabel("Username: ");
		lblUsername.setFont(FONT);
		lblUsername.setBounds(201, 46, 165, 14);
		contentPane.add(lblUsername);

		btnChangePass = new JButton("Đổi password");
		btnChangePass.setFont(FONT);
		btnChangePass.setBounds(128, 125, 119, 23);
		contentPane.add(btnChangePass);

		btnThemAccount = new JButton("Thêm account");
		btnThemAccount.setFont(FONT);
		btnThemAccount.setBounds(270, 125, 132, 23);
		contentPane.add(btnThemAccount);

		btnXoaAccount = new JButton("Xóa account");
		btnXoaAccount.setFont(FONT);
		btnXoaAccount.setBounds(426, 125, 103, 23);
		contentPane.add(btnXoaAccount);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(28, 167, 472, 166);
		contentPane.add(scrollPane);

		tblAccount = new JTable(new DefaultTableModel(new Object[][] {},
				new String[] { "Account", "Score" }));
		scrollPane.setViewportView(tblAccount);

		lblTotalScore = new JLabel("Total score: ");
		lblTotalScore.setFont(FONT);
		lblTotalScore.setBounds(201, 71, 250, 14);
		contentPane.add(lblTotalScore);

		btnPlayGame = new JButton("Chơi game");
		btnPlayGame.setFont(FONT);
		btnPlayGame.setBounds(10, 126, 108, 23);
		contentPane.add(btnPlayGame);

		if (!isAdmin) {
			btnThemAccount.setEnabled(false);
			btnXoaAccount.setEnabled(false);
			tblAccount.setEnabled(false);
			setSize(545, 185);
		}

		setTitle("Player Window");
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void setID(int id) {
		lblIdenNum.setText("ID: " + id);
	}

	public void setUsername(String username) {
		lblUsername.setText("Username: " + username);
	}

	public void setScore(int score) {
		lblTotalScore.setText("Score: " + score);
	}

	public void setWindowAdapter(WindowAdapter wa) {
		this.addWindowListener(wa);
	}

	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}

	public void showTable(List<Player> list) {
		String[] tenCot = { "Account", "Score" };
		DefaultTableModel mode = new DefaultTableModel(null, tenCot);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Vector<String> v = new Vector<>();
				v.add(list.get(i).getUsername());
				v.add(String.valueOf(list.get(i).getScore()));
				mode.addRow(v);
			}
		}
		tblAccount.setModel(mode);
	}

	public String getAccountName() {
		TableModel model = tblAccount.getModel();
		int rowSelected = tblAccount.getSelectedRow();
		String name = "";
		if (rowSelected > -1) {
			name = (String) model.getValueAt(rowSelected, 0);
		}
		return name;
	}

	public void addChangePassListener(ActionListener al) {
		btnChangePass.addActionListener(al);
	}

	public void addDeleteAccListener(ActionListener al) {
		btnXoaAccount.addActionListener(al);
	}

	public void addAddAccListener(ActionListener al) {
		btnThemAccount.addActionListener(al);
	}
	
	public void addPlayGameListener(ActionListener al){
		btnPlayGame.addActionListener(al);
	}

	public void closeLoginWindow() {
		dispose();
	}
}
