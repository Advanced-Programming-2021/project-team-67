package controller;

import controller.deckmenu.DeckMenuController;
import controller.deckmenu.DeckMenuDatabase;
import controller.deckmenu.DeckMenuOutput;
import controller.deckmenu.DeckMenuTools;
import controller.loginmenu.LoginMenuController;
import model.Deck;
import model.Player;
import model.cards.Card;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class DeckMenuControllerTest extends MenuTest{
    DeckMenuController deckMenuController = DeckMenuController.getInstance();
    DeckMenuDatabase deckMenuDatabase = DeckMenuDatabase.getInstance();
    Player player = Player.getPlayerByUsername("Iman");

    @BeforeAll
    static void createPlayer() {
        String userName = "Iman";
        String password = "123";
        String nickname = "P";
        new Player(userName, password, nickname);
    }

    @Test
    public void creatDeckCorrectTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));


        deckMenuController.createDeck("deck", player);
        Deck deck = deckMenuDatabase.getDeckByName("deck");

        Assertions.assertNotNull(deck);
        Assertions.assertEquals("deck created successfully!\n", outContent.toString());
        Assertions.assertFalse(DeckMenuTools.isDeckNameUnique("deck"));

    }


    @Test
    public void addCardToDeckTest() {
        Player player = Player.getPlayerByUsername("Iman");
        Deck deck = deckMenuDatabase.getDeckByName("deck");
        Card card = Card.getCardByName("Battle OX");


        Assertions.assertNotNull(card);
        Assertions.assertNotNull(player);
        Assertions.assertNotNull(deck);

        player.addCardToBoughtCards(card);
        player.addCardToBoughtCards(card);

        deckMenuController.addCardToDeck("Battle OX", "deck", player, true);
        deckMenuController.addCardToDeck("Battle OX", "deck", player, false);

        Assertions.assertFalse(deck.getMainCards().contains(card));
        Assertions.assertFalse(deck.getSideCards().contains(card));

    }

    @Test
    public void deleteCardFromDeckTest() {
        Player player = Player.getPlayerByUsername("Iman");
        Deck deck = deckMenuDatabase.getDeckByName("deck");
        Card card = Card.getCardByName("Battle OX");

        Assertions.assertNotNull(card);
        Assertions.assertNotNull(player);
        Assertions.assertNotNull(deck);

        deckMenuController.removeCardFromDeck("Battle OX", "deck", player, true);
        deckMenuController.removeCardFromDeck("Battle OX", "deck", player, false);

        Assertions.assertFalse(deck.getMainCards().contains(card));
        Assertions.assertFalse(deck.getSideCards().contains(card));
    }

    @Test
    public void showMessageTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        DeckMenuOutput.getInstance().showMessage("salam");
        Assertions.assertEquals("salam\n", outContent.toString());
    }

}
