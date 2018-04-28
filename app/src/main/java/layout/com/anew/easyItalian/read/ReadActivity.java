package layout.com.anew.easyItalian.read;

import android.app.Activity;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import layout.com.anew.easyItalian.R;

public class ReadActivity extends Activity {
    private  List<String> listT=new ArrayList<>();

    private List<Article> articleList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

      //  recyclerView.setAdapter();

        initArticle();
       // Toast.makeText(ReadActivity.this,"hh",Toast.LENGTH_LONG).show();
        ArticleAdapter adapter=new ArticleAdapter(ReadActivity.this,articleList);
       // RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);

    }

    private void initArticle(){

        int i;


        for(i=0;i<5;i++){
            String title="title1";
            int level=4;
            String text="text1";
            int imageId=R.drawable.ic_info;
            Article article=new Article(i,title,level,text,R.drawable.ic_unfinished);

            articleList.add(article);
        }
    }
}
