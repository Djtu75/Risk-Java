package util;

import java.util.HashSet;
import java.util.Set;

public class Province{
    
    private String name;
    private Player owner;
    private int numsoldiers;
    private Set<Province> adjacent = new HashSet<Province>();
    private Continent continent;

    public Province(String name, Player ownership, int numsoldiers){
        this.name = name;
        this.owner = ownership;
        this.numsoldiers = numsoldiers;
        adjacent = new HashSet<Province>();
    }

    public Province(String name, Player ownership, int numsoldiers, Set<Province> adjacents){
        this.name = name;
        this.owner = ownership;
        this.numsoldiers = numsoldiers;
        adjacent = adjacents;
        for(Province province: adjacents){
            province.addAdjacent(this);
        }
    }

    public Province(String name, Player ownership, int numsoldiers, Set<Province> adjacents, Continent continent){
        this.name = name;
        this.owner = ownership;
        this.numsoldiers = numsoldiers;
        adjacent = adjacents;
        this.continent = continent;
        continent.addProvince(this);
    }

    @Override
    public String toString() {
        String returnStr = name + "[owner=" + owner.getName() + "(" + owner + ")" + ", numsoldiers=" + numsoldiers + ", adjacent=(";
        for(Province province: adjacent){
            returnStr += province.getName() + ", ";
        }
        returnStr = returnStr.substring(0, returnStr.length()-2);
        returnStr += "), continent=" + ((continent == null) ? "null" : continent.getName()) + "]";
        return returnStr;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public Player getOwner() {
        return owner;
    }

    protected void setOwner(Player newOwner) {
        this.owner = newOwner;
    }

    public int getNumSoldiers() {
        return numsoldiers;
    }

    protected void setNumsoldiers(int numsoldiers) {
        this.numsoldiers = numsoldiers;
    }

    protected void addSoldiers(int num) {
        this.numsoldiers += num;
    }

    public Set<Province> getAdjacent() {
        return adjacent;
    }

    protected void setAdjacent(Set<Province> adjacent) {
        for(Province province: adjacent){
            province.addAdjacent(this);
        }
        this.adjacent = adjacent;
    }

    protected void addAdjacent(Province province){
        province.addAdjacent(this, true);
        this.adjacent.add(province);
    }

    protected void addAdjacent(Province province, boolean callback){
        if(callback){
           this.adjacent.add(province); 
        } 
    }

    public Continent getContinent() {
        return continent;
    }

    protected void setContinent(Continent continent) {
        this.continent = continent;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result + numsoldiers;
        result = prime * result + ((continent == null) ? 0 : continent.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Province other = (Province) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (owner == null) {
            if (other.owner != null)
                return false;
        } else if (!owner.equals(other.owner))
            return false;
        if (numsoldiers != other.numsoldiers)
            return false;
        if (continent == null) {
            if (other.continent != null)
                return false;
        } else if (!continent.equals(other.continent))
            return false;
        return true;
    }
    
    
    
}
