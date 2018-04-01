package layout.com.anew.layout1

import org.greenrobot.greendao.annotation.Entity
import org.greenrobot.greendao.annotation.Property
import org.greenrobot.greendao.annotation.Id
import org.greenrobot.greendao.annotation.Index

// Word Class for database test
// experimental try


class WordForDB1{
   // @Id
    var id : Int =0
   // @Property(nameInDb = "word")
    var word : String = " "
   // @Property(nameInDb = "pos")
     var pos : String =" "
  //  @Property(nameInDb = "tran")
     var tran : String = " "
  //  @Property(nameInDb = "trans")
     var trans : String = " "
   // @Property(nameInDb = "example")
     var example : String = " "
   // @Property(nameInDb = "appearTime")
    var appearTime : Int =0
  //l  @Property(nameInDb = "correctTime")
    var correctTime : Int = 0
  //  @Property(nameInDb = "incorrectTime")
    var incorrectTime : Int = 0
}

