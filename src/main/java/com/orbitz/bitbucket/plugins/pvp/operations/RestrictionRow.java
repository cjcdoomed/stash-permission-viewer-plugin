package com.orbitz.bitbucket.plugins.pvp.operations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ccooper on 8/28/15.
 */
public class RestrictionRow {

    private String description;
    private List<String> accessGrantList;


    public RestrictionRow() {
        accessGrantList = new ArrayList<String>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAccessGrant() {
        return accessGrantList;
    }
}
