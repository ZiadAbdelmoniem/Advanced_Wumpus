package code;

import java.util.*;


public class CoastGuard {

    static HashSet<String> existingStates = new HashSet<String>();

    public static String genGrid(){
        //Double.POSITIVE_INFINITY

        String res="";
        String stations="";
        String ships="";
        int M=(int) ((Math.random() * (15 - 5)) + 5);
        int N=(int) ((Math.random() * (15 - 5)) + 5);
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
                existingStates = new HashSet<String>();
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
                existingStates = new HashSet<String>();
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
    public static String ID(String grid){
        int maxDepth=0;
        while(true) {
            State firstState=new State(grid);
            Node firstNode=new Node(firstState,null,"",0,0);
            Stack<Node> nodes = new Stack<Node>(); // A stack used by IDS and DFS
            nodes.push(firstNode);
            existingStates = new HashSet<String>();
            while (!nodes.isEmpty()) {
                int nodesNumber = 0;
                Node node = nodes.pop();
                if (node.state.isGoalState()) {//Goal test
                    String s = node.operator + ";" + node.state.dead + ";" + node.state.pickedUp + ";" + nodesNumber;
                    s = s.substring(1);
                    existingStates = new HashSet<String>();
                    return s;

                } else {
                    int childDepth = node.depth + 1;
                    if (childDepth <= maxDepth) {
                    State checkState = node.pickUp();
                    String[] LastNode = node.operator.split(",", -1);
                    String lastOperator = LastNode[LastNode.length - 1];

                    if (checkState != null) {
                        Node addNode = new Node(checkState, node, node.operator + ",pickup", node.depth + 1, 0);
                        if (!isDuplicate(addNode.state)) {
                            nodes.push(addNode);
                            nodesNumber++;
                        }
                    }
                    checkState = node.drop();
                    if (checkState != null) {
                        Node addNode = new Node(checkState, node, node.operator + ",drop", node.depth + 1, 0);
                        if (!isDuplicate(addNode.state)) {
                            nodes.push(addNode);
                            nodesNumber++;
                        }
                    }
                    checkState = node.retrieve();
                    if (checkState != null) {
                        Node addNode = new Node(checkState, node, node.operator + ",retrieve", node.depth + 1, 0);
                        if (!isDuplicate(addNode.state)) {
                            nodes.push(addNode);
                            nodesNumber++;
                        }
                    }
                    checkState = node.right();
                    if (checkState != null && !lastOperator.equals("left")) {
                        Node addNode = new Node(checkState, node, node.operator + ",right", node.depth + 1, 0);
                        if (!isDuplicate(addNode.state)) {

                            nodes.push(addNode);
                            nodesNumber++;
                        }
                    }
                    checkState = node.left();
                    if (checkState != null && !lastOperator.equals("right")) {
                        Node addNode = new Node(checkState, node, node.operator + ",left", node.depth + 1, 0);
                        if (!isDuplicate(addNode.state)) {
                            nodes.push(addNode);
                            nodesNumber++;
                        }
                    }
                    checkState = node.up();
                    if (checkState != null && !lastOperator.equals("down")) {
                        Node addNode = new Node(checkState, node, node.operator + ",up", node.depth + 1, 0);
                        if (!isDuplicate(addNode.state)) {
                            nodes.push(addNode);
                            nodesNumber++;
                        }
                    }
                    checkState = node.down();
                    if (checkState != null && !lastOperator.equals("up")) {
                        Node addNode = new Node(checkState, node, node.operator + ",down", node.depth + 1, 0);
                        if (!isDuplicate(addNode.state)) {
                            nodes.push(addNode);
                            nodesNumber++;
                        }
                    }


                }
            }
            }
            maxDepth++;
        }
    }
    public static String greedy(String grid, int heuristicNumber){
        int nodesNumber = 0;
        State firstState=new State(grid);
        Node firstNode=new Node(firstState,null,"",0,0);
        //note: correctly estimating initial capacity in priority queue decreases memory and time needed
        //maybe we can estimate according to grid size
        PriorityQueue<Node> nodes = new PriorityQueue<Node>(20, new NodeGreedyComparator());
        nodes.add(firstNode);
        while(!nodes.isEmpty()){
            Node node = nodes.remove();
            if(node.state.isGoalState()){//Goal test
                String s=node.operator+";"+node.state.dead+";"+node.state.pickedUp+";"+nodesNumber;
                s=s.substring(1);
                existingStates = new HashSet<String>();
                return s;
            }
            else{
                String[] LastNode=node.operator.split(",",-1);
                String lastOperator=LastNode[LastNode.length-1];

                State checkState=node.pickUp();
                if(checkState!=null ){
                    Node addNode=new Node(checkState,node,node.operator+",pickup",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.drop();
                if(checkState!=null){
                    Node addNode=new Node(checkState,node,node.operator+",drop",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.retrieve();
                if(checkState!=null){
                    Node addNode=new Node(checkState,node,node.operator+",retrieve",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.right();
                if(checkState!=null && !lastOperator.equals("left")){
                    Node addNode=new Node(checkState,node,node.operator+",right",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.left();
                if(checkState!=null&& !lastOperator.equals("right")){
                    Node addNode=new Node(checkState,node,node.operator+",left",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.up();
                if(checkState!=null&& !lastOperator.equals("down")){
                    Node addNode=new Node(checkState,node,node.operator+",up",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.down();
                if(checkState!=null&& !lastOperator.equals("up")){
                    Node addNode=new Node(checkState,node,node.operator+",down",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }



            }

        }
        return "no solution";
    }
    public static String AStar(String grid, int heuristicNumber){
        int nodesNumber = 0;
        State firstState=new State(grid);
        Node firstNode=new Node(firstState,null,"",0,0);
        //note: correctly estimating initial capacity in priority queue decreases memory and time needed
        //maybe we can estimate according to grid size
        PriorityQueue<Node> nodes = new PriorityQueue<Node>(20, new NodeAStarComparator());
        nodes.add(firstNode);
        while(!nodes.isEmpty()){
            Node node = nodes.remove();
            if(node.state.isGoalState()){//Goal test
                String s=node.operator+";"+node.state.dead+";"+node.state.pickedUp+";"+nodesNumber;
                s=s.substring(1);
                existingStates = new HashSet<String>();
                return s;
            }
            else{
                String[] LastNode=node.operator.split(",",-1);
                String lastOperator=LastNode[LastNode.length-1];

                State checkState=node.pickUp();
                if(checkState!=null ){
                    Node addNode=new Node(checkState,node,node.operator+",pickup",node.depth+1,checkState.dead);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.drop();
                if(checkState!=null){
                    Node addNode=new Node(checkState,node,node.operator+",drop",node.depth+1,checkState.dead);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.retrieve();
                int BoxesLeft=0;
                if(checkState!=null){
                    Node addNode=new Node(checkState,node,node.operator+",retrieve",node.depth+1,checkState.dead);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.right();
                if(checkState!=null && !lastOperator.equals("left")){
                    Node addNode=new Node(checkState,node,node.operator+",right",node.depth+1,checkState.dead);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.left();
                if(checkState!=null&& !lastOperator.equals("right")){
                    Node addNode=new Node(checkState,node,node.operator+",left",node.depth+1,checkState.dead);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.up();
                if(checkState!=null&& !lastOperator.equals("down")){
                    Node addNode=new Node(checkState,node,node.operator+",up",node.depth+1,checkState.dead);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }
                checkState=node.down();
                if(checkState!=null&& !lastOperator.equals("up")){
                    Node addNode=new Node(checkState,node,node.operator+",down",node.depth+1,checkState.dead);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);
                        nodesNumber++;
                    }
                }



            }

        }
        return "no solution";
    }

    public static void visualize(String initial,String last){

        State initial_state=new State(initial);
       // Node curr_Node=new Node(initial_state,null,null,0,0);


        String[] solution = last.split(";", -1);
        String[] actions = solution[0].split(",", -1);

        initial_state.print2d();

        for (int i = 0; i < actions.length; i++) {
            Node curr_Node=new Node(initial_state,null,null,0,0);
            switch (actions[i]) {
                case "right":
                    initial_state=curr_Node.right();
                    initial_state.print2d();
                    break;
                case "left":
                    initial_state=curr_Node.left();
                    initial_state.print2d();
                    break;
                case "up":
                    initial_state=curr_Node.up();
                    initial_state.print2d();
                    break;
                case "down":
                    initial_state=curr_Node.down();
                    initial_state.print2d();
                    break;
                case "pickup":
                    initial_state=curr_Node.pickUp();
                    initial_state.print2d();
                    break;
                case "drop":
                    initial_state=curr_Node.drop();
                    initial_state.print2d();
                    break;
                case "retrieve":
                    initial_state=curr_Node.retrieve();
                    initial_state.print2d();
                    break;
            }

            System.out.println("");


        }





        //print 2d Array with the intial state
        //saved till now / dead till now
        //where is the guardship(numberof people/blackboxes)[]
        //where is every station
        //where is every ship(passengers/black box health)



    }

    public static String solve(String grid, String strategy, boolean visual){
        String s="No Solution";
        switch (strategy) {
            case "BF":
                s=BFS(grid);
                if(visual){
                    visualize(grid,s);
                }
                return s;
            case "DF":
                s=DFS(grid);
                if(visual){
                    visualize(grid,s);
                }
                return s;
            case "ID":
                s=ID(grid);
                if(visual){
                    visualize(grid,s);
                }
                return s;
            case "GR1":
                s=greedy(grid, 1);
                if(visual){
                    visualize(grid,s);
                }
                return s;
            case "GR2":
                s=greedy(grid, 2);
                if(visual){
                    visualize(grid,s);
                }
                return s;
            case "AS1":
                s=AStar(grid, 1);
                if(visual){
                    visualize(grid,s);
                }
                return s;
            case "AS2":
                s=AStar(grid, 2);
                if(visual){
                    visualize(grid,s);
                }
                return s;
        }
        existingStates = new HashSet<String>();
        return s;
    }

    public static boolean isDuplicate(State state){
        String stateString = state.toString();
        if (existingStates.contains(stateString))
            return true;

        existingStates.add(stateString);
        return false;
    }

    public static void main (String []args){


        String grid= "8,5;60;4,6;2,7;3,4,37,3,5,93,4,0,40;";
       // String s1=solve(grid,"AS2",true);
        String s2=solve(grid,"BF",false);
        //"5,6;50;0,1;0,4,3,3;1,1,90;"
        //System.out.println(s1);
        System.out.println(s2);
        visualize(grid,s2);



    }
}
