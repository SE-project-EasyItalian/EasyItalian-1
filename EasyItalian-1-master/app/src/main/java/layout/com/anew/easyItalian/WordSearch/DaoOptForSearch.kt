package layout.com.anew.easyItalian.WordSearch

import android.content.Context
import layout.com.anew.easyItalian.gen.WordSearchDao
import layout.com.anew.easyItalian.recite.GreenDaoManager
import layout.com.anew.easyItalian.WordSearch.WordSearch

class DaoOptForSearch private constructor(){
    private object mHolder {
        val instance = DaoOptForSearch()
    }

    companion object {
        fun getInstance(): DaoOptForSearch {
            return mHolder.instance
        }
    }

    /**
     * 添加数据至数据库
     * @param context
     * *
     * @param word
     */
    fun insertData(context: Context?, word: WordSearch) {

        GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordSearchDao?.insert(word)
    }


    /**
     * 将数据实体通过事务添加至数据库

     * @param context
     * *
     * @param list
     */
    fun insertData(context: Context?, list: List<WordSearch>?) {
        if (null == list || list.size <= 0) {
            return
        }
        GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordSearchDao?.insertInTx(list)
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；

     * @param context
     * *
     * @param word
     */
    fun saveData(context: Context?, word: WordSearch) {
        GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordSearchDao?.save(word)
    }

    /**
     * 删除数据至数据库

     * @param context
     * *
     * @param word 删除具体内容
     */
    fun deleteData(context: Context?, word: WordSearch) {
        GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordSearchDao?.delete(word)
    }

    /**
     * 根据id删除数据至数据库

     * @param context
     * *
     * @param id      删除具体内容
     */
    fun deleteByKeyData(context: Context?, id: Long) {
        GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordSearchDao?.deleteByKey(id)
    }

    /**
     * 删除全部数据

     * @param context
     */
    fun deleteAllData(context: Context?) {
        GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordSearchDao?.deleteAll()
    }

    /**
     * 更新数据库

     * @param context
     * *
     * @param word
     */
    fun updateData(context: Context?, word: WordSearch) {
        GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordSearchDao?.update(word)
    }


    /**
     * 查询所有数据

     * @param context
     * *
     * @return
     */
    fun queryAll(context: Context?): MutableList<WordSearch>? {
        val builder = GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordSearchDao?.queryBuilder()
        return builder?.build()?.list()
    }

    /**
     * 根据id，其他的字段类似

     * @param context
     * *
     * @param id
     * *
     * @return
     */
    fun queryForId(context: Context?, id: Long): MutableList<WordSearch>? {
        val builder = GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordSearchDao?.queryBuilder()
        /**
         * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
         * 这里build.list()；与where(WordDao.Properties.Id.eq(id)).list()结果是一样的；
         * 在QueryBuilder类中list()方法return build().list();

         */
        // Query<word> build = builder.where(WordDao.Properties.Id.eq(id)).build();
        // List<word> list = build.list();
        return builder?.where(WordSearchDao.Properties.Id.eq(id))?.list()
    }



    fun queryForIdNotEqual(context: Context?, id: MutableCollection<Long>): MutableList<WordSearch>? {
        val builder = GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordSearchDao?.queryBuilder()
        /**
         * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
         * 这里build.list()；与where(WordDao.Properties.Id.eq(id)).list()结果是一样的；
         * 在QueryBuilder类中list()方法return build().list();
         */
        // Query<word> build = builder.where(WordDao.Properties.Id.eq(id)).build();
        // List<word> list = build.list();
        return builder?.where(WordSearchDao.Properties.Id.notIn(id))?.list()
    }
    fun getNumberOfItems(context: Context?):Int {
        val builder = GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordSearchDao?.queryBuilder()
        return builder?.build()?.list()?.size ?:0
    }



    fun queryForWord(context: Context?, word: String): MutableList<WordSearch>? {
        val builder = GreenDaoManager.getInstance(context!!)?.getDaoSession(context)?.wordSearchDao?.queryBuilder()
        /**
         * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
         * 这里build.list()；与where(WordDao.Properties.Id.eq(id)).list()结果是一样的；
         * 在QueryBuilder类中list()方法return build().list();

         */
        // Query<word> build = builder.where(WordDao.Properties.Id.eq(id)).build();
        // List<word> list = build.list();
        return builder?.where(WordSearchDao.Properties.Word.eq(word))?.list()
    }

}