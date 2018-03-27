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
        val newWord = getWordFromXml(num)
        setWord(newWord.word)
        setTrans(newWord.trans)
        setOtherWordTranslation(mutableListOf("混淆1","混淆2","混淆3"))
        createRecite()
    }

    fun getWordFromXml(num:Int):Word{
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        val doc = db.parse(assets.open("wordbook.xml"))
        val wordList = doc.getElementsByTagName("items")
        val newWrod = Word()
        if(num<wordList.length) {
            val elem = wordList.item(num)
            newWrod.word = elem.childNodes.item(1).textContent
            newWrod.pos = elem.childNodes.item(3).textContent
            newWrod.tran = elem.childNodes.item(5).textContent
            newWrod.trans = elem.childNodes.item(7).textContent
            newWrod.example = elem.childNodes.item(9).textContent
        }
        return newWrod
    }


}
