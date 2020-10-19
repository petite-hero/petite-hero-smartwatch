package hero.api;

import android.net.Uri;

public class UriBuilder {

    public static String getUri(){
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(ServerAPIs.SCHEME)
                .encodedAuthority(ServerAPIs.AUTHORITY);
        return uriBuilder.build().toString();
    }

}
