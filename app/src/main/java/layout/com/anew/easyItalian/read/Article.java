package layout.com.anew.easyItalian.read;

// define the Class Article
public class Article {
    private  String id;
    private String title;
    private String level;
    private String text;
    private String imageUrl;
    //and so on

    public Article(String id,String title, String level,String text,String imageUrl){
        this.id=id;
        this.title=title;
        this.level=level;
        this.text=text;
        this.imageUrl=imageUrl;
    }


    public Article(){

    }

    public String getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }
    public String getLevel(){
        return this.level;
    }
    public String getText(){
        return this.text;
    }

    public String getimageUrl(){
        return this.imageUrl;
    }

}
