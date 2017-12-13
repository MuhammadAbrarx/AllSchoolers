package abdullahsoft.com.thenewspaperapp.Handler;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Md. Abrar on 5/4/2016.
 */
public class OkHttpRequestHandler
{
    private Context context;
    private String url;
    private static final String TAG = "okhttp";

    //    InputStream inputStream;
    private static final int SUCCESSFUL_DOWNLOAD = 200;//for now replying to all successcode in similar manner
    //but when needed check seperately for each 201,202 etc

    private boolean isRequestProblem = false;
    private boolean isResponseProblem = false;

    int cacheSize;
    File cacheDirectory;
    Cache cache;

    OkHttpClient client;
    Response response;
    RequestBody requestBody;
    FormBody.Builder formBuilder = new FormBody.Builder();
    Request request ;
    Request.Builder requestBuilder = new Request.Builder();
//    private Request request;


    public OkHttpRequestHandler(Context context)
    {
        this.context = context;
    }

    public void BuildCache()
    {
        //Cache path has to be created only once even if multiple objects created
        //Client initialization has to be done only once
        cacheDirectory = new File(context.getCacheDir(), "http");
        cacheSize = 10 * 1024 * 1024;
        cache = new Cache(cacheDirectory, cacheSize);
        Log.e("CachePath created", context.getCacheDir().getAbsolutePath());
        client = new OkHttpClient.Builder()
                .cache(cache)
                .build();
    }

    public void SetTimeouts(int connectionTimeout, int readTimeout, int writeTimeout)
    {
        if (connectionTimeout > 0 && readTimeout > 0 && writeTimeout > 0)
        {
            client = new OkHttpClient.Builder()
                    .cache(cache)
                    .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
                    .readTimeout(readTimeout, TimeUnit.SECONDS)
                    .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                    .build();
        }
        else if (connectionTimeout > 0 && readTimeout > 0)
        {
            client = new OkHttpClient.Builder()
                    .cache(cache)
                    .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
                    .readTimeout(readTimeout, TimeUnit.SECONDS)
                    .build();
        }
        else if (connectionTimeout > 0 && writeTimeout > 0)
        {

            client = new OkHttpClient.Builder()
                    .cache(cache)
                    .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
                    .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                    .build();
        }
        else if (readTimeout > 0 && writeTimeout > 0)
        {

            client = new OkHttpClient.Builder()
                    .cache(cache)
                    .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                    .readTimeout(readTimeout, TimeUnit.SECONDS)
                    .build();
        }
        else if (connectionTimeout > 0)
        {

            client = new OkHttpClient.Builder()
                    .cache(cache)
                    .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
                    .readTimeout(readTimeout, TimeUnit.SECONDS)
                    .build();
        }
        else if (readTimeout > 0)
        {
            client = new OkHttpClient.Builder()
                    .cache(cache)
                    .readTimeout(readTimeout, TimeUnit.SECONDS)
                    .build();
        }
        else if (writeTimeout > 0)
        {
            client = new OkHttpClient.Builder()
                    .cache(cache)
                    .readTimeout(readTimeout, TimeUnit.SECONDS)
                    .build();
        }
        else if (connectionTimeout == 0 && readTimeout == 0 && writeTimeout == 0)
        {
            client = new OkHttpClient.Builder()
                    .cache(cache)
                    .build();
            Log.e(TAG, "All Timeouts Null");
        }
        else
        {
            Log.e(TAG, "Invalid inputs,Negative inputs not accepted");
        }
    }

    public void AddingKeyValuesToRequestBody(String key, String value)
    {
        formBuilder.add(key, value);
    }

    public void BuildingRequestBody()
    {
        //Designed for use with LIIStudios Only
        // Need to modify for other uses
        if (formBuilder!=null)
        {
            requestBody = formBuilder.build();
        }
        else
        {
            Log.e(TAG,"FORM BUILDER NULL");
        }

    }

    public void AddUrlToRequestBuilder(String url)
    {
        this.url = url;
        requestBuilder.url(url);
    }

    public void AddHeaderToRequestBuilder(String key, String value)
    {
        requestBuilder.addHeader(key,value);
    }

    public void BuildSoundCloudUrl(String playListName, String clientID)
    {
        this.url = "http://api.soundcloud.com/playlists/" + playListName + "?client_id=" + clientID;
    }


    public void BuildingPostTypeRequestWithCustomBody()
    {
        if (requestBody != null)
        {
            requestBuilder.post(requestBody);
            Log.e("Build Custom Request", "Url Request Build Complete");
        }
        else
        {
            Log.e(TAG, "Requset Body is emptly while building post type request");
        }
    }

    public void BuildingGetTypeRequest(String url)
    {
        //Request Body is not Required here
        this.url = url;
        requestBuilder.url(url)
                .get()
                .build();
        Log.e("Build Custom Request", "Url Request Build Complete");

    }

    public void BuildingNormalRequest(String url)
    {
        this.url = url;
        requestBuilder.url(this.url)
                .build();
        Log.e("Build Request", "Url Request Build Complete");
    }

    public void AddDefaultUserAgentHeader()
    {
        requestBuilder.header("User-Agent", "OkHttp Headers.java");
    }
    public void FinalizeBuildingRequest()
    {
        request = requestBuilder.build();
    }

    // For syncronized tasks
    public Response NewRequestCallSync()
    {
        response = null;
        if (request!=null)
        {
            try
            {
                response = client.newCall(request).execute();
                isRequestProblem = false;
            }catch (IOException e)
            {
                e.printStackTrace();
                isRequestProblem = true;
            }
        }
        else
        {
            Log.e(TAG,"Request found null while NewRequestCallSync");
        }

        return response;
    }


    //For Asyncronus tasks
    public Response NewRequestCallAsync()
    {
        response = null;

            client.newCall(request).enqueue(new Callback() {
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    isRequestProblem = true;
                }

                public void onResponse(Call call, Response res) throws IOException {
                    if (!response.isSuccessful())
                    {
                        isResponseProblem = true;
                        throw new IOException("Failed to download file: " + response);
                    }
                    response = res;
                    // FileOutputStream fos = new FileOutputStream("d:/tmp.txt");
                    // fos.write(response.body().bytes());
                    // fos.close();
                }
            });


        return response;
    }



    public boolean isSuccessfulDownload()
    {
        if (!response.isSuccessful())
        {
            try
            {
                isRequestProblem = true;
                throw new IOException("Unexpected code " + response);
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        if (response.code() >= SUCCESSFUL_DOWNLOAD && response.code() < 300)
        {
            isResponseProblem = false;
            return true;// if download successfull ..
        }
        else
        {
            isResponseProblem = true;
        }
        return false; //by default
    }


    public boolean isRequestProblem()
    {
        return isRequestProblem;
    }

    public boolean isResponseProblem()
    {
        return isResponseProblem;
    }

}
