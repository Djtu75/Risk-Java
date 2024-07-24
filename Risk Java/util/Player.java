package util;

import java.util.HashSet;
import java.util.Set;
import participant.PlayerLogic;

public class Player {

    private Set<Province> territory = new HashSet<Province>();
    private int numsoldiers;
    private Set<Card> cards = new HashSet<Card>();
    private PlayerLogic logic;
    private String name;

    public Player(String name, PlayerLogic logic){
        this.name = name;
        this.logic = logic;
    }

    protected void setTerritory(Set<Province> newTerritory){
        for(Province province: newTerritory){
            province.setOwner(this);
        }
        territory = newTerritory;
    }

    protected void addTerritory(Province newProvince){
        if(newProvince.getOwner() != null){
            newProvince.getOwner().removeTerritory(newProvince);
        }
        newProvince.setOwner(this);
        territory.add(newProvince);
    }

    protected void removeTerritory(Province remProvince){
        territory.remove(remProvince);
    }

    public int getNumSoldiers(){
        return numsoldiers;
    }

    public int getNumCards(){
        return cards.size();
    }

    protected Set<Card> getCards(){
        return cards;
    }

    public Set<Province> getTerritory() {
        return territory;
    }

    protected void setNumsoldiers(int numsoldiers) {
        this.numsoldiers = numsoldiers;
    }

    protected void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    protected void addCard(Card card) {
        this.cards.add(card);
    }

    protected PlayerLogic getLogic() {
        return logic;
    }

    protected void setLogic(PlayerLogic logic) {
        this.logic = logic;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

}
