package layout.com.anew.easyItalian.read;

// define the Class Article
public class Article {
    private  Long id;
    private String title;
    private int level;
    private String text;
    private String imageUrl;
    //and so on

    public Article(Long id,String title, int level,String text,String imageUrl){
        this.id=id;
        this.title=title;
        this.level=level;
        this.text=text;
        this.imageUrl=imageUrl;
    }


    public Article(){

    }

    public long getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }
    public int getLevel(){
        return this.level;
    }
    public String getText(){
        return this.text;
    }

    public String getimageUrl(){
        return this.imageUrl;
    }

}
