import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.weatherapp.MainActivity
import com.example.weatherapp.NOTIFICATION_CHANNEL_ID
import com.example.weatherapp.R
import com.example.weatherapp.WeatherRepository
import com.example.weatherapp.api.WeatherItem
import java.util.Calendar
import java.util.concurrent.TimeUnit

private const val TAG = "PollWorker"
private val RAIN_THRESHOLD_PERCENTAGE = 5
class PollWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        Log.i(TAG, "Work request triggered")
        val weatherrepository = WeatherRepository()
        val forecast = weatherrepository.fetchWeather("84790")
        val isRainForecasted = isRainForecasted(forecast)

        Log.d(TAG, "Is rain forecasted: $isRainForecasted")

        if (isRainForecasted) {
            // Trigger notification
            Log.i(TAG, "rain forecasted")
            notifyUser("Rain Forecast","Rain is forecasted for today. Don't forget your umbrella!")
            return Result.success()
        } else {
            Log.i(TAG, "No rain forecasted")
            notifyUser("Clear Forecast","no rain is forecasted for today. Go for a hike!")

            return Result.success()
        }



    }
    private fun notifyUser(title: String,text: String) {
        val intent = MainActivity.newIntent(context)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val resources = context.resources

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.icon)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(context).notify(0, notification)
    }
    private fun isRainForecasted(forecast: List<WeatherItem>): Boolean {
        // Iterate through the list of WeatherItems in the forecast
        for (item in forecast) {
            // Check if rain percent is greater than a certain threshold indicating rain forecasted
            if (item.rainPercent > RAIN_THRESHOLD_PERCENTAGE) {
                return true
            }
        }
        return false
    }
    companion object {
        private const val TAG = "PollWorker"

        // Schedule the worker to run once per day at 8 AM
        fun scheduleWeatherCheck(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = PeriodicWorkRequestBuilder<PollWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "weather_check_worker",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
        }
        private fun calculateInitialDelay(): Long {
            val now = Calendar.getInstance()
            val targetTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 8) // Set target hour to 8 AM
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            if (now.after(targetTime)) {
                // If the current time is already past the target time for today,
                // schedule the worker to run at the same time tomorrow
                targetTime.add(Calendar.DAY_OF_MONTH, 1)
            }

            return targetTime.timeInMillis - now.timeInMillis
        }
    }



}