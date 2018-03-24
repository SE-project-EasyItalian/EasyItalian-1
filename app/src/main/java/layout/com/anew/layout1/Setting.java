package layout.com.anew.layout1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by liaoyujun on 2018/3/22.
 */

public class Setting extends Activity{
    private String[] data={"语言","主题","壁纸"};
 //   private Button button_back;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(Setting.this,android.R.layout.simple_list_item_1,data);
        ListView listView=(ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // String text = (String) ((TextView)view.findViewById(R.id.text)).getText();
                //大多数情况下，position和id相同，并且都从0开始
                String showText = "点击第" + position + "项，文本内容为："  + "，ID为：" + id;
                Toast.makeText(Setting.this,showText,Toast.LENGTH_SHORT).show();

            }
        });



        Button button_back=(Button)findViewById(R.id.back_);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Setting.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
