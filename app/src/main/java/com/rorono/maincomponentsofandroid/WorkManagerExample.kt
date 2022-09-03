package com.rorono.maincomponentsofandroid

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.work.*

class WorkManagerExample(context: Context, private val workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        log("doWork()")
        val page = workerParameters.inputData.getInt(PAGE, 0)
        for (i in 0..100) {
            Thread.sleep(1000)
            log("timer $i $page")
        }
        return Result.success()
    }

    private fun log(message: String) {
        Log.d("SERVICE", "WorkManagerExample: $message")
    }

    companion object {
        private const val PAGE = "page"
        const val NAME_WORK_MANAGER = "work name"

        fun makeRequest(page: Int): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<WorkManagerExample>().apply {
                setInputData(workDataOf(PAGE to page))
                setConstraints(makeConstraints())
            }.build()
        }

        private fun makeConstraints(): Constraints {
            return Constraints.Builder()
                .setRequiresCharging(true)
                .build()
        }

    }
}