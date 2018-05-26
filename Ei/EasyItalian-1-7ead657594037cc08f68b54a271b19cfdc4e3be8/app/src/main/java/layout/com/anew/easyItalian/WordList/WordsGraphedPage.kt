package layout.com.anew.easyItalian.WordList


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import layout.com.anew.easyItalian.MainActivity
import layout.com.anew.easyItalian.R
import com.tencent.qc.stat.common.User
import com.raizlabs.android.dbflow.sql.language.SQLite




class WordsGraphedPage : Activity() {

    private var data= mutableListOf("")








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_words)



        //wordGraphed.insertData("Ciao")
        //wordGraphed.insertData( "Buongiorno")
        //wordGraphed.insertData( "Senta")
        //wordGraphed.insertData("dire")
        //val save =  wordGraphed.save()

        insertGraphedWord("ciao1")
        insertGraphedWord("ciao2")
        insertGraphedWord("ciao3")

        //Toast.makeText(this,"wordInList Successfully "+save.toString() , Toast.LENGTH_SHORT).show()

        var wordGraphedList=SQLite.select().from(WordGraphed::class.java).queryList()
        //var n=wordGraphedList.size
        Toast.makeText(this,wordGraphedList[0].word , Toast.LENGTH_SHORT).show()

        initData(wordGraphedList)
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

    fun initData(wordGraphedList:List<WordGraphed>){

        for (wordGraphed in wordGraphedList){

            data.add(wordGraphed.word)
        }

    }

    fun insertGraphedWord(word:String){
        var wordGraphed:WordGraphed=WordGraphed()
        wordGraphed.insertData(word)
        wordGraphed.save()
    }
}
