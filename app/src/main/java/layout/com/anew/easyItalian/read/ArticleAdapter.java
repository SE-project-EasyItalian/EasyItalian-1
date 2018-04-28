package layout.com.anew.easyItalian.read;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import layout.com.anew.easyItalian.R;

/**
 * Created by liaoyujun on 2018/4/25.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>{


    private List<Article> mList;

    public ArticleAdapter(Context context, List<Article> mList) {
        this.mList = mList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private View articleView;
        private TextView title;
        private TextView level;
        private TextView text;
        //private ImageView image;
        public ViewHolder(View itemView) {

            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            level = (TextView) itemView.findViewById(R.id.level);
            text = (TextView) itemView.findViewById(R.id.text);
            //image = (ImageView) itemView.findViewById(R.id.image);
            articleView=itemView;
        }
    }



    @Override

    public int getItemCount() {
        return mList.size();
    }

    public void onBindViewHolder(ViewHolder holder, int position) {

        Article article =mList.get(position);
        //holder.itemView.setTag(position);
        holder.title.setText(article.getTitle());
        holder.level.setText(article.getLevel());
        holder.text.setText(article.getText());
        // holder.image.setImageResource(article.getimageUrl());
    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        //View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item,parent,false);
        View view = View.inflate(parent.getContext(), R.layout.article_item2, null);
        final ViewHolder holder = new ViewHolder(view);

        //点击效果
        holder.articleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Article article=mList.get(position);
                Toast.makeText(v.getContext(),"you click item "+position,Toast.LENGTH_SHORT).show();
            }
        });

        //点击title
        holder.title.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Article article=mList.get(position);
                Toast.makeText(v.getContext(),"you click title of item "+position+article.getTitle(),Toast.LENGTH_SHORT).show();
            }
        });

        //点击正文
        holder.text.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Article article=mList.get(position);
                Toast.makeText(v.getContext(),"you click text of item "+position+article.getTitle(),Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }


}

