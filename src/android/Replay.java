package com.kanoapps.cordova.replay;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.util.Log;
import android.content.Context;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Games.GamesOptions;
import com.google.android.gms.games.video.Videos;
import com.google.android.gms.games.video.CaptureState;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Cordova plugin that allows for an arbitrarly sized and positioned WebView to be shown ontop of the canvas
 */
public class Replay extends CordovaPlugin  {

    private static final String TAG = "Replay";
    private static final String PREFS_NAME = "Replay";

    protected GoogleApiClient mGoogleApiClient = null;

    /**
     * Executes the request and returns PluginResult
     *
     * @param  action
     * @param  args
     * @param  callbackContext
     * @return boolean
     * @throws JSONException
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        String error_msg = null;

        if (action.equals("init")) {

            Context context = this.cordova.getActivity().getApplicationContext();

            try {

                if( mGoogleApiClient == null )
                {
                    GamesOptions mGamesApiOptions = GamesOptions.builder().setShowConnectingPopup(false).build();

                    mGoogleApiClient = new GoogleApiClient.Builder(context)
                            .addApi(Games.API, mGamesApiOptions)
                            .addScope(Games.SCOPE_GAMES)
                            .build();
                }

                if (!mGoogleApiClient.isConnected()) {
                    Log.v(TAG, "Replay: connecting client");
                    mGoogleApiClient.connect();
                }

                callbackContext.success();

            } catch (Exception e) {
                error_msg = "Replay: " + e.getMessage();
                Log.e(TAG, error_msg);
                callbackContext.error(error_msg);
            }

            return true;
        }
        else if (action.equals("startRecording")) {

            try {

                if (mGoogleApiClient.isConnected()) {

                    Intent intent = Games.Videos.getCaptureOverlayIntent(mGoogleApiClient);

                    this.cordova.getActivity().startActivityForResult(intent, 777);

                    callbackContext.success();
                }
                else {
                    error_msg = "Replay: Client not connected";
                    Log.e(TAG, error_msg);
                    callbackContext.error(error_msg);
                }

            } catch (Exception e) {
                error_msg = "Replay: " + e.getMessage();
                Log.e(TAG, error_msg);
                callbackContext.error(error_msg);
            }

            return true;
        }
        else if (action.equals("stopRecording")) {

            //handled by the Android overlay
            return true;
        }
        else if (action.equals("getCaptureState")) {
            PendingResult<Videos.CaptureStateResult> pendingResult = Games.Videos.getCaptureState(mGoogleApiClient);

            final CallbackContext callbackContextFinal = callbackContext;

            pendingResult.setResultCallback(new ResultCallback<Videos.CaptureStateResult>() {
                @Override
                public void onResult(Videos.CaptureStateResult captureStateResult) {
                    CaptureState captureState = captureStateResult.getCaptureState();

                    try {
                        JSONObject r = new JSONObject();
                        r.put("isCapturing", captureState.isCapturing());
                        r.put("isOverlayVisible", captureState.isOverlayVisible());
                        r.put("isPaused", captureState.isPaused());
                        callbackContextFinal.success(r);
                    }
                    catch (Exception e) {
                        String error_msg = "Replay: " + e.getMessage();
                        Log.e(TAG, error_msg);
                        callbackContextFinal.error(error_msg);
                    }
                }
            });

            return true;
        }

        // Default response to say the action hasn't been handled
        return false;
    }
}