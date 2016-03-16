/*
 * File: app/view/TomcatInstallLogWindow.js
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

Ext.define('webapp.view.TomcatInstallLogWindow', {
    extend: 'Ext.window.Window',

    requires: [
        'Ext.form.Label',
        'Ext.form.field.TextArea',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button'
    ],

    height: 592,
    width: 894,
    layout: 'anchor',
    bodyPadding: 15,
    title: 'Tomcat Instance Install Logs',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'label',
                    cls: 'osc-h3',
                    text: 'Install Logs'
                },
                {
                    xtype: 'textareafield',
                    anchor: '100% 100%',
                    fieldLabel: 'Label',
                    hideLabel: true
                }
            ],
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'bottom',
                    ui: 'footer',
                    layout: {
                        type: 'hbox',
                        pack: 'center'
                    },
                    items: [
                        {
                            xtype: 'button',
                            handler: function(button, e) {
                                button.up("window").close();
                            },
                            text: 'Close'
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    }

});