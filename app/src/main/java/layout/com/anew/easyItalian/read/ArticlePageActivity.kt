package layout.com.anew.easyItalian.read


import android.app.Activity
import android.os.Bundle
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVQuery
import kotlinx.android.synthetic.main.activity_article_page.*
import android.os.StrictMode
import android.util.LayoutDirection
import android.view.Gravity
import android.widget.Toast
import layout.com.anew.easyItalian.selectabletextview.OnWordClickListener
import layout.com.anew.easyItalian.selectabletextview.SelectableTextView
import com.example.zhouwei.library.CustomPopWindow
import com.squareup.picasso.Picasso
import layout.com.anew.easyItalian.R
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.pop_layout1.view.*


class ArticlePageActivity() : Activity() {


    var uid = "9900000"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_page)

        getArticle()
        // onWordClickListener
        class onWordClick(a:Int,b:Int) : OnWordClickListener(){
            val a =a
            val b =b
            override  fun onNoDoubleClick(p0: String?) {

                val text = findViewById<SelectableTextView>(R.id.text)
                text.dismissSelected()
               // getTranslation(p0 ?:"")
                val contentView = LayoutInflater.from(this@ArticlePageActivity).inflate(R.layout.pop_layout1, null)
                //处理popWindow 显示内容
               // handleLogic(contentView)
                //创建并显示popWindow
                contentView.translation.text = getTranslation(p0?:"ciao")
                val mCustomPopWindow = CustomPopWindow.PopupWindowBuilder(this@ArticlePageActivity)
                        .setView(contentView)
                        .create().showAtLocation(text,Gravity.FILL_HORIZONTAL,a,b)
                        //.showAsDropDown(text, a, b)

            }
        }

        text.setOnWordClickListener(onWordClick(MotionEvent.AXIS_HAT_X,MotionEvent.AXIS_HAT_Y))

    }

    private fun getArticle(){

        val ins = intent
        val listdata = ins.getStringArrayListExtra("data")
        uid=listdata[0]
        // necessary for getting data from internet
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build())
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build())
        val query = AVQuery<AVObject>("Articles")
        val mytitle = query.whereEqualTo("UID",uid).first["title"].toString()
        titleOfArticle.setText(mytitle)
        val mylevel =  query.whereEqualTo("UID",uid).first["level"].toString()
        //show level
        // change it
        Toast.makeText(this,mylevel,Toast.LENGTH_SHORT).show()
        // directly use url when saving into leancloud
        val picNum = query.whereEqualTo("UID",uid).first["imageUrl"].toString()
        val imUrm = "http://avisy.ddns.net:3322/"+picNum
        Picasso.get().load(imUrm).fit().into(image)
        val mytext = query.whereEqualTo("UID",uid).first["text"].toString()
        text.setText(mytext)


    }

    fun getTranslation(word :String):String{
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
            return result
        }catch (e :Exception){
            result = "翻译失败";
            e.printStackTrace();
            result = "";
        }
        return result
    }

    private fun streamToString(myis: InputStream): String? {
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
