var deviceReady = false;
var appType = null;

var push = null;
var pushToken = null;

if (navigator.userAgent.match(/Android/i)) {
    if (navigator.userAgent.indexOf('com.newzenpnp.candy') >= 0) {
        console.log('Android App');
        document.addEventListener("deviceready", onDeviceReady, false);

        var head = document.getElementsByTagName('head')[0];
        var script = document.createElement('script');
        script.type = 'text/javascript';
        if (window.location.href.indexOf('/mobile/') >= 0) {
            script.src = 'assets/cordova/android/cordova.js?v=' + Date.now();
        } else {
            script.src = '/mobile/assets/cordova/android/cordova.js?v=' + Date.now();
        }
        head.appendChild(script);

        appType = 'android'
    } else {
        console.log('Android Web');
    }
} else if (navigator.userAgent.match(/iPhone|iPad|iPod/i)) {
    if (navigator.userAgent.indexOf('com.newzenpnp.candy') >= 0) {
        console.log('iOS App');
        document.addEventListener("deviceready", onDeviceReady, false);

        var head = document.getElementsByTagName('head')[0];
        var script = document.createElement('script');
        script.type = 'text/javascript';
        if (window.location.href.indexOf('/mobile/') >= 0) {
            script.src = 'assets/cordova/ios/cordova.js?v=' + Date.now();
        } else {
            script.src = '/mobile/assets/cordova/ios/cordova.js?v=' + Date.now();
        }
        head.appendChild(script);

        appType = 'ios';
    } else {
        console.log('iOS Web');
    }
} else {
    console.log('PC');
}

function onDeviceReady() {
    console.log('onDeviceReady');
    deviceReady = true;

    window.open = cordova.InAppBrowser.open;

    push = PushNotification.init({
        android: {
            topics: ['CANDY']
        },
        ios: {
            alert: true,
            badge: true,
            sound: true,
            topics: ['CANDY']
        }
    });

    PushNotification.createChannel(function() {
        console.log('createChannel success');
    }, function() {
        console.log('createChannel error');
    }, {
        id: 'PushPluginChannel',
        description: '일반',
        importance: 3,
        vibration: true
    });

    push.on('registration', function(data) {
        pushToken = data.registrationId;
        console.log(pushToken);
    });

    document.addEventListener("resume", function() {
        var pauseTime = isSetNull(localStorage.getItem('pauseTime'), Date.now());

        var currentTime = Date.now();
        if (currentTime - pauseTime > 5 * 60 * 1000) {
            location.href = '/mobile';
        }
    }, false);

    document.addEventListener("pause", function() {
        localStorage.setItem('pauseTime', Date.now());
    }, false);

    document.addEventListener("backbutton", function() {
        if (confirm('앱을 종료하시겠습니까?')) {
            navigator.app.exitApp();
        }
    }, false);
}
