package com.codepath.simpletodo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.R.attr.data;
import static android.R.attr.id;
import static android.R.attr.name;
import static com.codepath.simpletodo.Items_Table.item;
import static com.raizlabs.android.dbflow.sql.language.property.PropertyFactory.from;

public class MainActivity extends AppCompatActivity {

    public final int REQUEST_CODE = 20;

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems=(ListView)findViewById(R.id.lvItems);
        items=new ArrayList<>();

        readItems();
        itemsAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
        setupListEditListener();
    }

    public void onAddItem(View v) {

        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        itemsAdapter.add(etNewItem.getText().toString());

        Items itemsDB = new Items();
        itemsDB.setId(getRandomId());
        itemsDB.setItem(etNewItem.getText().toString());
        itemsDB.save();

        etNewItem.setText("");
        etNewItem.clearFocus();

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    public void setupListViewListener() {

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter,View item1, int pos, long id) {

                SQLite.delete().from(Items.class).where(item.is(items.get(pos).toString())).execute();
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    public void readItems(){

        int i;

        List<Items> itemsList = SQLite.select().from(Items.class).queryList();
        for(i=0;i<itemsList.size();i++){
            Items itemsFromDB = itemsList.get(i);
            items.add(i,itemsFromDB.getItem());
        }
    }

   /* public void writeItems(){
       File filesDir = getFilesDir();
        File todoFile = new File(filesDir,"todo.txt");
        try{
            FileUtils.writeLines(todoFile,items);
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    public void setupListEditListener() {

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("Item",items.get(pos));
                i.putExtra("code",2);
                i.putExtra("pos",pos);
                startActivityForResult(i,REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent editedData) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String itemstr = editedData.getStringExtra("Edited_Item");
            int pos = editedData.getExtras().getInt("position");
            int code = editedData.getExtras().getInt("code");

            if(code!=200)
            {
                Toast.makeText(this,"Sorry!Please Try Again",Toast.LENGTH_LONG).show();
            }
            else
            {
                SQLite.update(Items.class).set(item.eq(itemstr)).where(item.eq(items.get(pos))).execute();
                items.set(pos,itemstr);
                itemsAdapter.notifyDataSetChanged();
            }
        }
    }

    public int getRandomId()
    {
        Random r = new Random();
        int id = r.nextInt(1000+1);

        return id;
    }
}
