package layout.com.anew.easyItalian


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_chooce_profile_from_dialog.*

import layout.com.anew.easyItalian.recite.SetWordList
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import android.support.annotation.NonNull




class PersonalInfo : Activity(){
     val data = arrayOf("头像", "昵称", "词书")

    private val FILENAMEOFPIC="head.jpg"
    //private val ivHead: ImageView? = null//头像显示
    //private val btnTakephoto: Button? = null//拍照
    //private val btnPhotos: Button? = null//相册
    //private var head: Bitmap? = null//头像Bitmap
    //private val path = "/sdcard/DemoHead/"//sd路径
    private val path = Environment.getExternalStorageDirectory()//sd路径
    //(/sdcard/  目录  Environment.getExternalStorageDirectory()
    private var imageUri: Uri?=null
    //private var mFile :File?=null
    private val FILENAME="profile"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personal_info)

        val listView = findViewById<View>(R.id.list_view1) as ListView
        listView.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)

        listView.setOnItemClickListener{
            _, _, position, _ ->
            //set click listener
            when(position){
                0 -> {
                    //val intent = Intent(this, ChooseDialog::class.java)
                    //startActivity(intent)
                    showImagePickDialog(this)
                    Toast.makeText(this,"call 头像 activity",Toast.LENGTH_SHORT).show()
                }
                1 -> Toast.makeText(this,"call 昵称 activity",Toast.LENGTH_SHORT).show()
                2 -> {
                    val intent = Intent()
                    intent.setClass(this, SetWordList::class.java)
                    startActivity(intent)
                }
            }
        }
        val button_back = findViewById<Button>(R.id.back_)
        button_back.setOnClickListener(){
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }




    fun chooseCamera(){

        //val outPutImage=File(path,FILENAMEOFPIC)
        var mFile= File(this.getExternalFilesDir(null),FILENAME)
        if(!mFile.exists()){
            mFile.mkdirs()
        }
        val outPutImage= File(mFile,FILENAMEOFPIC)

        try{

            if(outPutImage.exists()){
                outPutImage.delete()
            }
            outPutImage.createNewFile()
            // Toast.makeText(this@ChooseDialog, "file for pic create", Toast.LENGTH_LONG).show()
        }catch (e : IOException){
            e.printStackTrace()
        }

        if(Build.VERSION.SDK_INT>=24){
            imageUri= FileProvider.getUriForFile(this,"com.example.cameraalbumtest.fileprovider",outPutImage)
            Toast.makeText(this, "大于", Toast.LENGTH_LONG).show()
        }
        else{
            imageUri=Uri.fromFile(outPutImage);
            //Toast.makeText(this@ChooseDialog, "小于 "+imageUri.toString(), Toast.LENGTH_LONG).show()
        }

        try {

            //申请权限
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    2)

            val intent2=Intent("android.media.action.IMAGE_CAPTURE")
            intent2.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
            startActivityForResult(intent2, 2)
            Toast.makeText(this, "试图打开摄像机", Toast.LENGTH_LONG).show()
            /*
            val intent2 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)//开启相机应用程序获取并返回图片（capture：俘获）
            intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(File(Environment.getExternalStorageDirectory(),
                    "head.jpg")))//指明存储图片或视频的地址URI
            startActivityForResult(intent2, 2)//采用ForResult打开
            */
        } catch (e: Exception) {
            Toast.makeText(this, "相机无法启动，请先开启相机权限", Toast.LENGTH_LONG).show()
        }
    }

    fun chooseAlbum(){
        val intent1 = Intent(Intent.ACTION_PICK, null)//返回被选中项的URI
        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")//得到所有图片的URI
//                System.out.println("MediaStore.Images.Media.EXTERNAL_CONTENT_URI  ------------>   "
//                        + MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//   content://media/external/images/media
        startActivityForResult(intent1, 1)
    }


    fun showImagePickDialog(activity: Activity) {

        val title = "选择获取图片方式"
        val items = arrayOf("拍照", "相册")

        AlertDialog.Builder(activity)
                .setTitle(title)
                .setItems(items, DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                    when (which) {
                        0 ->{
                            //选择拍照
                            chooseCamera()
                            Toast.makeText(this, "拍照", Toast.LENGTH_LONG).show()
                        }


                        1 ->{
                            chooseAlbum()
                            //选择相册
                            Toast.makeText(this, "相册", Toast.LENGTH_LONG).show()
                        }

                        else -> {
                        }
                    }
                }).show()
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
        //从相册里面取相片的返回结果
            1->
                try{
                    if (resultCode == AppCompatActivity.RESULT_OK) {
                        //cropPhoto(data?.getData())//裁剪图片
                        var bitmap :Bitmap?  = null
                        //判断手机系统版本号
                        if (Build.VERSION.SDK_INT >= 19) {
                            //4.4及以上系统使用这个方法处理图片
                            bitmap = ImgUtil.handleImageOnKitKat(this, data);        //ImgUtil是自己实现的一个工具类
                        } else {
                            //4.4以下系统使用这个方法处理图片
                            bitmap = ImgUtil.handleImageBeforeKitKat(this, data);
                        }

                        setPicToView(bitmap!!)
                    }

                }catch (e : IOException){
                    e.printStackTrace()
                }


        //相机拍照后的返回结果
            2->
                if (resultCode == AppCompatActivity.RESULT_OK) {

                    try{

                        cropPhoto(imageUri)//裁剪图片
                        //Toast.makeText(this@ChooseDialog, "address:"+imageUri.toString() , Toast.LENGTH_LONG).show()
                    }catch (e : IOException){
                        e.printStackTrace()
                    }
                }

        //调用系统裁剪图片后
            3->
                if (data != null) {

                    //bug
                    setImageToView(data)
                    //setPicToView(head);//保存在SD卡中
                    //上传服务器代码

                }
        }

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1->{
                if (grantResults.size> 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    chooseAlbum()
                 }
                else{
                    Toast.makeText(this,"deniy to open album",Toast.LENGTH_SHORT).show()
                }
            }

        }





    }



    /*
   var bt :Bitmap = BitmapFactory.decodeFile(path + "head.jpg");//从Sd中找头像，转换成Bitmap
   if (bt != null)
   {
       //如果本地有头像图片的话
       ivHead.setImageBitmap(bt);
   }
*/

    //保存图片
    fun setPicToView(mBitmap : Bitmap) {
        var sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd卡是否可用
            return;
        }
        var b: FileOutputStream?  = null;

        var mFile= File(this.getExternalFilesDir(null),FILENAME)
        if(!mFile.exists()){
            mFile.mkdirs()
        }

        try {
            b= FileOutputStream(mFile.toString()+"/$FILENAMEOFPIC"+"2")
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件（compress：压缩）

        } catch ( e: FileNotFoundException) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                b!!.flush();
                b!!.close();
            } catch ( e: IOException) {
                e.printStackTrace();
            }

        }
    }

    //裁剪图片
    fun cropPhoto(uri:Uri?) {
        val intent =  Intent("com.android.camera.action.CROP");
        //找到指定URI对应的资源图片
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        //进入系统裁剪图片的界面
        startActivityForResult(intent, 3);
    }

    //展示图片
    protected fun setImageToView(data: Intent) {
        Toast.makeText(this,"展示" , Toast.LENGTH_LONG).show()
        val extras = data.extras
        if (extras != null) {
            var photo = extras.getParcelable<Bitmap>("data") as Bitmap
            //photo = Utils.toRoundBitmap(photo, tempUri) // 这个时候的图片已经被处理成圆形的了
            //toRoundBitmap(photo)
            //
            //show.setImageBitmap(photo)
            //toRoundBitmap(photo)
            setPicToView(photo)

        }
    }

    /**
     * 转换图片成圆形
    fun toRoundBitmap(bitmap:Bitmap) :Bitmap{
    var  width = bitmap.getWidth() as Float
    var  height = bitmap.getHeight()
    var  roundPx :Float;
    var left:Float
    var top :Float
    var right:Float
    var bottom:Float
    var  dst_left :Float
    var dst_top:Float
    var  dst_right:Float
    var dst_bottom :Float

    if (width <= height) {
    roundPx = width / 2;
    left = 0 as Float
    top = 0 as Float
    right = width
    bottom = width
    height = width as Int
    dst_left = 0 as Float
    dst_top = 0 as Float
    dst_right = width;
    dst_bottom = width;
    } else {
    roundPx = height / 2 as Float
    var clip :Float = (width - height) / 2;
    left = clip;
    right = width - clip;
    top = 0 as Float
    bottom = height as Float
    width = height
    dst_left = 0 as Float
    dst_top = 0 as Float
    dst_right = height;
    dst_bottom = height;
    }

    var output :Bitmap = Bitmap.createBitmap(width as Int, height, Bitmap.Config.ARGB_8888);
    var canvas :Canvas = Canvas(output);

    val color = 0xff424242;
    val paint = Paint();
    val  src: Rect = Rect( left as Int,  top as Int,  right as Int,  bottom as Int);
    val   dst :Rect = Rect( dst_left as Int,  dst_top as Int,  dst_right as Int  ,  dst_bottom as Int);
    val  rectF :RectF = RectF(dst);

    paint.setAntiAlias(true);// 设置画笔无锯齿

    canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas
    paint.setColor(color as Int);

    // 以下有两种方法画圆,drawRounRect和drawCircle
    // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
    canvas.drawCircle(roundPx, roundPx, roundPx, paint);

    paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
    canvas.drawBitmap(bitmap, src, dst, paint); //以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

    return output;
    }
     */

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            startActivity(Intent(this, MainActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }




}

