package model;

public class OutSourced extends Part{
    private String companyName;

    /**
     *  @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param companyName
     */
    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * getter for company Name
     * @return company Name
     */
    public String getCompanyName(){
        return companyName;
    }

    /**
     * the setter for company Name
     * @param companyName
     */
    public void setCompnayName(String companyName){
        this.companyName = companyName;
    }
}
