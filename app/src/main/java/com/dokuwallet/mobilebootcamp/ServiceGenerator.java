package com.dokuwallet.mobilebootcamp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;

public class ServiceGenerator {
    private static final String CACHE_CONTROL = "Cache-Control";
    private static Retrofit provideRetrofit(String baseUrl) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("MMM dd,yyyy HH:mm:ss").create();

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            return new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(getUnsafeOkHttpClientTLS1())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        } else {
            return new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(getUnsafeOkHttpClientTLS())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
    }

    private static Cache provideCache() {
        Cache cache = null;
        try {
            cache = new Cache(new File(AnalyticsApps.getInstance().getCacheDir(), "http-cache"), 10 * 1024 * 1024); // 10 MB
        } catch (Exception e) {
            System.out.println("ServiceGenerator : Could not create Cache!");
        }
        return cache;
    }

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> System.out.println("ServiceGenerator :" + message));
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? HEADERS : NONE);
        return httpLoggingInterceptor;
    }

    private static Interceptor provideCacheInterceptor() {
        return chain -> {
            Response response = chain.proceed(chain.request());

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(2, TimeUnit.MINUTES)
                    .build();

            return response.newBuilder()
                    .header(CACHE_CONTROL, cacheControl.toString())
                    .build();
        };
    }

    private static Interceptor provideOfflineCacheInterceptor() {
        return chain -> {
            Request request = chain.request();

            if (!AnalyticsApps.hasNetwork()) {
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();

                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();
                return chain.proceed(request);
            }
            return chain.proceed(request);
        };
    }

    private static OkHttpClient getUnsafeOkHttpClientTLS1() {
        ConnectionSpec spec;
        try {
            final X509TrustManager trustAllCerts = new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            };

            final SSLSocketFactory sslSocketFactory = new TLSSocketFactory();

            spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA)
                .build();


            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient = okHttpClient.newBuilder()
                    .addInterceptor(provideHttpLoggingInterceptor())
                    .addInterceptor(provideOfflineCacheInterceptor())
                    .addNetworkInterceptor(provideCacheInterceptor())
                    .cache(provideCache())
                    //.sslSocketFactory(sslSocketFactory,trustAllCerts)
                    //.connectionSpecs(Collections.singletonList(spec))
                    .retryOnConnectionFailure(false)
                    .readTimeout(300, TimeUnit.SECONDS)
                    .connectTimeout(300, TimeUnit.SECONDS)
                    .writeTimeout(300, TimeUnit.SECONDS)
                    .build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static OkHttpClient getUnsafeOkHttpClientTLS() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};

            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();

            ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_0)
                    .cipherSuites(CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient = okHttpClient.newBuilder()
                    .addInterceptor(provideHttpLoggingInterceptor())
                    .addInterceptor(provideOfflineCacheInterceptor())
                    .addNetworkInterceptor(provideCacheInterceptor())
                    .cache(provideCache())
                    //.sslSocketFactory(sslSocketFactory)
                    //.connectionSpecs(Collections.singletonList(spec))
                    .retryOnConnectionFailure(false)
                    .readTimeout(300, TimeUnit.SECONDS)
                    .connectTimeout(300, TimeUnit.SECONDS)
                    .writeTimeout(300, TimeUnit.SECONDS)
                    .build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ListApiInterface ApiService(String URL) {
        return provideRetrofit(URL).create(ListApiInterface.class);
    }
}