package com.orbitz.bitbucket.plugins.pvp.operations;

import com.atlassian.bitbucket.permission.Permission;
import com.atlassian.bitbucket.permission.PermissionAdminService;
import com.atlassian.bitbucket.permission.PermittedGroup;
import com.atlassian.bitbucket.permission.PermittedUser;
import com.atlassian.bitbucket.project.Project;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.user.*;
import com.atlassian.bitbucket.util.Page;
import com.atlassian.bitbucket.util.PageRequest;
import com.atlassian.bitbucket.util.PageRequestImpl;
import com.atlassian.bitbucket.util.UncheckedOperation;

import java.util.*;


/**
 * Secure operation that retrieves all of the groups and users that have permissions applied to the repository
 */
public class PermissionAdminOperation implements UncheckedOperation<Map<Permission, List<String>>> {

    private PermissionAdminService permissionAdminService;
    private UserService userService;
    private Project project;
    private Repository repository;

    public PermissionAdminOperation(PermissionAdminService permissionAdminService, UserService userService, Project project) {
        this.permissionAdminService = permissionAdminService;
        this.userService = userService;
        this.project = project;
        this.repository = null;
    }

    public PermissionAdminOperation(PermissionAdminService permissionAdminService, UserService userService, Project project, Repository repository) {
        this.permissionAdminService = permissionAdminService;
        this.userService = userService;
        this.project = project;
        this.repository = repository;
    }

    /**
     * Perform the secure operations to retrieve all groups and users that have permissions applied to the repository
     * @return
     */
    public Map<Permission, List<String>> perform() {

        Map<Permission, List<String>> identityMap = new HashMap<Permission, List<String>>();


        PageRequest pageRequest = new PageRequestImpl(0, 1000);

        // Project level permissions
        Page<PermittedUser> permittedUserProjectPage =
                permissionAdminService.findUsersWithProjectPermission(project, null, pageRequest);
        Page<PermittedGroup> permittedGroupProjectPage =
                permissionAdminService.findGroupsWithProjectPermission(project, null, pageRequest);

        identityMap.put(Permission.PROJECT_ADMIN, new java.util.ArrayList<String>());
        identityMap.put(Permission.PROJECT_WRITE, new java.util.ArrayList<String>());
        identityMap.put(Permission.PROJECT_READ, new java.util.ArrayList<String>());
        identityMap.put(Permission.REPO_ADMIN, new java.util.ArrayList<String>());
        identityMap.put(Permission.REPO_WRITE, new java.util.ArrayList<String>());
        identityMap.put(Permission.REPO_READ, new java.util.ArrayList<String>());

        for (PermittedGroup pg : permittedGroupProjectPage.getValues()) {
            identityMap.get(pg.getPermission()).add(buildGroupString(pg, pageRequest));
        }
        for (PermittedUser pu : permittedUserProjectPage.getValues()) {
            identityMap.get(pu.getPermission()).add(pu.getUser().getDisplayName());
        }


        // Repository level permissions
        if (repository != null) {

            Page<PermittedUser> permittedUserRepositoryPage =
                    permissionAdminService.findUsersWithRepositoryPermission(repository, null, pageRequest);
            Page<PermittedGroup> permittedGroupRepositoryPage =
                    permissionAdminService.findGroupsWithRepositoryPermission(repository, null, pageRequest);

            for (PermittedGroup pg : permittedGroupRepositoryPage.getValues()) {
                identityMap.get(pg.getPermission()).add(buildGroupString(pg, pageRequest));
            }

            for (PermittedUser pu : permittedUserRepositoryPage.getValues()) {
                identityMap.get(pu.getPermission()).add(pu.getUser().getDisplayName());
            }
        }

        return identityMap;
    }

    /**
     * Create a display string consisting of the group name and members
     * @param permittedGroup
     * @return display string
     */
    private String buildGroupString(PermittedGroup permittedGroup, PageRequest pageRequest) {

        StringBuilder builder = new StringBuilder();

        builder.append(permittedGroup.getGroup());
        Page<? extends ApplicationUser> appUserPage =  userService.findUsersByGroup(permittedGroup.getGroup(), pageRequest);


        ArrayList<String> userList = new ArrayList<String>();

        for (ApplicationUser appUser : appUserPage.getValues()) {
            userList.add(appUser.getDisplayName());
        }
        builder.append(Arrays.toString(userList.toArray()));


        return builder.toString();

    }


}