package eu.qm.fiszki.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.shamanland.fab.ShowHideOnScroll;


import eu.qm.fiszki.ListPopulate;
import eu.qm.fiszki.R;
import eu.qm.fiszki.database.DBAdapter;
import eu.qm.fiszki.database.DBStatus;
import eu.qm.fiszki.database.DBTransform;
import eu.qm.fiszki.model.Category;
import eu.qm.fiszki.model.CategoryRepository;
import eu.qm.fiszki.model.Flashcard;
import eu.qm.fiszki.model.FlashcardRepository;
import eu.qm.fiszki.toolbar.ToolbarAfterClick;
import eu.qm.fiszki.toolbar.ToolbarMainActivity;

import com.apptentive.android.sdk.Apptentive;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    static final public String typeCategory = "TYPECATEGORY";
    static final public String typeFlashcard = "TYPEFLASHCARD";
    static public Category expandedGroup;
    static public DBAdapter myDb;
    static public DBStatus openDataBase;
    static public Context context;
    static public ExpandableListView expandableListView;
    static public FloatingActionButton fab;
    static public String selectedType;
    static public Flashcard selectedFlashcard;
    static public Category selectedCategory;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    ToolbarAfterClick toolbarAfterClick;
    ToolbarMainActivity toolbarMainActivity;
    FlashcardRepository flashcardRepository;
    DBTransform transform;
    CategoryRepository categoryRepository;
    Activity activity;
    ListPopulate listPopulate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        openDataBase = new DBStatus();
        myDb = new DBAdapter(this);
        context = this;
        expandableListView = (ExpandableListView) findViewById(R.id.list);
        openDataBase.openDB(myDb);
        flashcardRepository = new FlashcardRepository(context);
        categoryRepository = new CategoryRepository(context);
        sharedPreferences = getSharedPreferences("eu.qm.fiszki.activity", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        toolbarAfterClick = new ToolbarAfterClick(activity);
        toolbarMainActivity = new ToolbarMainActivity(activity);
        listPopulate = new ListPopulate(activity);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, AddWordActivity.class);
                startActivity(myIntent);
            }
        });
        toolbarMainActivity.set();
        selectionFlashcard();
        expandableListView.setOnTouchListener(new ShowHideOnScroll(fab));
    }

    @Override
    public void onBackPressed() {
        if (selectedFlashcard != null || selectedCategory != null) {
            toolbarMainActivity.set();
            fab.show();
            listPopulate.populate(null, null);
            selectedFlashcard = null;
            selectedCategory = null;
        } else {
            this.finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Apptentive.onStart(this);
        categoryRepository.addSystemCategory();
        transform = new DBTransform(myDb, context);
        boolean shownOnlyOnce = Apptentive.engage(this, "changelog");
        boolean shown = Apptentive.engage(this, "notes");
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbarMainActivity.set();
        listPopulate.populate(null, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void selectionFlashcard() {
        selectedFlashcard = null;
        expandableListView.setLongClickable(true);
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);

                    if (selectedFlashcard != null && selectedFlashcard.getId() == (listPopulate.adapterExp.getFlashcard(groupPosition, childPosition)).getId()) {
                        toolbarMainActivity.set();
                        fab.show();
                        listPopulate.populate(null, null);
                        selectedFlashcard = null;
                        selectedCategory = null;
                        expandedGroup = null;
                    } else {
                        selectedFlashcard =
                                listPopulate.adapterExp.getFlashcard(groupPosition, childPosition);
                        selectedCategory = null;
                        expandedGroup = listPopulate.adapterExp.getCategory(groupPosition);
                        selectedType = typeFlashcard;
                        toolbarAfterClick.set(selectedCategory, selectedFlashcard, selectedType, listPopulate);
                        fab.hide();
                        listPopulate.populate(selectedFlashcard, selectedCategory);
                    }
                }
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    if (selectedCategory != null && selectedCategory.getId() == (listPopulate.adapterExp.getCategory(groupPosition).getId())) {
                        toolbarMainActivity.set();
                        selectedCategory = listPopulate.adapterExp.getCategory(groupPosition);
                        selectedType = typeCategory;
                    }
                }
                return true;
            }
        });
    }
    }

