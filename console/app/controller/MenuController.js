/*
 * File: app/controller/MenuController.js
 *
 * This file was generated by Sencha Architect version 3.2.0.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Ext JS 4.2.x library, under independent license.
 * License of Sencha Architect does not include license for Ext JS 4.2.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

Ext.define('webapp.controller.MenuController', {
    extend: 'Ext.app.Controller',

    onTreepanelItemClick: function(dataview, record, item, index, e, eOpts) {
        var menuId = record.get("menuId");
        var menuText = record.get("text");
        var is_leaf = record.get("leaf");
        var navField = Ext.getCmp("navigationField");

        //if (is_leaf){
        //    navigationText = navField.getValue() + " > " + menuText;
        //
        //else {
        //vigationText = menuText;
        //}

        navField.setText(menuText);

        if(menuId !== undefined){
            if (!is_leaf){
                this.loadChildMenus(menuId, dataview.up('treepanel'));
            }

            this.showMenu(menuId, menuText);
            // turn off live busy threads chart on monitoring part
            if(GlobalData.busyThreadsChartInterval > -1){
                clearInterval(GlobalData.busyThreadsChartInterval);
                GlobalData.busyThreadsChartInterval = -1;
            }
        }

    },

    onTreepanelItemContextMenu: function(dataview, record, item, index, e, eOpts) {
        var menuId = record.get("menuId");
        var mnuContext = null;

        //Tomcat management level
        if (menuId==="tomcatMng"){
            mnuContext = Ext.create("Ext.menu.Menu",{

            items: [{
                id: 'create-wizard',
                text: 'Create Wizard'
            },
            {
                id: 'new-domain',
                text: 'New Domain'
            },
         //   {
         //       id: 'collapse',
          ///      text: 'Collapse'
           // },
            {
                id: 'refresh',
                text: 'Refresh'
            }

                   ],
            listeners: {

                click: function( _menu, _item, _e, _eOpts ) {
                   switch (_item.id) {
                        case 'create-wizard':
                            Ext.create('widget.ticWizard').show();
                            break;
                        case 'new-domain':
                            webapp.app.getController("DomainController").showDomainWindow("new", 0);
                            break;
                        case 'collapse':
                            //item.collapse();
                            break;
                        case 'refresh':
                            webapp.app.getController("MenuController").loadChildMenus(menuId);
                            break;
                        default:
                            webapp.app.getController("MenuController").loadChildMenus(menuId);
                            break;
                   }
                },
                hide:function(menu){
                    menu.destroy();
                }
            },
            defaults: {
               clickHideDelay: 1
            }
        });
        } else if (menuId.indexOf("tomcatMng_domain_") >= 0 && menuId.indexOf("_tomcat_") < 0) { //domain level
            mnuContext =  Ext.create("Ext.menu.Menu",{

            items: [{
                id: 'new-tomcat',
                text: 'New Tomcat'
            },
            {
                id: 'edit-domain',
                text: 'Edit'
            },
            {
                id: 'delete-domain',
                text: 'Delete'
            },
            {
                id:'expand',
                text:'Expand'
            },
            {
                id:'refresh',
                text:'Refresh'
            }

                   ],
            listeners: {
               click: function( _menu, _item, _e, _eOpts ) {
                   var domainId = menuId.substr(menuId.indexOf("_domain_") + "_domain_".length);
                   switch (_item.id) {
                        case 'new-tomcat':
                            webapp.app.getController("TomcatController").showTomcatWindow("new", 0, domainId);
                            break;
                        case 'edit-domain':
                            webapp.app.getController("DomainController").showDomainWindow("edit", domainId);
                            break;
                        case 'delete-domain':
                            webapp.app.getController("DomainController").deleteDomain(domainId);
                            break;
                        case 'expand':
                            alert("Expand");
                            break;
                        case 'refresh':
                            alert("Refresh");
                            break;
                    }
                },
                hide:function(menu){
                    menu.destroy();
                }
           },
           defaults: {
             clickHideDelay: 1
           }
        });

        }
        else if (menuId.indexOf("tomcatMng_domain_") >= 0 && menuId.indexOf("_tomcat_") >= 0) { //tomcat level
           var tomcatId =  menuId.substr(menuId.indexOf("_tomcat_") + "_tomcat_".length);
            mnuContext =  Ext.create("Ext.menu.Menu",{
                items: [{
                    id: 'start-tomcat',
                    text: 'Start',
                    disabled:record.get("state") === 1?true:false
                },
                        {
                            id: 'stop-tomcat',
                            text: 'Stop',
                            disabled:record.get("state") === 2?true:false

                        },
                        {
                            id: 'edit-tomcat',
                            text: 'Edit'
                        },
                        {
                            id: 'delete-tomcat',
                            text: 'Delete'
                        },
                        {
                            id:'refresh',
                            text:'Refresh'
                        }

                       ],
                listeners: {
                    click: function( _menu, _item, _e, _eOpts ) {
                        switch (_item.id) {
                            case 'start-tomcat':
                                webapp.app.getController("TomcatController").changeState(tomcatId, 1);
                                break;
                            case 'stop-tomcat':
                                webapp.app.getController("TomcatController").changeState(tomcatId, 2);
                                break;
                            case 'edit-tomcat':
                                webapp.app.getController("TomcatController").showTomcatWindow("edit", tomcatId, 0);
                                break;
                            case 'delete-tomcat':
                                alert("Delete tomcat");
                                break;
                            case 'refresh':
                                alert("Refresh");
                                break;
                        }
                    },
                    hide:function(menu){
                        menu.destroy();
                    }
           },
           defaults: {
             clickHideDelay: 1
           }
        });
        }

        if (mnuContext !== null){
            mnuContext.showAt(e.getXY());

        }
        e.stopEvent();

    },

    showMenu: function(menuId, menuText) {
        var activeItem = -1;
        var objectId = -1;
        if(menuId === "dashboard"){
            activeItem = 0;
        }else if (menuId.indexOf("tomcatMng_domain_") >=0 && menuId.indexOf("_tomcat_") < 0) {
            objectId = menuId.substr(menuId.indexOf("tomcatMng_domain_")+ "tomcatMng_domain_".length );
            GlobalData.lastSelectedMenuId = objectId;
            webapp.app.getController("DomainController").loadDomainInfo(objectId);
            activeItem = 1;
        }else if (menuId.indexOf("tomcatMng_domain_") >=0 && menuId.indexOf("_tomcat_") >= 0) {
            objectId = menuId.substr(menuId.indexOf("_tomcat_") + "_tomcat_".length);
            GlobalData.lastSelectedMenuId = objectId;
            webapp.app.getController("TomcatController").displayTomcatInstance(objectId);
            activeItem = 2;
            //navigationText = "Tomcat Management > Domain ... > ...";
        }else if (menuId === "monitoring_servers" && menuId.indexOf("_server_") < 0) {
            activeItem = 3;
            //    navigationText = "Monitoring > Servers";
        }else if (menuId.indexOf("monitoring_tomcats") >= 0 && menuId.indexOf("_tomcat_") < 0) {
            activeItem = 4;
            //  navigationText = "Monitoring > Tomcat Instances";
        }else if (menuId.indexOf("monitoring_servers") >= 0 && menuId.indexOf("_server_") >= 0) {
            GlobalData.lastSelectedMenuId = menuId.substr(menuId.indexOf("_server_") + "_server_".length);
            activeItem = 5;
            ///navigationText = "Monitoring > Servers > ...";
        }else if (menuId.indexOf("monitoring_tomcats") >= 0 && menuId.indexOf("_tomcat_") >= 0) {
            GlobalData.lastSelectedMenuId = menuId.substr(menuId.indexOf("_tomcat_")+"_tomcat_".length);
            activeItem = 6;
            //navigationText = "Monitoring > Tomcat Instances > ...";
        }else if (menuId === "resourcemng_servers") {
            activeItem = 7;
             webapp.app.getStore("ServerStore").reload();
            //navigationText = "Resource Management > Servers";

        }else if (menuId === "resourcemng_servers_groups") {
            activeItem = 8;
            //navigationText = "Resource Management > Servers";
        }else if (menuId === "resourcemng_datasources") {
            activeItem = 9;
            //navigationText = "Resource Management > Datasources";
        }else if (menuId === "usermnt"){
            activeItem = 10;
            //navigationText = "User Management";
            is_child = false;
        }else if (menuId ==="logmnt"){
            alert("Under construction.\n Reused from other project");
            //navigationText = "Log Management";
            is_child = false;
        }

        if(activeItem > -1){
            Ext.getCmp("subCenterContainer").layout.setActiveItem(activeItem);
        }

    },

    loadChildMenus: function(parentId, treePanel) {
        var url = GlobalData.urlPrefix;
        var is_child_leaf = false;
        var prefix_child_menu_id = "";
        var params = {};
        if (parentId.indexOf("tomcatMng") >=0 && parentId.length == "tomcatMng".length) {
            url+="domain/list";
            is_child_leaf = false;
            prefix_child_menu_id = parentId + "_domain_";
        }
        else if (parentId.indexOf("tomcatMng_domain") >= 0) {
            var domainId = parentId.substr(parentId.lastIndexOf("_") + 1);
            params = {"domainId":domainId};
            url+="domain/tomcatlist";
            is_child_leaf = true;
            prefix_child_menu_id = parentId + "_tomcat_";

        }
        else if (parentId.indexOf("monitoring_servers") >= 0) {
            url+="machine/list";
            is_child_leaf = true;
            prefix_child_menu_id = parentId + "_server_";
        }
        else if (parentId.indexOf("monitoring_tomcats") >= 0 && parentId.length =="monitoring_tomcats".length) {
            url+="domain/list";
            is_child_leaf = false;
            prefix_child_menu_id = parentId + "_domain_";
        }
        else if(parentId.indexOf("monitoring_tomcats") >= 0 && parentId.indexOf("_domain_") > 0){
            var domainId = parentId.substr(parentId.lastIndexOf("_") + 1);
            params = {"domainId":domainId};
            url+="domain/tomcatlist";
            is_child_leaf = true;
            prefix_child_menu_id = parentId + "_tomcat_";
        }
        else {
            return;
        }

        //var treePanel = Ext.getCmp("menuTreePanel");
        var parentNode = treePanel.getRootNode().findChild("menuId", parentId, true);
        if (parentNode === undefined || parentNode === null) {
            return;
        }
        //remove all childs
        parentNode.removeAll();
        //get data from ajax
        Ext.Ajax.request({
            url: url,
            params: params,
            method: "GET",
            success: function(resp, ops) {
                var response = Ext.decode(resp.responseText);
                for(i = 0; i < response.data.length; i ++){
                    if (prefix_child_menu_id.indexOf("_tomcat_") >= 0){
                        parentNode.appendChild({"text":response.data[i].name, "menuId": prefix_child_menu_id + response.data[i].id, "leaf": is_child_leaf,"expanded":!is_child_leaf,"state":response.data[i].state});;
                    } else {
                        parentNode.appendChild({"text":response.data[i].name, "menuId": prefix_child_menu_id + response.data[i].id, "leaf": is_child_leaf,"expanded":!is_child_leaf,"state":0});
                    }
                }
            }
        });
    },

    loadDomainList: function() {
        this.loadChildMenus("tomcatMng");
    },

    loadTomcatList: function(domainId) {
        this.loadChildMenus("tomcatMng_domain_"+domainId);
    },

    loadMonitoringServerList: function() {
        alert("Server list is loading");
    },

    loadMonitoringTomcatList: function() {
        this.loadChildMenus("tomcatMng");
    },

    init: function(application) {
        this.control({
            "#menuTreePanel2": {
                itemclick: this.onTreepanelItemClick
            },
            "treepanel": {
                itemcontextmenu: this.onTreepanelItemContextMenu
            }
        });
    }

});
