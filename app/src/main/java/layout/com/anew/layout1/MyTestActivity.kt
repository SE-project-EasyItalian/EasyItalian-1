package layout.com.anew.layout1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_recite_word.*
import kotlinx.android.synthetic.main.content_test.*
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory


// stage now : build the vocabulary dadabase
//          and  design the recite word algorithm
// based on these codes and ReciteWord Activity
// get xml from server (later)
class MyTestActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recite_word_new)
        createDatabase()
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
    private fun getWordFromXml(num:Int):WordForDB{
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()

     //   val conn = URL("192.168.1.171/wordbook.xml").openConnection() as HttpURLConnection
      //  conn.connectTimeout=5000
       // conn.requestMethod="GET"
        //val inputStream = conn.inputStream
        //val doc = db.parse(inputStream)

        val doc = db.parse(assets.open("for_test.xml"))
        val wordList = doc.getElementsByTagName("items")
        val thisWord = WordForDB()
        thisWord.id = num.toLong()
        thisWord.appearTime=0
        thisWord.correctTime=0
        thisWord.incorrectTime=0
        thisWord.nextAppearTime=0
        thisWord.eFactor=2.0

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

        // use zeroWord.appearTime-10000 show the current number n
        val zeroWord = WordForDB(-1,"zero","zero","zero","zero",10000,-1,-1,-1.0,-1,-1)
        my.insertData(this,zeroWord)


        // 30 should be the length of xml factors
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
        var newInterval = 6
        if (wordForDB.appearTime==0){
            when(feedInfo){
                2 -> newInterval = 12  // 2 to pass it
                1 -> newInterval= 6   // 1 to correct choice
                0 -> newInterval= 3   // 0 to tell me
                -1 -> newInterval= 3  // -1 to incorrect choice
            }
        }else{
            newInterval = (wordForDB.interval * wordForDB.eFactor).toInt()
        }

        // add range limit
        if(newInterval>32) newInterval = 32
        if(newInterval==0) newInterval = 12
        if(newInterval<=3) newInterval = 3

        return newInterval //add for no error re-consider
    }

    // the create new recite page function should add the current word number to the word's NextAppearTime
    // call updateWordDate function after the user give a feedback of the word (right,wrong,remember-it,don't know it)
    //------------------------------------------------------------------
    //| !!!!!!!  so feedInfo should have more than 2 conditions !!!!!!!|
    //------------------------------------------------------------------
    // TODO fix feedInfo
    private fun updateWordData(wordForDB: WordForDB,feedInfo: Int){
        if(feedInfo==2){
            wordForDB.correctTime+=2
            wordForDB.eFactor=newEFactor(wordForDB)
            wordForDB.interval=newInterval(wordForDB,2)
        }else if (feedInfo==1){
            wordForDB.correctTime+=1
            wordForDB.eFactor=newEFactor(wordForDB)
            wordForDB.interval=newInterval(wordForDB,1)
        }else if(feedInfo==0){
            // unchanged ??!!??
            // maybe have to add feedInfo to newEFactor function
            wordForDB.eFactor=newEFactor(wordForDB)
            wordForDB.interval=newInterval(wordForDB,1)
        } else if (feedInfo==-1) {
            wordForDB.correctTime = 0
            wordForDB.incorrectTime += 1
            wordForDB.eFactor = newEFactor(wordForDB)
            wordForDB.interval = newInterval(wordForDB, -1)
        }



        // try to resolve the word loss problem
       // wordForDB.nextAppearTime+=wordForDB.interval
        val my = DaoOpt.getInstance()
        if (wordForDB.correctTime<3) {
            var nextTime = (wordForDB.nextAppearTime + wordForDB.interval).toLong()
            while (my.queryForNextAppearTime(this, nextTime)?.size != 0) {
                nextTime += 1
            }
            wordForDB.nextAppearTime = nextTime.toInt()
        }

        // update appearTime
        wordForDB.appearTime+=1

        DaoOpt.getInstance().saveData(this,wordForDB)    //save to database
    }



    // create new recite page function
    // call this onClick correct answer or "continue" button in word Details page

    // n could be a global variable to record the number of the words(that showed repeat record)
        private fun createNew(){

        setContentView(R.layout.activity_recite_word_new)
        // firstly search database for NextAppearTime property if exist one take that one
        // if no matches, take a new one in random(from those NextAppearTime ==0
        // after take a word firstly (above line's condition) update its NextAppearTime = n
        val my = DaoOpt.getInstance()
        val zeroWord = my.queryForId(this,-1)?.get(0)
        // zeroWord records the num (current number of all word shown)
        // -10000 is too large maybe
        val num = zeroWord?.appearTime!!.toLong()-10000

        // get thisWord to show
        val thisWordId : Long
        if (my.queryForNextAppearTime(this,num)?.size != 0){
            Toast.makeText(this,"not null",Toast.LENGTH_SHORT).show()
            thisWordId = my.queryForNextAppearTime(this,num)?.get(0)?.id  ?:0
        }else{
            val rand = Random()
            // TODO add a limit to the randNum because it may be out of bounds
            // all the randNum should be re-consider it

            // ATTENTION!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // not just appearTime == 0 but also ==1 ==2
            val randNum = rand.nextInt(6)
            thisWordId =  my.queryForAppearTime(this,0)?.get(randNum)?.id ?:0
        }

        val errorWord = WordForDB(-10086,"Error","Error","Error","Error",-1,-1,-1,-1.0,-1,-1)
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
            Toast.makeText(this,"答对了",Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this, "答错了！Call showDetails and create a new page", Toast.LENGTH_LONG).show()
                    updateWordData(thisWord,-1)
                    zeroWord.appearTime+=1
                    my.saveData(this,zeroWord)
                    //TODO
                    createNew()// this should call wordDetails function
                })
                otherWords.removeAt(0)
            }
        }

        //set rememberIt button
        remembereIt.setOnClickListener{
            Toast.makeText(this, "已记住，correctTime += 2", Toast.LENGTH_SHORT).show()
            updateWordData(thisWord,2)
            zeroWord.appearTime+=1
            my.saveData(this,zeroWord)
            createNew()
        }
        //set showDetails button
        showDetails.setOnClickListener {
            Toast.makeText(this, "不认识，展示详情", Toast.LENGTH_SHORT).show()
            updateWordData(thisWord,0)
            zeroWord.appearTime+=1
            my.saveData(this,zeroWord)
            //TODO
            createNew()   // this should call showDetails
        }

        return
    }

}
