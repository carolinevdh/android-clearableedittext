import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import mynamespace.R;

/**
 * EditText clearable through X-shaped button drawn at end
 */
public class ClearableEditText extends RelativeLayout {

    /**
     * EditText component
     */
    private EditText editText;

    /**
     * Button that clears the EditText contents
     */
    private Button clearButton;

    /**
     * Additional listener fired when cleared
     */
    private OnClickListener onClickClearListener;

    public ClearableEditText(Context context) {
        super(context);
        init(null);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    /**
     * Initialize view
     * @param attrs
     */
    private void init(AttributeSet attrs) {

        //inflate layout
        LayoutInflater inflater
            = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_clearable_edittext, this, true);

        //pass attributes to EditText, make clearable
        editText = (EditText) findViewById(R.id.edittext);
        boolean enabled = true;
        if (attrs != null){
            TypedArray attrsArray = 
                getContext().obtainStyledAttributes(attrs,R.styleable.ClearableEditText);
            editText.setInputType(
                attrsArray.getInt(
                    R.styleable.ClearableEditText_android_inputType, InputType.TYPE_CLASS_TEXT));
            editText.setHint(attrsArray.getString(R.styleable.ClearableEditText_android_hint));
            enabled = attrsArray.getBoolean(R.styleable.ClearableEditText_android_enabled, true);
        }
        if (enabled) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0)
                        clearButton.setVisibility(RelativeLayout.VISIBLE);
                    else
                        clearButton.setVisibility(RelativeLayout.GONE);
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        } else { editText.setEnabled(false); }

        //build clear button
        clearButton = (Button) findViewById(R.id.button_clear);
        clearButton.setVisibility(RelativeLayout.INVISIBLE);
        clearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                if(onClickClearListener != null) onClickClearListener.onClick(v);
            }
        });
    }

    /**
     * Get value
     * @return text
     */
    public Editable getText() {
        return editText.getText();
    }

    /**
     * Set value
     * @param text
     */
    public void setText(String text) {
        editText.setText(text);
    }

    /**
     * Set OnClickListener, making EditText unfocusable
     * @param listener
     */
    @Override
    public void setOnClickListener(OnClickListener listener) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setOnClickListener(listener);
    }

    /**
     * Set listener to be fired after EditText is cleared
     * @param listener
     */
    public void setOnClearListener(OnClickListener listener) {
        onClickClearListener = listener;
    }
}
