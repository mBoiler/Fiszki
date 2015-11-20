package eu.qm.fiszki.activity;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import eu.qm.fiszki.Alert;
import eu.qm.fiszki.Checker;
import eu.qm.fiszki.R;
import eu.qm.fiszki.database.DBAdapter;
import eu.qm.fiszki.database.DBModel;
import eu.qm.fiszki.database.DBStatus;

public class LearningModeActivity extends AppCompatActivity {

    TextView word;
    EditText enteredWord;
    DBAdapter myDb = new DBAdapter(this);
    DBStatus OpenDataBase = new DBStatus();

    String wordFromData;
    String expectedWord;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        OpenDataBase.openDB(myDb);
        Cursor c = myDb.getRandomRow();
        wordFromData = c.getString(c.getColumnIndex(DBModel.KEY_WORD));
        expectedWord = c.getString(c.getColumnIndex(DBModel.KEY_TRANSLATION));
        enteredWord = (EditText) findViewById(R.id.EnteredWord);
        enteredWord.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        word = (TextView) findViewById(R.id.textView3);
        word.append(wordFromData);
        enteredWord.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_learning_mode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        Alert message = new Alert();
        Checker check = new Checker();
        if (id == R.id.action_OK)
        {
            if(check.Check(expectedWord, enteredWord.getText().toString()))
            {
                message.learningModePass(this, getString(R.string.alert_message_pass), getString(R.string.alert_title_pass), getString(R.string.alert_nameButton_OK));
            }
            else
            {
                message.learningModeFail(this, expectedWord, getString(R.string.alert_message_fail), getString(R.string.alert_title_fail), getString(R.string.alert_nameButton_OK));
            }
        } else if (id == R.id.learningMode_stop)
        {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
