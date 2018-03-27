package layout.com.anew.layout1


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class WordDetailsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_imformation)
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
