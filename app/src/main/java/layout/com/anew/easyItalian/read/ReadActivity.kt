package layout.com.anew.easyItalian.read

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVQuery
import layout.com.anew.easyItalian.MainActivity
import layout.com.anew.easyItalian.R
import java.util.ArrayList
import android.view.ViewGroup




class ReadActivity : Activity() {


    private val articleList = ArrayList<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        val layoutManager = LinearLayoutManager(this@ReadActivity)


        //初始化数据
        initArticle()


        //将articleTist里的文章展示出来
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        val adapter = ArticleAdapter(this@ReadActivity, articleList)
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val back = findViewById<Button>(R.id.backForRead)
        back.setOnClickListener {
            finish()
            val intent = Intent()
            intent.setClass(this@ReadActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    //初始化数据
    private fun initArticle() {
        //    for(int i=0;i<2;i++){
        /*
            *
            *
            * String title="Amo la mia mamma e il mio papa  ";
            String level="4星";
            String text="Per la maggior parte del mondo cristiano, il culmine della" +
                    " festività natalizia è il cenone di Natale. è il momento in cui le " +
                    "famiglie si riuniscono per festeggiare la vigilia di Natale che" +
                    " normalmente si celebra dopo la Messa di mezzanotte. In Francia " +
                    "questo pasto viene chiamato \"le reveillon\". Il cenone natalizio " +
                    "varia secondo le tradizioni culinarie, il menu consiste in anatra, " +
                    "paste fatte con farina di grano saraceno con panna acida, tacchino con " +
                    "castagne, mentre per i parigini, ostriche e Foie Gras. Una torta che " +
                    "porta il nome di \"La Bouche de Noel\" con la forma di un tronco d'albero" +
                    " viene consumata durante la reveillon. Lo Champagne, il vino frizzante " +
                    "prodotto nelle regione denominata Champagne, viene bevuto per celebrare " +
                    "entrambe le festività di Natale e di Capodanno."+i;
            String id="id"+i;
            Article article=new Article(id,title,level,text,"image1");*/
        //  String uid = "9900000";
        var uid = 9900000
        var article = getArticle(uid.toString())
        //  int a = 99;
       // articleList.add(article)
        //String.valueOf(a);
        //将article放入链表中
        //   articleList.add(article);
        //  Article article1 = getArticle("9900001");
        //将article放入链表中
        // articleList.add(article1);
        //Article article2 = getArticle("9900002");
        //将article放入链表中
        // articleList.add(article2);

        while (article.title !== "Error") {
            articleList.add(article)
            uid++
            article = getArticle(uid.toString())
        }

        //}
    }

    private fun getArticle(uid: String): Article {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build())
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build())


        val query = AVQuery<AVObject>("Articles")
        val thisArticle: Article
        if (query.whereEqualTo("UID", uid) != null) {
            val a = query.whereEqualTo("UID", uid).first

            val thisTitle :String?
            val thisLevel :String?
            val thisImageUrl:String?
            val thisText :String ?
                if(a != null){
                    thisTitle = a.get("title").toString()
                    thisLevel = a.get("level").toString()
                    thisImageUrl = "http://avisy.ddns.net:3322/" + a.get("imageUrl").toString()
                    thisText = a.get("text").toString()
                    thisArticle = Article(uid, thisTitle, thisLevel, thisText, thisImageUrl)
                } else thisArticle = Article("9900000", "Error", "0", "Error", "Error")
            return thisArticle

        } else {
            thisArticle = Article("9900000", "Error", "0", "Error", "Error")
            return thisArticle
        }
    }

}

