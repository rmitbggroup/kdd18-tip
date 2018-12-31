package algorithm2.entity;

/**
 * Created by marco on 04/06/2017.
 */



public class Route implements Comparable<Route> {

    public int routeID;     // route id

    public boolean influenced;  // Is it affected

    public int routeNum;        // the same number of trajectories


    @Override
    public int compareTo(Route o) {

        return routeID - o.routeID;
    }

}
