package com.orbitz.stash.plugins.pvp.servlet;

import com.orbitz.stash.plugins.pvp.operations.*;

import com.atlassian.soy.renderer.SoyException;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.atlassian.stash.project.Project;
import com.atlassian.stash.project.ProjectService;
import com.atlassian.stash.user.PermissionAdminService;
import com.atlassian.stash.user.Permission;
import com.atlassian.stash.user.SecurityService;
import com.atlassian.stash.user.UserService;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ProjectPermissionsServlet extends HttpServlet{
    private static final Logger log = LoggerFactory.getLogger(ProjectPermissionsServlet.class);

    private final ProjectService projectService;
private final SecurityService securityService;
private final PermissionAdminService permissionAdminService;
private final UserService userService;
private final SoyTemplateRenderer soyTemplateRenderer;





    public ProjectPermissionsServlet(ProjectService projectService,
                                     SecurityService securityService,
                                     PermissionAdminService permissionAdminService,
                                     UserService userService,
                                     SoyTemplateRenderer soyTemplateRenderer) {
        this.projectService = projectService;
        this.securityService = securityService;
        this.permissionAdminService = permissionAdminService;
        this.userService = userService;
        this.soyTemplateRenderer = soyTemplateRenderer;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        // Get userSlug from path
        String pathInfo = req.getPathInfo();

        String projectKey = pathInfo.substring(1); // Strip leading slash
        Project project = projectService.getByKey(projectKey);

        //
        // Need to wrap all of the permission access in an operation called by the security service
        //
        PermissionAdminOperation permissionAdminOperation = new PermissionAdminOperation(permissionAdminService, userService, project);

        //
        // Build a map using Permission as the key that's value is a list of all the groups and users
        //
        Map<Permission, List<String>> identityMap = securityService.doWithPermission("Get groups and users to display", Permission.PROJECT_ADMIN, permissionAdminOperation);


        // Create the view model for Soy
        ImmutableMap.Builder<String, Object> immutableMapBuilder =  new ImmutableMap.Builder<String, Object>();

        immutableMapBuilder.
                put("project", project).
                put("projectAdmin", ImmutableList.copyOf(identityMap.get(Permission.PROJECT_ADMIN))).
                put("projectWrite", ImmutableList.copyOf(identityMap.get(Permission.PROJECT_WRITE))).
                put("projectRead", ImmutableList.copyOf(identityMap.get(Permission.PROJECT_READ)));

        // Now render the tab
        render(resp, "plugin.permissionviewer.projectPermissionsTab", immutableMapBuilder.build());
    }

    // Generic soy render method
    private void render(HttpServletResponse resp, String templateName, Map<String, Object> data) throws IOException, ServletException {
        resp.setContentType("text/html;charset=UTF-8");
        try {
            soyTemplateRenderer.render(resp.getWriter(),
                    "com.orbitz.stash.plugins.permission-viewer-plugin:soy-templates",
                    templateName,
                    data);
        } catch (SoyException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw new ServletException(e);
        }
    }
}