package algorithm2.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by marco on 18/05/2017.
 */


// used to store info from billboardFinalResult.txt
// <-- billboard & its routes -->     (routes that got influenced by this billboard)

public class Billboard implements Comparable<Billboard> {


    public String panelID;  // panelID = billboardID

    public int influence;   // number of routes get influenced

    public double charge;  //  cost

    public double influencePerCharge;   // initialized as routes.size() / charge

    public Set<Route> routes;  // googleRouteID (used to store routes influenced by this billboard)

    public  Map BoundMap ;     // the upper bound of this billboard within a given budget


    public Billboard() {

        routes = new TreeSet<>();
        BoundMap = new HashMap<Integer, Double>();
    }


    public void updateInfluence() {

        int counter = 0;

        for (Route route : routes) {
            if (!route.influenced) // if not influenced by already picked billboards
                counter += route.routeNum;         // add one influence
        }

        influence = counter;
        influencePerCharge = influence / charge;
    }


    @Override
    public String toString() {

        String result = "";

        result += "panelID : " + panelID + "\n";
        result += "influence : " + influence + "\n";
        result += "charge : " + charge + "\n";
        result += "influence per charge : " + influencePerCharge + "\n";
        result += "routes : ";

        for (Route route : routes)
            result += (route.routeID + ", ");

        return result;
    }


    // order by descending order (9 8 7 6 5 4 3 2 1)

    @Override
    public int compareTo(Billboard o) {

        double difference =  o.influencePerCharge - influencePerCharge;

        if (difference > 0)
            return 1;
        else if (difference < 0)
            return -1;
        else
            return 0;
    }
}
