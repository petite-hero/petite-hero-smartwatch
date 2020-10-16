package hero.api;

import android.net.Uri;

public class UriBuilder {

    public static String getLocationUri(){
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(ServerAPIs.SCHEME)
                .encodedAuthority(ServerAPIs.AUTHORITY)
                .appendPath(ServerAPIs.LOCATION);
        return uriBuilder.build().toString();
    }

}
