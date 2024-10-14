package participant;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.LogRecord;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.logging.Level;

import util.*;

public class ExamplePlayer1 extends PlayerLogic {
    GameLogger GL; 
    public ExamplePlayer1(){
        super();
    }

    private double[][] winPct = {
        {0.417 ,0.106 ,0.027 ,0.007 ,0.002 ,0.000 ,0.000 ,0.000 ,0.000 ,0.000},
        {0.754 ,0.363 ,0.206 ,0.091, 0.049, 0.021, 0.011, 0.005, 0.003, 0.001},
        {0.916 ,0.656 ,0.470 ,0.315 ,0.206 ,0.134 ,0.084 ,0.054 ,0.033, 0.021},
        {0.972 ,0.785 ,0.642 ,0.477 ,0.359 ,0.253 ,0.181 ,0.123 ,0.086, 0.057},
        {0.990 ,0.890 ,0.769 ,0.638 ,0.506 ,0.397 ,0.297 ,0.224 ,0.162 ,0.118},
        {0.997 ,0.934 ,0.857 ,0.745 ,0.638 ,0.521 ,0.423 ,0.329 ,0.258 ,0.193},
        {0.999 ,0.967 ,0.910 ,0.834 ,0.736 ,0.640 ,0.536 ,0.446 ,0.357 ,0.287},
        {1.000 ,0.980 ,0.947 ,0.888 ,0.818 ,0.730 ,0.643 ,0.547 ,0.464 ,0.380},
        {1.000 ,0.990 ,0.967 ,0.930 ,0.873 ,0.808 ,0.726 ,0.646 ,0.558 ,0.480},
        {1.000 ,0.994 ,0.981 ,0.954 ,0.916 ,0.861 ,0.800 ,0.724 ,0.650 ,0.568}
        };

    /**
     * @return Method to tell player their turn is about to start. Intended to let player initialize variables and logic.
     */
    public void beginTurn(Snapshot s){
        //Do setup logic
    }

    /**
     * @return //Method to tell player their turn is about to end. Intended to let player clean up variables and logic.
     */
    public void endTurn(Snapshot s){
        //Do ending logic
    }

    /**
     * @return //Asks player to place troops. Should return [int numTroopstoPlace, Province destination]
     */

    public Set<DeployCommand> draftPhase(Snapshot s, int numTroopstoPlace){
        GL = GameLogger.getGameLogger();
        Set<DeployCommand> returnArray = new HashSet<DeployCommand>();
        Set<Province> myterritory = s.getMyPlayer().getTerritory();
        if(myterritory.size() <= 0){
            return returnArray;
        }
        Set<Province> neighbors = new HashSet<Province>();
        Set<Province> borders = new HashSet<Province>();
        int placeXTroops = numTroopstoPlace;

        //find all neighbors
        for(Province owned : myterritory){
            Set<Province> adjacent =  owned.getAdjacent();
            for(Province target : adjacent){
                if(target.getOwner() != s.getMyPlayer()){
                    neighbors.add(target);
                    borders.add(owned);

                }
            }
        }
        GL.LogMessage(("Has "+ borders.size() +" border territories and "+ neighbors.size() +" neighboring territories").toString());
        while(placeXTroops > 0){
            for(Province  border : borders){
                if(placeXTroops > 0){
                    returnArray.add(new DeployCommand(1,border));
                    placeXTroops--;
                    //GL.LogMessage((s.getMyPlayer().getName() + " placed 1 troop in "+ border.getName()));
                }else{
                    break;
                }
            }
        }
        return(returnArray);
    }

    /**
     * @return //Asks player to perform attack. Should return [int numTroopstoUse, Province attackingProvince, Province defendingProvince]
     */
    public AttackCommand attackPhase(Snapshot s){
        if(s.getMyPlayer().getTerritory().size() <= 0){
            return new AttackCommand(0, null,null);
        }
        AttackCommand ret;
        Province attackingProvince = null;
        Province defendingProvince = null;
        int useXtroops = 1;
        double pct;
        for(Province p: s.getMyPlayer().getTerritory()){
            if(p.getNumSoldiers() > 3){
                for(Province adj: p.getAdjacent()){
                    if(adj.getOwner() != p.getOwner()){
                        pct = winPctOfPartialAttack(useXtroops , adj.getNumSoldiers());
                        if(pct == -1.0 || p.getNumSoldiers() >= 100){
                            if((p.getNumSoldiers() - 1) >= (3*adj.getNumSoldiers())){
                                useXtroops = 3*adj.getNumSoldiers();
                                attackingProvince = p;
                                defendingProvince = adj;
                            }else if(p.getNumSoldiers() >= 2000){
                                useXtroops = p.getNumSoldiers() -1;
                                attackingProvince = p;
                                defendingProvince = adj;
                            }
                            //win percentage is not exact, just go with who has more at this point
                        }else{
                            while(pct < 70.0){
                                useXtroops++;
                                if(useXtroops >= p.getNumSoldiers()){
                                    break;
                                }
                                pct = winPctOfPartialAttack(useXtroops , adj.getNumSoldiers());
                            }
                            if(useXtroops <= p.getNumSoldiers() - 1 && pct > 70.0){
                                attackingProvince = p;
                                defendingProvince = adj;
                            }
                        }
                    }
                }
            }
        }
        if((attackingProvince != null) && (defendingProvince != null)){
            //GL.LogMessage((s.getMyPlayer().getName()+" is attacking from " + attackingProvince.getName() +" Soliders: "+ String.valueOf(attackingProvince.getNumSoldiers()) +" WinPercentage: " + winPctOfPartialAttack(useXtroops, defendingProvince.getNumSoldiers())));
            //GL.LogMessage(("Defending " + defendingProvince.getName() + " with "+ String.valueOf(defendingProvince.getNumSoldiers())+ " Soldiers"));
            ret = new AttackCommand(useXtroops, attackingProvince, defendingProvince);
            //return (returnArray);
            return ret;
        }else { return null;}



    }

    /**
     * @return //Asks player to move troops. Should return [int numTroopstoMove, Province source, Province destination]
     */
    public MoveCommand movePhase(Snapshot s){
        if(s.getMyPlayer().getTerritory().size() <= 0){
            return new MoveCommand(0, null,null);
        }
        Set<Province> owned = s.getMyPlayer().getTerritory();
        int maxLazyTroops = 0;
        int maxEnemies = 0;
        Province underSuppliedProvince = null;
        Province lazyProvince = null;
        for(Province mine: owned){
            Set<Province> adjacent =  mine.getAdjacent();
            int lazyTroops = 0;
            int enemies = 0;
            for(Province target : adjacent){
                if(target.getOwner() != s.getMyPlayer()){
                    lazyTroops = -1;
                    enemies += target.getNumSoldiers();
                }else{
                    if(lazyTroops == -1){
                    lazyTroops = mine.getNumSoldiers() - 1;
                    }
                }
            }
            if(enemies > maxEnemies && enemies > 0){
                maxEnemies = enemies;
                underSuppliedProvince = mine;
            }
            if(lazyTroops > maxLazyTroops && lazyTroops > 0){
                maxLazyTroops = lazyTroops;
                lazyProvince = mine;
            }
        }
        return new MoveCommand(maxLazyTroops , underSuppliedProvince, lazyProvince);
        //return (returnArray);
    }

    public void attackPhaseResults(int[] battleResults){
        //Check if attack went well
        //Change logic before attackPhase gets called again

        return;
    }


    /**
     * @return //Asks player how many troops to move into conquered territory. Should return [int numTroops]
     */
    public int moveAfterConquer(Snapshot s, Province attackingProvince, Province defendingProvince){
    //find all neighbors
    if(s.getMyPlayer().getTerritory().size() <= 0){
        return 0;
    }
        int TransferXTroops = 0;
        Set<Province> adjacent =  attackingProvince.getAdjacent();
            for(Province target : adjacent){
                if(target.getOwner() != s.getMyPlayer()){
                    TransferXTroops = (attackingProvince.getNumSoldiers()-1) / 2;
                    break;
                }else{
                    TransferXTroops = (attackingProvince.getNumSoldiers()-1);
                }
            }

        return TransferXTroops;

    }

    /**
     * @return //Asks player to turn in set of cards. If required, must be [Card X, Card Y, Card Z]. If not required, any invalid input will count as passing on your turn.
     */
    public Set<Card> turnInCards(Snapshot s, boolean required, int currentTroops){
        
            Set<Card> mycards = s.getUserHand();
            Set<Card> set = makeSet(mycards);
            return(set);
    }

    public double winPctOfFullAttack(Province attacker, Province defender){
        int attackers = attacker.getNumSoldiers();
        int defenders = defender.getNumSoldiers();
        if(attackers > 10 || defenders > 10){
            return -1.0;
        }

        return (winPct[attackers -1][defenders -1] * 100);

    }
    public double winPctOfPartialAttack(int attackers, int defenders){
        if(attackers > 10 || defenders > 10){
            return -1.0;
        }
        return (winPct[attackers -1][defenders -1] * 100);
    }

    public Set<Card> makeSet(Set<Card> set){
        int[] types = new int[4]; //indexes 0 = infantry, 1 = cavalry, 2 = artillery, 3 = wild
        Set<Card> returnSet = new HashSet<Card>();

        for(Card c : set){
            types[c.getType()]++;
        }
        int setType = 3;
        for(int i = 0; i < types.length; i++){
            if(types[i] >= 3){
                setType = i;
            }
        }
        if(setType == 3){
            if(types[0] > 0 && types[1] > 0 && types[2] > 0){
                int[] needed = {0, 0, 0}; //0 = not found, 1 = found
                for(Card c : set){
                    for(int i = 0; i < needed.length; i++){
                        if(needed[i] == 0 && c.getType() == i){
                            returnSet.add(c);
                            needed[i] = 1;
                        }
                    }
                }
            }
            else{
                int found = 0;
                int target = 2;
                if(types[0] >= types[1] && types[0] >= types[2]){
                    target = 0;
                }
                else if(types[1] >= types[0] && types[1] >= types[2]){
                    target = 1;
                }
                for(Card c : set){
                    if(c.getType() == target){
                        returnSet.add(c);
                        found++;
                    }
                }
                for(Card c : set){
                    if(found < 3 && c.getType() == 3){
                        returnSet.add(c);
                        found++;
                    }
                }
            }
        }
        else{
            int found = 0;
            for(Card c : set){
                if(found < 3 && c.getType() == setType){
                    returnSet.add(c);
                    found++;
                }
            }
        }
        //System.out.println("Number of infantry "+ types[0]);
        //System.out.println("Number of cavalry "+types[1]);
        //System.out.println("Number of artillery "+types[2]);
        //System.out.println("Number of wilds "+types[3]);

        return returnSet;
    }
    
}

