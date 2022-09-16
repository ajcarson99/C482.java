package model;

public class InHouse extends Part{
    private static int machineId;

    /**
     *  @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param machineId
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }
    public InHouse(){
        this(0,"",0.0,0,0,0,0);
    }

    /**
     *
     * @return machineID
     */
    public static int getMachineId(){
        return machineId;
    }

    /**
     *
     * @param machineId
     */
    public void setMachineId(int machineId){
        this.machineId = machineId;
    }
}
