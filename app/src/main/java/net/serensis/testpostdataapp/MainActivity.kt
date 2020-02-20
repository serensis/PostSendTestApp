package net.serensis.testpostdataapp

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.makeText
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    internal class NetworkAsyncTask(applicationContext: Context) : AsyncTask<String, Void, Void>() {

        var sUrl = ""
        var param = ""
        var data = ""
        var context:Context? = applicationContext

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            Log.d("!!!!!!!!!!!!!!!!!!!!!", "끝")

            makeText(context, data, Toast.LENGTH_LONG).show()

        }

        override fun doInBackground(vararg p0: String?): Void? {
            sUrl = p0[0].toString()
            param = p0[1].toString()
            var isSuccess:Boolean = Scanrisk(0,"test","test2", "test3", "test4")
            if (isSuccess) {
                publishProgress()
            }
            return null
        }

        fun Scanrisk(
                policy: Int,
                code: String,
                name: String,
                reason: String,
                version: String): Boolean {


            // 아래는 2차 난독화를 적용후 보안이벤트 로그가 정상적으로 들어오는지 확인하기 위한 로그입니다.
            Log.i("HYYYYYYCallBack", "policy=$policy, code=$code, name=$name, reason=$reason")

            var success:Boolean = false

            var httpcon:HttpURLConnection? = null
            var url: URL? = null
            try {
                if (sUrl.isBlank()) sUrl = "http://192.168.10.107:8000/cgi-bin/test.py"
                url = URL(sUrl)
                httpcon = url.openConnection() as HttpURLConnection   // 접속

                //--------------------------
                //   전송 모드 설정
                //--------------------------
                httpcon.doInput = true                 // 서버에서 읽기 모드 지정
                httpcon.doOutput = true                // 서버로 쓰기 모드 지정
                httpcon.requestMethod = "POST"         // 전송 방식은 POST

                httpcon.setRequestProperty("Accept-Charset", "UTF-8")
                // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
                httpcon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")

                val buffer = StringBuffer()

                if (param.isBlank()) {
                    buffer.append("policy").append("=").append(policy).append("&")// 변수에 값 대입
                    buffer.append("code").append("=").append(code).append("&")//  변수 앞에 '$' 붙이지 않는다
                    buffer.append("name").append("=").append(name).append("&")// 변수 구분은 '&' 사용
                    buffer.append("reason").append("=").append(reason).append("&")// 변수 구분은 '&' 사용
                    buffer.append("version").append("=").append(version)
                } else {
                    buffer.append(param)
                }

                var inStream: InputStream? = null
                try {
                    val outStream = httpcon.outputStream
                    outStream.write(buffer.toString().toByteArray(charset("UTF-8")))
                    outStream.flush()
                    outStream.close()

                    Log.i("HYYYYYYCallBack", "push connect!")

                    if (httpcon.responseCode != HttpURLConnection.HTTP_OK) {
                        Log.i("HYYYYYYCallBack", "왜?!!!!")
                        //return
                    } else {
                        Log.i("HYYYYYYCallBack", "성공!!!!")
                        inStream = BufferedInputStream(httpcon.getInputStream())

                        val reader = BufferedReader(InputStreamReader(inStream, "UTF-8"))
                        var cBuffer = CharArray(2048)
                        var iReadSize: Int = 0

                        var datas = StringBuilder()

                        iReadSize = reader.read(cBuffer)

                        while (iReadSize != -1) {
                            datas.append(cBuffer, 0, iReadSize)
                            iReadSize = reader.read(cBuffer)
                        }

                        Log.i("HYYYYYYCallBack", "ddata!!! :: $datas")

//                        var inputAsString = httpcon.inputStream.bufferedReader().use { it.readText() }  // defaults to UTF-8
                        var inputAsString = datas.toString()
                        Log.i("HYYYYYYCallBack", inputAsString)
                        data = inputAsString
                        success = true
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    if (inStream != null) {
                        try {
                            inStream?.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    httpcon?.disconnect()
                    Log.i("HYYYYYYCallBack", "작업 끝")
                }

            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                httpcon?.disconnect()
                return success
            }

            //---------------------------------------------------------------------------------------------------------------------
            // if(policy==1) 분기문은 보안정책 위반의 경우에만 경고 창을 표시하는 예제입니다.
            // 필요한 경우 policy가 0 일 때도 처리를 할 수 있습니다.
            //---------------------------------------------------------------------------------------------------------------------
        }
    }

    fun sendData(v: View){

        val task:NetworkAsyncTask = NetworkAsyncTask(applicationContext)

        try {
            var sUrl:String = ""
            sUrl = findViewById<TextView>(R.id.textView).text.toString()
            var sParam:String = ""
            sParam = findViewById<TextView>(R.id.textView2).text.toString()

            task.execute(sUrl,sParam)

        } catch (e: Exception) {
            // No error
            Log.i("TEST!!!","errr")
            e.printStackTrace()
        }
    }

}
