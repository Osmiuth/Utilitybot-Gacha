import java.util.Random;

/**
 * EXPLANATION:
 * 
 * class Game is an object that instantiates integers Money and Time. A setter and getter method is created to accommodate changing/accessing data.
 * class Wheel is an object that instantiates Payoff, PayOffChance, and CurrentChance. A setter and getter method for PayOff and PayOffChance is created for any user-defined
 * modifications.
 * 
 * 
 */

class Game {
    private int Money = 0;
    private int Time = 0;

    public int getMoney(){
        return this.Money;
    }

    public void setMoney(int n){
        this.Money = n;
    }

    public int getTime(){
        return this.Time;
    }

    public void setTime(int n){
        this.Time = n;
    }
}


class Wheel {
    private int PayOff = 0;
    private float PayOffChance = 0;
    private float CurrentChance = 0;
    Random rand = new Random();

    public int getPayOff(){ //Getter method
        return this.PayOff;
    }

    public void setPayOff(int n){ //Setter method
        this.PayOff = n;
    }

    public float getPayOffChance(){ //Getter method
        return this.PayOffChance;
    }

    public void setPayOffChance(float n){ //Setter method
        this.PayOffChance = n;
    }

    public boolean roll() { //Rolling function 
        this.CurrentChance = rand.nextFloat(100);
        return this.CurrentChance >= PayOffChance *100;
    }

}

class MachineBot {

/**
 * This is where we put the utility-based agent's functions here.
 * 
 * 
 */


}

public class main {

public static void main(String[] args){
    /**
     * 
     * Added instances for testing purposes.
     * 
     */
    Game game = new Game();
    game.setMoney(100);
    game.setTime(3600);

    Wheel wheel1 = new Wheel();
    wheel1.setPayOff(10);
    wheel1.setPayOffChance((float) 0.65);
    System.out.println(wheel1.roll());
}

}

