package layout.com.anew.easyItalian

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog



class WordListAdapter: RecyclerView.Adapter<WordListAdapter.RecyclerHolder>{


    private var mContext: Context? =null
    private var mList: List<Wordlist>? = null
    private var item :ItemClick? = null

    constructor( context: Context,list: ArrayList<Wordlist>,item:ItemClick) {
        this.mList = list;
        this.mContext = context;
        this.item=item;
    }
    override fun getItemCount(): Int {
        return mList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        val wordlist = mList!![position]
        holder.text.text = wordlist.wordlistName
      //  holder.text.setText(wordlist.wordlistName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wordlist, null)

        val holder = RecyclerHolder(view)

        holder.text.setOnClickListener(View.OnClickListener { v ->
            val position = holder.adapterPosition
            item?.OnItemClick(v,position)
        })

     //       val position = holder.adapterPosition
      //      Toast.makeText(mContext,position,Toast.LENGTH_SHORT).show()

      //  view.setOnClickListener(onClick())
     //   holder.wordlistView.setOnClickListener{

    //        val position = holder.adapterPosition
           // mItemClickListener?.onItemClick(position)
       //     val wordlist = mList[position]
        //    val id = wordlist.url
           // Toast.makeText(mContext,id,Toast.LENGTH_LONG).show()
       //     MaterialDialog.Builder(mContext)
       //             .title(wordlist.wordlistName)
       // /            .content(wordlist.wordlistDesc)
       //             .negativeText("Cancel")
       //             .positiveText("Download").onPositive {
       //                 dialog: MaterialDialog, which: DialogAction ->
        //                Toast.makeText(mContext,"下载",Toast.LENGTH_LONG).show()
       //                 }
       //             .show()


        return holder
    }

    class RecyclerHolder(private val wordlistView: View) : RecyclerView.ViewHolder(wordlistView) {
         var text: TextView

        init {
            text = wordlistView.findViewById<View>(R.id.text) as TextView
        }
    }


    inner class onClick:View.OnClickListener{
        override fun onClick(v: View) {
            var position = RecyclerHolder(v).adapterPosition as Int
          //  var positon: Int = v!!.getTag() as Int? ?:9999
          //  Toast.makeText(mContext,position.toString(),Toast.LENGTH_LONG).show();
            if(item==null){
                Toast.makeText(mContext,"哈哈哈",Toast.LENGTH_LONG).show();
            }
            item!!.OnItemClick(v,position);
        }

    }
}


