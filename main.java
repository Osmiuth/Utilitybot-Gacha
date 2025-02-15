import java.util.Random;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * EXPLANATION:
 * 
 * class Game is an object that instantiates integers Money and Time. A setter and getter method is created to accommodate changing/accessing data.
 * class Wheel is an object that instantiates Payoff, PayOffChance, and CurrentChance. A setter and getter method for PayOff and PayOffChance is created for any user-defined
 * modifications.
 * class MachineBot is an object that acts as the agent, wherein its objective is to have as much money in the given time being allotted.
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

        public int getPayOff(){ //Getter method for payoff
            return this.PayOff;
        }

        public void setPayOff(int n){ //Setter method for payoff
            this.PayOff = n;
        }

        public float getPayOffChance(){ //Getter method for chance
            return this.PayOffChance;
        }

        public void setPayOffChance(float n){ //Setter method for chance
            this.PayOffChance = n;
        }

        public void roll(Game game) { //Rolling function
            game.setMoney(game.getMoney()-1); //decreases the money for every roll
            game.setTime(game.getTime()-10); //decreases the time for every roll
            this.CurrentChance = rand.nextFloat(100); //random function (acts as an RNG element)
            if (this.CurrentChance <= PayOffChance *100){ //probability if-decision
                game.setMoney(game.getMoney()+this.PayOff); //adds payoff money to the overall money of the player.
            } 
        }
}

/**
 * ALGORITHM OF MACHINE BOT
 * 
 * This agent is Utility-based, as it focuses on having the most amount of
 * 
 * 1. Runs both fortune wheels if it does not have any actions for an amount of an epoch (in this case, epoch = 5), during this phase, it sets its action into, "rolling"
 * 2. Agent compares the history of both fortune wheels, and see which one has a bigger value in terms of payoff.
 * 3. If a fortune wheel has a bigger value than the other, the agent sets its action to "preferred_A" or "preferred_B"
 * 4. If both fortune wheels have an equal value, it sets its own action into "none", and the agent runs step 1 over again.
 * 
 * WHAT IS AN EPOCH?
 * An epoch is the amount of memory the agent is given, so the bot can calculate the values based on how far the epoch was set.
 * An optimal epoch value can improve the agent's performance.
 * 
 *
 * Time Complexity:
 * O(epoch)
 * 
 * Space Complexity:
 * O(n+k)
 * 
 */

class MachineBot {
    private String action = "none";
    private LinkedList<Integer> LWheelA = new LinkedList<Integer>(); //Wheel A history
    private LinkedList<Integer> LWheelB = new LinkedList<Integer>(); //Wheel B history
    private int epoch = 5; //epoch value
    private int tempA; //tempA storage value
    private int tempB; //tempB storage value

    public LinkedList<Integer> getListA() { //For debugging purposes
        return this.LWheelA;
    }

    public LinkedList<Integer> getListB() { //For debugging purposes
        return this.LWheelB;
    }

    public String getAction() { //Returns the preferred wheel of the agent
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

        if ((LWheelA.size() % epoch == 0) && (LWheelB.size() % epoch == 0) && (LWheelA.size() > 0) && (LWheelB.size() > 0)) {
            for (int j = LWheelA.size() - epoch; j < LWheelA.size(); j++) {
                this.tempA = this.tempA + LWheelA.get(j); //Precept history of wheelA
            }


            for (int k = LWheelB.size() - epoch; k < LWheelB.size(); k++) {
                this.tempB = this.tempB + LWheelB.get(k); //Precept history of wheelB
            }


            if (this.tempA > this.tempB){
                this.action = "preferred_A";
            } else if (tempA < tempB){
                this.action = "preferred_B";
            } else if (tempA == tempB){
                this.action = "none";
            }
        } 
         else if (this.action.equals("none")) {
            this.action = "rolling";
            wheelA.roll(game);
            LWheelA.add(game.getMoney()); //Initial stage, where precept history is below the epoch value

            wheelB.roll(game);
            LWheelB.add(game.getMoney()); //Initial stage, where precept history is below the epoch value
            this.action = "none";

        }
    }


}

public class main {

public static void main(String[] args){

    Scanner scan = new Scanner(System.in);
    Game game = new Game();

    System.out.print("Set initial money: \n");
    game.setMoney(scan.nextInt());
    System.out.print("Set initial Time: \n");
    game.setTime(scan.nextInt());
    Wheel wheelA = new Wheel();
    System.out.print("Set Wheel A's Payoff: \n");
    wheelA.setPayOff(scan.nextInt());
    System.out.print("Set Wheel B's Payoff chance: \n");
    wheelA.setPayOffChance(scan.nextFloat());
    Wheel wheelB = new Wheel();
    System.out.print("Set Wheel B's Payoff: \n");
    wheelB.setPayOff(scan.nextInt());
    System.out.print("Set Wheel B's Payoff chance: \n");
    wheelB.setPayOffChance(scan.nextFloat());

    MachineBot bot = new MachineBot();

    while (game.getMoney() > 0 && game.getTime() > 0){
        bot.solve(game, wheelA, wheelB);
    }  
    System.out.println(game.getMoney() + " Money earned during " + game.getTime() + " seconds.");

    System.out.println(bot.getAction());

}
}
