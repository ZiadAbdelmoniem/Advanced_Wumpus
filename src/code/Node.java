package code;

public class Node {

    public State state;
    public Node parent;
    public String operators;
    public int depth;
    public int path_cost;

    public int heuristic_cost = 0;

    public Node(State s, Node parent, String operators, int depth, int path_cost){
        this.state=s;
        this.parent=parent;
        this.operators = operators;
        this.depth=depth;
        this.path_cost=path_cost;

    }

    /*
    function for calculating and setting heuristic cost of the node where heuristicNumber is the number of
    heuristic function chosen.
     */

    public void calculateHeuristic(int heuristicNumber) {
        if (heuristicNumber == 1) {
            int passengersOnShips = 0;
            int blackBoxesNotRetrieved=0;
            for (int i = 0; i < this.state.ships.length; i++) {//find total number of passengers on the ship
                passengersOnShips += this.state.ships[i][2];
            }

            for (int i = 0; i < this.state.blackbox.length; i++) {// find total number of black boxes not retrieved values is 21 as we add an 1 redundant 1 at the start their health
                if(this.state.blackbox[i][2]<21){
                    blackBoxesNotRetrieved++;
                }
            }

            this.heuristic_cost = (int) Math.ceil((passengersOnShips / this.state.coastGuardCapacity))+blackBoxesNotRetrieved ;// cost is the number of passengers divided by the guard ship capacity + the number of unpicked boxes


        } else if (heuristicNumber == 2) {// this is similar to heuristic 1 with an addition of the passengers on the coast guard shi[
            int passengersOnShips = 0;
            int blackBoxesNotRetrieved = 0;
            for (int i = 0; i < this.state.ships.length; i++) {
                passengersOnShips += this.state.ships[i][2];
            }
            for (int i = 0; i < this.state.blackbox.length; i++) {
                if (this.state.blackbox[i][2] < 21) {
                    blackBoxesNotRetrieved++;
                }
            }
            byte dropOnCoastGuard = 0;
            if (this.state.coastGuardCapacity == this.state.passengersOnCoastGuard) {// we check if the coast guard ship is at maximum capacity if so that means we have an additional step of dropping passengers on ship
                dropOnCoastGuard = 1;
            }
            if (passengersOnShips==0) {
                this.heuristic_cost = blackBoxesNotRetrieved ;

            } else {
                this.heuristic_cost = (int) Math.ceil((passengersOnShips / this.state.coastGuardCapacity)) + blackBoxesNotRetrieved +dropOnCoastGuard;
            }
        }
    }

    /*
    for all the following actions/operators (right, left, down, up, pickUp, retrieve, drop), we check if that action is possible,
    if it is, return the state after taking that action else return null.
     */

    //move right action
    public State right(){
        State newState = new State();
        newState.copy(state);
        if(newState.currentCoastGuardY !=newState.m-1){
            newState.currentCoastGuardY++;
            newState=death(newState);
            return newState;
        }
        else{
            return null;
        }

    }

    //move left action
    public State left(){
        State newState = new State();
        newState.copy(state);
        if(newState.currentCoastGuardY !=0){
            newState.currentCoastGuardY--;
            newState=death(newState);
            return newState;
        }
        else{
            return null;
        }

    }

    //move down action
    public State down(){
        State newState = new State();
        newState.copy(state);
        if(newState.currentCoastGuardX != newState.n-1){
            newState.currentCoastGuardX++;
            newState=death(newState);
            return newState;
        }
        else{
            return null;
        }

    }

    //move up action
    public State up(){
        State newState = new State();
        newState.copy(state);
        if(newState.currentCoastGuardX != 0){
            newState.currentCoastGuardX--;
            newState=death(newState);
            return newState;
        }
        else{
            return null;
        }

    }

    //pickup action
    public State pickUp(){
        State newState = new State();
        newState.copy(state);
        if(newState.passengersOnCoastGuard < newState.coastGuardCapacity){
            boolean shipExists=false;
            for (int[]ship:newState.ships ) {
                if(ship[0]== newState.currentCoastGuardX &&ship[1]== newState.currentCoastGuardY &&ship[2]>0){//ship is in out position and has passengers
                    shipExists=true;
                    int carriageSpace=newState.coastGuardCapacity -newState.passengersOnCoastGuard;
                    if(carriageSpace<ship[2]){
                    ship[2]-=carriageSpace;
                    newState.passengersOnCoastGuard =newState.coastGuardCapacity;
                    newState.saved+= carriageSpace;
                }
                    else{
                        newState.passengersOnCoastGuard +=ship[2];
                        ship[2]=0;
                        newState.saved+= ship[2];
                        for (int[]blackbox: newState.blackbox
                             ) {if(blackbox[0]==ship[0] &&blackbox[1]==ship[1]){
                                 blackbox[2]=1;
                        }

                        }
                    }
                }
            }
            if(!shipExists){
                return null;
            }
            newState=death(newState);
            return newState;
        }
        else{
            return null;
        }

    }

    //retrieve action
    public State retrieve(){
        State newState = new State();
        newState.copy(state);
        boolean shipExists=false;
        for (int[]ship:newState.ships ) {
            if (ship[0] == newState.currentCoastGuardX && ship[1] == newState.currentCoastGuardY && ship[2] == 0) {//ship is in out position and has passengers
                for (int[]blackbox:newState.blackbox
                     ) {
                    if(blackbox[0]==ship[0] &&blackbox[1]==ship[1] && blackbox[2]<21){
                        shipExists = true;
                        blackbox[2]=22;
                        newState.blackboxesPickedUp++;
                    }
                }

            }
        }
        if(!shipExists){
            return null;
        }
            newState=death(newState);
            return newState;

    }

    //drop action
    public State drop(){
        State newState = new State();
        newState.copy(state);
        if(newState.passengersOnCoastGuard >0){
            boolean stationExists=false;
            for (int[]station: newState.stations
                 ) {if(station[0]== newState.currentCoastGuardX &&station[1]== newState.currentCoastGuardY){
                     stationExists=true;
                     newState.passengersOnCoastGuard =0;
            }

            }
            if(!stationExists){
                return null;
            }

            newState=death(newState);
            return newState;
        }else{
            return null;
        }
    }


    public static State death(State newState){// this function kills off the passengers every time step and adds to the damage of the blackboxes
        for (int i = 0; i <newState.ships.length ; i++) {
            if(newState.ships[i][2]>0){
                newState.ships[i][2]--;
                newState.dead++;
                if(newState.ships[i][2]==0){
                    newState.blackbox[i][2]=1;//replace ship with wreck
                }
            }

        }
        for (int i = 0; i <newState.blackbox.length ; i++) {
            if(newState.blackbox[i][2]>0){
                newState.blackbox[i][2]++;
            }
        }
        return newState;
    }

    public static void main(String[] args) {
        State m=new State("5,6;50;1,1;0,4,3,3;1,1,50;");
        Node trial=new Node(m,null,null,0,0);
        State newState=trial.up();
        Node trial2=new Node(newState,null,null,0,0);
        State newState1=trial2.up();



        System.out.println(newState1.dead);
        System.out.println(newState1.currentCoastGuardX);
        System.out.println(newState1.currentCoastGuardY);
        System.out.println(newState1.blackbox[0][2]);
        System.out.println(newState1.ships[0][2]);
        System.out.println(newState1.passengersOnCoastGuard);
        System.out.println(newState1.blackboxesPickedUp);
    }
}
