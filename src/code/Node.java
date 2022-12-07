package code;

public class Node {

    public State state;
    public Node parent;
    public String operator;
    public int depth;
    public int path_cost;

    public int heuristic_cost;

    public Node(State s, Node parent, String operator, int depth, int path_cost){
        this.state=s;
        this.parent=parent;
        this.operator=operator;
        this.depth=depth;
        this.path_cost=path_cost;

    }

    public Node(State s, Node parent, String operator, int depth, int path_cost, int heuristic_cost){
        this.state=s;
        this.parent=parent;
        this.operator=operator;
        this.depth=depth;
        this.path_cost=path_cost;
        this.heuristic_cost = heuristic_cost;

    }
    public void calculateHeuristic(int heuristicNumber){
        if (heuristicNumber == 1){
            int passengersOnShips = 0;
            for(int i = 0; i< this.state.ships.length;i++){
                passengersOnShips += this.state.ships[i][2];
            }

            int passengersOnGuardShip = this.state.capacity;

            this.heuristic_cost = 2*passengersOnShips + passengersOnGuardShip;
        }

        else if (heuristicNumber == 2){
            int passengersOnShips = 0;
            for(int i = 0; i< this.state.ships.length;i++){
                passengersOnShips += this.state.ships[i][2];
            }
            this.heuristic_cost = passengersOnShips * (this.state.dead / 2*(this.state.saved+1));
        }
    }

    public State right(){
        State newState = new State();
        newState.copy(state);
        if(newState.currenty!=newState.m-1){
            newState.currenty++;
            newState=death(newState);
            return newState;
        }
        else{
            return null;
        }

    }
    public State left(){
        State newState = new State();
        newState.copy(state);
        if(newState.currenty!=0){
            newState.currenty--;
            newState=death(newState);
            return newState;
        }
        else{
            return null;
        }

    }
    public State down(){
        State newState = new State();
        newState.copy(state);
        if(newState.currentx!= newState.n-1){
            newState.currentx++;
            newState=death(newState);
            return newState;
        }
        else{
            return null;
        }

    }
    public State up(){
        State newState = new State();
        newState.copy(state);
        if(newState.currentx!= 0){
            newState.currentx--;
            newState=death(newState);
            return newState;
        }
        else{
            return null;
        }

    }
    public State pickUp(){
        State newState = new State();
        newState.copy(state);
        if(newState.capacity< newState.c){
            boolean shipExists=false;
            for (int[]ship:newState.ships ) {
                if(ship[0]== newState.currentx &&ship[1]== newState.currenty &&ship[2]>0){//ship is in out position and has passengers
                    shipExists=true;
                    int carriageSpace=newState.c-newState.capacity ;
                    if(carriageSpace<ship[2]){
                    ship[2]-=carriageSpace;
                    newState.capacity=newState.c;
                    newState.saved+= carriageSpace;
                }
                    else{
                        newState.capacity+=ship[2];
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
    public State retrieve(){
        State newState = new State();
        newState.copy(state);
        boolean shipExists=false;
        for (int[]ship:newState.ships ) {
            if (ship[0] == newState.currentx && ship[1] == newState.currenty && ship[2] == 0) {//ship is in out position and has passengers
                for (int[]blackbox:newState.blackbox
                     ) {
                    if(blackbox[0]==ship[0] &&blackbox[1]==ship[1] && blackbox[2]<21){
                        shipExists = true;
                        blackbox[2]=22;
                        newState.pickedUp++;
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
    public State drop(){
        State newState = new State();
        newState.copy(state);
        if(newState.capacity>0){
            boolean stationExists=false;
            for (int[]station: newState.stations
                 ) {if(station[0]== newState.currentx &&station[1]== newState.currenty){
                     stationExists=true;
                     newState.capacity=0;
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
    public static State death(State newState){
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
        System.out.println(newState1.currentx);
        System.out.println(newState1.currenty);
        System.out.println(newState1.blackbox[0][2]);
        System.out.println(newState1.ships[0][2]);
        System.out.println(newState1.capacity);
        System.out.println(newState1.pickedUp);
    }
}
