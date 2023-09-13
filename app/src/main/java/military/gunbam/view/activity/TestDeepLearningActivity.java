package military.gunbam.view.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.mediapipe.framework.image.BitmapImageBuilder;
import com.google.mediapipe.framework.image.MPImage;
import com.google.mediapipe.tasks.core.BaseOptions;
import com.google.mediapipe.tasks.vision.core.RunningMode;
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetector;
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetectorResult;

import military.gunbam.R;

public class TestDeepLearningActivity extends BasicActivity {
    private ObjectDetector objectDetector;
    private ObjectDetector.ObjectDetectorOptions options;
    private ObjectDetectorResult detectionResult;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testdeeplearning);


        options = ObjectDetector.ObjectDetectorOptions.builder()
                        .setBaseOptions(BaseOptions.builder().setModelAssetPath("model.tflite").build())
                        .setRunningMode(RunningMode.IMAGE)
                        .setMaxResults(5)
                        .build();
        objectDetector = ObjectDetector.createFromOptions(getApplicationContext(), options);

        Bitmap bitmap = null;
        MPImage mpImage = new BitmapImageBuilder(bitmap).build();

        Button button = findViewById(R.id.deep_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectionResult = objectDetector.detect(mpImage);

            }
        });





    }
}
