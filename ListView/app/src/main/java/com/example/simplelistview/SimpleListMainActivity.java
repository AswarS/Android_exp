package com.example.simplelistview;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.*;

import android.app.AlertDialog;
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

import com.example.simplelistview.S.Book;
import com.example.simplelistview.S.Databunk;

import java.util.ArrayList;
import java.util.List;

public class SimpleListMainActivity extends AppCompatActivity {

    private static final int CONTEXT_MENU_ITEM_NEW = 1;
    private static final int CONTEXT_MENU_ITEM_APPEND = CONTEXT_MENU_ITEM_NEW+1;
    private static final int CONTEXT_MENU_ITEM_UPDATE = CONTEXT_MENU_ITEM_APPEND+1;
    private static final int CONTEXT_MENU_ITEM_DELETE = CONTEXT_MENU_ITEM_UPDATE+1;

    private static final int REQUEST_CODE_ADD_CAT = 100;
    private static final int REQUEST_CODE_UPDATE_CAT = 101;

    private final String[] data = {"软件项目管理案例教程（第4版）", "创新工程实践", "信息安全数学基础（第2版）"};
    private final int[] imageData={R.drawable.book_2,R.drawable.book_no_name,R.drawable.book_1};
    private Databunk databunk;
    private ArrayList<Book> arrayListBooks;
    private CatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_simple_list);

        initData();

        initView();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(v==findViewById(R.id.listview_books)) {
            menu.setHeaderTitle("操作");
            menu.add(1, CONTEXT_MENU_ITEM_NEW, 1, "新增");
            menu.add(1, CONTEXT_MENU_ITEM_APPEND, 1, " 追加");
            menu.add(1, CONTEXT_MENU_ITEM_UPDATE, 1, "修改");
            menu.add(1, CONTEXT_MENU_ITEM_DELETE, 1, "删除");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ADD_CAT:
                if (resultCode == RESULT_OK) {
                    String cat_name = data.getStringExtra("cat_name");
                    int position=data.getIntExtra("cat_position",0);
                    arrayListBooks.add(position,new Book(cat_name,R.drawable.book_no_name));
                    databunk.save();
                    adapter.notifyDataSetChanged();
                }
                break;
            case REQUEST_CODE_UPDATE_CAT:
                if (resultCode == RESULT_OK) {
                    String cat_name = data.getStringExtra("cat_name");
                    int position=data.getIntExtra("cat_position",0);
                    arrayListBooks.get(position).setName(cat_name);
                    databunk.save();
                    adapter.notifyDataSetChanged();
                }
                break;
            default:
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent;
        final int position=menuInfo.position;
        switch(item.getItemId())
        {
            case CONTEXT_MENU_ITEM_NEW:
                intent = new Intent(SimpleListMainActivity.this, InputUpdateActivity.class);
                intent.putExtra("position",position);
                startActivityForResult(intent, REQUEST_CODE_ADD_CAT);
                break;
            case CONTEXT_MENU_ITEM_APPEND:
                intent = new Intent(SimpleListMainActivity.this, InputUpdateActivity.class);
                intent.putExtra("position",position+1);
                startActivityForResult(intent, REQUEST_CODE_ADD_CAT);
                break;
            case CONTEXT_MENU_ITEM_UPDATE:
                intent = new Intent(SimpleListMainActivity.this, InputUpdateActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("cat_name", arrayListBooks.get(position).getName() );
                startActivityForResult(intent, REQUEST_CODE_UPDATE_CAT);
                break;
            case CONTEXT_MENU_ITEM_DELETE:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("询问");
                builder.setMessage("你确定要删除\""+ arrayListBooks.get(position).getName() + "\"？");
                builder.setCancelable(true);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        arrayListBooks.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });  //正面的按钮（肯定）
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }); //反面的按钮（否定)
                builder.create().show();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void initView() {
        adapter = new CatAdapter(this, R.layout.item_cat, arrayListBooks);

        ListView listViewCats=findViewById(R.id.listview_books);
        listViewCats.setAdapter(adapter);

        this.registerForContextMenu(listViewCats);
    }

    private void initData() {
        databunk=new Databunk(this);
        databunk.load();
        arrayListBooks =databunk.getCat();
        arrayListBooks.add(new Book("软件项目管理案例教程（第4版）", R.drawable.book_2));
        arrayListBooks.add(new Book("创新工程实践", R.drawable.book_no_name));
        arrayListBooks.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1));
    }


    private class CatAdapter extends ArrayAdapter<Book> {
        private int resourceId;

        public CatAdapter(@NonNull Context context, int resource, @NonNull List<Book> objects) {
            super(context, resource,objects);
            this.resourceId=resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Book book = getItem(position);//获取当前项的实例
            View view;
            if(null==convertView)
                view = LayoutInflater.from(getContext()).inflate(this.resourceId, parent, false);
            else
                view=convertView;
            ((ImageView) view.findViewById(R.id.image_view_cat)).setImageResource(book.getImageResourceId());
            ((TextView) view.findViewById(R.id.text_view_name_cat)).setText(book.getName());
            return view;
        }
    }
}