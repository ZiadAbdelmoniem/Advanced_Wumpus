package code;

import java.util.Arrays;

public class State {
    public int c;
    public int m;
    public int n;
    public int cgX;
    public int cgY;

    public int currentx;
    public int currenty;
    public int [][] ships;
    public int [][] stations;
    public int [][] blackbox;
    public int capacity=0;
    public int saved=0;
    public int dead=0;
    public int pickedUp=0;
    public State(){

    }
    public State(String grid){
        //M, N ; C; cgX, cgY ;
        //I1X, I1Y, I2X, I2Y, ...IiX, IiY ;
        //S1X, S1Y, S1Passengers, S2X, S2Y, S2Passengers, ...Sj X, Sj Y, Sj Passengers;
        String[] arrOfStr = grid.split(";", -1);
        //initializing the size of the grid
        String []mxn=arrOfStr[0].split(",",-1);
        m=Integer.parseInt(mxn[0]);
        n=Integer.parseInt(mxn[1]);
        //initializing the maximum number of people on the guard ship
        this.c=Integer.parseInt(arrOfStr[1]);
        //initializing the initial position of the guard ship
        String []cgXY=arrOfStr[2].split(",",-1);
        this.cgX=Integer.parseInt(cgXY[0]);
        this.cgY=  Integer.parseInt(cgXY[1]);
        this.currentx=cgX;
        this.currenty=cgY;

        //initializing the station positions
        String[] sX=arrOfStr[3].split(",",-1);
        int noOfStations=(sX.length)/2;
        this.stations=new int[noOfStations][2];
        int loop=0;
        for(int i=0;i< sX.length;i+=2){
            int X=Integer.parseInt(sX[i]);
            int Y=Integer.parseInt(sX[i+1]);
            stations[loop][0]=X;
            stations[loop][1]=Y;
            loop++;
        }

        //initializing the ships positions
        String[] shipX=arrOfStr[4].split(",",-1);
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

    public static void print2D(String mat[][]) {
        for (String[] row : mat)
            System.out.println(Arrays.toString(row));
    }

    public void print2d(){
        String [][]curr_Grid=new String[this.n][this.m];


        for (int i = 0; i < stations.length; i++) {
            int x=stations[i][0];
            int y=stations[i][1];
            curr_Grid[x][y]="Station";
        }

        for (int i = 0; i < ships.length; i++) {
            int x=ships[i][0];
            int y=ships[i][1];
            int p=ships[i][2];
            if(p>0) {
                curr_Grid[x][y] = "Ship/" + p;
            }
            else{
                for (int[] box:this.blackbox
                     ) {
                    if(box[0]==ships[i][0] && box[1]==ships[i][1] ){
                        if(box[2]>20){
                            curr_Grid[x][y] = "BlackBox Gone" ;
                        }
                        else{
                            int ShowHealth=box[2]-1;
                            curr_Grid[x][y] = "BlackBox/" + ShowHealth ;
                        }
                    }

                }
            }
        }
        if(curr_Grid[this.currentx][this.currenty]==null){
            curr_Grid[this.currentx][this.currenty]="CoastGuard/"+this.capacity+"/"+this.pickedUp;

        }
        else{
            curr_Grid[this.currentx][this.currenty]= curr_Grid[this.currentx][this.currenty]+" "+"CoastGuard/"+this.capacity+"/"+this.pickedUp;

        }

        System.out.println("Dead: "+this.dead);
        print2D(curr_Grid);

    }

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
        if(this.capacity>0){
            return false;
        }

        return true;
    }


    @Override
    public String toString(){
        return ""+currentx+""+currenty+""+Arrays.deepToString(ships)+""+Arrays.deepToString(blackbox)+""+capacity+
                ""+saved+""+dead+""+pickedUp;
    }


    public static int[][] deepCopy(int[][] original) {
        if (original == null) {
            return null;
        }

        final int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }
    public void copy(State s){
        this.c = s.c;
        this.m = s.m;
        this.n = s.n;
        this.cgX = s.cgX;
        this.currentx = s.currentx;
        this.currenty = s.currenty;
        this.ships = deepCopy(s.ships);
        this.stations = deepCopy(s.stations);
        this.blackbox = deepCopy(s.blackbox);
        this.capacity = s.capacity;
        this.saved = s.saved;
        this.dead = s.dead;
        this.pickedUp = s.pickedUp;
    }

   public static void main(String[] args) {
       State m=new State("5,6;50;1,1;0,4,3,3;1,1,50;");



    }

}
