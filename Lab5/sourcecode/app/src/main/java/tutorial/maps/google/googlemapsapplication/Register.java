package tutorial.maps.google.googlemapsapplication;

/**
 * Created by Divya Gaddam on 2/22/2017.
 */
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by malin on 05-10-2016.
 */

public class Register extends AppCompatActivity{

    ImageView viewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        //b=(Button)findViewById(R.id.btnSelectPhoto);
        viewImage = (ImageView) findViewById(R.id.imageView);
        viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds options to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*public boolean validate() {
        boolean valid = true;

        if (userName.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
            Toast.makeText(getApplicationContext(), "Enter a valid email address", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            errorText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            Toast.makeText(getApplicationContext(), "Password should be atleast 4-6 alphanumeric", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            errorText.setError(null);
        }
        if (confirmPassword.isEmpty() || confirmPassword.length() < 4 || confirmPassword.length() > 10) {
            Toast.makeText(getApplicationContext(), "Password should be atleast 4-6 alphanumeric", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            errorText.setError(null);
        }

        if(!password.equals(confirmPassword)){
            valid=false;
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
        else {
            errorText.setError(null);
        }

        return valid;
    }
    public void newUser(View v) {

        EditText usernameCtrl = (EditText) findViewById(R.id.txt_email);
        EditText passwordCtrl = (EditText) findViewById(R.id.txt_Pwd);
        EditText confirmPasswordCtrl = (EditText) findViewById(R.id.txt_ConfirmPwd);

        errorText = (TextView) findViewById(R.id.sign_error);
        userName = usernameCtrl.getText().toString();
        password = passwordCtrl.getText().toString();
        confirmPassword = confirmPasswordCtrl.getText().toString();
    }
*/
    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    viewImage.setImageBitmap(bitmap);

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from ", picturePath + "");
                viewImage.setImageBitmap(thumbnail);
            }
        }
    }

    public void onClickButton(View v) {
        //This code redirects to the photo activity.
        Intent redirect = new Intent(Register.this, MapsActivity.class);
        startActivity(redirect);
    }
}
