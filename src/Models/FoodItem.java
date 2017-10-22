package Models;

public class FoodItem {
    private String name;
    private String info;

    public FoodItem(String name, String info){
        this.name = name;
        this.info = info;
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
        return name + d + info;
    }
}
