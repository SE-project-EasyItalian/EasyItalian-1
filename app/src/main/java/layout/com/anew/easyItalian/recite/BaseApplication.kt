package layout.com.anew.easyItalian.recite

import android.app.Application
import layout.com.anew.easyItalian.gen.DaoSession
import layout.com.anew.easyItalian.gen.DaoMaster



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
        //创建数据库
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