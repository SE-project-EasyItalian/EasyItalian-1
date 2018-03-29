package layout.com.anew.layout1


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_word_imformation.*

class WordDetailsActivity() : Activity() {
    val word = Word()
    // getWord accepts the StringArrayList from ReciteWord or another Activity
    // and turn it to Word-info
    fun getWord() {
        val ins = intent
        val listdata = ins.getStringArrayListExtra("data")
        word.word=listdata[0]
        word.pos=listdata[1]
        word.tran=listdata[2]
        word.trans=listdata[3]
        word.example=listdata[4]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_imformation)

        getWord()
        wordForDetails.setText(word.word)
        val data = arrayOf(word.pos+" "+word.trans,word.tran,word.example)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        val listView = findViewById<ListView>(R.id.wordDetails)
        listView.adapter = adapter


        // the buttons' listener has problem
        val button = findViewById<Button>(R.id.continue_study)
        button.setOnClickListener {
            val intent = Intent(this, ReciteWord::class.java)
            startActivity(intent)
        }

        val button_back = findViewById<Button>(R.id.back)
        button_back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

}
