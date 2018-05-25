package layout.com.anew.easyItalian

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import layout.com.anew.easyItalian.recite.DaoOpt
import layout.com.anew.easyItalian.recite.Word
import java.io.FileInputStream
import javax.xml.parsers.DocumentBuilderFactory
import android.os.StrictMode
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL



// TODO process expectation
class SetWordList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_word_list)


        // TODO use leancloud show all available wordLists in a listView and set onClick listener to set wordList
        downloadWordList("for_test.xml")
        createDatabase("for_test.xml")

    }

    private fun downloadWordList(wordlist: String){
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        //open xml file

        // TODO classify files on my server
        val path = "http://avisy.ddns.net:3322/"+wordlist
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

    private fun getWordFromXml(wordlist:String,num:Int): Word {
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

    private fun createDatabase(wordlist:String){

        val n = DocumentBuilderFactory.newInstance().newDocumentBuilder().
                parse(assets.open(wordlist)).getElementsByTagName("items").length
        //this function get words from xml and turn save to database
        val my = DaoOpt.getInstance()
        // my.deleteAllData(this)
       my.deleteAllData(this)
        // use zeroWord.appearTime-10000 show the current number n
        val zeroWord = Word(-1, "zero", "zero", "zero", "zero", 0, -1, -1, -1.0, -1, -1, true)
        my.insertData(this,zeroWord)

        for (i in 0..n-1){
            val insertWord = getWordFromXml(wordlist,i)
            my.insertData(this,insertWord)
        }
        Toast.makeText(this,"Create Database Successfully "+ n , Toast.LENGTH_SHORT).show()
    }




}
