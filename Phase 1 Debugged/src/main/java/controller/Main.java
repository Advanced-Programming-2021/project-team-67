package controller;

import view.LoginMenuView;

public class Main {
    public static void main(String[] args) {
        Database.prepareGame();
        LoginMenuView.loginMenuView();
    }
}