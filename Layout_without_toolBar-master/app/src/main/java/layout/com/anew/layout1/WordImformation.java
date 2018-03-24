package layout.com.anew.layout1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by liaoyujun on 2018/3/22.
 */

public class WordImformation extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_imformation);
        Button button =findViewById(R.id.continue_study);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(WordImformation.this,ResitePage.class);
                startActivity(intent);

            }
        });

        Button button_back=findViewById(R.id.back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(WordImformation.this,MainActivity.class);
                startActivity(intent);

            }
        });

    }
}
