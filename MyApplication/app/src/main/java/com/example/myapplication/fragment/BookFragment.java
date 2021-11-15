package com.example.myapplication.fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.InputUpdateActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.entity.Book;
import com.example.myapplication.entity.DataBunk;

import java.util.ArrayList;

public class BookFragment extends Fragment {

    private MyAdapter adapter;
    private ArrayList<Book> arrayListBooks;
    private DataBunk dataBunk;

    private static final int MENU_NEW = 1;
    private static final int MENU_APPEND = MENU_NEW +1;
    private static final int MENU_UPDATE = MENU_APPEND +1;
    private static final int MENU_DELETE = MENU_UPDATE +1;

    private static final int REQUEST_ADD = 100;
    private static final int REQUEST_UPDATE = 101;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        initDate();
        initView(view);

        return view;
    }

    private void initDate(){
        dataBunk=new DataBunk(this.getContext());
        dataBunk.load();
        arrayListBooks =dataBunk.getBook();
    }

    private void initView(View view){
        RecyclerView recyclerView = view.findViewById(R.id.recycle_view_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));

        adapter = new MyAdapter(arrayListBooks);
        //设置适配器
        recyclerView.setAdapter(adapter);
        recyclerView.setLongClickable(true);
        this.registerForContextMenu(recyclerView);
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private final ArrayList<Book> dataList;
        private int position;

        public int getPosition(){
            return position;
        }

        public void setPosition(int position){
            this.position = position;
        }

        MyAdapter(ArrayList<Book> dataList) {
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Book book = dataList.get(position);
            holder.cover.setImageResource(book.getResourceId());
            holder.title.setText(book.getTitle());

            holder.itemView.setOnLongClickListener(v -> {
                setPosition(holder.getLayoutPosition());
                return false;
            });
        }

        @Override
        public int getItemCount() {
            return dataList == null ? 0 : dataList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
            private final TextView title;
            private final ImageView cover;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) this);
                cover = itemView.findViewById(R.id.image_view_book_cover);
                title = itemView.findViewById(R.id.text_view_book_title);
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("操作");
                menu.add(1, MENU_NEW, 1, "新增");
                menu.add(1, MENU_UPDATE, 1, "修改");
                menu.add(1, MENU_DELETE, 1, "删除");
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_ADD:
                if (resultCode == RESULT_OK) {
                    assert data != null;
                    String book_title = data.getStringExtra("book_title");
                    int position=data.getIntExtra("book_position",0);
                    arrayListBooks.add(position,new Book(book_title,R.drawable.book_no_name));
                    dataBunk.save();
                    adapter.notifyDataSetChanged();
                }
                break;
            case REQUEST_UPDATE:
                if (resultCode == RESULT_OK) {
                    assert data != null;
                    String book_title = data.getStringExtra("book_title");
                    int position=data.getIntExtra("book_position",0);
                    arrayListBooks.get(position).setTitle(book_title);
                    dataBunk.save();
                    adapter.notifyDataSetChanged();
                }
                break;
            default:
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("NotifyDataSetChanged")
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Intent intent;
        final int position=adapter.position;
        switch(item.getItemId())
        {
            case MENU_NEW:
                intent = new Intent(this.getContext(), InputUpdateActivity.class);
                intent.putExtra("position",position);
                startActivityForResult(intent, REQUEST_ADD);
                break;
            case MENU_UPDATE:
                intent = new Intent(this.getContext(), InputUpdateActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("book_title", arrayListBooks.get(position).getTitle() );
                startActivityForResult(intent, REQUEST_UPDATE);
                break;
            case MENU_DELETE:
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                builder.setTitle("询问");
                builder.setMessage("你确定要删除\""+ arrayListBooks.get(position).getTitle() + "\"？");
                builder.setCancelable(true);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        arrayListBooks.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
}