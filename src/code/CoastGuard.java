package code;

import java.util.HashSet;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


public class CoastGuard {

    static HashSet<String> existingStates = new HashSet<String>();
    public static String GenGrid(){
        //Double.POSITIVE_INFINITY

        String res="";
        String stations="";
        String ships="";
        int M=(int) ((Math.random() * (20 - 5)) + 5);
        int N=(int) ((Math.random() * (15 - 1)) + 1);
        int C=(int) ((Math.random() * (100 - 30)) + 30);
        int cgX=(int) ((Math.random() * (M - 1)) + 1);
        int cgY=(int) ((Math.random() * (N - 1)) + 1);

        res+=M+","+N+";"+C+";"+cgX+","+cgY+";";

        int [][]myGrid=new int[M][N];
        myGrid[cgX][cgY]=9;

        //mandatory station + ship
        boolean flag=true;
        while(flag){

        int s1=(int) ((Math.random() * (M - 1)) + 1);
        int s2=(int) ((Math.random() * (N - 1)) + 1);
        int sh1=(int) ((Math.random() * (M - 1)) + 1);
        int sh2=(int) ((Math.random() * (N - 1)) + 1);
        int p1=(int) ((Math.random() * (100 - 1)) + 1);

            if(myGrid[s1][s2]==0 && myGrid[sh1][sh2]==0){
            myGrid[s1][s2]=1;
            myGrid[sh1][sh2]=2;
            stations=s1+","+s2;
            ships=sh1+","+sh2+","+p1;
            flag=false;
        }}

        //position of a station=1
        //position of a ship=2
        //position of empty=3,4,5,6,7

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (myGrid[i][j] != 9) {
                    int type = (int) ((Math.random() * (7 - 1)) + 1);

                    if (type == 1) {
                        stations += "," + i + "," + j;
                    }

                    if (type == 2) {
                        int p = (int) ((Math.random() * (100 - 1)) + 1);
                        ships += "," + i + "," + j + "," + p;
                    }

                }
            }
        }

        res=res+""+stations+";"+ships+";";

        return res;
    }
    public static String BFS(String grid){
        int nodesNumber = 0;
        State firstState=new State(grid);
        Node firstNode=new Node(firstState,null,"",0,0);
        Queue<Node> nodes = new LinkedList<>();//queue for bfc
        nodes.add(firstNode);
        while(!nodes.isEmpty()){
            Node node=nodes.remove();
            if(node.state.isGoalState()){//Goal test
                String s=node.operator+";"+node.state.dead+";"+node.state.pickedUp+";"+nodesNumber;
                s=s.substring(1);
                return s;

            }
            else{
                State checkState=node.pickUp();
                String[] LastNode=node.operator.split(",",-1);
                String lastOperator=LastNode[LastNode.length-1];

                if(checkState!=null ){
                    Node addNode=new Node(checkState,node,node.operator+",pickup",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.drop();
                if(checkState!=null){
                    Node addNode=new Node(checkState,node,node.operator+",drop",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.retrieve();
                if(checkState!=null){
                    Node addNode=new Node(checkState,node,node.operator+",retrieve",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.right();
                if(checkState!=null && !lastOperator.equals("left")){
                    Node addNode=new Node(checkState,node,node.operator+",right",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {

                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.left();
                if(checkState!=null&& !lastOperator.equals("right")){
                    Node addNode=new Node(checkState,node,node.operator+",left",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.up();
                if(checkState!=null&& !lastOperator.equals("down")){
                    Node addNode=new Node(checkState,node,node.operator+",up",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.down();
                if(checkState!=null&& !lastOperator.equals("up")){
                    Node addNode=new Node(checkState,node,node.operator+",down",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }



            }
        }
        return "no solution";
    }
    public static String DFS(String grid){
        int nodesNumber = 0;
        State firstState=new State(grid);
        Node firstNode=new Node(firstState,null,"",0,0);
        Stack<Node> nodes = new Stack<Node>(); // A stack used by IDS and DFS
        nodes.push(firstNode);
        while(!nodes.isEmpty()){
            Node node=nodes.pop();
            if(node.state.isGoalState()){//Goal test
                String s=node.operator+";"+node.state.dead+";"+node.state.pickedUp+";"+nodesNumber;
                s=s.substring(1);
                return s;

            }
            else{
                State checkState=node.pickUp();
                String[] LastNode=node.operator.split(",",-1);
                String lastOperator=LastNode[LastNode.length-1];

                if(checkState!=null ){
                    Node addNode=new Node(checkState,node,node.operator+",pickup",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.push(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.drop();
                if(checkState!=null){
                    Node addNode=new Node(checkState,node,node.operator+",drop",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.push(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.retrieve();
                if(checkState!=null){
                    Node addNode=new Node(checkState,node,node.operator+",retrieve",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.push(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.right();
                if(checkState!=null && !lastOperator.equals("left")){
                    Node addNode=new Node(checkState,node,node.operator+",right",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {

                        nodes.push(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.left();
                if(checkState!=null&& !lastOperator.equals("right")){
                    Node addNode=new Node(checkState,node,node.operator+",left",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.push(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.up();
                if(checkState!=null&& !lastOperator.equals("down")){
                    Node addNode=new Node(checkState,node,node.operator+",up",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.push(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.down();
                if(checkState!=null&& !lastOperator.equals("up")){
                    Node addNode=new Node(checkState,node,node.operator+",down",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.push(addNode);
                        nodesNumber++;
                    }
                }



            }
        }
        return "no solution";
    }

    public static String solve(String grid, String strategy, boolean visual){



        switch (strategy) {
            case "BF":
                return BFS(grid);
            case "DF":
                return DFS(grid);
        }
        return "no solution";
    }

    public static boolean isDuplicate(State state){
        String stateString = state.toString();
        if (existingStates.contains(stateString))
            return true;

        existingStates.add(stateString);
        return false;
    }

    public static void main (String []args){


        String grid= "6,6;52;2,0;2,4,4,0,5,4;2,1,19,4,2,6,5,0,8;";
        String s=solve(grid,"BF",true);
        //"5,6;50;0,1;0,4,3,3;1,1,90;"
        System.out.println(s);



    }
}