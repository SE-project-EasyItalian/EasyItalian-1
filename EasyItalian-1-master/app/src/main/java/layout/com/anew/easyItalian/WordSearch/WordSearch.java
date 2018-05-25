package layout.com.anew.easyItalian.WordSearch;

/**
 * Created by liaoyujun on 2018/5/25.
 */
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class WordSearch {
    @Id
    private Long id;

    @Property
    private String word;
    @Property
    private String wordDetail;
    @Generated(hash = 1476221827)
    public WordSearch(Long id, String word, String wordDetail) {
        this.id = id;
        this.word = word;
        this.wordDetail = wordDetail;
    }
    @Generated(hash = 452889251)
    public WordSearch() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getWord() {
        return this.word;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public String getWordDetail() {
        return this.wordDetail;
    }
    public void setWordDetail(String wordDetail) {
        this.wordDetail = wordDetail;
    }



}
