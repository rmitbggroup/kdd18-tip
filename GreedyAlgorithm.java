package algorithm2;

import algorithm2.entity.Billboard;
import algorithm2.entity.BillboardSet;
import algorithm2.entity.Route;

import java.util.Iterator;
import java.util.List;

/**
 * Created by marco on 06/05/2017.
 */
public class GreedyAlgorithm {

    public static boolean lazyForward = true;


    public BillboardSet greedy(List<Billboard> remainingBillboards, double budget) {

        BillboardSet pickedBillboards = new BillboardSet();
        Billboard billboardMax = new Billboard();

        //choose max I and w < budget
        double maxI = -1;
        for(int i = 0; i< remainingBillboards.size(); i++){
            if(remainingBillboards.get(i).influence > maxI && remainingBillboards.get(i).charge <= budget)
                billboardMax = remainingBillboards.get(i);
        }

        //Collections.sort(remainingBillboards);    // 1. reorder by routes/charge
        InsertSort(remainingBillboards);
        double budgetRemains = budget;

        while (budgetRemains > 0) {
            Billboard firstBoard = remainingBillboards.get(0);

            if (firstBoard.charge < budgetRemains) {

                budgetRemains -= firstBoard.charge;
                pickOne(firstBoard, pickedBillboards, remainingBillboards);

            } else {

                remainingBillboards.remove(0);
                updateBillboards(remainingBillboards);  // if did not pick first billboard, make sure all billboards get updated
                Iterator<Billboard> iterator = remainingBillboards.iterator();

                while (iterator.hasNext()) {
                    Billboard billboard = iterator.next();

                    if (billboard.charge < budgetRemains) {
                        budgetRemains -= billboard.charge;
                        pickOne(billboard, pickedBillboards, remainingBillboards);
                        break;
                    } else
                        iterator.remove();
                }
            }
            if (remainingBillboards.size() == 0)
                break;
        }

        if(billboardMax.influence > pickedBillboards.influence){
            BillboardSet billboardSet = new BillboardSet();
            billboardSet.add(billboardMax);

            return billboardSet;
        }
        return pickedBillboards;
    }



    public void pickOne(Billboard billboard, BillboardSet pickedBoards, List<Billboard> remainingBoards) {

        pickedBoards.add(billboard);
        remainingBoards.remove(billboard);

        for (Route route : billboard.routes)
            route.influenced = true;

        if (lazyForward)
            updateBillboardsLazyForward(remainingBoards);
        else
            updateBillboards(remainingBoards);
    }



    // remove overlap influenced routeds
    public void updateBillboardsLazyForward(List<Billboard> billboards) {

        for (int i = 0; i < billboards.size(); i++) {

            Billboard currentBillboard = billboards.get(i);
            currentBillboard.updateInfluence();

            // lazy-forward prune
            if (i + 1 != billboards.size()) {

                Billboard nextBillboard = billboards.get(i + 1);

                if (currentBillboard.influencePerCharge >= nextBillboard.influencePerCharge) {
                    break; // if goes there then billboard after currentBillboard will not be select in the next round & not get updated
                }
            }

        }
        //Collections.sort(billboards);
        InsertSort(billboards);
    }


    public void updateBillboards(List<Billboard> billboards) {

        for (int i = 0; i < billboards.size(); i++) {

            Billboard billboard = billboards.get(i);
            billboard.updateInfluence();

        }
        //Collections.sort(billboards);
        InsertSort(billboards);
    }

    public void InsertSort(List<Billboard> billboards){

        for(int i = 1; i< billboards.size(); i++){
            Billboard billboard = billboards.get(i);
            int j = i-1;
            while (j >= 0 && billboard.compareTo(billboards.get(j)) == -1){
                billboards.set(j + 1, billboards.get(j));
                j--;
            }
            billboards.set(j + 1, billboard);
        }
    }

}