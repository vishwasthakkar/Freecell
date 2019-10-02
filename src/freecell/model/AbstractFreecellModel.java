package freecell.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static freecell.model.PileType.CASCADE;
import static freecell.model.PileType.FOUNDATION;
import static freecell.model.PileType.OPEN;

/**
 * This abstract class represents a AbstractFreecellModel and supports all its operations.
 */
public abstract class AbstractFreecellModel implements FreecellOperations<Cards> {

  protected List<Cards> deck;
  protected List<List<Cards>> cascadesPile;
  protected List<List<Cards>> opensPile;
  protected List<List<Cards>> foundationsPile;

  protected Map<PileType, List<List<Cards>>> pileTypeMap;

  protected int cascadesNum;
  protected int opensNum;

  protected int status;
  protected static final int NOT_STARTED = 1;
  protected static final int STARTED = 2;
  protected static final int OVER = 3;

  protected static final String CLUB = "♣";
  protected static final String DIAMOND = "♦";
  protected static final String HEART = "♥";
  protected static final String SPADE = "♠";

  /**
   * Constructs a AbstractFreecellModel with a deck, cascades piles, open piles, foundation piles,
   * cascades number, open number, status and a pileTypeMap.
   *
   * @param cascadesNum the users' input for the number of this cascades pile
   * @param opensNum    the users' input for the number of this open pile
   */
  public AbstractFreecellModel(int cascadesNum, int opensNum) {
    deck = new ArrayList<>();
    cascadesPile = new ArrayList<>();
    opensPile = new ArrayList<>();
    foundationsPile = new ArrayList<>();
    this.cascadesNum = cascadesNum;
    this.opensNum = opensNum;
    this.status = 1;

    //initialize deck
    for (int i = 1; i <= 13; i++) {
      this.deck.add(new Cards(i, CLUB));
    }
    for (int i = 1; i <= 13; i++) {
      this.deck.add(new Cards(i, DIAMOND));
    }
    for (int i = 1; i <= 13; i++) {
      this.deck.add(new Cards(i, HEART));
    }
    for (int i = 1; i <= 13; i++) {
      this.deck.add(new Cards(i, SPADE));
    }

    //create a map with PileType as key and pile itself as value
    pileTypeMap = new HashMap<>(4);
    pileTypeMap.put(OPEN, opensPile);
    pileTypeMap.put(CASCADE, cascadesPile);
    pileTypeMap.put(FOUNDATION, foundationsPile);
  }

  @Override
  public List<Cards> getDeck() {
    return this.deck;
  }

  @Override
  public void startGame(List<Cards> deck, boolean shuffle) throws IllegalArgumentException {
    if (deck == null) {
      deck = this.deck;
    }
    if (deck.size() != 52) {
      throw new IllegalArgumentException("The deck number is not 52!");
    } else if (duplicateCards(deck)) {
      throw new IllegalArgumentException("There are duplicate cards!");
    } else if (invalidCards(deck)) {
      throw new IllegalArgumentException("There are invalid cards!");
    } else if (cascadesNum < 4 || opensNum < 1) {
      throw new IllegalArgumentException("The number of cascades piles or open piles "
              + "cannot be that small.");
    } else if (shuffle) {
      Collections.shuffle(deck);
    }

    initializePiles();

    //allocate cards to piles
    for (int i = 0; i < 52; i++) {
      cascadesPile.get(i % cascadesNum).add(deck.get(i));
    }
    this.status = 2;
  }

  /**
   * Initialize the cascades, open, foundation Piles.
   */
  private void initializePiles() {
    cascadesPile.clear();
    opensPile.clear();
    foundationsPile.clear();

    for (int i = 0; i < cascadesNum; i++) {
      cascadesPile.add(new ArrayList<>());
    }

    for (int i = 0; i < opensNum; i++) {
      opensPile.add(new ArrayList<>());
    }

    foundationsPile.add(new ArrayList<>());
    foundationsPile.add(new ArrayList<>());
    foundationsPile.add(new ArrayList<>());
    foundationsPile.add(new ArrayList<>());
  }

  /**
   * Return true if there are duplicate cards.
   *
   * @param deck the list of cards
   * @return true if there are duplicate cards
   */
  private Boolean duplicateCards(List<Cards> deck) {
    Set<Cards> cardsSet = new HashSet<>(deck);
    return cardsSet.size() != deck.size();
  }

  /**
   * Return true if there are invalid cards.
   *
   * @param deck the list of cards
   * @return true if there are invalid cards
   */
  private Boolean invalidCards(List<Cards> deck) {
    for (int i = 0; i < deck.size(); i++) {
      if (deck.get(i).getValue() > 13 || deck.get(i).getValue() < 1
              || (!deck.get(i).getSuit().equals(CLUB) && !deck.get(i).getSuit().equals(DIAMOND)
              && !deck.get(i).getSuit().equals(HEART) && !deck.get(i).getSuit().equals(SPADE))) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isGameOver() {
    if (this.status == 3) {
      return true;
    }
    if (this.status == 1) {
      return false;
    }
    List<Cards> foundationsPile1 = this.foundationsPile.get(0);
    List<Cards> foundationsPile2 = this.foundationsPile.get(1);
    List<Cards> foundationsPile3 = this.foundationsPile.get(2);
    List<Cards> foundationsPile4 = this.foundationsPile.get(3);
    if (checkSuits(foundationsPile1, foundationsPile2, foundationsPile3, foundationsPile4)
            && checkValues(foundationsPile1, foundationsPile2,
            foundationsPile3, foundationsPile4)) {
      this.status = 3;
      return true;
    } else {
      return false;
    }
  }

  /**
   * Return true if all cards in foundation piles meet the suit requirements of winning.
   *
   * @param foundationsPile1 the first foundation pile
   * @param foundationsPile2 the second foundation pile
   * @param foundationsPile3 the third foundation pile
   * @param foundationsPile4 the fourth foundation pile
   * @return true if all cards in foundation piles meet the suit requirements of winning
   */
  private Boolean checkSuits(List<Cards> foundationsPile1, List<Cards> foundationsPile2,
                             List<Cards> foundationsPile3, List<Cards> foundationsPile4) {
    Set<String> suitSet = new HashSet<>();
    if (foundationsPile1.size() == 13 && foundationsPile2.size() == 13
            && foundationsPile3.size() == 13 && foundationsPile4.size() == 13) {
      String foundationsPile1Suit = foundationsPile1.get(0).getSuit();
      String foundationsPile2Suit = foundationsPile2.get(0).getSuit();
      String foundationsPile3Suit = foundationsPile3.get(0).getSuit();
      String foundationsPile4Suit = foundationsPile4.get(0).getSuit();
      suitSet.add(foundationsPile1Suit);
      suitSet.add(foundationsPile2Suit);
      suitSet.add(foundationsPile3Suit);
      suitSet.add(foundationsPile4Suit);
      for (int i = 1; i < 13; i++) {
        if (!foundationsPile1.get(i).getSuit().equals(foundationsPile1Suit)
                || !foundationsPile2.get(i).getSuit().equals(foundationsPile2Suit)
                || !foundationsPile3.get(i).getSuit().equals(foundationsPile3Suit)
                || !foundationsPile4.get(i).getSuit().equals(foundationsPile4Suit)) {
          return false;
        }
      }
      if (!suitSet.contains(CLUB) || !suitSet.contains(DIAMOND)
              || !suitSet.contains(HEART) || !suitSet.contains(SPADE)) {
        return false;
      }
      return true;
    } else {
      return false;
    }
  }

  /**
   * Return true if all cards in foundation piles meet the value requirements of winning.
   *
   * @param foundationsPile1 the first foundation pile
   * @param foundationsPile2 the second foundation pile
   * @param foundationsPile3 the third foundation pile
   * @param foundationsPile4 the fourth foundation pile
   * @return true if all cards in foundation piles meet the value requirements of winning
   */
  private Boolean checkValues(List<Cards> foundationsPile1, List<Cards> foundationsPile2,
                              List<Cards> foundationsPile3, List<Cards> foundationsPile4) {
    int foundationsPile1Card1Value = foundationsPile1.get(0).getValue();
    int foundationsPile2Card1Value = foundationsPile2.get(0).getValue();
    int foundationsPile3Card1Value = foundationsPile3.get(0).getValue();
    int foundationsPile4Card1Value = foundationsPile4.get(0).getValue();

    if (foundationsPile1Card1Value == 1 && foundationsPile2Card1Value == 1
            && foundationsPile3Card1Value == 1 && foundationsPile4Card1Value == 1) {
      for (int i = 1; i < 13; i++) {
        if (foundationsPile1.get(i).getValue() != foundationsPile1.get(i - 1).getValue() + 1
                || foundationsPile2.get(i).getValue()
                != foundationsPile2.get(i - 1).getValue() + 1
                || foundationsPile3.get(i).getValue()
                != foundationsPile3.get(i - 1).getValue() + 1
                || foundationsPile4.get(i).getValue()
                != foundationsPile4.get(i - 1).getValue() + 1) {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }

  @Override
  public String getGameState() {
    if (this.status == 1) {
      return "";
    }
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < 4; i++) {
      if (foundationsPile.get(i).size() == 0) {
        sb.append("F" + (i + 1) + ":\n");
      } else {
        sb.append("F" + (i + 1) + ":");
      }
      List<Cards> specificPile = foundationsPile.get(i);
      appendGameState(specificPile, sb);
    }

    for (int i = 0; i < opensNum; i++) {
      if (opensPile.get(i).size() == 0) {
        sb.append("O" + (i + 1) + ":\n");
      } else {
        sb.append("O" + (i + 1) + ":");
      }
      List<Cards> specificPile = opensPile.get(i);
      appendGameState(specificPile, sb);
    }

    for (int i = 0; i < cascadesNum; i++) {
      if (cascadesPile.get(i).size() == 0) {
        sb.append("C" + (i + 1) + ":\n");
      } else {
        sb.append("C" + (i + 1) + ":");
      }
      List<Cards> specificPile = cascadesPile.get(i);
      appendGameState(specificPile, sb);
    }
    return sb.substring(0, sb.length() - 1);
  }

  /**
   * Append state of piles as a StringBuilder.
   *
   * @param specificPile the specific pile that I want to get the state of
   * @param sb           containing the state of piles as a StringBuilder
   */
  private void appendGameState(List<Cards> specificPile, StringBuilder sb) {
    for (int j = 0; j < specificPile.size(); j++) {
      Cards currentCard = specificPile.get(j);
      if (j == specificPile.size() - 1) {
        sb.append(" " + currentCard.toString() + "\n");
      } else {
        sb.append(" " + currentCard.toString() + ",");
      }
    }
  }
}
