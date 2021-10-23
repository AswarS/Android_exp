package com.jnu.listview;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.jnu.listview.S.Book;
import com.jnu.listview.S.Databunk;

import java.util.ArrayList;
import java.util.List;


public class BookApplication extends Application {
    private static final int CONTEXT_MENU_ITEM_NEW = 1;
    private static final int CONTEXT_MENU_ITEM_APPEND = CONTEXT_MENU_ITEM_NEW + 1;
    private static final int CONTEXT_MENU_ITEM_UPDATE = CONTEXT_MENU_ITEM_APPEND + 1;
    private static final int CONTEXT_MENU_ITEM_DELETE = CONTEXT_MENU_ITEM_UPDATE + 1;

    private static final int REQUEST_CODE_ADD_BOOK = 100;
    private static final int REQUEST_CODE_UPDATE_BOOK = 101;

    private ArrayList<Book> arraylistBooks;
    private Databunk databunk;

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        if( v==findViewById(R.id.listview_books)){
            menu.setHeaderTitle("Operation");
            menu.add(1, CONTEXT_MENU_ITEM_NEW, 1, "ADD");
            menu.add(1, CONTEXT_MENU_ITEM_APPEND, 1, "APPEND");
            menu.add(1, CONTEXT_MENU_ITEM_UPDATE, 1, "UPDATE");
            menu.add(1, CONTEXT_MENU_ITEM_DELETE, 1, "DELETE");
        }
    }

    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Intent intent;
        final int position = menuInfo.position;
        switch (item.getItemId())
        {
            case CONTEXT_MENU_ITEM_NEW:
                intent = new Intent(BookListMainActivity.this, InputUpdateActivity.class);
                intent.putExtra("position", position);
                startActivityForResult(intent, REQUEST_CODE_ADD_BOOK);
                break;
            case CONTEXT_MENU_ITEM_APPEND:
                intent = new Intent(BookListMainActivity.this, InputUpdateActivity.class);
                intent.putExtra("position", position+1);
                startActivityForResult(intent, REQUEST_CODE_ADD_BOOK);
                break;
            case CONTEXT_MENU_ITEM_UPDATE:
                intent = new Intent(this.getContext(), InputUpdateActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("book_name", arraylistBooks.get(position).getName());
                startActivityForResult(intent, REQUEST_CODE_UPDATE_BOOK);
                break;
            case CONTEXT_MENU_ITEM_DELETE:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Quest");
                builder.setMessage("Are u ☼sure to delete this?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        arraylistBooks.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ADD_BOOK:
                if(RESULT_OK == resultCode){
                    String book_name = data.getStringExtra("book_name");
                    int position = data.getIntExtra("book_position", 0);
                    arraylistBooks.add(position, new Book(book_name, R.drawable.book_2));
                    databunk.Save();
                    adapter.notifyDataSetChanged();
                }
                break;
            case REQUEST_CODE_UPDATE_BOOK:
                if(RESULT_OK == resultCode){
                    String book_name = data.getStringExtra("book_name");
                    int position = data.getIntExtra("book_position", 0);
                    arraylistBooks.get(position).setName(book_name);
                    databunk.Save();
                    adapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        //BookAdapter bookAdapter = new BookAdapter(BookListMainActivity.this, R.layout.item_book, books);
        //((ListView) findViewById(R.id.listview_books)).setAdapter(bookAdapter);

        adapter = new BookListMainActivity.BookAdapter(this, R.layout.item_book, arraylistBooks);
        ListView listViewBooks = findViewById(R.id.listview_books);
        listViewBooks.setAdapter(adapter);
        this.registerForContextMenu(listViewBooks);
    }

    public void initData(){
        databunk = new Databunk(this);
        databunk.Load();
        arraylistBooks = databunk.getBookArrayList();
        if(arraylistBooks.size()==0){
            arraylistBooks.add(new Book(getString(R.string.book_2), R.drawable.book_2));
            arraylistBooks.add(new Book(getString(R.string.book_no_name), R.drawable.book_no_name));
            arraylistBooks.add(new Book(getString(R.string.book_1), R.drawable.book_1));
        }
    }

    public class BookAdapter extends ArrayAdapter<Book> {

        private int resourceId;

        public BookAdapter(@NonNull Context context, int resource, @NonNull List<Book> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            Book book = getItem(position);  //获取实例
            @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            assert book != null;
            ((ImageView) view.findViewById(R.id.image)).setImageResource(book.getImage_id());   //将资源与布局绑定
            ((TextView) view.findViewById(R.id.name)).setText(book.getName());
            return view;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_cat_list, container, false);
        initData();
        initView(view);

        return view;
    }
}
