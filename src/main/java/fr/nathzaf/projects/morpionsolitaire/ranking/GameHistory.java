package fr.nathzaf.projects.morpionsolitaire.ranking;

import fr.nathzaf.projects.morpionsolitaire.game.Mode;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameHistory {

    public static final String DB_URL = "jdbc:mysql://ffdg7c1q2dhgstsg:squtlis0ir6m0oyx" +
            "@nuepp3ddzwtnggom.chr7pe7iynqr.eu-west-1.rds.amazonaws.com:3306/vyhmwc8ukf983jik";

    private final int id;

    private final String playerName;

    private final String mode;

    private final String autoSolver;

    private final int score;

    public GameHistory(int id, String playerName, String mode, String autoSolver, int score) {
        this.id = id;
        this.playerName = playerName;
        this.mode = mode;
        this.autoSolver = autoSolver;
        this.score = score;
    }


    public static List<GameHistory> getAllGameHistoryByMode(Mode mode) {
        List<GameHistory> gameHistoryList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement();
            ResultSet resultSet;

            resultSet = statement.executeQuery("SELECT * FROM GAME_HISTORY WHERE mode = '" + mode.getId() + "'");

            while (resultSet.next()) {
                gameHistoryList.add(new GameHistory(resultSet.getInt("id"), resultSet.getString("playerName"),
                        resultSet.getString("mode"), resultSet.getString("autoSolver"), resultSet.getInt("score")));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gameHistoryList;
    }

    public static void addNewGameHistory(String playerNameInput, String mode, String autoSolver, int scoreInput) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO GAME_HISTORY (playerName, mode, autoSolver, score)" +
                    "VALUES ('" + playerNameInput + "', '" + mode + "', '" + autoSolver + "', " + scoreInput + ")");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public String getMode() {
        return mode;
    }

    public String getAutoSolver() {
        return autoSolver;
    }

    @Override
    public String toString() {
        return "GameHistory{" +
                "id=" + id +
                ", playerName='" + playerName + '\'' +
                ", mode='" + mode + '\'' +
                ", autoSolver='" + autoSolver + '\'' +
                ", score=" + score +
                '}';
    }
}
