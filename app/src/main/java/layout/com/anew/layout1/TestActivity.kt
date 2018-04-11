package layout.com.anew.layout1

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.content_test.*
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory


// next step : build the vocabulary dadabase
//          and  design the recite word algorithm
// based on these codes and ReciteWord Activity
// get xml from server
class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recite_word_new)
        createDatabase()

    }


    fun getInfo(){
        // this function shows the word info in database
        val my = DaoOpt.getInstance()
        val n1 = editText2.text.toString()
        val n = n1.toInt()
        val getId = my.queryAll(this)?.get(n)?.id
        val getTestWord = my.queryAll(this)?.get(n)?.word
        val getTestWordTran = my.queryAll(this)?.get(n)?.transform
        val getTestTrans = my.queryAll(this)?.get(n)?.translation
        val getExample = my.queryAll(this)?.get(n)?.example
     //   textView3.setText(getId.toString()+" "+getTestWord.toString()+" "+getTestTrans+" "+getTestWordTran.toString()+" "+getExample.toString())

        val testWord = my.queryForAppearTime(this,1)?.get(n)?.word
       // textView3.setText(testWord)
    }


    private fun getWordFromXml(num:Int):WordForDB{
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()

     //   val conn = URL("192.168.1.171/wordbook.xml").openConnection() as HttpURLConnection
      //  conn.connectTimeout=5000
       // conn.requestMethod="GET"
        //val inputStream = conn.inputStream
        //val doc = db.parse(inputStream)

        val doc = db.parse(assets.open("test1.xml"))
        val wordList = doc.getElementsByTagName("items")
        val thisWord = WordForDB()
        thisWord.id = num.toLong()
        thisWord.appearTime=0
        thisWord.correctTime=0
        thisWord.incorrectTime=0
        thisWord.nextAppearTime=0
        //thisWord.eFactor=2

        if(num<wordList.length) {
            val elem = wordList.item(num)
            thisWord.word = elem.childNodes.item(1).textContent
            thisWord.transform = elem.childNodes.item(3).textContent
            thisWord.translation = elem.childNodes.item(5).textContent
            thisWord.example = elem.childNodes.item(7).textContent
        }
        else{
            thisWord.word = "None"
            thisWord.translation = "none"
            thisWord.transform = "None"
            thisWord.example = "None"
        }
        return thisWord
    }
    private fun createDatabase(){
        //this function get words from xml and turn save to database
        val my = DaoOpt.getInstance()
        my.deleteAllData(this)
        for (i in 0..30){
            val insertWord = getWordFromXml(i)
            my.insertData(this,insertWord)
        }
        Toast.makeText(this,"Create Database Successfully",Toast.LENGTH_SHORT).show()
    }




    // the following part is for algorithm sm-2(fixed version)
    // experimental build
    // update correct time before call newEFactorh
    private fun newEFactor(wordForDB: WordForDB):Double{
        if (wordForDB.correctTime==0) {
            if (wordForDB.incorrectTime==0)return 2.0
            if (wordForDB.incorrectTime==1)return 1.46
            if (wordForDB.incorrectTime==2)return 0.92
            if (wordForDB.incorrectTime==3)return 0.0
        }
        else if(wordForDB.correctTime==1) return 2.1
        else if(wordForDB.correctTime==2)return 2.2

        return 0.0 // add for no error ; re-consider
    }

    // call newInterval after  updating to newEFactor
    private fun newInterval(wordForDB: WordForDB,feedInfo:Int):Int{
        if (wordForDB.appearTime==0){
            when(feedInfo){
                1 -> return 6
                -1 -> return 3
            }
        }else{
            val newInternal = (wordForDB.interval * wordForDB.eFactor).toInt()
            return  newInternal
        }

        // TODO add range limit

        return -1 //add for no error re-consider
    }

    // the create new recite page function should add the current word number to the word's NextAppearTime
    // call updateWordDate function after the user give a feedback of the word (right,wrong,remember-it,don't know it)
    //------------------------------------------------------------------
    //| !!!!!!!  so feedInfo should have more than 2 conditions !!!!!!!|
    //------------------------------------------------------------------
    // TODO fix feedInfo
    private fun updateWordData(wordForDB: WordForDB,feedInfo: Int){


        if (feedInfo==1){
            wordForDB.correctTime+=1
            wordForDB.eFactor=newEFactor(wordForDB)
            wordForDB.interval=newInterval(wordForDB,1)
        }else if (feedInfo==-1) {
            wordForDB.correctTime = 0
            wordForDB.incorrectTime += 1
            wordForDB.eFactor = newEFactor(wordForDB)
            wordForDB.interval = newInterval(wordForDB, -1)
        }

        wordForDB.nextAppearTime+=wordForDB.interval
        wordForDB.appearTime+=1

        DaoOpt.getInstance().saveData(this,wordForDB)    //save to database
    }

    // get new word function
    // call this function to get a word and 4 translations
    //-----------------------------------------------------
    // |!!!Or maybe i can directly createNewRecitePage!!!|
    //-----------------------------------------------------

    // n could be a global variable to record the number of the words(that showed repeat record)
    private fun createNew(n:Int){
        // firstly search database for NextAppearTime property if exist one take that one
        // if no matches, take a new one in random(from those NextAppearTime ==0
        // after take a word firstly (above line's condition) update its NextAppearTime = n
        return
    }
}
