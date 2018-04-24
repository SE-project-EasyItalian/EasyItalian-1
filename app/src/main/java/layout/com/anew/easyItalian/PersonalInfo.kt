package layout.com.anew.easyItalian


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.*


class PersonalInfo : Activity(){
     val data = arrayOf("头像", "昵称", "词书")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personal_info)

        val listView = findViewById<ListView>(R.id.list_view1)
        listView.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)

        listView.setOnItemClickListener{
            _, _, position, _ ->
            //set click listener
            when(position){
                0 -> Toast.makeText(this,"call 头像 activity",Toast.LENGTH_SHORT).show()
                1 -> Toast.makeText(this,"call 昵称 activity",Toast.LENGTH_SHORT).show()
                2 -> Toast.makeText(this,"call 词书 activity",Toast.LENGTH_SHORT).show()
            }
        }
        val button_back = findViewById<Button>(R.id.back_)
        button_back.setOnClickListener(){
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            startActivity(Intent(this, MainActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }




}

