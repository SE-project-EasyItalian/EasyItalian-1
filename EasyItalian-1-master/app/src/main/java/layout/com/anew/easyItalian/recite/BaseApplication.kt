package layout.com.anew.easyItalian.recite

import android.app.Application
import android.widget.Toast
import com.avos.avoscloud.AVOSCloud
import layout.com.anew.easyItalian.WordSearch.DaoOptForSearch
import layout.com.anew.easyItalian.WordSearch.WordSearch
import layout.com.anew.easyItalian.gen.DaoSession
import layout.com.anew.easyItalian.gen.DaoMaster
import javax.xml.parsers.DocumentBuilderFactory


class BaseApplication : Application(){

    private var daoSession: DaoSession? = null
    private val XMLNAME="word_hole.xml"

    override fun onCreate() {
        super.onCreate()
        //配置数据库
        setupDatabase()
        GreenDaoManager.getInstance(this)

        // initialize leancloud
        AVOSCloud.initialize(this, "SbkfRG1bPvdg2EWKGKa3igM5-gzGzoHsz", "iC5bfhEPRSufXzogvr4pynno")

        //initialize search database from xml file
     //   prepareForSearh()

    }

    /**
     * 配置数据库
     */
    private fun setupDatabase() {
        //创建数据库
        val helper = DaoMaster.DevOpenHelper(this, "wordList.db", null)
        //获取可写数据库
        val db = helper.writableDatabase
        //获取数据库对象
        val daoMaster = DaoMaster(db)
        //获取Dao对象管理者
        daoSession = daoMaster.newSession()
    }

    fun getDaoInstant(): DaoSession? {
        return daoSession
    }


    private fun getWordFromXmlq(num:Int): WordSearch  {
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        //TODO get xml from server
        //   val conn = URL("192.168.1.171/wordbook.xml").openConnection() as HttpURLConnection
        //  conn.connectTimeout=5000
        // conn.requestMethod="GET"
        //val inputStream = conn.inputStream
        //val doc = db.parse(inputStream)

        val doc = db.parse(assets.open(XMLNAME))
        val wordList = doc.getElementsByTagName("items")
        val thisWord = WordSearch()
        thisWord.id = num.toLong()
        //thisWord.appearTime=0
        //thisWord.correctTime=0
        //thisWord.incorrectTime=0
        //thisWord.nextAppearTime=0
        //thisWord.eFactor=2.0
        //thisWord.grasp = false

        if(num<wordList.length) {
            val elem = wordList.item(num)
            thisWord.word = elem.childNodes.item(1).textContent
            thisWord.wordDetail = elem.childNodes.item(3).textContent
            //thisWord.transform = elem.childNodes.item(3).textContent
            //thisWord.translation = elem.childNodes.item(5).textContent
            //thisWord.example = elem.childNodes.item(7).textContent
        }
        else{
            thisWord.word = "None"
            thisWord.wordDetail = "none"
            //thisWord.translation = "none"
            //thisWord.transform = "None"
            //thisWord.example = "None"
        }
        return thisWord
    }

    private fun getWordDetailZero(word:String): WordSearch {
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        //TODO get xml from server
        //   val conn = URL("192.168.1.171/wordbook.xml").openConnection() as HttpURLConnection
        //  conn.connectTimeout=5000
        // conn.requestMethod="GET"
        //val inputStream = conn.inputStream
        //val doc = db.parse(inputStream)

        val doc = db.parse(assets.open(XMLNAME))
        val wordList = doc.getElementsByTagName("items")

        val thisWord = WordSearch()

        //thisWord.appearTime=0
        //thisWord.correctTime=0
        //thisWord.incorrectTime=0
        //thisWord.nextAppearTime=0
        //thisWord.eFactor=2.0
        //thisWord.grasp = false
        for(num in 0..wordList.length) {

                val elem = wordList.item(num)
            if (elem.childNodes.item(1).textContent==word){
                thisWord.word = elem.childNodes.item(1).textContent
                thisWord.wordDetail = elem.childNodes.item(3).textContent
                break
                //thisWord.transform = elem.childNodes.item(3).textContent
                //thisWord.translation = elem.childNodes.item(5).textContent
                //thisWord.example = elem.childNodes.item(7).textContent
            }
        }
        return thisWord
    }

    private fun createDatabaseForSearch(n : Int){
        //this function get words from xml and turn save to database
        val mySearch = DaoOptForSearch.getInstance()
        // my.deleteAllData(this)

        // use zeroWord.appearTime-10000 show the current number n
        val zeroWord = WordSearch(-1, "zero", "zero")
        mySearch.insertData(this,zeroWord)





        val n = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(assets.open(XMLNAME)).getElementsByTagName("items").length - 2
        //val insertWord = getWordFromXml(2)
        //mySearch.insertData(this,insertWord)
        //Toast.makeText(this,"Create Database For Search Successfully   "+ insertWord.wordDetail , Toast.LENGTH_LONG).show()

      //   for (i in 0..10){
           //  val insertWord = getWordFromXml(i)
           ///  mySearch.insertData(this,insertWord)
             //Toast.makeText(this,"insert "+insertWord.word+"  "+insertWord.wordDetail , Toast.LENGTH_LONG).show()
      //   }
         Toast.makeText(this,"Create Database For Search Successfully "+ n , Toast.LENGTH_SHORT).show()

    }

    private fun prepareForSearh(){
        val mySearch = DaoOptForSearch.getInstance()


        if (mySearch.queryAll(this)?.size==0) {

            // n is the total number of words
            val n = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(assets.open(XMLNAME)).getElementsByTagName("items").length
            Toast.makeText(this,"Create Database22 Successfully "+ n ,Toast.LENGTH_SHORT).show()

            createDatabaseForSearch(n)
        }





    }




}