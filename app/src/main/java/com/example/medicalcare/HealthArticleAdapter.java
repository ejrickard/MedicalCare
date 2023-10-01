package com.example.medicalcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class HealthArticleAdapter extends RecyclerView.Adapter<HealthArticleAdapter.ViewHolder> {

    Context context;
    List<Map<String, String>> data;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public HealthArticleAdapter(Context context, List<Map<String, String>> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.multi_lines, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> item = data.get(position);
        holder.lineA.setText(item.get("line1"));
        holder.lineB.setText(item.get("line2"));
        holder.lineC.setText(item.get("line3"));
        holder.lineD.setText(item.get("line4"));
        holder.lineE.setText(item.get("line5"));

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView lineA, lineB, lineC, lineD, lineE;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            lineA = itemView.findViewById(R.id.line_a);
            lineB = itemView.findViewById(R.id.line_b);
            lineC = itemView.findViewById(R.id.line_c);
            lineD = itemView.findViewById(R.id.line_d);
            lineE = itemView.findViewById(R.id.line_e);
        }
    }
}

