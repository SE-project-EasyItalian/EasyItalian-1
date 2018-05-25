package layout.com.anew.easyItalian.TouchSelectWordTextView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import org.json.JSONArray;


public class TouchSelectWordText extends AppCompatTextView {

    private final String TWO_CHINESE_BLANK = "  ";
    private BreakIterator iterator;
    private float dxLfet, dxRight, height;
    private final int DX = 5;
    private int mViewWidth;
    private int textHeight;
    private int mLineY;
    private boolean select = true;

    //add popupWindow
    private CustomActionMenuCallBack mCustomActionMenuCallBack;
    private PopupWindow mActionMenuPopupWindow; // 点击弹出菜单
    //add end
    private SparseArray<List<WordTouchBean>> wordlist = new SparseArray<List<WordTouchBean>>();

    public TouchSelectWordText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        iterator = BreakIterator.getWordInstance(Locale.US);
    }

    public TouchSelectWordText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchSelectWordText(Context context, boolean select) {
        this(context, null);
        this.select = select;
    }

    public TouchSelectWordText(Context context) {
        this(context, null);
    }

    /**
     * 在listview中清除其它item 选词
     */
    public void clear() {
        dxLfet = 0;
        dxRight = 0;
        height = 0;
    }

    /**
     * 开启选词
     */
    public void openSelect() {
        select = true;
    }

    /**
     * 关闭选词
     */
    public void closeSelect() {
        select = false;
    }

    /**
     * 判断是否开启选词
     *
     * @return false为关闭，否则为开启
     */
    public boolean isOpenSelect() {
        return select;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.performClick();
        hideActionMenu();
        if (!select) {
            clear();
            return false;
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN ) {
            clear();
            Layout layout = getLayout();
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (layout != null) {
                int line = layout.getLineForVertical(y);
                height = textHeight * line;
                // int offset = layout.getOffsetForHorizontal(line, x);
                String wordText = " ";
                for (WordTouchBean word : wordlist.get(line)) {
                    if (word.getStart() < x && x < word.getEnd()) {
                        dxLfet = word.getStart();
                        dxRight = word.getEnd();
                        wordText = word.getWordText();
                        break;
                    }
                }
                //call popupWindow
                float mTouchDownRawY = event.getRawY();
                int mPopWindowOffsetY = calculatorActionMenuYPosition((int) mTouchDownRawY, (int) event.getRawY());
                ActionMenu mActionMenu = createActionMenu(wordText);

                // show word and its translation on Menu item
                showActionMenu(mPopWindowOffsetY, mActionMenu);

                }
        }

        return false;

    }



    // getTranslation functions
    private String getTranslation(String word){
        String result = "";
        try{
            String  googleTranslate = "https://translate.google.cn/translate_a/single?client=gtx&sl=it&tl=zh-CN&dt=t&q=";
            String  translateUrl = googleTranslate+word;
            URL url = new URL(translateUrl);
            HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            //urlConn设置请求头信息
            String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
            urlConn.setRequestProperty("User-Agent", USER_AGENT);
            urlConn.connect();
            int statusCode = urlConn.getResponseCode();
            String googleResult = "";
            if (statusCode == 200) {
                // 获取返回的数据
                googleResult = streamToString(urlConn.getInputStream());
            }

            JSONArray jsonArray = new JSONArray(googleResult).getJSONArray(0);
            for (int i=0;i<jsonArray.length();i++){
                result += jsonArray.getJSONArray(i).getString(0);
            }

            return result;
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public static String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();
            is.close();
            byte[] byteArray = out.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            return null;
        }
    }

    // create ActionMenu
    private ActionMenu createActionMenu(String word) {
        // 创建菜单
        //set word and its translation
        ActionMenu actionMenu = new ActionMenu(getContext());
        actionMenu.setWord(word+":");
        actionMenu.setTranslation(getTranslation(word));

        actionMenu.addDefaultMenuItem(); // 添加默认item
        actionMenu.addCustomItem();  // 添加自定义item
        actionMenu.setFocusable(true); // 获取焦点
        actionMenu.setFocusableInTouchMode(true);


        return actionMenu;
    }

    //calculatorActionMenuYPosition
    private int calculatorActionMenuYPosition(int yOffsetStart, int yOffsetEnd) {
        if (yOffsetStart > yOffsetEnd) {
            int temp = yOffsetStart;
            yOffsetStart = yOffsetEnd;
            yOffsetEnd = temp;
        }
        int actionMenuOffsetY;
        Context mContext = getContext();
        int mActionMenuHeight = Utils.dp2px(getContext(), 45);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int mScreenHeight = wm.getDefaultDisplay().getHeight();
        if (yOffsetStart < mActionMenuHeight * 3 / 2 + Utils.getStatusBarHeight(mContext)) {
            if (yOffsetEnd > mScreenHeight - mActionMenuHeight * 3 / 2) {
                // 菜单显示在屏幕中间
                actionMenuOffsetY = mScreenHeight / 2 - mActionMenuHeight / 2;
            } else {
                // 菜单显示所选文字下方
                actionMenuOffsetY = yOffsetEnd + mActionMenuHeight / 2;
            }
        } else {
            // 菜单显示所选文字上方
            actionMenuOffsetY = yOffsetStart - mActionMenuHeight * 3 / 2;
        }
        return actionMenuOffsetY;
    }

    //showActionMenu
    private void showActionMenu(int offsetY, layout.com.anew.easyItalian.TouchSelectWordTextView.ActionMenu actionMenu) {
        mActionMenuPopupWindow = new PopupWindow(actionMenu, WindowManager.LayoutParams.WRAP_CONTENT,
                Utils.dp2px(getContext(), 35), true);
        mActionMenuPopupWindow.setFocusable(true);
        mActionMenuPopupWindow.setOutsideTouchable(false);
     //   mActionMenuPopupWindow.setBackgroundDrawable(new ColorDrawable(0xff000000));
        //TODO fix it
        mActionMenuPopupWindow.showAtLocation(this, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, offsetY);


    }

    //hideActionMenu
    private void hideActionMenu() {
        if (null != mActionMenuPopupWindow) {
            mActionMenuPopupWindow.dismiss();
            mActionMenuPopupWindow = null;
        }
    }

    @Override
    public boolean performClick(){
        super.performClick();
        return false;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.MULTIPLY));
        paint.setStyle(Style.FILL);// 实心矩形框
        paint.setColor(Color.parseColor("#C3D2E1F0"));
        canvas.drawRect(dxLfet, height + DX, dxRight, height + textHeight + DX, paint);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        mViewWidth = getMeasuredWidth();
        String text = getText().toString();
        mLineY = 0;
        mLineY += getTextSize();
        Layout layout = getLayout();

        if (layout == null) {
            return;
        }
        Paint.FontMetrics fm = paint.getFontMetrics();
        textHeight = (int) (Math.ceil(fm.descent - fm.ascent));
        textHeight = (int) (textHeight * layout.getSpacingMultiplier() + layout.getSpacingAdd());

        for (int i = 0; i < layout.getLineCount(); i++) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint());
            String line = text.substring(lineStart, lineEnd);

            if (i < layout.getLineCount() - 1) {
                if (needScale(line)) {
                    drawScaledText(canvas, lineStart, line, width, i, true);
                } else {
                    drawScaledText(canvas, lineStart, line, width, i, false);
                }
            } else {
                drawScaledText(canvas, lineStart, line, width, i, false);
            }
            mLineY += textHeight;
        }
    }

    private void drawScaledText(Canvas canvas, int lineStart, String line, float lineWidth,
                                int indexLine, boolean isScale) {
        float x = 0;
        if (isFirstLineOfParagraph(lineStart, line)) {
            canvas.drawText(TWO_CHINESE_BLANK, x, mLineY, getPaint());
            float bw = StaticLayout.getDesiredWidth(TWO_CHINESE_BLANK, getPaint());
            x += bw;
            line = line.substring(3);
        }

        int gapCount = line.length() - 1;
        int i = 0;
        if (line.length() > 2 && line.charAt(0) == 12288 && line.charAt(1) == 12288) {
            String substring = line.substring(0, 2);
            float cw = StaticLayout.getDesiredWidth(substring, getPaint());
            canvas.drawText(substring, x, mLineY, getPaint());
            x += cw;
            i += 2;
        }
        float d = isScale ? (mViewWidth - lineWidth) / gapCount : 0;
        iterator.setText(line);
        int start = iterator.first();
        List<WordTouchBean> words = new ArrayList<WordTouchBean>();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            String possibleWord = line.substring(start, end);
            for (; i < start; i++) {
                String c = String.valueOf(line.charAt(i));
                float cw = StaticLayout.getDesiredWidth(c, getPaint());
                canvas.drawText(c, x, mLineY, getPaint());
                x += cw + d;
            }
            if ((possibleWord.charAt(0) <= 'Z' && possibleWord.charAt(0) >= 'A')
                    || (possibleWord.charAt(0) <= 'z' && possibleWord.charAt(0) >= 'a')) {
                WordTouchBean word = new WordTouchBean(x, possibleWord);
                for (int j = 0; j < possibleWord.length(); j++) {
                    String c = String.valueOf(possibleWord.charAt(j));
                    float cw = StaticLayout.getDesiredWidth(c, getPaint());
                    canvas.drawText(c, x, mLineY, getPaint());
                    if (c.equals(".")) { // 可能存在分词不准确，出现xx.xx
                        WordTouchBean w =
                                new WordTouchBean(word.getStart(), possibleWord.substring(0, j));
                        w.setEnd(x);
                        words.add(w);
                        x += cw + d;
                        word.setStart(x);
                        word.setWordText(possibleWord.substring(j + 1));
                    } else {
                        x += cw + d;
                    }
                }
                word.setEnd(x);
                words.add(word);
                i = end;
            }
        }
        wordlist.put(indexLine, words);
        for (; i < line.length(); i++) {
            String c = String.valueOf(line.charAt(i));
            float cw = StaticLayout.getDesiredWidth(c, getPaint());
            canvas.drawText(c, x, mLineY, getPaint());
            x += cw + d;
        }
    }

    private boolean isFirstLineOfParagraph(int lineStart, String line) {
        return line.length() > 3 && line.charAt(0) == ' ' && line.charAt(1) == ' ';
    }

    private boolean needScale(String line) {
        if (line == null || line.length() == 0) {
            return false;
        } else {
            return line.charAt(line.length() - 1) != '\n';
        }
    }

}