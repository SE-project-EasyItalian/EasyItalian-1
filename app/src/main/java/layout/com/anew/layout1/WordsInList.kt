package layout.com.anew.layout1


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView


class WordsInList : Activity() {

    private val data = arrayOf("Ciao", "Buongiorno", "Senta")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_words)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        val listView = findViewById<ListView>(R.id.new_words)
        listView.adapter = adapter


        val buttonForBack = findViewById<Button>(R.id.back)
        buttonForBack.setOnClickListener {
            val returnToMain = Intent(this, MainActivity::class.java)
            startActivity(returnToMain)
        }
    }
}
