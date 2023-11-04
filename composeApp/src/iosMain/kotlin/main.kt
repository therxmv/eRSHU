import androidx.compose.ui.window.ComposeUIViewController
import com.therxmv.ershu.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
