package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.R.id.edit;
import static com.codepath.simpletodo.R.id.etNewItem;

public class EditItemActivity extends AppCompatActivity {

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String item = getIntent().getStringExtra("Item");
        position=getIntent().getExtras().getInt("pos");
        System.out.print("......"+item+"......");

        EditText editItem = (EditText) findViewById(R.id.editText);
        editItem.setText(item);
        editItem.setSelection(item.length());
    }

    public void onSaveItem(View v) {
        EditText editItem = (EditText) findViewById(R.id.editText);

        Intent editedItem = new Intent();
        editedItem.putExtra("Edited_Item",editItem.getText().toString());
        editedItem.putExtra("position",position);
        editedItem.putExtra("code",200);

        setResult(RESULT_OK,editedItem);
        finish();

    }
}
