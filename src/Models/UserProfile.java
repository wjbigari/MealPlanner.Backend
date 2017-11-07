package Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import static Models.UserProfile.gender.FEMALE;
import static Models.UserProfile.gender.MALE;

public class UserProfile implements Serializable {

    String username = "will";
    String name;
    int age,height,weight;
    gender gen;

    public enum gender{MALE, FEMALE}

    public UserProfile(){
        this.username = "will";
        this.name = "Will";
        this.age = 1;
        this.height = 2;
        this.weight =3;
        this.gen = MALE;
    }

    public UserProfile(String username, String name, int age, int height, int weight, gender gen){
        this.username = username;
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gen = gen;
    }

    public UserProfile(JSONObject jobject) throws JSONException {
        this.username = jobject.getString("username");
        this.name = jobject.getString("name");
        this.age = jobject.getInt("age");
        this.height = jobject.getInt("height");
        this.weight = jobject.getInt("weight");
        switch(jobject.getString("gender").toUpperCase()){
            case "MALE":
                this.gen = MALE;
                break;
            case "FEMALE":
                this.gen = FEMALE;
                break;
        }

    }

    public String getName(){
        return name;
    }

    public String getAge(){
        return Integer.toString(age);
    }

    public String getWeight(){
        return Integer.toString(weight);
    }

    public String getHeight(){
        return Integer.toString(height);
    }
    public String getUsername(){
        return this.username;
    }
    public gender getGen(){
        return gen;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAge(int age){
        this.age = age;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public void setGen(gender gen){
        this.gen = gen;
    }

    public String getHeighString(){
        int feet = this.height/12;
        int inches = this.height % 12;
        return "" + feet + "' " + inches + " \"";
    }
    public JSONObject toJSON() throws JSONException {
        JSONObject jobject = new JSONObject();
        jobject.put("username", this.username);
        jobject.put("name", this.name);
        jobject.put("age", this.age);
        jobject.put("height", this.height);
        jobject.put("weight", this.weight);
        jobject.put("gender", this.gen.name());
        return jobject;
    }
}
