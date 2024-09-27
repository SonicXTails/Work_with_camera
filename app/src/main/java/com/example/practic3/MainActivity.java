package com.example.practic3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button cameraBtn, galleryBtn, videoBtn, videoGalleryBtn;
    ImageView profile_iv;
    VideoView videoView;  // Добавим VideoView для воспроизведения видео

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profile_iv = findViewById(R.id.profile_iv);
        cameraBtn = findViewById(R.id.camera_btn);
        galleryBtn = findViewById(R.id.gallery_btn);
        videoBtn = findViewById(R.id.video_btn);
        videoGalleryBtn = findViewById(R.id.video_gallery_btn);
        videoView = findViewById(R.id.videoview);  // Инициализируем VideoView

        // Логика для камеры
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraResultLauncher.launch(cameraIntent);
            }
        });

        // Логика для галереи изображений
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryResultLauncher.launch(galleryIntent);
            }
        });

        // Логика для записи видео
        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                videoResultLauncher.launch(videoIntent);
            }
        });

        // Логика для выбора видео из галереи
        videoGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                videoGalleryResultLauncher.launch(videoGalleryIntent);
            }
        });
    }

    // Лаунчер для камеры
    ActivityResultLauncher<Intent> cameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap avatar = (Bitmap) result.getData().getExtras().get("data");
                        profile_iv.setImageBitmap(avatar);
                    }
                }
            }
    );

    // Лаунчер для галереи изображений
    ActivityResultLauncher<Intent> galleryResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        profile_iv.setImageURI(selectedImage);
                    }
                }
            }
    );

    // Лаунчер для записи видео
    ActivityResultLauncher<Intent> videoResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri videoUri = result.getData().getData();
                        // Устанавливаем видео в VideoView и начинаем воспроизведение
                        videoView.setVideoURI(videoUri);
                        videoView.start();  // Начинаем воспроизведение
                        Toast.makeText(MainActivity.this, "Видео записано: " + videoUri.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    // Лаунчер для выбора видео из галереи
    ActivityResultLauncher<Intent> videoGalleryResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedVideo = result.getData().getData();
                        // Воспроизведение выбранного видео
                        videoView.setVideoURI(selectedVideo);
                        videoView.start();  // Начинаем воспроизведение
                        Toast.makeText(MainActivity.this, "Выбрано видео: " + selectedVideo.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
}
