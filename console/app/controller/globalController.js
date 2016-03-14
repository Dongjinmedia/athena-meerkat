/*
 * File: app/controller/globalController.js
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

Ext.define('webapp.controller.globalController', {
    extend: 'Ext.app.Controller',

    init: function(application) {
        /**
         * Global Variables를 정의
         */


        // Memory Chart, CPU Chart 용 Store 정의
        var memoryStore, cpuStore;

        // Memory Chart, CPU Chart 용 Series 겸 Field 정의
        var fields = ['date', 'field1', 'field2', 'field3', 'field4', 'field5', 'field6', 'field7', 'field8'];

        // 최대 Infinispan 서버 갯수
        var serverSize = 8;


        var urlPrefix = '';
        /*
        // 현재 구동중인 Infinispan 서버 갯수를 조회한다.
        Ext.Ajax.request({
            async: false,
            url: urlPrefix + 'getServerList',
            params: {
            },
            success: function(response, opts){
                var obj = Ext.decode(response.responseText);

                serverSize = obj.length;

                if (obj.length == 1) {
                    fields = ['date', 'field1'];
                } else if (obj.length == 2) {
                    fields = ['date', 'field1', 'field2'];
                } else if (obj.length == 3) {
                    fields = ['date', 'field1', 'field2', 'field3'];
                } else if (obj.length == 4) {
                    fields = ['date', 'field1', 'field2', 'field3', 'field4'];
                } else if (obj.length == 5) {
                    fields = ['date', 'field1', 'field2', 'field3', 'field4', 'field5'];
                } else if (obj.length == 6) {
                    fields = ['date', 'field1', 'field2', 'field3', 'field4', 'field5', 'field6'];
                } else if (obj.length == 7) {
                    fields = ['date', 'field1', 'field2', 'field3', 'field4', 'field5', 'field6', 'field7'];
                }
            },
            failure: function(response, opts) {
                Ext.Msg.alert('Error', 'Server-side failure with status code ' + response.status);
            }
        });
        */
        memoryStore = Ext.create('Ext.data.JsonStore', {
            fields: fields
        });

        cpuStore = Ext.create('Ext.data.JsonStore', {
            fields: fields
        });

        // Global variables를 정의하는 구문으로 GlobalData.urlPrefix, GlobalData.serverSize 등으로 어디에서든 접근이 가능하다.
        Ext.define('GlobalData', {
            singleton: true,
            lastSelectedMenuId:-1,
            urlPrefix: urlPrefix,
            memoryStore: memoryStore,
            cpuStore: cpuStore,
            serverSize: serverSize,
            isLogined: false,
            viewAjaxException: false,
            busyThreadsChartInterval:-1
        });


        this.initExtAjax();
        this.initVType();
    },

    initExtAjax: function() {
        /*
         * Global Ajax Config
         */

        Ext.Ajax.timeout = 20000;// default is 30000.
        Ext.Ajax.on("requestexception", function(conn, response, options, eOpts){

            if(response.timedout){

                Ext.Msg.show({
                    title:'Request Timeout',
                    msg: options.url +' request is timeout.',
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.ERROR
                });

            }else if(response.status == 403){

                //if(options.url.indexOf("auth/onAfterLogin") > -1){
                if (GlobalData.viewAjaxException === false) {
                    return;
                }

                var resJson = Ext.JSON.decode(response.responseText);

                Ext.Msg.show({
                    title:'Access Deny',
                    msg: options.url + ": " + resJson.msg,
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.ERROR,
                    fn : function(btn) {
                        if (resJson.data == "notLogin") {
                            window.location.reload();
                        }
                    }
                });

            }else{
                var resJson = Ext.JSON.decode(response.responseText);

                Ext.Msg.show({
                    title:'Server Error',
                    //msg: 'server-side failure with status code ' + response.status,
                    msg: resJson.msg,
                    buttons: Ext.Msg.OK,
                    icon: Ext.Msg.ERROR
                });
            }
        });
    },

    initVType: function() {
        /*
         * Global Validation(VTypes) Config
         */
        Ext.apply(Ext.form.field.VTypes, {
            daterange: function(val, field) {
                var date = field.parseDate(val);

                if (!date) {
                    return false;
                }
                if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) {
                    var start = field.up('form').down('#' + field.startDateField);
                    start.setMaxValue(date);
                    start.validate();
                    this.dateRangeMax = date;
                }
                else if (field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) {
                    var end = field.up('form').down('#' + field.endDateField);
                    end.setMinValue(date);
                    end.validate();
                    this.dateRangeMin = date;
                }
                /*
                 * Always return true since we're only using this vtype to set the
                 * min/max allowed values (these are tested for after the vtype test)
                 */
                return true;
            },

            daterangeText: 'Start date must be less than end date',

            password: function(val, field) {
                //var pwd = field.up('form').down('#passwd');
                pwd = field.previousNode('textfield');
                return (val == pwd.getValue());
            },

            passwordText: 'Passwords do not match',

            numeric: function(val, field) {
                var numericRe = /(^-?\d\d*\.\d*$)|(^-?\d\d*$)|(^-?\.\d\d*$)/;
                return numericRe.test(val);
            },
            numericText : 'Not a valid numeric number. Must be numbers',
            numericMask : /[.0-9]/,

            template: function(val, field) {
                var templateRe = /^[a-zA-Z0-9_\.\-]*$/;
                return templateRe.test(val);
            },
            templateText : "영문 대소문자, 숫자, '_', '-', '.' 만 가능합니다."
        });
    }

});
