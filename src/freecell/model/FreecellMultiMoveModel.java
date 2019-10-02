package freecell.model;

import java.util.ArrayList;
import java.util.List;

import static freecell.model.PileType.CASCADE;
import static freecell.model.PileType.FOUNDATION;
import static freecell.model.PileType.OPEN;

/**
 * This class represents a FreecellMultiMoveModel and supports all its operations.
 */
public class FreecellMultiMoveModel extends AbstractFreecellModel {

  /**
   * This class represents an inner class FreecellOperationsBuilderImpl which is used mainly for
   * receive users' input about the numbers of cascades piles and open piles and create the
   * FreecellMultiMoveModel.
   */
  private static class FreecellOperationsBuilderImpl implements FreecellOperationsBuilder<Cards> {

    private int cascadesNum;
    private int opensNum;

    /**
     * Constructs a FreecellOperationsBuilderImpl and initialize the numbers of cascades piles and
     * open piles.
     */
    private FreecellOperationsBuilderImpl() {
      this.cascadesNum = 8;
      this.opensNum = 4;
    }

    @Override
    public FreecellOperationsBuilder<Cards> cascades(int c) throws IllegalArgumentException {
      if (c < 4) {
        throw new IllegalArgumentException("The number of cascades piles can't be less than 4.");
      }
      cascadesNum = c;
      return this;
    }

    @Override
    public FreecellOperationsBuilder<Cards> opens(int o) throws IllegalArgumentException {
      if (o < 1) {
        throw new IllegalArgumentException("The number of opens piles can't be less than 1.");
      }
      opensNum = o;
      return this;
    }

    @Override
    public FreecellOperations<Cards> build() {
      return new FreecellMultiMoveModel(cascadesNum, opensNum);
    }
  }

  /**
   * Constructs a FreecellMultiMoveModel with a deck, cascades piles, open piles, foundation piles,
   * cascades number, open number, status and a pileTypeMap.
   *
   * @param cascadesNum the users' input for the number of this cascades pile
   * @param opensNum    the users' input for the number of this open pile
   */
  public FreecellMultiMoveModel(int cascadesNum, int opensNum) {
    super(cascadesNum, opensNum);
  }

  /**
   * Returns the a new FreecellOperationsBuilderImpl.
   *
   * @return the a new FreecellOperationsBuilderImpl
   */
  public static FreecellOperationsBuilder<Cards> getBuilder() {
    return new FreecellOperationsBuilderImpl();
  }

  @Override
  public void move(PileType source,
                   int pileNumber,
                   int cardIndex,
                   PileType destination,
                   int destPileNumber) throws IllegalArgumentException, IllegalStateException {

    if (this.status == 3 || this.status == 1) {
      throw new IllegalStateException("Current state doesn't allow you to move cards.");
    }

    List<List<Cards>> listOfSourcePile = pileTypeMap.get(source);
    List<List<Cards>> listOfDestinationPile = pileTypeMap.get(destination);

    if (pileNumber >= listOfSourcePile.size() || destPileNumber >= listOfDestinationPile.size()) {
      throw new IllegalArgumentException("There is no such pile.");
    }

    if (cardIndex > listOfSourcePile.get(pileNumber).size() - 1) {
      throw new IllegalArgumentException("There is no such card.");
    }

    List<Cards> sourcePile = listOfSourcePile.get(pileNumber);
    List<Cards> sourceCards = sourcePile.subList(cardIndex, sourcePile.size());
    Cards sourceCard = listOfSourcePile.get(pileNumber).get(cardIndex);
    String sourceCardSuit = sourceCard.getSuit();
    int sourceCardValue = sourceCard.getValue();

    int freeOpenPileNumber = getFreePileNumber(opensPile);
    int emptyCascadesPileNumber = getFreePileNumber(cascadesPile);

    if (!multiMoveConditions1(sourceCards)
            || !multiMoveConditions2(sourceCards, freeOpenPileNumber, emptyCascadesPileNumber)) {
      throw new IllegalArgumentException("Source cards is not a valid build.");
    }

    if (destination == OPEN) {
      if (sourceCards.size() > 1) {
        throw new IllegalArgumentException("You cannot move more than 1 card to an open pile.");
      }
      destinationOpen(pileNumber, cardIndex, destPileNumber, listOfSourcePile, sourceCard);
    }

    if (destination == CASCADE) {
      destinationCascade(pileNumber, cardIndex, destPileNumber, listOfSourcePile,
              sourceCardSuit, sourceCardValue);
    }

    if (destination == FOUNDATION) {
      if (sourceCards.size() > 1) {
        throw new IllegalArgumentException("You didn't follow foundation pile rules.");
      }
      destinationFoundation(pileNumber, cardIndex, destPileNumber, listOfSourcePile,
              sourceCard, sourceCardSuit, sourceCardValue);
    }

    if (isGameOver()) {
      this.status = 3;
    }
  }

  /**
   * Return true if the cards waiting to be moved can form a valid build, i.e. they all follow
   * foundation pile rules,
   *
   * @param sourceCards the cards waiting to be moved
   * @return true if the cards waiting to be moved can form a valid build
   */
  private Boolean multiMoveToFoundationPile(List<Cards> sourceCards) {
    for (int i = 0; i < sourceCards.size() - 1; i++) {
      Cards currentCard = sourceCards.get(i);
      Cards nextCard = sourceCards.get(i + 1);
      String currentCardSuit = currentCard.getSuit();
      String nextCardSuit = nextCard.getSuit();
      int currentCardValue = currentCard.getValue();
      int nextCardValue = nextCard.getValue();
      if (!currentCardSuit.equals(nextCardSuit)) {
        return false;
      }
      if (currentCardValue != (nextCardValue - 1)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Return true if the cards waiting to be moved can form a valid build,i.e. they should be
   * arranged in alternating colors and consecutive, descending values in the cascade pile that they
   * are moving from.
   *
   * @param sourceCards the cards waiting to be moved
   * @return true if the cards waiting to be moved can form a valid build
   */
  private Boolean multiMoveConditions1(List<Cards> sourceCards) {
    for (int i = 0; i < sourceCards.size() - 1; i++) {
      Cards currentCard = sourceCards.get(i);
      Cards nextCard = sourceCards.get(i + 1);
      String currentCardSuit = currentCard.getSuit();
      String nextCardSuit = nextCard.getSuit();
      int currentCardValue = currentCard.getValue();
      int nextCardValue = nextCard.getValue();
      if (currentCardSuit.equals(HEART) || currentCardSuit.equals(DIAMOND)) {
        if (nextCardSuit.equals(HEART) || nextCardSuit.equals(DIAMOND)) {
          return false;
        } else {
          if (currentCardValue != nextCardValue + 1) {
            return false;
          }
        }
      } else if (currentCardSuit.equals(SPADE) || currentCardSuit.equals(CLUB)) {
        if (nextCardSuit.equals(SPADE) || nextCardSuit.equals(CLUB)) {
          return false;
        } else {
          if (currentCardValue != nextCardValue + 1) {
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * Return the number of free piles of the given kind of piles.
   *
   * @param pile piles that we want to get the number of its free piles
   * @return the number of its free piles
   */
  private int getFreePileNumber(List<List<Cards>> pile) {
    int count = 0;
    for (int i = 0; i < pile.size(); i++) {
      if (pile.get(i).size() == 0) {
        count = count + 1;
      }
    }
    return count;
  }

  /**
   * Return true if the number of cards waiting to be moved is less than maximum number. The maximum
   * number of cards that can be moved when there are N free open piles and K empty cascade piles is
   * (N + K) * 2 ^ K.
   *
   * @param sourceCards             the cards waiting to be moved
   * @param freeOpenPileNumber      the number of free piles among all open piles
   * @param emptyCascadesPileNumber the number of free piles among all cascades piles
   * @return true if the number of cards waiting to be moved is less than maximum number
   */
  private Boolean multiMoveConditions2(List<Cards> sourceCards, int freeOpenPileNumber,
                                       int emptyCascadesPileNumber) {
    double maxNumberOfMovedCards = (freeOpenPileNumber + 1) * Math.pow(2, emptyCascadesPileNumber);
    if (sourceCards.size() <= maxNumberOfMovedCards) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Move a card to open piles.
   *
   * @param pileNumber       pile index of the source card
   * @param cardIndex        card index of the source card
   * @param destPileNumber   pile index of the destination pile
   * @param listOfSourcePile source pile
   * @param sourceCard       the source card
   * @throws IllegalArgumentException if that open pile already exists a card
   */
  private void destinationOpen(int pileNumber, int cardIndex, int destPileNumber,
                               List<List<Cards>> listOfSourcePile,
                               Cards sourceCard) throws IllegalArgumentException {
    listOfSourcePile.get(pileNumber).remove(cardIndex);
    if (opensPile.get(destPileNumber).size() != 0) {
      listOfSourcePile.get(pileNumber).add(sourceCard);
      throw new IllegalArgumentException("This open pile already exists a card.");
    } else {
      opensPile.get(destPileNumber).add(sourceCard);
    }
  }

  /**
   * Move a card to cascade piles.
   *
   * @param pileNumber       pile index of the source card
   * @param cardIndex        card index of the source card
   * @param destPileNumber   pile index of the destination pile
   * @param listOfSourcePile source pile
   * @param sourceCardSuit   the suit of source card
   * @param sourceCardValue  the value of source card
   * @throws IllegalArgumentException if that move doesn't follow rules
   */
  private void destinationCascade(int pileNumber, int cardIndex, int destPileNumber,
                                  List<List<Cards>> listOfSourcePile,
                                  String sourceCardSuit,
                                  int sourceCardValue) throws IllegalArgumentException {
    List<Cards> movingCards = new ArrayList<>(listOfSourcePile.get(pileNumber)
            .subList(cardIndex, listOfSourcePile.get(pileNumber).size()));
    int pileSize = listOfSourcePile.get(pileNumber).size();
    for (int i = pileSize - 1; i > cardIndex - 1; i--) {
      listOfSourcePile.get(pileNumber).remove(i);
    }

    if (cascadesPile.get(destPileNumber).size() == 0) {
      for (int i = 0; i < movingCards.size(); i++) {
        cascadesPile.get(destPileNumber).add(movingCards.get(i));
      }
    } else {
      Cards lastCardInDestination = cascadesPile.get(destPileNumber)
              .get(cascadesPile.get(destPileNumber).size() - 1);
      String lastCardSuit = lastCardInDestination.getSuit();
      int lastCardValue = lastCardInDestination.getValue();
      if (sourceCardSuit.equals(HEART) || sourceCardSuit.equals(DIAMOND)) {
        checkSuitAndValue(lastCardSuit, lastCardValue, sourceCardValue, destPileNumber,
                movingCards, listOfSourcePile, pileNumber, CLUB, SPADE);
      } else {
        checkSuitAndValue(lastCardSuit, lastCardValue, sourceCardValue, destPileNumber,
                movingCards, listOfSourcePile, pileNumber, HEART, DIAMOND);
      }
    }
  }

  /**
   * Check whether this move follows rules.
   *
   * @param lastCardSuit     the suit of the last card in the destination pile
   * @param lastCardValue    the value of the last card in the destination pile
   * @param pileNumber       pile index of the source card
   * @param destPileNumber   pile index of the destination pile
   * @param listOfSourcePile source pile
   * @param movingCards      the list of source cards
   * @param sourceCardValue  the value of source card
   * @param suit1            a kind of four suits
   * @param suit2            another kind of four suits
   * @throws IllegalArgumentException if that move doesn't follow rules
   */
  private void checkSuitAndValue(String lastCardSuit, int lastCardValue, int sourceCardValue,
                                 int destPileNumber, List<Cards> movingCards,
                                 List<List<Cards>> listOfSourcePile, int pileNumber,
                                 String suit1, String suit2) throws IllegalArgumentException {

    if ((lastCardSuit.equals(suit1) || lastCardSuit.equals(suit2))
            && lastCardValue == sourceCardValue + 1) {
      cascadesPile.get(destPileNumber).addAll(movingCards);
    } else {
      listOfSourcePile.get(pileNumber).addAll(movingCards);
      throw new IllegalArgumentException("This card cannot be moved.");
    }
  }

  /**
   * Move a card to foundation piles.
   *
   * @param pileNumber       pile index of the source card
   * @param cardIndex        card index of the source card
   * @param destPileNumber   pile index of the destination pile
   * @param listOfSourcePile source pile
   * @param sourceCard       the source card
   * @param sourceCardSuit   the suit of source card
   * @param sourceCardValue  the value of source card
   * @throws IllegalArgumentException if that move doesn't follow rules
   */
  private void destinationFoundation(int pileNumber, int cardIndex, int destPileNumber,
                                     List<List<Cards>> listOfSourcePile,
                                     Cards sourceCard, String sourceCardSuit,
                                     int sourceCardValue) throws IllegalArgumentException {
    listOfSourcePile.get(pileNumber).remove(cardIndex);
    if (foundationsPile.get(destPileNumber).size() == 0) {
      foundationsPile.get(destPileNumber).add(sourceCard);
    } else {
      Cards lastCardInDestination = foundationsPile.get(destPileNumber)
              .get(foundationsPile.get(destPileNumber).size() - 1);
      String lastCardSuit = lastCardInDestination.getSuit();
      int lastCardValue = lastCardInDestination.getValue();
      if (sourceCardSuit.equals(lastCardSuit) && sourceCardValue == lastCardValue + 1) {
        foundationsPile.get(destPileNumber).add(sourceCard);
      } else {
        listOfSourcePile.get(pileNumber).add(sourceCard);
        throw new IllegalArgumentException("This card cannot be moved.");
      }
    }
  }

}
