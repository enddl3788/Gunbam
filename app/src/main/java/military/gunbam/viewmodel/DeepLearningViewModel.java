package military.gunbam.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import androidx.lifecycle.ViewModel;

import java.io.IOException;

import military.gunbam.model.DeepLearingModel;

public class DeepLearningViewModel extends ViewModel {
    private DeepLearingModel deepLearingModel;

    public DeepLearningViewModel(Context context, String modelPath) {
        deepLearingModel = new DeepLearingModel(context, modelPath);
    }
    public void run(Bitmap bitmap){
        try {
            Bitmap originalBitmap = bitmap;
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int newWidth = width / 2;
            int newHeight = height / 2;
            Bitmap topLeft = Bitmap.createBitmap(originalBitmap, 0, 0, newWidth, newHeight);
            Bitmap topRight = Bitmap.createBitmap(originalBitmap, newWidth, 0, newWidth, newHeight);
            Bitmap bottomLeft = Bitmap.createBitmap(originalBitmap, 0, newHeight, newWidth, newHeight);
            Bitmap bottomRight = Bitmap.createBitmap(originalBitmap, newWidth, newHeight, newWidth, newHeight);

            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                public void run() {

                    Bitmap leftUp = deepLearingModel.deepLearing(topLeft);
                    Bitmap rightUp = deepLearingModel.deepLearing(topRight);
                    Bitmap leftDown = deepLearingModel.deepLearing(bottomLeft);
                    Bitmap rightDown = deepLearingModel.deepLearing(bottomRight);

                    Bitmap mergeBitmap = deepLearingModel.mergeBitmapImage(leftUp, rightUp, leftDown, rightDown);
                    imgViewResult.setImageBitmap(mergeBitmap);
                }
            }, 1); // 0.001초후
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
