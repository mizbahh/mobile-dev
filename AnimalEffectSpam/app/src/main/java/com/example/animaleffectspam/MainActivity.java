package com.example.animaleffectspam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout layout;
    private final Random random = new Random();

    // loaded once at startup so clicks are fast
    private Bitmap catBitmap;
    private SoundPool soundPool;
    private int meowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        layout = findViewById(R.id.main);
        Button spamButton = findViewById(R.id.spamButton);

        // decode picture once and reuse it
        catBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cat);

        // preload sound so it plays instantly
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();
        meowId = soundPool.load(this, R.raw.meow, 1);

        // when button is clicked, add animal and play sound
        spamButton.setOnClickListener(v -> spawnAnimal());
    }

    private void spawnAnimal() {
        int areaLeft = layout.getPaddingLeft();
        int areaTop = layout.getPaddingTop();
        int areaWidth = layout.getWidth() - layout.getPaddingLeft() - layout.getPaddingRight();
        int areaHeight = layout.getHeight() - layout.getPaddingTop() - layout.getPaddingBottom();

        // random size
        float density = getResources().getDisplayMetrics().density;
        int minSize = (int) (80 * density);
        int maxSize = (int) (250 * density);
        maxSize = Math.min(maxSize, Math.min(areaWidth, areaHeight)); // never bigger than the screen
        int size = minSize + random.nextInt(maxSize - minSize + 1);

        // whole image stays on screen
        // random position
        int x = areaLeft + random.nextInt(areaWidth - size + 1);
        int y = areaTop + random.nextInt(areaHeight - size + 1);

        // create image and add to layout
        ImageView animal = new ImageView(this);
        animal.setImageBitmap(catBitmap);
        animal.setScaleType(ImageView.ScaleType.FIT_CENTER); // keeps the whole picture visible
        animal.setLayoutParams(new ConstraintLayout.LayoutParams(size, size));
        animal.setX(x);
        animal.setY(y);
        layout.addView(animal);

        // play sound
        soundPool.play(meowId, 1f, 1f, 1, 0, 1f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // free sound when app closes
        soundPool.release();
    }
}