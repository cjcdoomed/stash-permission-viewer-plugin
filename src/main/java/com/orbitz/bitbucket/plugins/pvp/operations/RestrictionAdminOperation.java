package com.orbitz.bitbucket.plugins.pvp.operations;

import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.repository.ref.restriction.*;
import com.atlassian.bitbucket.user.*;
import com.atlassian.bitbucket.util.*;

import java.util.*;


/**
 * Secure operation that retrieves all of the groups and users that have permissions applied to the repository
 */
public class RestrictionAdminOperation implements UncheckedOperation<Map<String, BranchRow>> {

    private RefRestrictionService refRestrictionService;
    private UserService userService;
    private Repository repository;

    public RestrictionAdminOperation(RefRestrictionService refRestrictionService, UserService userService, Repository repository) {
        this.refRestrictionService = refRestrictionService;
        this.userService = userService;
        this.repository = repository;
    }

    /**
     * Perform the secure operations to retrieve all groups and users that have refRestrictions applied to the repository
     * @return
     */
    public Map<String, BranchRow> perform() {

        Map<String, BranchRow> branchRowMap = new HashMap<String, BranchRow>();


        PageRequest pageRequest = new PageRequestImpl(0, 1000);

        RestrictionSearchRequest.Builder rsrBuilder = new RestrictionSearchRequest.Builder(repository);
        RestrictionSearchRequest refSearchRequest = rsrBuilder.build();

        Page<RefRestriction> refRestrictionPage = refRestrictionService.search(refSearchRequest, pageRequest);

        // Build the list of ref restrictions
        for (RefRestriction rr : refRestrictionPage.getValues()) {
            String key = rr.getMatcher().getId();

            BranchRow row = branchRowMap.get(key);
            if (row == null) {
                row = new BranchRow();
                row.setBranch(key);
            }
            RestrictionRow restrictionRow = new RestrictionRow();
            restrictionRow.setDescription(rr.getType().toString());

            // Handle access grants
            if (!rr.getAccessGrants().isEmpty()) {

                for (AccessGrant accessGrant : rr.getAccessGrants()) {
                    if (accessGrant instanceof GroupAccessGrant) {
                        restrictionRow.getAccessGrant().add(buildGroupString((GroupAccessGrant) accessGrant, pageRequest));
                    } else if (accessGrant instanceof UserAccessGrant) {
                        restrictionRow.getAccessGrant().add(((UserAccessGrant) accessGrant).getUser().getDisplayName());
                    }
                }

            } else {
                restrictionRow.getAccessGrant().add("Everyone");
            }
            row.getRestrictionRowList().add(restrictionRow);

        }

        return branchRowMap;
    }



    /**
     * Create a display string consisting of the group name and members
     * @param groupAccessGrant
     * @return display string
     */
    private String buildGroupString(GroupAccessGrant groupAccessGrant, PageRequest pageRequest) {

        StringBuilder builder = new StringBuilder();

        builder.append(groupAccessGrant.getGroup());
        Page<? extends ApplicationUser> appUserPage =  userService.findUsersByGroup(groupAccessGrant.getGroup(), pageRequest);


        ArrayList<String> userList = new ArrayList<String>();

        for (ApplicationUser appUser : appUserPage.getValues()) {
            userList.add(appUser.getDisplayName());
        }
        builder.append(Arrays.toString(userList.toArray()));


        return builder.toString();

    }


}