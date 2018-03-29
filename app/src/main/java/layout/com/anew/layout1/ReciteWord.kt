package layout.com.anew.layout1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_recite_word.*
import java.util.Random
import javax.xml.parsers.DocumentBuilderFactory


class ReciteWord : Activity() {

    var wordForRecite = " "
    var wordTrans = " "  //word translation . use setTrans to change it
    var otherWordTrans = mutableListOf<String>()
    var  n =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recite_word)
        //show the n-th word
        createNew(n)

        // functional button listener
        remembereIt.setOnClickListener {
            createNew(++n)
            //Toast.makeText(this,"call creat new function",Toast.LENGTH_SHORT).show()
        }

        showDetails.setOnClickListener {
            val intent = Intent(this, WordDetailsActivity::class.java)
            startActivity(intent)
            /*
            * should call a function like this:
            * showDetails(word:String){
            *  show the page of that exact word
            * }
            * */
        }

        backForRecite.setOnClickListener {
            //return to main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            //return to main rather than recreate a main activity
            //waiting for coding
        }

    }

    // to create the page of word recite
    fun createRecite(){

        word.setText(wordForRecite) // set the current word for recite
        //use map&random to set the correct button

        val buttonMap = mapOf<Int,Button>(0 to buttonA , 1 to buttonB,2 to buttonC ,3 to buttonD)
        val rand = Random()
        val randNum = rand.nextInt(4)
        buttonMap[randNum]?.setText(wordTrans) //set right choice

        // set for other buttons( incorrect button)
        for(i in 0..3){
            if(i!=randNum){
                buttonMap[i]?.setText(otherWordTrans[0]) //set choices
                otherWordTrans.removeAt(0)
            }
        }

        // set button listener for telling if the user gets the right answer

        buttonMap[randNum]?.setOnClickListener(){
            Toast.makeText(this, "答对了！", Toast.LENGTH_LONG).show()
            // this should  turn to a new word
            createNew(++n)  // the input maybe a database
            //Toast.makeText(this,wordForRecite+wordTrans+otherWordTrans,Toast.LENGTH_SHORT).show()
            //Toast for test
        }
        for(i in 0..3){
            if(i!=randNum){
                buttonMap[i]?.setOnClickListener({
                    Toast.makeText(this, "答错了！", Toast.LENGTH_LONG).show()
                    // this should  turn to a detail pages
                    //same as above showDetails() function
                })
            }
        }
    }

    // setter function
    fun setWord(word:String){
        wordForRecite=word
    }
    fun setTrans(trans:String){
        wordTrans=trans
    }
    fun setOtherWordTranslation(otherTrans : MutableList<String>){
         for (i in 0..2)
             otherWordTrans.add(otherTrans[i])
     }

    //setter new function
    //current just for test
    fun createNew(num:Int){
       // val newWord = getWordFromXml(num)
       // setWord(newWord.word)
       // setTrans(newWord.trans)
       // setOtherWordTranslation(mutableListOf("混淆1","混淆2","混淆3"))
       // createRecite()

        val newWords = getWordsFromXml(num)
        setWord(newWords[0].word)
        setTrans(newWords[0].trans)
        val otherWordsTrans =  mutableListOf(newWords[1].trans,newWords[2].trans,newWords[3].trans)
        setOtherWordTranslation(otherWordsTrans)
       // setOtherWordTranslation(mutableListOf("混淆1","混淆2","混淆3"))
        createRecite()
    }

    fun getWordFromXml( num:Int):Word{
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        val doc = db.parse(assets.open("wordbook.xml"))
        val wordList = doc.getElementsByTagName("items")
        val newWord = Word()
        if(num<wordList.length) {
            val elem = wordList.item(num)
            newWord.word = elem.childNodes.item(1).textContent
            newWord.pos = elem.childNodes.item(3).textContent
            newWord.tran = elem.childNodes.item(5).textContent
            newWord.trans = elem.childNodes.item(7).textContent
            newWord.example = elem.childNodes.item(9).textContent
        }
        return newWord
    }

    fun getWordsFromXml( num:Int):List<Word>{
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        val doc = db.parse(assets.open("wordbook.xml"))
        val wordList = doc.getElementsByTagName("items")
        val newWords = mutableListOf<Word>()
        if(num<wordList.length) {
            val elem = wordList.item(num)
            val thisWord = Word()
            thisWord.word = elem.childNodes.item(1).textContent
            thisWord.pos = elem.childNodes.item(3).textContent
            thisWord.tran = elem.childNodes.item(5).textContent
            thisWord.trans = elem.childNodes.item(7).textContent
            thisWord.example = elem.childNodes.item(9).textContent
            newWords.add(thisWord)
        }
        else{
            val thisWord = Word()
            thisWord.word = "None"
            thisWord.pos = "None"
            thisWord.tran = "None"
            thisWord.trans = "None"
            thisWord.example = "None"
            newWords.add(thisWord)
        }

        val rand = Random()
        val randomSet = mutableSetOf<Int>(num)
        while(randomSet.size<4){
            randomSet.add(rand.nextInt(wordList.length))
        }
     //   Toast.makeText(this,randomSet.toString(),Toast.LENGTH_SHORT).show()

        for (i in 0..3){
            if(randomSet.toList()[i]!=num)
            {
                val elem = wordList.item(randomSet.toList()[i])
                val thisWord = Word()
                thisWord.word = elem.childNodes.item(1).textContent
                thisWord.pos = elem.childNodes.item(3).textContent
                thisWord.tran = elem.childNodes.item(5).textContent
                thisWord.trans = elem.childNodes.item(7).textContent
                thisWord.example = elem.childNodes.item(9).textContent
                newWords.add(thisWord)
                if(newWords.size==4) break
            }

        }


        return newWords
    }
    /*
    * fun createChoices(){
    *  // use this function to create 3 other words'translation
    *  // consider the algorithm
    * }
    *
    * */

}
