import android.app.Application
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso

class Global1 : Application() {
    override fun onCreate() {
        super.onCreate()
        val builder = Picasso.Builder(applicationContext)
        builder.downloader(OkHttp3Downloader(applicationContext))
        val built = builder.build()
        built.setIndicatorsEnabled(true)
        built.isLoggingEnabled = true
        Picasso.setSingletonInstance(built)
    }
}