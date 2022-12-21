package com.k3.k3pler.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.k3.k3pler.R;
import com.k3.k3pler.sub.HTTPReq;

import java.util.ArrayList;

import io.netty.handler.codec.DecoderResult;

/** Adapter for recyclerView list [main page]  **/
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private final Context context;
    public ArrayList<HTTPReq> requests;

    public interface OnItemClickListener {
        void onItemClick(HTTPReq item, int i);
    }
    private final OnItemClickListener onItemClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txvRequest;
        public ViewHolder(View view) {
            super(view);
            txvRequest = view.findViewById(R.id.txvRequest);
        }
        public void bind(final HTTPReq item, final int i, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(item, i));
        }
    }
    public RequestAdapter(Context context, ArrayList<HTTPReq> requests, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.requests = requests;
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_requests, parent, false);
        return new ViewHolder(itemView);
    }
    @Override
    @SuppressWarnings("deprecation")
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int requestColor;
        if (requests.get(position).getBlocked())
            requestColor = ContextCompat.getColor(context, android.R.color.holo_red_dark);
        else
            requestColor = ContextCompat.getColor(context, android.R.color.white);
        String method = String.valueOf(requests.get(position).getMethod().charAt(0));
        String protocol = requests.get(position).getProtocol().replace("HTTP", "H");
        String decoderResult = getShortResult(requests.get(position).getResult());
        String htmlEntry = "<b>"+
                "<font color=\"" +
                requestColor + "\">"
                + requests.get(position).getUri() + "</font>"+"<font color=\""+
                ContextCompat.getColor(context, R.color.color2) +  "\">"
                +" ~ </font>"+"<font color=\""+
                ContextCompat.getColor(context, R.color.k3pler2) + "\">"
                + method +"</font>"+"<font color=\""+
                ContextCompat.getColor(context, R.color.color1) +  "\">"
                + " [" + protocol + "] " +"</font>"+"<font color=\""+
                ContextCompat.getColor(context, R.color.orange) +  "\">"
                + "_" + decoderResult + "_ " +"</font>"+"<font color=\""+
                ContextCompat.getColor(context, R.color.lightYellow) +  "\">"
                + " " + requests.get(position).getTime()  +"</font>"
                + "</b>";
        holder.txvRequest.setText(Html.fromHtml(htmlEntry));
        holder.bind(requests.get(position), position, onItemClickListener);
    }
    @Override
    public int getItemCount() {
        return requests.size();
    }

    private String getShortResult(DecoderResult result){
        if (result.isSuccess())
            return "S";
        else if (result.isFinished())
            return "F";
        else if (result.isFailure())
            return "X";
        else
            return "-";
    }
    public static String getLongResult(DecoderResult result){
        return result.toString();
    }


}