package edu.sjsu.nutritionfinder.networking

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class NetworkingUtils {

    companion object {
        private val executor: Executor = Executors.newSingleThreadExecutor()

        fun asyncConnection(
            url: String?,
            method: String,
            payload: String,
            headers: Map<String?, String?>,
            responseHandler: ResponseHandler
        ) {
            executor.execute(Runnable {
                val url1: URL
                var urlConnection: HttpURLConnection? = null
                var inputStream: InputStream? = null
                try {
                    url1 = URL(url)
                    urlConnection = url1.openConnection() as HttpURLConnection
                    urlConnection.setRequestMethod(method)
                    for ((key, value) in headers) {
                        urlConnection.setRequestProperty(key, value)
                    }
                    if (method.equals("POST", ignoreCase = true)) {
                        urlConnection.setDoOutput(true)
                        val bytes = payload.toByteArray(charset("utf-8"))
                        urlConnection.getOutputStream().write(bytes, 0, bytes.size)
                    }
                    urlConnection.connect()
                    val responseCode: Int = urlConnection.getResponseCode()
                    if (responseCode == 200) {
                        inputStream = urlConnection.getInputStream()
                        val sb = StringBuilder()
                        val br = BufferedReader(InputStreamReader(inputStream))
                        var line: String?
                        while (br.readLine().also { line = it } != null) {
                            sb.append(line)
                        }
                        responseHandler.onSuccess(sb.toString())
                    } else {
                        responseHandler.onFailure(
                            ResponseHandler.FailureType.RESPONSE,
                            responseCode
                        )
                    }
                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                    responseHandler.onFailure(ResponseHandler.FailureType.EXCEPTION, -1)
                } catch (e: IOException) {
                    e.printStackTrace()
                    responseHandler.onFailure(ResponseHandler.FailureType.EXCEPTION, -1)
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    urlConnection?.disconnect()
                }
            })
        }
    }

    interface ResponseHandler {
        fun onSuccess(response: String?)
        fun onFailure(failureType: FailureType?, responseCode: Int)
        enum class FailureType {
            EXCEPTION, RESPONSE
        }
    }
}