package com.nhom6.altp.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

public class DBUtils {

	private static Connection connection;

	public static void getDBConnection(String dbName, String user, String pass) {

		String dbUrl = "jdbc:mysql://localhost/" + dbName;
		String dbClass = "com.mysql.jdbc.Driver";

		try {
			Class.forName(dbClass);
			connection = DriverManager.getConnection(dbUrl, user, pass);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}
	}

	public static boolean checkUser(String username, String password) {
		String query = "Select * from account where username='" + username
				+ "' and password='" + password + "'";
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static int getPlayerScore(String username) {
		int score = 0;
		String query = "Select * from account where username='" + username
				+ "'";
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				score = rs.getInt("score");
			}
			return score;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return score;
	}

	public static boolean changePass(String user, String newPass) {
		String query = "Update account set password = '" + newPass
				+ "' where username='" + user + "'";
		try {
			PreparedStatement ps = (PreparedStatement) connection
					.prepareStatement(query);
			return (!ps.execute(query));
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static List<Player> getListPlayer() {
		List<Player> listPlayer = new ArrayList<>();
		String query = "Select * from account order by username";
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				Player player = new Player();
				player.setUsername(rs.getString("username"));
				player.setPassword(rs.getString("password"));
				player.setScore(rs.getInt("score"));
				listPlayer.add(player);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listPlayer;
	}

	public static List<Question> getListQuestions() {
		List<Question> listQuestions = new ArrayList<>();
		String query = "(SELECT * FROM Question ORDER BY RAND() LIMIT 0,15) order by level";
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				int id = rs.getInt("_id");
				int level = rs.getInt("level");
				int trueCase = rs.getInt("truecase");
				String cauHoi = rs.getString("question");
				String caseA = rs.getString("casea");
				String caseB = rs.getString("caseb");
				String caseC = rs.getString("casec");
				String caseD = rs.getString("cased");
				Question question = new Question(id, level, trueCase, cauHoi,
						caseA, caseB, caseC, caseD);
				listQuestions.add(question);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listQuestions;
	}

	public static Question doiCauHoi(int id, int level) {
		String query = "SELECT * FROM Question where _id <> " + id
				+ " and level = " + level + " ORDER BY RAND() LIMIT 0,1";
		Question question = null;
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				int idQues = rs.getInt("_id");
				int levelQues = rs.getInt("level");
				int trueCase = rs.getInt("truecase");
				String cauHoi = rs.getString("question");
				String caseA = rs.getString("casea");
				String caseB = rs.getString("caseb");
				String caseC = rs.getString("casec");
				String caseD = rs.getString("cased");
				question = new Question(idQues, levelQues, trueCase, cauHoi,
						caseA, caseB, caseC, caseD);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return question;
	}

	public static boolean addUser(String user, String pass) {
		String query = "Insert into account (username, password, score)"
				+ " values('" + user + "','" + pass + "', 0)";
		try {
			PreparedStatement ps = (PreparedStatement) connection
					.prepareStatement(query);
			return (!ps.execute());
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean deleteUser(String user) {
		if (user.equals("admin")) {
			return false;
		}
		String query = "Delete from account where username='" + user + "'";
		try {
			PreparedStatement ps = (PreparedStatement) connection
					.prepareStatement(query);
			return (!ps.execute());
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean updateScore(String user, int score) {
		String query = "Update account set account.score = " + score
				+ " where username='" + user + "'";
		try {
			PreparedStatement ps = (PreparedStatement) connection
					.prepareStatement(query);
			return (!ps.execute());
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
