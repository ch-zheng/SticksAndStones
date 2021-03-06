package com.afrikappakorps.sticksandstones;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.afrikappakorps.sticksandstones.data.SticksAndStonesContract;

public class AddPlayersAdapter extends RecyclerView.Adapter<AddPlayersAdapter.SticksAdapterViewHolder> {
    private Context mContext;
    private Cursor mPlayerData;

    public AddPlayersAdapter(Context context) {
        mContext = context;
    }

    @Override
    public SticksAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.item_playername, parent, false);
        return new SticksAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SticksAdapterViewHolder holder, int position) {
        if (mPlayerData.moveToPosition(position)) {
            holder.playerName.setText(mPlayerData.getString(mPlayerData.getColumnIndex(SticksAndStonesContract.PlayerEntry.COLUMN_PLAYER_NAME)));
            holder.itemView.setTag(mPlayerData.getInt(mPlayerData.getColumnIndex(SticksAndStonesContract.PlayerEntry._ID)));
        }
    }

    class SticksAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView playerName;

        SticksAdapterViewHolder(View view) {
            super(view);
            playerName = view.findViewById(R.id.player_name);
        }
    }

    @Override
    public int getItemCount() {
        if (mPlayerData != null) {
            return mPlayerData.getCount();
        } else {
            return 0;
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        final Cursor oldCursor = mPlayerData;
        mPlayerData = newCursor;
        notifyDataSetChanged();
        return oldCursor;
    }
}
