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

    //private  Context context;
    private List<Article> mList;
    public ArticleAdapter(Context context, List<Article> mList) {
        //this.context = context;
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

            image = (ImageView) itemView.findViewById(R.id.image);
            articleView=itemView;
        }
    }




    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item,parent,false);

        //View view = View.inflate(context, R.layout.article_item, null);

        final ViewHolder holder = new ViewHolder(view);

        holder.articleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Article article=mList.get(position);
                Toast.makeText(v.getContext(),"you click item"+article.getTitle(),Toast.LENGTH_SHORT);
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Article article=mList.get(position);
                Toast.makeText(v.getContext(),"you click image of"+article.getTitle(),Toast.LENGTH_SHORT);
            }
        });

        return holder;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        /**
         * 给itemView设置tag
         */
        Article article =mList.get(position);
        holder.itemView.setTag(position);
        holder.title.setText(article.getTitle());
        holder.level.setText(article.getLevel());
        holder.text.setText(article.getText());
        holder.image.setImageResource(article.getImageId());
    }


    public int getItemCount() {
        return mList.size();
    }


}

