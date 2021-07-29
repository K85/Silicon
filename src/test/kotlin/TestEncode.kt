import com.sakurawald.silicon.util.PluginUtil
import org.junit.jupiter.api.Test

class TestEncode {

    @Test
    fun testEncode() {
        var code = "qwertyuiop[]\\asdfghjkl;'zxcvbnm,./{}|：“”《》？?><L:\"P{}+_)(*&^%\$#@!~?><MKL:\"{}|+_)"
        code = PluginUtil.fastEncodeURL_V2(code,"UTF-8")
        println(code)

    }


}