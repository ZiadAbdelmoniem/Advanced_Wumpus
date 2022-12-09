package code;

import java.util.*;


public class CoastGuard {

    /*
    hashset for storing state strings (refer to the overridden toString in State.java) in order to detect duplicate
    states
     */
    static HashSet<String> existingStates = new HashSet<String>();

    public static String genGrid(){

        String res="";
        String stations="";
        String ships="";
        //variable names here refer to grid format
        int M=(int) ((Math.random() * (15 - 5)) + 5);
        int N=(int) ((Math.random() * (15 - 5)) + 5);
        int C=(int) ((Math.random() * (100 - 30)) + 30);
        int cgX=(int) ((Math.random() * (M - 1)) + 1);
        int cgY=(int) ((Math.random() * (N - 1)) + 1);

        res+=M+","+N+";"+C+";"+cgX+","+cgY+";";

        int [][]myGrid=new int[M][N];

        //set to non-zero value to mark the cell in which the coast guard is in as taken
        // 9 = coast guard
        myGrid[cgX][cgY]=9;

        //mandatory station + ship
        boolean flag=true;
        while(flag){

            int stationX=(int) ((Math.random() * (M - 1)) + 1);
            int stationY=(int) ((Math.random() * (N - 1)) + 1);
            int shipX=(int) ((Math.random() * (M - 1)) + 1);
            int shipY=(int) ((Math.random() * (N - 1)) + 1);
            int passengersOnShip=(int) ((Math.random() * (100 - 1)) + 1);

            if(myGrid[stationX][stationY]==0 && myGrid[shipX][shipY]==0){
                //set positions of ship and station to non-zero values to mark as taken if not already taken
                // 1 = station
                // 2 = ship
                myGrid[stationX][stationY]=1;
                myGrid[shipX][shipY]=2;
                stations=stationX+","+stationY;
                ships=shipX+","+shipY+","+passengersOnShip;
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
        /*
        first we initialize a variable to calculate nodes expanded. then we initialize both first states and first node
        which are the starting point of the coast guard. then we create a queue to implement breadth first search and
        enqueue the first node. Afterwards we loop until the queue is empty, firstly checking if we reached a goal state. If we
        did, we return the string in the required format and then we reset the hashset which we use to store state strings
        to check for duplicates. If not, we then attempt to execute all the operators and enqueuing all possible nodes resulting
        from possible operators. Afterwards, we check to see if the resulting state is a duplicate (redundant state).
        For movements (up, down, left, right), we check to see that the node hasn't just come from that direction.
        For example: if the node is going to move right, we check that the last operator wasn't left as it would be
        useless for the coast guard to move left and then move back right. If he does, it would be a redundant state.
        If both these checks pass, we enqueue the node. If the queue is empty before we reach the goal state, then we
        return "no solution"
         */
        int nodesNumber = 0;
        State firstState=new State(grid);
        Node firstNode=new Node(firstState,null,"",0,0);
        Queue<Node> nodes = new LinkedList<>();//queue for bfc
        nodes.add(firstNode);
        while(!nodes.isEmpty()){
            Node node=nodes.remove();
            nodesNumber++;
            if(node.state.isGoalState()){//Goal test
                String s=node.operators +";"+node.state.dead+";"+node.state.blackboxesPickedUp +";"+nodesNumber;
                // to remove ";" at the beginning of the string
                s=s.substring(1);
                //resetting state string hashset
                existingStates = new HashSet<String>();
                return s;

            }
            else{
                //get last operator of the node to use later for checking redundant state.
                String[] operatorsArray=node.operators.split(",",-1);
                String lastOperator=operatorsArray[operatorsArray.length-1];

                //if operator can be executed, the state after execution is returned, else the method returns null
                State stateAfterOperator=node.pickUp();
                if(stateAfterOperator!=null ){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",pickup",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.add(addNode);

                    }
                }
                stateAfterOperator=node.drop();
                if(stateAfterOperator!=null){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",drop",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.add(addNode);

                    }
                }
                stateAfterOperator=node.retrieve();
                if(stateAfterOperator!=null){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",retrieve",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.add(addNode);

                    }
                }
                stateAfterOperator=node.right();
                if(stateAfterOperator!=null && !lastOperator.equals("left")){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",right",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {

                        nodes.add(addNode);

                    }
                }
                stateAfterOperator=node.left();
                if(stateAfterOperator!=null&& !lastOperator.equals("right")){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",left",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.add(addNode);

                    }
                }
                stateAfterOperator=node.up();
                if(stateAfterOperator!=null&& !lastOperator.equals("down")){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",up",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.add(addNode);

                    }
                }
                stateAfterOperator=node.down();
                if(stateAfterOperator!=null&& !lastOperator.equals("up")){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",down",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.add(addNode);

                    }
                }

            }
        }
        return "no solution";
    }
    public static String DFS(String grid){
        /*
        first we initialize a variable to calculate nodes expanded. then we initialize both first states and first node
        which are the starting point of the coast guard. then we create a stack to implement depth first search and
        push the first node. Afterwards we loop until the stack is empty, firstly checking if we reached a goal state. If we
        did, we return the string in the required format, and then we reset the hashset which we use to store state strings
        to check for duplicates. If not, we then attempt to execute all the operators and pushing all possible nodes resulting
        from possible operators. Afterwards, we check to see if the resulting state is a duplicate (redundant state).
        For movements (up, down, left, right), we check to see that the node hasn't just come from that direction.
        For example: if the coast guard is going to move right, we check that the last operator wasn't left as it would be
        useless for the coast guard to move left and then move back right. If he does, it would be a redundant state.
        If both these checks pass, we push the node. If the stack is empty before we reach the goal state, then we
        return "no solution"
         */
        int nodesNumber = 0;
        State firstState=new State(grid);
        Node firstNode=new Node(firstState,null,"",0,0);
        Stack<Node> nodes = new Stack<Node>(); // A stack used by IDS and DFS
        nodes.push(firstNode);
        while(!nodes.isEmpty()){
            Node node=nodes.pop();
            nodesNumber++;
            if(node.state.isGoalState()){//Goal test
                String s=node.operators +";"+node.state.dead+";"+node.state.blackboxesPickedUp +";"+nodesNumber;
                s=s.substring(1);
                existingStates = new HashSet<String>();
                return s;

            }
            else{
                //get last operator of the node to use later for checking redundant state.
                String[] operatorsArray=node.operators.split(",",-1);
                String lastOperator=operatorsArray[operatorsArray.length-1];

                State stateAfterOperator=node.pickUp();
                if(stateAfterOperator!=null ){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",pickup",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.push(addNode);

                    }
                }
                stateAfterOperator=node.drop();
                if(stateAfterOperator!=null){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",drop",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.push(addNode);

                    }
                }
                stateAfterOperator=node.retrieve();
                if(stateAfterOperator!=null){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",retrieve",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.push(addNode);

                    }
                }
                stateAfterOperator=node.right();
                if(stateAfterOperator!=null && !lastOperator.equals("left")){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",right",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {

                        nodes.push(addNode);

                    }
                }
                stateAfterOperator=node.left();
                if(stateAfterOperator!=null&& !lastOperator.equals("right")){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",left",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.push(addNode);

                    }
                }
                stateAfterOperator=node.up();
                if(stateAfterOperator!=null&& !lastOperator.equals("down")){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",up",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.push(addNode);

                    }
                }
                stateAfterOperator=node.down();
                if(stateAfterOperator!=null&& !lastOperator.equals("up")){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",down",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        nodes.push(addNode);

                    }
                }

            }
        }
        return "no solution";
    }
    public static String ID(String grid){
        /*
        first we initialize a variable called maxDepth to represent the iterative deepening (ID) search depth for each
        iteration. Afterwards, we start an infinite loop to represent the ID iterations. We also initialize a variable
        to calculate nodes expanded. then after we enter the first loop, we initialize both first states and first node
        which are the starting point of the coast guard. then we create a stack to implement iterative deepening search and
        push the first node. we reset the hashset that contains state strings that are used for comparison and
        checking duplicates as in IDS we might loop over the same node more than one time.
        Afterwards we loop until the stack is empty, firstly checking if we reached a goal state. If we did, we return
        the string in the required format, and then we reset the hashset which we use to store state strings
        to check for duplicates. If not, we check if we didn't exceed the maximum depth. If we did, we check if any of
        the remaining nodes in the stack has a goal state and if none do, the second loop terminates, and we start the
        next IDS iteration. If we haven't exceeded max depth, we then attempt to execute all the operators and pushing all possible nodes resulting
        from possible operators. Afterwards, we check to see if the resulting state is a duplicate (redundant state).
        For movements (up, down, left, right), we check to see that the node hasn't just come from that direction.
        For example: if the coast guard is going to move right, we check that the last operator wasn't left as it would be
        useless for the coast guard to move left and then move back right. If he does, it would be a redundant state.
        If both these checks pass, we push the node. There has to be some goal state so we keep doing this until reaching
        a goal state.
         */
        int maxDepth=0;
        int nodesNumber = 0;
        while(true) {
            State firstState=new State(grid);
            Node firstNode=new Node(firstState,null,"",0,0);
            Stack<Node> nodes = new Stack<Node>(); // A stack used by IDS and DFS
            nodes.push(firstNode);
            existingStates = new HashSet<String>();
            while (!nodes.isEmpty()) {
                Node node = nodes.pop();
                nodesNumber++;
                if (node.state.isGoalState()) {//Goal test
                    String s = node.operators + ";" + node.state.dead + ";" + node.state.blackboxesPickedUp + ";" + nodesNumber;
                    s = s.substring(1);
                    existingStates = new HashSet<String>();
                    return s;

                } else {
                    int childDepth = node.depth + 1;
                    if (childDepth <= maxDepth) {
                        //get last operator of the node to use later for checking redundant state.
                        String[] operatorsArray = node.operators.split(",", -1);
                        String lastOperator = operatorsArray[operatorsArray.length - 1];

                        State stateAfterOperator = node.pickUp();
                        if (stateAfterOperator != null) {
                            Node addNode = new Node(stateAfterOperator, node, node.operators + ",pickup", node.depth + 1, 0);
                            if (!isDuplicate(addNode.state)) {
                                nodes.push(addNode);

                            }
                        }
                        stateAfterOperator = node.drop();
                        if (stateAfterOperator != null) {
                            Node addNode = new Node(stateAfterOperator, node, node.operators + ",drop", node.depth + 1, 0);
                            if (!isDuplicate(addNode.state)) {
                                nodes.push(addNode);

                            }
                        }
                        stateAfterOperator = node.retrieve();
                        if (stateAfterOperator != null) {
                            Node addNode = new Node(stateAfterOperator, node, node.operators + ",retrieve", node.depth + 1, 0);
                            if (!isDuplicate(addNode.state)) {
                                nodes.push(addNode);

                            }
                        }
                        stateAfterOperator = node.right();
                        if (stateAfterOperator != null && !lastOperator.equals("left")) {
                            Node addNode = new Node(stateAfterOperator, node, node.operators + ",right", node.depth + 1, 0);
                            if (!isDuplicate(addNode.state)) {

                                nodes.push(addNode);

                            }
                        }
                        stateAfterOperator = node.left();
                        if (stateAfterOperator != null && !lastOperator.equals("right")) {
                            Node addNode = new Node(stateAfterOperator, node, node.operators + ",left", node.depth + 1, 0);
                            if (!isDuplicate(addNode.state)) {
                                nodes.push(addNode);

                            }
                        }
                        stateAfterOperator = node.up();
                        if (stateAfterOperator != null && !lastOperator.equals("down")) {
                            Node addNode = new Node(stateAfterOperator, node, node.operators + ",up", node.depth + 1, 0);
                            if (!isDuplicate(addNode.state)) {
                                nodes.push(addNode);

                            }
                        }
                        stateAfterOperator = node.down();
                        if (stateAfterOperator != null && !lastOperator.equals("up")) {
                            Node addNode = new Node(stateAfterOperator, node, node.operators + ",down", node.depth + 1, 0);
                            if (!isDuplicate(addNode.state)) {
                                nodes.push(addNode);

                            }
                        }


                    }
                }
            }
            maxDepth++;
        }
    }
    public static String greedy(String grid, int heuristicNumber){
        /*
        first we initialize a variable to calculate nodes expanded. then we initialize both first states and first node
        which are the starting point of the coast guard. then we create a priority queue to implement greedy search and
        enqueue the first node. Afterwards we loop and dequeue until the priority queue is empty, firstly checking if we reached a goal state. If we
        did, we return the string in the required format, and then we reset the hashset which we use to store state strings
        to check for duplicates. If not, we then attempt to execute all the operators and pushing all possible nodes resulting
        from possible operators. Afterwards, we check to see if the resulting state is a duplicate (redundant state).
        For movements (up, down, left, right), we check to see that the node hasn't just come from that direction.
        For example: if the coast guard is going to move right, we check that the last operator wasn't left as it would be
        useless for the coast guard to move left and then move back right. If he does, it would be a redundant state.
        If both these checks pass, we enqueue the node. If the stack is empty before we reach the goal state, then we
        return "no solution"
         */
        int nodesNumber = 0;
        State firstState=new State(grid);
        Node firstNode=new Node(firstState,null,"",0,0);
        PriorityQueue<Node> nodes = new PriorityQueue<Node>(20, new NodeGreedyComparator());
        nodes.add(firstNode);
        while(!nodes.isEmpty()){
            Node node = nodes.remove();
            nodesNumber++;
            if(node.state.isGoalState()){//Goal test
                String s=node.operators +";"+node.state.dead+";"+node.state.blackboxesPickedUp +";"+nodesNumber;
                s=s.substring(1);
                existingStates = new HashSet<String>();
                return s;
            }
            else{
                //get last operator of the node to use later for checking redundant state.
                String[] operatorsArray=node.operators.split(",",-1);
                String lastOperator=operatorsArray[operatorsArray.length-1];

                State stateAfterOperator=node.pickUp();
                if(stateAfterOperator!=null ){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",pickup",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);

                    }
                }
                stateAfterOperator=node.drop();
                if(stateAfterOperator!=null){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",drop",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);

                    }
                }
                stateAfterOperator=node.retrieve();
                if(stateAfterOperator!=null){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",retrieve",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);

                    }
                }
                stateAfterOperator=node.right();
                if(stateAfterOperator!=null && !lastOperator.equals("left")){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",right",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);

                    }
                }
                stateAfterOperator=node.left();
                if(stateAfterOperator!=null&& !lastOperator.equals("right")){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",left",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);

                    }
                }
                stateAfterOperator=node.up();
                if(stateAfterOperator!=null&& !lastOperator.equals("down")){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",up",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);

                    }
                }
                stateAfterOperator=node.down();
                if(stateAfterOperator!=null&& !lastOperator.equals("up")){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",down",node.depth+1,0);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);

                    }
                }



            }

        }
        return "no solution";
    }
    public static String AStar(String grid, int heuristicNumber){
        /*
        first we initialize a variable to calculate nodes expanded. then we initialize both first states and first node
        which are the starting point of the coast guard. then we create a priority queue to implement A star search and
        enqueue the first node. Afterwards we loop and dequeue until the priority queue is empty, firstly checking if we reached a goal state. If we
        did, we return the string in the required format, and then we reset the hashset which we use to store state strings
        to check for duplicates. If not, we then attempt to execute all the operators and pushing all possible nodes resulting
        from possible operators. Afterwards, we check to see if the resulting state is a duplicate (redundant state).
        For movements (up, down, left, right), we check to see that the node hasn't just come from that direction.
        For example: if the coast guard is going to move right, we check that the last operator wasn't left as it would be
        useless for the coast guard to move left and then move back right. If he does, it would be a redundant state.
        If both these checks pass, we enqueue the node. If the stack is empty before we reach the goal state, then we
        return "no solution"
         */
        int nodesNumber = 0;
        State firstState=new State(grid);
        Node firstNode=new Node(firstState,null,"",0,0);
        PriorityQueue<Node> nodes = new PriorityQueue<Node>(20, new NodeAStarComparator());
        nodes.add(firstNode);
        while(!nodes.isEmpty()){
            Node node = nodes.remove();
            nodesNumber++;
            if(node.state.isGoalState()){//Goal test
                String s=node.operators +";"+node.state.dead+";"+node.state.blackboxesPickedUp +";"+nodesNumber;
                s=s.substring(1);
                existingStates = new HashSet<String>();
                return s;
            }
            else{
                //get last operator of the node to use later for checking redundant state.
                String[] operatorsArray=node.operators.split(",",-1);
                String lastOperator=operatorsArray[operatorsArray.length-1];

                State stateAfterOperator=node.pickUp();
                if(stateAfterOperator!=null ){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",pickup",node.depth+1,stateAfterOperator.dead*10-stateAfterOperator.blackboxesPickedUp);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);

                    }
                }
                stateAfterOperator=node.drop();
                if(stateAfterOperator!=null){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",drop",node.depth+1,stateAfterOperator.dead*10-stateAfterOperator.blackboxesPickedUp);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);

                    }
                }
                stateAfterOperator=node.retrieve();
                int BoxesLeft=0;
                if(stateAfterOperator!=null){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",retrieve",node.depth+1,stateAfterOperator.dead*10-stateAfterOperator.blackboxesPickedUp);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);

                    }
                }
                stateAfterOperator=node.right();
                if(stateAfterOperator!=null && !lastOperator.equals("left")){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",right",node.depth+1,stateAfterOperator.dead*10-stateAfterOperator.blackboxesPickedUp);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);

                    }
                }
                stateAfterOperator=node.left();
                if(stateAfterOperator!=null&& !lastOperator.equals("right")){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",left",node.depth+1,stateAfterOperator.dead*10-stateAfterOperator.blackboxesPickedUp);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);

                    }
                }
                stateAfterOperator=node.up();
                if(stateAfterOperator!=null&& !lastOperator.equals("down")){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",up",node.depth+1,stateAfterOperator.dead*10-stateAfterOperator.blackboxesPickedUp);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);

                    }
                }
                stateAfterOperator=node.down();
                if(stateAfterOperator!=null&& !lastOperator.equals("up")){
                    Node addNode=new Node(stateAfterOperator,node,node.operators +",down",node.depth+1,stateAfterOperator.dead*10-stateAfterOperator.blackboxesPickedUp);
                    if(!isDuplicate(addNode.state)) {
                        addNode.calculateHeuristic(heuristicNumber);
                        nodes.add(addNode);

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
        // switching over different search algorithms using the symbols
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
        //reset states hashset
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


        String grid0 = "5,6;50;0,1;0,4,3,3;1,1,90;";
        String grid1 = "6,6;52;2,0;2,4,4,0,5,4;2,1,19,4,2,6,5,0,8;";
        String grid2 = "7,5;40;2,3;3,6;1,1,10,4,5,90;";
        String grid3 = "8,5;60;4,6;2,7;3,4,37,3,5,93,4,0,40;";
        String grid4 = "5,7;63;4,2;6,2,6,3;0,0,17,0,2,73,3,0,30;";
        String grid5 = "5,5;69;3,3;0,0,0,1,1,0;0,3,78,1,2,2,1,3,14,4,4,9;";
        String grid6 = "7,5;86;0,0;1,3,1,5,4,2;1,1,42,2,5,99,3,5,89;";
        String grid7= "6,7;82;1,4;2,3;1,1,58,3,0,58,4,2,72;";
        String grid8 = "6,6;74;1,1;0,3,1,0,2,0,2,4,4,0,4,2,5,0;0,0,78,3,3,5,4,3,40;";
        String grid9 = "7,5;100;3,4;2,6,3,5;0,0,4,0,1,8,1,4,77,1,5,1,3,2,94,4,3,46;";

        String s1=solve(grid0,"AS1",false);
        String s2=solve(grid0,"AS2",false);
        String s3=solve(grid1,"AS1",false);
        String s4=solve(grid1,"AS2",false);
        String s5=solve(grid2,"AS1",false);
        String s6=solve(grid2,"AS2",false);
        String s7=solve(grid3,"AS1",false);
        String s8=solve(grid3,"AS2",false);
        String s9=solve(grid4,"AS1",false);
        String s10=solve(grid4,"AS2",false);
        String s11=solve(grid5,"AS1",false);
        String s12=solve(grid5,"AS2",false);
        String s13=solve(grid6,"AS1",false);
        String s14=solve(grid6,"AS2",false);
        String s15=solve(grid7,"AS1",false);
        String s16=solve(grid7,"AS2",false);
        String s17=solve(grid8,"AS1",false);
        String s18=solve(grid8,"AS2",false);
        String s19=solve(grid9,"AS1",false);
        String s20=solve(grid9,"AS2",false);

        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
        System.out.println(s4);
        System.out.println(s5);
        System.out.println(s6);
        System.out.println(s7);
        System.out.println(s8);
        System.out.println(s9);
        System.out.println(s10);
        System.out.println(s11);
        System.out.println(s12);
        System.out.println(s13);
        System.out.println(s14);
        System.out.println(s15);
        System.out.println(s16);
        System.out.println(s17);
        System.out.println(s18);
        System.out.println(s19);
        System.out.println(s20);

    }
}
