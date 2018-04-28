package layout.com.anew.easyItalian.read;

// define the Class Article
public class Article {
    private  int id;
    private String title;
    private int level;
    private String text;
    private int imageId;
    //and so on

    public Article(int id,String title, int level,String text,int imageId){
        this.id=id;
        this.title=title;
        this.level=level;
        this.text=text;
        this.imageId=imageId;
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

    public int getImageId(){
        return this.imageId;
    }

}
