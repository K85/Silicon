package com.sakurawald.silicon.data.beans.response

import com.sakurawald.silicon.data.beans.Problem

@Suppress("unused")
open class ProblemDetailResponse(var ownerProblem: Problem?, var HTML: String?) : ResponseBean()