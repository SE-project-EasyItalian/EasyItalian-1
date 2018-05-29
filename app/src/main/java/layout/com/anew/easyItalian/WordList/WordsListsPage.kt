package layout.com.anew.easyItalian.WordList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.raizlabs.android.dbflow.sql.language.SQLite
import layout.com.anew.easyItalian.MainActivity
import layout.com.anew.easyItalian.R
import layout.com.anew.easyItalian.recite.DaoOpt
import layout.com.anew.easyItalian.recite.Word

class WordsListsPage : Activity() {



    private val data = ArrayList<String>()
    //private var data= MutableList<String>()
    //private var data= mutableListOf("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wods_lists)

        val ins = intent
        val choice = ins.getStringArrayListExtra("choice")[0]
        when(choice){

            "1"->showNewWordsList()
            "2"->showFinishedWordsList()
            "3"->showComingWordsList()
            else->{}
        }
        //wordGraphed.insertData("Ciao")
        //wordGraphed.insertData( "Buongiorno")
        //wordGraphed.insertData( "Senta")
        //wordGraphed.insertData("dire")
        //val save =  wordGraphed.save()



        //Toast.makeText(this,"wordInList Successfully "+save.toString() , Toast.LENGTH_SHORT).show()



        val buttonForBack = findViewById<View>(R.id.back) as Button
        buttonForBack.setOnClickListener {
            val returnToMain = Intent(this, MainActivity::class.java)
            startActivity(returnToMain)
        }
    }

/*

    fun insertNewWord(word:WordNew){
        //var wordNew: WordNew = WordNew()
        var wordNew=WordNew()
        wordNew.insertData(word)
        wordNew.save()
    }
    */
    fun showNewWordsList(){
        var word= WordNew()
        //word.id=0;
        //word.word="text"
       // word.transform=""
       // word.translation=""
        //word.example=""
        //insertNewWord(word)
        //insertNewWord("ciao2")
       // insertNewWord("ciao3")
        var wordNewList= SQLite.select().from(WordNew::class.java).queryList()
        //var n=wordGraphedList.size
        //Toast.makeText(this, wordNewList[0].word, Toast.LENGTH_SHORT).show()
        for (wordNew in wordNewList){
            //Toast.makeText(this, wordNew.id.toString()+wordNew.word, Toast.LENGTH_SHORT).show()
            data.add(wordNew.id.toString()+wordNew.word)
        }
        if(data.size==0){
            Toast.makeText(this, "尚未加入单词至生词表", Toast.LENGTH_SHORT).show()
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        //val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        val listView = findViewById<View>(R.id.new_words) as ListView
        listView.adapter = adapter

    }

    fun showFinishedWordsList(){
        val my = DaoOpt.getInstance()
        val listFinshed:MutableList<Word>?=my.queryForGrasp(this,true)

        for (wordFinished in listFinshed.orEmpty()){
            //Toast.makeText(this, wordFinished.id.toString()+wordFinished.word, Toast.LENGTH_SHORT).show()
            data.add(wordFinished.id.toString()+wordFinished.word)
        }
        data.removeAt(0)
        if(data.size==0){
            Toast.makeText(this, "该表为空", Toast.LENGTH_SHORT).show()
    }
    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        //val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        val listView = findViewById<View>(R.id.new_words) as ListView
        listView.adapter = adapter
    }
    fun showComingWordsList(){
        val my = DaoOpt.getInstance()
        val listComing:MutableList<Word>?=my.queryForGrasp(this,false)

        for (wordFinished in listComing.orEmpty()){
            //Toast.makeText(this, wordFinished.id.toString()+wordFinished.word, Toast.LENGTH_SHORT).show()
            data.add(wordFinished.id.toString()+wordFinished.word)
        }
        if(data.size==0){
            Toast.makeText(this, "该表为空", Toast.LENGTH_SHORT).show()
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        //val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        val listView = findViewById<View>(R.id.new_words) as ListView
        listView.adapter = adapter
    }
}