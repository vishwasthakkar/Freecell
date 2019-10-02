package freecell.model;

import java.util.List;

import static freecell.model.PileType.CASCADE;
import static freecell.model.PileType.FOUNDATION;
import static freecell.model.PileType.OPEN;

/**
 * This class represents a FreecellModel and supports all its operations.
 */
public class FreecellModel extends AbstractFreecellModel {

  /**
   * This class represents an inner class FreecellOperationsBuilderImpl which is used mainly for
   * receive users' input about the numbers of cascades piles and open piles and create the
   * FreecellModel.
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
      return new FreecellModel(cascadesNum, opensNum);
    }
  }

  /**
   * Constructs a FreecellModel with a deck, cascades piles, open piles, foundation piles, cascades
   * number, open number, status and a pileTypeMap.
   *
   * @param cascadesNum the users' input for the number of this cascades pile
   * @param opensNum    the users' input for the number of this open pile
   */
  public FreecellModel(int cascadesNum, int opensNum) {
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

    if (cardIndex != listOfSourcePile.get(pileNumber).size() - 1) {
      throw new IllegalArgumentException("This card cannot be moved or there is no such card.");
    }

    Cards sourceCard = listOfSourcePile.get(pileNumber).get(cardIndex);
    String sourceCardSuit = sourceCard.getSuit();
    int sourceCardValue = sourceCard.getValue();

    if (destination == OPEN) {
      destinationOpen(pileNumber, cardIndex, destPileNumber, listOfSourcePile, sourceCard);
    }

    if (destination == CASCADE) {
      destinationCascade(pileNumber, cardIndex, destPileNumber, listOfSourcePile,
              sourceCard, sourceCardSuit, sourceCardValue);
    }

    if (destination == FOUNDATION) {
      destinationFoundation(pileNumber, cardIndex, destPileNumber, listOfSourcePile,
              sourceCard, sourceCardSuit, sourceCardValue);
    }

    if (isGameOver()) {
      this.status = 3;
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
   * @param sourceCard       the source card
   * @param sourceCardSuit   the suit of source card
   * @param sourceCardValue  the value of source card
   * @throws IllegalArgumentException if that move doesn't follow rules
   */
  private void destinationCascade(int pileNumber, int cardIndex, int destPileNumber,
                                  List<List<Cards>> listOfSourcePile,
                                  Cards sourceCard, String sourceCardSuit,
                                  int sourceCardValue) throws IllegalArgumentException {
    listOfSourcePile.get(pileNumber).remove(cardIndex);
    if (cascadesPile.get(destPileNumber).size() == 0) {
      cascadesPile.get(destPileNumber).add(sourceCard);
    } else {
      Cards lastCardInDestination = cascadesPile.get(destPileNumber)
              .get(cascadesPile.get(destPileNumber).size() - 1);
      String lastCardSuit = lastCardInDestination.getSuit();
      int lastCardValue = lastCardInDestination.getValue();
      if (sourceCardSuit.equals(HEART) || sourceCardSuit.equals(DIAMOND)) {
        checkSuitAndValue(lastCardSuit, lastCardValue, sourceCardValue, destPileNumber,
                sourceCard, listOfSourcePile, pileNumber, CLUB, SPADE);
      } else {
        checkSuitAndValue(lastCardSuit, lastCardValue, sourceCardValue, destPileNumber,
                sourceCard, listOfSourcePile, pileNumber, HEART, DIAMOND);
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
   * @param sourceCard       the source card
   * @param sourceCardValue  the value of source card
   * @param suit1            a kind of four suits
   * @param suit2            another kind of four suits
   * @throws IllegalArgumentException if that move doesn't follow rules
   */
  private void checkSuitAndValue(String lastCardSuit, int lastCardValue, int sourceCardValue,
                                 int destPileNumber, Cards sourceCard,
                                 List<List<Cards>> listOfSourcePile, int pileNumber,
                                 String suit1, String suit2) throws IllegalArgumentException {
    if ((lastCardSuit.equals(suit1) || lastCardSuit.equals(suit2))
            && lastCardValue == sourceCardValue + 1) {
      cascadesPile.get(destPileNumber).add(sourceCard);
    } else {
      listOfSourcePile.get(pileNumber).add(sourceCard);
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
