package edu.sjsu.nutritionfinder.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client
import edu.sjsu.nutritionfinder.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors

class FragmentAViewModel(application: Application) : AndroidViewModel(application) {

    val transferUtility: TransferUtility
    private val tempFileName = "tempImage.jpeg"
    var threadPool = Executors.newSingleThreadExecutor()

    init {
        val awsCredentials =
            BasicAWSCredentials(BuildConfig.S3_ACCESS_KEY_ID, BuildConfig.S3_ACCESS_KEY_SECRET)
        val s3Client = AmazonS3Client(awsCredentials, Region.getRegion("us-west-2"))
        transferUtility = TransferUtility.builder()
            .defaultBucket("nutritionfinder")
            .s3Client(s3Client)
            .context(getApplication<Application>().applicationContext)
            .build()
    }

    fun uploadImageToS3(file: File) {

        threadPool.execute {

            val observer = transferUtility.upload("test123.jpeg", file)
            val lossHandler =
                TransferNetworkLossHandler.getInstance(getApplication<Application>().applicationContext)
            observer.setTransferListener(object : TransferListener {
                override fun onStateChanged(id: Int, state: TransferState?) {
                    print("++++++ ---- State changes")
                }

                override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                    print("++++++ ---- Progress changes")
                }

                override fun onError(id: Int, ex: Exception?) {
                    print("++++++ ---- Error")
                }
            })
        }

    }

    fun copyBitmapToFileSystem(image: Bitmap?): File? {
        if (image == null) {
            return null
        }
        val cacheDir = getApplication<Application>().applicationContext.cacheDir
        val tempImageFile = File(cacheDir, tempFileName)
        if (tempImageFile.exists()) {
            tempImageFile.delete()
        }
        val fos = FileOutputStream(tempImageFile)
        image.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.close()
        return tempImageFile
    }
}