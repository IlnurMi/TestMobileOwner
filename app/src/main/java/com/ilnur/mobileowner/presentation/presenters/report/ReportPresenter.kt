package com.ilnur.mobileowner.presentation.presenters.report

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Environment
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.Headers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.ilnur.mobileowner.domain.interactors.report.ReportInteractor
import com.ilnur.mobileowner.domain.models.MainResponceArray
import com.ilnur.mobileowner.domain.models.request.ReportRequest
import com.ilnur.mobileowner.domain.models.response.DefaultRequest
import com.ilnur.mobileowner.interfaces.views.report.ReportView
import java.io.*

class ReportPresenter(private val interactor: ReportInteractor) {
    var reportView: ReportView? = null
    private var selectedReportFormatId: String = ""
    private var selectedReportFormatName: String = ""
    private var selectedReportId: String = ""
    private var selectedFileName: String = ""

    fun setView(view: ReportView) {
        reportView = view
    }

    fun getReportFormat() {
        interactor
            .getReportFormat()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result -> responseReportFormatSuccess(result) },
                { error -> responseReportFormatError(error) })
    }

    fun getReportList() {
        interactor
            .getReportList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result -> responseReportListSuccess(result) },
                { error -> responseReportListError(error) })
    }

    fun saveData(selectedReportId: Int, fileName: String) {
        this.selectedReportId = selectedReportId.toString()
        this.selectedFileName = fileName
    }

    @SuppressLint("CheckResult")
    fun downloadReport() {
        reportView?.showReportFormatAlertDialog()
    }

    private fun downloadReports() {
        var call = interactor.downloadReport(
            this.selectedReportId.toInt(),
            this.selectedReportFormatId.toInt()
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    reportView?.showToastStartDownloadReport()
                    val headers: Headers = response.headers()
                    val contentDisposition: String = headers.get("content-disposition").toString()
                    var fileNameResult = contentDisposition.substringAfter('"')
                    fileNameResult = fileNameResult.substringBefore('"').substringAfterLast('.')
                    var filename = "$selectedFileName.$fileNameResult"
                    filename = changeFileName(filename)

                    // Постараться не использовать Async
                    object : AsyncTask<Void, Void, Void>() {
                        override fun doInBackground(vararg voids: Void): Void? {
                            if (writeResponseBodyToDisk(response.body()!!, filename))
                                reportView?.createNotification(filename)

                            return null
                        }
                    }.execute()
                } else {
                    reportView?.showToast("Не удалось загрузить: ошибка сервера.")
                }
            }
        })
    }

    //сохраняем файл "Отчёт" на устройстве/в телефоне.
    private fun writeResponseBodyToDisk(body: ResponseBody, filename: String): Boolean {
        try {
            var sdPath = Environment.getExternalStorageDirectory()
            sdPath = File(sdPath.absolutePath + "/" + "Files")
            sdPath.mkdirs()
            var sdFile = File(sdPath, filename)
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(8192)
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(sdFile)

                while (true) {
                    val read = inputStream!!.read(fileReader)

                    if (read == -1) {
                        break
                    }
                    outputStream!!.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                }
                outputStream!!.flush()
                return true
            } catch (e: IOException) {
                FirebaseCrashlytics.getInstance().recordException(e)
                return false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            FirebaseCrashlytics.getInstance().recordException(e)
            return false
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            return false
        }
    }

    //замена недопустимых символов в имени файла на _
    private fun changeFileName(file: String): String {
        val regexChange = Regex("[?:\"*|/<>\'@#\$%&!`~+\\\\]")    // ?:"*|/<>'@#$%&!`~+\
        return file.replace(regexChange, "_")

    }

    private fun responseReportFormatSuccess(result: MainResponceArray<DefaultRequest>) {
        reportView?.populateReportFormatAdapter(result.list)
    }

    private fun responseReportFormatError(error: Throwable) {
    }

    private fun responseReportListSuccess(result: MainResponceArray<ReportRequest>) {
        reportView?.populateReportListAdapter(result.list)
    }

    private fun responseReportListError(error: Throwable) {
    }

    // Сохранение Id, в перспективе избавиться от такой реализации
    fun reportFormatAlertClick(defaultRequest: DefaultRequest) {
        selectedReportFormatId = defaultRequest.id
        selectedReportFormatName = defaultRequest.name
        downloadReports()
    }
}