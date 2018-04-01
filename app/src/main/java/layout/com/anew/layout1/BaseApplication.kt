package layout.com.anew.layout1

import android.app.Application
import layout.com.anew.layout1.gen.DaoSession
import layout.com.anew.layout1.gen.DaoMaster
import android.database.sqlite.SQLiteDatabase



class BaseApplication : Application(){

    private var daoSession: DaoSession? = null

    override fun onCreate() {
        super.onCreate()
        //配置数据库
        setupDatabase()
        GreenDaoManager.getInstance(this)
    }

    /**
     * 配置数据库
     */
    private fun setupDatabase() {
        //创建数据库shop.db"
        val helper = DaoMaster.DevOpenHelper(this, "wordList.db", null)
        //获取可写数据库
        val db = helper.writableDatabase
        //获取数据库对象
        val daoMaster = DaoMaster(db)
        //获取Dao对象管理者
        daoSession = daoMaster.newSession()
    }

    fun getDaoInstant(): DaoSession? {
        return daoSession
    }


}