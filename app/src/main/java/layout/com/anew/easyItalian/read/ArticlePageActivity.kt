package layout.com.anew.easyItalian.read


import android.app.Activity
import android.os.Bundle
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVQuery
import kotlinx.android.synthetic.main.activity_article_page.*
import android.os.StrictMode
import android.widget.Toast
import com.brioal.selectabletextview.OnWordClickListener
import com.squareup.picasso.Picasso
import layout.com.anew.easyItalian.R
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class ArticlePageActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_page)

        // necessary for getting data from internet
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build())
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build())
        //end

        val query = AVQuery<AVObject>("Articles")
        val mytitle = query.whereEqualTo("UID","9900000").first["title"].toString()
        titleOfArticle.setText(mytitle)
        // directly use url when saving into leancloud
        val picNum = query.whereEqualTo("UID","9900000").first["imageUrl"].toString()
        val imUrm = "http://avisy.ddns.net:3322/"+picNum
        Picasso.get().load(imUrm).fit().into(image)
        val mytext = query.whereEqualTo("UID","9900000").first["text"].toString()
        text.setText(mytext)
        // onWordClickListener
        class onWordClick() : OnWordClickListener(){
            override  fun onNoDoubleClick(p0: String?) {
                text.dismissSelected()
                getTranslation(p0 ?:"")
            }
        }
        text.setOnWordClickListener(onWordClick())


    }

    fun getTranslation(word :String){
        var result = ""
        try{
            val googleTranslate = "https://translate.google.cn/translate_a/single?client=gtx&sl=it&tl=zh-CN&dt=t&q="
        val translateUrl = googleTranslate+word
        val url = URL(translateUrl)
        val urlConn = url.openConnection() as HttpURLConnection
        // 设置连接主机超时时间
        urlConn.connectTimeout = 5 * 1000
        //设置从主机读取数据超时
        urlConn.readTimeout = 5 * 1000
        // 设置是否使用缓存  默认是true
        urlConn.useCaches = false
        // 设置为Post请求
        urlConn.requestMethod = "GET"
        //urlConn设置请求头信息
        val USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36"
        urlConn.setRequestProperty("User-Agent", USER_AGENT)
        urlConn.connect()
        val statusCode = urlConn.responseCode
        var googleResult = ""
        if (statusCode == 200) {
            // 获取返回的数据
            googleResult = streamToString(urlConn.getInputStream()) ?:""
        }

        val jsonArray = JSONArray(googleResult).getJSONArray(0)
        for (i in 0 until jsonArray.length()) {
            result += jsonArray.getJSONArray(i).getString(0)
        }
        Toast.makeText(this,result,Toast.LENGTH_SHORT).show()
        }catch (e :Exception){
            result = "翻译失败";
            e.printStackTrace();
            result = "";
        }
    }

    fun streamToString(myis: InputStream): String? {
        try {
            val out = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len = myis.read(buffer)
            while (len != -1) {
                out.write(buffer, 0, len)
                len = myis.read(buffer)
            }
            out.close()
            myis.close()
            val byteArray = out.toByteArray()
            return String(byteArray)
        } catch (e: Exception) {
            return null
        }
    }

}
