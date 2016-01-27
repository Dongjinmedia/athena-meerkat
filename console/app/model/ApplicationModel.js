/*
 * File: app/model/ApplicationModel.js
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

Ext.define('webapp.model.ApplicationModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.Field'
    ],

    fields: [
        {
            name: 'Id',
            type: 'int'
        },
        {
            name: 'contextPath',
            type: 'string'
        },
        {
            name: 'deployedDateString',
            type: 'string'
        },
        {
            name: 'version',
            type: 'string'
        },
        {
            name: 'displayName',
            type: 'string'
        },
        {
            name: 'warPath',
            type: 'string'
        },
        {
            name: 'lastModifiedDateString',
            type: 'string'
        },
        {
            name: 'lastStoppedDateString',
            type: 'string'
        },
        {
            name: 'lastReloadedDateString',
            type: 'string'
        },
        {
            name: 'lastStartedDateString',
            type: 'string'
        },
        {
            name: 'state',
            type: 'int'
        },
        {
            name: 'tomcatName'
        },
        {
            name: 'sessionTimeOut'
        },
        {
            name: 'sessions'
        }
    ]
});