package com.orbitz.bitbucket.plugins.pvp.operations;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by ccooper on 8/28/15.
 */
public class BranchRow {

    private String branch;

    private List<RestrictionRow> restrictionRowList;

    public BranchRow() {
        restrictionRowList = new ArrayList<RestrictionRow>();
    }

    public String getKey() { return getBranch(); }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public List<RestrictionRow> getRestrictionRowList() {
        return restrictionRowList;
    }
}
