package layout.com.anew.easyItalian.recite

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_recite_word.*
import layout.com.anew.easyItalian.MainActivity
import layout.com.anew.easyItalian.R
import java.lang.Math.ceil

import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
//import kotlin.math.ceil


// stage now : build the vocabulary database
//          and improve the recite word algorithm

// TODO separate the readFromXml function to personal info page
class ReciteWordAcitivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recite_word)

     //   createDatabase() this should be put in personalInfo page
        val my = DaoOpt.getInstance()
        if (my.queryAll(this)?.size==0) {

            // n is the total number of words
            val n = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(assets.open("for_test.xml")).getElementsByTagName("items").length
            createDatabase(n)
        }

        createNew()

        backForRecite.setOnClickListener {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }



    //  getWordFromXml and createDatabase should be in wordlist Page
    //  exactly get the total items from xml to database
    //  database should conclude all items from xml and a zero word for global num and an error word for showing error
    private fun getWordFromXml(num:Int): Word {
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        //TODO get xml from server
         //   val conn = URL("192.168.1.171/wordbook.xml").openConnection() as HttpURLConnection
         //  conn.connectTimeout=5000
         // conn.requestMethod="GET"
        //val inputStream = conn.inputStream
        //val doc = db.parse(inputStream)

        val doc = db.parse(assets.open("for_test.xml"))
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
            thisWord.word = "None"
            thisWord.translation = "none"
            thisWord.transform = "None"
            thisWord.example = "None"
        }
        return thisWord
    }
    private fun createDatabase(n : Int){
        //this function get words from xml and turn save to database
        val my = DaoOpt.getInstance()
       // my.deleteAllData(this)

        // use zeroWord.appearTime-10000 show the current number n
        val zeroWord = Word(-1, "zero", "zero", "zero", "zero", 0, -1, -1, -1.0, -1, -1, true)
        my.insertData(this,zeroWord)


     //   val n = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(assets.open("testWords.xml")).getElementsByTagName("items").length - 2

        for (i in 0..n-2){
            val insertWord = getWordFromXml(i)
            my.insertData(this,insertWord)
        }
        Toast.makeText(this,"Create Database Successfully "+ n ,Toast.LENGTH_SHORT).show()
    }




    // the following part is for algorithm sm-2(fixed version)
    // experimental build
    // update correct time before call newEFactorh
    private fun newEFactor(word: Word):Double{
        if (word.correctTime==0) {
            if (word.incorrectTime==0)return 2.0
            if (word.incorrectTime==1)return 1.46
            if (word.incorrectTime==2)return 0.92
            if (word.incorrectTime==3)return 0.0
        }
        else if(word.correctTime==1) return 2.1
        else if(word.correctTime==2)return 2.2

        return 0.0 // add for no error ; re-consider
    }

    // call newInterval after  updating to newEFactor
    private fun newInterval(word: Word, feedInfo:Int):Int{
        var newInterval = 6
        if (word.appearTime==0){
            when(feedInfo){
                2 -> newInterval = 12  // 2 to pass it
                1 -> newInterval= 6   // 1 to correct choice
                0 -> newInterval= 3   // 0 to tell me
                -1 -> newInterval= 3  // -1 to incorrect choice
            }
        }else{
            newInterval = (word.interval * word.eFactor).toInt()
        }

        // add range limit
        if(newInterval>32) newInterval = 32
        if(newInterval==0) newInterval = 12
        if(newInterval<=3) newInterval = 3

        return newInterval //add for no error re-consider
    }

    // the create new recite page function should add the current word number to the word's NextAppearTime
    // call updateWordDate function after the user give a feedback of the word (right,wrong,remember-it,don't know it)
    private fun updateWordData(word: Word, feedInfo: Int){
        // 2 to rememberIt // 1 to correct answer
        // 0 to don't know // -1 for incorrect answer
        if(feedInfo==2){
            word.correctTime+=2
            word.eFactor=newEFactor(word)
            word.interval=newInterval(word,2)
        }else if (feedInfo==1){
            word.correctTime+=1
            word.eFactor=newEFactor(word)
            word.interval=newInterval(word,1)
        }else if(feedInfo==0){
            // unchanged ??!!??
            // maybe have to add feedInfo to newEFactor function
            word.eFactor=newEFactor(word)
            word.interval=newInterval(word,1)
        } else if (feedInfo==-1) {
            word.correctTime = 0
            word.incorrectTime += 1
            word.eFactor = newEFactor(word)
            word.interval = newInterval(word, -1)
        }

       // word.nextAppearTime+=word.interval
        val my = DaoOpt.getInstance()
        if (word.correctTime<3) {
            var nextTime = (word.nextAppearTime + word.interval).toLong()
            while (my.queryForNextAppearTime(this, nextTime)?.size != 0) {
                nextTime += 1
            }
            word.nextAppearTime = nextTime.toInt()
        }
        // update appearTime
        word.appearTime+=1
        if (word.correctTime>=3){
            word.grasp=true
            word.interval=-1
            word.nextAppearTime= -1
        }

        DaoOpt.getInstance().saveData(this,word)    //save to database
    }



    // create new recite page function
    // call this onClick correct answer or "continue" button in word Details page

    // n could be a global variable to record the number of the words(that showed repeat record)
        private fun createNew(){

        // setContentView(R.layout.activity_recite_word_new)
        // firstly search database for NextAppearTime property if exist one take that one
        // if no matches, take a new one in random(from those NextAppearTime ==0
        // after take a word firstly (above line's condition) update its NextAppearTime = n
        val my = DaoOpt.getInstance()
        val zeroWord = my.queryForId(this,-1)?.get(0)
        // zeroWord records the num (current number of all word shown)
        // -10000 is too large maybe
        val num = zeroWord?.appearTime!!.toLong()

        // get thisWord to show
        val thisWordId : Long
        if (my.queryForNextAppearTime(this,num)?.size != 0 ){
            //Toast.makeText(this,"not null",Toast.LENGTH_SHORT).show()
            thisWordId = my.queryForNextAppearTime(this,num)?.get(0)?.id  ?:0
        }else{

            // TODO re-consider the choice of randNum
            // all the randNum should be re-consider it
            // ATTENTION!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // not just appearTime == 0 but also ==1 ==2
            //   val rand = Random()
            //  val randNum = rand.nextInt(6)
            //  thisWordId =  my.queryForAppearTime(this,0)?.get(randNum)?.id ?:0
            // var i : Long = 0
            // while (my.queryForAppearTime(this,i)?.size==0) i+=1
            //problem
            if (my.queryForAppearTime(this,0)?.size!=0) {
                val randLimit = my.queryForAppearTime(this, 0)?.size ?:3 -1
                val rand = Random()
                val randNum = rand.nextInt(ceil(randLimit / 2.0).toInt())
                thisWordId = my.queryForAppearTime(this, 0)?.get(randNum)?.id ?:0
            }else{
              //  while(my.queryForNextAppearTime(this,num)?.size == 0) num+=1
                if (my.queryForGrasp(this,false)?.size!=0) {
                    thisWordId=my.queryForGrasp(this,false)?.get(0)?.id ?:0
                }
                else {
                    thisWordId=0
                    val intent =Intent(this, FinishedPage::class.java)
                    startActivity(intent)
                    Toast.makeText(this,"完成学习！",Toast.LENGTH_SHORT).show()
                }
            }
        }

        val errorWord = Word(-10086, "Error", "Error", "Error", "Error", -1, -1, -1, -1.0, -1, -1, true)
        val thisWord = my.queryForId(this,thisWordId)?.get(0) ?:errorWord

        // get word 2
        val wordId2 : Long
        val rand = Random()
        var randNum :Int
        if(my.queryForNotZeroIncorrectTimes(this,thisWordId)?.size!=0)
        {
            randNum = rand.nextInt(my.queryForNotZeroIncorrectTimes(this,thisWordId)?.size ?:1 -1)
           // wordId2=1L
            //Toast.makeText(this,"!!!!",Toast.LENGTH_SHORT).show()
            wordId2 = my.queryForNotZeroIncorrectTimes(this,thisWordId)?.get(randNum)?.id ?:1
        }else{
            randNum = rand.nextInt(my.getNumberOfItems(this)-5)
            wordId2 = my.queryForIdNotEqual(this, mutableListOf(thisWordId,-1))?.get(randNum)?.id ?:1
        }

        //word 3
        randNum = rand.nextInt(my.getNumberOfItems(this)-5)
        val wordId3 = my.queryForIdNotEqual(this, mutableListOf(thisWordId,wordId2,-1))?.get(randNum)?.id ?:1

        //word 4
        randNum = rand.nextInt(my.getNumberOfItems(this)-5)
        val wordId4 = my.queryForIdNotEqual(this, mutableListOf(thisWordId,wordId2,wordId3,-1))?.get(randNum)?.id ?:1

        //create page

        // set word on show and record the current num to database
        word.setText(thisWord.word)
        thisWord.nextAppearTime = num.toInt()

        // set translation buttons onClickListener
        val buttonMap = mapOf<Int, Button>(0 to buttonA , 1 to buttonB,2 to buttonC ,3 to buttonD)
        randNum = rand.nextInt(4)
        buttonMap[randNum]?.setText(thisWord.translation)
        buttonMap[randNum]?.setOnClickListener{
          //  Toast.makeText(this,"答对了",Toast.LENGTH_SHORT).show()
            updateWordData(thisWord,1)
            zeroWord.appearTime+=1
            my.saveData(this,zeroWord)
            createNew()
        }
        val otherWords = mutableListOf(wordId2,wordId3,wordId4)
        for(i in 0..3){
            if(i!=randNum){
                buttonMap[i]?.setText(my.queryForId(this,otherWords[0])?.get(0)?.translation)
                buttonMap[i]?.setOnClickListener({
                 //   Toast.makeText(this, "答错了！Call showDetails and create a new page", Toast.LENGTH_LONG).show()
                    updateWordData(thisWord,-1)
                    zeroWord.appearTime+=1
                    my.saveData(this,zeroWord)
                    //show details
                    showDetails(thisWord)
                })
                otherWords.removeAt(0)
            }
        }

        //set rememberIt button
        remembereIt.setOnClickListener{
       //     Toast.makeText(this, "已记住，correctTime += 2", Toast.LENGTH_SHORT).show()
            updateWordData(thisWord,2)
            zeroWord.appearTime+=1
            my.saveData(this,zeroWord)
            createNew()
        }
        //set showDetails button
        showDetails.setOnClickListener {
          //  Toast.makeText(this, "不认识，展示详情", Toast.LENGTH_SHORT).show()
            updateWordData(thisWord,0)
            zeroWord.appearTime+=1
            my.saveData(this,zeroWord)
            // call showDetails
            showDetails(thisWord)
        }
        //call tts
        callTTS()

        return
    }

    fun showDetails(word : Word){
        val data = arrayListOf(word.word,word.transform,word.translation,word.example)
        val showDetailsActivity = Intent()
        showDetailsActivity.setClass(this, WordDetailsActivity::class.java)
        // pass the word info to WordDetailsActivity
        showDetailsActivity.putStringArrayListExtra("data",data)
        startActivity(showDetailsActivity)


    }



    //tts start
    private fun buildSpeechUrl(words: String): String {

        //tts from VoiceRss
        //val kVoiceRssServer = "http://api.voicerss.org"
        //val kVoiceRSSAppKey = "ec58f36cbbe846edb12b9fcc6e217d9f"
        //var url = ""
        //url = kVoiceRssServer + "/?key=" + kVoiceRSSAppKey + "&t=text&hl=" + "it-it" + "&src=" + words

        //tts from MARY TTS
        //http://mary.dfki.de:59125/process?INPUT_TEXT=pomodoro&INPUT_TYPE=TEXT&OUTPUT_TYPE=AUDIO&AUDIO=WAVE_FILE&LOCALE=it
        var url="http://mary.dfki.de:59125/process?INPUT_TEXT="
        val endIt="&INPUT_TYPE=TEXT&OUTPUT_TYPE=AUDIO&AUDIO=WAVE_FILE&LOCALE=it"
        url = url+words+endIt
        url = url.replace(' ','+')
        return url
    }


    fun callTTS(){
        val txtSentence = findViewById<TextView>(R.id.word)
        val text = txtSentence.text.toString()
        val url = buildSpeechUrl(text)
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(url)
        mediaPlayer.setOnErrorListener{_, _, _ -> false}
        mediaPlayer.setOnCompletionListener { mediaPlayer.release() }
        mediaPlayer.setOnPreparedListener {  mediaPlayer.start()}
        mediaPlayer.prepareAsync()
    }
    //tts end
}
