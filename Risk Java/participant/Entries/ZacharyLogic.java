package participant.Entries;

import participant.*;
import util.*;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.awt.Color;

public class ZacharyLogic extends PlayerLogic {

    Game mygame = null;
    Player myplayer = null;
    private boolean cardState = false, one1 = false;
    Set<Province> big3 = new HashSet<Province>();

    // public void initialize(Game mygame, Player myplayer){
    // this.mygame = mygame;
    // this.myplayer = myplayer;
    // }

    /**
     * @return Method to tell player their turn is about to start. Intended to let
     *         player initialize variables and logic.
     */
    public void beginTurn(Snapshot s) {
        // Do setup logic
        Player[] peeps = s.getPowerRankings();
        myplayer = s.getMyPlayer();
        
        if (peeps.length < 3 || peeps[peeps.length - 3].getNumSoldiers() < 1)
            one1 = true;
        Province one = null;
        Province two = null;
        Province three = null;
        System.out.println("a");
        for (Province owned : myplayer.getTerritory()) {
            System.out.println("b");
            if (three == null || owned.getNumSoldiers() > three.getNumSoldiers()) {
                three = owned;
                if (two == null || owned.getNumSoldiers() > two.getNumSoldiers()) {
                    three = two;
                    two = owned;
                    if (one == null || owned.getNumSoldiers() > one.getNumSoldiers()) {
                        two = one;
                        one = owned;
                    }
                }
            }
            System.out.println("c");
        }
        if (one != null)
            big3.add(one);
        if (two != null)
            big3.add(two);
        if (three != null)
            big3.add(three);

        cardState = false;
    }

    /**
     * @return //Method to tell player their turn is about to end. Intended to let
     *         player clean up variables and logic.
     */
    public void endTurn(Snapshot s) {
        // Do ending logic
        cardState = false;
        big3.clear();
    }

    /**
     * @return //Asks player to place troops. Should return [int numTroopstoPlace,
     *         Province destination]
     */
    public Set<DeployCommand> draftPhase(Snapshot s, int numTroopstoPlace) {
        System.out.println("d");
        myplayer = s.getMyPlayer();
        Set<DeployCommand> returnArray = new HashSet<DeployCommand>();
        Set<Province> myterritory = myplayer.getTerritory();
        Province bigBig = big3.iterator().next(), smallBig = null;
        int[] mexicans;
        int itt = 0;
        if (myterritory.size() <= 0) {
            return returnArray;
        }

        for (Province big : big3) {
            if (bigBig.getNumSoldiers() < big.getNumSoldiers()) {
                bigBig = big;
            }
        }
        for (Province big : big3) {
            if (big.getNumSoldiers() * 2 < bigBig.getNumSoldiers()) {
                smallBig = big;
            }
        }
        if (numTroopstoPlace % 3 == 2) {
            mexicans = new int[] { numTroopstoPlace / 3, numTroopstoPlace / 3 + 1, numTroopstoPlace / 3 + 1 };
        } else if (numTroopstoPlace % 3 == 1) {
            mexicans = new int[] { numTroopstoPlace / 3, numTroopstoPlace / 3, numTroopstoPlace / 3 + 1 };
        } else {
            mexicans = new int[] { numTroopstoPlace / 3, numTroopstoPlace / 3, numTroopstoPlace / 3 };
        }
        for (Province big : big3) {
            if (smallBig != null && smallBig.equals(big)) {
                returnArray.add(new DeployCommand(numTroopstoPlace, big));
            } else if (smallBig == null) {
                returnArray.add(new DeployCommand(mexicans[itt], big));
                itt++;
                System.out.println(myplayer.getName() + " placed 1 troop in " + big.getName());
            }
        }
        return returnArray;
    }

    /**
     * @return //Asks player to perform attack. Should return [int numTroopstoUse,
     *         Province attackingProvince, Province defendingProvince]
     */
    public AttackCommand attackPhase(Snapshot s) {
        myplayer = s.getMyPlayer();
        int useXtroops = 3, bUseXtroops = 0, big3p = 0, small = 0, smallMax = 0, bSmallMax = 0;
        Province attackingProvince = null, bAttackingProvince = null;
        Province defendingProvince = null, bDefendingProvince = null;
        Set<Province> victim = new HashSet<Province>();
        Set<Continent> conts = s.getWorld().getContinents();
        Continent contarget = null;
        HashSet<Province> edward = new HashSet<Province>();
        HashSet<Province> path = new HashSet<Province>();
        Object[] egg;

        for (Province big : big3)
            big3p += big.getNumSoldiers();

        for(Continent cont : conts){
            for(Province prov : cont.getProvinces()){
                if(!prov.getOwner().equals(s.getMyPlayer())){
                    smallMax+=prov.getNumSoldiers();
                }
                else{
                    bSmallMax+=prov.getNumSoldiers()-1;
                }
            } 
            if(bSmallMax > smallMax*1.25)
                contarget = cont;
        }
        smallMax = 0;
        bSmallMax = 0;
        if(contarget != null){
            for(Province big : big3){
                for(Province border : big.getAdjacent()){
                    if(contarget.getProvinces().contains(border)){
                        attackingProvince = big;
                        defendingProvince = border;
                        useXtroops = big.getNumSoldiers()-1;
                        AttackCommand command = new AttackCommand(useXtroops, attackingProvince, defendingProvince);
                        return (command);
                    }
                }
            }
             
        }
        else if (cardState == false || one1) {
            for (Province big : big3) {
                for (Province border : big.getAdjacent()) {
                    small = 0;
                    if (!border.getOwner().equals(this.myplayer)
                            && border.getNumSoldiers() * 4 < big.getNumSoldiers()) {
                        for (Province target : border.getAdjacent()) {
                            if (!target.getOwner().equals(myplayer)
                                    && target.getNumSoldiers() * 4 < border.getNumSoldiers()) {
                                small += 1;
                            }
                        }
                        if (small > smallMax) {
                            System.out.println("smallMax Works " + smallMax);
                            smallMax = small;
                            attackingProvince = big;
                            defendingProvince = border;
                            useXtroops = big.getNumSoldiers() - 1;
                        }
                        small = 0;
                    } else if (!border.getOwner().equals(this.myplayer)) {
                        for (Province target : border.getAdjacent()) {
                            if (!target.getOwner().equals(myplayer)
                                    && target.getNumSoldiers() * 4 < border.getNumSoldiers()) {
                                small += 1;
                            }
                        }
                        if (small > bSmallMax) {
                            System.out.println("bsmallMax Works " + bSmallMax);
                            bSmallMax = small;
                            bAttackingProvince = big;
                            bDefendingProvince = border;
                            bUseXtroops = big.getNumSoldiers() - 1;
                        }
                        small = 0;
                    }
                }
            }
            if (smallMax > 0) {
                AttackCommand command = new AttackCommand(useXtroops, attackingProvince, defendingProvince);
                return (command);
            } else if (bSmallMax > 0) {
                AttackCommand command = new AttackCommand(bUseXtroops, bAttackingProvince, bDefendingProvince);
                return (command);
            }
            Province whatever = big3.iterator().next();
            AttackCommand command = new AttackCommand(whatever.getNumSoldiers() - 1, whatever,
                    whatever.getAdjacent().iterator().next());
            return command;
        } else {
            for (Player enemy : s.getPlayers()) {
                potato: {
                    if (enemy.getNumSoldiers() * 1.5 < big3p && enemy.getNumSoldiers() > 0) {
                        for (Province big : big3) {
                            for (Province border : big.getAdjacent())
                                if (border.getOwner().equals(enemy)) {
                                    victim.addAll(Game.possibleDestinations(border));
                                    edward.addAll(Game.possibleDestinations(border));
                                }
                            for (Province veectim : edward)
                                bSmallMax += veectim.getNumSoldiers();
                            edward.clear();
                            if (bSmallMax * 1.5 > big.getNumSoldiers())
                                break potato;
                        }
                        if (victim.containsAll(enemy.getTerritory())) {
                            for (Province big : big3) {
                                for (Province border : big.getAdjacent()) {
                                    if (border.getOwner().equals(enemy)) {
                                        edward.addAll(Game.possibleDestinations(border));
                                        path = killEdward(edward, big);
                                    }
                                }
                                useXtroops = big.getNumSoldiers() - 1;
                                attackingProvince = big;
                                egg = path.toArray();
                                defendingProvince = (Province) egg[0];
                                AttackCommand command = new AttackCommand(useXtroops, attackingProvince,
                                        defendingProvince);
                                return (command);
                            }
                        }
                    }
                }
            }
        }

        AttackCommand command = new AttackCommand(useXtroops, attackingProvince, defendingProvince);
        return (command);

    }

    public HashSet<Province> killEdward(HashSet<Province> edward, Province big) {
        HashSet<Province> visited = new HashSet<Province>();
        visited.add(big);
        Set<Province> start = big.getAdjacent();
        start.retainAll(edward);
        for (Province next : start) {
            killEdward(edward, next, visited);
            if (edward.size() + 1 != visited.size())
                visited.remove(next);
        }
        return visited;
    }

    public HashSet<Province> killEdward(HashSet<Province> edward, Province big, HashSet<Province> visited) {
        visited.add(big);
        Set<Province> start = big.getAdjacent();
        start.retainAll(edward);
        for (Province next : start) {
            killEdward(edward, next, visited);
            if (edward.size() + 1 != visited.size())
                visited.remove(next);
        }
        return visited;
    }

    /**
     * @return //Asks player to move troops. Should return [int numTroopstoMove,
     *         Province source, Province destination]
     */
    public MoveCommand movePhase(Snapshot s) {
        myplayer = s.getMyPlayer();
        int moveXTroops = 1, small = 0, smallMin = 100, smallMax = 0;
        Province source = null, destination = null;

        for (Province big : this.myplayer.getTerritory()) {
            for (Province bigger : big3)
                if (big.getNumSoldiers() > bigger.getNumSoldiers() / 4 && !big3.contains(big))
                    if (Game.possibleDestinations(big).contains(bigger)) {
                        source = big;
                        destination = bigger;
                        moveXTroops = big.getNumSoldiers() - 2;
                        return new MoveCommand(moveXTroops, destination, source);
                    }
        }

        for (Province big : big3) {
            for (Province border : big.getAdjacent()) {
                if (!border.getOwner().equals(this.myplayer))
                    if (border.getNumSoldiers() * 4 < big.getNumSoldiers())
                        small++;
            }
            if (small < smallMin) {
                smallMin = small;
                source = big;
            }
            small = 0;
        }
        for (Province dest : Game.possibleDestinations(source)) {
            for (Province border : dest.getAdjacent()) {
                if (!border.getOwner().equals(this.myplayer))
                    if (border.getNumSoldiers() * 4 < dest.getNumSoldiers())
                        small++;
            }
            if (small > smallMax && !big3.contains(dest)) {
                smallMax = small;
                destination = dest;
            }
        }
        moveXTroops = source.getNumSoldiers() - 2;

        MoveCommand command = new MoveCommand(moveXTroops, destination, source);
        return (command);
    }

    public void attackPhaseResults(Snapshot s, int[] battleResults) {
        // Check if attack went well
        // Change logic before attackPhase gets called again
        return;
    }

    /**
     * @return //Asks player how many troops to move into conquered territory.
     *         Should return [int numTroops]
     */
    public int moveAfterConquer(Snapshot s, Province attackingProvince, Province defendingProvince) {
        myplayer = s.getMyPlayer();
        int atk = 0, def = 0;
        this.cardState = true;
        for (Province target : attackingProvince.getAdjacent()) {
            if (target.getOwner() != this.myplayer && target.getNumSoldiers() * 4 < attackingProvince.getNumSoldiers())
                atk += 1;
        }
        for (Province target : defendingProvince.getAdjacent()) {
            if (target.getOwner() != this.myplayer && target.getNumSoldiers() * 4 < defendingProvince.getNumSoldiers())
                def += 1;
        }
        if (atk > def)
            return 1;
        return (attackingProvince.getNumSoldiers() - 1);

    }

    /**
     * @return //Asks player to turn in set of cards. If required, must be [Card X,
     *         Card Y, Card Z]. If not required, any invalid input will count as
     *         passing on your turn.
     */
    public Set<Card> turnInCards(Snapshot s, boolean required, int currentTroops) {
        myplayer = s.getMyPlayer();
        if (required) {
            Set<Card> mycards = s.getUserHand();
            // Some Logic to pick which 3 to turn in
            Iterator<Card> iter = mycards.iterator();

            Card card1 = iter.next();
            Card card2 = iter.next();
            Card card3 = iter.next();
            Set<Card> returnSet = new HashSet<Card>(Arrays.asList(card1, card2, card3));
            return (returnSet);
        } else if (one1) {
            Set<Card> mycards = s.getUserHand();

            Set<Card> set = (mycards);
            return (set);
        } else {
            return (new HashSet<Card>(Arrays.asList()));
        }
    }

    /**
     * @return This lets you choose a custom player name to show on the leaderboard.
     * 
     */
    public String getCustomName() {
        String name = "Zachary";
        // Return null if you want to be assigned a number 1-4 randomly.
        return name;
    }

    /**
     * @return This lets you choose a custom player color to show on the map. Needs
     *         to return an array of dimension two.
     *         The two colors are used as the corners of a gradient, so if you want
     *         to be one solid color, return the same color twice.
     * 
     */
    public Color[] getCustomColor() {
        Color[] colors = { new Color(153, 209, 255), new Color(153, 209, 255) };
        // Return null if you want to be assigned a random color.
        return colors;
    }

    /**
     * @return This lets you choose a custom player profile to show on the map.
     *         Needs to be a url.
     * 
     */
    public String getCustomProfile() {
        String image = "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/ce9484b3-9994-4b30-ab7c-f461ddcef0a0/dex2bvj-18376cdd-d749-4e5d-a02f-d7e8ff666247.png/v1/fill/w_721,h_635,q_80,strp/snorlax_pixel_art_by_nimowerytheking_dex2bvj-fullview.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9NjM1IiwicGF0aCI6IlwvZlwvY2U5NDg0YjMtOTk5NC00YjMwLWFiN2MtZjQ2MWRkY2VmMGEwXC9kZXgyYnZqLTE4Mzc2Y2RkLWQ3NDktNGU1ZC1hMDJmLWQ3ZThmZjY2NjI0Ny5wbmciLCJ3aWR0aCI6Ijw9NzIxIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmltYWdlLm9wZXJhdGlvbnMiXX0.PTnFoMU9hXeNGnBbBbQXClkPGsk1Bok3IiGWHdK_jLs";
        // Return null if you don't want a profile picture.
        return image;
    }

}
