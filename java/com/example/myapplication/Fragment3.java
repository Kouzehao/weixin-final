package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment3 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    // 添加 MyDAO 作为成员变量
    private MyDAO myDAO;

    public Fragment3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment3 newInstance(String param1, String param2) {
        Fragment3 fragment = new Fragment3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        myDAO=new MyDAO(getContext());
        myDAO.connectDB();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 首先，通过 inflater 加载布局，然后在这个布局上查找视图
        View view = inflater.inflate(R.layout.tab3, container, false);

        // 现在可以使用 view 变量来查找布局中的按钮
        Button button1 = view.findViewById(R.id.contact_button1);
        Button button2 = view.findViewById(R.id.contact_button2);
        Button button3 = view.findViewById(R.id.contact_button3);


        // 为按钮设置点击事件监听器
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理 button1 的点击事件
                showDialogToAddContact();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理 button2 的点击事件
                showUpdateContactDialog();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理 button3 的点击事件
                showDeleteContactDialog();
            }
        });

        // 返回加载的视图
        return view;
    }
    private void showDialogToAddContact() {
        // 获取LayoutInflater用于从XML布局文件加载视图
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // 使用自定义布局文件activity_contact来创建视图
        View dialogView = inflater.inflate(R.layout.activity_contact, null);

        // 构建一个AlertDialog.Builder，用于创建弹出对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("添加联系人") // 设置对话框标题
                .setView(dialogView) // 将加载的视图设置为对话框内容
                .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取对话框中的EditText组件
                        EditText editTextName = dialogView.findViewById(R.id.editTextName);
                        EditText editTextAge = dialogView.findViewById(R.id.editTextAge);
                        EditText editTextGender = dialogView.findViewById(R.id.editTextGender);

                        // 从EditText获取用户输入的数据
                        String name = editTextName.getText().toString();
                        int age = Integer.parseInt(editTextAge.getText().toString());
                        String gender = editTextGender.getText().toString();

                        // 插入数据到数据库
                        Log.d("xr", String.valueOf(getContext()));
                        myDAO.myInsert(name, age, gender); // 调用数据库操作方法插入数据
                    }
                })
                .setNegativeButton("取消", null); // 设置一个“取消”按钮

        // 创建并显示AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showUpdateContactDialog() {
        // 使用LayoutInflater来加载XML布局文件
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // 加载自定义布局文件activity_contact2来创建视图
        View dialogView = inflater.inflate(R.layout.activity_contact2, null);

        // 构建一个AlertDialog.Builder，用于创建弹出对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("更新联系人") // 设置对话框的标题
                .setView(dialogView) // 设置对话框的自定义视图
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取对话框中的EditText组件
                        EditText editTextId = dialogView.findViewById(R.id.editTextId);
                        EditText editTextName = dialogView.findViewById(R.id.editTextNameUpdate);
                        EditText editTextAge = dialogView.findViewById(R.id.editTextAgeUpdate);
                        EditText editTextGender = dialogView.findViewById(R.id.editTextGenderUpdate);

                        // 从EditText获取用户输入的数据
                        int id = Integer.parseInt(editTextId.getText().toString());
                        String name = editTextName.getText().toString();
                        int age = Integer.parseInt(editTextAge.getText().toString());
                        String gender = editTextGender.getText().toString();

                        // 调用数据库操作方法来更新数据
                        myDAO.myUpdate(id, name, age, gender);
                    }
                })
                .setNegativeButton("取消", null); // 设置一个“取消”按钮

        // 创建并显示AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteContactDialog() {
        // 使用LayoutInflater来加载XML布局文件
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // 加载自定义布局文件activity_contact3来创建视图
        View dialogView = inflater.inflate(R.layout.activity_contact3, null);

        // 构建一个AlertDialog.Builder，用于创建弹出对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("删除联系人") // 设置对话框的标题
                .setView(dialogView) // 设置对话框的自定义视图
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取对话框中的EditText组件
                        EditText editTextId = dialogView.findViewById(R.id.editTextIdDelete);
                        // 从EditText获取用户输入的ID
                        int id = Integer.parseInt(editTextId.getText().toString());

                        // 调用数据库操作方法来删除指定ID的联系人
                        myDAO.myDelete(id);
                    }
                })
                .setNegativeButton("取消", null); // 设置一个“取消”按钮

        // 创建并显示AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}