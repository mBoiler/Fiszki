package eu.qm.fiszki;

import android.app.Activity;
import android.widget.EditText;

import java.util.ArrayList;

import eu.qm.fiszki.model.Flashcard;
import eu.qm.fiszki.model.FlashcardRepository;

/**
 * Created by mBoiler on 11.02.2016.
 */
public class Rules {

    Alert alert;
    FlashcardRepository flashcardRepository;

    public boolean addNewWordRule(EditText orginalWord, EditText translateWord, Activity activity,
                                  int categoryId) {
        alert = new Alert();
        flashcardRepository = new FlashcardRepository(activity.getBaseContext());
        if (orginalWord.getText().toString().isEmpty() || translateWord.getText().toString().isEmpty()) {
            alert.buildAlert(activity.getString(R.string.alert_title),
                    activity.getString(R.string.alert_message_onEmptyFields),
                    activity.getString(R.string.button_action_ok), activity);
            return false;
        }
        if (flashcardRepository.getFlashcardByName(orginalWord.getText().toString())!=null) {
            ArrayList<Flashcard> flashcards = flashcardRepository.getFlashcardsByCategoryID(categoryId);
            for (Flashcard flashcard:flashcards) {
                if (flashcard.getWord().equals(orginalWord.getText().toString())) ;
                alert.buildAlert(activity.getString(R.string.alert_title),
                        activity.getString(R.string.alert_message_onRecordExist),
                        activity.getString(R.string.button_action_ok), activity);
                orginalWord.setText(null);
                translateWord.setText(null);
                orginalWord.requestFocus();
                return false;
            }
            return true;
        }
        return true;
    }
}
