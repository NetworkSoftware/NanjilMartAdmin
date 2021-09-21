package procrop;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rahul.media.R;

public class BasicActivity extends AppCompatActivity {


    // Lifecycle Method ////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_basic);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, BasicFragment.newInstance()).commit();
        }

        // apply custom font
        initToolbar();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void initToolbar() {
        getSupportActionBar().setTitle("Crop Image");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void startResultActivity(Uri uri) {
        if (isFinishing()) return;
        // Start ResultActivity
        setResult(RESULT_OK, new Intent().putExtra(MediaStore.EXTRA_OUTPUT, uri));
        finish();
    }
}
