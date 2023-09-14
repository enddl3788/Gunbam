package military.gunbam.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.mediapipe.framework.image.BitmapImageBuilder;
import com.google.mediapipe.framework.image.MPImage;
import com.google.mediapipe.tasks.core.BaseOptions;
import com.google.mediapipe.tasks.vision.core.RunningMode;
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetector;
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetectorResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import military.gunbam.R;
import military.gunbam.model.DeepLearingModel;
import military.gunbam.viewmodel.DeepLearningViewModel;
import military.gunbam.viewmodel.DeepLearningViewModelFactory;

public class TestDeepLearningActivity extends BasicActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private File file;
    private String fileName = null;
    private ImageView imgViewResult;
    private ObjectDetector objectDetector;
    private ObjectDetector.ObjectDetectorOptions options;
    private ObjectDetectorResult detectionResult;
    private DeepLearningViewModel deepLearningViewModel;
    private Button selectImageButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testdeeplearning);

        Context context = getApplication();
        String modelPath = "model.tflite";
        deepLearningViewModel = new ViewModelProvider(this, new DeepLearningViewModelFactory(context, modelPath)).get(DeepLearningViewModel.class);

        options = ObjectDetector.ObjectDetectorOptions.builder()
                        .setBaseOptions(BaseOptions.builder().setModelAssetPath("model.tflite").build())
                        .setRunningMode(RunningMode.IMAGE)
                        .setMaxResults(5)
                        .build();
        objectDetector = ObjectDetector.createFromOptions(getApplicationContext(), options);

        imgViewResult = findViewById(R.id.img_view_result);

        deepLearningViewModel.getResultBitmap().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(@Nullable Bitmap resultBitmap) {
                if (resultBitmap != null) {
                    imgViewResult.setImageBitmap(resultBitmap);
                }
            }
        });

        //Bitmap bitmap = null;
        //MPImage mpImage = new BitmapImageBuilder(bitmap).build();
        deepLearningViewModel.getResultBitmap().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(@Nullable Bitmap resultBitmap) {
                if (resultBitmap != null) {
                    imgViewResult.setImageBitmap(resultBitmap);
                }
            }
        });

        selectImageButton = findViewById(R.id.deep_button);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 갤러리에서 이미지 선택
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                deepLearningViewModel.run(selectedImage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
