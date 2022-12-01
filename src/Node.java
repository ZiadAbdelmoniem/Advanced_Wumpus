public class Node {

    public static State state;
    public Node parent;
    public String operator;
    public int depth;
    public int path_cost;

    public static State up(){
        State newState = state;
        if(newState.currenty!=newState.m){
            newState.currenty+=1;
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
        else{
            return null;
        }

    }
}
