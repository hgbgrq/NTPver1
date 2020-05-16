package com.example.ntpver1.login.login;

import com.example.ntpver1.item.Card;

import java.util.ArrayList;

public class User {

    public User(String userEmail, String userName, ArrayList<Card> cards) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.cards = cards;
    }

    String userEmail;
    String userName;
    ArrayList<Card> cards;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
}
