package layout.com.anew.layout1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_recite_word.*
import java.util.Random
import kotlin.math.abs

class ReciteWord : Activity() {

    var wordForRecite = "Ciao"
    var wordTrans = "你好，再见"  //word translation . use setTrans to change it
    var otherWordTrans = mutableListOf<String>("番茄","函数","相等")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recite_word)

        word.setText(wordForRecite) // set the current word for recite

        val buttonMap = mapOf<Int,Button>(0 to buttonA , 1 to buttonB,2 to buttonC ,3 to buttonD)
        val rand = Random()
        val randnum = abs(rand.nextInt())%4

        buttonMap[randnum]?.setText(wordTrans) //set right choice

        for(i in 0..3){
            if(i!=randnum){
                buttonMap[i]?.setText(otherWordTrans[0]) //set choices
                otherWordTrans.removeAt(0)
            }
        }

        buttonMap[randnum]?.setOnClickListener(){
            Toast.makeText(this, "答对了！", Toast.LENGTH_LONG).show()
            // this should  turn to a new word
        }
        for(i in 0..3){
            if(i!=randnum){
                buttonMap[i]?.setOnClickListener({
                    Toast.makeText(this, "答错了！", Toast.LENGTH_LONG).show()
                    // this should  turn to a detail pages
                })
            }
        }

        remembereIt.setOnClickListener { }
        showDetails.setOnClickListener {
            val intent = Intent(this, WordImformation::class.java)
            startActivity(intent)
        }

        backForRecite.setOnClickListener {
            //return to main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    fun setWord(word:String){
        wordForRecite=word
    }
    fun setTrans(trans:String){
        wordTrans=trans
    }
    // need a fun to set other words' trans
    /*
    *
    * fun setOtherWordTrans(){
    *
    * }
    * */
}
