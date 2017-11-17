#import "CDVReplay.h"
#import <Cordova/CDVViewController.h>

@implementation CDVReplay

#pragma mark - RPScreenRecorderDelegate

- (void)screenRecorder:(RPScreenRecorder *)screenRecorder didStopRecordingWithError:(NSError *)error previewViewController:(nullable RPPreviewViewController *)previewViewController {
    // handle error which caused unexpected stop of recording
}

- (void)screenRecorderDidChangeAvailability:(RPScreenRecorder *)screenRecorder {
    // handle screen recorder availability changes
}

#pragma mark - RPPreviewViewControllerDelegate

- (void)previewControllerDidFinish:(RPPreviewViewController *)previewController {
    [previewController dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark - Interface Actions

- (void)init:(CDVInvokedUrlCommand*)command {

    CDVPluginResult* success = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:success callbackId:command.callbackId];
}

- (void)getCaptureState:(CDVInvokedUrlCommand*)command {

    CDVPluginResult* success = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:success callbackId:command.callbackId];
}

- (void)startRecording:(CDVInvokedUrlCommand*)command {
    RPScreenRecorder *sharedRecorder = RPScreenRecorder.sharedRecorder;
    sharedRecorder.delegate = self;
    [sharedRecorder startRecordingWithMicrophoneEnabled:YES handler:^(NSError *error) {
        if (error) {
            CDVPluginResult* failed = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:[error localizedDescription]];
            [self.commandDelegate sendPluginResult:failed callbackId:command.callbackId];
        }
        else
        {
            CDVPluginResult* success = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
            [self.commandDelegate sendPluginResult:success callbackId:command.callbackId];
        }
    }];
}

- (void)stopRecording:(CDVInvokedUrlCommand*)command {
    RPScreenRecorder *sharedRecorder = RPScreenRecorder.sharedRecorder;
    [sharedRecorder stopRecordingWithHandler:^(RPPreviewViewController *previewViewController, NSError *error) {
        if (error) {
            CDVPluginResult* failed = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:[error localizedDescription]];
            [self.commandDelegate sendPluginResult:failed callbackId:command.callbackId];
        }

        if (previewViewController) {

            previewViewController.previewControllerDelegate = self;

            previewViewController.modalPresentationStyle = UIModalPresentationFullScreen;

            [self.viewController presentViewController:previewViewController animated:YES completion:nil];

            CDVPluginResult* success = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
            [self.commandDelegate sendPluginResult:success callbackId:command.callbackId];
        }
    }];
}

@end
