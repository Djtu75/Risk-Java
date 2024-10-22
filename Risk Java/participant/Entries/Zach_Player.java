package participant.Entries;

import participant.*;
import util.AttackCommand;
import util.Card;
import util.DeployCommand;
import util.MoveCommand;
import util.Province;
import util.Snapshot;
import java.util.Set;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.awt.Color;

import util.Player;

public class Zach_Player extends PlayerLogic {
    // 25 x 25 table showing the percentage win chance for each amount of troops for defenders and attackers, up to 25 v 25.
    // Getting a table item is: winPercentages[defenders][attackers]
    double[][] winPercentages = {{41.67, 75.42, 91.64, 97.15, 99.03, 99.67, 99.89, 99.96, 99.99, 100.00, 100.00, 100.00, 100.00, 100.00, 100.00, 100.00, 100.00, 100.00, 100.00, 100.00, 100.00, 100.00, 100.00, 100.00, 100.00},
                                {10.61, 36.27, 65.60, 78.55, 88.98, 93.40, 96.66, 98.03, 99.01, 99.42, 99.71, 99.83, 99.91, 99.95, 99.98, 99.99, 99.99, 100.00, 100.00, 100.00, 100.00, 100.00, 100.00, 100.00, 100.00},
                                {2.70, 20.61, 47.03, 64.16, 76.94, 85.69, 90.99, 94.68, 96.70, 98.11, 98.84, 99.35, 99.60, 99.78, 99.87, 99.93, 99.96, 99.98, 99.99, 99.99, 100.00, 100.00, 100.00, 100.00, 100.00},
                                {0.69, 9.13, 31.50, 47.65, 63.83, 74.49, 83.37, 88.78, 92.98, 95.39, 97.20, 98.20, 98.93, 99.32, 99.60, 99.75, 99.86, 99.91, 99.95, 99.97, 99.98, 99.99, 99.99, 100.00, 100.00},
                                {0.18, 4.91, 20.59, 35.86, 50.62, 63.77, 73.64, 81.84, 87.29, 91.63, 94.30, 96.37, 97.58, 98.50, 99.02, 99.40, 99.61, 99.77, 99.85, 99.91, 99.94, 99.97, 99.98, 99.99, 99.99},
                                {0.04, 2.14, 13.37, 25.25, 39.68, 52.07, 64.01, 72.96, 80.76, 86.11, 90.52, 93.35, 95.61, 96.99, 98.06, 98.70, 99.18, 99.46, 99.66, 99.78, 99.87, 99.91, 99.95, 99.97, 99.98},
                                {0.01, 1.13, 8.37, 18.15, 29.74, 42.33, 53.55, 64.29, 72.61, 79.98, 85.20, 89.61, 92.54, 94.93, 96.44, 97.64, 98.38, 98.95, 99.29, 99.55, 99.70, 99.81, 99.87, 99.92, 99.95},
                                {0.00, 0.49, 5.35, 12.34, 22.40, 32.95, 44.56, 54.74, 64.64, 72.40, 79.41, 84.49, 88.86, 91.84, 94.32, 95.93, 97.24, 98.06, 98.71, 99.11, 99.42, 99.61, 99.75, 99.83, 99.89},
                                {0.00, 0.26, 3.28, 8.62, 16.16, 25.78, 35.69, 46.40, 55.81, 65.01, 72.30, 78.99, 83.92, 88.23, 91.23, 93.77, 95.47, 96.86, 97.76, 98.48, 98.93, 99.29, 99.51, 99.68, 99.78},
                                {0.00, 0.11, 2.08, 5.72, 11.83, 19.34, 28.68, 37.99, 47.99, 56.76, 65.38, 72.28, 78.68, 83.46, 87.70, 90.70, 93.28, 95.04, 96.50, 97.47, 98.25, 98.75, 99.16, 99.41, 99.61},
                                {0.00, 0.06, 1.26, 3.92, 8.29, 14.70, 22.19, 31.17, 39.99, 49.40, 57.63, 65.76, 72.32, 78.45, 83.09, 87.25, 90.25, 92.85, 94.65, 96.17, 97.19, 98.03, 98.58, 99.02, 99.30},
                                {0.00, 0.03, 0.79, 2.55, 5.94, 10.72, 17.33, 24.70, 33.37, 41.75, 50.65, 58.43, 66.14, 72.40, 78.28, 82.79, 86.87, 89.85, 92.46, 94.29, 95.86, 96.92, 97.81, 98.40, 98.88},
                                {0.00, 0.01, 0.48, 1.73, 4.08, 7.96, 13.04, 19.73, 26.97, 35.34, 43.33, 51.79, 59.17, 66.51, 72.50, 78.17, 82.55, 86.55, 89.50, 92.11, 93.96, 95.57, 96.67, 97.60, 98.23},
                                {0.00, 0.01, 0.30, 1.11, 2.88, 5.68, 9.96, 15.22, 21.94, 29.03, 37.11, 44.76, 52.83, 59.87, 66.88, 72.63, 78.10, 82.36, 86.27, 89.19, 91.79, 93.67, 95.30, 96.43, 97.41},
                                {0.00, 0.00, 0.18, 0.74, 1.94, 4.14, 7.32, 11.89, 17.28, 23.98, 30.90, 38.72, 46.06, 53.79, 60.52, 67.25, 72.78, 78.06, 82.21, 86.04, 88.92, 91.51, 93.39, 95.05, 96.21},
                                {0.00, 0.00, 0.11, 0.47, 1.35, 2.90, 5.49, 8.96, 13.75, 19.21, 25.87, 32.63, 40.20, 47.26, 54.68, 61.14, 67.61, 72.93, 78.06, 82.09, 85.84, 88.69, 91.26, 93.15, 94.82},
                                {0.00, 0.00, 0.07, 0.31, 0.90, 2.08, 3.96, 6.87, 10.59, 15.54, 21.03, 27.62, 34.23, 41.57, 48.38, 55.52, 61.73, 67.96, 73.10, 78.07, 82.00, 85.68, 88.48, 91.03, 92.92},
                                {0.00, 0.00, 0.04, 0.20, 0.62, 1.44, 2.92, 5.08, 8.27, 12.18, 17.25, 22.75, 29.27, 35.72, 42.84, 49.42, 56.30, 62.29, 68.30, 73.28, 78.10, 81.93, 85.53, 88.30, 90.83},
                                {0.00, 0.00, 0.02, 0.13, 0.41, 1.02, 2.07, 3.83, 6.25, 9.67, 13.74, 18.89, 24.38, 30.80, 37.11, 44.03, 50.39, 57.04, 62.83, 68.64, 73.46, 78.15, 81.89, 85.42, 88.14},
                                {0.00, 0.00, 0.02, 0.08, 0.28, 0.70, 1.51, 2.79, 4.80, 7.44, 11.07, 15.25, 20.46, 25.92, 32.25, 38.41, 45.15, 51.31, 57.75, 63.34, 68.97, 73.65, 78.21, 81.86, 85.32},
                                {0.00, 0.00, 0.01, 0.05, 0.18, 0.49, 1.06, 2.07, 3.57, 5.82, 8.65, 12.44, 16.71, 21.96, 27.38, 33.62, 39.64, 46.19, 52.18, 58.41, 63.84, 69.29, 73.84, 78.28, 81.85},
                                {0.00, 0.00, 0.01, 0.03, 0.12, 0.33, 0.76, 1.49, 2.70, 4.40, 6.86, 9.86, 13.79, 18.13, 23.39, 28.77, 34.91, 40.80, 47.19, 53.00, 59.05, 64.31, 69.61, 74.03, 78.36},
                                {0.00, 0.00, 0.00, 0.02, 0.08, 0.23, 0.53, 1.09, 1.98, 3.39, 5.27, 7.92, 11.06, 15.11, 19.50, 24.77, 30.10, 36.14, 41.90, 48.13, 53.78, 59.66, 64.77, 69.92, 74.23},
                                {0.00, 0.00, 0.00, 0.01, 0.05, 0.15, 0.38, 0.77, 1.48, 2.53, 4.12, 6.17, 8.99, 12.26, 16.39, 20.83, 26.09, 31.36, 37.30, 42.94, 49.02, 54.52, 60.25, 65.22, 70.23},
                                {0.00, 0.00, 0.00, 0.01, 0.03, 0.11, 0.26, 0.56, 1.07, 1.92, 3.12, 4.89, 7.10, 10.06, 13.44, 17.65, 22.11, 27.35, 32.57, 38.42, 43.93, 49.87, 55.23, 60.81, 65.65}};
    
    // Decides if the bot is an an aggressive phase
    boolean aggro;
    
    // Stores the player's neighboring tiles
    Set<Province> neighbors = new HashSet<Province>();

    // Stores the owned provinces
    Set<Province> owned = new HashSet<Province>();

    // Stores the edges of player's territory
    Set<Province> borders = new HashSet<Province>();

    // Stores the command for the next attack
    AttackCommand nextAttack;

    Dictionary<String, Double> weights;

    

    public Zach_Player()
    {
        super();
        this.weights = getWeights();
    }

    /**
     * @return Method to tell player their turn is about to start. Intended to let player initialize variables and logic.
     */
    public void beginTurn(Snapshot s){
        //Do setup logic
        neighbors = new HashSet<Province>();
        owned = new HashSet<Province>();
        borders = new HashSet<Province>();

        setBorders(s);
        nextAttack = checkAggression();
    }

    public void setBorders(Snapshot s)
    {
        
        Player myPlayer = s.getMyPlayer();
        owned = myPlayer.getTerritory();
        for(Province province : owned){
            Set<Province> adjacent = province.getAdjacent();
            for(Province target : adjacent){
                if(target.getOwner() != myPlayer){
                    neighbors.add(target);
                    borders.add(province);
                }
            }
        }
        // System.out.println("Owned provinces" + owned);
        // System.out.println("Neighbors:" + neighbors);
        // System.out.println("borders" + borders);
    }

    public AttackCommand checkAggression()
    {
        double bestAttackWeight = 0;
        AttackCommand retAttack = null;
        aggro = false;
        Iterator<Province> iter = borders.iterator();
        Province mostTroop = null;
        if(!borders.isEmpty())
        {
            mostTroop = iter.next();
            while(iter.hasNext())
            {
                Province prov = iter.next();
                if (mostTroop.getNumSoldiers() < prov.getNumSoldiers())
                {
                    mostTroop = prov;
                }
            }
        }
        for(Province province : borders)
        {
            Set<Province> adjacent = province.getAdjacent();
            for(Province target : adjacent)
            {
                if(neighbors.contains(target))
                {
                    int targetSoldiers = target.getNumSoldiers();
                    int mySoldiers = province.getNumSoldiers();
                    double winPerc;
                    if (targetSoldiers < 26 && mySoldiers < 26)
                        winPerc = winPercentages[targetSoldiers-1][mySoldiers-1];
                    else
                        winPerc = calculateWinProbability(targetSoldiers, mySoldiers);

                    double weight = winPerc + weights.get(target.getName());
                    if (mostTroop != null && province.equals(mostTroop))
                        weight+=10;

                    if(weight > 60 && weight > bestAttackWeight && province.getNumSoldiers() > 3)
                    {
                        if(!aggro)
                            aggro = true;
                        bestAttackWeight = weight;
                        retAttack = new AttackCommand(3, province, target);
                    }
                }
            }
        }
        return retAttack;
    }

    // Calculate the probability of winning for an attack
    public static double calculateWinProbability(int attackerTroops, int defenderTroops) {
        // Base win chance for equal troops is 45% (out of 100)
        double baseWinChance = 53.0;

        // Each troop difference adjusts the win chance by 2% (out of 100)
        double troopDifferenceFactor = 2.0;

        // Calculate the troop difference
        int troopDifference = attackerTroops - defenderTroops;

        // Adjust win chance based on the troop difference
        double winProbability = baseWinChance + (troopDifferenceFactor * troopDifference);

        // Ensure the win probability is bounded between 0 and 100
        if (winProbability < 0) {
            winProbability = 0;
        } else if (winProbability > 100) {
            winProbability = 100;
        }

        return winProbability;
    }

    public Dictionary<String,Double> getWeights()
    {
        Dictionary<String, Double> weights = new Hashtable<String, Double>();
        
        // North America
        weights.put("Alaska", 2.0);
        weights.put("Northwest_Territory", -2.0);
        weights.put("Greenland", 4.0);
        weights.put("Alberta", -4.0);
        weights.put("Ontario", 0.0);
        weights.put("Eastern_Canada", 0.0);
        weights.put("Western_United_States", -2.0);
        weights.put("Eastern_United_States", -2.0);
        weights.put("Central_America", 4.0);

        // South America
        weights.put("Venezuela", 4.0);
        weights.put("Peru", 0.0);
        weights.put("Brazil", 6.0);
        weights.put("Argentina", -2.0);

        // Europe
        weights.put("Iceland", 4.0);
        weights.put("Scandinavia", 2.0);
        weights.put("Russia", 8.0);
        weights.put("United_Kingdom", 2.0);
        weights.put("Northern_Europe", 0.0);
        weights.put("Western_Europe", 2.0);
        weights.put("Southern_Europe", 4.0);

        // Africa
        weights.put("West_Africa", 6.0);
        weights.put("Egypt", 4.0);
        weights.put("East_Africa", 0.0);
        weights.put("Central_Africa", -2.0);
        weights.put("South_Africa", -2.0);
        weights.put("Madagascar", -4.0);

        // Asia
        weights.put("Ural", 6.0);
        weights.put("Siberia", 0.0);
        weights.put("Yakutsk", 2.0);
        weights.put("Kamchatka", 8.0);
        weights.put("Irkutsk", 0.0);
        weights.put("Mongolia", 2.0);
        weights.put("Japan", 0.0);
        weights.put("Afghanistan", 6.0);
        weights.put("China", 4.0);
        weights.put("India", 4.0);
        weights.put("Middle_East", 8.0);
        weights.put("Southeast_Asia", 4.0);

        // Australia
        weights.put("Indonesia", 2.0);
        weights.put("New_Guinea", -2.0);
        weights.put("Western_Australia", -2.0);
        weights.put("Eastern_Australia", -2.0);

        return weights;
    }

    /**
     * @return //Method to tell player their turn is about to end. Intended to let player clean up variables and logic.
     */
    public void endTurn(Snapshot s){
        //Do ending logic
        aggro = false;
        nextAttack = null;
    }

    /**
     * @return //Asks player to place troops. Should return [int numTroopstoPlace, Province destination]
     */
    public Set<DeployCommand> draftPhase(Snapshot s, int numTroopstoPlace){
        // Set of deploy commands to be returned
        Set<DeployCommand> deployCommands = new HashSet<DeployCommand>();

        // // Provinces that only have two units
        // Set<Province> twoUnit = new HashSet<Province>();
        // // Provinces that only have one unit
        // Set<Province> oneUnit = new HashSet<Province>();
        
        // // How many troops are left to place
        int troopsLeft = numTroopstoPlace;

        // for(Province province : borders)
        // {
        //     if(province.getNumSoldiers() == 2)
        //     {
        //         twoUnit.add(province);
        //     }
        //     else if(province.getNumSoldiers() == 1)
        //     {
        //         oneUnit.add(province);
        //     }
        // }

        // Iterator<Province> iter = oneUnit.iterator(); 
        // while(troopsLeft > 0 && iter.hasNext())
        // {
        //     Province curr = iter.next();
        //     deployCommands.add(new DeployCommand(1, curr));
        //     troopsLeft--;
        //     twoUnit.add(curr);
        // }
        // iter = twoUnit.iterator();
        // while(troopsLeft > 0 && iter.hasNext())
        // {
        //     Province curr = iter.next();
        //     deployCommands.add(new DeployCommand(1, curr));
        //     troopsLeft--;
        // }
        if(troopsLeft > 0)
        {
            if(aggro)
                deployCommands.add(new DeployCommand(troopsLeft, nextAttack.getAttackingProvince()));
            else
                deployCommands.add(new DeployCommand(troopsLeft, borders.iterator().next()));
        }

        return deployCommands;
    }

    /**
     * @return //Asks player to perform attack. Should return [int numTroopstoUse, Province attackingProvince, Province defendingProvince]
     */
    public AttackCommand attackPhase(Snapshot s){
        //System.out.println(aggro);
        if (nextAttack != null) {
            //System.out.println(nextAttack.getAttackingProvince().getName());
            //System.out.println(nextAttack.getDefendingProvince().getName());   
            return nextAttack;
        }
        return null;
        // if(aggro)
        //     return nextAttack;
        // return null;
    }

    /**
     * @return //Asks player to move troops. Should return [int numTroopstoMove, Province source, Province destination]
     */
    public MoveCommand movePhase(Snapshot s){
        MoveCommand command = new MoveCommand(0, null, null);
        if(nextAttack != null && nextAttack.getDefendingProvince().getOwner() == s.getMyPlayer())
        {
            command = new MoveCommand(nextAttack.getAttackingProvince().getNumSoldiers()-1, nextAttack.getDefendingProvince(), nextAttack.getAttackingProvince());
        }
        for(Province province : owned)
        {
            if(!borders.contains(province) && province.getNumSoldiers() > 1)
            {
                for(Province possBord : province.getAdjacent())
                {
                    // if the province I'm looking at isn't a border, continue
                    if (borders.contains(possBord))
                        continue;
                    // If there is no command and this tile is a border, set the move command to move units from teh current tile to this one
                    else if (command == null)
                        command = new MoveCommand(province.getNumSoldiers()-1, possBord, province);
                    else if (province.getNumSoldiers() == command.getnumMovingTroops())
                    {
                        int threats = 0;
                        for(Province possThreat : possBord.getAdjacent())
                        {
                            if (possThreat.getOwner() != s.getMyPlayer())
                            {
                                threats += possThreat.getNumSoldiers();
                            }
                        }
                        int currThreats = 0;
                        for(Province possThreat : command.getTargetProvince().getAdjacent())
                        {
                            if (possThreat.getOwner() != s.getMyPlayer())
                            {
                                currThreats += possThreat.getNumSoldiers();
                            }
                        }
                        if (threats > currThreats)
                        {
                            command = new MoveCommand(province.getNumSoldiers()-1, possBord, province);
                        }
                    }    
                    else if(province.getNumSoldiers() > command.getnumMovingTroops())
                        command = new MoveCommand(province.getNumSoldiers()-1, possBord, province);
                }
            }
        }
        return command;
    }

    public void attackPhaseResults(Snapshot s, int[] battleResults){
        //Check if attack went well
        //Change logic before attackPhase gets called again
        //return;
        if (battleResults[1] == 0 && !s.getMyPlayer().getTerritory().contains(nextAttack.getDefendingProvince()))
        {
            return;
        }
        // else {
        //     setBorders(s);
        //     nextAttack = checkAggression();
        // }
    }


    /**
     * @return //Asks player how many troops to move into conquered territory. Should return [int numTroops]
     */
    public int moveAfterConquer(Snapshot s, Province attackingProvince, Province defendingProvince){
        // int transferXTroops = 1;
        // return(transferXTroops);
        boolean moveall = true;
        Iterator<Province> iter = attackingProvince.getAdjacent().iterator();
        while (iter.hasNext() && moveall)
        {
            if(iter.next().getOwner() != s.getMyPlayer())
            {
                moveall = false;
            }
        }
        if (moveall)
            return attackingProvince.getNumSoldiers() - 1;
        else
        {
            if (attackingProvince.getNumSoldiers() > 3 && attackingProvince.getNumSoldiers() < 6)
            {
                if (attackingProvince.getNumSoldiers() < 6)
                    return attackingProvince.getNumSoldiers() - 3;
                else 
                    return attackingProvince.getNumSoldiers() / 2;
            }
            else
            {
                return 1;
            }
        }
    }

    /**
     * @return //Asks player to turn in set of cards. If required, must be [Card X, Card Y, Card Z]. If not required, any invalid input will count as passing on your turn.
     */
    public Set<Card> turnInCards(Snapshot s, boolean required, int currentTroops){
        Set<Card> myCards = s.getUserHand();

        // 0 = infantry, 1 = cavalry, 2 = artillery, 3 = wild
        int[] types = new int[4];

        for(Card card : myCards)
        {
            if(card != null){
                types[card.getType()] ++;
            }
            
        }

        // -1 = invalid, 0 = infantry, 1 = cavalry, 2 = artillery, 3 = one of each, 4 = infantry w/ wild, 5 = cavalry w/ wild, 6 = artillery w/ wild, 7 = one of each w/ wild
        int type = -1;
        // Counts how many types of cards have at least one
        int poss3 = 0;
        // Keeps track of which type of card I have the most of
        int most = 0;
        // Checks if 3 infantry, cavalry, or artillery are an option
        for(int i = 0; i < 3 && type == -1; i++)
        {
            if(types[i] > 2)
            {
                type = i;
                return makeSet(type, myCards);
            }
            else if (types[i] > 0)
            {
                poss3++;
            }
            if (types[most] < types[i])
                most = i;
        }
        
        // Checks if one of each is an option
        if (poss3 == 3)
            return makeSet(3, myCards);
        
        // If the number of cards that I have the most of, plus the number of wilds I have is 3 or more, then that type of cards plus wilds can be turned in
        if ( ( types[most] + types[3] ) > 2 )
            return makeSet(most, myCards);
        
        // If there are 5 cards, one of each including wilds *must* be an option if the previous are not
        if(myCards.size() >= 5)
        {
            return makeSet(3, myCards);
        }
        return null;
    }

    // 0 = infantry, 1 = cavalry, 2 = artillery, 3 = one of each
    public Set<Card> makeSet(int type, Set<Card> hand)
    {
        Set<Card> set = new HashSet<Card>();
        Set<Card> wilds = new HashSet<Card>();
        int[] types = new int[3];
        if(type == 0)
            types[0] = 3;
        else if (type == 1)
            types[1] = 3;
        else if (type == 2)
            types[2] = 3;
        else
        {
            types[0] = 1;
            types[1] = 1;
            types[2] = 1;
        }
        
        Iterator<Card> iter = hand.iterator();
        while( ( types[0] > 0 || types[1] > 0 || types[2] > 0 ) && iter.hasNext() )
        {
            Card currCard = iter.next();
            int cardType = 0;
            if(currCard != null){
                cardType = currCard.getType();
            }
            
            if(cardType == 3)
            {
                wilds.add(currCard);
                continue;
            }
            else if(types[cardType] > 0)
            {
                set.add(currCard);
                types[cardType]--;
            }
        }
        iter = wilds.iterator();
        while(types[0] > 0)
        {
            set.add(iter.next());
            types[0]--;
        }
        while(types[1] > 0)
        {
            set.add(iter.next());
            types[1]--;
        }
        while(types[2] > 0)
        {
            set.add(iter.next());
            types[2]--;
        }
        return set;
    }

    /**
     * @return This lets you choose a custom player name to show on the leaderboard.
     * 
     */
    public String getCustomName(){
        String name = "Zach's Bot";
        //Return null if you want to be assigned a number 1-4 randomly. 
        return name;
    }

    /**
     * @return This lets you choose a custom player color to show on the map. Needs to return an array of dimension two.
     * The two colors are used as the corners of a gradient, so if you want to be one solid color, return the same color twice.
     * 
     */
    public Color[] getCustomColor(){
        Color[] colors = {Color.BLACK, Color.BLACK};
        //Return null if you want to be assigned a random color.
        return colors;
    }

    /**
     * @return This lets you choose a custom player profile to show on the map. Needs to be a url.
     * 
     */
    public String getCustomProfile(){
        String image = "https://waapple.org/wp-content/uploads/2021/06/Variety_Cosmic-Crisp-transparent-658x677.png";
        //Return null if you don't want a profile picture.
        return image;
    }
}
