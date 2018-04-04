package layout.com.anew.layout1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_recite_word.*
import java.util.Random
import javax.xml.parsers.DocumentBuilderFactory


class ReciteWord : Activity() {


    var wordForRecite = " "
    var wordTrans = " "  //word translation . use setTrans to change it
    var otherWordTrans = mutableListOf<String>()
    var  n =0

    //tts

    //end
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recite_word_new)
        //show the n-th word
        createNew(n)

        // functional button listener
        remembereIt.setOnClickListener {
            createNew(++n)
        }

        showDetails.setOnClickListener {
            showDetails( getWordsFromXml(n)[0])
        }

        backForRecite.setOnClickListener {
            //return to main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            //return to main rather than recreate a main activity
            //waiting for coding TODO
        }

        voice.setOnClickListener(){
            val thisView = findViewById<View>(R.id.word)
            btnSpeakNowOnClick(thisView)
        }

    }
    //end

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
           // Toast.makeText(this, "答对了！", Toast.LENGTH_LONG).show()
            // this should  turn to a new word
            createNew(++n)  // the input maybe a database

        }
        for(i in 0..3){
            if(i!=randNum){
                buttonMap[i]?.setOnClickListener({
                   // Toast.makeText(this, "答错了！", Toast.LENGTH_LONG).show()
                    // this should  turn to a detail pages
                    showDetails( getWordsFromXml(n)[0])
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
    fun createNew(num:Int){
        val newWords = getWordsFromXml(num)
        setWord(newWords[0].word)
        setTrans(newWords[0].trans)
        val otherWordsTrans =  mutableListOf(newWords[1].trans,newWords[2].trans,newWords[3].trans)
        setOtherWordTranslation(otherWordsTrans)
        createRecite()
        val thisView = findViewById<View>(R.id.word)
        btnSpeakNowOnClick(thisView)

    }

    /*
    *  // the code before
    * fun getWordFromXml( num:Int):Word{
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
    */

    // getWordsFromXml function returns a word list contains 4 word
    // the 0-index word is the current word to recite
    // the other 3 words are words for incorrect choices
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


    // showDetails function turns to WordDetailsActivity, show the current word to recite
    fun showDetails(word: Word){
        val data = arrayListOf(word.word,word.pos,word.tran,word.trans,word.example)
        val showDetailsActivity = Intent()
        showDetailsActivity.setClass(this,WordDetailsActivity::class.java)
        // pass the word info to WordDetailsActivity
        showDetailsActivity.putStringArrayListExtra("data",data)
        startActivity(showDetailsActivity)

    }

    //tts start
    private fun buildSpeechUrl(words: String, language: String): String {
        val kVoiceRssServer = "http://api.voicerss.org"
        val kVoiceRSSAppKey = "e4f83f4e095646cbad5bbc28bf1a65ec"
        var url = ""

        url = kVoiceRssServer + "/?key=" + kVoiceRSSAppKey + "&t=text&hl=" + language + "&src=" + words

        return url
    }

    fun btnSpeakNowOnClick(v: View) {
        var mp : MediaPlayer? = null
        val txtSentence = findViewById<TextView>(R.id.word)

        val text = txtSentence.text.toString()

        try {
            val onPreparedListener = MediaPlayer.OnPreparedListener {
                mp?.setVolume(1f, 1f)
                mp?.start()
            }

            val onErrorListener = MediaPlayer.OnErrorListener { mp, _, _ -> false }

            val onCompletionListener = MediaPlayer.OnCompletionListener { mp ->
                mp.release()
            }

            val url = buildSpeechUrl(text, "it-it")

            val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0)

            try {
                if (mp != null)
                    mp.release()

                mp = MediaPlayer()
                mp.setDataSource(url)
                mp.setOnErrorListener(onErrorListener)
                mp.setOnCompletionListener(onCompletionListener)
                mp.setOnPreparedListener(onPreparedListener)
                mp.prepareAsync()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
