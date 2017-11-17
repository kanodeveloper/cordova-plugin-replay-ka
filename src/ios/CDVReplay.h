/*
#import <UIKit/UIKit.h>
#import <ReplayKit/ReplayKit.h>

@interface ViewController : UIViewController <RPScreenRecorderDelegate, RPPreviewViewControllerDelegate>

@property (weak, nonatomic) RPPreviewViewController *previewViewController;

- (IBAction)doRecord:(id)sender;
- (IBAction)doPreviewSave:(id)sender;

@end
*/


#import <Cordova/CDV.h>
#import <UIKit/UIKit.h>
#import <ReplayKit/ReplayKit.h>

@interface CDVReplay : CDVPlugin <UIWebViewDelegate, RPScreenRecorderDelegate, RPPreviewViewControllerDelegate>
{
}

@property (weak, nonatomic) RPPreviewViewController *previewViewController;

- (void)init:(CDVInvokedUrlCommand*)command;
- (void)startRecording:(CDVInvokedUrlCommand*)command;
- (void)stopRecording:(CDVInvokedUrlCommand*)command;
- (void)getCaptureState:(CDVInvokedUrlCommand*)command;

@end
