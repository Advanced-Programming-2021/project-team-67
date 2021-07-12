package controller.deckmenu;

import model.*;
import model.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class DeckMenuController {

    private static DeckMenuController instance = null;

    private DeckMenuController() {
    }

    public static DeckMenuController getInstance() {
        return Objects.requireNonNullElseGet(instance, () -> (instance = new DeckMenuController()));
    }

    public void createDeck(String name, Player owner) {
        if (!DeckMenuTools.isDeckNameUnique(name))
            return;

        new Deck(name, owner , true , true);
        DeckMenuOutput.getInstance().showMessage("deck created successfully!");
    }

    public void deleteDeck(String name) {
        if (!DeckMenuTools.doesDeckExist(name)) {return;}
        // if (DeckMenuTools.isDeckNameUnique(name)) return;
        Deck deck = DeckMenuDatabase.getInstance().getDeckByName(name);
        Player player = deck.getOwner();
        for (Card card : deck.getMainCards()) {
            player.addCardToBoughtCards(card);
        }
        for (Card card : deck.getSideCards()) {
            player.addCardToBoughtCards(card);
        }
        DeckMenuDatabase.removeDeck(name);
        DeckMenuOutput.getInstance().showMessage("deck deleted successfully!");

    }


    public void setActiveDeck(String name, Player player) {
        Deck deck = null;
        boolean isPermitted = DeckMenuTools.doesDeckExist(name)
                && DeckMenuTools.doesDeckBelongToPlayer(deck = DeckMenuDatabase.getInstance().getDeckByName(name), player);
        if (isPermitted) {
            player.activateADeck(deck);

            DeckMenuOutput.getInstance().showMessage("deck activated successfully!");
        }

    }

    public void addCardToDeck(String cardName, String deckName, Player player, boolean isMain) {
        Card card = null;
        Deck deck = null;
        boolean isPermitted = //DeckMenuTools.doesCardExist(cardName) &&
                DeckMenuTools.doesDeckExist(deckName)
                && DeckMenuTools.doesDeckBelongToPlayer(deck = DeckMenuDatabase.getInstance().getDeckByName(deckName), player)
                && ((isMain) ? DeckMenuTools.doesDeckHaveSpace(deck) : DeckMenuTools.doesSideDeckHaveSpace(deck));
//        if (!DeckMenuTools.doesCardExist(cardName)) {}
//        else {}
        if ((card = DeckMenuDatabase.getInstance().getCardByName(cardName, player)) == null) DeckMenuOutput.getInstance().showMessage("card with name " + cardName + " does not exist");
        else {
            isPermitted = isPermitted
                    && DeckMenuTools.isNumberOfCardsInDeckLessThanFour(deck, card)
                    && DeckMenuTools.doesPlayerHaveEnoughCards(card , player);
            if (isPermitted) {
                deck.addCard(card, isMain);
                player.removeCardFromBoughtCards(card);
                DeckMenuOutput.getInstance().showMessage("card added to deck successfully!");
            }
        }
    }

    public void removeCardFromDeck(String cardName, String deckName, Player player, boolean isMain) {
        Card card;
        Deck deck = null;
        boolean isPermitted = DeckMenuTools.doesCardExist(cardName)
                && DeckMenuTools.doesDeckExist(deckName)
                && DeckMenuTools.doesDeckBelongToPlayer(deck = DeckMenuDatabase.getInstance().getDeckByName(deckName), player)
                && DeckMenuTools.doesDeckHasCard(cardName, deck, card = DeckMenuDatabase.getInstance().getCardByName(cardName, player, deck, isMain), player);
        if (isPermitted) {
            card = DeckMenuDatabase.getInstance().getCardByName(cardName, player, deck, isMain);
//            deck = DeckMenuDatabase.getInstance().getDeckByName(deckName);
            if (card == null) {
                if (isMain) DeckMenuOutput.getInstance().showMessage("card with name " + cardName + " does not exist in main deck");
                else DeckMenuOutput.getInstance().showMessage("card with name " + cardName + " does not exist in side deck");
            } else {
                player.addCardToBoughtCards(card);
                deck.moveCardTo(card, isMain , true);
                DeckMenuOutput.getInstance().showMessage("card removed from deck successfully!");
            }
        }
    }

    public void showAllDecks(Player player) {
        System.out.print("Decks:\n" + "Active deck:\n");
        for (Deck deck : player.getAllDeck()){
            if (deck.getActive()) {
                deck.toString(deck);
            }
        }
        System.out.print("Other Decks:\n");
        ArrayList<String> sideCardsName = new ArrayList<>();
        ArrayList<Deck> allDeck = new ArrayList<>();
        for (Deck deck : player.getAllDeck()) {
            sideCardsName.add(deck.getName());
        }
        Collections.sort(sideCardsName);
        for (String name : sideCardsName) {
            allDeck.add(player.getDeckByName(name, player));
        }
        for (Deck deck : allDeck) {
            if (!deck.getActive()) {
                deck.toString(deck);
            }
        }
    }

    public void showDeck(String name, Player player, boolean isMain) {
        if (!DeckMenuTools.doesDeckExist(name))
            return;
        Deck deck = DeckMenuDatabase.getInstance().getDeckByName(name);
        if (!DeckMenuTools.doesDeckBelongToPlayer(deck, player))
            return;
        DeckMenuOutput.getInstance().showMessage(deck.toString(isMain, deck));

    }

    public void showCards(Player loggedInPlayer) {
        ArrayList<String> cardsName = new ArrayList<>();
        for (Deck deck : loggedInPlayer.getAllDeck()){
            for (Card card : deck.getMainCards()) {
                cardsName.add(card.getName());
            }
            for (Card card : deck.getSideCards()) {
                cardsName.add(card.getName());
            }
        }
        for (Card card : loggedInPlayer.getBoughtCards()) {
            cardsName.add(card.getName());
        }
        Collections.sort(cardsName);
        StringBuilder output = new StringBuilder();
        for (String cardName : cardsName) {
            Card card = Card.getCardByName(cardName);
            output.append(cardName).append(":").append(card.getDescription()).append("\n");
        }
        System.out.print(output);
    }

}
