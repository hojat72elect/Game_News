package ca.on.hojat.gamenews.core.urlopeners

import android.content.Context
import android.content.Intent
import android.net.Uri
import ca.on.hojat.gamenews.core.extensions.attachNewTaskFlagIfNeeded
import ca.on.hojat.gamenews.core.extensions.canIntentBeHandled
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

@BindType(withQualifier = true)
@UrlOpenerKey(UrlOpenerKey.Type.BROWSER)
internal class BrowserUrlOpener @Inject constructor() : UrlOpener {

    override fun openUrl(url: String, context: Context): Boolean {
        val intent = createIntent(url, context)

        return if (context.canIntentBeHandled(intent)) {
            context.startActivity(intent)
            true
        } else {
            false
        }
    }

    private fun createIntent(url: String, context: Context): Intent {
        return Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            attachNewTaskFlagIfNeeded(context)
        }
    }
}
