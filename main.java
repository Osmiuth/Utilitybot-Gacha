import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 * EXPLANATION:
 * 
 * class Game is an object that instantiates integers Money and Time. A setter and getter method is created to accommodate changing/accessing data.
 * class Wheel is an object that instantiates Payoff, PayOffChance, and CurrentChance. A setter and getter method for PayOff and PayOffChance is created for any user-defined
 * modifications.
 * class MachineBot is an object that acts as the agent, wherein its objective is to have as much money in the given time being allotted.
 * 
 * class Game has getMoney, setMoney, getTime, and setTime. All of this are for user-defined operations, which will be used as the environment for the demonstration of the utility-based learning agent.
 * 
 */

class Game { //OOP class for Game, this is so it's modular and easy to update.
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

/**
 * 
 * Class Wheel is the object class that allows the agent to roll the wheel, in order to gain money.
 * 
 * Class Wheel has PayOff,PayOff0,PayOff1,PayOff5,PayOff100,and PayOffChance.
 * PayOff0-PayOff100 are the user-defined luck percentage for each denomination that can possibly be earned within the rolling of the wheel.
 * This class handles the luck-based environmment, allowing for a schocastic path.
 * This class has the getPayOff, getPayOffChance, setPayOffChance, getPayOff0, getPayOff1, getPayOff5, getPayOff100, all of which are for debugging purposes.
 * 
 */

class Wheel {
        private int PayOff = 0; //Payoff amount of the Wheel
        private float PayOff0; //Chance for 0 denomination to be rolled
        private float PayOff1; //Chance for 1 denomination to be rolled
        private float PayOff5; //Chance for 5 denomination to be rolled
        private float PayOff100; //Chance for 100 denomination to be rolled
        private float PayOffChance = 0;
        private float CurrentChance = 0;
        Random rand = new Random();

        public int getPayOff(){
            return this.PayOff;
        }

        public float getPayOffChance(){ //Getter method for chance, this is for debugging purposes.
            return this.PayOffChance;
        }

        public void setPayOffChance(float n){ //Setter method for chance
            this.PayOffChance = n;
        }

        public float getPayOff0(){ //Getter method for PayOff0
            return this.PayOff0;
        }

        public float getPayOff1(){ //Getter method for PayOff1
            return this.PayOff1;
        }

        public float getPayOff5(){ //Getter method for PayOff5
            return this.PayOff5;
        }

        public float getPayOff100(){ //Getter method for PayOff100
            return this.PayOff100;
        }

        public void setPayOff0(float n){ //Setter method for PayOff0
            this.PayOff0 = n;
        }

        public void setPayOff1(float n){ //Setter method for PayOff1
            this.PayOff1 = n;
        }

        public void setPayOff5(float n){ //Setter method for PayOff5
            this.PayOff5 = n;
        }

        public void setPayOff100(float n){ //Setter method for PayOff100
            this.PayOff100 = n;
        }

        public void roll(Game game) { //Rolling function, this is to allow agents to roll the wheel.
            this.CurrentChance = rand.nextFloat(100); //random function (acts as an RNG element)
            if (this.CurrentChance <= PayOff0 *100){ //probability if-decision
                this.PayOff = 0; 
                game.setMoney(game.getMoney()+this.PayOff); //adds payoff money to the overall money of the player.
            } else if (this.CurrentChance <= PayOff1 * 100){
                this.PayOff = 1;
                game.setMoney(game.getMoney()+this.PayOff); //adds payoff money to the overall money of the player.
            } else if (this.CurrentChance <= PayOff5 * 100){
                this.PayOff = 5;
                game.setMoney(game.getMoney()+this.PayOff); //adds payoff money to the overall money of the player.
            } else if (this.CurrentChance <= PayOff100 * 100){
                this.PayOff = 100;
                game.setMoney(game.getMoney()+this.PayOff); //adds payoff money to the overall money of the player.
            }
            game.setMoney(game.getMoney()-1); //decreases the money for every roll
            game.setTime(game.getTime()-10); //decreases the time for every roll
        }
}

/**
 * ALGORITHM OF MACHINE BOT
 * 
 * This agent is Utility-based learning agent, as it focuses on having the most amount of money.
 * 
 * 1. Runs both fortune wheels if it does not have any actions for an amount of an epoch (in this case, epoch = time/10 * 0.2), during this phase, it sets its action into, "rolling"
 * 2. Agent compares the history of both fortune wheels, and see which one has a bigger value in terms of payoff divided by the index of epoch * payoff.
 * 3. If a fortune wheel has a bigger value than the other, the agent sets its action to "preferred_A" or "preferred_B"
 * 4. If both fortune wheels have an equal value, it sets its own action into "none", and the agent runs step 1 over again.
 * 
 * WHAT IS AN EPOCH?
 * An epoch is the amount of memory the agent is given, so the bot can calculate the values based on how far the epoch was set.
 * An optimal epoch value can improve the agent's performance.
 * 
 * Generally, a higher epoch value means that the bot would be very accurate in optimizing the most amount of money. But it means that there should be more rounds.
 * 
 * PLEASE NOTE: the amount of rounds should never equal or be lesser than the epoch value. Hence, the optimization of the epoch value is a must.
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
    private LinkedList<String> BotActions = new LinkedList<String>(); //Bot interaction history logs
    private LinkedList<Integer> Rounds = new LinkedList<Integer>(); //Rounds history logs
    private int epoch = 15; //epoch value
    private int index; //Index variable to address the proportion problem
    private float tempA; //tempA storage value
    private float tempB; //tempB storage value

    public LinkedList<Integer> getListA() { //For debugging purposes
        return this.LWheelA;
    }

    public void setEpoch(int n) {
        this.epoch = n;
    }

    public int getEpoch() {
        return this.epoch;
    }

    public LinkedList<Integer> getListB() { //For debugging purposes
        return this.LWheelB;
    }

    public LinkedList<String> getBotActions() { //For debugging purposes
        return this.BotActions;
    }

    public LinkedList<Integer> getRounds() { //For debugging purposes
        return this.Rounds;
    }


    public String getAction() { //Returns the preferred wheel of the agent
        return this.action;
    }

    public void solve(Game game, Wheel wheelA, Wheel wheelB) {
        if (this.action.equals("preferred_A")) { //Runs this commmand if it prefers wheel A
            BotActions.add(this.action);
            Rounds.add(game.getTime()/10);
            LWheelA.add(game.getMoney());
            wheelA.roll(game);
        } else if (this.action.equals("preferred_B")) { //Runs this commmand if it prefers wheel B
            BotActions.add(this.action);
            Rounds.add(game.getTime()/10);
            LWheelB.add(game.getMoney());
            wheelB.roll(game);
        }

        if ((Rounds.size() % epoch == 0) && (!LWheelA.isEmpty() && !LWheelB.isEmpty())) {
            index = epoch * 100;
            for (int j = LWheelA.size() - epoch; j < LWheelA.size(); j++) {
                this.tempA = this.tempA + LWheelA.get(j); //Precept history of wheelA
            }

            this.tempA = this.tempA / (index);


            for (int k = LWheelB.size() - epoch; k < LWheelB.size(); k++) {
                this.tempB = this.tempB + LWheelB.get(k); //Precept history of wheelB
            }

            this.tempB = this.tempB / (index);
            
            System.out.println(this.tempA + " " + this.tempB);
            /**
             * Below this line is a comparison tree that determines the future action of the agent. If tempA is greater than tempB, then the first wheel will be accepted, if tempB is greater than tempA
             * then the second wheel will be accepted.
             * 
             * Otherwise, if both are equal, then a roll will be made again.
             * 
             */

            if (this.tempA > this.tempB){
                this.action = "preferred_A";
                Rounds.add(game.getTime()/10+1); //considers this step as a half of a round.
            } else if (this.tempA < this.tempB){
                this.action = "preferred_B";
                Rounds.add(game.getTime()/10+1); //considers this step as a half of a round.
            } else if (this.tempA == this.tempB){
                this.action = "none";
                Rounds.add(game.getTime()/10+1); //considers this step as a half of a round.
            } 

        } 
         else if (this.action.equals("none")) {
            this.action = "rolling";
            BotActions.add(this.action);
            Rounds.add(game.getTime()/10);
            wheelA.roll(game);
            LWheelA.add(game.getMoney()-wheelB.getPayOff()); //Initial stage, where precept history is below the epoch value, and the stage where the agent 're-learns' the environment.

            BotActions.add(this.action);
            Rounds.add(game.getTime()/10);
            wheelB.roll(game);
            LWheelB.add(game.getMoney()-wheelA.getPayOff()); //Initial stage, where precept history is below the epoch value, and the stage where the agent 're-learns' the environment.
            this.action = "none";

        }
    }


}

public class main {

public static void main(String[] args){

    /**
     * EVERYTHING BELOW HERE IS JUST TO CREATE THE GAME
     * Everything that was instantiated here is for the sake of demonstration only. This section of the code represents the user-defined variables the users
     * will be placing.
     * 
     *It also has basic error-checks, in case the percentage goes beyond 100 or goes less of 100. 
     * Basic checks for scanning inputs are not implemented.
     */

    Scanner scan = new Scanner(System.in);
    Game game = new Game();

    System.out.print("Set initial money: \n");
    game.setMoney((int) scan.nextInt()); //Money Variable has been set here.
    System.out.print("Set initial Time: \n");
    game.setTime((int) scan.nextInt()); //Time Variable has been set here.
    int oldTime = game.getTime(); //De-referenced Time is stored
    int oldMoney = game.getMoney(); //De-referenced Money is stored.
    Wheel wheelA = new Wheel();
    Wheel wheelB = new Wheel();
    float chanceA = 0;
    float chanceB = 0;
    while (chanceA != 100 && chanceB != 100){ //Error Checking system to determine if all percentages do not go beyond 100, or goes less.
    System.out.print("Set Wheel A's Payoff chance for 0 peso: \n");
    wheelA.setPayOff0((float) scan.nextInt()/100);
    System.out.print("Set Wheel A's Payoff chance for 1 peso: \n");
    wheelA.setPayOff1((float) scan.nextInt()/100);
    System.out.print("Set Wheel A's Payoff chance for 5 pesos: \n");
    wheelA.setPayOff5((float) scan.nextInt()/100);
    System.out.print("Set Wheel A's Payoff chance for 100 pesos: \n");
    wheelA.setPayOff100((float) scan.nextInt()/100);

    System.out.print("Set Wheel B's Payoff chance for 0 peso: \n");
    wheelB.setPayOff0((float) scan.nextInt()/100);
    System.out.print("Set Wheel B's Payoff chance for 1 peso: \n");
    wheelB.setPayOff1((float) scan.nextInt()/100);
    System.out.print("Set Wheel B's Payoff chance for 5 pesos: \n");
    wheelB.setPayOff5((float) scan.nextInt()/100);
    System.out.print("Set Wheel B's Payoff chance for 100 pesos: \n");
    wheelB.setPayOff100((float) scan.nextInt()/100);

    chanceA = chanceA+wheelA.getPayOff0()*100;
    chanceA = chanceA+wheelA.getPayOff1()*100;
    chanceA = chanceA+wheelA.getPayOff5()*100;
    chanceA = chanceA+wheelA.getPayOff100()*100;

    chanceB = chanceB+wheelB.getPayOff0()*100;
    chanceB = chanceB+wheelB.getPayOff1()*100;
    chanceB = chanceB+wheelB.getPayOff5()*100;
    chanceB = chanceB+wheelB.getPayOff100()*100;

    if (chanceA == 100 && chanceB == 100){
        wheelA.setPayOff1(wheelA.getPayOff1() + wheelA.getPayOff0());
        wheelA.setPayOff5(wheelA.getPayOff5() + wheelA.getPayOff1());
        wheelA.setPayOff100(wheelA.getPayOff100() + wheelA.getPayOff5());
        wheelB.setPayOff1(wheelB.getPayOff1() + wheelB.getPayOff0());
        wheelB.setPayOff5(wheelB.getPayOff5() + wheelB.getPayOff1());
        wheelB.setPayOff100(wheelB.getPayOff100() + wheelB.getPayOff5());
        break;
    } else {
        System.out.println("Everything should equal to 100 in Wheel A and Wheel B. Try again.");
        chanceA = 0;
        chanceB = 0;
    }
    }

    MachineBot bot = new MachineBot(); //Initialization of the utility-based bot
    while (game.getMoney() > 0 && game.getTime() > 0){ //While loop that makes the bot run, until the condition has been satisfied.
        bot.solve(game, wheelA, wheelB);
    }  

    
    int a = 0;
    int b = 0;
    int temp = oldMoney;
    for(int i = 0; i < bot.getBotActions().size(); i++){
        if(bot.getBotActions().get(i) == "rolling" && a < bot.getEpoch() && b < bot.getEpoch()){
            System.out.println("Rolling");
            System.out.println("You earned: " + (bot.getListA().get(a) - (temp-1)));
            System.out.println("Current money is now: " + bot.getListA().get(a));
            temp = bot.getListA().get(a);
            a++;

            System.out.println("Rolling");
            System.out.println("You earned: " + (bot.getListB().get(b) - (temp-1)));
            System.out.println("Current money is now: " + bot.getListB().get(b));
            temp = bot.getListB().get(b);
            b++;
        }
        else if(bot.getBotActions().get(i) == "preferred_A"){
            System.out.println("Rolling on Machine A");
            System.out.println("You earned: " + (bot.getListA().get(a) - (temp-1)));
            System.out.println("Current money is now: " + bot.getListA().get(a));
            temp = bot.getListA().get(a);
            a++;
        }
        else if(bot.getBotActions().get(i) == "preferred_B"){
            System.out.println("Rolling on Machine B");
            System.out.println("You earned: " + (bot.getListB().get(b) - (temp-1)));
            System.out.println("Current money is now: " + bot.getListB().get(b));
            temp = bot.getListB().get(b);
            b++;
        }
    }
    
    System.out.println(game.getMoney() + " Current money");
    System.out.println(game.getMoney() - oldMoney + " Earned after " + oldTime + " seconds.");

    System.out.println(bot.getAction()); //Gets the preferred action of the bot.

    System.out.println("Would you like a detailed log of everything that transpired? y/n"); //Gets the detailed logs of the environment, and the actions of the bot.
    String ans = scan.next();
    if (ans.equals("y")) {
            System.out.println("Wheel A logs: " + bot.getListA());
            System.out.println("Wheel B logs: " + bot.getListB());
            System.out.println("Rounds: " + bot.getRounds());
            System.out.println("Actions: " + bot.getBotActions());
        } else {
            //Nothing happens
        }
    }
}
