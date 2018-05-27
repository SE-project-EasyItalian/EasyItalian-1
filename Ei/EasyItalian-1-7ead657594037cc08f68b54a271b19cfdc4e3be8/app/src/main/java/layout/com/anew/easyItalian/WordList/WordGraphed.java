package layout.com.anew.easyItalian.WordList;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by liaoyujun on 2018/5/25.
 */

@Table(database = DBFlowDataBase.class)
public class WordGraphed extends BaseModel {

    @PrimaryKey(autoincrement = true)
    public long id;

    @Column
    public String word;


    public WordGraphed(){

    }

    public  void insertData(String word){
        this.word=word;
    }

    public long getId() {
        return id;
    }
    public String getWord(){
        return word;
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setWord(String word) {
        this.word = word;
    }

}
