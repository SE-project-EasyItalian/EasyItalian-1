package layout.com.anew.easyItalian.WordList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.raizlabs.android.dbflow.sql.language.SQLite
import layout.com.anew.easyItalian.MainActivity
import layout.com.anew.easyItalian.R

class WordsLearningPage : Activity() {

    private var data= mutableListOf("")





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wods_lists)



        //wordGraphed.insertData("Ciao")
        //wordGraphed.insertData( "Buongiorno")
        //wordGraphed.insertData( "Senta")
        //wordGraphed.insertData("dire")
        //val save =  wordGraphed.save()

        insertLearningWord("ciao1")
        insertLearningWord("ciao2")
        insertLearningWord("ciao3")

        //Toast.makeText(this,"wordInList Successfully "+save.toString() , Toast.LENGTH_SHORT).show()

        var wordLearningList= SQLite.select().from(WordLearning::class.java).queryList()
        //var n=wordGraphedList.size
        Toast.makeText(this, wordLearningList[0].word, Toast.LENGTH_SHORT).show()

        initData(wordLearningList)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        //val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        val listView = findViewById<ListView>(R.id.new_words)
        listView.adapter = adapter


        val buttonForBack = findViewById<Button>(R.id.back)
        buttonForBack.setOnClickListener {
            val returnToMain = Intent(this, MainActivity::class.java)
            startActivity(returnToMain)
        }
    }

    fun initData(wordLearningList:List<WordLearning>){

        for (wordLearning in wordLearningList){

            data.add(wordLearning.word)
        }

    }

    fun insertLearningWord(word:String){
        var wordLearning: WordLearning = WordLearning()
        wordLearning.insertData(word)
        wordLearning.save()
    }
}