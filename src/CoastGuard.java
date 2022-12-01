public class CoastGuard {

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

    public static void Solve(String grid, String stratgy, boolean visual){}


    public static void main (String []args){

        //grid=GenGrid()
        //Solve(grid,"bf",true)
        System.out.print(GenGrid());

    }
}
