package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Continent{
    private Set<Province> provinces;
    private String name;
    private int bonus;

    public Continent(String initName, int initBonus){
        provinces = new HashSet<Province>();
        this.name = initName;
        this.bonus = initBonus;
    }

    public Continent(String initName, int initBonus, Set<Province> initProvinces){
        setProvinces(initProvinces);
        this.name = initName;
        this.bonus = initBonus;
    }

    @Override
    public String toString() {
        String returnStr = name + " [provinces=(";
        for(Province province: provinces){
            returnStr += province.getName() + ", ";
        }
        returnStr = returnStr.substring(0, returnStr.length()-2);
        returnStr += "), bonus=" + bonus + "]";
        return returnStr;
    }

    public String getName() {
        return name;
    }

    public int getBonus(){
        return bonus;
    }

    public Set<Province> getProvinces() {
        //ArrayList<Province> returnArray = new ArrayList<Province>();
        return provinces;
        
    }

    protected void setProvinces(Set<Province> provinces) {
        for(Province province: provinces){
            province.setContinent(this);
        }
        this.provinces = provinces;
    }

    protected void addProvince(Province province){
        province.setContinent(this);
        this.provinces.add(province);
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public Set<Province> calculateAdjacent(){ //Returns a set of provinces that are adjacent to the continent
        Set<Province> adjacentProvinces = new HashSet<Province>();
        for (Province province : provinces) {
            Set<Province> temp = province.getAdjacent();
            for(Province adjacent: temp){
                adjacentProvinces.add(adjacent);
            }
        }
        adjacentProvinces.removeAll(provinces);

        return(adjacentProvinces);
    }

    public static void main(String[] args) {
        Province upperright = new Province("upperright", null, 0);
        Province upperleft = new Province("upperleft",null, 1);
        Province lowermiddle = new Province("lowermiddle",null, 2);
        Province middle = new Province("middle",null, 3);
        Province outsideright = new Province("outsideright",null, 4);
        Province outsideleft = new Province("outsideleft",null, 5);
        Province outsidedown = new Province("outsidedown",null, 6);
        Province outsidetop = new Province("outsidetop",null, 7);

        upperright.setAdjacent(new HashSet<Province>(Arrays.asList(outsideright, middle)));
        upperleft.setAdjacent(new HashSet<Province>(Arrays.asList(outsideleft, middle)));
        lowermiddle.setAdjacent(new HashSet<Province>(Arrays.asList(outsidedown, middle)));
        middle.setAdjacent(new HashSet<Province>(Arrays.asList(upperright, upperleft, lowermiddle)));
        outsideright.setAdjacent(new HashSet<Province>(Arrays.asList(upperright, outsideleft, outsidedown, outsidetop)));
        outsideleft.setAdjacent(new HashSet<Province>(Arrays.asList(upperleft, outsideright, outsidedown, outsidetop)));
        outsidedown.setAdjacent(new HashSet<Province>(Arrays.asList(outsideright, outsideleft, lowermiddle)));
        outsidetop.setAdjacent(new HashSet<Province>(Arrays.asList(outsideright, outsideleft)));
        Set<Province> testprovincesforcontinent = new HashSet<Province>(Arrays.asList(upperleft, upperright, lowermiddle, middle));
        Continent test = new Continent("guy", 2, testprovincesforcontinent);
        System.out.println(test);
        System.out.println(test.getProvinces());
        System.out.println(test.calculateAdjacent());

    }

    

}
