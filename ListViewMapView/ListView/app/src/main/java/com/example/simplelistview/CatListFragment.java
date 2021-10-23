package com.example.simplelistview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import com.example.simplelistview.dataprocessor.Cat;
import com.example.simplelistview.dataprocessor.DataBank;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CatListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CatListFragment extends Fragment {

    private DataBank dataBank;
    private CatAdapter adapter;
    private static final int CONTEXT_MENU_ITEM_NEW = 1;
    private static final int CONTEXT_MENU_ITEM_APPEND = CONTEXT_MENU_ITEM_NEW+1;
    private static final int CONTEXT_MENU_ITEM_UPDATE = CONTEXT_MENU_ITEM_APPEND+1;
    private static final int CONTEXT_MENU_ITEM_DELETE = CONTEXT_MENU_ITEM_UPDATE+1;

    private static final int REQUEST_CODE_ADD_CAT = 100;
    private static final int REQUEST_CODE_UPDATE_CAT = 101;


    public CatListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(v==this.getActivity().findViewById(R.id.listview_books)) {
            menu.setHeaderTitle("操作");
            menu.add(1, CONTEXT_MENU_ITEM_NEW, 1, "新增");
            menu.add(1, CONTEXT_MENU_ITEM_APPEND, 1, " 追加");
            menu.add(1, CONTEXT_MENU_ITEM_UPDATE, 1, "修改");
            menu.add(1, CONTEXT_MENU_ITEM_DELETE, 1, "删除");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {

            case REQUEST_CODE_ADD_CAT:
                if (resultCode == RESULT_OK) {
                    String cat_name = data.getStringExtra("cat_name");
                    int position=data.getIntExtra("cat_position",0);
                    dataBank.getCats().add(position,new Cat(cat_name, R.drawable.cat2));
                    dataBank.Save();
                    adapter.notifyDataSetChanged();
                }
                break;

            case REQUEST_CODE_UPDATE_CAT:
                if (resultCode == RESULT_OK) {
                    String cat_name = data.getStringExtra("cat_name");
                    int position=data.getIntExtra("cat_position",0);
                    dataBank.getCats().get(position).setName(cat_name);
                    dataBank.Save();
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
                intent = new Intent(this.getContext(), InputUpdateActivity.class);
                intent.putExtra("position",position);
                startActivityForResult(intent, REQUEST_CODE_ADD_CAT);

                break;
            case CONTEXT_MENU_ITEM_APPEND:
                intent = new Intent(this.getContext(), InputUpdateActivity.class);
                intent.putExtra("position",position+1);
                startActivityForResult(intent, REQUEST_CODE_ADD_CAT);
                break;
            case CONTEXT_MENU_ITEM_UPDATE:
                intent = new Intent(this.getContext(), InputUpdateActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("cat_name",dataBank.getCats().get(position).getName() );
                startActivityForResult(intent, REQUEST_CODE_UPDATE_CAT);
                break;
            case CONTEXT_MENU_ITEM_DELETE:

                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                builder.setTitle("询问");
                builder.setMessage("你确定要删除\""+dataBank.getCats().get(position).getName() + "\"？");
                builder.setCancelable(true);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataBank.getCats().remove(position);
                        dataBank.Save();
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_cat_list, container, false);
        initData();
        initView(view);

        return view;
    }

    private void initView(View view) {
        adapter = new CatAdapter(this.getContext(), R.layout.item_cat,  dataBank.getCats());

        ListView listViewCats=view.findViewById(R.id.listview_books);
        listViewCats.setAdapter(adapter);

        this.registerForContextMenu(listViewCats);
    }

    private void initData() {
        dataBank=new DataBank(this.getContext());
        dataBank.Load();
        if(0==dataBank.getCats().size())
        {
            dataBank.getCats().add(new Cat("请添加一个猫",R.drawable.cat1));
        }
    }



    private class CatAdapter extends ArrayAdapter<Cat> {
        private int resourceId;

        public CatAdapter(@NonNull Context context, int resource, @NonNull List<Cat> objects) {
            super(context, resource,objects);
            this.resourceId=resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Cat cat = getItem(position);//获取当前项的实例
            View view;
            if(null==convertView)
                view = LayoutInflater.from(getContext()).inflate(this.resourceId, parent, false);
            else
                view=convertView;
            assert cat != null;
            ((ImageView) view.findViewById(R.id.image_view_cat)).setImageResource(cat.getImageResourceId());
            ((TextView) view.findViewById(R.id.text_view_name_cat)).setText(cat.getName());
            return view;
        }
    }
}