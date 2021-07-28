package com.sakurawald.silicon.api

class Sentence(
    id: Int, content: String?, type: String?, from: String?,
    creator: String?, created_at: String?
) {
    var id = 0
    var content: String? = null
    var type: String? = null
    var from: String? = null
    var creator: String? = null
    var created_at: String? = null

    /**
     * @return 格式化后的文本, 可用于快速展示. 本身为空则返回null.
     */
    val formatedString: String?
        get() = if (content == null && from == null) {
            null
        } else "『" + content + "』" + "-「" + from + "」"

    override fun toString(): String {
        return ("Sentence [id=" + id + ", content=" + content + ", type=" + type
                + ", from=" + from + ", creator=" + creator + ", created_at="
                + created_at + "]")
    }

    companion object {
        /**
         * @return new空的Sentence对象.
         */
        val nullSentence: Sentence
            get() = Sentence(0, null, null, null, null, null)
    }

    init {
        this.id = id
        this.content = content
        this.type = type
        this.from = from
        this.creator = creator
        this.created_at = created_at
    }
}