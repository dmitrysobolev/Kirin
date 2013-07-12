package com.futureplatforms.kirin.gwt.client.delegates;

import com.futureplatforms.kirin.dependencies.StaticDependencies.LocationDelegate;
import com.futureplatforms.kirin.dependencies.StaticDependencies.LocationResponse;

public class KirinLocation implements LocationDelegate {

    private LocationResponse resp;
    private boolean continuous;
    
    @Override
    public void getLocation(LocationResponse resp, Accuracy accuracy, boolean continuous) {
        this.resp = resp;
        this.continuous = continuous;
        doIt(accuracy == Accuracy.Fine, this);
    }
    
    private void locationSuccess(double lat, double lng) {
        resp.onSuccess(lat, lng);
        if (!continuous) {
            unregister();
        }
    }
    
    private void locationError(String errDesc) {
        resp.onFail(errDesc);
        if (!continuous) {
            unregister();
        }
    }
    
    public native void unregister() /*-{
        var location = require("Location")
        location.unregisterAllListeners();
    }-*/;
    
    private static native void doIt(boolean isFine, KirinLocation p) /*-{
        var location = require("Location")
        location.unregisterAllListeners()
        location.registerLocationListener(isFine, function(newLocation) {
            var lat = newLocation.latitude;
            var lng = newLocation.longitude;
            var timestamp = newLocation.timestamp;
            var horizAcc = newLocation.horizontalAccuracy;
            $entry( p.@com.futureplatforms.kirin.gwt.client.delegates.KirinLocation::locationSuccess(DD)(lat, lng) );
        });
        
        location.registerLocationErrorListener(function(errDesc) {
            $entry( p.@com.futureplatforms.kirin.gwt.client.delegates.KirinLocation::locationError(Ljava/lang/String;)(errDesc) );
        });
        
        location.refreshLocation();
    }-*/;
}
