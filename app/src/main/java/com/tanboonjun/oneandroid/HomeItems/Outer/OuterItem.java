package com.tanboonjun.oneandroid.HomeItems.Outer;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ramotion.garlandview.header.HeaderDecorator;
import com.ramotion.garlandview.header.HeaderItem;
import com.ramotion.garlandview.inner.InnerLayoutManager;
import com.ramotion.garlandview.inner.InnerRecyclerView;
import com.tanboonjun.oneandroid.HomeItems.Inner.InnerAdapter;
import com.tanboonjun.oneandroid.HomeItems.Inner.InnerData;
import com.tanboonjun.oneandroid.R;

import java.util.ArrayList;
import java.util.List;

public class OuterItem extends HeaderItem {

    private final static float AVATAR_RATIO_START = 1f;
    private final static float AVATAR_RATIO_MAX = 0.25f;
    private final static float AVATAR_RATIO_DIFF = AVATAR_RATIO_START - AVATAR_RATIO_MAX;

    private final static float ANSWER_RATIO_START = 0.75f;
    private final static float ANSWER_RATIO_MAX = 0.35f;
    private final static float ANSWER_RATIO_DIFF = ANSWER_RATIO_START - ANSWER_RATIO_MAX;

    private final static float MIDDLE_RATIO_START = 0.7f;
    private final static float MIDDLE_RATIO_MAX = 0.1f;
    private final static float MIDDLE_RATIO_DIFF = MIDDLE_RATIO_START- MIDDLE_RATIO_MAX;

    private final static float FOOTER_RATIO_START = 1.1f;
    private final static float FOOTER_RATIO_MAX = 0.35f;
    private final static float FOOTER_RATIO_DIFF = FOOTER_RATIO_START - FOOTER_RATIO_MAX;

    private final View mHeader;
    private final View mHeaderAlpha;

    private final InnerRecyclerView mRecyclerView;

    private final TextView mHeaderCaption1;

    private final View mMiddle;
    private final View mFooter;

    private final List<View> mMiddleCollapsible = new ArrayList<>(2);

    private final int m10dp;
    private final int m120dp;

    private boolean mIsScrolling;

    public OuterItem(View itemView, RecyclerView.RecycledViewPool pool) {
        super(itemView);

        // Init header
        m10dp = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dp10);
        m120dp = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dp120);

        mHeader = itemView.findViewById(R.id.header);
        mHeaderAlpha = itemView.findViewById(R.id.header_alpha);

        mHeaderCaption1 = (TextView) itemView.findViewById(R.id.header_text_1);

        mMiddle = itemView.findViewById(R.id.header_middle);
        mFooter = itemView.findViewById(R.id.header_footer);

        // Init RecyclerView
        mRecyclerView = (InnerRecyclerView) itemView.findViewById(R.id.recycler_view);
        mRecyclerView.setRecycledViewPool(pool);
        mRecyclerView.setAdapter(new InnerAdapter());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                mIsScrolling = newState != RecyclerView.SCROLL_STATE_IDLE;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                onItemScrolled(recyclerView, dx, dy);
            }
        });

        mRecyclerView.addItemDecoration(new HeaderDecorator(
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.inner_item_height),
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.inner_item_offset)));

        // Init fonts
        DataBindingUtil.bind(((FrameLayout)mHeader).getChildAt(0));
    }

    @Override
    public boolean isScrolling() {
        return mIsScrolling;
    }

    @Override
    public InnerRecyclerView getViewGroup() {
        return mRecyclerView;
    }

    @Override
    public View getHeader() {
        return mHeader;
    }

    @Override
    public View getHeaderAlphaView() {
        return mHeaderAlpha;
    }

    void setContent(@NonNull List<InnerData> innerDataList) {
        final Context context = itemView.getContext();

        final InnerData header = innerDataList.subList(0, 1).get(0);
        final List<InnerData> tail = innerDataList.subList(1, innerDataList.size());

        mRecyclerView.setLayoutManager(new InnerLayoutManager());
        ((InnerAdapter)mRecyclerView.getAdapter()).addData(tail);

        final String title1 = header.subTitle;
        mHeaderCaption1.setText(title1);
    }

    void clearContent() {
        ((InnerAdapter)mRecyclerView.getAdapter()).clearData();
    }

    private float computeRatio(RecyclerView recyclerView) {
        final View child0 = recyclerView.getChildAt(0);
        final int pos = recyclerView.getChildAdapterPosition(child0);
        if (pos != 0) {
            return 0;
        }

        final int height = child0.getHeight();
        final float y = Math.max(0, child0.getY());
        return y / height;
    }

    private void onItemScrolled(RecyclerView recyclerView, int dx, int dy) {
        final float ratio = computeRatio(recyclerView);

        final float footerRatio = Math.max(0, Math.min(FOOTER_RATIO_START, ratio) - FOOTER_RATIO_DIFF) / FOOTER_RATIO_MAX;
        final float middleRatio = Math.max(0, Math.min(MIDDLE_RATIO_START, ratio) - MIDDLE_RATIO_DIFF) / MIDDLE_RATIO_MAX;

        ViewCompat.setPivotY(mFooter, 0);
        ViewCompat.setScaleY(mFooter, footerRatio);
        ViewCompat.setAlpha(mFooter, footerRatio);

        final ViewGroup.LayoutParams lp = mMiddle.getLayoutParams();
        lp.height = m120dp - (int)(m10dp * (1f - middleRatio));
        mMiddle.setLayoutParams(lp);
    }
}
