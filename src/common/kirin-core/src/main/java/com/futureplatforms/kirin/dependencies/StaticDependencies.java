package com.futureplatforms.kirin.dependencies;

import java.util.HashMap;
import java.util.Map;

import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.HttpVerb;
import com.futureplatforms.kirin.dependencies.StaticDependencies.NetworkDelegate.NetworkResponse;
import com.futureplatforms.kirin.dependencies.internal.TimerTask.TimerDelegate;
import com.futureplatforms.kirin.dependencies.json.JSONDelegate;
import com.futureplatforms.kirin.dependencies.xml.parser.XMLParser;

public final class StaticDependencies {
    public enum Configuration { Debug, Release } ;
    
    private static StaticDependencies instance;
    public static StaticDependencies getInstance() {
        if (instance == null) { instance = new StaticDependencies(); }
        return instance;
    }
    public static interface LogDelegate {
        public void log(String s);
    }
    public static interface SettingsDelegate {
        public String get(String key);
        public void put(String key, String value);
    }
    public static interface NetworkDelegateClient {
        public void doHttp(HttpVerb verb, String url, String payload, Map<String, String> headers, NetworkResponse callback);
    }
    
    public static class NetworkDelegate {
        private NetworkDelegateClient _Client;
        public NetworkDelegate(NetworkDelegateClient client) {
            this._Client = client;
        }
        
        public static enum HttpVerb { 
            DELETE(false), GET(false), POST(true), PUT(true);
            public final boolean _HasPayload;
            private HttpVerb(boolean hasPayload) {
                this._HasPayload = hasPayload;
            }
        };
        
        public static interface NetworkResponse {
            public void onSuccess(int res, String result, Map<String, String> headers);
            public void onFail(String code);
        }
        
        public final void doHttp(HttpVerb verb, String url, NetworkResponse callback) {
            doHttp(verb, url, null, new HashMap<String, String>(), callback);
        }
        
        public final void doHttp(HttpVerb verb, String url, String payload, NetworkResponse callback) {
            doHttp(verb, url, payload, new HashMap<String, String>(), callback);
        }
        
        public final void doHttp(HttpVerb verb, String url, String payload, Map<String, String> headers, NetworkResponse callback) {
            _Client.doHttp(verb, url, payload, headers, callback);
        }
    }

    private LogDelegate mLogDelegate;
    private SettingsDelegate mSettingsDelegate;
    private LocationDelegate mLocationDelegate;
    private JSONDelegate mJsonDelegate;
    private NetworkDelegate mNetworkDelegate;
    private XMLParser mXmlParser;
    private Formatter mFormatter;
    private boolean mInitialised;
    private Configuration _Profile;
    
    public LogDelegate getLogDelegate() { return mLogDelegate; }
    public SettingsDelegate getSettingsDelegate() { return mSettingsDelegate; }
    public LocationDelegate getLocationDelegate() { return mLocationDelegate; }
    public NetworkDelegate getNetworkDelegate() { return mNetworkDelegate; }
    public JSONDelegate getJsonDelegate() { return mJsonDelegate; }
    public XMLParser getXmlParser() { return mXmlParser; }
    public Formatter getFormatter() { return mFormatter; }
    public boolean initialised() { return mInitialised; }
    public Configuration getProfile() { return _Profile; }
    
    public void setDependencies(    LogDelegate logDelegate,
                                    SettingsDelegate settingsDelegate,
                                    LocationDelegate locationDelegate, 
                                    NetworkDelegateClient networkDelegateClient,
                                    JSONDelegate jsonDelegate,
                                    XMLParser xmlParser, 
                                    Formatter formatter, 
                                    Configuration profile) {
        this.mLogDelegate = logDelegate;
        this.mSettingsDelegate = settingsDelegate;
        this.mLocationDelegate = locationDelegate;
        this.mNetworkDelegate = new NetworkDelegate(networkDelegateClient);
        this.mJsonDelegate = jsonDelegate;
        this.mXmlParser = xmlParser;
        this.mFormatter = formatter;
        this._Profile = profile;
        this.mInitialised = true;
    }
}
