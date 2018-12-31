package algorithm2.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by marco on 18/05/2017.
 */
public class BillboardSet implements Cloneable {

    public int numberOfBillboards = 0;  // number Of Billboards

    public int influence = 0;           // Number of route affected

    public List<String> billboards;    // panelID~charge

    public Set<Integer> routeIDs;       // googleRouteID


    public BillboardSet() {

        billboards = new ArrayList<>();
        routeIDs = new TreeSet<>();
    }


    public void add(Billboard billboard) {

        billboards.add(billboard.panelID + "~" + billboard.charge);

        influence+=billboard.influence;

        for (Route route : billboard.routes){
            routeIDs.add(route.routeID);
            //influence += route.routeNum;
        }
        numberOfBillboards++;
    }


    public void addSet(BillboardSet set) {

        numberOfBillboards += set.numberOfBillboards;
        billboards.addAll(set.billboards);
        routeIDs.addAll(set.routeIDs);
        influence += set.influence;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {

        BillboardSet cloneBillboardSet = (BillboardSet) super.clone();

        List<String> cloneBillboards = new ArrayList<>();
        Set<Integer> cloneRoutes = new TreeSet<>();

        try {
            for (String item : billboards)
                cloneBillboards.add(item);
            for (Integer item : routeIDs)
                cloneRoutes.add(item);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cloneBillboardSet.billboards = cloneBillboards;
        cloneBillboardSet.routeIDs = cloneRoutes;

        return cloneBillboardSet;
    }


    @Override
    public String toString() {

        String result = "";
        result += "billboard id : ";

        for (String billboard : billboards)
            result += billboard.split("~")[0] + ", ";

        result += "\nbillboard cost : ";

        for (String billboard : billboards) {
            result += billboard.split("~")[1] + ", ";
         //   int sum=(int)billboard.split("~")[1];
       //     result += "\ntotal cost : "+sum;
        }
        result += "\nnumber of billboards: " + billboards.size();
        result += "\nnumber of routes get influenced: " + influence;

        result += "\n-----------------------------------------------------";

        return result;
    }
}
