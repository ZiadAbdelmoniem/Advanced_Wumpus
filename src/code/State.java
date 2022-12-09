package code;

import java.util.Arrays;

public class State {
    public int coastGuardCapacity;
    public int m;
    public int n;

    //coast guard initial coordinates:
    public int initialCoastGuardX;
    public int initialCoastGuardY;

    //coast guard current coordinates:
    public int currentCoastGuardX;
    public int currentCoastGuardY;
    public int [][] ships;
    public int [][] stations;

    public int [][] blackbox;
    public int passengersOnCoastGuard = 0;
    public int saved=0;
    public int dead=0;
    public int blackboxesPickedUp =0;

    //empty constructor so we can initialize a new empty state and later deep copy another node's state into it
    public State(){

    }
    public State(String grid){

        String[] gridStringArr = grid.split(";", -1);

        //initializing the size of the grid
        String[] mxn=gridStringArr[0].split(",",-1);
        m=Integer.parseInt(mxn[0]);
        n=Integer.parseInt(mxn[1]);

        //initializing the maximum number of people on the guard ship
        this.coastGuardCapacity =Integer.parseInt(gridStringArr[1]);

        //initializing the initial position of the guard ship
        String[] cgXY=gridStringArr[2].split(",",-1);
        this.initialCoastGuardX =Integer.parseInt(cgXY[0]);
        this.initialCoastGuardY =  Integer.parseInt(cgXY[1]);
        this.currentCoastGuardX = initialCoastGuardX;
        this.currentCoastGuardY = initialCoastGuardY;

        //initializing the station positions:
        /*
        creating a 2d array with length = the number of stations. each array inside the 2d array is of length 2
        and represents the coordinates:- index 0: X coordinate, index 1: Y coordinate
        */
        String[] sXY=gridStringArr[3].split(",",-1);
        int noOfStations=(sXY.length)/2;
        this.stations=new int[noOfStations][2];
        int loop=0;
        for(int i=0;i< sXY.length;i+=2){
            int X=Integer.parseInt(sXY[i]);
            int Y=Integer.parseInt(sXY[i+1]);
            stations[loop][0]=X;
            stations[loop][1]=Y;
            loop++;
        }

        //initializing the ships & blackboxes positions:
        /*
        creating a 2d array whose length = the number of ships. each array inside the 2d array is of length 3
        and represents the coordinates and passengers:- index 0: X coordinate, index 1: Y coordinate,
        index 2: number of passengers.
        for blackboxes: same respective coordinates however index 2 represents damage. it is initialized with 0,
        when index 2's value is 21, it means either the counter counted up to 21 and the box is no longer retrievable,
        or the coast guard collected the box and the value was set to 21 in order to mark that it is already collected
         */
        String[] shipX=gridStringArr[4].split(",",-1);
        int noOfShips=(shipX.length)/3;
        this.ships=new int[noOfShips][3];
        this.blackbox=new int[noOfShips][3];
        loop=0;
        for(int i=0;i< shipX.length;i+=3){
            int X=Integer.parseInt(shipX[i]);
            int Y=Integer.parseInt(shipX[i+1]);
            int Passenger=Integer.parseInt(shipX[i+2]);
            ships[loop][0]=X;
            ships[loop][1]=Y;
            ships[loop][2]=Passenger;
            blackbox[loop][0]=X;
            blackbox[loop][1]=Y;
            blackbox[loop][2]=0;
            loop++;
        }

    }

    public static void print2D(String mat[][]) {// prints an 2D array in a more viewable way
        for (String[] row : mat)
            System.out.println(Arrays.toString(row));
    }

    public void print2d(){//prints the current state for visualizing a node
        String [][]curr_Grid=new String[this.n][this.m];


        for (int i = 0; i < stations.length; i++) {//position of stations
            int x=stations[i][0];
            int y=stations[i][1];
            curr_Grid[x][y]="Station";
        }

        for (int i = 0; i < ships.length; i++) {//position of ships
            int x=ships[i][0];
            int y=ships[i][1];
            int p=ships[i][2];
            if(p>0) {// if there are passengers put ship in array
                curr_Grid[x][y] = "Ship/" + p;
            }
            else{//if all dead passengers put blackbox
                for (int[] box:this.blackbox
                     ) {
                    if(box[0]==ships[i][0] && box[1]==ships[i][1] ){
                        if(box[2]>20){//un-retrievable black box
                            curr_Grid[x][y] = "BlackBox Gone" ;
                        }
                        else{//boxes that can be retrieved
                            int ShowHealth=box[2]-1;
                            curr_Grid[x][y] = "BlackBox/" + ShowHealth ;
                        }
                    }

                }
            }
        }
        if(curr_Grid[this.currentCoastGuardX][this.currentCoastGuardY]==null){//if guard ship is alone in square print guard ship with capacity
            curr_Grid[this.currentCoastGuardX][this.currentCoastGuardY]="CoastGuard/"+this.passengersOnCoastGuard +"/"+this.blackboxesPickedUp;

        }
        else{//if guard ship is in a square with a station or ship print them with coast guard ship
            curr_Grid[this.currentCoastGuardX][this.currentCoastGuardY]= curr_Grid[this.currentCoastGuardX][this.currentCoastGuardY]+" "+"CoastGuard/"+this.passengersOnCoastGuard +"/"+this.blackboxesPickedUp;

        }

        System.out.println("Dead: "+this.dead);//number of dead people print
        print2D(curr_Grid);

    }

    // goal test for state
    public boolean isGoalState(){

        //check for every ship

        //check if there are any passengers on any ship
        for (int i = 0; i < this.ships.length; i++) {
            if(this.ships[i][2]>0){
                return false;
            }
        }

        //check if there are any blackboxes on any ship
        for (int i = 0; i < this.blackbox.length; i++) {
            if(this.blackbox[i][2]<21){
                return false;
            }
        }

        //check if the coast guard ship has any passengers
        if(this.passengersOnCoastGuard >0){
            return false;
        }

        return true;
    }

    /*
    overriding toString method to be able to get a string value which is used for comparison in order to check
    duplicate states. only variables that are needed for comparison are added to the string.
     */
    @Override
    public String toString(){
        return ""+ currentCoastGuardX +""+ currentCoastGuardY +""+Arrays.deepToString(ships)+""+Arrays.deepToString(blackbox)+""+ passengersOnCoastGuard +
                ""+saved+""+dead+""+ blackboxesPickedUp;
    }

    //deep copying 2d array which we use to deep copy state.
    public static int[][] deepCopy(int[][] arr) {
        if (arr == null) {
            return null;
        }

        final int[][] result = new int[arr.length][];
        for (int i = 0; i < arr.length; i++) {
            result[i] = Arrays.copyOf(arr[i], arr[i].length);
        }
        return result;
    }

    //deep copy state. s is the state to be copied. The function is invoked on the empty state which the values are copied to
    public void copy(State s){
        this.coastGuardCapacity = s.coastGuardCapacity;
        this.m = s.m;
        this.n = s.n;
        this.initialCoastGuardX = s.initialCoastGuardX;
        this.currentCoastGuardX = s.currentCoastGuardX;
        this.currentCoastGuardY = s.currentCoastGuardY;
        this.ships = deepCopy(s.ships);
        this.stations = deepCopy(s.stations);
        this.blackbox = deepCopy(s.blackbox);
        this.passengersOnCoastGuard = s.passengersOnCoastGuard;
        this.saved = s.saved;
        this.dead = s.dead;
        this.blackboxesPickedUp = s.blackboxesPickedUp;
    }

}
