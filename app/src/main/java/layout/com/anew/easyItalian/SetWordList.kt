package layout.com.anew.easyItalian

import android.app.Activity
import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import layout.com.anew.easyItalian.recite.DaoOpt
import layout.com.anew.easyItalian.recite.Word
import java.io.FileInputStream
import javax.xml.parsers.DocumentBuilderFactory
import android.os.StrictMode
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVQuery
import kotlinx.android.synthetic.main.activity_set_word_list.*
import kotlinx.android.synthetic.main.article_item.*
import layout.com.anew.easyItalian.recite.BaseApplication
import layout.com.anew.easyItalian.recite.ReciteWordAcitivity
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL



// TODO process expectation
class SetWordList : Activity() {
    private val wordLists = ArrayList<Wordlist>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_word_list)

        // TODO use leancloud show all available wordLists in a listView and set onClick listener to set wordList
        //   downloadWordList("for_test.xml")
        //    createDatabase("for_test.xml")
        initWordlist()
        val layoutManager = LinearLayoutManager(this@SetWordList)
        recyclerView1.layoutManager = layoutManager
         recyclerView1!!.adapter = WordListAdapter(this,wordLists,itemClick())

    }

    // implement download and set wordList in the following part
    inner class itemClick: ItemClick{
        override fun OnItemClick(v: View, position: Int) {
            val wordlist = wordLists[position]
            val positiveText:String
            val mFile = File(getExternalFilesDir("wordlist").path+ "/" + wordlist.wordlistName)
            val flag = mFile.exists()
            if (flag) positiveText = "设置"  else positiveText="下载"
            MaterialDialog.Builder(this@SetWordList)
                         .title(wordlist.wordlistName)
                         .content(wordlist.wordlistDesc)
                         .negativeText("取消")
                         .positiveText(positiveText).onPositive {
                            dialog: MaterialDialog, which: DialogAction ->
                            Toast.makeText(this@SetWordList,positiveText,Toast.LENGTH_LONG).show()
                            try {
                                if(flag)    createDatabase(wordlist)
                                else {downloadWordList(wordlist)
                                    createDatabase(wordlist)}
                                val my = DaoOpt.getInstance()
                                if (getWordFromXml(wordlist,0).word==my.queryForId(this@SetWordList,0L)?.get(0)?.word){
                                    MaterialDialog.Builder(this@SetWordList)
                                            .title(wordlist.wordlistName)
                                            .content(wordlist.wordlistDesc+"\n设置成功\n是否进入学习?")
                                            .negativeText("否")
                                            .positiveText("是")
                                            .onPositive{
                                                dialog1: MaterialDialog, which1: DialogAction ->
                                                finish()
                                                val intent =Intent()
                                                intent.setClass(this@SetWordList,ReciteWordAcitivity::class.java)
                                                startActivity(intent)
                                            }.onNegative{
                                                dialog1: MaterialDialog, which1: DialogAction ->
                                                finish()
                                                val intent =Intent()
                                                intent.setClass(this@SetWordList,MainActivity::class.java)
                                                startActivity(intent)
                                            }.show()
                                }else{
                                    Toast.makeText(this@SetWordList,"Error",Toast.LENGTH_SHORT).show()
                                }
                            }catch (e:Exception) {
                                Toast.makeText(this@SetWordList,"Error",Toast.LENGTH_SHORT).show()
                                e.printStackTrace()
                            }
                            }
                         .show()
        }
    }


    private fun initWordlist(){
        var i = 1
        var wordlist = getWordlist(i)
        while (wordlist.wordlistID!="-1"){
            wordLists.add(wordlist)
            i+=1
            wordlist = getWordlist(i)
        }
    }

    private fun getWordlist(i:Int): Wordlist{
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build())
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build())
        val query = AVQuery<AVObject>("wordlist")
      if (query.whereEqualTo("wordlistID", i) != null){
            val a = query.whereEqualTo("wordlistID",i).first
            if (a!=null){
                val wordlist = Wordlist()
                wordlist.wordlistID = a.get("wordlistID").toString()
                wordlist.wordlistName = a.get("wordlistName").toString()
                wordlist.wordlistDesc = a.get("wordlistDesc").toString()
                wordlist.url=a.get("url").toString()
                return wordlist
            }else{
                val wordlist = Wordlist()
                wordlist.wordlistID = "-1"
                return wordlist
            }
        }else{
          val wordlist = Wordlist()
          wordlist.wordlistID = "-1"
          return wordlist
      }
    }

    private fun downloadWordList(wordList: Wordlist){

        //open xml file
        // TODO classify files on my server
        val path = wordList.url
        val wordlist = wordList.wordlistName+".xml"
        try {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads().detectDiskWrites().detectNetwork()
                    .penaltyLog().build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                    .penaltyLog().penaltyDeath().build())
            val httpUrl = URL(path)
            val connection = httpUrl.openConnection() as HttpURLConnection
            connection.readTimeout = 3000
            connection.connectTimeout=5000
            connection.requestMethod = "GET"
            connection.useCaches = false
            val USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36"
            connection.setRequestProperty("User-Agent", USER_AGENT)
            connection.connect()
            val statusCode = connection.responseCode
            if (statusCode == 200) {
                val input = connection.inputStream
                //写入本地
                val mFile = File(this.getExternalFilesDir("wordlist").path)
                val outputStream = FileOutputStream(mFile.path+ "/" + wordlist)
                var index: Int
                val bytes = ByteArray(1024)
                val downloadFile = outputStream
                index = input.read(bytes)
                while (index != -1) {
                    downloadFile.write(bytes, 0, index)
                    downloadFile.flush()
                    index = input.read(bytes)
                }
                downloadFile.close()
                input.close()
                Toast.makeText(this,"Download Complicated.",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Connect filed",Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

    }

    private fun getWordFromXml(wordList:Wordlist,num:Int): Word {
        val wordlist = wordList.wordlistName+".xml"
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        //open xml file
        //  val doc = db.parse(assets.open(list))
        val mFile = File(this.getExternalFilesDir("wordlist").path)
        val myIs = FileInputStream(mFile.path+ "/" + wordlist)

        val doc = db.parse(myIs)
        val wordList = doc.getElementsByTagName("items")
        val thisWord = Word()
        thisWord.id = num.toLong()
        thisWord.appearTime=0
        thisWord.correctTime=0
        thisWord.incorrectTime=0
        thisWord.nextAppearTime=0
        thisWord.eFactor=2.0
        thisWord.grasp = false

        if(num<wordList.length) {
            val elem = wordList.item(num)
            thisWord.word = elem.childNodes.item(1).textContent
            thisWord.transform = elem.childNodes.item(3).textContent
            thisWord.translation = elem.childNodes.item(5).textContent
            thisWord.example = elem.childNodes.item(7).textContent
        }
        else{
            thisWord.word = "Error"
            thisWord.translation = "Error"
            thisWord.transform = "Error"
            thisWord.example = "Error"
        }
        return thisWord
    }

    private fun createDatabase(wordList :Wordlist){
        val wordlist = wordList.wordlistName+".xml"
        val n = DocumentBuilderFactory.newInstance().newDocumentBuilder().
                parse( FileInputStream(getExternalFilesDir("wordlist").path+ "/" + wordlist)).getElementsByTagName("items").length
        //this function get words from xml and turn save to database
        val my = DaoOpt.getInstance()
        // my.deleteAllData(this)
        my.deleteAllData(this)
        // use zeroWord.appearTime-10000 show the current number n
        val zeroWord = Word(-1, "zero", "zero", "zero", "zero", 0, -1, -1, -1.0, -1, -1, true)
        my.insertData(this,zeroWord)

        for (i in 0..n-1){
            val insertWord = getWordFromXml(wordList,i)
            my.insertData(this,insertWord)
        }
        Toast.makeText(this,"Create Database Successfully "+ n , Toast.LENGTH_SHORT).show()
    }




}
