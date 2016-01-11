package in.shaapps.mygola.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;

import in.shaapps.mygola.R;
import in.shaapps.mygola.revealcolorview.RevealColorView;

public class LaunchScreen extends AppCompatActivity {

    private RevealColorView revealColorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        revealColorView = (RevealColorView) findViewById(R.id.rippleView);

        final ImageView imageView = (ImageView) findViewById(R.id.launchImage);

        /**
         * Start the animation after some delay
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                revealView(imageView, Color.parseColor("#F6D600"));
            }
        }, 500);
    }

    /**
     * Animate reveal effect on Reveal View
     */
    private void revealView(View viewFromRevealed, int parsedColor) {
        final Point p = getLocationInView(revealColorView, viewFromRevealed);
        revealColorView.reveal(p.x, p.y, parsedColor,
                viewFromRevealed.getHeight() / 2, 500, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        /**
                         * After reveal finish start the activity
                         */
                        startActivity(new Intent(LaunchScreen.this, MainInfoActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    /**
     * Get the location of the view from where it should be revealed
     *
     * @param src    source view
     * @param target target view
     * @return view location
     */
    private Point getLocationInView(View src, View target) {
        final int[] l0 = new int[2];
        src.getLocationOnScreen(l0);

        final int[] l1 = new int[2];
        target.getLocationOnScreen(l1);

        l1[0] = l1[0] - l0[0] + target.getWidth() / 2;
        l1[1] = l1[1] - l0[1] + target.getHeight() / 2;

        return new Point(l1[0], l1[1]);
    }

}