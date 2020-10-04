package Mentalist.utils;

import java.util.Arrays;

public class Offer {
    private int[][] offer;

    private int num_issues;

    public Offer(int num_issues) {
        this.num_issues = num_issues;
        this.offer = new int[num_issues][3];
    }

    public int[] getItem(int index) {
        return this.offer[index];
    }

    public int getIssueCount() {
        return this.num_issues;
    }

    public void setItem(int index, int[] state) {
        if (state.length != 3)
            throw new IllegalArgumentException("Wrong array length for 3-state board.");
        if (index >= this.num_issues || index < 0)
            throw new IllegalArgumentException("Incorrect issue index.");
        this.offer[index][0] = state[0];
        this.offer[index][1] = state[1];
        this.offer[index][2] = state[2];
    }

    public void setOffer(Offer copiedOffer) {
        int[][] thisOffer = this.offer;
        int[][] toCopy = copiedOffer.offer;
        for (int i = 0; i < toCopy.length; i++) {
            for (int j = 0; j < (toCopy[i]).length; j++) {
                int currentValue = toCopy[i][j];
                thisOffer[i][j] = currentValue;
            }
        }
    }

    public boolean equals(Object ob) {
        Offer o = (Offer)ob;
        if (ob == null)
            return false;
        return Arrays.equals((Object[])o.offer, (Object[])this.offer);
    }

    public String toString() {
        String ans = "[ ";
        for (int i = 0; i < this.offer.length; i++)
            ans += Arrays.toString(this.offer[i]) + ", ";
        ans = ans.substring(0, ans.length() - 2);
        ans += " ]";
        return ans;
    }
}
