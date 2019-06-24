package pl.dzielins42.skinflint.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.view.ViewCompat
import androidx.coordinatorlayout.widget.CoordinatorLayout

class ScrollAwareFloatingActionButtonBehavior(
    context: Context,
    attrs: AttributeSet
) : FloatingActionButton.Behavior(
    context, attrs
) {
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View,
        nestedScrollAxes: Int,
        @ViewCompat.NestedScrollType type: Int
    ): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(
            coordinatorLayout, child, directTargetChild, target,
            nestedScrollAxes,
            type
        )
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View,
        dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int,
        @ViewCompat.NestedScrollType type: Int
    ) {
        super.onNestedScroll(
            coordinatorLayout, child, target,
            dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
            type
        )

        if (dyConsumed > 0 && child.visibility == View.VISIBLE) {
            // Hack for https://code.google.com/p/android/issues/detail?id=230298
            child.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
                @SuppressLint("RestrictedApi")
                override fun onHidden(fab: FloatingActionButton) {
                    super.onHidden(fab)
                    fab.visibility = View.INVISIBLE
                }
            })
        } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
            child.show()
        }
    }
}