package com.nerdyprogrammer.studmgt;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    private List<Result> resultList;
    Context context;

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        public TextView textNo, textId, textName, textScore, textGrade;
        public Button buttonEdit;

        public ResultViewHolder(View itemView) {
            super(itemView);
            textNo = itemView.findViewById(R.id.text_no);
            textId = itemView.findViewById(R.id.text_id);
            textName = itemView.findViewById(R.id.text_name);
            textScore = itemView.findViewById(R.id.text_score);
            textGrade = itemView.findViewById(R.id.text_grade);
            buttonEdit = itemView.findViewById(R.id.button_edit);
        }
    }

    public ResultAdapter(List<Result> resultList,Context context) {
        this.resultList = resultList;
        this.context=context;
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_result, parent, false);
        return new ResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        Result result = resultList.get(position);
        holder.textNo.setText(String.valueOf(position + 1));
        holder.textId.setText(result.getId());
        holder.textName.setText(result.getName());
        holder.textScore.setText(String.valueOf(result.getScore()));
        holder.textGrade.setText(result.getGrade());

        holder.buttonEdit.setOnClickListener(v -> {
            // Implement edit functionality
            editResult(result);
        });
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }
    private void editResult(Result result) {
        // Launch an activity or show a dialog to edit the result
        // For example:
        Intent intent = new Intent(context, EditResultActivity.class);
        intent.putExtra("result", result);
        context.startActivity(intent);
    }

}

