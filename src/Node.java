public class Node {

    public static State state;
    public Node parent;
    public String operator;
    public int depth;
    public int path_cost;

    public Node(State s, Node parent, String operator, int depth, int path_cost){
        state=s;
        this.parent=parent;
        this.operator=operator;
        this.depth=depth;
        this.path_cost=path_cost;

    }

    public static State up(){
        State newState = state;
        if(newState.currenty!=newState.m-1){
            newState.currenty++;
            newState=death(newState);
            return newState;
        }
        else{
            return null;
        }

    }
    public static State down(){
        State newState = state;
        if(newState.currenty!=0){
            newState.currenty--;
            newState=death(newState);
            return newState;
        }
        else{
            return null;
        }

    }
    public static State right(){
        State newState = state;
        if(newState.currentx!= newState.n-1){
            newState.currentx++;
            newState=death(newState);
            return newState;
        }
        else{
            return null;
        }

    }
    public static State left(){
        State newState = state;
        if(newState.currentx!= 0){
            newState.currentx--;
            newState=death(newState);
            return newState;
        }
        else{
            return null;
        }

    }
    public static State pickUp(){
        State newState = state;
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
    public static State retrieve(){
        State newState = state;
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
    public static State drop(){
        State newState = state;
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

        Node non=new Node(m,null,"",0,0);
        State newState=non.pickUp();

        Node new2=new Node(newState,null,"",0,0);
        newState=new2.right();
        Node new3=new Node(newState,null,"",0,0);
        newState=new3.left();
         new2=new Node(newState,null,"",0,0);
        Node no1=new Node(m,null,"",0,0);
        newState=no1.retrieve();


        System.out.println(newState.dead);
        System.out.println(newState.currentx);
        System.out.println(newState.currenty);
        System.out.println(newState.blackbox[0][2]);
        System.out.println(newState.ships[0][2]);
        System.out.println(newState.capacity);
        System.out.println(newState.pickedUp);
    }
}
