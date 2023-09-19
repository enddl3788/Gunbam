package military.gunbam.model;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.widget.ImageView;

import com.google.mediapipe.framework.image.BitmapImageBuilder;
import com.google.mediapipe.tasks.components.containers.Category;
import com.google.mediapipe.tasks.components.containers.Detection;
import com.google.mediapipe.tasks.core.BaseOptions;
import com.google.mediapipe.tasks.core.OutputHandler;
import com.google.mediapipe.tasks.vision.core.RunningMode;
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetector;
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetectorResult;
import com.google.mediapipe.framework.image.MPImage;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeepLearingModel {
    private String[] labels = {"CAR_NUM", "RANK", "MARK", "KOREA", "NAME"};
    private static final int WIDTH = 320;
    private static final int HEIGHT = 320;
    private static final int CHANNELS = 3;

    private ObjectDetector objectDetector;
    private ObjectDetector.ObjectDetectorOptions options;
    private ObjectDetectorResult detectionResult;
    private ImageView deepLearningImageView;

    public DeepLearingModel(Context context, String modelPath) {
        options = ObjectDetector.ObjectDetectorOptions.builder()
                .setBaseOptions(BaseOptions.builder().setModelAssetPath(modelPath).build())
                .setRunningMode(RunningMode.IMAGE)
                .setMaxResults(5)
                .build();
        objectDetector = ObjectDetector.createFromOptions(context, options);
    }


    public Bitmap deepLearing(Bitmap bitmap) {
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_596);
        Log.d("DeepLearingModel", "넓이 : " + bitmap.getWidth() + "\n높이 : " + bitmap.getHeight());

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, true);
        int[] intValues = new int[WIDTH * HEIGHT];
        resizedBitmap.getPixels(intValues, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());

        float[] floatValues = new float[WIDTH * HEIGHT * CHANNELS];
        for (int i = 0; i < intValues.length; ++i) {
            final int val = intValues[i];
            floatValues[i * 3] = ((val >> 16) & 0xFF);
            floatValues[i * 3 + 1] = ((val >> 8) & 0xFF);
            floatValues[i * 3 + 2] = (val & 0xFF);
        }

        byte[][][][] inputArray = new byte[1][HEIGHT][WIDTH][CHANNELS];
        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                for (int c = 0; c < CHANNELS; ++c) {
                    inputArray[0][i][j][c] = (byte) floatValues[(i * WIDTH + j) * CHANNELS + c];
                }
            }
        }

        MPImage mpImage = new BitmapImageBuilder(bitmap).build();
        ObjectDetectorResult detectionResult = objectDetector.detect(mpImage);

        float[][] outputScores = new float[1][25];
        float[][][] outputLocations = new float[1][25][4];
        float[] outputDetections = new float[1];
        float[][] outputCategories = new float[1][25];

        Map<Integer, Object> outputMap = new HashMap<>();
        outputMap.put(0, outputScores);
        outputMap.put(1, outputLocations);
        outputMap.put(2, outputDetections);
        outputMap.put(3, outputCategories);

        OutputHandler handler = new OutputHandler();

        OutputHandler.PureResultListener<ObjectDetectorResult> listener = new OutputHandler.PureResultListener<ObjectDetectorResult>() {
            @Override
            public void run(ObjectDetectorResult result) {
                // 이곳에 원하는 동작을 구현합니다.
                // result.detections()
                // result.timestampMs()
                // result.boundingBox()
                // result.categories()
                long timestampMs = result.timestampMs();
                System.out.println("결과값 출력");
                System.out.println("timestampMs: " + Long.toString(result.timestampMs()));
                List<Detection> detections = result.detections();
                for (Detection detection : detections) {
                    RectF bbox = detection.boundingBox();

                    System.out.println("bbox.bottom: " + bbox.bottom);
                    System.out.println("bbox.left: " + bbox.left);
                    System.out.println("bbox.right: " + bbox.right);
                    System.out.println("bbox.top: " + bbox.top);

                    List<Category> category = detection.categories();

                    for (Category cat : detection.categories()) {
                        String catName = cat.categoryName();
                        String displayName = cat.displayName();
                        float catScore = cat.score();
                        int index = cat.index();
                        System.out.println("catName: " + catName);
                        System.out.println("displayName: " + displayName);
                        System.out.println("catScore: " + Float.toString(catScore));
                        System.out.println("index: " + Integer.toString(index));
                    }
                }
            }
        };

        Bitmap processedBitmap = bitmap;
        //deepLearningImageView.setImageBitmap(bitmap);

        processedBitmap = Bitmap.createScaledBitmap(processedBitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        Bitmap mutableBitmap = processedBitmap.copy(processedBitmap.getConfig(), true);
        Canvas canvas = new Canvas(mutableBitmap);

        //Canvas canvas2 = new Canvas(processedBitmap);
        int numDetections = (int) outputDetections[0];
        for (int i = 0; i < numDetections; i++) {
            Log.d("DeepLearingModel", outputScores[0][i] + "확률");
            try {
                if (outputScores[0][i] >= 0.3) {
                    Log.d("DeepLearingModel", outputScores[0][i] + "통과");
                    float[] location = outputLocations[0][i];
                    int x1 = (int) (location[1] * bitmap.getWidth());
                    int y1 = (int) (location[0] * bitmap.getHeight());
                    int x2 = (int) (location[3] * bitmap.getWidth());
                    int y2 = (int) (location[2] * bitmap.getHeight());

                    if (x1 < 0) {
                        x1 = 0;
                    }
                    if (x2 < 0) {
                        x2 = 0;
                    }
                    Log.d("DeepLearingModel", x1 + " " + y1 + " " + x2 + " " + y2);

                    int categoryIndex = (int) outputCategories[0][i]; // 객체의 카테고리 인덱스 추출
                    String category = labels[categoryIndex]; // 레이블 목록에서 해당 인덱스에 해당하는 카테고리 추출

                    Paint paint = new Paint();

                    // 바운딩 박스 출력
                    paint.setColor(Color.RED);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(10.0f);
                    canvas.drawRect(x1, y1, x2, y2, paint);

                    paint.setStyle(Paint.Style.FILL);
                    paint.setTextSize(30);
                    paint.setColor(Color.BLACK);
                    //canvas.drawText(category, x1 + 10, y1, paint); // 카테고리 출력

                    // 해당 영역 캡처 및 모자이크 처리
                    int width = x2 - x1;
                    int height = y2 - y1;
                    Log.d("DeepLearingModel", width + " " + height);
                    Bitmap capture = Bitmap.createBitmap(processedBitmap, x1, y1, width, height);
                    Bitmap scaledCapture = Bitmap.createScaledBitmap(capture, width / 30, height / 30, false);  // 모자이크 강도 조절 부분
                    Bitmap finalCapture = Bitmap.createScaledBitmap(scaledCapture, width, height, false);

                    // 모자이크 처리된 영역 덮어씌우기
                    Rect rect = new Rect(x1, y1, x2, y2);
                    Log.d("DeepLearingModel", rect + " ");
                    canvas.drawBitmap(finalCapture, null, rect, null);

                }
            } catch (Exception e) {
                Log.d("DeepLearingModel", String.valueOf(e));
            }
        }
        return processedBitmap;
    }

    public MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public Bitmap mergeBitmapImage(Bitmap leftUp, Bitmap rightUp, Bitmap leftDown, Bitmap rightDown) {
        // 가로로 합쳐진 이미지의 폭과 높이 계산
        int resultWidth = leftUp.getWidth() + rightUp.getWidth();
        int resultHeight = leftUp.getHeight() + leftDown.getHeight();

        // 새로운 비트맵 생성
        Bitmap mergeResult = Bitmap.createBitmap(resultWidth, resultHeight, Bitmap.Config.ARGB_8888);

        // 비트맵을 캔버스에 그림
        Canvas canvas = new Canvas(mergeResult);
        canvas.drawBitmap(leftUp, 0, 0, null);
        canvas.drawBitmap(rightUp, leftUp.getWidth(), 0, null);
        canvas.drawBitmap(leftDown, 0, leftUp.getHeight(), null);
        canvas.drawBitmap(rightDown, leftUp.getWidth(), leftUp.getHeight(), null);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(mergeResult, WIDTH * 4, HEIGHT * 4, true);
        return resizedBitmap;
    }
}
