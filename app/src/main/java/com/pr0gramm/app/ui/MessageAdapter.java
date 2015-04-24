package com.pr0gramm.app.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;
import com.pr0gramm.app.R;
import com.pr0gramm.app.api.pr0gramm.response.Message;
import com.squareup.picasso.Picasso;

import java.util.List;

import roboguice.RoboGuice;

/**
 * Created by oliver on 24.04.15.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private final List<Message> messages;
    private final Context context;
    private final Picasso picasso;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = ImmutableList.copyOf(messages);
        this.picasso = RoboGuice.getInjector(context).getInstance(Picasso.class);
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inbox_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messages.get(position);

        // set the type. if we have no thumbnail, we have a private message
        holder.type.setText(message.getThumb() != null
                ? context.getString(R.string.inbox_message_comment)
                : context.getString(R.string.inbox_message_private));

        // the text of the message
        holder.text.setText(message.getMessage());
        Linkify.addLinks(holder.text, Linkify.WEB_URLS);

        if (message.getThumb() != null) {
            holder.image.setVisibility(View.VISIBLE);

            String url = "http://thumb.pr0gramm.com/" + message.getThumb();
            picasso.load(url).into(holder.image);
        } else {
            holder.image.setVisibility(View.GONE);
            picasso.cancelRequest(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        final TextView text;
        final TextView type;
        final ImageView image;

        public MessageViewHolder(View itemView) {
            super(itemView);

            text = (TextView) itemView.findViewById(R.id.message_text);
            type = (TextView) itemView.findViewById(R.id.message_type);
            image = (ImageView) itemView.findViewById(R.id.message_image);
        }
    }
}
