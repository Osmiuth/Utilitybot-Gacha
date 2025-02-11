import java.util.Random;
import java.util.LinkedList;

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

        public void roll(Game game) { //Rolling function
            game.setMoney(game.getMoney()-1);
            game.setTime(game.getTime()-10);
            this.CurrentChance = rand.nextFloat(100);
            if (this.CurrentChance <= PayOffChance *100){
                game.setMoney(game.getMoney()+this.PayOff);
            } 
        }
}

class MachineBot {
    private String action = "none";
    private LinkedList<Integer> LWheelA = new LinkedList<Integer>();
    private LinkedList<Integer> LWheelB = new LinkedList<Integer>();
    private int epoch = 5;
    private int tempA;
    private int tempB;

    public LinkedList<Integer> getListA() {
        return this.LWheelA;
    }

    public LinkedList<Integer> getListB() {
        return this.LWheelB;
    }

    public String getAction() {
        return this.action;
    }

    public void solve(Game game, Wheel wheelA, Wheel wheelB) {
        if (this.action.equals("preferred_A")) {
            LWheelA.add(game.getMoney());
            wheelA.roll(game);
        } else if (this.action.equals("preferred_B")) {
            LWheelB.add(game.getMoney());
            wheelB.roll(game);
        }

        if ((LWheelA.size() % 5 == 0) && (LWheelB.size() % 5 == 0) && (LWheelA.size() > 0) && (LWheelB.size() > 0)) {
            for (int j = LWheelA.size() - epoch; j < LWheelA.size(); j++) {
                this.tempA = this.tempA + LWheelA.get(j);
            }


            for (int k = LWheelB.size() - epoch; k < LWheelB.size(); k++) {
                this.tempB = this.tempB + LWheelB.get(k);
            }


            if (this.tempA > this.tempB){
                this.action = "preferred_A";
            } else if (tempA < tempB){
                this.action = "preferred_B";
            } else if (tempA == tempB){
                this.action = "none";
            }
        } 
         else if (this.action.equals("none") || (LWheelA.size() < epoch) || (LWheelB.size() < epoch)) {
            this.action = "rolling";
            wheelA.roll(game);
            LWheelA.add(game.getMoney());

            wheelB.roll(game);
            LWheelB.add(game.getMoney());
            this.action = "none";

        }
    }


}

public class main {

public static void main(String[] args){
    /**
     * 
     * Added instances for testing purposes./**
 *
 * 
 * 
 */
    Game game = new Game();
    game.setMoney(100);
    game.setTime(3600);
    Wheel wheelA = new Wheel();
    wheelA.setPayOff(5);
    wheelA.setPayOffChance((float) 0.75);
    Wheel wheelB = new Wheel();
    wheelB.setPayOff(1000);
    wheelB.setPayOffChance((float) 0.1);
    MachineBot bot = new MachineBot();

    while (game.getMoney() > 0 && game.getTime() > 0){
        bot.solve(game, wheelA, wheelB);
    }  
    System.out.println(game.getMoney());

    System.out.println(bot.getAction());

}
}
