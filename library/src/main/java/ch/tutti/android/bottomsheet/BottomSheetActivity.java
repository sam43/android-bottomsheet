package ch.tutti.android.bottomsheet;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by pboos on 26/01/15.
 */
public abstract class BottomSheetActivity extends Activity {

    private ResolverDrawerLayout mResolverDrawerLayout;

    private View mTitleBar;

    private TextView mTitle;

    private ImageView mIcon;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.bs_activity_bottom_sheet);
        mResolverDrawerLayout = (ResolverDrawerLayout) findViewById(R.id.bs_contentPanel);
        mTitleBar = findViewById(R.id.bs_title_bar);
        mTitle = (TextView) findViewById(R.id.bs_title);
        mIcon = (ImageView) findViewById(R.id.bs_icon);
        mIcon.setVisibility(View.GONE);
        setOnClickOutsideListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ViewGroup content = (ViewGroup) findViewById(R.id.bs_content);
        View.inflate(this, layoutResID, content);

        View itemsView = findItemsView(content);
        mResolverDrawerLayout.setScrollableChildView(itemsView);
    }

    /**
     * Sets the listener for when one clicks outside of the bottom sheet.
     *
     * @param listener OnClickListener that gets called when user clicks outside of the
     *                 BottomSheet.
     */
    public void setOnClickOutsideListener(View.OnClickListener listener) {
        mResolverDrawerLayout.setOnClickOutsideListener(listener);
    }

    public void setBottomSheetTitleVisible(boolean visible) {
        mTitleBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setBottomSheetTitle(@StringRes int textRes) {
        mTitle.setText(textRes);
    }

    public void setBottomSheetTitle(CharSequence text) {
        mTitle.setText(text);
    }

    public void setBottomSheetIcon(@DrawableRes int drawableRes) {
        mIcon.setImageResource(drawableRes);
        updateIconVisibility();
    }

    public void setBottomSheetIcon(Drawable drawable) {
        mIcon.setImageDrawable(drawable);
        updateIconVisibility();
    }

    private void updateIconVisibility() {
        mIcon.setVisibility(mIcon.getDrawable() != null ? View.VISIBLE : View.GONE);
    }

    public boolean isBottomSheetCollapsed() {
        return mResolverDrawerLayout.isCollapsed();
    }

    private View findItemsView(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof AbsListView
                    || child instanceof RecyclerView
//                    || "RecyclerView".equals(child.getClass().getSimpleName())
                    || child instanceof ScrollView) {
                return child;
            }
            if (child instanceof ViewGroup) {
                View itemsView = findItemsView((ViewGroup) child);
                if (itemsView != null) {
                    return itemsView;
                }
            }
        }
        return null;
    }
}
