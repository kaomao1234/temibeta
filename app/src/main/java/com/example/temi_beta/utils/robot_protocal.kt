import android.util.Log
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.listeners.OnDetectionDataChangedListener
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener
import com.robotemi.sdk.listeners.OnRobotReadyListener
import com.robotemi.sdk.listeners.OnUserInteractionChangedListener
import com.robotemi.sdk.model.DetectionData
import com.robotemi.sdk.navigation.model.Position
import com.robotemi.sdk.permission.Permission

// reformat(Android Studio) = ctrl + alt + l
// compile code(Android Studio) =  alt + shift + F10
class RobotProtocol(
    val onGoToLocationStatusChangedListener: (
        instance: RobotProtocol,
        location: String,
        status: String,
        descriptionId: Int,
        description: String
    ) -> Unit
) :
    OnRobotReadyListener,
    OnDetectionDataChangedListener, OnDetectionStateChangedListener,
    OnUserInteractionChangedListener, OnGoToLocationStatusChangedListener {
    private var mRobot: Robot = Robot.getInstance();
    init {
        mRobot.setKioskModeOn(true)
        mRobot.locked = true
    }
    fun tiltBy(degree: Int, speed: Float) {
        mRobot.tiltBy(degree, speed)
    }

    fun textToSpeech(
        text: String,
        isShowText: Boolean,
        language: TtsRequest.Language = TtsRequest.Language.SYSTEM
    ) {
        this.mRobot.speak(
            TtsRequest.create(
                speech = text,
                isShowOnConversationLayer = isShowText,
                language = language
            )
        )
    }


    fun goToPosition(posX: Float, posY: Float, tilt: Int, yaw: Float) {
        mRobot.goToPosition(Position(posX, posY, yaw, tilt))
    }

    fun setDetectionAndTracking(
        enableTrack: Boolean,
        enableDetect: Boolean,
        detectRange: Float = 2F
    ) {
        mRobot.trackUserOn = enableTrack
        mRobot.detectionModeOn = enableDetect
//        mRobot.setDetectionModeOn(enableDetect, detectRange);

    }


    fun goToLocation(location: String) {
        mRobot.goTo(location)
    }

    fun deleteLocation(name: String) {
        mRobot.deleteLocation(name)
    }

    fun saveLocation(name: String) {
        mRobot.saveLocation(name)

    }

    fun getAllLocation(): List<String> {
        return mRobot.locations;
    }

    fun setLocked(locked:Boolean){
    }

//    override fun onCurrentPositionChanged(position: Position) {
//        Log.d(
//            "Robot Action",
//            "onCurrentPositionChanged: ${
//                listOf(
//                    position.x,
//                    position.y,
//                    position.yaw,
//                    position.tiltAngle
//                )
//            }"
//        )
//    }

    override fun onRobotReady(isReady: Boolean) {
        if (isReady) {
            val permissions = listOf<Permission>(
                Permission.MAP,
                Permission.FACE_RECOGNITION,
                Permission.MEETINGS,
                Permission.SEQUENCE,
            )
            mRobot.requestPermissions(permissions,1)
            this.textToSpeech("Robot is ready", true, TtsRequest.Language.EN_US);
            this.mRobot.hideTopBar()
        }
    }

    fun repose() {
        mRobot.repose()
    }

    fun onStart() {
        mRobot.addOnRobotReadyListener(this)
//        mRobot.addOnCurrentPositionChangedListener(this)
        mRobot.addOnDetectionDataChangedListener(this)
        mRobot.addOnDetectionStateChangedListener(this)
        mRobot.addOnUserInteractionChangedListener(this)
        mRobot.addOnGoToLocationStatusChangedListener(this)
//        mRobot.add

    }

    fun onEnd() {
        mRobot.removeOnRobotReadyListener(this)
//        mRobot.removeOnCurrentPositionChangedListener(this)
        mRobot.removeOnDetectionDataChangedListener(this)
        mRobot.removeOnDetectionStateChangedListener(this)
        mRobot.removeOnUserInteractionChangedListener(this)
        mRobot.removeOnGoToLocationStatusChangedListener(this)
    }

    override fun onDetectionDataChanged(detectionData: DetectionData) {
        Log.d("Robot Action", "onDetectionDataChanged: ${detectionData.isDetected}")
    }

    override fun onDetectionStateChanged(state: Int) {
        Log.d("Robot Action", "onDetectionStateChanged: $state")
    }

    override fun onUserInteraction(isInteracting: Boolean) {
        Log.d("Robot Action", "onUserInteraction: $isInteracting")
    }


    override fun onGoToLocationStatusChanged(
        location: String,
        status: String,
        descriptionId: Int,
        description: String
    ) {
        onGoToLocationStatusChangedListener(
            this,
            location,
            status,
            descriptionId,
            description
        )
        if (status == "complete") {
            mRobot.repose()
        }
    }

}

