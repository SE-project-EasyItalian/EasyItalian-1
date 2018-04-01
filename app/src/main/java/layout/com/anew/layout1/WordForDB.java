package layout.com.anew.layout1;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class WordForDB {

    @Id
    private Long id;

    @Property(nameInDb = "word")
    private String word;
    @Property(nameInDb = "pos")
    private String pos;
    @Property(nameInDb = "tran")
    private String tran;
    @Property(nameInDb = "trans")
    private String trans;
    @Property(nameInDb = "example")
    private String example;
    @Property(nameInDb = "appearTime")
    private Integer appearTime;
    @Property(nameInDb = "correctTime")
    private Integer correctTime;
    @Property(nameInDb = "incorrectTime")
    private Integer incorrectTime;
    @Generated(hash = 1256709422)
    public WordForDB(Long id, String word, String pos, String tran, String trans,
            String example, Integer appearTime, Integer correctTime,
            Integer incorrectTime) {
        this.id = id;
        this.word = word;
        this.pos = pos;
        this.tran = tran;
        this.trans = trans;
        this.example = example;
        this.appearTime = appearTime;
        this.correctTime = correctTime;
        this.incorrectTime = incorrectTime;
    }
    @Generated(hash = 1882615787)
    public WordForDB() {
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
    public String getPos() {
        return this.pos;
    }
    public void setPos(String pos) {
        this.pos = pos;
    }
    public String getTran() {
        return this.tran;
    }
    public void setTran(String tran) {
        this.tran = tran;
    }
    public String getTrans() {
        return this.trans;
    }
    public void setTrans(String trans) {
        this.trans = trans;
    }
    public String getExample() {
        return this.example;
    }
    public void setExample(String example) {
        this.example = example;
    }
    public Integer getAppearTime() {
        return this.appearTime;
    }
    public void setAppearTime(Integer appearTime) {
        this.appearTime = appearTime;
    }
    public Integer getCorrectTime() {
        return this.correctTime;
    }
    public void setCorrectTime(Integer correctTime) {
        this.correctTime = correctTime;
    }
    public Integer getIncorrectTime() {
        return this.incorrectTime;
    }
    public void setIncorrectTime(Integer incorrectTime) {
        this.incorrectTime = incorrectTime;
    }
}
