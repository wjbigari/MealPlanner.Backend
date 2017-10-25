package Models;

public class DBFoodItem {
    private String name;
    private String info;
    private String servingName;
    private String servingAmount;

    public DBFoodItem(String name, String info, String servingAmount, String servingName){
        this.name = name;
        //calories | carbs | proteins | fat | serving name
        this.info = info;
        this.servingName = servingName;
        this.servingAmount = servingAmount;
    }

    public String getName(){
        return name;
    }

    public String getInfo(){
        return info;
    }

    @Override
    public String toString(){
        String d = "~";
        return name + d + info + d + servingAmount + d + servingName ;
    }
}
