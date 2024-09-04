package util;

public class GraphicProvince {

    int[] dimensions;
    Province prov;

    public GraphicProvince(Province p, int[] d){
        dimensions = d;
        prov = p;
    }

    public int[] getDimensions() {
        return dimensions;
    }

    protected void setDimensions(int[] dimensions) {
        this.dimensions = dimensions;
    }

    public Province getProv() {
        return prov;
    }

    protected void setProv(Province prov) {
        this.prov = prov;
    }
    
}
