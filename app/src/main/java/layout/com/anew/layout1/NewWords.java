package layout.com.anew.layout1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by liaoyujun on 2018/3/22.
 */

public class NewWords extends Activity {
    //private Button button_back;
    private String[] data={"Ciao","Buongiorno","Senta"};
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_words);

        ArrayAdapter<String> adapter=new ArrayAdapter<>(NewWords.this,android.R.layout.simple_list_item_1,data);
        ListView listView=findViewById(R.id.new_words);
        listView.setAdapter(adapter);


        Button button_back=findViewById(R.id.back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent returnToMain=new Intent(NewWords.this,MainActivity.class);
                startActivity(returnToMain);
            }
        });
    }
}
