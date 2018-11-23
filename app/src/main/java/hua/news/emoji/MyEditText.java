package hua.news.emoji;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/22 9:49
 */

public class MyEditText extends AppCompatEditText {
    public MyEditText(Context context) {
        this(context, null);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new MyInputConnection(this, true, super.onCreateInputConnection(outAttrs));
    }

    static class MyInputConnection extends BaseInputConnection {

        private InputConnection ic;

        public MyInputConnection(View targetView, boolean fullEditor, InputConnection ic) {
            super(targetView, fullEditor);
            this.ic = ic;
        }

        public MyInputConnection(View targetView, boolean fullEditor) {
            super(targetView, fullEditor);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            Log.e("@@@hua", "send key = " + event.getCharacters());
            return ic.sendKeyEvent(event);
        }

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            Log.e("@@@hua", "commit text = " + text);
            return ic.commitText(text, newCursorPosition);
        }

//        @Override
//        public boolean beginBatchEdit() {
//            Log.e("@@@hua","begin bach");
//            return ic.beginBatchEdit();
//        }
//
//        @Override
//        public boolean endBatchEdit() {
//            Log.e("@@@hua","end bach");
//            return ic.endBatchEdit();
//        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            Log.e("@@@hua", "before length = " + beforeLength + ", after length = " + afterLength);
            return ic.deleteSurroundingText(beforeLength, afterLength);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean deleteSurroundingTextInCodePoints(int beforeLength, int afterLength) {
            Log.e("@@@hua", "deleteSurroundingTextInCodePoints before length = " + beforeLength + ", after length = " + afterLength);
            return ic.deleteSurroundingTextInCodePoints(beforeLength, afterLength);
        }
    }
}
