/*
 * File: app.js
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

// @require @packageOverrides
Ext.Loader.setConfig({
    enabled: true
});


Ext.application({

    requires: [
        'Ext.window.MessageBox'
    ],
    models: [
        'SessionKeyData',
        'UserModel',
        'TomcatInstanceModel',
        'UserRoleModel',
        'DomainModel',
        'DatagridServerGroupModel',
        'MachineModel',
        'ClusteringConfigurationModel',
        'DatasourceModel',
        'DatagridModel',
        'EnvironmentVariableModel',
        'RevisionModel',
        'TomcatBusyThreadModel'
    ],
    stores: [
        'sessionDataStore',
        'MenuTreeStore',
        'TomcatInstanceListStore',
        'TempoStore',
        'UserStore',
        'UserRoleStore',
        'DomainStore',
        'DatagridServerGroupStore',
        'MachineStore',
        'ClusteringConfigurationStore',
        'ApplicationStore',
        'DatasourceStore',
        'LinkingTomcatDatasourceStore',
        'DatagridServerStore',
        'EnvironmentVariableStore',
        'RevisionStore',
        'TomcatBusyThreadStore'
    ],
    views: [
        'meerkatViewport',
        'loginWindow',
        'dashboardPanel',
        'DomainContainer',
        'TomcatInstanceWindow',
        'DeployWindow',
        'TomcatInstanceContainer',
        'LinkNewDataSourceWindow',
        'UserWindow',
        'DatagridServerGroupWindow',
        'DataSourceServerWindow',
        'DetailMonitoringTomcatInstance',
        'MonitoringMachineContainer',
        'UserMntContainer',
        'DetailMonitoringMachineContainer',
        'SeverDataGridWindow',
        'DataSourceWindow',
        'ResourceManagementContainer',
        'MonitoringTomcatInstance',
        'ClusteringConfigurationWindow',
        'ClusteringConfigurationComparingWindow',
        'DomainWindow',
        'UserRoleWindow',
        'EnvironmentVariableWindow',
        'TomcatInstanceCreateWizard',
        'DomainForm',
        'TomcatForm'
    ],
    controllers: [
        'headerController',
        'footerController',
        'dashboardController',
        'globalController',
        'LoginController',
        'MenuController',
        'DomainController',
        'TomcatController',
        'UserController',
        'ApplicationController',
        'CluteringConfigurationController',
        'DatasourceController',
        'ServerManagementController',
        'DatagridServerController'
    ],
    name: 'webapp',

    launch: function() {
        Ext.create('webapp.view.meerkatViewport');
    }

});
