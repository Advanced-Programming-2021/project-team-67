package model;

import controller.deckmenu.DeckMenuDatabase;
import model.cards.Card;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    public ArrayList<Card> mainCards = new ArrayList<>();
    public ArrayList<Card> sideCards = new ArrayList<>();
    Player owner;
    String name;
    DeckType type;
    Boolean isActive = false;
    Boolean IsValid;

    public Deck(String name, Player owner, boolean hasSideDeck, boolean shouldBeSaved) {
        this.name = name;
        if (shouldBeSaved)
            DeckMenuDatabase.allDecks.add(this);
        if (!hasSideDeck)
            sideCards = null;
        this.owner = owner;
    }

    public Deck(String name, Player owner) {
        this.name = name;
        sideCards = null;
        this.owner = owner;
    }

    public Deck(String name) {
        this.name = name;
        sideCards = null;
    }
    public Deck() {
        sideCards = null;
    }

    public void updateOwnerDecks() {
        String deckType = (name.length() > 16) ? name.substring(name.length() - 16) : "";
        if (deckType.equals(".purchased-cards"))
            owner.setAllPlayerCard(this);
        else {
            owner.getAllDeck().add(this);
            if (this.isActive)
                owner.setActiveDeck(this);
        }
    }

    public ArrayList<Card> getMainCards() {
        if (mainCards == null)
            return (mainCards = new ArrayList<>());
        return mainCards;
    }

    public void setMainCards(ArrayList<Card> mainCards) {
        this.mainCards = mainCards;
    }

    public ArrayList<Card> getSideCards() {
        return sideCards;
    }

    public void setSideCards(ArrayList<Card> sideCards) {
        this.sideCards = sideCards;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
    public boolean getActive() {
        return  isActive;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActivation(Boolean active) {
        isActive = active;
    }

    public void setValid(Boolean valid) {
        IsValid = valid;
    }

    public void setType(DeckType type) {
        this.type = type;
    }

    public void addCard(Card card, boolean shouldBeAddedToMain) {
        if (shouldBeAddedToMain)
            mainCards.add(card);
        else
            sideCards.add(card);
        if (card != null)
            card.setCurrentDeck(this);
    }

    public void addCard(Card card) {
        mainCards.add(card);
        if (card != null && !name.equals("selected collected deck"))
            card.setCurrentDeck(this);
    }

    public void moveCardTo(Card card, boolean isMainForOrigin, boolean isMainForDestination) {
        removeCard(card, isMainForOrigin);
    }

    public void moveCardToForGame(Deck destination, Card card, boolean isMainForOrigin, boolean isMainForDestination) {
        removeCardForGame(card, isMainForOrigin);
        destination.addCard(card, isMainForDestination);
    }

    public boolean hasCard(Card card, boolean isMain) {
        if (isMain) {
            for (Card cardInMain : mainCards)
                if (cardInMain.getName().equals(card.getName()))
                    return true;

        } else
            for (Card cardInSide : sideCards)
                if (cardInSide.getName().equals(card.getName())) {
                    return true;
                }
        return false;
    }

    public void removeCard(Card card, boolean shouldBeRemovedFromMain) {
        if (shouldBeRemovedFromMain) {
            for (Card cardInMain : mainCards)
                if (cardInMain.getName().equals(card.getName())) {
                    mainCards.remove(cardInMain);
                    return;
                }
        } else
            for (Card cardInSide : sideCards)
                if (cardInSide.getName().equals(card.getName())) {
                    sideCards.remove(cardInSide);
                    return;
                }
    }

    public void removeCardForGame(Card card, boolean shouldBeRemovedFromMain) {
        if (shouldBeRemovedFromMain)
            mainCards.remove(card);
        else
            sideCards.remove(card);
    }

    public String isValid(Deck deck){
        if (deck.getNumberOfCardsInMainDeck() >= 40 && deck.getNumberOfCardsInMainDeck() <= 60 && deck.getNumberOfCardsInSideDeck() >= 0 && deck.getNumberOfCardsInSideDeck() <= 15) {
            return "valid";
        }
        return "invalid";
    }
    public int getNumberOfCardsInDeck(Card card) {
        int count = 0;
        for (Card cardInDeck : mainCards) {
            if (cardInDeck.getName().equals(card.getName()))
                count++;
        }
        for (Card cardInDeck : sideCards) {
            if (cardInDeck.getName().equals(card.getName()))
                count++;
        }
        return count;
    }

    public int getNumberOfCardsInMainDeck() {
        return mainCards.size();
    }

    public int getNumberOfCardsInSideDeck() {
        return sideCards.size();
    }

    public void updateCurrentDeck() {
        if (mainCards != null)
            for (Card card : mainCards)
                if (card != null)
                    card.setCurrentDeck(this);
        if (sideCards != null)
            for (Card card : sideCards)
                if (card != null)
                    card.setCurrentDeck(this);
    }
    public String toString(boolean isMain, Deck deck) {
        ArrayList<String> mainCardsName = new ArrayList<>();
        ArrayList<String> sideCardsName = new ArrayList<>();
        for (Card card : mainCards) {
            mainCardsName.add(card.getName());
        }
        for (Card card : sideCards) {
            sideCardsName.add(card.getName());
        }
        Collections.sort(mainCardsName);
        Collections.sort(sideCardsName);
        ArrayList<Card> mainCardss = new ArrayList<>();
        ArrayList<Card> sideCardss = new ArrayList<>();
        for (String cardName : mainCardsName) {
            mainCardss.add(Card.getCardByName(cardName));
        }
        for (String cardName : sideCardsName) {
            sideCardss.add(Card.getCardByName(cardName));
        }
        StringBuilder output = new StringBuilder();
        StringBuilder output2 = new StringBuilder();
        if (isMain) {
            for (Card card : mainCardss) {
                if (model.cards.Card.isMonsterCard(card)) {
                    if (card == null)
                        continue;
                    output.append(card.toString(card));
                } else {
                    if (card == null)
                        continue;
                    output2.append(card.toString(card));
                }
            }
        }
        else
            for (Card card : sideCardss) {
                if (model.cards.Card.isMonsterCard(card)) {
                    if (card == null)
                        continue;
                    output.append(card.toString(card));
                }
                else {
                    if (card == null)
                        continue;
                    output2.append(card.toString(card));
                }
            }
        System.out.println("Deck: "+ deck.name);
            if (isMain){
                System.out.println("Main deck:");
            }
            else {
                System.out.println("Side deck:");
            }
        System.out.println("Monsters:");
        output.append("Spell and Traps:\n").append(output2.toString());
        return output.toString();

    }

    public void toString(Deck deck) {
        System.out.print(deck.getName());
        System.out.print(": ");
        System.out.print("main deck ");
        System.out.print(deck.getNumberOfCardsInMainDeck());
        System.out.print(", ");
        System.out.print("side deck ");
        System.out.print(deck.getNumberOfCardsInSideDeck());
        System.out.print(", ");
        System.out.print(isValid(deck));
        System.out.print("\n");
    }

//    public String toString(Player player, Deck deck){
//        StringBuilder output = new StringBuilder();
//        for (Card card : deck.getMainCards()) {
//                    if (card == null)
//                        continue;
//                    output.append(card.toString(card));
//            }
//        for (Card card : deck.getSideCards()) {
//                if (card == null)
//                    continue;
//                output.append(card.toString(card));
//        }
//        return output.toString();
//
//    }

}