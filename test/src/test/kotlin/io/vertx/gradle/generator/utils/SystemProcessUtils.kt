package io.vertx.gradle.generator.utils

import org.assertj.core.api.Assertions.assertThat
import org.zeroturnaround.exec.ProcessExecutor
import org.zeroturnaround.exec.stream.LogOutputStream
import org.zeroturnaround.process.ProcessUtil
import org.zeroturnaround.process.Processes
import org.zeroturnaround.process.SystemProcess
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.concurrent.thread


class SystemProcessUtils {

  companion object {

    fun String.execute(workingDir: File): Int = ProcessExecutor()
      .commandSplit(this)
      .directory(workingDir)
      .execute().exitValue

    fun String.executeAndGetOutput(workingDir: File): String {
      val result = ProcessExecutor()
        .commandSplit(this)
        .directory(workingDir)
        .readOutput(true)
        .execute()
      assertThat(result.exitValue).isEqualTo(0)
      return result.outputUTF8()
    }

    fun String.executeAndFindFirstInOutput(workingDir: File, timeout: Long = 10, predicate: (line: String?) -> Boolean): Boolean {
      var found = false
      val startedProcess = ProcessExecutor()
        .commandSplit(this)
        .directory(workingDir)
        .readOutput(true)
        .redirectOutput(object : LogOutputStream() {
          override fun processLine(line: String?) {
            if (!found) { // Do not check the condition after first found, dirty sync with the killer thread
              found = predicate(line)
            }
          }
        }).start()

      val systemProcess = Processes.newStandardProcess(startedProcess.process)
      val killer = killProcessWhen(systemProcess) { !found && systemProcess.isAlive }
      try {
        startedProcess.future.get(timeout, TimeUnit.MINUTES)
      } catch (e: TimeoutException) {
        e.printStackTrace()
        killProcess(systemProcess)
      }
      killer.join()
      return found
    }

    private fun killProcess(systemProcess: SystemProcess) = ProcessUtil.destroyGracefullyOrForcefullyAndWait(
      systemProcess,
      30, TimeUnit.SECONDS,
      10, TimeUnit.SECONDS
    )

    private fun killProcessWhen(systemProcess: SystemProcess, predicate: (SystemProcess) -> Boolean) = thread(start = true) {
      while (predicate(systemProcess)) {
      }
      if (systemProcess.isAlive) {
        killProcess(systemProcess)
      }
    }

  }

}
