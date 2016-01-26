/*
 * File: app/view/DomainWindow.js
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

Ext.define('webapp.view.DomainWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.DomainWindow',

    requires: [
        'Ext.form.Panel',
        'Ext.form.RadioGroup',
        'Ext.form.field.Radio',
        'Ext.form.field.ComboBox',
        'Ext.form.field.Hidden',
        'Ext.button.Button'
    ],

    height: 193,
    id: 'domainWindow',
    width: 440,
    layout: 'fit',
    title: 'New Domain',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                    height: 168,
                    id: 'domainForm',
                    itemId: 'domainForm',
                    bodyPadding: 10,
                    frameHeader: false,
                    header: false,
                    title: 'My Form',
                    items: [
                        {
                            xtype: 'textfield',
                            id: 'domainNameTextField',
                            itemId: 'domainNameTextField',
                            width: 388,
                            fieldLabel: 'Name',
                            name: 'UserIDTextField',
                            allowBlank: false,
                            allowOnlyWhitespace: false,
                            emptyText: '',
                            validateBlank: true
                        },
                        {
                            xtype: 'radiogroup',
                            id: 'domainTypeRadioButtonField',
                            fieldLabel: 'Type',
                            items: [
                                {
                                    xtype: 'radiofield',
                                    id: 'domainTypeClustering',
                                    name: 'DomainType',
                                    value: true,
                                    boxLabel: 'Clustering',
                                    checked: true,
                                    listeners: {
                                        change: {
                                            fn: me.onDomainTypeClusteringChange,
                                            scope: me
                                        }
                                    }
                                },
                                {
                                    xtype: 'radiofield',
                                    id: 'domainTypeNoneClustering',
                                    name: 'DomainType',
                                    value: false,
                                    boxLabel: 'Non-clustering',
                                    listeners: {
                                        change: {
                                            fn: me.onDomainTypeNoneClusteringChange,
                                            scope: me
                                        }
                                    }
                                }
                            ]
                        },
                        {
                            xtype: 'combobox',
                            id: 'dataGridServerGroupComboBoxField',
                            width: 390,
                            fieldLabel: 'Datagrid server group',
                            name: 'DatagridServerGroupComboBox',
                            allowBlank: false,
                            allowOnlyWhitespace: false,
                            displayField: 'name',
                            store: 'DatagridServerGroupStore',
                            valueField: 'id'
                        },
                        {
                            xtype: 'hiddenfield',
                            id: 'domainIdHiddenField',
                            fieldLabel: 'Label',
                            name: 'IDHiddenField'
                        },
                        {
                            xtype: 'container',
                            height: 38,
                            layout: {
                                type: 'hbox',
                                align: 'middle',
                                defaultMargins: {
                                    top: 0,
                                    right: 10,
                                    bottom: 10,
                                    left: 0
                                },
                                pack: 'center'
                            },
                            items: [
                                {
                                    xtype: 'button',
                                    margins: '10 10 10 10',
                                    id: 'btnSubmitNewDomain',
                                    itemId: 'btnSubmitNewDomain',
                                    text: 'Create'
                                },
                                {
                                    xtype: 'button',
                                    handler: function(button, e) {
                                        Ext.MessageBox.confirm('Confirm', '작업을 취소하시겠습니까?', function(btn){

                                            if(btn == "yes"){
                                                button.up("window").close();
                                            }
                                        });
                                    },
                                    text: 'Cancel'
                                }
                            ]
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    },

    onDomainTypeClusteringChange: function(field, newValue, oldValue, eOpts) {
        var comboBox = Ext.getCmp("dataGridServerGroupComboBoxField");
        if (comboBox.isVisible()){
            comboBox.hide();
        }

    },

    onDomainTypeNoneClusteringChange: function(field, newValue, oldValue, eOpts) {
        var comboBox = Ext.getCmp("dataGridServerGroupComboBoxField");
        if (comboBox.isHidden){
            comboBox.show();
        }
    }

});