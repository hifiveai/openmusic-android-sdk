package com.hifive.sdk.rx

import android.content.Context
import android.os.Looper
import android.os.Process
import android.util.Log
import android.widget.Toast
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer

class HIFivesdkCatchException  //保证只有一个实例
    : Thread.UncaughtExceptionHandler {
    //系统默认的uncatchException
    private var mDefaultException: Thread.UncaughtExceptionHandler? = null
    private var mContext: Context? = null

    //获取系统默认的异常处理器,并且设置本类为系统默认处理器
    fun init(ctx: Context?) {
        mContext = ctx
        mDefaultException = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    //自定义错误处理器
    private fun handlerException(ex: Throwable?): Boolean {
        if (ex == null) {  //如果已经处理过这个Exception,则让系统处理器进行后续关闭处理
            return false
        }

        //获取错误原因
        val writer: Writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        object : Thread() {
            override fun run() {
                // Toast 显示需要出现在一个线程的消息队列中
                Looper.prepare()
                Toast.makeText(mContext, "程序出错:$result", Toast.LENGTH_LONG).show()
                //将异常记录到本地的数据库或者文件中.或者直接提交到后台服务器
//                Util.writeLog("全局异常",msg);
                Looper.loop()
            }
        }.start()
        return true
    }

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (!handlerException(ex) && mDefaultException != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultException!!.uncaughtException(thread, ex)
        } else { //否则自己进行处理
            try {  //Sleep 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
//                Util.writeLog("全局异常",e.getMessage());
                Log.d("2635", "uncaughtException: " + e.message)
            } catch (e: Exception) {
//                Util.writeLog("全局异常",e.getMessage());
                Log.d("2635", "Exception: " + e.message)
            }
            //如果不关闭程序,会导致程序无法启动,需要完全结束进程才能重新启动
            Process.killProcess(Process.myPid())
            System.exit(10)
        }
    }

    companion object {
        //本类实例
        private var mInstance: HIFivesdkCatchException? = null

        //单例模式
        val instance: HIFivesdkCatchException?
            get() {
                if (mInstance == null) {
                    mInstance = HIFivesdkCatchException()
                }
                return mInstance
            }
    }
}