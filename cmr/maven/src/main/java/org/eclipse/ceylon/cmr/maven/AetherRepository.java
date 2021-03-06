/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.maven;

import java.util.List;

import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.ModuleVersionQuery;
import org.eclipse.ceylon.cmr.api.ModuleVersionResult;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.api.ModuleQuery.Type;
import org.eclipse.ceylon.cmr.impl.MavenRepository;
import org.eclipse.ceylon.cmr.impl.NodeUtils;
import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.common.ModuleUtil;
import org.eclipse.ceylon.common.log.Logger;
import org.eclipse.ceylon.model.cmr.ArtifactResult;

/**
 * Aether repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class AetherRepository extends MavenRepository {
    private final AetherUtils utils;

    private AetherRepository(AetherContentStore acs) {
        super(acs.createRoot());
        utils = acs.getUtils();
    }

    public static CmrRepository createRepository(Logger log, boolean offline, int timeout) {
        return createRepository(log, null, offline, timeout, null);
    }

    public static CmrRepository createRepository(Logger log, String settingsXml, boolean offline, int timeout, String currentDirectory) {
        AetherContentStore acs = new AetherContentStore(log, settingsXml, null, offline, timeout, currentDirectory);
        return new AetherRepository(acs);
    }

    public static CmrRepository createRepository(Logger log, String settingsXml, String rootFolderOverride, boolean offline, int timeout, String currentDirectory) {
        AetherContentStore acs = new AetherContentStore(log, settingsXml, rootFolderOverride, offline, timeout, currentDirectory);
        return new AetherRepository(acs);
    }

    @Override
    public String[] getArtifactNames(ArtifactContext context) {
        String name = context.getName();
        final int p = name.contains(":") ? name.lastIndexOf(":") : name.lastIndexOf(".");

        return getArtifactNames(p >= 0 ? name.substring(p + 1) : name, context.getVersion(), context.getSuffixes());
    }

    @Override
    protected String toModuleName(Node node) {
        ArtifactContext context = ArtifactContext.fromNode(node);
        if (context != null) {
            return context.getName();
        }
        String moduleName = node.getLabel();
        Node parent = NodeUtils.firstParent(node);
        String groupId = NodeUtils.getFullPath(parent, ".");
        // That's sort of an invariant, but let's be safe
        if (groupId.startsWith("."))
            groupId = groupId.substring(1);
        moduleName = groupId != null ? groupId + ":" + moduleName : moduleName;
        return moduleName;
    }

    @Override
    protected List<String> getDefaultParentPathInternal(ArtifactContext context) {
        return MavenRepository.getParentPath(context);
    }

    @Override
    public Node findParent(ArtifactContext context) {
        if (context.getName().startsWith("ceylon.")) {
            return null;
        }
        return super.findParent(context);
    }

    public ArtifactResult getArtifactResultInternal(RepositoryManager manager, Node node) {
        return utils.findDependencies(manager, node);
    }
    
    @Override
    public void completeVersions(ModuleVersionQuery lookup, ModuleVersionResult result) {
        if(lookup.getType() != Type.ALL && lookup.getType() != null){
            boolean ok = false;
            for(String suffix : lookup.getType().getSuffixes()){
                if(suffix.equals(ArtifactContext.JAR)){
                    ok = true;
                    break;
                }
            }
            if(!ok)
                return;
        }
        // this means only for explicitly Maven modules that have a ":"
        if(!ModuleUtil.isMavenModule(lookup.getName()))
            return;
        String[] groupArtifactIds = utils.nameToGroupArtifactIds(lookup.getName());
        if(groupArtifactIds == null)
            return;
        // FIXME: does not respect paging or count
        utils.search(groupArtifactIds[0], groupArtifactIds[1], lookup.getVersion(), lookup.isExactVersionMatch(), 
                result, getOverrides(), getDisplayString());
    }
}
