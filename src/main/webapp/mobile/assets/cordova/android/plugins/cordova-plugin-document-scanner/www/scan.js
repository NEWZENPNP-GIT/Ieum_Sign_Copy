cordova.define("cordova-plugin-document-scanner.scan", function(require, exports, module) {
/*global cordova, module*/

module.exports = {
    scanDoc: function (sourceType, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Scan", "scanDoc", [sourceType]);
    }
};
});
