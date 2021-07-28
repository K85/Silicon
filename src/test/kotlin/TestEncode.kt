import com.sakurawald.silicon.util.PluginUtil
import com.sakurawald.silicon.util.PluginUtil.fastEncodeURL
import org.junit.jupiter.api.Test
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*

class TestEncode {

    @Test
    fun testEncode() {
        var code = "qwertyuiop[]\\asdfghjkl;'zxcvbnm,./{}|：“”《》？?><L:\"P{}+_)(*&^%\$#@!~?><MKL:\"{}|+_)"
        code = PluginUtil.fastEncodeURL_V2(code,"UTF-8")
        println(code)

    }


}