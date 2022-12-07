package code;


public class Node {
    State state;
    String operator;
    int depth;
    int pathCost;

    public Node(State state,String oper,int dep,int cost){
        this.state = state;
        this.operator = oper;
        this.depth = dep;
        this.pathCost = cost;
    }

    @Override
    public String toString(){
        String s="";
        s+= this.state.toString();
        return s;
    }

    @Override
    public boolean equals(Object a){
        Node n = (Node) a ;
        if(n.state.equals(this.state) && this.operator.equals(n.operator) && this.depth == n.depth && n.pathCost == this.pathCost){
            return true;
        }else{
            return false;
        }
    }

    public Node moveLeft (){
        Node a = deepClone(this);
        a.state.checks();
        a.state.coastGuard_y--;
        if(a.state.coastGuard_y < 0 || a.state.coastGuard_y>a.state.n-1){
            return null;
        }
        a.operator+="left,";
        a.state.action();
        a.depth++;
        
        return a;
    }
    public Node moveUp (){
        Node a = deepClone(this);
        a.state.checks();
        a.state.coastGuard_x--;
        if(a.state.coastGuard_x < 0 || a.state.coastGuard_x>a.state.m-1){
            return null;
        }
        a.operator+="up,";
        a.state.action();
        a.depth++;
        
        return a;
    }
    public Node moveDown (){
        Node a = deepClone(this);
        a.state.checks();
        a.state.coastGuard_x++;
        if(a.state.coastGuard_x < 0 || a.state.coastGuard_x>a.state.m-1){
            return null;
        }
        a.operator+="down,";
        a.state.action();
        a.depth++;
        return a;
    }
    public Node moveRight (){
        Node a = deepClone(this);
        a.state.checks();
        a.state.coastGuard_y +=1;
   
        if(a.state.coastGuard_y < 0 || a.state.coastGuard_y>a.state.n-1){
            return null;
        }
        a.operator+="right,";
        a.depth++;
        a.state.action();
        return a;
    }
    public Node pickUp(){
        Node a = deepClone(this);
        a.state.checks();
        if(a.state.onShip){
            if(a.state.guard.numberOfPassengers == a.state.guard.capacity){return null;}
            a.state.guard.pickUp(a.state.currentShip);
            a.operator+="pickup,";
            a.state.action();
            a.depth++;
            return a;
        }
       
        return null;
    }
    public Node drop(){
        Node a = deepClone(this);
        a.state.checks();
        if(a.state.onStation){
            a.state.passengersSaved += a.state.guard.numberOfPassengers;
            a.state.guard.numberOfPassengers =0;
            a.state.action();
            a.operator+="drop,";
            a.depth++;
            return a;
        }
        return null;
    }
    public Node reitreive(){
        Node a = deepClone(this);
        a.state.checks();
        if(a.state.onWreck && a.state.currentShip.isReitrivable){
            a.state.guard.reitreiveBox(a.state.currentShip);
            a.state.currentShip.isReitrieved = true;
            a.state.blackBoxesRetreived++;
             a.operator+="retrieve,";
            a.state.action();
            a.depth++;
            return a;
        }
        return null ;
    }
    public Node deepClone(Node n){
        State s = n.state.deepClone();
        return new Node(s,n.operator,n.depth,n.pathCost);
    }


    
    
    
}
