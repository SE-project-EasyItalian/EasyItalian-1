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

class WordsNewPage : Activity() {

    private var data= mutableListOf("")








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_words)



        //wordGraphed.insertData("Ciao")
        //wordGraphed.insertData( "Buongiorno")
        //wordGraphed.insertData( "Senta")
        //wordGraphed.insertData("dire")
        //val save =  wordGraphed.save()

        insertNewWord("ciao1")
        insertNewWord("ciao2")
        insertNewWord("ciao3")

        //Toast.makeText(this,"wordInList Successfully "+save.toString() , Toast.LENGTH_SHORT).show()

        var wordNewList= SQLite.select().from(WordNew::class.java).queryList()
        //var n=wordGraphedList.size
        Toast.makeText(this, wordNewList[0].word, Toast.LENGTH_SHORT).show()

        initData(wordNewList)
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

    fun initData(wordNewList:List<WordNew>){

        for (wordNew in wordNewList){

            data.add(wordNew.word)
        }

    }

    fun insertNewWord(word:String){
        //var wordNew: WordNew = WordNew()
        var wordNew:WordNew=WordNew()
        wordNew.insertData(word)
        wordNew.save()
    }
}