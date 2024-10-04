package util;

import java.util.Set;

public class Snapshot {
    private Game game;
    private World world;
    private Player[] players;
    private Player yourPlayer;
    private Set<Card> userHand;
    private int currentSetBonus;

    public Snapshot(Game game, World world, Player[] players, Player yourPlayer, Set<Card> userHand, int currentSetBonus){
        this.game = game;
        this.world = world;
        this.players = players;
        this.yourPlayer = yourPlayer;
        this.userHand = userHand;
        this.currentSetBonus = currentSetBonus;
    }

    public Game getGame() {
        return game;
    }

    public World getWorld() {
        return world;
    }

    public Player[] getPlayers() {
        return players;
    }

    public Player getMyPlayer() {
        return yourPlayer;
    }

    public Set<Card> getUserHand() {
        return userHand;
    }

    public int getCurrentSetBonus() {
        return currentSetBonus;
    }

    public int getPlayerHandSize(Player p){
        int returnnum = -1;
        for(Player player: players){
            if(p == player){
                return p.getNumCards();
            }
        }
        return returnnum;
    }

    public Player[] getPowerRankings(){
        Player[] returnArray = players.clone();

        for(int i = 0; i < returnArray.length; i++){
            for(int j = i + 1; j < returnArray.length; j++){
                if(returnArray[i].getNumSoldiers() > returnArray[j].getNumSoldiers()){
                    Player temp = returnArray[j];
                    returnArray[j] = returnArray[i];
                    returnArray[i] = temp;
                }
            }
        }

        return returnArray;
    }


}
