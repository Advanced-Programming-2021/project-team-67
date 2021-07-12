package controller.scoreboardmenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import model.*;

public class Scoreboard {

    private Scoreboard() {
    }

    private static Scoreboard instance;

    public static Scoreboard getInstance() {
        if (instance == null)
            instance = new Scoreboard();
        return instance;
    }

    public void showScoreboard() {
        int counter = 1;
        int index = 0;
        long previousScore = -1;
        StringBuilder output = new StringBuilder();
        ArrayList<Player> allUsers = Player.getAllPlayers();
//        allUsers.sort(Player::compareTo);
        ArrayList<String> playersName = new ArrayList<>();
        for (Player player : allUsers) {
            playersName.add(player.getNickname());
        }
//        Collections.sort(playersName);
        Collections.sort(playersName, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        ArrayList<Player> allUsersForScoreboard = new ArrayList<>();
        for (String name : playersName) {
            allUsersForScoreboard.add(Player.getPlayerByNickname(name));
        }
        for (Player player : allUsersForScoreboard) {


            if (player.getScore() != previousScore) {
                index += counter;
                counter = 1;
            } else counter++;
            output.append(index).append(". ").append(player.getNickname()).append(": ").append(player.getScore()).append("\n");
            previousScore = player.getScore();

        }
        ScoreboardOutput.getInstance().showMessage(output.toString());
    }

}
