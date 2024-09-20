import java.util.List;
import java.util.ArrayList;

public class TripletSum {
    public static void main(String[] args) {
        TripletSum t = new TripletSum();

        int[] numbers = {-1,0,1,2,-1,-4};

        System.out.println(t.threeSum(numbers));
    }

    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> triplets = new ArrayList<List<Integer>>();
        ArrayList<Integer> threes = new ArrayList<>();

        for (int r = 0; r < nums.length - 2; r++) {
            for (int i = r + 1; i < nums.length - 1; i++) {
                for (int y = i + 1; y < nums.length; y++) {
                    if (nums[y] + nums[i] + nums[r] == 0) {
                        threes.add(0, nums[r]);
                        threes.add(1, nums[i]);
                        threes.add(2, nums[y]);

                        triplets.add(new ArrayList<Integer>(threes));

                        threes.clear();
                    }
                }
            }
        }

        int validDup = 0;

        // clear duplicates

        for (int i = 0; i < triplets.size(); i++) {
            for (int j = 0; j < triplets.size(); j++) {
                for (int counter = 0; counter < 3; counter++) {
                    for (int k = 0; k < 3; k++) {
                        if (triplets.get(i).get(counter) == triplets.get(j).get(k)) {
                            validDup++;
                        }
                    }
                }

                if (validDup == 3) {
                    triplets.remove(j);

                    validDup = 0;
                }
            }
        }

        return triplets;
    }
}
