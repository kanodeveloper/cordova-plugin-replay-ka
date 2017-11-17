var exec = require('cordova/exec');

var Replay = function() {};

Replay.startRecording = function(success, error) {
    exec(success, error, "Replay", "startRecording");
};

Replay.stopRecording = function(success, error) {
    exec(success, error, "Replay", "stopRecording");
};

Replay.init = function(success, error) {
    exec(success, error, "Replay", "init");
};

Replay.getCaptureState = function(success, error) {
    exec(success, error, "Replay", "getCaptureState");
};

module.exports = Replay;
