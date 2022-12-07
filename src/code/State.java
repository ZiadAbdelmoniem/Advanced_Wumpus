package code;
import java.util.ArrayList;

public class State {
    int m;
    int n;
    
    int totalPassengers;
    CoastGuard guard;
    int coastGuard_x;
    int coastGuard_y;
    Ship currentShip;
    Station currentStation;
    boolean onShip = false;
    boolean onStation =false;
    boolean onWreck = false;

    
    ArrayList<Station> stations = new ArrayList<Station>();
    ArrayList<Ship> ships = new ArrayList<Ship>();

    int deadPassengers=0;
    int passengersSaved;
    int blackBoxesRetreived;

    boolean isGoal;

    @Override
    public String toString(){
        String s = "";
        s+= this.totalPassengers + this.guard.toString()+this.coastGuard_x+this.coastGuard_y+this.deadPassengers+this.passengersSaved+this.blackBoxesRetreived;
        return s;
    }
    public boolean equals(State s){
        if(s.totalPassengers == this.totalPassengers && this.guard.equal(s.guard) 
        && this.coastGuard_x == s.coastGuard_x && this.coastGuard_y == s.coastGuard_y
        &&this.deadPassengers == s.deadPassengers && this.passengersSaved == s.passengersSaved && this.blackBoxesRetreived == s.blackBoxesRetreived
        ){
            return true;
        }else{
            return false;
        }
    }
    public State(String grid,CoastGuard guard){
        this.guard = guard;
        //parse el grid ba2a hena
            String[] split = grid.split(";");
       
            //Awel index heya el width wel height
           String[]dimensions = split[0].split(",");
            m= Integer.parseInt(dimensions[0]);
            n= Integer.parseInt(dimensions[1]);
            //Tany index Capacity Coast Guard 
            guard.setCapacity(Integer.parseInt(split[1])); 
            //Talta location coast guard
            String[]location = split[2].split(",");
            coastGuard_x = Integer.parseInt(location[0]);
            coastGuard_y = Integer.parseInt(location[1]);
            //rab3a locations of stations
            String[] stationsLocations = split[3].split(",");
            int locx = -1;
            int locy = -1;
            for(int i=0;i<stationsLocations.length;i++){    
                if(i%2==0){
                    locx = Integer.parseInt(stationsLocations[i]);
                }else{
                    locy = Integer.parseInt(stationsLocations[i]);
                    stations.add(new Station(locx,locy));
                }
            }
            //5amsa location and number of passengers of ship
            String[] shipsDetails = split[4].split(",");
            locx = -1;
            locy = -1;
            for(int i =0 ;i<shipsDetails.length;i++){
                if(i%3==0){
                    locx = Integer.parseInt(shipsDetails[i]);
                }
                else if(i%3==1){
                    locy = Integer.parseInt(shipsDetails[i]);
                }
                else{
                    totalPassengers+=Integer.parseInt(shipsDetails[i]);
                    int passengers = Integer.parseInt(shipsDetails[i]);
                    ships.add(new Ship(passengers,locx,locy));
                }
            }



        

    }
    public State(int saved,int died,boolean onShip,boolean onWreck,Ship ship ,boolean onStation,int reitreived,ArrayList<Ship>ships,int m, int n,int totalPassengers,
    CoastGuard guard,int x,int y,ArrayList<Station> stations){
        this.passengersSaved =saved;
        this.deadPassengers = died;
        this.blackBoxesRetreived = reitreived;
        this.currentShip = ship;
        this.ships = new ArrayList<Ship>();
        for(int i=0;i<ships.size();i++){
            this.ships.add(ships.get(i).deepClone());
        }
        this.m = m;
        this.n = n;
        this.totalPassengers = totalPassengers;
        this.guard=guard.deepClone();
        this.guard.setCapacity(guard.capacity);
        this.coastGuard_x = x;
        this.coastGuard_y = y;
        
        for(int i=0;i<stations.size();i++){
            this.stations.add(stations.get(i));
        }


    }
   
   public void checks(){
    boolean setGoal = true;
    boolean setShip = false;
    boolean setStation = false;
    boolean setWreck = false;

    for(int i=0;i<ships.size();i++){
        Ship current1 = ships.get(i);
        if(coastGuard_x == current1.locationX && coastGuard_y ==current1.locationY){
            setShip = true;
            currentShip = current1;
            if(currentShip.isWreck){
                setWreck = true;
            }
        }
        if(current1.numberOfPassengers !=0 || (current1.isReitrivable && !current1.isReitrieved) ||guard.numberOfPassengers !=0) {
            setGoal =false;
        } 
        
    }
    
    for(int i=0;i<stations.size();i++){
        Station current = stations.get(i);
        if(current.locationX == coastGuard_x && current.locationY == coastGuard_y){
            setStation = true;
            currentStation = current;
        }
    }
    this.onShip = setShip;
    this.onStation = setStation;
    this.isGoal = setGoal;
    this.onWreck = setWreck;
   }
   
    public void action(){
        for(int i=0;i<ships.size();i++){
            //Check if action reduced number of passengers
            boolean reduced = ships.get(i).action();
            if(reduced){
                deadPassengers++;
            }
        }

   }


   

    public State deepClone(){
        return new State(this.passengersSaved,this.deadPassengers,this.onShip,this.onWreck,this.currentShip,this.onStation,this.blackBoxesRetreived,this.ships,this.m,this.n,this.totalPassengers,this.guard,this.coastGuard_x
        ,this.coastGuard_y,this.stations);

    }


}
