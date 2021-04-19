package edu.sjsu.nutritionfinder.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.services.rekognition.AmazonRekognitionClient
import com.amazonaws.services.rekognition.model.DetectLabelsRequest
import com.amazonaws.services.rekognition.model.DetectLabelsResult
import com.amazonaws.services.rekognition.model.Image
import com.amazonaws.services.rekognition.model.S3Object
import com.amazonaws.services.s3.AmazonS3Client
import edu.sjsu.nutritionfinder.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors

class FragmentAViewModel(application: Application) : AndroidViewModel(application) {

    interface ImageRecognitionResult {
        fun onSuccess(imageName: String)
        fun onFailure()
    }

    val transferUtility: TransferUtility
    private val tempFileName = "tempImage.jpeg"
    var threadPool = Executors.newSingleThreadExecutor()
    lateinit var imageRecognitionResult: ImageRecognitionResult
    var transferListener = object : TransferListener {
        override fun onStateChanged(id: Int, state: TransferState?) {
            val state = state?.name ?: ""
            print("++++++ ---- State changes $state")
            if (state == "COMPLETED") {
                triggerImageRecognition()
            }
        }

        override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
            print("++++++ ---- Progress changes")
        }

        override fun onError(id: Int, ex: Exception?) {
            print("++++++ ---- Error")
            imageRecognitionResult.onFailure()

        }
    }
    private var awsCredentials: AWSCredentials =
        BasicAWSCredentials(BuildConfig.S3_ACCESS_KEY_ID, BuildConfig.S3_ACCESS_KEY_SECRET)


    init {
        val s3Client = AmazonS3Client(awsCredentials, Region.getRegion("us-west-2"))
        transferUtility = TransferUtility.builder()
            .defaultBucket("nutritionfinder")
            .s3Client(s3Client)
            .context(getApplication<Application>().applicationContext)
            .build()
    }

    private fun triggerImageRecognition() {
        threadPool.execute {
            val rekognitionClient = AmazonRekognitionClient(awsCredentials)
            rekognitionClient.setRegion(Region.getRegion("us-west-2"))
            val s3Object = S3Object()
            s3Object.bucket = "nutritionfinder"
            s3Object.name = "test223.jpeg"
            val image = Image().withS3Object(s3Object)
            val labelRequest = DetectLabelsRequest().withImage(image).withMaxLabels(5)
            try {
                val detectLabelResult = rekognitionClient.detectLabels(labelRequest)
                getImageName(detectLabelResult)
                deleteImageFromS3()
            } catch (e: java.lang.Exception) {
                imageRecognitionResult.onFailure()
            }
        }
    }

    private fun deleteImageFromS3() {
        val s3Client = AmazonS3Client(awsCredentials, Region.getRegion("us-west-2"))
        s3Client.deleteObject("nutritionfinder", "test223.jpeg")
    }

    private fun getImageName(detectLabelResult: DetectLabelsResult) {
        for (label in detectLabelResult.labels) {
            for (parent in label.parents) {
                if (parent.name == "Vegetable") {
                    imageRecognitionResult.onSuccess(label.name)
                    return
                }
            }
        }
    }

    fun uploadImageToS3(file: File) {

        threadPool.execute {

            val observer = transferUtility.upload("test223.jpeg", file)
            observer.setTransferListener(transferListener)
            TransferNetworkLossHandler.getInstance(getApplication<Application>().applicationContext)
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

    private fun removeImageFromS3() {

    }
}