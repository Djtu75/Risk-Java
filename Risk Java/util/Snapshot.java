package util;

import java.util.Set;

public class Snapshot {
    private World world;
    private Player[] players;
    private Set<Card> userHand;
    private int currentSetBonus;

    public Snapshot(World world, Player[] players, Set<Card> userHand, int currentSetBonus){
        this.world = world;
        this.players = players;
        this.userHand = userHand;
        this.currentSetBonus = currentSetBonus;
    }

    public World getWorld() {
        return world;
    }

    public Player[] getPlayers() {
        return players;
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
