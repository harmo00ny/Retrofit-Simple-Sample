package com.marysugar.retrofit_simple_sample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private List<Repo> rowDataList;

    MainAdapter(List<Repo> rowDataList) {
        this.rowDataList = rowDataList;
    }

    /**
     * 一行分のデータ
     */
    static class MainViewHolder extends RecyclerView.ViewHolder {
        TextView hogeTitle;

        MainViewHolder(@NonNull View itemView) {
            super(itemView);
            hogeTitle = itemView.findViewById(R.id.hoge_title_text_view);
        }
    }

    /**
     * ViewHolder作るメソッド
     * 最初しか呼ばれない。
     * ここでViewHolderのlayoutファイルをインフレーとして生成したViewHolderをRecyclerViewに返す。
     */
    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_main, parent, false);
        return new MainViewHolder(view);
    }

    /**
     * ViewHolderとRecyclerViewをバインドする
     * 一行のViewに対して共通でやりたい処理をここで書く。今回はテキストのセットしかしてないけど。
     */
    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        Repo rowData = this.rowDataList.get(position);
        holder.hogeTitle.setText(rowData.id.toString());
    }

    /**
     * リストの行数
     */
    @Override
    public int getItemCount() {
        return rowDataList.size();
    }
}
