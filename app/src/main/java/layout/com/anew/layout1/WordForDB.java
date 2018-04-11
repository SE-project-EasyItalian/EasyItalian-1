package layout.com.anew.layout1;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class WordForDB {

    @Id
    private Long id;

    @Property(nameInDb = "word")
    private String word;
    @Property(nameInDb = "transform")
    private String transform;
    @Property(nameInDb = "translation")
    private String translation;
    @Property(nameInDb = "example")
    private String example;
    @Property(nameInDb = "appearTime")
    private Integer appearTime;
    @Property(nameInDb = "correctTime")
    private Integer correctTime;
    @Property(nameInDb = "incorrectTime")
    private Integer incorrectTime;
    @Property(nameInDb = "EFactor")
    private Double EFactor;
    @Property(nameInDb = "interval")
    private Integer interval;
    @Property(nameInDb = "nextAppearTime")
    private Integer nextAppearTime;


    @Generated(hash = 2130861856)
    public WordForDB(Long id, String word, String transform, String translation,
            String example, Integer appearTime, Integer correctTime,
            Integer incorrectTime, Double EFactor, Integer interval,
            Integer nextAppearTime) {
        this.id = id;
        this.word = word;
        this.transform = transform;
        this.translation = translation;
        this.example = example;
        this.appearTime = appearTime;
        this.correctTime = correctTime;
        this.incorrectTime = incorrectTime;
        this.EFactor = EFactor;
        this.interval = interval;
        this.nextAppearTime = nextAppearTime;
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
    public String getTransform() {
        return this.transform;
    }
    public void setTransform(String transform) {
        this.transform = transform;
    }
    public String getTranslation() {
        return this.translation;
    }
    public void setTranslation(String translation) {
        this.translation = translation;
    }
    public Integer getNextAppearTime() {
        return this.nextAppearTime;
    }
    public void setNextAppearTime(Integer nextAppearTime) {
        this.nextAppearTime = nextAppearTime;
    }
    public Double getEFactor() {
        return this.EFactor;
    }
    public void setEFactor(Double EFactor) {
        this.EFactor = EFactor;
    }
    public Integer getInterval() {
        return this.interval;
    }
    public void setInterval(Integer interval) {
        this.interval = interval;
    }

}
