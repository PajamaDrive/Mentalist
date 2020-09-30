package Mentalist.utils;

public class Preference {
    public enum Relation {
        GREATER_THAN, LESS_THAN, BEST, WORST, EQUAL;
    }

    private Relation relation = Relation.GREATER_THAN;

    private int issue1 = -1;

    private int issue2 = -1;

    private boolean isQuery = false;

    public Preference() {}

    public Preference(Preference pref) {
        this.issue1 = pref.getIssue1();
        this.issue2 = pref.getIssue2();
        this.relation = pref.getRelation();
        this.isQuery = pref.isQuery();
    }

    public Preference(int issue1, int issue2, Relation relation, boolean isQuery) {
        this.issue1 = issue1;
        this.issue2 = issue2;
        this.relation = relation;
        this.isQuery = isQuery;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public void setIssue1(int issue1) {
        this.issue1 = issue1;
    }

    public void setIssue2(int issue2) {
        this.issue2 = issue2;
    }

    public void setQuery(boolean isQuery) {
        this.isQuery = isQuery;
    }

    public boolean equals(Object o) {
        Preference p = (Preference)o;
        return (p.issue1 == this.issue1 && p.issue2 == this.issue2 && p.relation == this.relation && p.isQuery == this.isQuery);
    }

    public Relation getRelation() {
        return this.relation;
    }

    public int getIssue1() {
        return this.issue1;
    }

    public int getIssue2() {
        return this.issue2;
    }

    public boolean isQuery() {
        return this.isQuery;
    }

    public String toString() {
        return "1: " + this.issue1 + " 2: " + this.issue2 + " " + this.relation + " " + this.isQuery;
    }
}
