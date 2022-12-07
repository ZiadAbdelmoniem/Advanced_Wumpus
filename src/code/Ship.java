package code;

public class Ship {
    int numberOfPassengers;
    boolean isWreck;
    boolean isReitrivable;
    boolean isReitrieved;
    int blackBoxDamage;
    int locationX;
    int locationY;
   

    public Ship(int numberOfPassengers,int locx,int locy){
        this.locationX = locx;
        this.locationY = locy;
        this.isWreck = false;
        this.isReitrivable = true;
        this.blackBoxDamage=0;
        this.numberOfPassengers = numberOfPassengers;
    }
    public Ship(){}

    void reitreiveBox(){
        this.isReitrieved = true;
    }


    public boolean action(){
        if(this.blackBoxDamage >= 20){
            this.isReitrivable = false;
        }
        else if(numberOfPassengers != 0){
            this.numberOfPassengers --;
            return true;
            
        }else{
            this.isWreck = true;
            this.blackBoxDamage ++;
        }
        return false;
    }
    int pickUpPassengers(int numberPickUp){
        if(numberPickUp >= this.numberOfPassengers){
            int spare = this.numberOfPassengers;
            this.numberOfPassengers = 0;
            return spare;
        }
        this.numberOfPassengers -= numberPickUp;
        return numberPickUp;
    }
    public Ship deepClone(){
        Ship a = new Ship();
        a.blackBoxDamage = this.blackBoxDamage;
        a.isReitrieved = this.isReitrieved;
        a.isReitrivable = this.isReitrivable;
        a.isWreck = this.isWreck;
        a.numberOfPassengers = this.numberOfPassengers;
        a.locationX = this.locationX;
        a.locationY = this.locationY;
        return a;
    }
    
}
