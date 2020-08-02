package com.example.testassignment.Model

import com.example.testassignment.DB.CompanyDataItem
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import javax.security.cert.CertificateException


class CompanyInteractor {

    interface OnFinishedListener {
        fun onResultSuccess(arrNewsUpdates: List<CompanyDataItem>)
        fun onResultFail(strError: String)
    }

    fun requestNewsDataAPI(onFinishedListener: OnFinishedListener) {

        var builder = Retrofit.Builder()
            .baseUrl("https://next.json-generator.com/api/json/")
            .client(trustAllCertificates())
            .addConverterFactory(GsonConverterFactory.create())

        var api = builder.build().create(APIServie::class.java)
        var call = api.getCompanyData()
        call.enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                var gson = GsonBuilder().serializeNulls().create()
                if(response.code() == 200) {
                    var model = gson.fromJson(response.body()!!.string(), Array<CompanyDataItem>::class.java).toList()
                    onFinishedListener.onResultSuccess(model)
                }
            }

        })

    }

    fun trustAllCertificates(): OkHttpClient  {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts =
                arrayOf<TrustManager>(
                    object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    }
                )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.MINUTES)
            builder.sslSocketFactory(
                sslSocketFactory,
                trustAllCerts[0] as X509TrustManager
            )
            builder.hostnameVerifier(object : HostnameVerifier {
                override fun verify(hostname: String?, session: SSLSession?): Boolean {
                    return true
                }
            })
            builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }


    interface APIServie{
        @GET("get/Vk-LhK44U")
        fun getCompanyData(): Call<ResponseBody>
    }
}