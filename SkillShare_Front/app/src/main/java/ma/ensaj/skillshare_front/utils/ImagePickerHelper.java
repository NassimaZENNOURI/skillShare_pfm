package ma.ensaj.skillshare_front.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.fragment.app.Fragment;

public class ImagePickerHelper {
    public static final int PICK_IMAGE_REQUEST = 1;

    public static void pickImage(Fragment fragment) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        fragment.startActivityForResult(
                Intent.createChooser(intent, "Sélectionner une image"),
                PICK_IMAGE_REQUEST
        );
    }

    public static boolean isImageRequestCode(int requestCode) {
        return requestCode == PICK_IMAGE_REQUEST;
    }

    public static Uri getImageUriFromResult(Intent data) {
        if (data != null && data.getData() != null) {
            return data.getData();
        }
        return null;
    }
}