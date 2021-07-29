package com.sakurawald.silicon.data.beans.response

import com.sakurawald.silicon.data.beans.Problem
import java.util.*

open class ProblemsResponse(var problems: ArrayList<Problem>) : ResponseBean()