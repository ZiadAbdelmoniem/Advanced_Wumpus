package code;

import java.util.Comparator;

public class NodeAStarComparator implements Comparator<Node> {
    public int compare(Node n1, Node n2){
        if((n1.heuristic_cost+n1.path_cost)>(n2.heuristic_cost+ n2.path_cost))
            return 1;
        else if ((n1.heuristic_cost+n1.path_cost)< (n2.heuristic_cost+ n2.path_cost))
            return -1;
        return 0;
    }
}
