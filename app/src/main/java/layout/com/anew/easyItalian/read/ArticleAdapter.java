package layout.com.anew.easyItalian.read;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
        private ImageView image;
        public ViewHolder(View itemView) {

            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            level = (TextView) itemView.findViewById(R.id.level);
            text = (TextView) itemView.findViewById(R.id.text);
            image = (ImageView) itemView.findViewById(R.id.imageView);
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
        Picasso.get().load(article.getimageUrl()).fit().into(holder.image);
    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        //View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item,parent,false);

        View //view = View.inflate(parent.getContext(), R.layout.article_item, null);
         view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item,null);
        final ViewHolder holder = new ViewHolder(view);

        //点击效果
        holder.articleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Article article=mList.get(position);
                String uid = article.getId();
                ArrayList<String> data = new ArrayList<String>();
                data.add(uid);
                Intent showArticlePageActivity = new Intent();
                showArticlePageActivity.setClass(v.getContext(),ArticlePageActivity.class);
                showArticlePageActivity.putStringArrayListExtra("data",data);
                v.getContext().startActivity(showArticlePageActivity);
            }
        });

        //点击title
        holder.title.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Article article=mList.get(position);

                // the following part is ok
                String uid = article.getId();
                ArrayList<String> data = new ArrayList<String>();
                data.add(uid);
                Intent showArticlePageActivity = new Intent();
                showArticlePageActivity.setClass(v.getContext(),ArticlePageActivity.class);
                showArticlePageActivity.putStringArrayListExtra("data",data);
                v.getContext().startActivity(showArticlePageActivity);
                }
        });

        //点击正文
        holder.text.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Article article=mList.get(position);
                String uid = article.getId();
                ArrayList<String> data = new ArrayList<String>();
                data.add(uid);
                Intent showArticlePageActivity = new Intent();
                showArticlePageActivity.setClass(v.getContext(),ArticlePageActivity.class);
                showArticlePageActivity.putStringArrayListExtra("data",data);
                v.getContext().startActivity(showArticlePageActivity);
            }
        });
        return holder;
    }


}

