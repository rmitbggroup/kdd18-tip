
package algorithm2;

import algorithm2.entity.Billboard;
import algorithm2.entity.BillboardSet;
import algorithm2.entity.Route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by marco on 18/05/2017.
 */
public class EnumBasedGreedyAlgorithm {

    final int step = 1000;
    final boolean FastEnum = true;


  
    public BillboardSet EnumSel(List<Billboard> PartitionOneSet, int Budget){

        BillboardSet InfluencedSet = new BillboardSet();

        BillboardSet InfluencedSet1 = getOneMaxInfluence(PartitionOneSet, Budget);
        BillboardSet InfluencedSet2 = getTwoMaxInfluence(PartitionOneSet, Budget);

        if(InfluencedSet1!=null && InfluencedSet2!=null){
            if(InfluencedSet1.influence > InfluencedSet2.influence)
                InfluencedSet = InfluencedSet1;
            else
                InfluencedSet = InfluencedSet2;
        }else if(InfluencedSet1 == null)
            InfluencedSet = InfluencedSet2;
        else{
            InfluencedSet = InfluencedSet1;
        }

        return InfluencedSet;

    }


    //FastEnum: Establish an upper bound for all the solutions that contain b
    public BillboardSet FastEnum(List<Billboard> PartitionOneSet, int Budget){

        BillboardSet InfluencedSet = new BillboardSet();

        EstimateBillboardBound(PartitionOneSet,(double)Budget);

        BillboardSet InfluencedSet1 = getOneMaxInfluence(PartitionOneSet, Budget);
        BillboardSet InfluencedSet2 = getTwoMaxInfluence(PartitionOneSet, Budget);

        if(InfluencedSet1!=null && InfluencedSet2!=null){
            if(InfluencedSet1.influence > InfluencedSet2.influence)
                InfluencedSet = InfluencedSet1;
            else
                InfluencedSet = InfluencedSet2;
        }else if(InfluencedSet1 == null)
            InfluencedSet = InfluencedSet2;
        else{
            InfluencedSet = InfluencedSet1;
        }

        return InfluencedSet;

    }


    // the phase one (|Billboards| = 1、2)
    public BillboardSet getOneMaxInfluence(List<Billboard> PartitionOneSet, int Budget) {

        BillboardSet InfluencedSet = new BillboardSet();
        // choose one billboard

        for(int i = 0; i < PartitionOneSet.size(); i++){
            if((InfluencedSet==null || PartitionOneSet.get(i).influence > InfluencedSet.influence) && PartitionOneSet.get(i).charge <= Budget){
                BillboardSet InfluenceSet = new BillboardSet();
                InfluenceSet.add(PartitionOneSet.get(i));
                InfluencedSet = InfluenceSet;
            }
        }

        // choose two billboard

        for(int i = 0; i < PartitionOneSet.size(); i++){
            for(int k = i + 1; k < PartitionOneSet.size(); k++){
                if((InfluencedSet==null || (PartitionOneSet.get(i).influence + PartitionOneSet.get(k).influence) > InfluencedSet.influence) && ((PartitionOneSet.get(i).charge + PartitionOneSet.get(k).charge) <= Budget)){
                    BillboardSet InfluenceSet = new BillboardSet();
                    InfluenceSet.add(PartitionOneSet.get(i));
                    InfluenceSet.add(PartitionOneSet.get(k));
                    InfluencedSet = InfluenceSet;
                }
            }
        }

        return InfluencedSet;

    }

    //the phase two (|Billboards| >= 3)
    public BillboardSet getTwoMaxInfluence(List<Billboard> PartitionOneSet, int Budget){

        BillboardSet InfluencedSet = new BillboardSet();
		
        List<GreedyParameter> greedyParameters = generateOneGreedyParameters(PartitionOneSet, Budget);

        if(FastEnum){
            InfluencedSet = EnumTwo(greedyParameters, PartitionOneSet, Budget );
        }else{
            InfluencedSet = EnumPhaseTwo(greedyParameters, PartitionOneSet, Budget );
        }


        return InfluencedSet;
    }

	//enumerate size-3 combinations
    private List<GreedyParameter> generateOneGreedyParameters(List<Billboard> PartitionOneSet, int Budget) {

        List<GreedyParameter> parameterList = new ArrayList<>();

        for (int i = 0; i < PartitionOneSet.size()-2; i++) {

            for (int j = i+1; j < PartitionOneSet.size()-1; j++) {

                for (int k = j+1; k < PartitionOneSet.size(); k++) {

                    GreedyParameter parameter = new GreedyParameter();

                    parameter.firstBillboardIndex = i;
                    parameter.secondBillboardIndex = j;
                    parameter.thirdBillboardIndex = k;

                    if(Budget - PartitionOneSet.get(i).charge - PartitionOneSet.get(j).charge - PartitionOneSet.get(k).charge > 0)
                        parameterList.add(parameter);
                }
            }
        }
        return parameterList;
    }

    public class GreedyParameter {

        public double budgetRemains;

        public int firstBillboardIndex;

        public int secondBillboardIndex;

        public int thirdBillboardIndex;


        @Override
        public String toString() {

            String result = "";
            result += "budgetRemains : " + budgetRemains + "\n";
            result += "firstBillboardIndex : " + firstBillboardIndex + "\n";
            result += "secondBillboardIndex : " + secondBillboardIndex + "\n";
            result += "thirdBillboardIndex : " + thirdBillboardIndex + "\n";

            return result;
        }

    }

    private BillboardSet EnumPhaseTwo(List<GreedyParameter> parameterList,final List<Billboard> PartitionOneSet, int Budget){

        BillboardSet InfluencedSet = new BillboardSet();




        for (int i = 0; i < parameterList.size(); i++) {

            GreedyParameter parameters = parameterList.get(i);

            List<Billboard> partitionOneSet = new ArrayList<>();
            partitionOneSet.addAll(PartitionOneSet);

            if(parameters.firstBillboardIndex < partitionOneSet.size() && parameters.secondBillboardIndex < partitionOneSet.size() && parameters.thirdBillboardIndex < partitionOneSet.size()){

                parameters.budgetRemains = Budget - partitionOneSet.get(parameters.firstBillboardIndex).charge - partitionOneSet.get(parameters.secondBillboardIndex).charge
                        - partitionOneSet.get(parameters.thirdBillboardIndex).charge;
                if(parameters.budgetRemains >= 0){

                    BillboardSet billboardSet = callGreedy(parameters, partitionOneSet);
                    try {
                        if (InfluencedSet != null && InfluencedSet.influence < billboardSet.influence){
                            InfluencedSet = (BillboardSet)billboardSet.clone();
                        }else if(InfluencedSet == null){
                            InfluencedSet = (BillboardSet)billboardSet.clone();
                        }
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }


                }
            }
        }
        return InfluencedSet;

    }
	
    private BillboardSet EnumTwo(List<GreedyParameter> parameterList,final List<Billboard> PartitionOneSet, int Budget){

        int LowerBound = 0;


        BillboardSet InfluencedSet = new BillboardSet();

        List<Billboard> partitionSet = new ArrayList<>();
        partitionSet.addAll(PartitionOneSet);

        Collections.sort(partitionSet);

        for(int i=0;i<partitionSet.size()-2;i++){
            if(partitionSet.get(i).charge + partitionSet.get(i+1).charge + partitionSet.get(i+2).charge <Budget){
                GreedyParameter parameters = new GreedyParameter();
                parameters.firstBillboardIndex = i;
                parameters.secondBillboardIndex = i+1;
                parameters.thirdBillboardIndex = i+2;

                BillboardSet billboardSet = callGreedy(parameters, partitionSet);

                if (billboardSet != null){
                    LowerBound = billboardSet.influence;
                    break;
                }
            }
        }



        for (int i = 0; i < parameterList.size(); i++) {

            GreedyParameter parameters = parameterList.get(i);

            List<Billboard> partitionOneSet = new ArrayList<>();
            partitionOneSet.addAll(PartitionOneSet);

            if(parameters.firstBillboardIndex < partitionOneSet.size() && parameters.secondBillboardIndex < partitionOneSet.size() && parameters.thirdBillboardIndex < partitionOneSet.size()){
				
				//Use the upper bound for pruning
                if((double)partitionOneSet.get(parameters.firstBillboardIndex).BoundMap.get(Budget) <= LowerBound ||
                        (double)partitionOneSet.get(parameters.secondBillboardIndex).BoundMap.get(Budget) <= LowerBound ||
                        (double)partitionOneSet.get(parameters.thirdBillboardIndex).BoundMap.get(Budget) <= LowerBound) {
                    continue;
                }


                parameters.budgetRemains = Budget - partitionOneSet.get(parameters.firstBillboardIndex).charge - partitionOneSet.get(parameters.secondBillboardIndex).charge
                        - partitionOneSet.get(parameters.thirdBillboardIndex).charge;
                if(parameters.budgetRemains >= 0){

                    BillboardSet billboardSet = callGreedy(parameters, partitionOneSet);
                    try {
                        if (InfluencedSet != null && InfluencedSet.influence < billboardSet.influence){
                            InfluencedSet = (BillboardSet)billboardSet.clone();
                        }else if(InfluencedSet == null){
                            InfluencedSet = (BillboardSet)billboardSet.clone();
                        }
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }


                    if(InfluencedSet.influence > LowerBound)
                    {
                        LowerBound = InfluencedSet.influence;
                    }
                }
            }
        }
        return InfluencedSet;

    }

	//call greedy algorithm to make up the gain of size-3 combination
    private BillboardSet callGreedy(GreedyParameter parameters, List<Billboard> PartitionOneSet) {

        GreedyAlgorithm greedyAlgorithm = new GreedyAlgorithm();

        BillboardSet billboardSet = new BillboardSet();

        List<Billboard> allBillboards = PartitionOneSet;

        Billboard firstBillboard = allBillboards.get(parameters.firstBillboardIndex);
        Billboard secondBillboard = allBillboards.get(parameters.secondBillboardIndex);
        Billboard thirdBillboard = allBillboards.get(parameters.thirdBillboardIndex);

        billboardSet.add(firstBillboard);
        billboardSet.add(secondBillboard);
        billboardSet.add(thirdBillboard);

        // remove 3 already picked billboards and update remain billboards influences
        allBillboards.remove(firstBillboard);
        allBillboards.remove(secondBillboard);
        allBillboards.remove(thirdBillboard);

        for (Route route : firstBillboard.routes)
            route.influenced = true;

        for (Route route : secondBillboard.routes)
            route.influenced = true;

        for (Route route : thirdBillboard.routes)
            route.influenced = true;

        greedyAlgorithm.updateBillboardsLazyForward(allBillboards);

        BillboardSet billboardSet2 = greedyAlgorithm.greedy(allBillboards, parameters.budgetRemains);

        billboardSet.addSet(billboardSet2);

        return billboardSet;
    }
	
	
	// Estimate the upper bound of gain for a given budget on a billboard collection
    public double EstimateBound(List<Billboard> PartitionOneSet, double Budget){

        List<Billboard> remainingBillboards = new ArrayList<>();
        remainingBillboards.addAll(PartitionOneSet);
        BillboardSet pickedBillboards = new BillboardSet();
        Billboard firstOne = new Billboard();

        GreedyAlgorithm greedyAlgorithm = new GreedyAlgorithm();
        greedyAlgorithm.updateBillboards(remainingBillboards);
        greedyAlgorithm.InsertSort(remainingBillboards);

        double budgetRemains = Budget;

        while (budgetRemains > 0) {
            Billboard firstBoard = new Billboard();
            if(remainingBillboards.size() > 0)
                firstBoard = remainingBillboards.get(0);

            if (firstBoard.charge < budgetRemains) {

                budgetRemains -= firstBoard.charge;
                greedyAlgorithm.pickOne(firstBoard, pickedBillboards, remainingBillboards);

            } else {

                firstOne = firstBoard;
                break;

            }
            if (remainingBillboards.size() == 0)
                break;
        }

        double result = pickedBillboards.influence + firstOne.influencePerCharge*budgetRemains;

        return result;
    }


	// Estimate the upper bound for all billboard in given budget
    public void EstimateBillboardBound(List<Billboard> PartitionOneSet, double Budget){
        for(int j=0;j<PartitionOneSet.size();j++){

            List<Billboard> PartitionSet = new ArrayList<>();
            PartitionSet.addAll(PartitionOneSet);

            for(int i=0;i<PartitionSet.size();i++){
                if(i==j){
                    for(Route route:PartitionSet.get(i).routes)
                        route.influenced=true;
                }else{
                    for(Route route:PartitionSet.get(i).routes)
                        route.influenced=false;
                }
            }

            PartitionSet.remove(PartitionOneSet.get(j));

            double result = EstimateBound(PartitionSet,Budget - PartitionOneSet.get(j).charge);
            PartitionOneSet.get(j).BoundMap.put((int)Budget,result + PartitionOneSet.get(j).influence);
        }

    }

}



