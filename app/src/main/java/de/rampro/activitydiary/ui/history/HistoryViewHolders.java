/*
 * ActivityDiary
 *
 * Copyright (C) 2017-2018 Raphael Mack http://www.raphael-mack.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.rampro.activitydiary.ui.history;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Logger;

import de.rampro.activitydiary.R;
import de.rampro.activitydiary.ui.generic.DetailRecyclerViewAdapter;

class HistoryViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private int mDiaryEntryId;
    private int mLoaderId = -1;

    public CardView mMainView;
    public TextView mHeader;
    public TextView mStartLabel;
    public TextView mDurationLabel;
    public TextView mNoteLabel;
    public ImageView mSymbol;
    public ImageView mInsertBtn;
    public ImageView mResizeBtn;
    public CardView mActivityCardView;
    public TextView mName;
    public View mBackground;
    public DetailRecyclerViewAdapter mDetailAdapter;
    public RecyclerView mImageRecycler;
    private HistoryRecyclerViewAdapter.SelectListener mListener;

    public HistoryViewHolders(int loaderId, HistoryRecyclerViewAdapter.SelectListener listener, View itemView) {
        super(itemView);
        mLoaderId = loaderId;

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        mMainView = (CardView) itemView.findViewById(R.id.card_view);
        mHeader = (TextView) itemView.findViewById(R.id.header);
        mStartLabel = (TextView) itemView.findViewById(R.id.start_label);
        mNoteLabel = (TextView) itemView.findViewById(R.id.note);
        mDurationLabel = (TextView) itemView.findViewById(R.id.duration_label);
        mSymbol = (ImageView) itemView.findViewById(R.id.picture);
        mActivityCardView = (CardView) itemView.findViewById(R.id.activity_card);
        mName = (TextView) itemView.findViewById(R.id.activity_name);
        mBackground = itemView.findViewById(R.id.activity_background);
        mImageRecycler = (RecyclerView)itemView.findViewById(R.id.image_grid);
        mInsertBtn = (ImageView)itemView.findViewById(R.id.insertButton);
        mInsertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    mListener.onInsertItemClick(HistoryViewHolders.this, position, mDiaryEntryId);
                }
            }
        });
        mResizeBtn = (ImageView)itemView.findViewById(R.id.resizeButton);

        mResizeBtn.setOnTouchListener(new View.OnTouchListener() {
            private float mPointerOffset;

            @Override
            public boolean onTouch(View v, MotionEvent me) {
                if (me.getAction() == MotionEvent.ACTION_DOWN) {
/*                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(null, shadowBuilder, v, 0);
                    */
                    mPointerOffset = me.getRawY();// - getPrimaryContentSize();
                    return true;
                } else if (me.getAction() == MotionEvent.ACTION_MOVE) {
/*                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(null, shadowBuilder, v, 0);
                    */
                    //if (getOrientation() == VERTICAL) {

                    float delta =( (int)(me.getRawY() - mPointerOffset));
                    mPointerOffset = me.getRawY();// - getPrimaryContentSize();

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mMainView.getLayoutParams();

                    params.height += delta;
                    // set the primary content parameter to do not stretch anymore and
                    // use the height specified in the layout params
//                        params.weight = 0;
                    mMainView.setLayoutParams(params);
                    mDurationLabel.setText(Float.toString(params.height));

                    Log.i("Resize", Float.toString(delta) + " " + me + " " + me.getRawY());
//                    } else {
//                        setPrimaryContentWidth( (int)(me.getRawX() - mPointerOffset));
//                    }
                        return true;
                } else {
                    return true;
                }
            }

        });

        mResizeBtn.setOnDragListener(new View.OnDragListener() {
            float startPos = 0.0f;
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if(event.getAction() == DragEvent.ACTION_DRAG_LOCATION){
                    float delta = event.getY() - startPos;
                    Log.i("Resize", Float.toString(delta));
                    if(delta > 0)
                        delta = -delta;
                }else if(event.getAction() == DragEvent.ACTION_DRAG_STARTED){
                    startPos = event.getY();
                }else if(event.getAction() == DragEvent.ACTION_DRAG_EXITED){

                }
                return true;
            }
        });
        mListener = listener;
    }

    public int getDiaryEntryID(){
        return mDiaryEntryId;
    }

    public void setDiaryEntryID(int id){
        mDiaryEntryId = id;
    }

    public int getDetailLoaderID(){
        return mLoaderId;
    }

    @Override
    public void onClick(View view) {
        final int position = getAdapterPosition();
        if(position != RecyclerView.NO_POSITION) {
            mListener.onItemClick(this, position, mDiaryEntryId);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        final int position = getAdapterPosition();
        if(position != RecyclerView.NO_POSITION) {
            return mListener.onItemLongClick(this, position, mDiaryEntryId);
        }
        return false;
    }

}
