package com.sakurawald.silicon.api

class Sentence(
    var id: Int?, var content: String?, var type: String?, var from: String?,
    var creator: String?, var createdAt: String?
) {

    /**
     * @return 格式化后的文本, 可用于快速展示. 本身为空则返回null.
     */
    val formatedString: String?
        get() = if (content == null && from == null) {
            null
        } else "『$content』-「$from」"

    override fun toString(): String {
        return ("Sentence [id=$id, content=$content, type=$type, from=$from, creator=$creator, created_at=$createdAt]")
    }

    companion object {
        /**
         * @return Sentence的空对象.
         */
        val nullSentence: Sentence
            get() = Sentence(null, null, null, null, null, null)
    }

}