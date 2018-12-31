package algorithm2;

import algorithm2.entity.Billboard;
import algorithm2.entity.BillboardSet;
import java.util.*;

public class PartitionBasedAlgorithm2 {

    final int step = 1000;

    EnumBasedGreedyAlgorithm enumBasedGreedyAlgorithm = new EnumBasedGreedyAlgorithm();

    public BillboardSet PartSel(List<List<Billboard>> PartitionSet, int Budget){
        int m = PartitionSet.size();
        BillboardSet[] InfluencedMaxSet = new BillboardSet[Budget+1];
        double ILowerBound[] = new double[Budget+1];

        for(int i = 1; i <= m; i++){
            System.out.println("计算第 "+i+" 个partition");
            BillboardSet[] InfluencedMaxSetTmp = new BillboardSet[Budget+1];
            BillboardSet[] InfluencedSet = new BillboardSet[Budget+1];

            for(int j = 0; j <= Budget; j=j+step){
                InfluencedSet[j] = enumBasedGreedyAlgorithm.FastEnum(PartitionSet.get(i-1), j);
            }

            for(int j = 0; j <= Budget; j=j+step){

                try{
                    for(int q = 0; q <= j; q=q+step){
                        if(InfluencedSet[q] != null && InfluencedMaxSet[j-q] != null && ILowerBound[j]<InfluencedMaxSet[j-q].influence+InfluencedSet[q].influence){
                            ILowerBound[j] = InfluencedMaxSet[j-q].influence+InfluencedSet[q].influence;
                            InfluencedMaxSetTmp[j] =(BillboardSet) InfluencedSet[q].clone();
                            InfluencedMaxSetTmp[j].addSet(InfluencedMaxSet[j-q]);
                        }else if(InfluencedSet[q] != null && InfluencedMaxSet[j-q] == null && ILowerBound[j]< InfluencedSet[q].influence){
                            ILowerBound[j] = InfluencedSet[q].influence;
                            InfluencedMaxSetTmp[j] =(BillboardSet) InfluencedSet[q].clone();
                        }else if(InfluencedSet[q] == null && InfluencedMaxSet[j-q] != null && ILowerBound[j]<InfluencedMaxSet[j-q].influence){
                            ILowerBound[j] = InfluencedMaxSet[j-q].influence;
                            InfluencedMaxSetTmp[j] =(BillboardSet) InfluencedMaxSet[j-q].clone();
                        }

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

                if(InfluencedMaxSetTmp[j] == null)
                    InfluencedMaxSetTmp[j] = InfluencedMaxSet[j];
            }

            InfluencedMaxSet = InfluencedMaxSetTmp;


            for(int j = 0; j <= Budget; j=j+step){
                if(InfluencedMaxSet[j]!=null)
                    ILowerBound[j] = InfluencedMaxSet[j].influence;
                else
                    ILowerBound[j] = 0;
            }

        }


        return InfluencedMaxSet[Budget];
    }

    public BillboardSet lazyProbe(List<List<Billboard>> PartitionSet, int Budget){
        int m = PartitionSet.size();
        double ILowerBound[] = new double[Budget+1];
        BillboardSet[] InfluencedMaxSet = new BillboardSet[Budget+1];


        for(int i = 1; i <= m; i++){

            System.out.println("Partition Set "+i);

            BillboardSet[] InfluencedMaxSetTmp = new BillboardSet[Budget+1];
            BillboardSet[] InfluencedSet = new BillboardSet[Budget+1];
            boolean[] key = new boolean[Budget+1];
            double EUpperBound[] = new double[Budget+1];

            for(int j = 0; j <= Budget; j=j+step){
                EUpperBound[j] = enumBasedGreedyAlgorithm.EstimateBound(PartitionSet.get(i-1),Budget);
            }

            for(int j = 0; j <= Budget; j=j+step){

                for(int q = 0; q <= j; q = q+step){

                    double eUpperBound = EUpperBound[q];


                    if(InfluencedMaxSet[j-q] != null)
                        eUpperBound += InfluencedMaxSet[j-q].influence;

                    if(ILowerBound[j] <= eUpperBound){

                        if(key[q] == false){
                            InfluencedSet[q] = enumBasedGreedyAlgorithm.FastEnum(PartitionSet.get(i-1), q);
                            key[q] = true;
                        }

                        try {
                            if (InfluencedSet[q] != null && InfluencedMaxSet[j - q] != null && ILowerBound[j] < InfluencedMaxSet[j - q].influence + InfluencedSet[q].influence) {
                                ILowerBound[j] = InfluencedMaxSet[j - q].influence + InfluencedSet[q].influence;
                                InfluencedMaxSetTmp[j] =(BillboardSet) InfluencedSet[q].clone();
                                InfluencedMaxSetTmp[j].addSet(InfluencedMaxSet[j - q]);
                            } else if (InfluencedSet[q] != null && InfluencedMaxSet[j - q] == null && ILowerBound[j] < InfluencedSet[q].influence) {
                                ILowerBound[j] = InfluencedSet[q].influence;
                                InfluencedMaxSetTmp[j] =(BillboardSet) InfluencedSet[q].clone();
                            } else if (InfluencedSet[q] == null && InfluencedMaxSet[j - q] != null && ILowerBound[j] < InfluencedMaxSet[j - q].influence) {
                                ILowerBound[j] = InfluencedMaxSet[j - q].influence;
                                InfluencedMaxSetTmp[j] =(BillboardSet) InfluencedMaxSet[j - q].clone();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                }

                if(InfluencedMaxSetTmp[j] == null)
                    InfluencedMaxSetTmp[j] = InfluencedMaxSet[j];
            }

            InfluencedMaxSet = InfluencedMaxSetTmp;

            for(int j = 0; j <= Budget; j=j+step){
                if(InfluencedMaxSet[j]!=null)
                    ILowerBound[j] = InfluencedMaxSet[j].influence;
                else
                    ILowerBound[j] = 0;
            }
        }

        return InfluencedMaxSet[Budget];
    }

}
