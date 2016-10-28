/*
 * Copyright 2016 JBoss, by Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dashbuilder.client.navigation.layout.editor;

import java.util.Collections;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import org.dashbuilder.client.navigation.NavigationManager;
import org.dashbuilder.client.navigation.resources.i18n.NavigationConstants;
import org.dashbuilder.client.navigation.widget.NavItemSelectionModal;
import org.dashbuilder.client.navigation.widget.NavItemSelectionModalView;
import org.dashbuilder.client.navigation.widget.NavTilesWidget;
import org.dashbuilder.navigation.NavGroup;
import org.dashbuilder.navigation.NavItem;
import org.dashbuilder.navigation.NavTree;
import org.gwtbootstrap3.client.ui.Modal;
import org.uberfire.ext.layout.editor.client.api.HasModalConfiguration;
import org.uberfire.ext.layout.editor.client.api.ModalConfigurationContext;
import org.uberfire.ext.layout.editor.client.api.RenderingContext;
import org.uberfire.ext.plugin.client.perspective.editor.api.PerspectiveEditorDragComponent;

/**
 * A layout editor's navigation component that shows a navigation group structure using two tile types: folders
 * (for nav group items) and links to perspective items.
 * @see NavTilesWidget
 */
@Dependent
public class NavTilesDragComponent implements PerspectiveEditorDragComponent, HasModalConfiguration {

    NavigationManager navigationManager;
    NavItemSelectionModal navItemSelectionModal;
    NavTilesWidget navTilesWidget;
    String navGroupId = null;

    public static final String NAV_GROUP_ID = "navGroupId";

    @Inject
    public NavTilesDragComponent(NavigationManager navigationManager,
                                 NavItemSelectionModal navItemSelectionModal,
                                 NavTilesWidget navTilesWidget) {
        this.navigationManager = navigationManager;
        this.navItemSelectionModal = navItemSelectionModal;
        this.navTilesWidget = navTilesWidget;
        this.navTilesWidget.setHideEmptyGroups(true);
        this.navTilesWidget.setOnStaleCommand(this::showNavTiles);
    }

    @Override
    public String getDragComponentTitle() {
        return NavigationConstants.INSTANCE.navTilesDragComponent();
    }

    @Override
    public IsWidget getPreviewWidget(RenderingContext ctx) {
        return getShowWidget(ctx);
    }

    @Override
    public IsWidget getShowWidget(RenderingContext ctx) {
        Map<String, String> properties = ctx.getComponent().getProperties();
        navGroupId = properties.get(NAV_GROUP_ID);
        this.showNavTiles();
        return navTilesWidget;
    }

    private void showNavTiles() {
        NavTree navTree = navigationManager.getNavTree();
        NavGroup navGroup = (NavGroup) navTree.getItemById(navGroupId);
        if (navGroup != null) {
            navGroup = (NavGroup) navGroup.cloneItem();
            navGroup.setParent(null);
            navTilesWidget.show(navGroup);
        } else {
            navTilesWidget.show(Collections.emptyList());
        }
    }

    @Override
    public Modal getConfigurationModal(ModalConfigurationContext ctx) {
        navItemSelectionModal.setHelpHint(NavigationConstants.INSTANCE.navTilesDragComponentHelp());
        navItemSelectionModal.setOnlyGroups(true);
        navItemSelectionModal.setOnOk(() -> navGroupSelectionOk(ctx));
        navItemSelectionModal.setOnCancel(() -> navGroupSelectionCancel(ctx));

        NavTree navTree = navigationManager.getNavTree();
        String groupId = ctx.getComponentProperty(NAV_GROUP_ID);
        navItemSelectionModal.show(navTree.getRootItems(), groupId);
        return ((NavItemSelectionModalView) navItemSelectionModal.getView()).getModal();
    }

    private void navGroupSelectionOk(ModalConfigurationContext ctx) {
        NavItem navItem = navItemSelectionModal.getSelectedItem();
        ctx.setComponentProperty(NAV_GROUP_ID, navItem.getId());
        ctx.configurationFinished();
    }

    private void navGroupSelectionCancel(ModalConfigurationContext ctx) {
        ctx.configurationCancelled();
    }
}
