package layout.com.anew.easyItalian

import android.content.Intent
import android.os.*
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.view.View
import android.widget.TextView
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVQuery
import com.avos.avoscloud.AVUser
import com.lapism.searchview.Search
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.content_main.*
import layout.com.anew.easyItalian.WordList.WordsListsPage
import layout.com.anew.easyItalian.read.ReadActivity
import layout.com.anew.easyItalian.recite.DaoOpt
import layout.com.anew.easyItalian.recite.ReciteWordAcitivity
import layout.com.anew.easyItalian.recite.Word
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {



    override fun onCreate(savedInstanceState: Bundle?) {

      //  val login = Intent(this,LoginActivity::class.java)
     //   startActivity(login)
        val currentUser = AVUser.getCurrentUser()
        if (currentUser == null) {
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        try {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads().detectDiskWrites().detectNetwork()
                    .penaltyLog().build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                    .penaltyLog().penaltyDeath().build())
            val myProfile =   currentUser.fetch().get("profile").toString()
            val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
            val headerView = navigationView.getHeaderView(0)
            val profile = headerView.findViewById<CircleImageView>(R.id.profile_picture)
            if (myProfile!="laura")
                Picasso.get().load(myProfile).into(profile)
            val nickName = headerView.findViewById<TextView>(R.id.textView)
            if(currentUser!=null){
                nickName.setText(currentUser.fetch().get("nickName").toString() )
            }
        } catch (e:Exception){
            val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
            val headerView = navigationView.getHeaderView(0)
            val profile = headerView.findViewById<CircleImageView>(R.id.profile_picture)
            if (File(getExternalFilesDir("profile"),"head3.jpg").exists()){
                Picasso.get().load(File(getExternalFilesDir("profile"),"head3.jpg")).into(profile)
                Log.d("Profile","Connection Error, Use local profile")
            }else
                Picasso.get().load(R.mipmap.laura).into(profile)
            Log.d("Profile","Can't find local profile")
        }


       val amHandler:Handler= object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                when (msg?.what) {
                    1 -> {
                        try {
                            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                                    .detectDiskReads().detectDiskWrites().detectNetwork()
                                    .penaltyLog().build())
                            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                                    .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                                    .penaltyLog().penaltyDeath().build())
                            val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
                            val headerView = navigationView.getHeaderView(0)
                            val profile = headerView.findViewById<CircleImageView>(R.id.profile_picture)
                            val myProfile =  AVUser.getCurrentUser().fetch().get("profile").toString()
                            if (myProfile!="laura")
                                Picasso.get().load(myProfile).into(profile)
                            if(AVUser.getCurrentUser()!=null){
                                val nickName = headerView.findViewById<TextView>(R.id.textView)
                                nickName.setText(AVUser.getCurrentUser().fetch().get("nickName").toString() )
                            }
                        } catch (e:Exception){

                            if (File(getExternalFilesDir("profile"),"head3.jpg").exists()){
                                val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
                                val headerView = navigationView.getHeaderView(0)
                                val profile = headerView.findViewById<CircleImageView>(R.id.profile_picture)
                                Picasso.get().load(File(getExternalFilesDir("profile"),"head3.jpg")).into(profile)
                                Log.d("Profile","Connection Error, Use local profile")
                            }else
                            {
                                val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
                                val headerView = navigationView.getHeaderView(0)
                                val profile = headerView.findViewById<CircleImageView>(R.id.profile_picture)
                                Picasso.get().load(R.mipmap.laura).into(profile)
                            }
                            Log.d("Profile","Can't find local profile")
                        }
                    }
                    else -> {
                        val mBundle = msg?.data
                    }
                }
            }

        }
        amHandler.sendEmptyMessage(1)

        val mApp = application as BaseApplication
        mApp.setHandler(amHandler)



        // find parent view to make profile clickable on first call
        // CHOICE make the profile to a circle
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val profile = headerView.findViewById<CircleImageView>(R.id.profile_picture)
    profile.setOnClickListener(){
       //     Toast.makeText(this,"call 个人资料 activity",Toast.LENGTH_SHORT).show()
            val changeToPersonalInfoActivity = Intent()
            changeToPersonalInfoActivity.setClass(this,PersonalInfo::class.java)
            startActivity(changeToPersonalInfoActivity)
            drawer_layout.closeDrawer(GravityCompat.START) // close side bar
        }

        //search_new_word & learn_button & read_button
        searchWordButton.setOnClickListener(){
         //   AVUser.logOut()// 清除缓存用户对象
            // use handler to refresh ui
            val mApp = application as BaseApplication
            mApp.mHandler!!.sendEmptyMessage(1)
             Toast.makeText(this,"call 查单词 activity",Toast.LENGTH_SHORT).show()
        }


        searchBar.setOnQueryTextListener(object:Search.OnQueryTextListener{

            override fun onQueryTextSubmit(query:CharSequence):Boolean{

                    //searchBar.setMicIcon(R.drawable.ic_search_black)
                    StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                            .detectDiskReads().detectDiskWrites().detectNetwork()
                            .penaltyLog().build())
                    StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                            .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                            .penaltyLog().penaltyDeath().build())
                    val text=searchBar.getText().toString()
                    //Toast.makeText(this,"search"+text,Toast.LENGTH_SHORT).show()
                    //searchBar.setQuery(text,true)
                    if(text.length!=0){

                        val my = DaoOpt.getInstance()
                        val mList=my.queryForWord(this@MainActivity,text)

                        if(mList?.size!=0){
                            val word : Word?=mList?.get(0)
                            val data = arrayListOf(word?.word,word?.transform,word?.translation,word?.example)
                            Toast.makeText(this@MainActivity,"search"+word?.word,Toast.LENGTH_SHORT).show()

                            val changeToSearchWordPage = Intent()
                            changeToSearchWordPage.setClass(this@MainActivity, SearchWordPage::class.java)
                            changeToSearchWordPage.putStringArrayListExtra("data",data)
                            startActivity(changeToSearchWordPage)
                        }
                        else{
                            val word : Word?= Word()
                            word?.setWord(text)
                            //word?.setTranslation(getTranslation(text))
                            //val data = arrayListOf(word?.word,word?.transform,word?.translation,word?.example)
                            //      val translation=getTranslation(text)
                            val translation=getDetails(text)
                            Toast.makeText(this@MainActivity,translation,Toast.LENGTH_SHORT).show()
                            val data = arrayListOf(word?.word,translation)
                            val changeToSearchWordPage = Intent()
                            changeToSearchWordPage.setClass(this@MainActivity, SearchWordPage::class.java)
                            changeToSearchWordPage.putStringArrayListExtra("data",data)
                            startActivity(changeToSearchWordPage)
                        }
                    }
                //Toast.makeText(this@MainActivity,"1"+searchBar.getText().toString(),Toast.LENGTH_SHORT).show()
                return false
            }
            override fun onQueryTextChange(newText: CharSequence):Unit{
                //Toast.makeText(this@MainActivity,"2",Toast.LENGTH_SHORT).show()

            }
        })



        searchBar.setOnMicClickListener(){

            if(searchBar.isOpen) {
                searchBar.close()
            }
        }


        buttonForLearn.setOnClickListener(){
            val changeToRecite = Intent()
            changeToRecite.setClass(this, ReciteWordAcitivity::class.java)
            startActivity(changeToRecite)
        }
        buttonForRead.setOnClickListener(){
           val changeToRead = Intent();
            changeToRead.setClass(this, ReadActivity::class.java)
           startActivity(changeToRead)
        }

        // make sure the nav_view open&close in time
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_new_word -> {
                val choice=arrayListOf("1")
                val changeToNewWords = Intent()
                changeToNewWords.setClass(this, WordsListsPage::class.java)
                changeToNewWords.putStringArrayListExtra("choice",choice);
                startActivity(changeToNewWords)
            }
            R.id.nav_finished_word-> {
                val choice=arrayListOf("2")
                val changeToFinishedWords = Intent()
                changeToFinishedWords.setClass(this, WordsListsPage::class.java)
                changeToFinishedWords.putStringArrayListExtra("choice",choice)
                startActivity(changeToFinishedWords)
            }
            R.id.nav_coming_word -> {
                // same as above
                val choice=arrayListOf("3")
                val changeToComingWords = Intent()
                changeToComingWords.setClass(this, WordsListsPage::class.java)
                changeToComingWords.putStringArrayListExtra("choice",choice)
                startActivity(changeToComingWords)
            }
            R.id.nav_setting -> {
                val changeToSetting = Intent()
                changeToSetting.setClass(this, SettingActivity::class.java)
                startActivity(changeToSetting)
            }
            R.id.nav_about -> {
                Toast.makeText(this,"call 关于 activity",Toast.LENGTH_SHORT).show()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun streamToString(myis: InputStream): String? {
        try {
            val out = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len = myis.read(buffer)
            while (len != -1) {
                out.write(buffer, 0, len)
                len = myis.read(buffer)
            }
            out.close()
            myis.close()
            val byteArray = out.toByteArray()
            return String(byteArray)
        } catch (e: Exception) {
            return null
        }
    }

    fun getTranslation(word :String):String{
        var result = ""
        try{
            val googleTranslate = "https://translate.google.cn/translate_a/single?client=gtx&sl=it&tl=zh-CN&dt=t&q="
            val translateUrl = googleTranslate+word
            val url = URL(translateUrl)
            val urlConn = url.openConnection() as HttpURLConnection
            // 设置连接主机超时时间
            urlConn.connectTimeout = 5 * 1000
            //设置从主机读取数据超时
            urlConn.readTimeout = 5 * 1000
            // 设置是否使用缓存  默认是true
            urlConn.useCaches = false
            // 设置为Post请求
            urlConn.requestMethod = "GET"
            //urlConn设置请求头信息
            val USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36"
            urlConn.setRequestProperty("User-Agent", USER_AGENT)
            urlConn.connect()
            val statusCode = urlConn.responseCode
            var googleResult = ""
            if (statusCode == 200) {
                // 获取返回的数据
                googleResult = streamToString(urlConn.getInputStream()) ?:""
            }

            val jsonArray = JSONArray(googleResult).getJSONArray(0)
            for (i in 0 until jsonArray.length()) {
                result += jsonArray.getJSONArray(i).getString(0)
            }
            Toast.makeText(this,result,Toast.LENGTH_SHORT).show()
            return result
        }catch (e :Exception){
            e.printStackTrace();
            result = "";
        }
        return result
    }

    fun getDetails(word :String):String{
        val result:String
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build())
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build())
        val query = AVQuery<AVObject>("Words")
        if (query.whereEqualTo("word",word).first!=null){
            result = query.whereEqualTo("word",word).first["details"].toString()
        }else  {

            result=getTranslation(word)
        }


        return result
    }



}

