package participant.Entries;

import participant.*;
import java.util.*;
import java.awt.Color;
import util.*;

public class CooperPlayer extends PlayerLogic{
    /**
     * @return Method to tell player their turn is about to start. Intended to let player initialize variables and logic.
     */
    public void beginTurn(Snapshot s){
    }

    /**
     * @return //Method to tell player their turn is about to end. Intended to let player clean up variables and logic.
     */
    public void endTurn(Snapshot s){
    }

    /**
     * @return //Asks player to place troops. Should return [int numTroopstoPlace, Province destination]
     */
    public Set<DeployCommand> draftPhase(Snapshot s, int numTroopstoPlace){
        return Set.of(new DeployCommand(numTroopstoPlace, s.getMyPlayer().getTerritory().stream().skip(new Random().nextLong(s.getMyPlayer().getTerritory().size())).findFirst().orElse(null)));
    }

    /**
     * @return //Asks player to perform attack. Should return [int numTroopstoUse, Province attackingProvince, Province defendingProvince]
     */
    public AttackCommand attackPhase(Snapshot s){
        return 
        new AttackCommand(
            s.getMyPlayer().getTerritory().stream().max((p1, p2) -> Integer.compare(p1.getNumSoldiers(), p2.getNumSoldiers())).orElse(null).getNumSoldiers()-1,
            s.getMyPlayer().getTerritory().stream().max((p1, p2) -> Integer.compare(p1.getNumSoldiers(), p2.getNumSoldiers())).orElse(null),
            s.getMyPlayer().getTerritory().stream().max((p1, p2) -> Integer.compare(p1.getNumSoldiers(), p2.getNumSoldiers())).orElse(null).getAdjacent().stream().filter((prov) -> (!prov.getOwner().equals(s.getMyPlayer()))).min((p1, p2) -> Integer.compare(p1.getNumSoldiers(), p2.getNumSoldiers())).orElse(null)
        );
    }

    /**
     * @return //Asks player to move troops. Should return [int numTroopstoMove, Province source, Province destination]
     */
    public MoveCommand movePhase(Snapshot s){
        return new MoveCommand(0, null, null);
    }

    public void attackPhaseResults(Snapshot s, int[] battleResults){
    }


    /**
     * @return //Asks player how many troops to move into conquered territory. Should return [int numTroops]
     */
    public int moveAfterConquer(Snapshot s, Province attackingProvince, Province defendingProvince){
        return attackingProvince.getNumSoldiers()-1;
    }

    /**
     * @return //Asks player to turn in set of cards. If required, must be [Card X, Card Y, Card Z]. If not required, any invalid input will count as passing on your turn.
     */
    public Set<Card> turnInCards(Snapshot s, boolean required, int currentTroops){
        return required || evalTurnIn(getBestCardSet(null, null, null, 0, 0, new ArrayList<Card>(s.getUserHand()), s.getMyPlayer().getTerritory()), s.getMyPlayer().getTerritory()) >= 0 ? getBestCardSet(null, null, null, 0, 0, new ArrayList<Card>(s.getUserHand()), s.getMyPlayer().getTerritory()) : null;
    }

    public static Set<Card> getBestCardSet(Card c1, Card c2, Card c3, int pos, int idx, ArrayList<Card> hand, Set<Province> op) {
        return (pos >= 3) ? Set.of(c1, c2, c3) :
        (idx >= hand.size()) ? null :
        (pos == 0) ? 
            (evalTurnIn(getBestCardSet(hand.get(idx), c2, c3, pos+1, idx+1, hand, op), op) >= evalTurnIn(getBestCardSet(c1, c2, c3, pos, idx+1, hand, op), op)) ? 
            getBestCardSet(hand.get(idx), c2, c3, pos+1, idx+1, hand, op):
            getBestCardSet(c1, c2, c3, pos, idx+1, hand, op):
        (pos == 1) ?
            (evalTurnIn(getBestCardSet(c1, hand.get(idx), c3, pos+1, idx+1, hand, op), op) >= evalTurnIn(getBestCardSet(c1, c2, c3, pos, idx+1, hand, op), op)) ?
            getBestCardSet(c1, hand.get(idx), c3, pos+1, idx+1, hand, op):
            getBestCardSet(c1, c2, c3, pos, idx+1, hand, op):
        (pos == 2) ? 
            evalTurnIn(getBestCardSet(c1, c2, hand.get(idx), pos+1, idx+1, hand, op), op) >= evalTurnIn(getBestCardSet(c1, c2, c3, pos, idx+1, hand, op), op) ?
            getBestCardSet(c1, c2, hand.get(idx), pos+1, idx+1, hand, op):
            getBestCardSet(c1, c2, c3, pos, idx+1, hand, op):
        null;
    }

    public static int evalTurnIn(Set<Card> cards, Set<Province> ownedProvinces) {
        return cards == null ? -99999 :
        cards.size() > 3 ? -99999 : 
        (cards.stream().filter((card) -> card.getType() == 0 || card.getType() == 3).count() >= 3 || 
        cards.stream().filter((card) -> card.getType() == 1 || card.getType() == 3).count() >= 3 ||
        cards.stream().filter((card) -> card.getType() == 2 || card.getType() == 3).count() >= 3 ||
        (cards.stream().filter((card) -> card.getType() == 0).count() == 1 ? 1 : 0) + 
        (cards.stream().filter((card) -> card.getType() == 1).count() == 1 ? 1 : 0) + 
        (cards.stream().filter((card) -> card.getType() == 2).count() == 1 ? 1 : 0) + 
        (cards.stream().filter((card) -> card.getType() == 3).count()) == 3) ? 
        cards.stream().mapToInt((card) -> card == null ? -99999 : card.getProvince() == null ? 0 : card.getProvince().getOwner().getName().equals("hide on fenwick tree!!!") ? 2 : 0).sum(): -99999;
    }

    /**
     * @return This lets you choose a custom player name to show on the leaderboard.
     * 
     */
    public String getCustomName(){
        return "hide on fenwick tree!!!";
    }

    /**
     * @return This lets you choose a custom player color to show on the map. Needs to return an array of dimension two.
     * The two colors are used as the corners of a gradient, so if you want to be one solid color, return the same color twice.
     * 
     */
    public Color[] getCustomColor(){
        return new Color[]{new Color(255, 255, 255), new Color(255, 255, 255)};
    }

    /**
     * @return This lets you choose a custom player profile to show on the map. Needs to be a url.
     * 
     */
    public String getCustomProfile(){
        return "https://i.pinimg.com/736x/3d/77/d7/3d77d7e19f5860ff911a05db229df3a6.jpg";
    }
}
